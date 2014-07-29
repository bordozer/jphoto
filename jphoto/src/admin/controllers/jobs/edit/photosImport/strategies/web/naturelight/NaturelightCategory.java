package admin.controllers.jobs.edit.photosImport.strategies.web.naturelight;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum NaturelightCategory implements RemotePhotoSiteCategory {

	BIRDS( 1, "NaturelightCategory: Birds" )
	, MACRO( 4, "NaturelightCategory: Macro" )
	, LANDSCAPE( 5, "NaturelightCategory: Landscape" )
	, CREEP( 11, "NaturelightCategory: Creep" ) // presmykaychiesya
	, MAMMAL( 2, "NaturelightCategory: Mammal" ) // mlekopitauschee
	, FLORA( 12, "NaturelightCategory: Flora" )
	, HUMAN_AND_NATURE( 15, "NaturelightCategory: Human and nature" )
	, OTHER( 18, "NaturelightCategory: Other" )
	, UNDERWATER( 6, "NaturelightCategory: Underwater" )
	, TRAVELLING( 14, "NaturelightCategory: Travelling" )
	, ARTHROPODA( 20, "NaturelightCategory: Athropoda" ) // chlenistonogie
	, AMPHIBIA( 9, "NaturelightCategory: Amphibia" ) // zemnovodmye
	, Action( 19, "NaturelightCategory: Action" )
	, APERIODICITIES( 13, "NaturelightCategory: Nature aperiodicities" ) // anomalii
	, UNDERWATER_MACRO( 16, "NaturelightCategory: Underwater macro" )
	, STARS( 7, "NaturelightCategory: Stars" )
	, FACES( 8, "NaturelightCategory: Faces" )
	, TECH( 17, "NaturelightCategory: Tech" )
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
