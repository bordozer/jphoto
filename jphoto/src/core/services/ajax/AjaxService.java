package core.services.ajax;

import core.dtos.AjaxResultDTO;
import core.dtos.ComplaintMessageDTO;

public interface AjaxService {

	String BEAN_NAME = "ajaxService";

	AjaxResultDTO sendComplaintMessageAjax( final ComplaintMessageDTO complaintMessageDTO );
}
