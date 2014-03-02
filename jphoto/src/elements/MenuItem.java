package elements;

public class MenuItem {
	final private String caption;
	final private String link;

	public MenuItem( final String caption, final String link ) {
		this.caption = caption;
		this.link = link;
	}

	public String getCaption() {
		return caption;
	}

	public String getLink() {
		return link;
	}

	public static MenuItem noLinkMenu( final String title ) {
		return new MenuItem( title, "#" ); // TODO: translate
	}
}
