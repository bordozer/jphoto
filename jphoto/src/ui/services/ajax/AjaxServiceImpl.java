package ui.services.ajax;

import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightUserDTO;
import core.enums.FavoriteEntryType;
import core.general.configuration.ConfigurationKey;
import core.services.entry.ActivityStreamService;
import core.services.entry.FavoritesService;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import ui.context.EnvironmentContext;
import ui.dtos.AjaxResultDTO;
import ui.dtos.ComplaintMessageDTO;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.ComplaintReasonType;
import core.general.user.User;
import core.services.photo.PhotoService;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import utils.NumberUtils;
import utils.UserUtils;

import java.util.Date;

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
}
