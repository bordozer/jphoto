package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum PhotosightCategory implements RemotePhotoSiteCategory {

	NUDE( 15, "PhotosightCategory: Nude" )
	, GLAMOUR( 18, "PhotosightCategory: Glamour" )
	, CITY( 7, "PhotosightCategory: City" )
	, CHILDREN( 80, "PhotosightCategory: Children" )
	, GENRE( 12, "PhotosightCategory: Genre" )
	, GENRE_PORTRAIT( 70, "PhotosightCategory: Genre portrait" )
	, ANIMALS( 8, "PhotosightCategory: Animals" )
	, DIGITAL_ART( 14, "PhotosightCategory: Digital art" )
	, MACRO( 5, "PhotosightCategory: Macro" )
	, MOBILE_PHOTO( 78, "PhotosightCategory: Mobile photo" )
	, STILL( 4, "PhotosightCategory: Still" )
	, LANDSCAPE( 36, "PhotosightCategory: Landscape" )
	, UNDERWATER( 65, "PhotosightCategory: Underwater" )
	, PORTRAIT( 2, "PhotosightCategory: Portrait" )
	, TRAVELLING( 6, "PhotosightCategory: Travelling" )
	, ADVERTISING( 19, "PhotosightCategory: Advertising" )
	, REPORTING( 9, "PhotosightCategory: Reporting" )
	, WEDDING( 91, "PhotosightCategory: Wedding" )
	, SPORT( 10, "PhotosightCategory: Sport" )
	, PHOTOSIGHT( 17, "PhotosightCategory: Photosight" )
	, PHOTO_HUNTING( 64, "PhotosightCategory: Photo hunting" )
	, ARCHITECTURE( 82, "PhotosightCategory: Architecture" )
	, MUSEUM( 13, "PhotosightCategory: Museum" )
	, NATURE( 3, "PhotosightCategory: Nature" )
	, TECH( 92, "PhotosightCategory: Tech" )
	, MODELS( 87, "PhotosightCategory: Models" )
	, HUMOR( 11, "PhotosightCategory: Humor" )
	, REST( 16, "PhotosightCategory: Rest" )
	, PAPARAZZI( 27, "PhotosightCategory: Paparazzi" )
	;

	private final int id;
	private final String name;


	private PhotosightCategory( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public static PhotosightCategory getById( final int id ) {
		for ( final PhotosightCategory category : values() ) {
			if ( category.getId() == id ) {
				return category;
			}
		}

		return null;
	}

	public static PhotosightCategory getById( final String id ) {
		return getById( NumberUtils.convertToInt( id ) );
	}
}
