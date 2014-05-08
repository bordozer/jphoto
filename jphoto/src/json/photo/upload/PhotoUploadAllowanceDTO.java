package json.photo.upload;

public class PhotoUploadAllowanceDTO {

	private int useId;
	private int genreId;

	public void setUseId( final int useId ) {
		this.useId = useId;
	}

	public int getUseId() {
		return useId;
	}

	public void setGenreId( final int genreId ) {
		this.genreId = genreId;
	}

	public int getGenreId() {
		return genreId;
	}
}
