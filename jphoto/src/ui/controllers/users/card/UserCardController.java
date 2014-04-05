package ui.controllers.users.card;

import ui.controllers.activity.list.ActivityStreamController;
import ui.controllers.users.card.data.AbstractUserCardModelFillStrategy;
import ui.controllers.users.card.data.UserCardModelFillService;
import core.context.EnvironmentContext;
import core.enums.UserCardTab;
import core.general.base.PagingModel;
import core.general.user.User;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import core.services.security.SecurityService;
import core.services.system.Services;
import core.services.user.UserService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.DeviceType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import utils.NumberUtils;
import utils.PagingUtils;

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
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private Services services;

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
		final PagingModel pagingModel = new PagingModel( services );

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

	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/card/activity/type/{activityTypeId}/" )
	public String userActivityTab( final @PathVariable( "userId" ) String _userId, final @PathVariable( "activityTypeId" ) String _activityTypeId
		, final @ModelAttribute( "userCardModel" ) UserCardModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final UserCardTab tab = UserCardTab.ACTIVITY_STREAM;

		final int activityTypeId = NumberUtils.isNumeric( _activityTypeId ) ? NumberUtils.convertToInt( _activityTypeId ) : 0;
		model.setFilterActivityTypeId( activityTypeId );

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

		model.setPageTitleData( breadcrumbsUserService.getUserCardBreadcrumbs( user, userCardTab ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/tech/" )
	public String userTech() {
		return UrlUtilsServiceImpl.UNDER_CONSTRUCTION_VIEW;
	}
}