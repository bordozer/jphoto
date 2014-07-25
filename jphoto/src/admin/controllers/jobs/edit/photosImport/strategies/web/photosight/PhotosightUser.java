package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

public class PhotosightUser {

	private final String id;
	private String name;

	public PhotosightUser( final String id ) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format( "Photosight user #%s: %s", id, name );
	}
}
