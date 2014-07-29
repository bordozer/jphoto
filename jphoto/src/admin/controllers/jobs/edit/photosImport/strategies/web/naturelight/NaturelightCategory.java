package admin.controllers.jobs.edit.photosImport.strategies.web.naturelight;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import utils.NumberUtils;

public enum NaturelightCategory implements RemotePhotoSiteCategory {

	BIRDS( 1, "NaturelightCategory: Birds", "Birds" )
	, MACRO( 4, "NaturelightCategory: Macro", "Macro" )
	, LANDSCAPE( 5, "NaturelightCategory: Landscape", "Landscape" )
	, CREEP( 11, "NaturelightCategory: Creep", "Creep" ) // presmykaychiesya
	, MAMMAL( 2, "NaturelightCategory: Mammal", "Mammal" ) // mlekopitauschee
	, FLORA( 12, "NaturelightCategory: Flora", "Flora" )
	, HUMAN_AND_NATURE( 15, "NaturelightCategory: Human and nature", "Human and nature" )
	, OTHER( 18, "NaturelightCategory: Other", "Other" )
	, UNDERWATER( 6, "NaturelightCategory: Underwater", "Underwater" )
	, TRAVELLING( 14, "NaturelightCategory: Travelling", "Travelling" )
	, ARTHROPODA( 20, "NaturelightCategory: Athropoda", "Athropoda" ) // chlenistonogie
	, AMPHIBIA( 9, "NaturelightCategory: Amphibia", "Amphibia" ) // zemnovodmye
	, Action( 19, "NaturelightCategory: Action", "Action" )
	, APERIODICITIES( 13, "NaturelightCategory: Nature aperiodicities", "Nature aperiodicities" ) // anomalii
	, UNDERWATER_MACRO( 16, "NaturelightCategory: Underwater macro", "Underwater macro" )
	, ASTROPHOTOGRAPHY( 7, "NaturelightCategory: astrophotography", "Astrophotography" )
	, FACES( 8, "NaturelightCategory: Faces", "Faces" )
	, TECH( 17, "NaturelightCategory: Tech", "Tech" )
	;

	private final int id;
	private final String name;
	private final String folder;

	NaturelightCategory( final int id, final String name, final String folder ) {
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
