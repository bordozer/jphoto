package json.photo.appraisal;

public class PhotoAppraisalCategory {

	private int id;
	private String nameTranslated;

	private boolean selected;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public String getNameTranslated() {
		return nameTranslated;
	}

	public void setNameTranslated( final String nameTranslated ) {
		this.nameTranslated = nameTranslated;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected( final boolean selected ) {
		this.selected = selected;
	}
}
