package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum PhotosightCategory implements RemotePhotoSiteCategory {

	NUDE( 15, "Nude" )
	, GLAMOUR( 18, "Glamour" )
	, CITY( 7, "City" )
	, CHILDREN( 80, "Children" )
	, GENRE( 12, "Genre" )
	, GENRE_PORTRAIT( 70, "Genre portrait" )
	, ANIMALS( 8, "Animals" )
	, DIGITAL_ART( 14, "Digital art" )
	, MACRO( 5, "Macro" )
	, MOBILE_PHOTO( 78, "Mobile photo" )
	, STILL( 4, "Still" )
	, LANDSCAPE( 36, "Landscape" )
	, UNDERWATER( 65, "Underwater" )
	, PORTRAIT( 2, "Portrait" )
	, TRAVELLING( 6, "Travelling" )
	, ADVERTISING( 19, "Advertising" )
	, REPORTING( 9, "Reporting" )
	, WEDDING( 91, "Wedding" )
	, SPORT( 10, "Sport" )
	, PHOTOSIGHT( 17, "Photosight" )
	, PHOTO_HUNTING( 64, "Photo hunting" )
	, ARCHITECTURE( 82, "Architecture" )
	, MUSEUM( 13, "Museum" )
	, NATURE( 3, "Nature" )
	, TECH( 92, "Tech" )
	, MODELS( 87, "Models" )
	, HUMOR( 11, "Humor" )
	, REST( 16, "Rest" )
	, PAPARAZZI( 27, "Paparazzi" )
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
		for ( final PhotosightCategory category : PhotosightCategory.values() ) {
			if ( category.getId() == id ) {
				return category;
			}
		}

//		throw new IllegalArgumentException( String.format( "Illegal PhotosightCategory id: %d", id ));
		return null;
	}

	public static PhotosightCategory getById( final String id ) {
		return getById( NumberUtils.convertToInt( id ) );
	}
}
