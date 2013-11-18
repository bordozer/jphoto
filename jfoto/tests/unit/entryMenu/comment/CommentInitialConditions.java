package entryMenu.comment;

import core.general.photo.PhotoComment;
import core.general.user.User;
import entryMenu.Initialable;

class CommentInitialConditions implements Initialable {

	private final int userWhoIsCallingMenuId;
	private final int photoId;
	private final int photoCommentId;
	private final int commentAuthorId;
	private final int photoAuthorId;

	private final boolean anonymousPeriod;
	private final boolean menuCallerInBlackListOfCommentAuthor;

	private final boolean menuCallerSuperAdmin;
	private PhotoComment photoComment;

	private User userWhoIsCallingMenu;
	private int photoCommentAuthorPhotosQty;
	private boolean showMenuGoToForOwnEntriesSettingIsSwitchedOn;

	public CommentInitialConditions( final int userWhoIsCallingMenuId, final int photoId, final int photoCommentId, final int commentAuthorId, final int photoAuthorId, final boolean anonymousPeriod, final boolean menuCallerInBlackListOfCommentAuthor, final boolean menuCallerSuperAdmin ) {
		this.userWhoIsCallingMenuId = userWhoIsCallingMenuId;
		this.photoId = photoId;
		this.photoCommentId = photoCommentId;
		this.commentAuthorId = commentAuthorId;
		this.photoAuthorId = photoAuthorId;
		this.anonymousPeriod = anonymousPeriod;
		this.menuCallerInBlackListOfCommentAuthor = menuCallerInBlackListOfCommentAuthor;
		this.menuCallerSuperAdmin = menuCallerSuperAdmin;
	}

	public int getUserWhoIsCallingMenuId() {
		return userWhoIsCallingMenuId;
	}

	public int getPhotoId() {
		return photoId;
	}

	public int getPhotoCommentId() {
		return photoCommentId;
	}

	public int getCommentAuthorId() {
		return commentAuthorId;
	}

	public boolean isAnonymousPeriod() {
		return anonymousPeriod;
	}

	public boolean isMenuCallerInBlackListOfCommentAuthor() {
		return menuCallerInBlackListOfCommentAuthor;
	}

	public boolean isMenuCallerSuperAdmin() {
		return menuCallerSuperAdmin;
	}

	public PhotoComment getPhotoComment() {
		return photoComment;
	}

	public void setPhotoComment( final PhotoComment photoComment ) {
		this.photoComment = photoComment;
	}

	public User getUserWhoIsCallingMenu() {
		return userWhoIsCallingMenu;
	}

	public void setUserWhoIsCallingMenu( final User userWhoIsCallingMenu ) {
		this.userWhoIsCallingMenu = userWhoIsCallingMenu;
	}

	public int getPhotoCommentAuthorPhotosQty() {
		return photoCommentAuthorPhotosQty;
	}

	public void setPhotoCommentAuthorPhotosQty( final int photoCommentAuthorPhotosQty ) {
		this.photoCommentAuthorPhotosQty = photoCommentAuthorPhotosQty;
	}

	public int getPhotoAuthorId() {
		return photoAuthorId;
	}

	public boolean isShowMenuGoToForOwnEntriesSettingIsSwitchedOn() {
		return showMenuGoToForOwnEntriesSettingIsSwitchedOn;
	}

	public void setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( final boolean showMenuGoToForOwnEntriesSettingIsSwitchedOn ) {
		this.showMenuGoToForOwnEntriesSettingIsSwitchedOn = showMenuGoToForOwnEntriesSettingIsSwitchedOn;
	}
}
