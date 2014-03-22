package controllers.photos.list;

import core.general.genre.Genre;
import org.springframework.validation.BindingResult;

import java.util.List;

public class PhotoFilterModel {

	private String filterPhotoName;
	private String filterGenreId;
	private boolean showPhotosWithNudeContent;

	private String filterAuthorName;
	private List<Integer> photoAuthorMembershipTypeIds;

	private List<Genre> filterGenres;
	private BindingResult bindingResult;

	public String getFilterPhotoName() {
		return filterPhotoName;
	}

	public void setFilterPhotoName( final String filterPhotoName ) {
		this.filterPhotoName = filterPhotoName;
	}

	public String getFilterGenreId() {
		return filterGenreId;
	}

	public void setFilterGenreId( final String filterGenreId ) {
		this.filterGenreId = filterGenreId;
	}

	public boolean isShowPhotosWithNudeContent() {
		return showPhotosWithNudeContent;
	}

	public void setShowPhotosWithNudeContent( final boolean showPhotosWithNudeContent ) {
		this.showPhotosWithNudeContent = showPhotosWithNudeContent;
	}

	public String getFilterAuthorName() {
		return filterAuthorName;
	}

	public void setFilterAuthorName( final String filterAuthorName ) {
		this.filterAuthorName = filterAuthorName;
	}

	public List<Integer> getPhotoAuthorMembershipTypeIds() {
		return photoAuthorMembershipTypeIds;
	}

	public void setPhotoAuthorMembershipTypeIds( final List<Integer> photoAuthorMembershipTypeIds ) {
		this.photoAuthorMembershipTypeIds = photoAuthorMembershipTypeIds;
	}

	public List<Genre> getFilterGenres() {
		return filterGenres;
	}

	public void setFilterGenres( final List<Genre> filterGenres ) {
		this.filterGenres = filterGenres;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

	public void setBindingResult( final BindingResult bindingResult ) {
		this.bindingResult = bindingResult;
	}
}
