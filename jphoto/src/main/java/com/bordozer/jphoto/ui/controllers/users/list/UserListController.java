package com.bordozer.jphoto.ui.controllers.users.list;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.services.dao.UserDaoImpl;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.core.services.utils.sql.BaseSqlUtilsService;
import com.bordozer.jphoto.core.services.utils.sql.PhotoQueryService;
import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.SqlSelectResult;
import com.bordozer.jphoto.sql.builder.SqlColumnSelect;
import com.bordozer.jphoto.sql.builder.SqlColumnSelectable;
import com.bordozer.jphoto.sql.builder.SqlCondition;
import com.bordozer.jphoto.sql.builder.SqlConditionList;
import com.bordozer.jphoto.sql.builder.SqlCriteriaOperator;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlLogicallyAnd;
import com.bordozer.jphoto.sql.builder.SqlLogicallyJoinable;
import com.bordozer.jphoto.sql.builder.SqlLogicallyOr;
import com.bordozer.jphoto.sql.builder.SqlTable;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.elements.PageTitleData;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsUserService;
import com.bordozer.jphoto.ui.services.menu.entry.EntryMenuService;
import com.bordozer.jphoto.utils.PagingUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

@Controller
@RequestMapping(UrlUtilsServiceImpl.USERS_URL)
public class UserListController {

    private static final String VIEW = "users/list/UserList";

    private static final String USER_FILTER_MODEL = "userFilterModel";
    private static final String USER_FILTER_DATA_SESSION_KEY = "UserFilterDataKey";

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private FilterValidator filterValidator;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private BreadcrumbsUserService breadcrumbsUserService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private EntryMenuService entryMenuService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private PhotoQueryService photoQueryService;

    @Autowired
    private BaseSqlUtilsService baseSqlUtilsService;

    @Autowired
    private Services services;

    @ModelAttribute("userListModel")
    public UserListModel prepareModel() {
        return new UserListModel();
    }

    @ModelAttribute(USER_FILTER_MODEL)
    public UserFilterModel prepareUserFilterModel() {
        final UserFilterModel filterModel = new UserFilterModel();
        filterModel.setVisible(true);

        return filterModel;
    }

