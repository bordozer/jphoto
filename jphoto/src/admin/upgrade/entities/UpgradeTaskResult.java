package admin.upgrade.entities;

import utils.StringUtilities;

public enum UpgradeTaskResult {

	ERROR ( 0, "Error" ), SUCCESSFUL( 1, "Done" );

	private final int id;
	private final String name;

	private UpgradeTaskResult( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static UpgradeTaskResult getById( final int id ) {
		for ( final UpgradeTaskResult upgradeTaskResult : UpgradeTaskResult.values() ) {
			if ( upgradeTaskResult.getId() == id ) {
				return upgradeTaskResult;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal UpgradeTaskResult id: %d", id ) );
	}
}
