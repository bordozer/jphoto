package admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum Photo35Category implements RemotePhotoSiteCategory {

	GLAMOUR( 97, "Photo35Category: Glamour" )
	, CITY( 101, "Photo35Category: City" )
	, GENRE( 95, "Photo35Category: Genre" )
	, GENRE_PORTRAIT( 114, "Photo35Category: Genre portrait" )
	, ANIMALS( 103, "Photo35Category: Animals" )
	, COLLAGE( 115, "Photo35Category: Collage" )
	, MACRO( 102, "Photo35Category: Macro" )
	, STILL_LIFE( 104, "Photo35Category: Still life" )
	, EROTICA( 98, "Photo35Category: Erotica" )
	, OTHER( 108, "Photo35Category: Other" )
	, LANDSCAPE( 99, "Photo35Category: Landscape" )
	, UNDERWATER( 109, "Photo35Category: Underwater" )
	, PORTRAIT( 96, "Photo35Category: Portrait" )
	, NATURE( 111, "Photo35Category: Nature" )
	, TRAVEL( 110, "Photo35Category: Travel" )
	, COMMERCIAL_PHOTOGRAPHY( 100, "Photo35Category: Commercial photography" )
	, REPORTAGE( 94, "Photo35Category: Reportage" )
	, SPORT( 105, "Photo35Category: Sport" )
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
