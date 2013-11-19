package controllers.photos.card;

import core.context.EnvironmentContext;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoInfo;
import core.general.photo.PhotoPreview;
import core.general.user.User;
import core.services.conversion.PhotoPreviewService;
import core.services.entry.EntryMenuService;
import core.services.entry.GenreService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.pageTitle.PageTitleService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.*;

@Controller
@RequestMapping( UrlUtilsServiceImpl.PHOTOS_URL )
public class PhotoCardController {

	private static final String PHOTO_CARD_MODEL = "photoCardModel";

	private static final String VIEW = "photos/card/PhotoCard";

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private PhotoPreviewService photoPreviewService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PageTitleService pageTitleService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private EntryMenuService entryMenuService;

	@ModelAttribute( PHOTO_CARD_MODEL )
	public PhotoCardModel prepareModel() {
		return new PhotoCardModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{photoId}/card/" )
	public String photoCard( final @PathVariable( "photoId" ) String _photoId, final @ModelAttribute( PHOTO_CARD_MODEL ) PhotoCardModel model ) {

		securityService.assertPhotoExists( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );

		model.clear();

		final Photo photo = photoService.load( photoId );

		securityService.assertUserWantSeeNudeContent( EnvironmentContext.getCurrentUser(), photo, urlUtilsService.getPhotoCardLink( photoId ) );

		model.setPhoto( photo );

		final User photoAuthor = userService.load( photo.getUserId() );
		model.setUser( photoAuthor );

		final Genre genre = genreService.load( photo.getGenreId() );
		model.setGenre( genre );

		model.setRootCommentsIds( photoCommentService.loadCommentsWithoutParentIds( photoId ) );

		final User currentUser = EnvironmentContext.getCurrentUser();
		final int loggedUserId = currentUser.getId();

		model.setCommentDelay( photoCommentService.getUserDelayToNextComment( currentUser.getId() ) );
		model.setUserNextCommentTime( photoCommentService.getUserNextCommentTime( currentUser.getId() ) );
		model.setUsedDelayBetweenComments( photoCommentService.getUserDelayBetweenCommentsSec( currentUser ) );

		model.setUserPhotoVotes( photoVotingService.getUserVotesForPhoto( currentUser, photo ) );

		savePhotoPreviewIfNecessary( photo ); // should be before photoInfo loading

		final PhotoInfo photoInfo = photoService.getPhotoInfo( photo, EnvironmentContext.getCurrentUser() );
		model.setPhotoInfo( photoInfo );

		model.setMinCommentLength( configurationService.getInt( ConfigurationKey.COMMENTS_MIN_LENGTH ) );
		model.setMaxCommentLength( configurationService.getInt( ConfigurationKey.COMMENTS_MAX_LENGTH ) );

		model.setVotingUserMinAccessibleMarkForGenre( userRankService.getUserLowestNegativeMarkInGenre( loggedUserId, genre.getId() ) );
		model.setVotingUserMaxAccessibleMarkForGenre( userRankService.getUserHighestPositiveMarkInGenre( loggedUserId, genre.getId() ) );

		model.setCommentingValidationResult( securityService.getPhotoCommentingValidationResult( currentUser, model.getPhoto() ) );
		model.setVotingValidationResult( securityService.getPhotoVotingValidationResult( currentUser, model.getPhoto() ) );

		model.setVotingModel( userRankService.getVotingModel( photo.getUserId(), photo.getGenreId(), currentUser ) );

		model.setPageTitleData( pageTitleService.photoCardTitle( photo, EnvironmentContext.getCurrentUser() ) );

		model.setEntryMenu( entryMenuService.getPhotoMenu( photo, currentUser ) );

		return VIEW;
	}

	private void savePhotoPreviewIfNecessary( final Photo photo ) {
		final User loggedUser = EnvironmentContext.getCurrentUser();
		final int loggedUserId = loggedUser.getId();

		if ( UserUtils.isCurrentUserLoggedUser() ) {
			boolean hasUserAlreadySeenThePhoto = photoPreviewService.hasUserAlreadySeenThisPhoto( photo.getId(), loggedUserId );
			if ( ! hasUserAlreadySeenThePhoto ) {
				final PhotoPreview photoPreview = new PhotoPreview( photo, loggedUser );
				photoPreview.setPreviewTime( dateUtilsService.getCurrentTime() );

				photoPreviewService.save( photoPreview );
			}
		}
	}
}
