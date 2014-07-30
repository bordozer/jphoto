package admin.controllers.jobs.edit.photosImport.strategies.web.download;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.*;
import admin.jobs.entries.AbstractJob;
import core.services.system.Services;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RemotePhotoDownloader {

	private final CachedRemotePhotosDownloadStrategy cacheStrategy;
	private final NotCachedRemotePhotosDownloadStrategy downloadStrategy;

	private RemoteUser remoteUser;
	private AbstractRemotePhotoSiteUrlHelper remoteContentHelper;
	private Services services;

	public RemotePhotoDownloader( final AbstractJob job, final RemoteUser remoteUser, final List<RemotePhotoData> remotePhotosData, final RemotePhotoSiteCacheXmlUtils remotePhotoSiteCacheXmlUtils, final AbstractRemotePhotoSiteUrlHelper remoteContentHelper, final PhotosImportSource importSource, final Services services ) {

		this.remoteUser = remoteUser;
		this.remoteContentHelper = remoteContentHelper;
		this.services = services;

		this.cacheStrategy = new CachedRemotePhotosDownloadStrategy( job, importSource, remotePhotosData, remotePhotoSiteCacheXmlUtils );
		this.downloadStrategy = new NotCachedRemotePhotosDownloadStrategy( job, remoteUser, remotePhotosData, remotePhotoSiteCacheXmlUtils, remoteContentHelper, services );
	}

	public List<RemotePhotoData> getCachedRemotePhotosData() {
		return cacheStrategy.getRemotePhotosData();
	}

	public List<RemotePhotoData> getNotCachedRemotePhotosData() {
		return downloadStrategy.getRemotePhotosData();
	}

	public List<RemotePhoto> getRemotePhotos() throws IOException {
		final List<RemotePhoto> result = newArrayList();

		result.addAll( cacheStrategy.download() );
		result.addAll( downloadStrategy.download() );

		return result;
	}
}
