package ui.controllers.photos.groupoperations;

public class GroupOperationResult {

	private final GroupOperationResultType groupOperationResultType;
	private final String message;

	private GroupOperationResult( final GroupOperationResultType groupOperationResultType, final String message ) {
		this.groupOperationResultType = groupOperationResultType;
		this.message = message;
	}

	public static GroupOperationResult successful( final String message ) {
		return new GroupOperationResult( GroupOperationResultType.SUCCESSFUL, message );
	}

	public static GroupOperationResult warning( final String message ) {
		return new GroupOperationResult( GroupOperationResultType.WARNING, message );
	}

	public static GroupOperationResult error( final String message ) {
		return new GroupOperationResult( GroupOperationResultType.ERROR, message );
	}

	public static GroupOperationResult skipped( final String message ) {
		return new GroupOperationResult( GroupOperationResultType.SKIPPED, message );
	}

	public GroupOperationResultType getGroupOperationResultType() {
		return groupOperationResultType;
	}

	public String getMessage() {
		return message;
	}
}
