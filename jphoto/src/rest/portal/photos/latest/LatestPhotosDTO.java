package rest.portal.photos.latest;

import java.util.List;

public class LatestPhotosDTO {

	private int id;
	private List<LatestPhotoDTO> latestPhotosDTOs;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public List<LatestPhotoDTO> getLatestPhotosDTOs() {
		return latestPhotosDTOs;
	}

	public void setLatestPhotosDTOs( final List<LatestPhotoDTO> latestPhotosDTOs ) {
		this.latestPhotosDTOs = latestPhotosDTOs;
	}
}
