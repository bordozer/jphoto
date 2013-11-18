package core.general.data;

public class PhotoMarksForPeriod {

	private int photoId;
	private int sumMarks;

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId( final int photoId ) {
		this.photoId = photoId;
	}

	public int getSumMarks() {
		return sumMarks;
	}

	public void setSumMarks( final int sumMarks ) {
		this.sumMarks = sumMarks;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", photoId, sumMarks );
	}
}
