package rest.photo.list;

import core.general.configuration.ConfigurationKey;

import java.util.List;
import java.util.Map;

public class PhotoEntryDTO {

	private final int userId;
	private final int photoId;

	private String groupOperationCheckbox;
	private String photoUploadDate;
	private String photoCategory;
	private String photoImage;

	private boolean showPhotoContextMenu;

	private String photoName;
	private String photoLink;
	private String photoCardLink;
	private String photoAuthorLink;

	private boolean showUserRank;
	private String photoAuthorRank;

	private boolean showStatistics;
	private int todayMarks;
	private int periodMarks;
	private String totalMarks;
	private String todayMarksTitle;
	private String periodMarksTitle;

	private String previewsIcon;
	private String commentsIcon;

	private String previewsCount;
	private String commentsCount;

	private boolean showAnonymousPeriodExpirationInfo;
	private String photoAnonymousPeriodExpirationInfo;

	private boolean showAdminFlag_Anonymous;

	private boolean showAdminFlag_Nude;

	private boolean userOwnThePhoto;
	private List<PhotoBookmarkIcon> photoBookmarkIcons;

	private boolean hidePreviewBecauseOfNudeContent;
	private String nudeContentWarning0;
	private String nudeContentWarning1;
	private String nudeContentWarning2;
	private String nudeContentWarning3;
	private String nudeContentWarning4;

	protected Map<String, SpecialIconDTO> specialRestrictionIcons;

	private boolean memberOfAlbum;
	private String photoAlbumLink;

	private boolean showPhotoListPreviewFooter;

	private String iconTitlePhotoIsInAlbum;
	private String iconTitleAnonymousPosting;
	private String iconTitleNudeContent;

	private boolean photoCategoryCanContainNudeContent;
	private boolean photoCategoryContainsNudeContent;

	private String textConfirmSettingNudeContent;
	private String textConfirmRemovingNudeContent;
	private String textCategoryCanNotContainNudeContent;
	private boolean userSuperAdmin;

	public PhotoEntryDTO( final int userId, final int photoId ) {
		this.userId = userId;
		this.photoId = photoId;
	}

