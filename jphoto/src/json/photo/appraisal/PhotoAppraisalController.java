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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RequestMapping( "photos/{photoId}" )
@Controller
public class PhotoAppraisalController {

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

	@RequestMapping( method = RequestMethod.GET, value = "/appraisal/", produces = "application/json" )
	@ResponseBody
	public PhotoAppraisalDTO userCardVotingAreas( final @PathVariable( "photoId" ) int photoId ) {

		final User currentUser = EnvironmentContext.getCurrentUser();
		final Photo photo = photoService.load( photoId );

		final PhotoAppraisalDTO photoAppraisalDTO = new PhotoAppraisalDTO( photoId );

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
			appraisalSections.add( new AppraisalSection( i, getAccessibleAppraisalCategories( language, photo.getGenreId() ), getAccessibleMarks( photo, user ) ) );
		}
		form.setAppraisalSections( appraisalSections );

		return form;
	}

	private List<PhotoAppraisalCategory> getAccessibleAppraisalCategories( final Language language, final int genreId ) {

		final List<PhotoVotingCategory> photoVotingCategories = votingCategoryService.getGenreVotingCategories( genreId ).getVotingCategories();

		final List<PhotoAppraisalCategory> photoAppraisalCategories = newArrayList();

		for ( final PhotoVotingCategory photoVotingCategory : photoVotingCategories ) {
			final PhotoAppraisalCategory category = new PhotoAppraisalCategory();
			category.setId( photoVotingCategory.getId() );
			category.setNameTranslated( translatorService.translatePhotoVotingCategory( photoVotingCategory, language ) );
			photoAppraisalCategories.add( category );
		}

		return photoAppraisalCategories;
	}

	private List<Mark> getAccessibleMarks( final Photo photo, final User user ) {
		final int userHighestPositiveMarkInGenre = userRankService.getUserHighestPositiveMarkInGenre( user.getId(), photo.getGenreId() );
		final int userLowestNegativeMarkInGenre = userRankService.getUserLowestNegativeMarkInGenre( user.getId(), photo.getGenreId() );

		final List<Mark> accessibleMarks = newArrayList();
		for ( int i = userHighestPositiveMarkInGenre; i >= userLowestNegativeMarkInGenre  ; i-- ) {
			accessibleMarks.add( new Mark( i, ( i > 0 ? String.format( "+%d", i ) : String.format( "%d", i ) ) ) );
		}
		return accessibleMarks;
	}
}
