package ui.services.page;

import core.services.translator.Language;

public class LanguageWrapper {

	private final Language language;
	private String style;
	private String title;

	public LanguageWrapper( final Language language ) {
		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle( final String style ) {
		this.style = style;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( final String title ) {
		this.title = title;
	}
}
