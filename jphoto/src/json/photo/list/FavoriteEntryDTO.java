package json.photo.list;

public class FavoriteEntryDTO {

	private final String name;
	private final String icon;

	public FavoriteEntryDTO( final String name, final String icon ) {
		this.name = name;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}
}
