package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.naturelight.NaturelightCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.photos35.Photo35Category;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;

public interface RemotePhotoSiteCategory {

	int getId();

	String getName();

	public static RemotePhotoSiteCategory[] getRemotePhotoSiteCategories( final PhotosImportSource photosImportSource ) {

		switch ( photosImportSource ) {
			case PHOTOSIGHT:
				return PhotosightCategory.values();
			case PHOTO35:
				return Photo35Category.values();
			case NATURELIGHT:
				return NaturelightCategory.values();
		}

		throw new IllegalArgumentException( String.format( "Unsupported photos import source: %s", photosImportSource ) );
	}

	public static RemotePhotoSiteCategory getById( final PhotosImportSource photosImportSource, final int id ) {
		switch ( photosImportSource ) {
			case PHOTOSIGHT:
				return PhotosightCategory.getById( id );
			case PHOTO35:
				return Photo35Category.getById( id );
			case NATURELIGHT:
				return NaturelightCategory.getById( id );
		}

		throw new IllegalArgumentException( String.format( "Unsupported photos import source: %s", photosImportSource ) );
	}
}
