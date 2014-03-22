package controllers.photos.list;

import core.general.genre.Genre;
import sql.builder.SqlIdsSelectQuery;

import java.util.List;

public class PhotoFilterData {

	private SqlIdsSelectQuery selectQuery;

	private String filterPhotoName;
	private Genre filterGenre;
	private boolean showPhotosWithNudeContent;

	private String filterAuthorName;
	private List<Integer> photoAuthorMembershipTypeIds;

	private PhotoFilterSortColumn photosSortColumn;
	private PhotoFilterSortOrder photosSortOrder;

	public SqlIdsSelectQuery getSelectQuery() {
		return selectQuery;
	}

	public void setSelectQuery( final SqlIdsSelectQuery selectQuery ) {
		this.selectQuery = selectQuery;
	}

	public String getFilterPhotoName() {
		return filterPhotoName;
	}

	public void setFilterPhotoName( final String filterPhotoName ) {
		this.filterPhotoName = filterPhotoName;
	}

	public Genre getFilterGenre() {
		return filterGenre;
	}

	public void setFilterGenre( final Genre filterGenre ) {
		this.filterGenre = filterGenre;
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

	public PhotoFilterSortColumn getPhotosSortColumn() {
		return photosSortColumn;
	}

	public void setPhotosSortColumn( final PhotoFilterSortColumn photosSortColumn ) {
		this.photosSortColumn = photosSortColumn;
	}

	public PhotoFilterSortOrder getPhotosSortOrder() {
		return photosSortOrder;
	}

	public void setPhotosSortOrder( final PhotoFilterSortOrder photosSortOrder ) {
		this.photosSortOrder = photosSortOrder;
	}
}
