package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.ImportedImage;

public class RemotePhotoSitePhotoDiskEntry {

	private final RemotePhotoSitePhoto remotePhotoSitePhoto;
	private final ImportedImage importedImage;

	public RemotePhotoSitePhotoDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final ImportedImage importedImage ) {
		this.remotePhotoSitePhoto = remotePhotoSitePhoto;
		this.importedImage = importedImage;
	}

	public RemotePhotoSitePhoto getRemotePhotoSitePhoto() {
		return remotePhotoSitePhoto;
	}

	public ImportedImage getImportedImage() {
		return importedImage;
	}

	@Override
	public String toString() {
		return String.format( "%s", remotePhotoSitePhoto );
	}
}
