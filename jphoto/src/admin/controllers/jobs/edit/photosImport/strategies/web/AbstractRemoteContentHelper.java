package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightRemoteContentHelper;
import core.log.LogHelper;
import core.services.entry.GenreService;
import core.services.translator.Language;
import core.services.utils.EntityLinkUtilsService;

public abstract class AbstractRemoteContentHelper {

	protected final LogHelper log;

	protected AbstractRemoteContentHelper( final LogHelper log ) {
		this.log = log;
	}

	public abstract String getImageContentFromUrl( final String cardUrl );

	public abstract String getUserCardUrl( final String userId );

	public abstract String getUserCardUrl( final String userId, int page );

	public abstract String getRemotePhotoSiteCategoryPageUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory );

	public abstract String getRemotePhotoSiteUserName( final RemotePhotoSiteUser remotePhotoSiteUser );

	public abstract String getRemotePhotoSiteUserName( final String remotePhotoSiteUserId );

	public abstract String getRemotePhotoSiteUserPageLink( final RemotePhotoSiteUser remotePhotoSiteUser );

	public abstract String getRemotePhotoSitePhotoPageLink( final RemotePhotoSitePhoto remotePhotoSitePhoto );

	public abstract String getRemotePhotoSiteCategoryPageLink( final RemotePhotoSiteCategory remotePhotoSiteCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService, Language language );

	public abstract String getPhotoCardLink( final int remotePhotoSitePhotoId );

	public abstract String getUserPageContent( final int page, final String remotePhotoSiteUserId );

	public abstract String getPhotoPageContent( final RemotePhotoSiteUser remotePhotoSiteUser, final int photoId );

	public static AbstractRemoteContentHelper getInstance( final PhotosImportSource importSource ) {

		switch ( importSource ) {
			case PHOTOSIGHT:
				return new PhotosightRemoteContentHelper();
		}

		throw new IllegalArgumentException( String.format( "Illegal web photos import source: '%s'", importSource ) );
	}
}
