package rest.admin.restriction;

public class CheckboxDTO {

	private String value;
	private String label;
	private boolean checked;

	public String getValue() {
		return value;
	}

	public void setValue( final String value ) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel( final String label ) {
		this.label = label;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked( final boolean checked ) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", value, label );
	}
}
