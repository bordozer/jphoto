package com.bordozer.jphoto.core.general.data;

import java.util.Date;

public class PhotoRating {

    private int photoId;

    private Date timeFrom;
    private Date timeTo;

    private int ratingPosition;
    private int summaryMark;

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(final int photoId) {
        this.photoId = photoId;
    }

    public Date getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(final Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Date getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(final Date timeTo) {
        this.timeTo = timeTo;
    }

    public int getRatingPosition() {
        return ratingPosition;
    }

    public void setRatingPosition(final int ratingPosition) {
        this.ratingPosition = ratingPosition;
    }

    public int getSummaryMark() {
        return summaryMark;
    }

    public void setSummaryMark(final int summaryMark) {
        this.summaryMark = summaryMark;
    }

    @Override
    public int hashCode() {
        return photoId;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", photoId, summaryMark);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PhotoRating)) {
            return false;
        }

        final PhotoRating user = (PhotoRating) obj;
        return user.getPhotoId() == photoId;
    }
}
