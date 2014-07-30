package admin.controllers.jobs.edit.photosImport.strategies.web.download;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhoto;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoData;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCacheXmlUtils;
import admin.jobs.entries.AbstractJob;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

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

	protected List<RemotePhotoData> filterRemotePhotosData( final List<RemotePhotoData> remotePhotosData, final boolean isCached ) {

		final List<RemotePhotoData> cachedRemotePhotosData = newArrayList( remotePhotosData );

		CollectionUtils.filter( cachedRemotePhotosData, new Predicate<RemotePhotoData>() {
			@Override
			public boolean evaluate( final RemotePhotoData remotePhotoData ) {
				return remotePhotoData.isCached() == isCached;
			}
		} );

		return cachedRemotePhotosData;
	}
}
