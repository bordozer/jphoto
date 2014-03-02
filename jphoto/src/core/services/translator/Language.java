package core.services.translator;

public enum Language {

	NERD( 1, "nerd", "Nerd" )
	, RU( 2, "ru", "Russian" );

	private final int id;
	private final String code;
	private final String name;

	private Language( final int id, final String code, final String name ) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
