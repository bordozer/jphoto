package json.photo.list;

import core.enums.FavoriteEntryType;

import java.util.List;

public class PhotoEntryDTO {

	private final int photoId;

	private String groupOperationCheckbox;
	private String photoUploadDate;
	private String photoCategory;
	private String photoImage;

	private boolean showPhotoContextMenu;

	private String photoName;
	private String photoAuthorLink;

	private boolean showUserRank;
	private String photoAuthorRank;

	private boolean showStatistics;
	private int todayMarks;
	private int periodMarks;
	private int totalMarks;
	private String todayMarksTitle;
	private String periodMarksTitle;
	private String totalMarksUrl;
	private String totalMarksTitle;

	private boolean showAnonymousPeriodExpirationInfo;
	private String photoAnonymousPeriodExpirationInfo;

	private boolean showAdminFlag_Anonymous;

	private boolean showAdminFlag_Nude;

	private boolean userOwnThePhoto;
	private List<FavoriteEntryDTO> favoriteEntryDTOs;

	public PhotoEntryDTO( final int photoId ) {
		this.photoId = photoId;
	}

	public int getPhotoId() {
		return photoId;
	}

	public String getGroupOperationCheckbox() {
		return groupOperationCheckbox;
	}

	public void setGroupOperationCheckbox( final String groupOperationCheckbox ) {
		this.groupOperationCheckbox = groupOperationCheckbox;
	}

	public String getPhotoUploadDate() {
		return photoUploadDate;
	}

	public void setPhotoUploadDate( final String photoUploadDate ) {
		this.photoUploadDate = photoUploadDate;
	}

	public String getPhotoCategory() {
		return photoCategory;
	}

	public void setPhotoCategory( final String photoCategory ) {
		this.photoCategory = photoCategory;
	}

	public String getPhotoImage() {
		return photoImage;
	}

	public void setPhotoImage( final String photoImage ) {
		this.photoImage = photoImage;
	}

	public boolean isShowPhotoContextMenu() {
		return showPhotoContextMenu;
	}

	public void setShowPhotoContextMenu( final boolean showPhotoContextMenu ) {
		this.showPhotoContextMenu = showPhotoContextMenu;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName( final String photoName ) {
		this.photoName = photoName;
	}

	public String getPhotoAuthorLink() {
		return photoAuthorLink;
	}

	public void setPhotoAuthorLink( final String photoAuthorLink ) {
		this.photoAuthorLink = photoAuthorLink;
	}

	public String getPhotoAuthorRank() {
		return photoAuthorRank;
	}

	public void setPhotoAuthorRank( final String photoAuthorRank ) {
		this.photoAuthorRank = photoAuthorRank;
	}

	public boolean isShowStatistics() {
		return showStatistics;
	}

	public void setShowStatistics( final boolean showStatistics ) {
		this.showStatistics = showStatistics;
	}

	public boolean isShowUserRank() {
		return showUserRank;
	}

	public void setShowUserRank( final boolean showUserRank ) {
		this.showUserRank = showUserRank;
	}

	public void setTodayMarks( final int todayMarks ) {
		this.todayMarks = todayMarks;
	}

	public int getTodayMarks() {
		return todayMarks;
	}

	public void setPeriodMarks( final int periodMarks ) {
		this.periodMarks = periodMarks;
	}

	public int getPeriodMarks() {
		return periodMarks;
	}

	public void setTotalMarks( final int totalMarks ) {
		this.totalMarks = totalMarks;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public String getTodayMarksTitle() {
		return todayMarksTitle;
	}

	public void setTodayMarksTitle( final String todayMarksTitle ) {
		this.todayMarksTitle = todayMarksTitle;
	}

	public String getPeriodMarksTitle() {
		return periodMarksTitle;
	}

	public void setPeriodMarksTitle( final String periodMarksTitle ) {
		this.periodMarksTitle = periodMarksTitle;
	}

	public String getTotalMarksUrl() {
		return totalMarksUrl;
	}

	public void setTotalMarksUrl( final String totalMarksUrl ) {
		this.totalMarksUrl = totalMarksUrl;
	}

	public String getTotalMarksTitle() {
		return totalMarksTitle;
	}

	public void setTotalMarksTitle( final String totalMarksTitle ) {
		this.totalMarksTitle = totalMarksTitle;
	}

	public void setShowAnonymousPeriodExpirationInfo( final boolean showAnonymousPeriodExpirationInfo ) {
		this.showAnonymousPeriodExpirationInfo = showAnonymousPeriodExpirationInfo;
	}

	public boolean isShowAnonymousPeriodExpirationInfo() {
		return showAnonymousPeriodExpirationInfo;
	}

	public String getPhotoAnonymousPeriodExpirationInfo() {
		return photoAnonymousPeriodExpirationInfo;
	}

	public void setPhotoAnonymousPeriodExpirationInfo( final String photoAnonymousPeriodExpirationInfo ) {
		this.photoAnonymousPeriodExpirationInfo = photoAnonymousPeriodExpirationInfo;
	}

	public void setShowAdminFlag_Anonymous( final boolean showAdminFlag_Anonymous ) {
		this.showAdminFlag_Anonymous = showAdminFlag_Anonymous;
	}

	public boolean isShowAdminFlag_Anonymous() {
		return showAdminFlag_Anonymous;
	}

	public boolean isShowAdminFlag_Nude() {
		return showAdminFlag_Nude;
	}

	public void setShowAdminFlag_Nude( final boolean showAdminFlag_Nude ) {
		this.showAdminFlag_Nude = showAdminFlag_Nude;
	}

	public boolean isUserOwnThePhoto() {
		return userOwnThePhoto;
	}

	public void setUserOwnThePhoto( final boolean userOwnThePhoto ) {
		this.userOwnThePhoto = userOwnThePhoto;
	}

	public List<FavoriteEntryDTO> getFavoriteEntryDTOs() {
		return favoriteEntryDTOs;
	}

	public void setFavoriteEntryDTOs( final List<FavoriteEntryDTO> favoriteEntryDTOs ) {
		this.favoriteEntryDTOs = favoriteEntryDTOs;
	}
}
