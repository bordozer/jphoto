package core.services.photo.list.factory;

import java.util.List;
import java.util.Set;

public class PhotoListMetrics {

	private final List<Integer> photoIds;
	private final int photosCount;
	private final Set<Integer> hiddenPhotoIds;

	public PhotoListMetrics( final List<Integer> photoIds, final int photosCount, final Set<Integer> hiddenPhotoIds ) {
		this.photoIds = photoIds;
		this.photosCount = photosCount;
		this.hiddenPhotoIds = hiddenPhotoIds;
	}

	public List<Integer> getPhotoIds() {
		return photoIds;
	}

	public int getPhotosCount() {
		return photosCount;
	}

	public boolean hasPhotos() {
		return photoIds != null && photoIds.size() > 0;
	}

	public Set<Integer> getHiddenPhotoIds() {
		return hiddenPhotoIds;
	}
}
