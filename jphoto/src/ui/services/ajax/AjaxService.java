package ui.services.ajax;

import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightUserDTO;
import ui.dtos.AjaxResultDTO;
import ui.dtos.ComplaintMessageDTO;

public interface AjaxService {

	String BEAN_NAME = "ajaxService";

	AjaxResultDTO sendComplaintMessageAjax( final ComplaintMessageDTO complaintMessageDTO );

	PhotosightUserDTO getPhotosightUserDTO( final String _photosightUserId );
}
