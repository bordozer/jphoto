package com.bordozer.jphoto.ui.controllers.users.bookmarks;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.GroupOperationService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.list.PhotoListFactoryService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.core.services.utils.sql.PhotoQueryService;
import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.controllers.photos.list.PhotoListController;
import com.bordozer.jphoto.ui.controllers.photos.list.PhotoListModel;
import com.bordozer.jphoto.ui.elements.PhotoList;
import com.bordozer.jphoto.ui.services.UtilsService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsUserService;
import com.bordozer.jphoto.utils.NumberUtils;
import com.bordozer.jphoto.utils.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(UrlUtilsServiceImpl.USERS_URL)
public class UserBookmarksController {

    private static final String VIEW = PhotoListController.VIEW;

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BreadcrumbsUserService breadcrumbsUserService;

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private PhotoQueryService photoQueryService;

    @Autowired
    private GroupOperationService groupOperationService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private PhotoListFactoryService photoListFactoryService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private Services services;

    @ModelAttribute("photoListModel")
    public PhotoListModel prepareModel() {
        final PhotoListModel model = new PhotoListModel();
        model.setShowPhotoSearchForm(false);

        model.setDeviceType(EnvironmentContext.getDeviceType());

        return model;
    }

    @ModelAttribute
    public PagingModel preparePagingModel(final HttpServletRequest request) {
        final PagingModel pagingModel = new PagingModel(services);
        PagingUtils.initPagingModel(pagingModel, request);

        pagingModel.setItemsOnPage(utilsService.getPhotosOnPage(EnvironmentContext.getCurrentUser()));

        return pagingModel;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/favorites/photos/")
    public String showFavoritePhotos(final @PathVariable("userId") String _userId, final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel) {
        return getView(_userId, model, pagingModel, FavoriteEntryType.FAVORITE_PHOTOS);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/bookmark/")
    public String showBookmarkedPhotos(@PathVariable("userId") String _userId, @ModelAttribute("photoListModel") PhotoListModel model, @ModelAttribute("pagingModel") PagingModel pagingModel) {
        return getView(_userId, model, pagingModel, FavoriteEntryType.BOOKMARKED_PHOTOS);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/notification/comments/")
    public String showPhotosWithNewCommentNotification(@PathVariable("userId") String _userId, @ModelAttribute("photoListModel") PhotoListModel model, @ModelAttribute("pagingModel") PagingModel pagingModel) {
        return getView(_userId, model, pagingModel, FavoriteEntryType.NEW_COMMENTS_NOTIFICATION);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/favorites/members/photos/")
    public String showPhotosOfFavoriteMembers(final @PathVariable("userId") String _userId, final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel) {
        securityService.assertUserExists(_userId);

        final User user = userService.load(NumberUtils.convertToInt(_userId));

        final int page = pagingModel.getCurrentPage();
        final PhotoList photoList = photoListFactoryService.photosOfFavoriteAuthorsOfUser(user, page, pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()).getPhotoList(0, page, EnvironmentContext.getLanguage(), dateUtilsService.getCurrentTime());
        pagingModel.setTotalItems(photoList.getPhotosCount());

        model.addPhotoList(photoList);
        model.setPageTitleData(breadcrumbsUserService.getPhotosOfUserFavoriteMembersBreadcrumb(user));

        return VIEW;
		/*securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final SqlIdsSelectQuery selectQuery = photoSqlHelperService.getPhotosOfUserFavoritesMembersSQL( pagingModel, userId );
//		initFavorites( selectQuery, userId, model, pagingModel, FavoriteEntryType.FAVORITE_PHOTOS );

		final User user = userService.load( userId );
		model.setPageTitleData( breadcrumbsUserService.getPhotosOfUserFavoriteMembersBreadcrumb( user ) );

		return VIEW;*/
    }

    private String getView(final String _userId, final PhotoListModel model, final PagingModel pagingModel, final FavoriteEntryType favoriteEntryType) {
        securityService.assertUserExists(_userId);

        final User user = userService.load(NumberUtils.convertToInt(_userId));

        final int page = pagingModel.getCurrentPage();
        final PhotoList photoList = photoListFactoryService.userBookmarkedPhotos(user, favoriteEntryType, page, pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()).getPhotoList(0, page, EnvironmentContext.getLanguage(), dateUtilsService.getCurrentTime());
        pagingModel.setTotalItems(photoList.getPhotosCount());

        model.addPhotoList(photoList);
        model.setPageTitleData(breadcrumbsUserService.getUserFavoriteEntryListBreadcrumbs(user, favoriteEntryType));

        return VIEW;
    }

	/*private void initFavorites( final int userId, final PhotoListModel model, final PagingModel pagingModel, final FavoriteEntryType favoriteEntryType ) {
//		final SqlIdsSelectQuery selectQuery = photoSqlHelperService.getFavoritesPhotosSQL( pagingModel, userId, favoriteEntryType );
		initFavorites( selectQuery,  userId, model, pagingModel, favoriteEntryType );
	}

	private void initFavorites( final SqlIdsSelectQuery selectQuery, final int userId, final PhotoListModel model, final PagingModel pagingModel, final FavoriteEntryType entryType ) {

		model.clear();

		final List<Integer> photosIds = selectDataFromDB( pagingModel, selectQuery );

		final User user = userService.load( userId );

		final String listTitle = String.format( "%s: %s", entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ), translatorService.translate( entryType.getName(), EnvironmentContext.getLanguage() ) );

		final PhotoList photoList = new PhotoList( photosIds, listTitle ); // TODO: do not ignore photoIconsTypes or userIconsTypes
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( EnvironmentContext.getCurrentUser() ) );
		model.addPhotoList( photoList );

		model.setPageTitleData( breadcrumbsUserService.getUserFavoriteEntryListBreadcrumbs( user, entryType ) );
	}*/

    private List<Integer> selectDataFromDB(final PagingModel pagingModel, final SqlIdsSelectQuery selectIdsQuery) {
        final SqlSelectIdsResult selectResult = photoService.load(selectIdsQuery);
        pagingModel.setTotalItems(selectResult.getRecordQty());

        return selectResult.getIds();
    }
}