    @ModelAttribute
    public PagingModel preparePagingModel(final HttpServletRequest request) {
        final PagingModel pagingModel = new PagingModel(services);
        PagingUtils.initPagingModel(pagingModel, request);
        pagingModel.setItemsOnPage(configurationService.getInt(ConfigurationKey.USER_LIST_USERS_ON_PAGE));

        return pagingModel;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String allUserList(final @ModelAttribute("userListModel") UserListModel model
            , final @ModelAttribute("userFilterModel") UserFilterModel filterModel
            , final @ModelAttribute("pagingModel") PagingModel pagingModel) {

        model.clear();

        final List<UserMembershipType> userMembershipTypeList = newArrayList(UserMembershipType.values());
        model.setUserMembershipType(userMembershipTypeList);

        final List<User> users = selectDataFromDB(pagingModel, photoQueryService.getUserIdsForPageSQL(pagingModel));

        model.setUserList(users);
        model.setUserListDataMap(getUserListDataMap(users));
        model.setPageTitleData(breadcrumbsUserService.getUserListBreadcrumbs());

        model.setShowEditIcons(securityService.isSuperAdminUser(EnvironmentContext.getCurrentUser().getId()) && configurationService.getBoolean(ConfigurationKey.ADMIN_CAN_EDIT_OTHER_USER_DATA));

        filterModel.clear();
        final ArrayList<Integer> typeList = newArrayList();
        for (final UserMembershipType membershipType : UserMembershipType.values()) {
            typeList.add(membershipType.getId());
        }
        filterModel.setMembershipTypeList(typeList);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "type/{typeId}/")
    public String showPhotosByMembershipType(final @PathVariable("typeId") int typeId, final @ModelAttribute("userListModel") UserListModel model
            , final @ModelAttribute("userFilterModel") UserFilterModel filterModel, final @ModelAttribute("pagingModel") PagingModel pagingModel) {
        model.clear();

        final UserMembershipType membershipType = UserMembershipType.getById(typeId);

        final List<User> users = selectDataFromDB(pagingModel, photoQueryService.getUsersByMembershipTypeSQL(membershipType, pagingModel));
        model.setUserList(users);

        model.setUserListDataMap(getUserListDataMap(users));
        model.setPageTitleData(breadcrumbsUserService.getUserListBreadcrumbs());

        model.setMembershipType(membershipType);

        filterModel.clear();
        model.setPageTitleData(breadcrumbsUserService.getUsersFilteredByMembershipTypeBreadcrumbs(membershipType));
        model.setUserListTitle(StringUtils.EMPTY);

        filterModel.setMembershipTypeList(Lists.<Integer>newArrayList(typeId));


        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/favorites/members/")
    public String showFavoriteUsers(final @PathVariable("userId") int userId, final @ModelAttribute("userListModel") UserListModel model
            , final @ModelAttribute("userFilterModel") UserFilterModel filterModel, final @ModelAttribute("pagingModel") PagingModel pagingModel) {

        initUserFavorites(userId, model, filterModel, pagingModel, FavoriteEntryType.FAVORITE_MEMBERS);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/favorites/members/in/")
    public String showUsersQtyWhoAddedInFavoriteMembers(final @PathVariable("userId") int userId, final @ModelAttribute("userListModel") UserListModel model
            , final @ModelAttribute("userFilterModel") UserFilterModel filterModel, final @ModelAttribute("pagingModel") PagingModel pagingModel) {

        final SqlIdsSelectQuery selectIdsQuery = photoQueryService.getAddedToFavoritesBySQL(pagingModel, userId);

        final User user = userService.load(userId);
        initUserFavoritesByQuery(model, filterModel, pagingModel, selectIdsQuery, breadcrumbsUserService.getUserIsAddedInFavoriteMembersByBreadcrumbs(user));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/friends/")
    public String showFriends(@PathVariable("userId") int userId, final @ModelAttribute("userListModel") UserListModel model
            , final @ModelAttribute("userFilterModel") UserFilterModel filterModel, final @ModelAttribute("pagingModel") PagingModel pagingModel) {

        initUserFavorites(userId, model, filterModel, pagingModel, FavoriteEntryType.FRIENDS);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/invisibility/")
    public String membersInvisibilityList(@PathVariable("userId") int userId, final @ModelAttribute("userListModel") UserListModel model
            , final @ModelAttribute("userFilterModel") UserFilterModel filterModel, final @ModelAttribute("pagingModel") PagingModel pagingModel) {

        initUserFavorites(userId, model, filterModel, pagingModel, FavoriteEntryType.MEMBERS_INVISIBILITY_LIST);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/notification/photos/")
    public String showUsersThatNotificateAboutNewPhotos(final @PathVariable("userId") int userId, final @ModelAttribute("userListModel") UserListModel model
            , final @ModelAttribute("userFilterModel") UserFilterModel filterModel, final @ModelAttribute("pagingModel") PagingModel pagingModel) {

        initUserFavorites(userId, model, filterModel, pagingModel, FavoriteEntryType.NEW_PHOTO_NOTIFICATION);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}/blacklist/")
    public String showBlackList(final @PathVariable("userId") int userId, final @ModelAttribute("userListModel") UserListModel model
            , final @ModelAttribute("userFilterModel") UserFilterModel filterModel, final @ModelAttribute("pagingModel") PagingModel pagingModel) {

        initUserFavorites(userId, model, filterModel, pagingModel, FavoriteEntryType.BLACKLIST);

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/filter/")
    public String searchGet(final UserListModel model, final @ModelAttribute(USER_FILTER_MODEL) UserFilterModel filterModel
            , final @ModelAttribute("pagingModel") PagingModel pagingModel, final HttpServletRequest request) {

        final HttpSession session = request.getSession();
        final UserFilterData filterData = (UserFilterData) session.getAttribute(USER_FILTER_DATA_SESSION_KEY);

        if (filterData == null) {
            return String.format("redirect:%s", urlUtilsService.getAllUsersLink()); // TODO: the session is expired - haw to be?
        }

        final SqlIdsSelectQuery selectQuery = filterData.getSelectQuery();
        baseSqlUtilsService.initLimitAndOffset(selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage());

        final SqlSelectResult<User> selectResult = userService.loadByIds(selectQuery);

        model.setUserList(selectResult.getItems());

        pagingModel.setTotalItems(selectResult.getRecordQty());

        filterModel.setFilterUserName(filterData.getFilterUserName());
        filterModel.setMembershipTypeList(filterData.getMembershipTypeIds());

        model.setPageTitleData(breadcrumbsUserService.getUserListSearchResultBreadcrumbs());

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/filter/")
    public String searchPost(final UserListModel model, final @ModelAttribute(USER_FILTER_MODEL) UserFilterModel filterModel
            , final @ModelAttribute("pagingModel") PagingModel pagingModel, final HttpServletRequest request) {

        model.setPageTitleData(breadcrumbsUserService.getUserListSearchResultBreadcrumbs());

        final BindingResult bindingResult = new BindException(filterModel, "");
        filterValidator.validate(filterModel, bindingResult);
        filterModel.setBindingResult(bindingResult);

        if (bindingResult.hasErrors()) {
            return VIEW;
        }

        final String byNameFilterString = filterModel.getFilterUserName();

        final List<UserMembershipType> membershipTypes = newArrayList();
        final List<Integer> membershipTypeIds = filterModel.getMembershipTypeList();
        if (membershipTypeIds != null) {
            for (final Integer membershipTypeId : membershipTypeIds) {
                membershipTypes.add(UserMembershipType.getById(membershipTypeId));
            }
        }

        final SqlTable tUsers = new SqlTable(UserDaoImpl.TABLE_USERS);
        final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery(tUsers);

        final SqlColumnSelectable tUserColName = new SqlColumnSelect(tUsers, UserDaoImpl.TABLE_COLUMN_NAME);
        final SqlLogicallyJoinable likeNameCondition = new SqlCondition(tUserColName, SqlCriteriaOperator.LIKE, byNameFilterString, dateUtilsService);

        final SqlConditionList membershipConditions = new SqlLogicallyOr();
        final List<SqlLogicallyJoinable> membershipConditionsItems = newArrayList();
        final SqlColumnSelectable tUsersColMembershipType = new SqlColumnSelect(tUsers, UserDaoImpl.TABLE_COLUMN_MEMBERSHIP_TYPE);
        for (final UserMembershipType membershipType : membershipTypes) {
            membershipConditionsItems.add(new SqlCondition(tUsersColMembershipType, SqlCriteriaOperator.EQUALS, membershipType.getId(), dateUtilsService));
        }
        membershipConditions.setLogicallyItems(membershipConditionsItems);
        final SqlLogicallyJoinable where = new SqlLogicallyAnd(likeNameCondition, new SqlLogicallyOr(membershipConditions));

        selectQuery.setWhere(where);
        baseSqlUtilsService.initLimitAndOffset(selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage());

        final SqlSelectResult<User> selectResult = userService.loadByIds(selectQuery);

        model.setUserList(selectResult.getItems());
        model.setUserListDataMap(getUserListDataMap(selectResult.getItems()));

        pagingModel.setTotalItems(selectResult.getRecordQty());

        final UserFilterData filterData = new UserFilterData();
        filterData.setFilterUserName(byNameFilterString);
        filterData.setMembershipTypeIds(membershipTypeIds);
        filterData.setSelectQuery(selectQuery);

        final HttpSession session = request.getSession();
        session.setAttribute(USER_FILTER_DATA_SESSION_KEY, filterData);

        return VIEW;
    }

    private Map<Integer, UserListData> getUserListDataMap(final List<User> users) {
        final User currentUser = EnvironmentContext.getCurrentUser();

        final Map<Integer, UserListData> userListDataMap = newHashMap();

        for (final User user : users) {

            final UserListData userListData = new UserListData();
            userListData.setPhotosByUser(photoService.getPhotosCountByUser(user.getId()));
            userListData.setUserAvatar(userService.getUserAvatar(user.getId()));
            userListData.setUserMenu(entryMenuService.getUserMenu(user, currentUser));

            userListDataMap.put(user.getId(), userListData);
        }

        return userListDataMap;
    }

    private void initUserFavorites(final int userId, final UserListModel model, final UserFilterModel filterModel
            , final PagingModel pagingModel, final FavoriteEntryType entryType) {

        final SqlIdsSelectQuery selectIdsQuery = photoQueryService.getUsersFavoritesSQL(pagingModel, userId, entryType);

        final User user = userService.load(userId);
        initUserFavoritesByQuery(model, filterModel, pagingModel, selectIdsQuery, breadcrumbsUserService.getUserFavoriteEntryListBreadcrumbs(user, entryType));
    }

    private void initUserFavoritesByQuery(final UserListModel model, final UserFilterModel filterModel, final PagingModel pagingModel, final SqlIdsSelectQuery selectIdsQuery, final PageTitleData favoriteEntry) {
        model.clear();

        final SqlSelectResult<User> selectResult = userService.loadByIds(selectIdsQuery);
        final List<User> users = selectResult.getItems();
        model.setUserList(users);
        pagingModel.setTotalItems(selectResult.getRecordQty());

        model.setPageTitleData(favoriteEntry);

        model.setUserListDataMap(getUserListDataMap(users));

        filterModel.setVisible(false);
    }

    private List<User> selectDataFromDB(final PagingModel pagingModel, final SqlIdsSelectQuery selectQuery) {
        final SqlSelectIdsResult idsSql = userService.load(selectQuery);

        final List<User> users = newArrayList();
        for (final int userId : idsSql.getIds()) {
            users.add(userService.load(userId));
        }

        pagingModel.setTotalItems(idsSql.getRecordQty());

        return users;
    }

}
