package ui.services.ajax;

import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightUserDTO;
import core.enums.FavoriteEntryType;
import core.enums.PrivateMessageType;
import core.general.configuration.ConfigurationKey;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.ComplaintReasonType;
import core.general.message.PrivateMessage;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.log.LogHelper;
import core.services.entry.ActivityStreamService;
import core.services.entry.FavoritesService;
import core.services.entry.PrivateMessageService;
import core.services.notification.NotificationService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.security.RestrictionService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;
import ui.dtos.*;
import utils.NumberUtils;
import utils.StringUtilities;
import utils.UserUtils;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class AjaxServiceImpl implements AjaxService {

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private FavoritesService favoritesService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PrivateMessageService privateMessageService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private RestrictionService restrictionService;

	private final LogHelper log = new LogHelper( AjaxServiceImpl.class );

	@Override
	public AjaxResultDTO sendComplaintMessageAjax( final ComplaintMessageDTO complaintMessageDTO ) {
		final int complaintEntityTypeId = complaintMessageDTO.getComplaintEntityTypeId();
		final int entryId = complaintMessageDTO.getEntryId();
		final int fromUserId = complaintMessageDTO.getFromUserId();
		final int complaintReasonTypeId = complaintMessageDTO.getComplaintReasonTypeId();
		final String customDescription = complaintMessageDTO.getCustomDescription();

		final EntryMenuType entryMenuType = EntryMenuType.getById( complaintEntityTypeId );
		final ComplaintReasonType complaintReasonType = ComplaintReasonType.getById( complaintReasonTypeId );

		final AjaxResultDTO resultDTO = AjaxResultDTO.successResult();
		return resultDTO;
	}

	@Override
	public PhotosightUserDTO getPhotosightUserDTO( final String _photosightUserId ) {
		final int photosightUserId = NumberUtils.convertToInt(_photosightUserId);

		if ( photosightUserId == 0 ) {
			final PhotosightUserDTO photosightUserDTO = new PhotosightUserDTO( 0 );
			photosightUserDTO.setPhotosightUserFound( false );
			return photosightUserDTO;
		}

		final PhotosightUserDTO photosightUserDTO = new PhotosightUserDTO( photosightUserId );

		final String photosightUserName = PhotosightRemoteContentHelper.getPhotosightUserName( photosightUserId );
		final String photosightUserCardUrl = PhotosightRemoteContentHelper.getUserCardUrl( photosightUserId );

		photosightUserDTO.setPhotosightUserName( photosightUserName );
		photosightUserDTO.setPhotosightUserCardUrl( photosightUserCardUrl );

		final boolean photosightUserFound = StringUtils.isNotEmpty( photosightUserName );
		photosightUserDTO.setPhotosightUserFound( photosightUserFound );

		if ( photosightUserFound ) {
			photosightUserDTO.setPhotosightUserPhotosCount( PhotosightContentDataExtractor.extractPhotosightUserPhotosCount( photosightUserId ) );
		}

		final String userLogin = PhotosightImportStrategy.getPhotosightUserLogin( photosightUserId );
		final User user = userService.loadByLogin( userLogin );
		final boolean userExistsInTheSystem = user != null;
		photosightUserDTO.setPhotosightUserExistsInTheSystem( userExistsInTheSystem );

		if ( userExistsInTheSystem ) {
			photosightUserDTO.setUserCardLink( entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ) );
			photosightUserDTO.setPhotosCount( photoService.getPhotoQtyByUser( user.getId() ) );
			photosightUserDTO.setUserPhotosUrl( urlUtilsService.getPhotosByUserLink( user.getId() ) );
			photosightUserDTO.setUserGender( user.getGender() );
			photosightUserDTO.setUserMembershipType( user.getMembershipType() );
		}

		return photosightUserDTO;
	}

	@Override
	public AjaxResultDTO addEntryToFavoritesAjax( final int userId, final int favoriteEntryId, final int entryTypeId ) {
		boolean isSuccessful;
		String message = "";

		// TODO: change a test if exists
		if ( ! UserUtils.isLoggedUser( userId ) ) {
			final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
			ajaxResultDTO.setSuccessful( false );
			ajaxResultDTO.setMessage( translatorService.translate( "You are not logged in", EnvironmentContext.getLanguage() ) );

			return ajaxResultDTO;
		}

		// TODO: make a test
		if ( ! UserUtils.isTheUserThatWhoIsCurrentUser( userId ) ) {
			final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
			ajaxResultDTO.setSuccessful( false );
			ajaxResultDTO.setMessage( translatorService.translate( "Wrong account", EnvironmentContext.getLanguage() ) );

			return ajaxResultDTO;
		}

		if ( favoritesService.isEntryInFavorites( userId, favoriteEntryId, entryTypeId ) ) {
			isSuccessful = false;
			message = translatorService.translate( "Entry is already in your favorites", EnvironmentContext.getLanguage() );
		} else {
			final FavoriteEntryType entryType = FavoriteEntryType.getById( entryTypeId );
			final Date currentTime = dateUtilsService.getCurrentTime();
			isSuccessful = favoritesService.addEntryToFavorites( userId, favoriteEntryId, currentTime, entryType );
			if ( configurationService.getBoolean( ConfigurationKey.SYSTEM_ACTIVITY_LOG_FAVORITE_ACTIONS ) ) {
				activityStreamService.saveFavoriteAction( userId, favoriteEntryId, currentTime, entryType );
			}
		}

		final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
		ajaxResultDTO.setSuccessful( isSuccessful );
		ajaxResultDTO.setMessage( message );

		return ajaxResultDTO;
	}

	@Override
	public AjaxResultDTO removeEntryFromFavoritesAjax( final int userId, final int favoriteEntryId, final int entryTypeId ) {
		boolean isSuccessful;
		String message = "";

		// TODO: change a test if exists
		if ( ! UserUtils.isLoggedUser( userId ) ) {
			final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
			ajaxResultDTO.setSuccessful( false );
			ajaxResultDTO.setMessage( translatorService.translate( "You are not logged in", EnvironmentContext.getLanguage() ) );

			return ajaxResultDTO;
		}

		// TODO: make a test
		if ( !UserUtils.isTheUserThatWhoIsCurrentUser( userId ) ) {
			final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
			ajaxResultDTO.setSuccessful( false );
			ajaxResultDTO.setMessage( translatorService.translate( "Wrong account", EnvironmentContext.getLanguage() ) );

			return ajaxResultDTO;
		}

		if ( !favoritesService.isEntryInFavorites( userId, favoriteEntryId, entryTypeId ) ) {
			isSuccessful = false;
			message = translatorService.translate( "Entry is NOT in your favorites", EnvironmentContext.getLanguage() );
		} else {
			isSuccessful = favoritesService.removeEntryFromFavorites( userId, favoriteEntryId, FavoriteEntryType.getById( entryTypeId ) );
		}

		final AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
		ajaxResultDTO.setSuccessful( isSuccessful );
		ajaxResultDTO.setMessage( message );

		return ajaxResultDTO;
	}

	@Override
	public AjaxResultDTO sendPrivateMessageAjax( final PrivateMessageSendingDTO messageDTO ) {

		final Language language = EnvironmentContext.getLanguage();

		if ( !UserUtils.isCurrentUserLoggedUser() ) {
			return AjaxResultDTO.failResult( translatorService.translate( "You are not logged in", language ) );
		}

		final int fromUserId = messageDTO.getFromUserId();
		final User fromUser = userService.load( fromUserId );
		if ( fromUser == null ) {
			return AjaxResultDTO.failResult( translatorService.translate( "Member FROM not found", language ) );
		}

		if ( !UserUtils.isTheUserThatWhoIsCurrentUser( fromUser ) ) {
			return AjaxResultDTO.failResult( translatorService.translate( "Attempt to send the message from another account. It seems you have changed your account after loading of this page, haven't you?", language ) );
		}

		final int toUserId = messageDTO.getToUserId();
		final User toUser = userService.load( toUserId );
		if ( toUser == null ) {
			return AjaxResultDTO.failResult( translatorService.translate( "Member you are trying to send message not found", language ) );
		}

		if ( UserUtils.isUsersEqual( fromUser, toUser ) ) {
			return AjaxResultDTO.failResult( translatorService.translate( "You can not send message to yourself", language ) );
		}

		final String privateMessageText = messageDTO.getPrivateMessageText();
		if ( StringUtils.isEmpty( privateMessageText ) ) {
			return AjaxResultDTO.failResult( translatorService.translate( "Message text should not be empty", language ) );
		}

		final PrivateMessage privateMessageOut = new PrivateMessage();
		privateMessageOut.setFromUser( fromUser );
		privateMessageOut.setToUser( toUser );
		privateMessageOut.setMessageText( privateMessageText );
		privateMessageOut.setPrivateMessageType( PrivateMessageType.USER_PRIVATE_MESSAGE_OUT );
		privateMessageOut.setCreationTime( dateUtilsService.getCurrentTime() );

		final boolean isSuccessfulOut = privateMessageService.save( privateMessageOut );

		final AjaxResultDTO resultDTO = new AjaxResultDTO();
		resultDTO.setSuccessful( isSuccessfulOut );

		if ( !isSuccessfulOut ) {
			resultDTO.setMessage( translatorService.translate( "Error saving OUT message to DB", language ) );

			return resultDTO;
		}

		final PrivateMessage privateMessageIn = new PrivateMessage( privateMessageOut );
		privateMessageIn.setPrivateMessageType( PrivateMessageType.USER_PRIVATE_MESSAGE_IN );
		privateMessageIn.setOutPrivateMessageId( privateMessageOut.getId() );

		final boolean isSuccessfulIn = privateMessageService.save( privateMessageIn );

		resultDTO.setSuccessful( isSuccessfulIn );

		if ( !isSuccessfulIn ) {
			resultDTO.setMessage( translatorService.translate( "Error saving IN message to DB", language ) );
			privateMessageService.delete( privateMessageOut.getId() );
		}

		if ( resultDTO.isSuccessful() ) {
			new Thread( new Runnable() {
				@Override
				public void run() {
					notificationService.newPrivateMessage( privateMessageOut );
				}
			} ).start();
		}

		return resultDTO;
	}

	@Override
	public CommentDTO markCommentAsDeletedAjax( final int userId, final int commentId ) { // TODO: move to AJAX service

		if ( ! securityService.userCanDeletePhotoComment( userId, commentId ) ) {
			final CommentDTO commentDTO = new CommentDTO( commentId );
			commentDTO.setErrorMessage( translatorService.translate( "You do not have permission to delete this comment", EnvironmentContext.getLanguage() ) );

			return commentDTO;
		}

		final User userWhoIsDeletingComment = EnvironmentContext.getCurrentUser();

		final PhotoComment comment = photoCommentService.load( commentId );

		if ( comment.isCommentDeleted() ) {
			final CommentDTO commentDTO = new CommentDTO( commentId );
			commentDTO.setErrorMessage( translatorService.translate( "The comment has already been deleted", EnvironmentContext.getLanguage() ) );

			return commentDTO;
		}

		final PhotoComment deletedComment = new PhotoComment( comment );
		deletedComment.setId( commentId );
		deletedComment.setCommentDeleted( true );

		final Photo photo = photoService.load( comment.getPhotoId() );

		String userRole = "";
		if ( securityService.isSuperAdminUser( userWhoIsDeletingComment.getId() ) ) {
			userRole = "admin";
		} else if( UserUtils.isUserOwnThePhoto( userWhoIsDeletingComment, photo ) ) {
			userRole = "photo author";
		} else {
			userRole = "comment author";
		}

		final String commentText = String.format( "%s ( %s ) deleted this comment: %s"
			, userWhoIsDeletingComment.getNameEscaped(), userRole, dateUtilsService.formatDateTime( dateUtilsService.getCurrentTime() ) );
		deletedComment.setCommentText( commentText );

		photoCommentService.save( deletedComment );

		return new CommentDTO( commentId, commentText );
	}

	@Override
	public List<UserPickerDTO> userLinkAjax( final String searchString ) {

		final List<User> users = userService.searchByPartOfName( searchString );

		final List<UserPickerDTO> userPickerDTOs = newArrayList();

		if ( users.size() == 0 ) {
			return newArrayList();
		}

		for ( final User user : users ) {
			final UserPickerDTO userPickerDTO = new UserPickerDTO();

			userPickerDTO.setUserId( String.valueOf( user.getId() ) );
			userPickerDTO.setUserName( user.getName() );
			userPickerDTO.setUserNameEscaped( StringUtilities.escapeHtml( user.getName() ) );
			userPickerDTO.setUserCardLink( entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ) );
			userPickerDTO.setUserAvatarUrl( userPhotoFilePathUtilsService.getUserAvatarFileUrl( user.getId() ) );
			userPickerDTO.setUserGender( translatorService.translate( user.getGender().getName(), EnvironmentContext.getLanguage() ) );

			userPickerDTOs.add( userPickerDTO );
		}

		return userPickerDTOs;
	}

	@Override
	public long getUserDelayToNextCommentAjax( final int userId ) {
		return photoCommentService.getUserDelayToNextComment( userId );
	}

	@Override
	public CommentDTO getCommentDTOAjax( final int commentId ) {
		final PhotoComment photoComment = photoCommentService.load( commentId );
		return new CommentDTO( photoComment );
	}

	@Override
	public boolean isUserCanCommentPhotosAjax( final int userId ) {
		return photoCommentService.isUserCanCommentPhotos( userId );
	}

	@Override
	public boolean isEntryInFavoritesAjax( final int userWhoIsAddingToFavorites, final int beingAddedEntryId, final int entryTypeId ) {
		return favoritesService.isEntryInFavorites( userWhoIsAddingToFavorites, beingAddedEntryId, entryTypeId );
	}

	@Override
	public void lockUser( final int userId, final String timeFrom, final String timeTo ) {
//		final Date dateFrom = dateUtilsService.parseDate( timeFrom );
		//		restrictionService.lockUser( userService.load( userId ), timeFrom, timeTo );
		log.debug( String.format( "userId: %d, timeFrom: '%s', timeTo: '%s'", userId, timeFrom, timeTo ) );
	}

	@Override
	public void lockPhoto( final int photoId, final String timeFrom, final String timeTo ) {
//		final Date dateFrom = dateUtilsService.parseDate( timeFrom );
//		restrictionService.lockPhotoToBePhotoOfTheDay( photoService.load( photoId ), timeFrom, timeTo );
		log.debug( String.format( "photoId: %d, timeFrom: '%s', timeTo: '%s'", photoId, timeFrom, timeTo ) );
	}
}
