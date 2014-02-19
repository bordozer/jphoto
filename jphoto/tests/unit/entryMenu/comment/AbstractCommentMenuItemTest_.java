package entryMenu.comment;

import common.AbstractTestCase;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.configuration.ConfigurationKey;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.services.entry.FavoritesService;
import core.services.entry.GenreService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;

public abstract class AbstractCommentMenuItemTest_ extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	protected void assertCommentMenuItemAccess( final CommentMenuItemAccessStrategy accessStrategy, final AbstractCommentMenuItem menuItem, final CommentInitialConditions initialConditions ) {

		final boolean isUserWhoIsOpeningMenuOwnerOfThePhoto = initialConditions.getPhotoAuthorId() == initialConditions.getUserWhoIsCallingMenuId();

		final User userWhoIsCallingMenu = new User();
		userWhoIsCallingMenu.setId( initialConditions.getUserWhoIsCallingMenuId() );

		final User commentAuthor = new User();
		commentAuthor.setId( initialConditions.getCommentAuthorId() );

		final Photo photo = new Photo();
		photo.setId( initialConditions.getPhotoId() );

		final PhotoComment photoComment = new PhotoComment();
		photoComment.setId( initialConditions.getPhotoCommentId() );
		photoComment.setCommentAuthor( commentAuthor );
		photoComment.setPhotoId( initialConditions.getPhotoId() );
		photoComment.setCommentDeleted( initialConditions.isCommentDeleted() );

		final PhotoCommentService photoCommentService = EasyMock.createMock( PhotoCommentService.class );
		EasyMock.expect( photoCommentService.load( initialConditions.getPhotoCommentId() ) ).andReturn( photoComment ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoCommentService );
		menuItem.setPhotoCommentService( photoCommentService );

		final Genre genre = new Genre();
		genre.setId( 14 ); // Does not mater

		final GenreService genreService = EasyMock.createMock( GenreService.class );
		EasyMock.expect( genreService.load( EasyMock.anyInt() ) ).andReturn( genre ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( genreService );
		menuItem.setGenreService( genreService );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.load( photo.getId() ) ).andReturn( photo ).anyTimes();
		EasyMock.expect( photoService.getPhotoQtyByUserAndGenre( commentAuthor.getId(), genre.getId() ) ).andReturn( initialConditions.getPhotoCommentAuthorPhotosQty() ).anyTimes();
		EasyMock.expect( photoService.getPhotoQtyByUser( commentAuthor.getId() ) ).andReturn( initialConditions.getPhotoCommentAuthorPhotosQty() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );
		menuItem.setPhotoService( photoService );

		menuItem.createMenuItemCommand( photoComment.getId(), userWhoIsCallingMenu );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES ) ).andReturn( initialConditions.isShowMenuGoToForOwnEntriesSettingIsSwitchedOn() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );
		menuItem.setConfigurationService( configurationService );

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );
		EasyMock.expect( securityService.userOwnThePhoto( userWhoIsCallingMenu, photo ) ).andReturn( isUserWhoIsOpeningMenuOwnerOfThePhoto ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( userWhoIsCallingMenu.getId() ) ).andReturn( initialConditions.isMenuCallerSuperAdmin() ).anyTimes();
		EasyMock.expect( securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( photoComment, userWhoIsCallingMenu ) ).andReturn( initialConditions.isAnonymousPeriod() ).anyTimes();
		EasyMock.expect( securityService.isPhotoAuthorNameMustBeHidden( photo, initialConditions.getUserWhoIsCallingMenu() ) ).andReturn( false ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );
		menuItem.setSecurityService( securityService );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoComment.getCommentAuthor().getId(), userWhoIsCallingMenu.getId() ) ).andReturn( initialConditions.isMenuCallerInBlackListOfCommentAuthor() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );
		menuItem.setFavoritesService( favoritesService );

		final User photoAuthor = new User();
		photoAuthor.setId( initialConditions.getPhotoAuthorId() );
		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( photo.getUserId() ) ).andReturn( photoAuthor ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );
		menuItem.setUserService( userService );

		initialConditions.setUserWhoIsCallingMenu( userWhoIsCallingMenu );
		initialConditions.setPhotoComment( photoComment );

		accessStrategy.assertMenuItemAccess( menuItem, initialConditions );
	}
}
