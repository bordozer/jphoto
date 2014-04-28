package ui.controllers.photos.edit;

import core.general.base.AbstractGeneralModel;
import core.general.photo.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public class PhotoEditDataModel extends AbstractGeneralModel {

	private Photo photo;

	private MultipartFile photoFile;
	private File tempPhotoFile;

	private String photoName;
	private String photoDescription;
	private String photoKeywords;

	private int selectedGenreId;
	private List<GenreWrapper> genreWrappers;

	private boolean containsNudeContent;

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public Photo getPhoto() {
		return photo;
	}

	public MultipartFile getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile( final MultipartFile photoFile ) {
		this.photoFile = photoFile;
	}

	public void setTempPhotoFile( final File tempPhotoFile ) {
		this.tempPhotoFile = tempPhotoFile;
	}

	public File getTempPhotoFile() {
		return tempPhotoFile;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName( final String photoName ) {
		this.photoName = photoName;
	}

	public String getPhotoDescription() {
		return photoDescription;
	}

	public void setPhotoDescription( final String photoDescription ) {
		this.photoDescription = photoDescription;
	}

	public String getPhotoKeywords() {
		return photoKeywords;
	}

	public void setPhotoKeywords( final String photoKeywords ) {
		this.photoKeywords = photoKeywords;
	}

	public int getSelectedGenreId() {
		return selectedGenreId;
	}

	public void setSelectedGenreId( final int selectedGenreId ) {
		this.selectedGenreId = selectedGenreId;
	}

	public List<GenreWrapper> getGenreWrappers() {
		return genreWrappers;
	}

	public void setGenreWrappers( final List<GenreWrapper> genreWrappers ) {
		this.genreWrappers = genreWrappers;
	}

	public boolean isContainsNudeContent() {
		return containsNudeContent;
	}

	public void setContainsNudeContent( final boolean containsNudeContent ) {
		this.containsNudeContent = containsNudeContent;
	}
}
