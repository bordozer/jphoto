package ui.elements;

import core.general.photo.group.PhotoGroupOperationMenuContainer;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class PhotoList {

	private int photoListId;

	private final List<Integer> photoIds;
	private final String photoListTitle;
	private String noPhotoText = "PhotoList: No photos correspond to the criterias";

	private final boolean showPaging;

	private String linkToFullListText = "PhotoList: All photos";

	private String linkToFullList = StringUtils.EMPTY;
	private String photosCriteriasDescription = StringUtils.EMPTY;

	private String bottomText = StringUtils.EMPTY;

	private PhotoGroupOperationMenuContainer photoGroupOperationMenuContainer;

	public PhotoList( final List<Integer> photoIds, final String photoListTitle ) {
		this( photoIds, photoListTitle, true );
	}

	public PhotoList( final List<Integer> photoIds, final String photoListTitle, final boolean showPaging ) {
		this.photoIds = photoIds;
		this.photoListTitle = photoListTitle;
		this.showPaging = showPaging;
	}

	public void setPhotoListId( final int photoListId ) {
		this.photoListId = photoListId;
	}

	public int getPhotoListId() {
		return photoListId;
	}

	public List<Integer> getPhotoIds() {
		return photoIds;
	}

	public String getPhotoListTitle() {
		return photoListTitle;
	}

	public int getTotalPhotos() {
		return photoIds.size();
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
		return photoIds != null && ! photoIds.isEmpty();
	}
}
