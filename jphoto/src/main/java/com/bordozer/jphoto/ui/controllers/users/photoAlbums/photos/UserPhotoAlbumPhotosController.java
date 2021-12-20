package com.bordozer.jphoto.ui.controllers.users.photoAlbums.photos;

import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.services.photo.list.PhotoListFactoryService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.user.UserPhotoAlbumService;
import com.bordozer.jphoto.core.services.user.UserService;
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

@Controller
@RequestMapping("members/{userId}/albums/{albumId}")
public class UserPhotoAlbumPhotosController {

    private static final String MODEL_NAME = "userPhotoAlbumPhotosModel";
    private static final String VIEW = "users/photoAlbums/photos/PhotoAlbumPhotos";

    @Autowired
    private UserPhotoAlbumService userPhotoAlbumService;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private BreadcrumbsUserService breadcrumbsUserService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Autowired
    private Services services;

    @Autowired
    private PhotoListFactoryService photoListFactoryService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @ModelAttribute(MODEL_NAME)
    public UserPhotoAlbumPhotosModel prepareModel() {
        return new UserPhotoAlbumPhotosModel();
    }

    @ModelAttribute("pagingModel")
    public PagingModel preparePagingModel(final HttpServletRequest request) {
        final PagingModel pagingModel = new PagingModel(services);
        PagingUtils.initPagingModel(pagingModel, request);

        pagingModel.setItemsOnPage(utilsService.getPhotosOnPage(EnvironmentContext.getCurrentUser()));

        return pagingModel;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String albumPhotos(final @PathVariable("userId") String _userId, final @PathVariable("albumId") int albumId, final @ModelAttribute(MODEL_NAME) UserPhotoAlbumPhotosModel model
            , final @ModelAttribute("pagingModel") PagingModel pagingModel, final HttpServletRequest request) {

        securityService.assertUserExists(_userId);

        final int userId = NumberUtils.convertToInt(_userId);
        final User user = userService.load(userId);

        final UserPhotoAlbum photoAlbum = userPhotoAlbumService.load(albumId);

        model.setPhotoAlbum(photoAlbum);

        final PhotoList photoList = photoListFactoryService.userAlbumPhotos(user, photoAlbum, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser())
                .getPhotoList(photoAlbum.getId(), pagingModel.getCurrentPage(), EnvironmentContext.getLanguage(), dateUtilsService.getCurrentTime());

        photoList.setSelectedPhotoListViewModeType(PhotoListViewModeType.getById(request.getParameter("mode")));

        model.setPhotoList(photoList);

        pagingModel.setTotalItems(photoList.getPhotosCount());

        model.setPageTitleData(breadcrumbsUserService.getUserPhotoAlbumPhotosBreadcrumbs(photoAlbum));

        return VIEW;
    }
}
