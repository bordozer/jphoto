package rest.portal.photos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class LatestPhotosDTO {

	private int id;
	private List<LatestPhotoDTO> latestPhotosDTOs;

	public LatestPhotosDTO() {
	}

	public LatestPhotosDTO( final int id, final List<LatestPhotoDTO> latestPhotosDTOs ) {
		this.id = id;
		this.latestPhotosDTOs = latestPhotosDTOs;
	}

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
