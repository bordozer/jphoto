package core.enums;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum YesNo {

	YES( 1, "yes" )
	, NO( -1, "no" )
	;

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

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( TranslatorUtils.translate( name ) );
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
