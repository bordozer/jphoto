package ui.dtos;

public class ComplaintMessageDTO {

	private int complaintEntityTypeId;
	private int entryId;
	private int fromUserId;
	private int complaintReasonTypeId;
	private String customDescription;

	public int getComplaintEntityTypeId() {
		return complaintEntityTypeId;
	}

	public void setComplaintEntityTypeId( final int complaintEntityTypeId ) {
		this.complaintEntityTypeId = complaintEntityTypeId;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId( final int entryId ) {
		this.entryId = entryId;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId( final int fromUserId ) {
		this.fromUserId = fromUserId;
	}

	public int getComplaintReasonTypeId() {
		return complaintReasonTypeId;
	}

	public void setComplaintReasonTypeId( final int complaintReasonTypeId ) {
		this.complaintReasonTypeId = complaintReasonTypeId;
	}

	public String getCustomDescription() {
		return customDescription;
	}

	public void setCustomDescription( final String customDescription ) {
		this.customDescription = customDescription;
	}
}
