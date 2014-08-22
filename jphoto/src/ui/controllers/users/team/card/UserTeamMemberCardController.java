package ui.controllers.users.team.card;

import core.general.base.PagingModel;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.services.entry.GroupOperationService;
import core.services.photo.PhotoService;
import core.services.photo.list.PhotoListFactoryService;
import core.services.security.SecurityService;
import core.services.system.Services;
import core.services.user.UserService;
import core.services.user.UserTeamService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.elements.PhotoList;
import ui.services.UtilsService;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import utils.NumberUtils;
import utils.PagingUtils;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( "members/{userId}/team" )
public class UserTeamMemberCardController {

	private static final String MODEL_NAME = "userTeamMemberCardModel";
	private static final String VIEW = "users/team/card/UserTeamMemberCard";

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UtilsService utilsService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoListFactoryService photoListFactoryService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private Services services;

	@ModelAttribute( MODEL_NAME )
	public UserTeamMemberCardModel prepareModel() {
		return new UserTeamMemberCardModel();
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel( services );
		PagingUtils.initPagingModel( pagingModel, request );

		pagingModel.setItemsOnPage( utilsService.getPhotosOnPage( EnvironmentContext.getCurrentUser() ) );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{userTeamMemberId}/" )
	public String userTeamMemberCard( final @PathVariable( "userId" ) String _userId, final @PathVariable( "userTeamMemberId" ) int userTeamMemberId, final @ModelAttribute( MODEL_NAME ) UserTeamMemberCardModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );
		final User user = userService.load( userId );

		final UserTeamMember userTeamMember = userTeamService.load( userTeamMemberId );

		model.setUserTeamMember( userTeamMember );

		final PhotoList photoList = photoListFactoryService.userTeamMemberPhotos( user, userTeamMember, pagingModel.getCurrentPage(), EnvironmentContext.getCurrentUser() ).getPhotoList( userTeamMember.getId(), pagingModel.getCurrentPage(), EnvironmentContext.getLanguage(), dateUtilsService.getCurrentTime() );

		model.setPhotoLists( newArrayList( photoList ) );

		pagingModel.setTotalItems( photoList.getPhotosCount() );

		model.setPageTitleData( breadcrumbsUserService.getUserTeamMemberCardBreadcrumbs( userTeamMember ) );

		return VIEW;
	}
}
