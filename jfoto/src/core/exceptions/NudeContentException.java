package core.exceptions;

public class NudeContentException extends BaseRuntimeException {

	private String url;

	public NudeContentException( final String url ) {
		super( "Nude Content Exception" );
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
