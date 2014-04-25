package admin.controllers.jobs.edit.photosImport;

import core.interfaces.IdentifiableNameable;

public enum PhotosImportSource implements IdentifiableNameable {
	FILE_SYSTEM( 1, "PhotosImportSource: File system" )
	, PHOTOSIGHT( 2, "PhotosImportSource: Photosight" )
	;

	private final int id;
	private final String name;

	PhotosImportSource( int id, String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static PhotosImportSource getById( String id ) {
		return  getById( Integer.parseInt( id ) );
	}

	public static PhotosImportSource getById( int id ) {
		for ( PhotosImportSource importSource : PhotosImportSource.values() ) {
			if ( importSource.getId() == id ) {
				return importSource;
			}
		}

		return null;
	}
}
