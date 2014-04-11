package core.general.data.photoList;

import core.general.data.PhotoListCriterias;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import ui.elements.PageTitleData;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;

public abstract class AbstractPhotoListData {

	protected PageTitleData titleData;
	protected final SqlIdsSelectQuery photoListQuery;
	protected PhotoListCriterias photoListCriterias;
	protected String linkToFullList;
	protected String photoListBottomText;
	protected Date photoRatingTimeFrom;
	protected Date photoRatingTimeTo;
	protected boolean isPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos;
	protected PhotoGroupOperationMenuContainer photoGroupOperationMenuContainer;
	private int sortColumnNumber;

	public abstract boolean isGroupOperationEnabled();

	public AbstractPhotoListData( final SqlIdsSelectQuery photoListQuery ) {
		this.photoListQuery = photoListQuery;
	}

	public PageTitleData getTitleData() {
		return titleData;
	}

	public void setTitleData( final PageTitleData titleData ) {
		this.titleData = titleData;
	}

	public SqlIdsSelectQuery getPhotoListQuery() {
		return photoListQuery;
	}

	public PhotoListCriterias getPhotoListCriterias() {
		return photoListCriterias;
	}

	public void setPhotoListCriterias( final PhotoListCriterias photoListCriterias ) {
		this.photoListCriterias = photoListCriterias;
	}

	public String getLinkToFullList() {
		return linkToFullList;
	}

	public void setLinkToFullList( final String linkToFullList ) {
		this.linkToFullList = linkToFullList;
	}

	public String getPhotoListBottomText() {
		return photoListBottomText;
	}

	public void setPhotoListBottomText( final String photoListBottomText ) {
		this.photoListBottomText = photoListBottomText;
	}

	public Date getPhotoRatingTimeFrom() {
		return photoRatingTimeFrom;
	}

	public void setPhotoRatingTimeFrom( final Date photoRatingTimeFrom ) {
		this.photoRatingTimeFrom = photoRatingTimeFrom;
	}

	public Date getPhotoRatingTimeTo() {
		return photoRatingTimeTo;
	}

	public void setPhotoRatingTimeTo( final Date photoRatingTimeTo ) {
		this.photoRatingTimeTo = photoRatingTimeTo;
	}

	public boolean isPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos() {
		return isPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos;
	}

	public void setPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos( final boolean photoPreviewMustBeHiddenForAnonymouslyPostedPhotos ) {
		isPhotoPreviewMustBeHiddenForAnonymouslyPostedPhotos = photoPreviewMustBeHiddenForAnonymouslyPostedPhotos;
	}

	public PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer() {
		return photoGroupOperationMenuContainer;
	}

	public void setPhotoGroupOperationMenuContainer( final PhotoGroupOperationMenuContainer photoGroupOperationMenuContainer ) {
		this.photoGroupOperationMenuContainer = photoGroupOperationMenuContainer;
	}

	public int getSortColumnNumber() {
		return sortColumnNumber;
	}

	public void setSortColumnNumber( final int sortColumnNumber ) {
		this.sortColumnNumber = sortColumnNumber;
	}
}
