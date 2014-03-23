package core.services.ajax;

import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightUserDTO;
import core.dtos.AjaxResultDTO;
import core.dtos.ComplaintMessageDTO;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.ComplaintReasonType;
import org.apache.commons.lang.StringUtils;
import utils.StringUtilities;

public class AjaxServiceImpl implements AjaxService {

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
	public PhotosightUserDTO getPhotosightUserDTO( final int photosightUserId ) {
		final PhotosightUserDTO photosightUserDTO = new PhotosightUserDTO( photosightUserId );

		final String photosightUserName = PhotosightRemoteContentHelper.getPhotosightUserName( photosightUserId );
		final String photosightUserCardUrl = PhotosightRemoteContentHelper.getUserCardUrl( photosightUserId );

		photosightUserDTO.setPhotosightUserName( photosightUserName );
		photosightUserDTO.setPhotosightUserCardUrl( photosightUserCardUrl );
		photosightUserDTO.setPhotosightUserFound( StringUtils.isNotEmpty( photosightUserName ) );

		return photosightUserDTO;
	}
}
