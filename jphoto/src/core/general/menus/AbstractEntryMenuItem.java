package core.general.menus;

import core.general.user.User;
import core.general.configuration.ConfigurationKey;
import core.services.entry.EntryMenuService;
import core.services.entry.FavoritesService;
import core.services.entry.GenreService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserService;
import core.services.user.UserTeamService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import utils.UserUtils;

public abstract class AbstractEntryMenuItem {

	public static final String COMPLAINT_MESSAGE_JS_FUNCTION = "sendComplaintMessage";

	public static final int MENU_ITEM_HEIGHT = 27;
	public static final int MENU_SEPARATOR_HEIGHT = 5;

	protected AbstractEntryMenuItemCommand menuItemCommand;

	@Autowired
	protected UserService userService;

	@Autowired
	protected PhotoService photoService;

	@Autowired
	protected SecurityService securityService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	protected GenreService genreService;

	@Autowired
	protected FavoritesService favoritesService;

	@Autowired
	protected UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	protected UserTeamService userTeamService;

	@Autowired
	protected PhotoCommentService photoCommentService;

	@Autowired
	protected UrlUtilsService urlUtilsService;

	@Autowired
	protected EntryMenuService entryMenuService;

	public abstract EntryMenuOperationType getEntryMenuType();

	protected abstract AbstractEntryMenuItemCommand initMenuItemCommand( final int entryId, final User userWhoIsCallingMenu );

	public boolean isSubMenu() {
		return getEntryMenuType().isSubMenu();
	}

	public final void createMenuItemCommand( final int entryId, final User userWhoIsCallingMenu ) {
		menuItemCommand = initMenuItemCommand( entryId, userWhoIsCallingMenu );
	}

	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		return menuItemCommand;
	}

	final protected boolean isUserWhoIsCallingMenuLogged( final User userWhoIsCallingMenu ) {
		return UserUtils.isLoggedUser( userWhoIsCallingMenu );
	}

	final protected boolean isSuperAdminUser( final int userId ) {
		return securityService.isSuperAdminUser( userId );
	}

	final protected boolean isSuperAdminUser( final User user ) {
		return isSuperAdminUser( user.getId() );
	}

	protected boolean isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() {
		return configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES );
	}

	protected boolean isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() {
		return ! isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn();
	}

	public void setSecurityService( final SecurityService securityService ) {
		this.securityService = securityService;
	}

	public void setConfigurationService( final ConfigurationService configurationService ) {
		this.configurationService = configurationService;
	}

	public void setUserService( final UserService userService ) {
		this.userService = userService;
	}

	public void setPhotoService( final PhotoService photoService ) {
		this.photoService = photoService;
	}

	public void setGenreService( final GenreService genreService ) {
		this.genreService = genreService;
	}

	public void setFavoritesService( final FavoritesService favoritesService ) {
		this.favoritesService = favoritesService;
	}

	public void setUserPhotoAlbumService( final UserPhotoAlbumService userPhotoAlbumService ) {
		this.userPhotoAlbumService = userPhotoAlbumService;
	}

	public void setUserTeamService( final UserTeamService userTeamService ) {
		this.userTeamService = userTeamService;
	}

	public void setPhotoCommentService( final PhotoCommentService photoCommentService ) {
		this.photoCommentService = photoCommentService;
	}

	@Override
	public String toString() {
		return String.format( "Entry menu: %s", getMenuItemCommand() != null ? getMenuItemCommand().getMenuText() : getEntryMenuType() );
	}

	public int getHeight() {
		return MENU_ITEM_HEIGHT;
	}
}
