package rest.photo.appraisal;

import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.ValidationResult;
import core.general.user.User;
import core.general.user.UserPhotoVote;
import core.services.entry.VotingCategoryService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import rest.exceptions.SaveToDBRuntimeException;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "photos/{photoId}" )
@Controller
public class PhotoAppraisalController {

	public static final int DEFAULT_SELECTED_MARK = 1;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private VotingCategoryService votingCategoryService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PhotoAppraisalFormValidator photoAppraisalFormValidator;

	@RequestMapping( method = RequestMethod.GET, value = "/appraisal/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoAppraisalDTO userCardVotingAreas( final @PathVariable( "photoId" ) int photoId ) {
		return getPhotoAppraisalDTO( photoService.load( photoId ), EnvironmentContext.getCurrentUser() );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/appraisal/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoAppraisalDTO createNote( @RequestBody final PhotoAppraisalDTO appraisalDTO, final @PathVariable( "photoId" ) int photoId ) {

		appraisalDTO.setCurrentUserId( EnvironmentContext.getCurrentUserId() );

		ValidationHelper.validate( appraisalDTO, photoAppraisalFormValidator );

		final Photo photo = photoService.load( photoId );
		final User user = userService.load( appraisalDTO.getUserId() );

		savePhotoAppraisal( user, photo, appraisalDTO );

		final PhotoAppraisalDTO photoAppraisalDTO = getPhotoAppraisalDTO( photo, user );
		photoAppraisalDTO.setAppraisalSaveCallbackMessage( translatorService.translate( "Photo appraisal: Your appraisal has been saved successfully", getLanguage() ) );

		return photoAppraisalDTO;
	}

	private void savePhotoAppraisal( final User user, final Photo photo, final PhotoAppraisalDTO appraisalDTO ) {

		final Date currentTime = dateUtilsService.getCurrentTime();

		final List<AppraisalSection> sections = appraisalDTO.getPhotoAppraisalForm().getAppraisalSections();

		final List<AppraisalSection> notZeroSections = newArrayList( sections );
		CollectionUtils.filter( notZeroSections, new Predicate<AppraisalSection>() {
			@Override
			public boolean evaluate( final AppraisalSection appraisalSection ) {
				return appraisalSection.getSelectedCategoryId() != 0 && appraisalSection.getSelectedMark() != 0;
			}
		} );

		final List<UserPhotoVote> appraisals = newArrayList();
		for ( final AppraisalSection section : notZeroSections ) {

			final int selectedCategoryId = section.getSelectedCategoryId();

			final PhotoVotingCategory category = votingCategoryService.load( selectedCategoryId );

			final UserPhotoVote photoVote = new UserPhotoVote( user, photo, category );
			photoVote.setMark( section.getSelectedMark() );
			photoVote.setMaxAccessibleMark( userRankService.getUserHighestPositiveMarkInGenre( user.getId(), photo.getGenreId() ) );
			photoVote.setVotingTime( currentTime );

			appraisals.add( photoVote );
		}

		if ( ! photoVotingService.saveUserPhotoVoting( user, photo, currentTime, appraisals ) ) {
			throw new SaveToDBRuntimeException( translatorService.translate( "", getLanguage() ) );
		}
	}

	@ExceptionHandler( ValidationException.class )
	@ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY )
	@ResponseBody
	public List<FieldError> processValidationError( final ValidationException validationException ) {
		return validationException.getBindingResult().getFieldErrors();
	}

	@ExceptionHandler( SaveToDBRuntimeException.class )
	@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
	@ResponseBody
	public List<FieldError> processSaveToDBError( final SaveToDBRuntimeException exception ) {
		return newArrayList( new FieldError( "SaveToDBRuntimeException", "", exception.getMessage() ) );
	}

	private PhotoAppraisalDTO getPhotoAppraisalDTO( final Photo photo, final User user ) {

		final PhotoAppraisalDTO photoAppraisalDTO = new PhotoAppraisalDTO();

		photoAppraisalDTO.setUserId( user.getId() );
		photoAppraisalDTO.setPhotoId( photo.getId() );

		photoAppraisalDTO.setAppraisalBlockTitle( translatorService.translate( "Photo appraisal: Photo appraisal title", getLanguage() ) );

		final ValidationResult validationResult = securityService.validateUserCanVoteForPhoto( user, photo, getLanguage() );
		photoAppraisalDTO.setUserCanAppraiseThePhoto( validationResult.isValidationPassed() );
		if ( validationResult.isValidationFailed() ) {
			photoAppraisalDTO.setUserCanNotAppraiseThePhotoText( translatorService.translate( "Photo appraisal: You can not appraise the photo", getLanguage() ) );
			photoAppraisalDTO.setUserCanNotAppraiseThePhotoReason( validationResult.getValidationMessage() );
			return photoAppraisalDTO;
		}

		final boolean hasUserVotedForPhoto = photoVotingService.isUserVotedForPhoto( user, photo );
		photoAppraisalDTO.setUserHasAlreadyAppraisedPhoto( hasUserVotedForPhoto );

		if ( hasUserVotedForPhoto ) {

			Date votingTime = dateUtilsService.getCurrentTime(); // TODO: this initialization is spare

			final List<PhotoAppraisalResult> photoAppraisalResults = newArrayList();
			final List<UserPhotoVote> userVotesForPhoto = photoVotingService.getUserVotesForPhoto( user, photo );
			for ( final UserPhotoVote userPhotoVote : userVotesForPhoto ) {

				votingTime = userPhotoVote.getVotingTime();

				final PhotoVotingCategory photoVotingCategory = userPhotoVote.getPhotoVotingCategory();
				final int mark = userPhotoVote.getMark();
				final int maxAccessibleMark = userPhotoVote.getMaxAccessibleMark();
				final String votingCategoryNameTranslated = translatorService.translatePhotoVotingCategory( photoVotingCategory, getLanguage() );

				final PhotoAppraisalResult appraisalResult = new PhotoAppraisalResult();
				appraisalResult.setAppraisalCategoryNameTranslated( votingCategoryNameTranslated );
				appraisalResult.setMark( formatMark( mark ) );
				appraisalResult.setMaxAccessibleMark( formatMark( maxAccessibleMark ) );

				photoAppraisalResults.add( appraisalResult );
			}
			photoAppraisalDTO.setPhotoAppraisalResults( photoAppraisalResults );

			photoAppraisalDTO.setAppraisalBlockTitle( translatorService.translate( "Photo appraisal: done at $1", getLanguage(), dateUtilsService.formatDateTimeShort( votingTime ) ) );

			return photoAppraisalDTO;
		}

		photoAppraisalDTO.setPhotoAppraisalForm( getPhotoAppraisalForm( photo, user ) );

		return photoAppraisalDTO;
	}

