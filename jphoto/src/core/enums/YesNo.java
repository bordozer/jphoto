package core.enums;

import core.interfaces.IdentifiableNameable;

public enum YesNo implements IdentifiableNameable {

	YES( 1, "YesNo: yes" )
	, NO( -1, "YesNo: no" );

	private final int id;
	private final String name;

	private YesNo( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static YesNo getById( final int id ) {
		for ( final YesNo upgradeTaskResult : YesNo.values() ) {
			if ( upgradeTaskResult.getId() == id ) {
				return upgradeTaskResult;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal YesNo id: %d", id ) );
	}
}
