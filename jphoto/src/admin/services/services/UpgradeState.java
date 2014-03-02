package admin.services.services;

import utils.StringUtilities;

public enum UpgradeState {
	IN_PROGRESS( 1, "In progress")
	, STOPPED( 1, "Finished")
	, ERROR( 1, "Error")
	;

	private final int id;
	private final String name;

	private UpgradeState( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( name ); // TODO: translate
	}

	public static UpgradeState getById( final int id ) {
		for ( final UpgradeState upgradeTaskResult : UpgradeState.values() ) {
			if ( upgradeTaskResult.getId() == id ) {
				return upgradeTaskResult;
			}
		}
		throw new IllegalArgumentException( String.format( "Illegal UpgradeState id: %d", id ) );
	}
}
