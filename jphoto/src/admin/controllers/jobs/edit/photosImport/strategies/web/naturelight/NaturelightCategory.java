package admin.controllers.jobs.edit.photosImport.strategies.web.naturelight;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum NaturelightCategory implements RemotePhotoSiteCategory {

	BIRDS( 1, "Birds" )
	, MACRO( 4, "Macro" )
	, LANDSCAPE( 5, "Landscape" )
	, CREEP( 11, "Creep" ) // presmykaychiesya
	, MAMMAL( 2, "Mammal" ) // mlekopitauschee
	, FLORA( 12, "Flora" )
	, HUMAN_AND_NATURE( 15, "Human and nature" )
	, OTHER( 18, "Other" )
	, UNDERWATER( 6, "Underwater" )
	, TRAVELLING( 14, "Travelling" )
	, ARTHROPODA( 20, "Athropoda" ) // chlenistonogie
	, AMPHIBIA( 9, "Amphibia" ) // zemnovodmye
	, Action( 19, "Action" )
	, APERIODICITIES( 13, "Nature aperiodicities" ) // anomalii
	, UNDERWATER_MACRO( 16, "Underwater macro" )
	, STARS( 7, "Stars" )
	, FACES( 8, "Faces" )
	, TECH( 17, "Tech" )
	;

	private final int id;
	private final String name;

	NaturelightCategory( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static NaturelightCategory getById( final int id ) {
		for ( final NaturelightCategory category : values() ) {
			if ( category.getId() == id ) {
				return category;
			}
		}

		return null;
	}

	public static NaturelightCategory getById( final String id ) {
		return getById( NumberUtils.convertToInt( id ) );
	}
}