	private String formatMark( final int mark ) {
		return mark > 0 ? String.format( "+%d", mark ) : String.format( "%d", mark );
	}

	private PhotoAppraisalForm getPhotoAppraisalForm( final Photo photo, final User user ) {

		final Language language = getLanguage();

		final PhotoAppraisalForm form = new PhotoAppraisalForm();

		final int categoriesCount = configurationService.getInt( ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT );
		final List<AppraisalSection> appraisalSections = newArrayList();
		for ( int i = 0; i < categoriesCount; i++ ) {

			final List<PhotoAppraisalCategory> accessibleAppraisalCategories = getAccessibleAppraisalCategories( photo.getGenreId(), language );

			final AppraisalSection section = new AppraisalSection( i, accessibleAppraisalCategories, getAccessibleMarks( photo, user ) );
			section.setSelectedCategoryId( accessibleAppraisalCategories.get( i + 1 ).getId() );
			section.setSelectedMark( DEFAULT_SELECTED_MARK );

			appraisalSections.add( section );
		}
		form.setAppraisalSections( appraisalSections );

		final int userHighestPositiveMarkInGenre = userRankService.getUserHighestPositiveMarkInGenre( user.getId(), photo.getGenreId() );
		form.setUserHighestPositiveMarkInGenre( userHighestPositiveMarkInGenre );

		form.setCustomButtonText( translatorService.translate( "Photo appraisal: Custom mark set", language ) );
		form.setCustomButtonTitle( translatorService.translate( "Photo appraisal: Appraise the photo with custom marks", language ) );

		final int goodButtonMark = userHighestPositiveMarkInGenre == 2 ? 1 : 2; // TODO: '2' is hardcoded!
		form.setGoodButtonMark( goodButtonMark );
		form.setGoodButtonText( translatorService.translate( "Photo appraisal: Good ( +$1 )", getLanguage(), String.valueOf( goodButtonMark ) ) );
		form.setGoodButtonTitle( translatorService.translate( "Photo appraisal: Button Good title ( +$1 )", language, String.valueOf( goodButtonMark ) ) );

		form.setExcellentButtonText( translatorService.translate( "Photo appraisal: Excellent ( +$1 )", getLanguage(), String.valueOf( userHighestPositiveMarkInGenre ) ) );
		form.setExcellentButtonTitle( translatorService.translate( "Photo appraisal: Appraise the photo with maximum accessible for you marks ( +$1 )", language, String.valueOf( userHighestPositiveMarkInGenre ) ) );

		return form;
	}

	private List<PhotoAppraisalCategory> getAccessibleAppraisalCategories( final int genreId, final Language language ) {

		final List<PhotoVotingCategory> photoVotingCategories = votingCategoryService.getGenreVotingCategories( genreId ).getVotingCategories();

		final List<PhotoAppraisalCategory> accessibleAppraisalCategories = newArrayList();
		final PhotoAppraisalCategory noCategory = new PhotoAppraisalCategory();
		noCategory.setId( 0 );
		noCategory.setNameTranslated( "--------" );
		accessibleAppraisalCategories.add( noCategory );

		for ( final PhotoVotingCategory photoVotingCategory : photoVotingCategories ) {
			final PhotoAppraisalCategory category = new PhotoAppraisalCategory();
			category.setId( photoVotingCategory.getId() );
			category.setNameTranslated( translatorService.translatePhotoVotingCategory( photoVotingCategory, language ) );

			accessibleAppraisalCategories.add( category );
		}

		return accessibleAppraisalCategories;
	}

	private List<Mark> getAccessibleMarks( final Photo photo, final User user ) {
		final int userHighestPositiveMarkInGenre = userRankService.getUserHighestPositiveMarkInGenre( user.getId(), photo.getGenreId() );
		final int userLowestNegativeMarkInGenre = userRankService.getUserLowestNegativeMarkInGenre( user.getId(), photo.getGenreId() );

		final List<Mark> accessibleMarks = newArrayList();
		for ( int i = userHighestPositiveMarkInGenre; i >= userLowestNegativeMarkInGenre  ; i-- ) {

			if ( i == 0 ) {
				accessibleMarks.add( new Mark( i, "--" ) );
				continue;
			}

			accessibleMarks.add( new Mark( i, ( i > 0 ? String.format( "+%d", i ) : String.format( "%d", i ) ) ) );
		}
		return accessibleMarks;
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
