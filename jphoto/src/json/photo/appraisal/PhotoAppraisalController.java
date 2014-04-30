package json.photo.appraisal;

import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.services.entry.VotingCategoryService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "photos/{photoId}" )
@Controller
public class PhotoAppraisalController {

	public static final int DEFAULT_SELECTED_MARK = 1;
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
	private PhotoAppraisalFormValidator photoAppraisalFormValidator;

	@RequestMapping( method = RequestMethod.GET, value = "/appraisal/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoAppraisalDTO userCardVotingAreas( final @PathVariable( "photoId" ) int photoId ) {
		return getPhotoAppraisalDTO( photoId );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/appraisal/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoAppraisalDTO createNote( @RequestBody final PhotoAppraisalDTO appraisalDTO, final @PathVariable( "photoId" ) int photoId ) {
		ValidationHelper.validate( appraisalDTO, photoAppraisalFormValidator );

		// TODO: save results

		final PhotoAppraisalDTO photoAppraisalDTO = getPhotoAppraisalDTO( photoId );
		photoAppraisalDTO.setUserHasAlreadyAppraisedPhoto( true ); // TODO: temporary hack

		return photoAppraisalDTO;
	}

	@ExceptionHandler( ValidationException.class )
	@ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY )
	@ResponseBody
	public List<FieldError> processValidationError( final ValidationException validationException ) {
		return validationException.getBindingResult().getFieldErrors();
	}

	private PhotoAppraisalDTO getPhotoAppraisalDTO( final int photoId ) {
		final User currentUser = EnvironmentContext.getCurrentUser();
		final Photo photo = photoService.load( photoId );

		final PhotoAppraisalDTO photoAppraisalDTO = new PhotoAppraisalDTO();
		photoAppraisalDTO.setPhotoId( photoId );

		final boolean hasUserVotedForPhoto = photoVotingService.isUserVotedForPhoto( currentUser, photo );
		photoAppraisalDTO.setUserHasAlreadyAppraisedPhoto( hasUserVotedForPhoto );

		if ( ! hasUserVotedForPhoto ) {
			photoAppraisalDTO.setPhotoAppraisalForm( getPhotoAppraisalForm( photo, currentUser ) );
		}

		return photoAppraisalDTO;
	}

	private PhotoAppraisalForm getPhotoAppraisalForm( final Photo photo, final User user ) {

		final Language language = EnvironmentContext.getLanguage();

		final PhotoAppraisalForm form = new PhotoAppraisalForm();

		final int categoriesCount = configurationService.getInt( ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT );
		final List<AppraisalSection> appraisalSections = newArrayList();
		for ( int i = 0; i < categoriesCount; i++ ) {
			appraisalSections.add( new AppraisalSection( i, getAccessibleAppraisalCategories( i, photo.getGenreId(), language ), getAccessibleMarks( photo, user ) ) );
		}
		form.setAppraisalSections( appraisalSections );

		form.setAppraisalText( translatorService.translate( "Appraise the photo", language ) );
		form.setAppraisalTitle( translatorService.translate( "Appraise the photo with custom marks", language ) );

		final int userHighestPositiveMarkInGenre = userRankService.getUserHighestPositiveMarkInGenre( user.getId(), photo.getGenreId() );
		form.setMaxAppraisalText( String.format( "+%1$d +%1$d +%1$d", userHighestPositiveMarkInGenre ) );
		form.setMaxAppraisalTitle( translatorService.translate( "Appraise the photo with maximum accessible for you marks", language ) );

		return form;
	}

	private List<PhotoAppraisalCategory> getAccessibleAppraisalCategories( final int count, final int genreId, final Language language ) {

		final List<PhotoVotingCategory> photoVotingCategories = votingCategoryService.getGenreVotingCategories( genreId ).getVotingCategories();

		final List<PhotoAppraisalCategory> accessibleAppraisalCategories = newArrayList();
		final PhotoAppraisalCategory noCategory = new PhotoAppraisalCategory();
		noCategory.setId( -1 );
		noCategory.setNameTranslated( "--------" );
		accessibleAppraisalCategories.add( noCategory );

		for ( final PhotoVotingCategory photoVotingCategory : photoVotingCategories ) {
			final PhotoAppraisalCategory category = new PhotoAppraisalCategory();
			category.setId( photoVotingCategory.getId() );
			category.setNameTranslated( translatorService.translatePhotoVotingCategory( photoVotingCategory, language ) );

			accessibleAppraisalCategories.add( category );
		}

		accessibleAppraisalCategories.get( count + 1 ).setSelected( true );

		return accessibleAppraisalCategories;
	}

	private List<Mark> getAccessibleMarks( final Photo photo, final User user ) {
		final int userHighestPositiveMarkInGenre = userRankService.getUserHighestPositiveMarkInGenre( user.getId(), photo.getGenreId() );
		final int userLowestNegativeMarkInGenre = userRankService.getUserLowestNegativeMarkInGenre( user.getId(), photo.getGenreId() );

		final List<Mark> accessibleMarks = newArrayList();
//		for ( int i = userLowestNegativeMarkInGenre; i <= userHighestPositiveMarkInGenre; i++ ) {
		for ( int i = userHighestPositiveMarkInGenre; i >= userLowestNegativeMarkInGenre  ; i-- ) {

			if ( i == 0 ) {
				continue;
			}

			final Mark mark = new Mark( i, ( i > 0 ? String.format( "+%d", i ) : String.format( "%d", i ) ) );
			mark.setSelected( i == DEFAULT_SELECTED_MARK );

			accessibleMarks.add( mark );
		}
		return accessibleMarks;
	}
}
