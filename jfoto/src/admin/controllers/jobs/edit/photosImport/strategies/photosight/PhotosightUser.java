package admin.controllers.jobs.edit.photosImport.strategies.photosight;

public class PhotosightUser {

	private final int id;
	private String name;

	public PhotosightUser( final int id ) {
		this.id = id;
	}

	public int getId() {
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
		return String.format( "Photosight user #%d: %s", id, name );
	}
}
