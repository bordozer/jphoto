package elements;

import core.general.photo.PhotoInfo;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class PhotoList {

	final private List<PhotoInfo> photoInfos;
	final private String photoListTitle;
	private int photosInLine;
	private String noPhotoText = "No photos correspond to the criterias";

	private final boolean showPaging;

	private String linkToFullListText = "All photos"; // TODO: translate
	private String linkToFullList = StringUtils.EMPTY;
	private String photosCriteriasDescription = StringUtils.EMPTY;

	private String bottomText = StringUtils.EMPTY;

	private PhotoGroupOperationMenuContainer photoGroupOperationMenuContainer;

	private int sortColumnNumber;

	public PhotoList( final List<PhotoInfo> photoInfos, final String photoListTitle ) {
		this( photoInfos, photoListTitle, true );
	}

	public PhotoList( final List<PhotoInfo> photoInfos, final String photoListTitle, final boolean showPaging ) {
		this.photoInfos = photoInfos;
		this.photoListTitle = photoListTitle;
		this.showPaging = showPaging;
	}

	public List<PhotoInfo> getPhotoInfos() {
		return photoInfos;
	}

	public String getPhotoListTitle() {
		return photoListTitle;
	}

	public int getPhotosInLine() {
		return photosInLine;
	}

	public void setPhotosInLine( int photosInLine ) {
		this.photosInLine = photosInLine;
	}

	public int getTotalPhotos() {
		return photoInfos.size();
	}

	public String getNoPhotoText() {
		return noPhotoText;
	}

	public void setNoPhotoText( final String noPhotoText ) {
		this.noPhotoText = noPhotoText;
	}

	public String getLinkToFullListText() {
		return linkToFullListText;
	}

	public void setLinkToFullListText( final String linkToFullListText ) {
		this.linkToFullListText = linkToFullListText;
	}

	public String getLinkToFullList() {
		return linkToFullList;
	}

	public void setLinkToFullList( final String linkToFullList ) {
		this.linkToFullList = linkToFullList;
	}

	public String getBottomText() {
		return bottomText;
	}

	public void setBottomText( final String bottomText ) {
		this.bottomText = bottomText;
	}

	public String getPhotosCriteriasDescription() {
		return photosCriteriasDescription;
	}

	public void setPhotosCriteriasDescription( final String photosCriteriasDescription ) {
		this.photosCriteriasDescription = photosCriteriasDescription;
	}

	public boolean isShowPaging() {
		return showPaging;
	}

	public PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer() {
		return photoGroupOperationMenuContainer;
	}

	public void setPhotoGroupOperationMenuContainer( final PhotoGroupOperationMenuContainer photoGroupOperationMenuContainer ) {
		this.photoGroupOperationMenuContainer = photoGroupOperationMenuContainer;
	}

	public boolean hasPhotos() {
		return photoInfos != null && ! photoInfos.isEmpty();
	}

	public int getSortColumnNumber() {
		return sortColumnNumber;
	}

	public void setSortColumnNumber( final int sortColumnNumber ) {
		this.sortColumnNumber = sortColumnNumber;
	}
}
