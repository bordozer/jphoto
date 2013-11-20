package entryMenu.user;

import core.general.user.User;
import entryMenu.Initialable;

class UserInitialConditions implements Initialable {

	private final int userId;
	private final int userWhoIsCallingMenuId;

	private User user;
	private User userWhoIsCallingMenu;
	private boolean userWhoIsCallingMenuInTheBlackList;
	private boolean showMenuGoToForOwnEntriesSettingIsSwitchedOn;
	private Integer userPhotosQty;
	private boolean menuCallerSuperAdmin;

	public UserInitialConditions( final int userId, final int userWhoIsCallingMenuId ) {
		this.userId = userId;
		this.userWhoIsCallingMenuId = userWhoIsCallingMenuId;
	}

	public int getUserId() {
		return userId;
	}

	public int getUserWhoIsCallingMenuId() {
		return userWhoIsCallingMenuId;
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public User getUserWhoIsCallingMenu() {
		return userWhoIsCallingMenu;
	}

	public void setUserWhoIsCallingMenu( final User userWhoIsCallingMenu ) {
		this.userWhoIsCallingMenu = userWhoIsCallingMenu;
	}

	public boolean isUserWhoIsCallingMenuInTheBlackList() {
		return userWhoIsCallingMenuInTheBlackList;
	}

	public void setUserWhoIsCallingMenuInTheBlackList( final boolean userWhoIsCallingMenuInTheBlackList ) {
		this.userWhoIsCallingMenuInTheBlackList = userWhoIsCallingMenuInTheBlackList;
	}

	public boolean isShowMenuGoToForOwnEntriesSettingIsSwitchedOn() {
		return showMenuGoToForOwnEntriesSettingIsSwitchedOn;
	}

	public void setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( final boolean showMenuGoToForOwnEntriesSettingIsSwitchedOn ) {
		this.showMenuGoToForOwnEntriesSettingIsSwitchedOn = showMenuGoToForOwnEntriesSettingIsSwitchedOn;
	}

	public Integer getUserPhotosQty() {
		return userPhotosQty;
	}

	public void setUserPhotosQty( final Integer userPhotosQty ) {
		this.userPhotosQty = userPhotosQty;
	}

	public boolean isMenuCallerSuperAdmin() {
		return menuCallerSuperAdmin;
	}

	public void setMenuCallerSuperAdmin( final boolean menuCallerSuperAdmin ) {
		this.menuCallerSuperAdmin = menuCallerSuperAdmin;
	}
}
