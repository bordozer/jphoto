package ui.viewModes;

public enum PhotoListViewModeType {

	VIEW_MODE_PREVIEW( "preview", "PhotoListViewMode: Preview", "layouts_preview.png" )
	, VIEW_MODE_BIG_PREVIEW( "details", "PhotoListViewMode: Details", "layouts_details.png" );

	private final String key;
	private final String name;
	private final String icon;

	PhotoListViewModeType( final String key, final String name, final String icon ) {
		this.key = key;
		this.name = name;
		this.icon = icon;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return String.format( "view-modes/%s", icon );
	}

	public static PhotoListViewModeType getById( final String key ) {

		for ( final PhotoListViewModeType viewMode : PhotoListViewModeType.values() ) {
			if ( viewMode.getKey().equalsIgnoreCase( key ) ) {
				return viewMode;
			}
		}

		return VIEW_MODE_PREVIEW;
	}
}
