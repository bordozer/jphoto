package core.general.photo;

import core.enums.PhotoAwardKey;

import java.util.Date;

public class PhotoAward {

	private final int photoId;
	private PhotoAwardKey awardKey;
	private Date timeFrom;
	private Date timeTo;

	public PhotoAward( final int photoId ) {
		this.photoId = photoId;
	}

	public int getPhotoId() {
		return photoId;
	}

	public PhotoAwardKey getAwardKey() {
		return awardKey;
	}

	public void setAwardKey( final PhotoAwardKey awardKey ) {
		this.awardKey = awardKey;
	}

	public Date getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom( final Date timeFrom ) {
		this.timeFrom = timeFrom;
	}

	public Date getTimeTo() {
		return timeTo;
	}

	public void setTimeTo( final Date timeTo ) {
		this.timeTo = timeTo;
	}
}
