package admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum Photo35Category implements RemotePhotoSiteCategory {

	PORTRAIT( 1, "Portrait" )
	, EROTIC_GLAMOUR( 2, "Erotic, Glamour" )
	, NATURE( 3, "Nature" )
	, LANDSCAPES( 4, "Landscapes" )
	, OTHER( 5, "Other" )
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
