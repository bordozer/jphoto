package ui.services.ajax;

import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightUserDTO;
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

public class AjaxServiceImpl implements AjaxService {

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

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
}
