package admin.controllers.jobs.edit.photosImport.strategies.web.download;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhoto;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoData;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCacheXmlUtils;
import admin.jobs.entries.AbstractJob;

import java.io.IOException;
import java.util.List;

public abstract class RemotePhotosDownloadStrategy {

	protected AbstractJob job;
	protected List<RemotePhotoData> remotePhotosData;
	protected RemotePhotoSiteCacheXmlUtils remotePhotoSiteCacheXmlUtils;

	public abstract List<RemotePhoto> download() throws IOException;

	protected RemotePhotosDownloadStrategy( final AbstractJob job, final RemotePhotoSiteCacheXmlUtils remotePhotoSiteCacheXmlUtils ) {
		this.job = job;
		this.remotePhotoSiteCacheXmlUtils = remotePhotoSiteCacheXmlUtils;
	}

	public List<RemotePhotoData> getRemotePhotosData() {
		return remotePhotosData;
	}
}
