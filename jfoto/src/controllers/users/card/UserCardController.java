package controllers.users.card;

import controllers.activity.list.ActivityStreamController;
import core.general.base.PagingModel;
import core.enums.UserCardTab;
import core.context.EnvironmentContext;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.user.UserService;
import core.services.utils.UrlUtilsServiceImpl;
import controllers.users.card.data.AbstractUserCardModelFillStrategy;
import controllers.users.card.data.UserCardModelFillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.DeviceType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import utils.*;
import core.services.pageTitle.PageTitleUserUtilsService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping( UrlUtilsServiceImpl.USERS_URL )
public class UserCardController {

	private static final String VIEW = "users/card/UserCard";
	private static final String VIEW_BRIEF = "users/card/UserCardBrief";

	@Autowired
	private UserService userService;

	@Autowired
	private UserCardValidator userCardValidator;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserCardModelFillService userCardModelFillService;

	@Autowired
	private PageTitleUserUtilsService pageTitleUserUtilsService;

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( userCardValidator );
	}

	@ModelAttribute( "userCardModel" )
	public UserCardModel prepareModel() {
		return new UserCardModel();
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel();

		PagingUtils.initPagingModel( pagingModel, request );
		pagingModel.setItemsOnPage( ActivityStreamController.ACTIVITY_STREAM_ITEMS_ON_PAGE );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/card/" )
	public String userCard( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "userCardModel" ) UserCardModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		userCardTab( _userId, UserCardTab.getDefaultUserCardTab(), model, pagingModel );

		return VIEW_BRIEF;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/card/{userCardTabId}/" )
	public String userCardTab( final @PathVariable( "userId" ) String _userId, final @PathVariable( "userCardTabId" ) String userCardTabId
		, final @ModelAttribute( "userCardModel" ) UserCardModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final UserCardTab tab = UserCardTab.getById( userCardTabId );

		userCardTab( _userId, tab, model, pagingModel );

		return tab.isDefaultTab() ? VIEW_BRIEF : VIEW;
	}

	private void userCardTab( final String _userId, final UserCardTab userCardTab, final UserCardModel model, final PagingModel pagingModel ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );
		final DeviceType deviceType = EnvironmentContext.getDeviceType();

		model.clear();
		model.setDeviceType( deviceType );
		model.setSelectedUserCardTab( userCardTab );

		final User user = userService.load( userId );
		model.setUser( user );

		model.setEditingUserDataIsAccessible( securityService.userCanEditUserData( EnvironmentContext.getCurrentUser(), user ) );

		final AbstractUserCardModelFillStrategy fillStrategy = AbstractUserCardModelFillStrategy.getInstance( model, userCardTab, pagingModel, userCardModelFillService );
		fillStrategy.fillModel();

		model.setPageTitleData( pageTitleUserUtilsService.getUserCardData( user, userCardTab ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/tech/" )
	public String userTech() {
		return UrlUtilsServiceImpl.UNDER_CONSTRUCTION_VIEW;
	}
}
