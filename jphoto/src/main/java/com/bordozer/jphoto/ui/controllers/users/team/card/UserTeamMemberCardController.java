package com.bordozer.jphoto.ui.controllers.users.team.card;

import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.list.PhotoListFactoryService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.elements.PhotoList;
import com.bordozer.jphoto.ui.services.UtilsService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsUserService;
import com.bordozer.jphoto.ui.viewModes.PhotoListViewModeType;
import com.bordozer.jphoto.utils.NumberUtils;
import com.bordozer.jphoto.utils.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping("members/{userId}/team")
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

    @ModelAttribute(MODEL_NAME)
    public UserTeamMemberCardModel prepareModel() {
        return new UserTeamMemberCardModel();
    }

    @ModelAttribute("pagingModel")
    public PagingModel preparePagingModel(final HttpServletRequest request) {
        final PagingModel pagingModel = new PagingModel(services);
        PagingUtils.initPagingModel(pagingModel, request);

        pagingModel.setItemsOnPage(utilsService.getPhotosOnPage(EnvironmentContext.getCurrentUser()));

        return pagingModel;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userTeamMemberId}/")
    public String userTeamMemberCard(final @PathVariable("userId") String _userId, final @PathVariable("userTeamMemberId") int userTeamMemberId
            , final @ModelAttribute(MODEL_NAME) UserTeamMemberCardModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final HttpServletRequest request) {

        securityService.assertUserExists(_userId);

        final int userId = NumberUtils.convertToInt(_userId);
        final User user = userService.load(userId);

        final UserTeamMember userTeamMember = userTeamService.load(userTeamMemberId);

        model.setUserTeamMember(userTeamMember);

        final PhotoList photoList = photoListFactoryService.userTeamMemberPhotos(user, userTeamMember, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()).getPhotoList(userTeamMember.getId(), pagingModel.getCurrentPage(), EnvironmentContext.getLanguage(), dateUtilsService.getCurrentTime());
        photoList.setSelectedPhotoListViewModeType(PhotoListViewModeType.getById(request.getParameter("mode")));

        model.setPhotoLists(newArrayList(photoList));

        pagingModel.setTotalItems(photoList.getPhotosCount());

        model.setPageTitleData(breadcrumbsUserService.getUserTeamMemberCardBreadcrumbs(userTeamMember));

        return VIEW;
    }
}
