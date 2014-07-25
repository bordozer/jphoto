package admin.controllers.jobs.edit.photosImport;

import core.interfaces.IdentifiableNameable;

public enum PhotosImportSource implements IdentifiableNameable {

	FILE_SYSTEM( 1, "PhotosImportSource: File system", "", "" )
	, PHOTOSIGHT( 2, "PhotosImportSource: Photosight", "photosight.ru", "photosight.ru" )
	;

	private final int id;
	private final String name;
	private final String url;
	private final String localStorageFolder;

	PhotosImportSource( int id, String name, final String url, final String localStorageFolder ) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.localStorageFolder = localStorageFolder;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {

		if ( this == FILE_SYSTEM ) {
			throw new IllegalArgumentException( String.format( "Local file system does not have URL" ) );
		}

		return url;
	}

	public String getLocalStorageFolder() {

		if ( this == FILE_SYSTEM ) {
			throw new IllegalArgumentException( String.format( "Local file system does not have local storage folder" ) );
		}

		return localStorageFolder;
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
