package entryMenu.photo;

import core.general.photo.Photo;
import core.general.user.User;
import entryMenu.Initialable;

class PhotoInitialConditions implements Initialable {

	private final int userWhoIsCallingMenuId;
	private final int photoId;

	private User userWhoIsCallingMenu;
	private Photo photo;
	private boolean userWhoIsCallingMenuInTheBlackList;
	private boolean showMenuGoToForOwnEntriesSettingIsSwitchedOn;
	private int photoAuthorPhotosQty;
	private int photoAuthorId;
	private boolean userWhoIsOpeningMenuOwnerOfThePhoto;
	private boolean menuCallerSuperAdmin;
	private boolean photoAuthorNameMustBeHidden;
	private boolean userCanDeletePhoto;
	private boolean userCanEditPhoto;
	private int userPhotoAlbumPhotosQty;
	private int photoQtyByUserAndGenre;
	private int teamMemberPhotosQty;

	PhotoInitialConditions( final int userWhoIsCallingMenuId, final int photoId ) {
		this.userWhoIsCallingMenuId = userWhoIsCallingMenuId;
		this.photoId = photoId;
	}

	public User getUserWhoIsCallingMenu() {
		return userWhoIsCallingMenu;
	}

	public void setUserWhoIsCallingMenu( final User userWhoIsCallingMenu ) {
		this.userWhoIsCallingMenu = userWhoIsCallingMenu;
	}

	public int getUserWhoIsCallingMenuId() {
		return userWhoIsCallingMenuId;
	}

	public int getPhotoId() {
		return photoId;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public boolean isUserWhoIsCallingMenuInTheBlackList() {
		return userWhoIsCallingMenuInTheBlackList;
	}

	public boolean isShowMenuGoToForOwnEntriesSettingIsSwitchedOn() {
		return showMenuGoToForOwnEntriesSettingIsSwitchedOn;
	}

	public int getPhotoAuthorPhotosQty() {
		return photoAuthorPhotosQty;
	}

	public void setUserWhoIsCallingMenuInTheBlackList( final boolean userWhoIsCallingMenuInTheBlackList ) {
		this.userWhoIsCallingMenuInTheBlackList = userWhoIsCallingMenuInTheBlackList;
	}

	public void setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( final boolean showMenuGoToForOwnEntriesSettingIsSwitchedOn ) {
		this.showMenuGoToForOwnEntriesSettingIsSwitchedOn = showMenuGoToForOwnEntriesSettingIsSwitchedOn;
	}

	public void setPhotoAuthorPhotosQty( final int photoAuthorPhotosQty ) {
		this.photoAuthorPhotosQty = photoAuthorPhotosQty;
	}

	public int getPhotoAuthorId() {
		return photoAuthorId;
	}

	public void setPhotoAuthorId( final int photoAuthorId ) {
		this.photoAuthorId = photoAuthorId;
	}

	public boolean isUserWhoIsOpeningMenuOwnerOfThePhoto() {
		return userWhoIsOpeningMenuOwnerOfThePhoto;
	}

	public void setUserWhoIsOpeningMenuOwnerOfThePhoto( final boolean userWhoIsOpeningMenuOwnerOfThePhoto ) {
		this.userWhoIsOpeningMenuOwnerOfThePhoto = userWhoIsOpeningMenuOwnerOfThePhoto;
	}

	public boolean isMenuCallerSuperAdmin() {
		return menuCallerSuperAdmin;
	}

	public void setMenuCallerSuperAdmin( final boolean menuCallerSuperAdmin ) {
		this.menuCallerSuperAdmin = menuCallerSuperAdmin;
	}

	public boolean isPhotoAuthorNameMustBeHidden() {
		return photoAuthorNameMustBeHidden;
	}

	public void setPhotoAuthorNameMustBeHidden( final boolean photoAuthorNameMustBeHidden ) {
		this.photoAuthorNameMustBeHidden = photoAuthorNameMustBeHidden;
	}

	public boolean isUserCanDeletePhoto() {
		return userCanDeletePhoto;
	}

	public void setUserCanDeletePhoto( final boolean userCanDeletePhoto ) {
		this.userCanDeletePhoto = userCanDeletePhoto;
	}

	public boolean isUserCanEditPhoto() {
		return userCanEditPhoto;
	}

	public void setUserCanEditPhoto( final boolean userCanEditPhoto ) {
		this.userCanEditPhoto = userCanEditPhoto;
	}

	public int getUserPhotoAlbumPhotosQty() {
		return userPhotoAlbumPhotosQty;
	}

	public void setUserPhotoAlbumPhotosQty( final int userPhotoAlbumPhotosQty ) {
		this.userPhotoAlbumPhotosQty = userPhotoAlbumPhotosQty;
	}

	public int getPhotoQtyByUserAndGenre() {
		return photoQtyByUserAndGenre;
	}

	public void setPhotoQtyByUserAndGenre( final int photoQtyByUserAndGenre ) {
		this.photoQtyByUserAndGenre = photoQtyByUserAndGenre;
	}

	public Integer getTeamMemberPhotosQty() {
		return teamMemberPhotosQty;
	}

	public void setTeamMemberPhotosQty( final int teamMemberPhotosQty ) {
		this.teamMemberPhotosQty = teamMemberPhotosQty;
	}
}
