package json.photo.appraisal;

public class PhotoAppraisalCategory {

	private int id;
	private String nameTranslated;

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

	@Override
	public String toString() {
		return String.format( "#%d: %s", id, nameTranslated );
	}
}
