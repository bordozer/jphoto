package ui.controllers.photos.list.factory;

import java.util.List;

public class PhotoListMetrics {

	private final List<Integer> photoIds;
	private final int photosCount;

	public PhotoListMetrics( final List<Integer> photoIds, final int photosCount ) {
		this.photoIds = photoIds;
		this.photosCount = photosCount;
	}

	public List<Integer> getPhotoIds() {
		return photoIds;
	}

	public int getPhotosCount() {
		return photosCount;
	}
}