package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightPhoto;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightUser;
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

	public abstract String getPhotosightCategoryPageUrl( final PhotosightCategory photosightCategory );

	public abstract String getPhotosightUserName( final PhotosightUser photosightUser );

	public abstract String getPhotosightUserName( final String photosightUserId );

	public abstract String getPhotosightUserPageLink( final PhotosightUser photosightUser );

	public abstract String getPhotosightPhotoPageLink( final PhotosightPhoto photosightPhoto );

	public abstract String getPhotosightCategoryPageLink( final PhotosightCategory photosightCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService, Language language );

	public abstract String getPhotoCardLink( final int photosightPhotoId );

	public abstract String getUserPageContent( final int page, final String photosightUserId );

	public abstract String getPhotoPageContent( final PhotosightUser photosightUser, final int photoId );

	public static AbstractRemoteContentHelper getInstance( final PhotosImportSource importSource ) {
		switch ( importSource ) {
			case PHOTOSIGHT:
				return new PhotosightRemoteContentHelper();
		}

		throw new IllegalArgumentException( String.format( "Illegal web photos import source: '%s'", importSource ) );
	}
}
