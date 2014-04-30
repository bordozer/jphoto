package json.photo.appraisal;

public class Mark {

	final int value;
	final String text;

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
}
