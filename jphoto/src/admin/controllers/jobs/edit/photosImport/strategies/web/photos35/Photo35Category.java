package admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum Photo35Category implements RemotePhotoSiteCategory {

	GLAMOUR( 97, "Photo35Category: Glamour", "Glamour" )
	, CITY( 101, "Photo35Category: City", "City" )
	, GENRE( 95, "Photo35Category: Genre", "Genre" )
	, GENRE_PORTRAIT( 114, "Photo35Category: Genre portrait", "Genre portrait" )
	, ANIMALS( 103, "Photo35Category: Animals", "Animals" )
	, COLLAGE( 115, "Photo35Category: Collage", "Collage" )
	, MACRO( 102, "Photo35Category: Macro", "Macro" )
	, STILL_LIFE( 104, "Photo35Category: Still life", "Still life" )
	, EROTICA( 98, "Photo35Category: Erotica", "Erotica" )
	, OTHER( 108, "Photo35Category: Other", "Other" )
	, LANDSCAPE( 99, "Photo35Category: Landscape", "Landscape" )
	, UNDERWATER( 109, "Photo35Category: Underwater", "Underwater" )
	, PORTRAIT( 96, "Photo35Category: Portrait", "Portrait" )
	, NATURE( 111, "Photo35Category: Nature", "Nature" )
	, TRAVEL( 110, "Photo35Category: Travel", "Travel" )
	, COMMERCIAL_PHOTOGRAPHY( 100, "Photo35Category: Commercial photography", "Commercial photography" )
	, REPORTAGE( 94, "Photo35Category: Reportage", "Reportage" )
	, SPORT( 105, "Photo35Category: Sport", "Sport" )
	;

	private final int id;
	private final String name;
	private final String folder;

	Photo35Category( final int id, final String name, final String folder ) {
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

	public static Photo35Category getById( final int id ) {
		for ( final Photo35Category category : values() ) {
			if ( category.getId() == id ) {
				return category;
			}
		}

		return null;
	}

	public static Photo35Category getById( final String id ) {
		return getById( NumberUtils.convertToInt( id ) );
	}
}