	public int getUserId() {
		return userId;
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

	public String getPhotoLink() {
		return photoLink;
	}

	public void setPhotoLink( final String photoLink ) {
		this.photoLink = photoLink;
	}

	public String getPhotoCardLink() {
		return photoCardLink;
	}

	public void setPhotoCardLink( final String photoCardLink ) {
		this.photoCardLink = photoCardLink;
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

	public void setTotalMarks( final String totalMarks ) {
		this.totalMarks = totalMarks;
	}

	public String getTotalMarks() {
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

	public List<PhotoBookmarkIcon> getPhotoBookmarkIcons() {
		return photoBookmarkIcons;
	}

	public void setPhotoBookmarkIcons( final List<PhotoBookmarkIcon> photoBookmarkIcons ) {
		this.photoBookmarkIcons = photoBookmarkIcons;
	}

	public String getPreviewsIcon() {
		return previewsIcon;
	}

	public void setPreviewsIcon( final String previewsIcon ) {
		this.previewsIcon = previewsIcon;
	}

	public String getCommentsIcon() {
		return commentsIcon;
	}

	public void setCommentsIcon( final String commentsIcon ) {
		this.commentsIcon = commentsIcon;
	}

	public String getPreviewsCount() {
		return previewsCount;
	}

	public void setPreviewsCount( final String previewsCount ) {
		this.previewsCount = previewsCount;
	}

	public String getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount( final String commentsCount ) {
		this.commentsCount = commentsCount;
	}

	public Map<String, SpecialIconDTO> getSpecialRestrictionIcons() {
		return specialRestrictionIcons;
	}

	public void setSpecialRestrictionIcons( final Map<String, SpecialIconDTO> specialRestrictionIcons ) {
		this.specialRestrictionIcons = specialRestrictionIcons;
	}

	public boolean isHidePreviewBecauseOfNudeContent() {
		return hidePreviewBecauseOfNudeContent;
	}

	public void setHidePreviewBecauseOfNudeContent( final boolean hidePreviewBecauseOfNudeContent ) {
		this.hidePreviewBecauseOfNudeContent = hidePreviewBecauseOfNudeContent;
	}

	public String getNudeContentWarning0() {
		return nudeContentWarning0;
	}

	public void setNudeContentWarning0( final String nudeContentWarning0 ) {
		this.nudeContentWarning0 = nudeContentWarning0;
	}

	public String getNudeContentWarning1() {
		return nudeContentWarning1;
	}

	public void setNudeContentWarning1( final String nudeContentWarning1 ) {
		this.nudeContentWarning1 = nudeContentWarning1;
	}

	public String getNudeContentWarning2() {
		return nudeContentWarning2;
	}

	public void setNudeContentWarning2( final String nudeContentWarning2 ) {
		this.nudeContentWarning2 = nudeContentWarning2;
	}

	public String getNudeContentWarning3() {
		return nudeContentWarning3;
	}

	public void setNudeContentWarning3( final String nudeContentWarning3 ) {
		this.nudeContentWarning3 = nudeContentWarning3;
	}

	public String getNudeContentWarning4() {
		return nudeContentWarning4;
	}

	public void setNudeContentWarning4( final String nudeContentWarning4 ) {
		this.nudeContentWarning4 = nudeContentWarning4;
	}

	public boolean isMemberOfAlbum() {
		return memberOfAlbum;
	}

	public void setMemberOfAlbum( final boolean memberOfAlbum ) {
		this.memberOfAlbum = memberOfAlbum;
	}

	public String getPhotoAlbumLink() {
		return photoAlbumLink;
	}

	public void setPhotoAlbumLink( final String photoAlbumLink ) {
		this.photoAlbumLink = photoAlbumLink;
	}

	public void setShowPhotoListPreviewFooter( final boolean showPhotoListPreviewFooter ) {
		this.showPhotoListPreviewFooter = showPhotoListPreviewFooter;
	}

	public boolean isShowPhotoListPreviewFooter() {
		return showPhotoListPreviewFooter;
	}

	public String getIconTitlePhotoIsInAlbum() {
		return iconTitlePhotoIsInAlbum;
	}

	public void setIconTitlePhotoIsInAlbum( final String iconTitlePhotoIsInAlbum ) {
		this.iconTitlePhotoIsInAlbum = iconTitlePhotoIsInAlbum;
	}

	public String getIconTitleNudeContent() {
		return iconTitleNudeContent;
	}

	public void setIconTitleNudeContent( final String iconTitleNudeContent ) {
		this.iconTitleNudeContent = iconTitleNudeContent;
	}

	public String getIconTitleAnonymousPosting() {
		return iconTitleAnonymousPosting;
	}

	public void setIconTitleAnonymousPosting( final String iconTitleAnonymousPosting ) {
		this.iconTitleAnonymousPosting = iconTitleAnonymousPosting;
	}

	public void setPhotoCategoryCanContainNudeContent( final boolean photoCategoryCanContainNudeContent ) {
		this.photoCategoryCanContainNudeContent = photoCategoryCanContainNudeContent;
	}

	public boolean isPhotoCategoryCanContainNudeContent() {
		return photoCategoryCanContainNudeContent;
	}

	public void setPhotoCategoryContainsNudeContent( final boolean photoCategoryContainsNudeContent ) {
		this.photoCategoryContainsNudeContent = photoCategoryContainsNudeContent;
	}

	public boolean isPhotoCategoryContainsNudeContent() {
		return photoCategoryContainsNudeContent;
	}

	public void setTextConfirmSettingNudeContent( final String textConfirmSettingNudeContent ) {
		this.textConfirmSettingNudeContent = textConfirmSettingNudeContent;
	}

	public String getTextConfirmSettingNudeContent() {
		return textConfirmSettingNudeContent;
	}

	public void setTextConfirmRemovingNudeContent( final String textConfirmRemovingNudeContent ) {
		this.textConfirmRemovingNudeContent = textConfirmRemovingNudeContent;
	}

	public String getTextConfirmRemovingNudeContent() {
		return textConfirmRemovingNudeContent;
	}

	public void setTextCategoryCanNotContainNudeContent( final String textCategoryCanNotContainNudeContent ) {
		this.textCategoryCanNotContainNudeContent = textCategoryCanNotContainNudeContent;
	}

	public String getTextCategoryCanNotContainNudeContent() {
		return textCategoryCanNotContainNudeContent;
	}

	public void setUserSuperAdmin( final boolean userSuperAdmin ) {
		this.userSuperAdmin = userSuperAdmin;
	}

	public boolean isUserSuperAdmin() {
		return userSuperAdmin;
	}
}
