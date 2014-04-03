package ui.controllers.marks.list;

import core.general.base.AbstractGeneralPageModel;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoPreviewWrapper;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserPhotoVote;

import java.util.Map;
import java.util.Set;

public class PhotoMarkListModel extends AbstractGeneralPageModel {

	private Photo photo;
	private User photoAuthor;
	private Genre genre;
	private String photoPreviewImgUrl;

	private Set<PhotoVotingCategory> votingCategories;
	private Map<User, Map<PhotoVotingCategory, UserPhotoVote>> userVotesMap;
	private Map<PhotoVotingCategory, Integer> marksByCategoriesMap;

	private PhotoPreviewWrapper photoPreviewWrapper;

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public Photo getPhoto() {
		return photo;
	}

	public User getPhotoAuthor() {
		return photoAuthor;
	}

	public void setPhotoAuthor( final User photoAuthor ) {
		this.photoAuthor = photoAuthor;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	public String getPhotoPreviewImgUrl() {
		return photoPreviewImgUrl;
	}

	public void setPhotoPreviewImgUrl( final String photoPreviewImgUrl ) {
		this.photoPreviewImgUrl = photoPreviewImgUrl;
	}

	public Set<PhotoVotingCategory> getVotingCategories() {
		return votingCategories;
	}

	public void setVotingCategories( final Set<PhotoVotingCategory> votingCategories ) {
		this.votingCategories = votingCategories;
	}

	public Map<User, Map<PhotoVotingCategory, UserPhotoVote>> getUserVotesMap() {
		return userVotesMap;
	}

	public void setUserVotesMap( final Map<User, Map<PhotoVotingCategory, UserPhotoVote>> userVotesMap ) {
		this.userVotesMap = userVotesMap;
	}

	public Map<PhotoVotingCategory, Integer> getMarksByCategoriesMap() {
		return marksByCategoriesMap;
	}

	public void setMarksByCategoriesMap( final Map<PhotoVotingCategory, Integer> marksByCategoriesMap ) {
		this.marksByCategoriesMap = marksByCategoriesMap;
	}

	public PhotoPreviewWrapper getPhotoPreviewWrapper() {
		return photoPreviewWrapper;
	}

	public void setPhotoPreviewWrapper( final PhotoPreviewWrapper photoPreviewWrapper ) {
		this.photoPreviewWrapper = photoPreviewWrapper;
	}
}
