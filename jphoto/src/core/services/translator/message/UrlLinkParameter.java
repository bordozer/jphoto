package core.services.translator.message;

import core.services.translator.Language;

public class UrlLinkParameter extends AbstractTranslatableMessageParameter {

	private String url;
	private String text;

	public UrlLinkParameter( final String url, final String text ) {
		super( null );
		this.url = url;
		this.text = text;
	}

	@Override
	public String getValue( final Language language ) {
		return String.format( "<a href='http://%s'>%s</a>", url, text );
	}
}
