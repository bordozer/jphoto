package rest.photo.list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PhotoListEntryDisplayOptions {

	private boolean groupOperationEnabled;
	private boolean hidePreviewsForAnonymouslyPostedPhotos;

	public boolean isGroupOperationEnabled() {
		return groupOperationEnabled;
	}

	public void setGroupOperationEnabled( final boolean groupOperationEnabled ) {
		this.groupOperationEnabled = groupOperationEnabled;
	}

	public boolean isHidePreviewsForAnonymouslyPostedPhotos() {
		return hidePreviewsForAnonymouslyPostedPhotos;
	}

	public void setHidePreviewsForAnonymouslyPostedPhotos( final boolean hidePreviewsForAnonymouslyPostedPhotos ) {
		this.hidePreviewsForAnonymouslyPostedPhotos = hidePreviewsForAnonymouslyPostedPhotos;
	}
}
