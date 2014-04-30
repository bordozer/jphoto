package json.photo.appraisal;

public class Mark {

	final int value;
	final String text;

	private boolean selected;

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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected( final boolean selected ) {
		this.selected = selected;
	}
}
