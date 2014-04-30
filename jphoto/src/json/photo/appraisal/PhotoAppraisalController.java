package json.photo.appraisal;

import json.exceptions.SaveToDBRuntimeException;
import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserPhotoVote;
import core.services.entry.VotingCategoryService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import java.util.ArrayList;
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
	private PhotoAppraisalFormValidator photoAppraisalFormValidator;

	@RequestMapping( method = RequestMethod.GET, value = "/appraisal/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoAppraisalDTO userCardVotingAreas( final @PathVariable( "photoId" ) int photoId ) {
		return getPhotoAppraisalDTO( photoService.load( photoId ), EnvironmentContext.getCurrentUser() );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/appraisal/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoAppraisalDTO createNote( @RequestBody final PhotoAppraisalDTO appraisalDTO, final @PathVariable( "photoId" ) int photoId ) {

		ValidationHelper.validate( appraisalDTO, photoAppraisalFormValidator );

		final Photo photo = photoService.load( photoId );
		final User user = userService.load( appraisalDTO.getUserId() );

//		savePhotoAppraisal( user, photo, appraisalDTO );

//		return getPhotoAppraisalDTO( photo, user );
		throw new SaveToDBRuntimeException( translatorService.translate( "Error saving data", EnvironmentContext.getLanguage() ) );
	}

	private void savePhotoAppraisal( final User user, final Photo photo, final PhotoAppraisalDTO appraisalDTO ) {

		final List<AppraisalSection> sections = appraisalDTO.getPhotoAppraisalForm().getAppraisalSections();

		final List<UserPhotoVote> appraisals = newArrayList();
		for ( final AppraisalSection section : sections ) {

			final int selectedCategoryId = section.getSelectedCategoryId();

			final PhotoVotingCategory category = votingCategoryService.load( selectedCategoryId );
			appraisals.add( new UserPhotoVote( user, photo, category ) );
		}

		if ( ! photoVotingService.saveUserPhotoVoting( user, photo, dateUtilsService.getCurrentTime(), appraisals ) ) {
			throw new SaveToDBRuntimeException( translatorService.translate( "", EnvironmentContext.getLanguage() ) );
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

		final boolean hasUserVotedForPhoto = photoVotingService.isUserVotedForPhoto( user, photo );
		photoAppraisalDTO.setUserHasAlreadyAppraisedPhoto( hasUserVotedForPhoto );

		if ( ! hasUserVotedForPhoto ) {
			photoAppraisalDTO.setPhotoAppraisalForm( getPhotoAppraisalForm( photo, user ) );
		}

		return photoAppraisalDTO;
	}

	private PhotoAppraisalForm getPhotoAppraisalForm( final Photo photo, final User user ) {

		final Language language = EnvironmentContext.getLanguage();

		final PhotoAppraisalForm form = new PhotoAppraisalForm();

		final int categoriesCount = configurationService.getInt( ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT );
		final List<AppraisalSection> appraisalSections = newArrayList();
		for ( int i = 0; i < categoriesCount; i++ ) {
			final List<PhotoAppraisalCategory> categories = getAccessibleAppraisalCategories( i, photo.getGenreId(), language );
			final AppraisalSection section = new AppraisalSection( i, categories, getAccessibleMarks( photo, user ) );
			section.setSelectedCategoryId( categories.get( i + 1 ).getId() );
			section.setSelectedMark( DEFAULT_SELECTED_MARK );

			appraisalSections.add( section );
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

		return accessibleAppraisalCategories;
	}

	private List<Mark> getAccessibleMarks( final Photo photo, final User user ) {
		final int userHighestPositiveMarkInGenre = userRankService.getUserHighestPositiveMarkInGenre( user.getId(), photo.getGenreId() );
		final int userLowestNegativeMarkInGenre = userRankService.getUserLowestNegativeMarkInGenre( user.getId(), photo.getGenreId() );

		final List<Mark> accessibleMarks = newArrayList();
		for ( int i = userHighestPositiveMarkInGenre; i >= userLowestNegativeMarkInGenre  ; i-- ) {

			if ( i == 0 ) {
				accessibleMarks.add( new Mark( i, "--" ) );
			}

			accessibleMarks.add( new Mark( i, ( i > 0 ? String.format( "+%d", i ) : String.format( "%d", i ) ) ) );
		}
		return accessibleMarks;
	}
}
