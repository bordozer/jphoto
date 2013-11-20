package core.services.ajax;

import core.dtos.AjaxResultDTO;
import core.dtos.ComplaintMessageDTO;
import core.general.menus.comment.ComplaintReasonType;
import core.general.menus.EntryMenuType;

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
}
