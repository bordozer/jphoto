package json.photo.appraisal;

public class Mark {

	private int value;
	private String text;

	public Mark() {
	}

	public Mark( final int value, final String text ) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return String.format( "%d: %s", value, text );
	}
}
