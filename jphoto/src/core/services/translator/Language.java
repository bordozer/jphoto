package core.services.translator;

public enum Language {

	NERD( 1, "Nerd" )
	, RU( 2, "Russian" );

	private final int id;
	private final String name;

	private Language( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
