package com.bordozer.jphoto.ui.controllers.photos.list;

import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.services.dao.PhotoCommentDaoImpl;
import com.bordozer.jphoto.core.services.dao.PhotoDaoImpl;
import com.bordozer.jphoto.core.services.dao.PhotoPreviewDaoImpl;
import com.bordozer.jphoto.core.services.dao.PhotoVotingDaoImpl;
import com.bordozer.jphoto.core.services.dao.UserDaoImpl;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.entry.GroupOperationService;
import com.bordozer.jphoto.core.services.entry.VotingCategoryService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.list.PhotoListFactoryService;
import com.bordozer.jphoto.core.services.photo.list.factory.AbstractPhotoListFactory;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.core.services.utils.sql.BaseSqlUtilsService;
import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.builder.SqlBuildable;
import com.bordozer.jphoto.sql.builder.SqlColumnAggregate;
import com.bordozer.jphoto.sql.builder.SqlColumnSelect;
import com.bordozer.jphoto.sql.builder.SqlColumnSelectable;
import com.bordozer.jphoto.sql.builder.SqlCondition;
import com.bordozer.jphoto.sql.builder.SqlConditionList;
import com.bordozer.jphoto.sql.builder.SqlCriteriaOperator;
import com.bordozer.jphoto.sql.builder.SqlFunctions;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlJoin;
import com.bordozer.jphoto.sql.builder.SqlJoinCondition;
import com.bordozer.jphoto.sql.builder.SqlLogicallyJoinable;
import com.bordozer.jphoto.sql.builder.SqlLogicallyOr;
import com.bordozer.jphoto.sql.builder.SqlSortOrder;
import com.bordozer.jphoto.sql.builder.SqlTable;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.controllers.photos.edit.GenreWrapper;
import com.bordozer.jphoto.ui.elements.PhotoList;
import com.bordozer.jphoto.ui.services.UtilsService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsPhotoGalleryService;
import com.bordozer.jphoto.utils.NumberUtils;
import com.bordozer.jphoto.utils.PagingUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping(UrlUtilsServiceImpl.PHOTOS_URL)
public class PhotoListController {

    public static final String VIEW = "photos/list/PhotoList";

    private static final String PHOTO_FILTER_MODEL = "photoFilterModel";
    private static final String PHOTO_FILTER_DATA_SESSION_KEY = "PhotoFilterDataKey";

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private VotingCategoryService votingCategoryService;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private BreadcrumbsPhotoGalleryService breadcrumbsPhotoGalleryService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private GroupOperationService groupOperationService;

    @Autowired
    private Services services;

    @Autowired
    private BaseSqlUtilsService baseSqlUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private PhotoFilterValidator photoFilterValidator;

    @Autowired
    private PhotoListFactoryService photoListFactoryService;

    @ModelAttribute("photoListModel")
    public PhotoListModel prepareModel() {
        final PhotoListModel model = new PhotoListModel();

        model.setDeviceType(EnvironmentContext.getDeviceType());

        return model;
    }

    @ModelAttribute("pagingModel")
    public PagingModel preparePagingModel(final HttpServletRequest request) {
        final PagingModel pagingModel = new PagingModel(services);
        PagingUtils.initPagingModel(pagingModel, request);

        pagingModel.setItemsOnPage(utilsService.getPhotosOnPage(getCurrentUser()));

        return pagingModel;
    }

