package rest.photo.upload.category;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import ui.controllers.photos.edit.GenreWrapper;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PhotoCategoryHandlerDTO {

	private int photoId;

	private int selectedCategoryId;
	private boolean photoContainsNude;

	private List<PhotoCategoryDTO> photoCategoryDTOs;

	private PhotoUploadNudeContentDTO nudeContentDTO;
	private PhotoUploadAllowanceDTO photoUploadAllowanceDTO;

	public void setPhotoId( final int photoId ) {
		this.photoId = photoId;
	}

	public int getPhotoId() {
		return photoId;
	}

	public int getSelectedCategoryId() {
		return selectedCategoryId;
	}

	public void setSelectedCategoryId( final int selectedCategoryId ) {
		this.selectedCategoryId = selectedCategoryId;
	}

	public boolean isPhotoContainsNude() {
		return photoContainsNude;
	}

	public void setPhotoContainsNude( final boolean photoContainsNude ) {
		this.photoContainsNude = photoContainsNude;
	}

	public List<PhotoCategoryDTO> getPhotoCategoryDTOs() {
		return photoCategoryDTOs;
	}

	public void setPhotoCategoryDTOs( final List<PhotoCategoryDTO> photoCategoryDTOs ) {
		this.photoCategoryDTOs = photoCategoryDTOs;
	}

	public PhotoUploadNudeContentDTO getNudeContentDTO() {
		return nudeContentDTO;
	}

	public void setNudeContentDTO( final PhotoUploadNudeContentDTO nudeContentDTO ) {
		this.nudeContentDTO = nudeContentDTO;
	}

	public PhotoUploadAllowanceDTO getPhotoUploadAllowanceDTO() {
		return photoUploadAllowanceDTO;
	}

	public void setPhotoUploadAllowanceDTO( final PhotoUploadAllowanceDTO photoUploadAllowanceDTO ) {
		this.photoUploadAllowanceDTO = photoUploadAllowanceDTO;
	}
}
