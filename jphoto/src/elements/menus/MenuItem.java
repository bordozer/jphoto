package elements.menus;

public class MenuItem {

	protected final String caption;
	private final String link;
	private String jsFunction;

	public MenuItem( final String caption, final String link ) {
		this.caption = caption;
		this.link = link;

		this.jsFunction = "return true;";
	}

	public String getCaption() {
		return caption;
	}

	public String getLink() {
		return link;
	}

	public String getJsFunction() {
		return jsFunction;
	}

	public void setJsFunction( final String jsFunction ) {
		this.jsFunction = jsFunction;
	}

	public static MenuItem noLinkMenu( final String title ) {
		return new MenuItem( title, "#" );
	}

	public static MenuItem jsFunctionLinkMenu( final String title, final String jsFunction ) {
		final MenuItem menuItem = new MenuItem( title, "#" );
		menuItem.setJsFunction( jsFunction );
		return menuItem;
	}
}