    @ModelAttribute(PHOTO_FILTER_MODEL)
    public PhotoFilterModel prepareUserFilterModel(final HttpServletRequest request) {
        final PhotoFilterModel filterModel = new PhotoFilterModel();

        filterModel.setShowPhotosWithNudeContent(true);

        final HttpSession session = request.getSession();
        final PhotoFilterData filterData = (PhotoFilterData) session.getAttribute(PHOTO_FILTER_DATA_SESSION_KEY);

        if (filterData == null) {
            setDefaultOrdering(filterModel);
        } else {
            filterModel.setPhotosSortColumn(String.valueOf(filterData.getPhotosSortColumn().getId()));
            filterModel.setPhotosSortOrder(String.valueOf(filterData.getPhotosSortOrder().getId()));
        }

        filterModel.setGenreWrappers(getGenreWrappers());

        filterModel.setPhotoAuthorMembershipTypeIds(Lists.transform(Arrays.asList(UserMembershipType.values()), new Function<UserMembershipType, Integer>() {
            @Override
            public Integer apply(final UserMembershipType membershipType) {
                return membershipType.getId();
            }
        }));

        return filterModel;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showPhotoGallery(final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryTopBest(getCurrentUser()), pagingModel));
        model.addPhotoList(getPhotoList(photoListFactoryService.gallery(pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotoGalleryBreadcrumbs());

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/popular/")
    public String popularPhotos(final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryLastPopular(pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getLastPopularPhotosBreadcrumbs());

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/best/")
    public String showAbsoluteBestPhotos(final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryAbsolutelyBest(pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getAbsolutelyBestPhotosBreadcrumbs());

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "genres/{genreId}/")
    public String showPhotosByGenre(final @PathVariable("genreId") String _genreId, final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        final int genreId = assertGenreExists(_genreId);
        final Genre genre = genreService.load(genreId);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryForGenreTopBest(genre, EnvironmentContext.getCurrentUser()), pagingModel));
        model.addPhotoList(getPhotoList(photoListFactoryService.galleryForGenre(genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByGenreBreadcrumbs(genre));

        filterModel.setFilterGenreId(_genreId);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "genres/{genreId}/best/")
    public String showPhotosByGenreBest(final @PathVariable("genreId") String _genreId, final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        final int genreId = assertGenreExists(_genreId);
        final Genre genre = genreService.load(genreId);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryForGenreBest(genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByGenreBestBreadcrumbs(genre));

        filterModel.setFilterGenreId(_genreId);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "members/{userId}/")
    public String showPhotosByUser(final @PathVariable("userId") String _userId, final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        final int userId = assertUserExistsAndGetUserId(_userId);
        final User user = userService.load(userId);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryForUserTopBest(user, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));
        model.addPhotoList(getPhotoList(photoListFactoryService.galleryForUser(user, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        fillFilterModelWithUserData(filterModel, user);

        initUserGenres(model, user);

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByUserBreadcrumbs(user));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "members/{userId}/best/")
    public String showPhotosByUserBest(final @PathVariable("userId") String _userId, final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        final int userId = assertUserExistsAndGetUserId(_userId);
        final User user = userService.load(userId);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryForUserBest(user, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        fillFilterModelWithUserData(filterModel, user);

        initUserGenres(model, user);

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByUserBestBreadcrumbs(user));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "members/{userId}/genre/{genreId}/")
    public String showPhotosByUserByGenre(final @PathVariable("userId") String _userId, final @PathVariable("genreId") String _genreId, final @ModelAttribute("photoListModel") PhotoListModel model, @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        final int userId = assertUserExistsAndGetUserId(_userId);
        final int genreId = assertGenreExists(_genreId);

        final Genre genre = genreService.load(genreId);
        final User user = userService.load(userId);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryForUserAndGenreTopBest(user, genre, EnvironmentContext.getCurrentUser()), pagingModel));
        model.addPhotoList(getPhotoList(photoListFactoryService.galleryForUserAndGenre(user, genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        filterModel.setFilterGenreId(_genreId);
        fillFilterModelWithUserData(filterModel, user);

        initUserGenres(model, user);

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByUserAndGenreBreadcrumbs(user, genre));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "members/{userId}/genre/{genreId}/best/")
    public String showPhotosByUserByGenreBest(final @PathVariable("userId") String _userId, final @PathVariable("genreId") String _genreId, final @ModelAttribute("photoListModel") PhotoListModel model, @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        final int userId = assertUserExistsAndGetUserId(_userId);
        final int genreId = assertGenreExists(_genreId);

        final Genre genre = genreService.load(genreId);
        final User user = userService.load(userId);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryForUserAndGenreBest(user, genre, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByUserAndGenreBestBreadcrumbs(user, genre));

        initUserGenres(model, user);

        filterModel.setFilterGenreId(_genreId);

        fillFilterModelWithUserData(filterModel, user);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "members/{userId}/category/")
    public String showPhotosVotedByUser(final @PathVariable("userId") String _userId, final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        final int userId = assertUserExistsAndGetUserId(_userId);
        final User user = userService.load(userId);

        model.addPhotoList(getPhotoList(photoListFactoryService.appraisedByUserPhotos(user, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosAppraisedByUserBreadcrumbs(user));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "members/{userId}/category/{votingCategoryId}/")
    public String showPhotosByUserByVotingCategory(final @PathVariable("userId") String _userId, final @PathVariable("votingCategoryId") int votingCategoryId, final @ModelAttribute("photoListModel") PhotoListModel model
            , final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {

        final int userId = assertUserExistsAndGetUserId(_userId);
        final User user = userService.load(userId);

        final PhotoVotingCategory votingCategory = votingCategoryService.load(votingCategoryId);

        model.addPhotoList(getPhotoList(photoListFactoryService.appraisedByUserPhotos(user, votingCategory, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByUserByVotingCategoryBreadcrumbs(user, votingCategory));

        fillFilterModelWithUserData(filterModel, user);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "date/{date}/uploaded/")
    public String showPhotosByDate(final @PathVariable("date") String date, final @ModelAttribute("photoListModel") PhotoListModel model
            , final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {
        return processPhotosOnPeriod(date, date, model, pagingModel, filterModel);
    }

    @RequestMapping(method = RequestMethod.GET, value = "from/{datefrom}/to/{dateto}/uploaded/")
    public String showPhotosByPeriod(final @PathVariable("datefrom") String dateFrom, final @PathVariable("dateto") String dateTo
            , final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {
        return processPhotosOnPeriod(dateFrom, dateTo, model, pagingModel, filterModel);
    }

    @RequestMapping(method = RequestMethod.GET, value = "date/{date}/best/")
    public String showBestPhotosByDate(final @PathVariable("date") String date, final @ModelAttribute("photoListModel") PhotoListModel model
            , final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {
        return processBestPhotosOnPeriod(date, date, model, pagingModel, filterModel);
    }

    @RequestMapping(method = RequestMethod.GET, value = "from/{datefrom}/to/{dateto}/best/")
    public String showBestPhotosByPeriod(final @PathVariable("datefrom") String dateFrom, final @PathVariable("dateto") String dateTo
            , final @ModelAttribute("photoListModel") PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {
        return processBestPhotosOnPeriod(dateFrom, dateTo, model, pagingModel, filterModel);
    }

    private String processPhotosOnPeriod(final String dateFrom, final String dateTo, final PhotoListModel model, final PagingModel pagingModel, final PhotoFilterModel filterModel) {
        final Date fDateFrom = dateUtilsService.parseDate(dateFrom);
        final Date fDateTo = dateUtilsService.parseDate(dateTo);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryUploadedInDateRange(fDateFrom, fDateTo, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByPeriodBreadcrumbs(fDateFrom, fDateTo));

        return VIEW;
    }

    private String processBestPhotosOnPeriod(final String dateFrom, final String dateTo, final PhotoListModel model, final PagingModel pagingModel, final PhotoFilterModel filterModel) {
        final Date fDateFrom = dateUtilsService.parseDate(dateFrom);
        final Date fDateTo = dateUtilsService.parseDate(dateTo);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryUploadedInDateRangeBest(fDateFrom, fDateTo, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByPeriodBestBreadcrumbs(fDateFrom, fDateTo));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "type/{typeId}/")
    public String showPhotosByMembershipType(final @PathVariable("typeId") int typeId, final @ModelAttribute("photoListModel") PhotoListModel model
            , final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {
        final UserMembershipType membershipType = UserMembershipType.getById(typeId);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryByUserMembershipTypeTopBest(membershipType, EnvironmentContext.getCurrentUser()), pagingModel));
        model.addPhotoList(getPhotoList(photoListFactoryService.galleryByUserMembershipType(membershipType, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByMembershipTypeBreadcrumbs(membershipType));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "type/{typeId}/best/")
    public String showPhotosByMembershipTypeBest(final @PathVariable("typeId") int typeId, final @ModelAttribute("photoListModel") PhotoListModel model
            , final @ModelAttribute("pagingModel") PagingModel pagingModel, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel) {
        final UserMembershipType membershipType = UserMembershipType.getById(typeId);

        model.addPhotoList(getPhotoList(photoListFactoryService.galleryByUserMembershipTypeBest(membershipType, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage(), EnvironmentContext.getCurrentUser()), pagingModel));

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getPhotosByMembershipTypeBestBreadcrumbs(membershipType));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/filter/")
    public String searchGet(final PhotoListModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel
            , final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel filterModel, final HttpServletRequest request) {

        final HttpSession session = request.getSession();
        final PhotoFilterData filterData = (PhotoFilterData) session.getAttribute(PHOTO_FILTER_DATA_SESSION_KEY);

        if (filterData == null) {
            return String.format("redirect:%s", urlUtilsService.getAllPhotosLink()); // TODO: the session is expired - haw to be?
        }

        final User currentUser = getCurrentUser();

        final SqlIdsSelectQuery selectQuery = filterData.getSelectQuery();
        baseSqlUtilsService.initLimitAndOffset(selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage());

        final SqlSelectIdsResult selectResult = photoService.load(selectQuery);

        final PhotoList photoList = new PhotoList(selectResult.getIds(), translatorService.translate("Photo list: Search result title", getLanguage()));
        photoList.setPhotoGroupOperationMenuContainer(groupOperationService.getPhotoListPhotoGroupOperationMenuContainer(currentUser));
        model.addPhotoList(photoList);

        pagingModel.setTotalItems(selectResult.getRecordQty());

        filterModel.setFilterPhotoName(filterData.getFilterPhotoName());
        filterModel.setFilterGenreId(filterData.getFilterGenre() != null ? String.valueOf(filterData.getFilterGenre().getId()) : "-1");
        filterModel.setShowPhotosWithNudeContent(filterData.isShowPhotosWithNudeContent());
        filterModel.setFilterAuthorName(filterData.getFilterAuthorName());
        filterModel.setPhotoAuthorMembershipTypeIds(filterData.getPhotoAuthorMembershipTypeIds());

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getFilteredPhotoListBreadcrumbs());

        return PhotoListController.VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/filter/")
    public String searchPost(final PhotoListModel model, final @ModelAttribute(PHOTO_FILTER_MODEL) PhotoFilterModel photoFilterModel
            , final @ModelAttribute("pagingModel") PagingModel pagingModel, final HttpServletRequest request) {

        model.setPageTitleData(breadcrumbsPhotoGalleryService.getFilteredPhotoListBreadcrumbs());

        final BindingResult bindingResult = new BindException(photoFilterModel, "");
        photoFilterValidator.validate(photoFilterModel, bindingResult);
        photoFilterModel.setBindingResult(bindingResult);

        if (bindingResult.hasErrors()) {
            return VIEW;
        }

        final String filterByPhotoName = photoFilterModel.getFilterPhotoName();
        final Genre filterByGenre = !photoFilterModel.getFilterGenreId().equals("-1") ? genreService.load(NumberUtils.convertToInt(photoFilterModel.getFilterGenreId())) : null;
        final boolean showPhotosWithNudeContent = photoFilterModel.isShowPhotosWithNudeContent();
        final String filterByAuthorName = photoFilterModel.getFilterAuthorName();
        final List<Integer> filterByPhotoAuthorMembershipTypeIds = photoFilterModel.getPhotoAuthorMembershipTypeIds();
        final PhotoFilterSortColumn sortColumn = PhotoFilterSortColumn.getById(Integer.parseInt(photoFilterModel.getPhotosSortColumn()));
        final PhotoFilterSortOrder sortOrder = PhotoFilterSortOrder.getById(Integer.parseInt(photoFilterModel.getPhotosSortOrder()));

        final SqlTable tPhotos = new SqlTable(PhotoDaoImpl.TABLE_PHOTOS);
        final SqlIdsSelectQuery selectIdsQuery = new SqlIdsSelectQuery(tPhotos);

        final SqlTable tUsers = new SqlTable(UserDaoImpl.TABLE_USERS);

        if (StringUtils.isNotEmpty(filterByPhotoName)) {
            final SqlColumnSelectable tPhotoColName = new SqlColumnSelect(tPhotos, PhotoDaoImpl.TABLE_COLUMN_NAME);
            final SqlLogicallyJoinable photoNameCondition = new SqlCondition(tPhotoColName, SqlCriteriaOperator.LIKE, filterByPhotoName, dateUtilsService);
            selectIdsQuery.addWhereAnd(photoNameCondition);
        }

        if (filterByGenre != null) {
            final SqlColumnSelectable tPhotoColGenreId = new SqlColumnSelect(tPhotos, PhotoDaoImpl.TABLE_COLUMN_GENRE_ID);
            final SqlLogicallyJoinable photoNameCondition = new SqlCondition(tPhotoColGenreId, SqlCriteriaOperator.EQUALS, filterByGenre.getId(), dateUtilsService);
            selectIdsQuery.addWhereAnd(photoNameCondition);
        }

        if (!showPhotosWithNudeContent) {
            final SqlColumnSelectable tPhotoColHasNudeContent = new SqlColumnSelect(tPhotos, PhotoDaoImpl.TABLE_COLUMN_CONTAINS_NUDE_CONTENT);
            final SqlLogicallyJoinable photoNameCondition = new SqlCondition(tPhotoColHasNudeContent, SqlCriteriaOperator.EQUALS, !showPhotosWithNudeContent, dateUtilsService);
            selectIdsQuery.addWhereAnd(photoNameCondition);
        }

        final boolean isFilterByAuthorNameNeeded = StringUtils.isNotEmpty(filterByAuthorName);
        final boolean isFilterByAuthorMembershipNeeded = filterByPhotoAuthorMembershipTypeIds.size() < UserMembershipType.values().length;
        final boolean isFilterByUserDataNeeded = isFilterByAuthorNameNeeded || isFilterByAuthorMembershipNeeded;
        if (isFilterByUserDataNeeded) {
            final SqlColumnSelect tPhotosColUserId = new SqlColumnSelect(tPhotos, PhotoDaoImpl.TABLE_COLUMN_USER_ID);
            final SqlColumnSelect tUsersColId = new SqlColumnSelect(tUsers, UserDaoImpl.ENTITY_ID);
            final SqlJoin join = SqlJoin.inner(tUsers, new SqlJoinCondition(tPhotosColUserId, tUsersColId));
            selectIdsQuery.joinTable(join);

            if (isFilterByAuthorNameNeeded) {
                final SqlColumnSelectable tUserColName = new SqlColumnSelect(tUsers, UserDaoImpl.TABLE_COLUMN_NAME);
                final SqlLogicallyJoinable photoAuthorNameCondition = new SqlCondition(tUserColName, SqlCriteriaOperator.LIKE, filterByAuthorName, dateUtilsService);
                selectIdsQuery.addWhereAnd(photoAuthorNameCondition);
            }

            if (isFilterByAuthorMembershipNeeded) {
                final SqlColumnSelectable tUsersColMembershipType = new SqlColumnSelect(tUsers, UserDaoImpl.TABLE_COLUMN_MEMBERSHIP_TYPE);
                final SqlConditionList membershipConditions = new SqlLogicallyOr();
                final List<SqlLogicallyJoinable> membershipConditionsItems = newArrayList();

                for (final UserMembershipType membershipType : UserMembershipType.getByIds(filterByPhotoAuthorMembershipTypeIds)) {
                    membershipConditionsItems.add(new SqlCondition(tUsersColMembershipType, SqlCriteriaOperator.EQUALS, membershipType.getId(), dateUtilsService));
                }
                membershipConditions.setLogicallyItems(membershipConditionsItems);
                selectIdsQuery.addWhereAnd(membershipConditions);
            }
        }

        final SqlColumnSelect tPhotoColId = new SqlColumnSelect(tPhotos, UserDaoImpl.ENTITY_ID);
        final SqlColumnSelectable tPhotosColUploadTime = new SqlColumnSelect(tPhotos, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME);

        final SqlColumnSelectable tableSortColumn;

        switch (sortColumn) {
            case POSTING_TIME:
                tableSortColumn = tPhotosColUploadTime;
                break;
            case COMMENTS_COUNT:
                final SqlTable tComments = new SqlTable(PhotoCommentDaoImpl.TABLE_COMMENTS);
                final SqlColumnSelect tCommentsColPhotoId = new SqlColumnSelect(tComments, PhotoCommentDaoImpl.TABLE_COLUMN_PHOTO_ID);


                final SqlJoin join1 = SqlJoin.inner(tComments, new SqlJoinCondition(tPhotoColId, tCommentsColPhotoId));
                selectIdsQuery.joinTable(join1);

                final SqlColumnSelect tCommentsColId = new SqlColumnSelect(tComments, PhotoCommentDaoImpl.ENTITY_ID);
                tableSortColumn = new SqlColumnAggregate(tCommentsColId, SqlFunctions.COUNT, "commentsCount");

                final List<SqlBuildable> groupBy1 = newArrayList();
                groupBy1.add(tPhotoColId);
                selectIdsQuery.setGroupColumns(groupBy1);
                break;
            case VIEWS_COUNT:
                final SqlTable tPreviews = new SqlTable(PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW);
                final SqlColumnSelect tPreviewsColPhotoId = new SqlColumnSelect(tPreviews, PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID);

                final SqlJoin join2 = SqlJoin.inner(tPreviews, new SqlJoinCondition(tPhotoColId, tPreviewsColPhotoId));
                selectIdsQuery.joinTable(join2);

                final SqlColumnSelect tPreviewsColId = new SqlColumnSelect(tPreviews, PhotoCommentDaoImpl.ENTITY_ID);
                tableSortColumn = new SqlColumnAggregate(tPreviewsColId, SqlFunctions.COUNT, "viewsCount");

                final List<SqlBuildable> groupBy2 = newArrayList();
                groupBy2.add(tPhotoColId);
                selectIdsQuery.setGroupColumns(groupBy2);
                break;
            case RATING:
                final SqlTable tPhotoVotingSummary = new SqlTable(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY);
                final SqlColumnSelect tPhotoVotingSummaryColPhotoId = new SqlColumnSelect(tPhotoVotingSummary, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID);

                final SqlJoin join3 = SqlJoin.inner(tPhotoVotingSummary, new SqlJoinCondition(tPhotoColId, tPhotoVotingSummaryColPhotoId));
                selectIdsQuery.joinTable(join3);

                final SqlColumnSelect tPhotoVotingSummaryColPhotoSummaryMark = new SqlColumnSelect(tPhotoVotingSummary, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY_MARKS);
                tableSortColumn = new SqlColumnAggregate(tPhotoVotingSummaryColPhotoSummaryMark, SqlFunctions.SUM, "photoSummaryMark");

                final List<SqlBuildable> groupBy3 = newArrayList();
                groupBy3.add(tPhotoColId);
                selectIdsQuery.setGroupColumns(groupBy3);
                break;
            default:
                throw new IllegalArgumentException(String.format("Illegal photo search form sort column: %s", sortColumn));
        }
        selectIdsQuery.addSorting(tableSortColumn, sortOrder.getSortOrder());
        selectIdsQuery.addSorting(tPhotosColUploadTime, SqlSortOrder.DESC);

        baseSqlUtilsService.initLimitAndOffset(selectIdsQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage());

        final User currentUser = getCurrentUser();

        final SqlSelectIdsResult selectResult = photoService.load(selectIdsQuery);

        final PhotoList photoList = new PhotoList(selectResult.getIds(), translatorService.translate("Photo list: Search result", getLanguage()));
        photoList.setPhotoGroupOperationMenuContainer(groupOperationService.getPhotoListPhotoGroupOperationMenuContainer(currentUser));
        model.addPhotoList(photoList);

        pagingModel.setTotalItems(selectResult.getRecordQty());

        final PhotoFilterData filterData = new PhotoFilterData();
        filterData.setFilterPhotoName(filterByPhotoName);
        filterData.setFilterGenre(filterByGenre);
        filterData.setShowPhotosWithNudeContent(showPhotosWithNudeContent);
        filterData.setFilterAuthorName(filterByAuthorName);
        filterData.setPhotoAuthorMembershipTypeIds(filterByPhotoAuthorMembershipTypeIds);
        filterData.setSelectQuery(selectIdsQuery);
        filterData.setPhotosSortColumn(sortColumn);
        filterData.setPhotosSortOrder(sortOrder);

        final HttpSession session = request.getSession();
        session.setAttribute(PHOTO_FILTER_DATA_SESSION_KEY, filterData);

        return VIEW;
    }

    private void initUserGenres(final PhotoListModel model, final User user) {
        model.setUser(user);
        model.setUserPhotosByGenres(photoService.getUserPhotosByGenres(user.getId()));
    }

    private int assertGenreExists(final String _genreId) {
        securityService.assertGenreExists(_genreId);
        return NumberUtils.convertToInt(_genreId);
    }

    private int assertUserExistsAndGetUserId(final String _userId) {
        securityService.assertUserExists(_userId);
        return NumberUtils.convertToInt(_userId);
    }

    private void setDefaultOrdering(final PhotoFilterModel filterModel) {
        filterModel.setPhotosSortColumn(String.valueOf(PhotoFilterSortColumn.POSTING_TIME.getId()));
        filterModel.setPhotosSortOrder(String.valueOf(PhotoFilterSortOrder.DESC.getId()));
    }

    private void fillFilterModelWithUserData(final PhotoFilterModel filterModel, final User user) {
        filterModel.setFilterAuthorName(user.getName());
        filterModel.setPhotoAuthorMembershipTypeIds(newArrayList(user.getMembershipType().getId()));
    }

    // TODO: duplecated in /home/blu/dev/src/jphoto/jphoto/src/ui/controllers/photos/edit/PhotoEditDataController.java
    private List<GenreWrapper> getGenreWrappers() {
        final Language language = getLanguage();

        final List<GenreWrapper> result = newArrayList();

        final List<Genre> genres = genreService.loadAllSortedByNameForLanguage(language);
        for (final Genre genre : genres) {
            final GenreWrapper wrapper = new GenreWrapper(genre);
            wrapper.setGenreNameTranslated(translatorService.translateGenre(genre, language));

            result.add(wrapper);
        }

        return result;
    }

    private PhotoList getPhotoList(final AbstractPhotoListFactory photoListFactory, final PagingModel pagingModel) {
        final int photoListId = 0; // TODO: check if it still has influence on sth
        final PhotoList photoList = photoListFactory.getPhotoList(photoListId, pagingModel.getCurrentPage(), getLanguage(), dateUtilsService.getCurrentTime());

        if (photoList == null) {
            return null;
        }

        pagingModel.setTotalItems(photoList.getPhotosCount());

        return photoList;
    }

    private Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }

    private User getCurrentUser() {
        return EnvironmentContext.getCurrentUser();
    }
}
