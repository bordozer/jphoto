package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum PhotosightCategory implements RemotePhotoSiteCategory {

	NUDE( 15, "PhotosightCategory: Nude", "Nude" )
	, GLAMOUR( 18, "PhotosightCategory: Glamour", "Glamour" )
	, CITY( 7, "PhotosightCategory: City", "City" )
	, CHILDREN( 80, "PhotosightCategory: Children", "Children" )
	, GENRE( 12, "PhotosightCategory: Genre", "Genre" )
	, GENRE_PORTRAIT( 70, "PhotosightCategory: Genre portrait", "Genre portrait" )
	, ANIMALS( 8, "PhotosightCategory: Animals", "Animals" )
	, DIGITAL_ART( 14, "PhotosightCategory: Digital art", "Digital art" )
	, MACRO( 5, "PhotosightCategory: Macro", "Macro" )
	, MOBILE_PHOTO( 78, "PhotosightCategory: Mobile photo", "Mobile photo" )
	, STILL( 4, "PhotosightCategory: Still", "Still" )
	, LANDSCAPE( 36, "PhotosightCategory: Landscape", "Landscape" )
	, UNDERWATER( 65, "PhotosightCategory: Underwater", "Underwater" )
	, PORTRAIT( 2, "PhotosightCategory: Portrait", "Portrait" )
	, TRAVELLING( 6, "PhotosightCategory: Travelling", "Travelling" )
	, ADVERTISING( 19, "PhotosightCategory: Advertising", "Advertising" )
	, REPORTING( 9, "PhotosightCategory: Reporting", "Reporting" )
	, WEDDING( 91, "PhotosightCategory: Wedding", "Wedding" )
	, SPORT( 10, "PhotosightCategory: Sport", "Sport" )
	, PHOTOSIGHT( 17, "PhotosightCategory: Photosight", "Photosight" )
	, PHOTO_HUNTING( 64, "PhotosightCategory: Photo hunting", "Photo hunting" )
	, ARCHITECTURE( 82, "PhotosightCategory: Architecture", "Architecture" )
	, MUSEUM( 13, "PhotosightCategory: Museum", "Museum" )
	, NATURE( 3, "PhotosightCategory: Nature", "Nature" )
	, TECH( 92, "PhotosightCategory: Tech", "Tech" )
	, MODELS( 87, "PhotosightCategory: Models", "Models" )
	, HUMOR( 11, "PhotosightCategory: Humor", "Humor" )
	, REST( 16, "PhotosightCategory: Rest", "Rest" )
	, PAPARAZZI( 27, "PhotosightCategory: Paparazzi", "Paparazzi" )
	;

	private final int id;
	private final String name;
	private final String folder;

	private PhotosightCategory( final int id, final String name, final String folder ) {
		this.id = id;
		this.name = name;
		this.folder = folder;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getFolder() {
		return folder;
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
