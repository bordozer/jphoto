package core.exceptions;

public class RestrictionException extends BaseRuntimeException {

	private final int restrictionHistoryEntryId;

	public RestrictionException( final int restrictionHistoryEntryId, final String message ) {
		super( message );
		this.restrictionHistoryEntryId = restrictionHistoryEntryId;
	}

	public int getRestrictionHistoryEntryId() {
		return restrictionHistoryEntryId;
	}
}
