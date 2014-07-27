package admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum Photo35Category implements RemotePhotoSiteCategory {

	GLAMOUR( 97, "Glamour" )
	, CITY( 101, "City" )
	, GENRE( 95, "Genre" )
	, GENRE_PORTRAIT( 114, "Genre portrait" )
	, ANIMALS( 103, "Animals" )
	, COLLAGE( 115, "Collage" )
	, MACRO( 102, "Macro" )
	, STILL_LIFE( 104, "Still life" )
	, EROTICA( 98, "Erotica" )
	, OTHER( 108, "Other" )
	, LANDSCAPE( 99, "Landscape" )
	, UNDERWATER( 109, "Underwater" )
	, PORTRAIT( 96, "Portrait" )
	, NATURE( 111, "Nature" )
	, TRAVEL( 110, "Travel" )
	, COMMERCIAL_PHOTOGRAPHY( 100, "Commercial photography" )
	, REPORTAGE( 94, "Reportage" )
	, SPORT( 105, "Sport" )
	;

	private final int id;
	private final String name;

	Photo35Category( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
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
