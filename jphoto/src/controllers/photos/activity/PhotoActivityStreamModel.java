package controllers.photos.activity;

import core.general.activity.AbstractActivityStreamEntry;
import core.general.base.AbstractGeneralPageModel;
import core.general.photo.Photo;

import java.util.List;

public class PhotoActivityStreamModel extends AbstractGeneralPageModel {

	private final Photo photo;
	private List<AbstractActivityStreamEntry> activities;

	public PhotoActivityStreamModel( final Photo photo ) {
		this.photo = photo;
	}

	public Photo getPhoto() {
		return photo;
	}

	public List<AbstractActivityStreamEntry> getActivities() {
		return activities;
	}

	public void setActivities( final List<AbstractActivityStreamEntry> activities ) {
		this.activities = activities;
	}
}
