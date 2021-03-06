package com.bordozer.jphoto.ui.controllers.users.card.data;

import com.bordozer.jphoto.core.enums.UserCardTab;
import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeam;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.dao.ActivityStreamDaoImpl;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.entry.GroupOperationService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.PhotoVotingService;
import com.bordozer.jphoto.core.services.photo.list.PhotoListFactoryService;
import com.bordozer.jphoto.core.services.photo.list.factory.AbstractPhotoListFactory;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserPhotoAlbumService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserStatisticService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.sql.BaseSqlUtilsService;
import com.bordozer.jphoto.core.services.utils.sql.PhotoListQueryBuilder;
import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.builder.SqlColumnSelect;
import com.bordozer.jphoto.sql.builder.SqlColumnSelectable;
import com.bordozer.jphoto.sql.builder.SqlCondition;
import com.bordozer.jphoto.sql.builder.SqlCriteriaOperator;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlLogicallyJoinable;
import com.bordozer.jphoto.sql.builder.SqlTable;
import com.bordozer.jphoto.ui.activity.AbstractActivityStreamEntry;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardGenreInfo;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardModel;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardTabDTO;
import com.bordozer.jphoto.ui.controllers.users.card.UserStatistic;
import com.bordozer.jphoto.ui.elements.PhotoList;
import com.bordozer.jphoto.ui.services.menu.entry.EntryMenuService;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenu;
import com.bordozer.jphoto.ui.services.security.UsersSecurityService;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;

@Service("userCardModelFillService")
public class UserCardModelFillServiceImpl implements UserCardModelFillService {

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private UserStatisticService userStatisticService;

    @Autowired
    private PhotoVotingService photoVotingService;

    @Autowired
    private UserTeamService userTeamService;

    @Autowired
    private UserPhotoAlbumService userPhotoAlbumService;

    @Autowired
    private UserRankService userRankService;

    @Autowired
    private UsersSecurityService usersSecurityService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private EntryMenuService entryMenuService;

    @Autowired
    private GroupOperationService groupOperationService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private BaseSqlUtilsService baseSqlUtilsService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private PhotoListFactoryService photoListFactoryService;

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private SecurityService securityService;

    @Override
    public void setUserAvatar(final UserCardModel model) {
        model.setUserAvatar(userService.getUserAvatar(getUserId(model)));
    }

    @Override
    public void setUserStatistic(final UserCardModel model) {

        final int userId = getUserId(model);

        final UserStatistic userStatistic = new UserStatistic();

        userStatistic.setFavoritePhotosQty(userStatisticService.getFavoritePhotosQty(userId));
        userStatistic.setBookmarkedPhotosQty(userStatisticService.getBookmarkedPhotosQty(userId));
        userStatistic.setUsersQtyWhoAddedInFavoriteMembers(userStatisticService.getUsersQtyWhoAddedInFavoriteMembers(userId));

        userStatistic.setWrittenCommentsQty(userStatisticService.getWrittenCommentsQty(userId));
        userStatistic.setReceivedCommentsQty(userStatisticService.getReceivedCommentsQty(userId));
        userStatistic.setReceivedUnreadCommentsQty(userStatisticService.setReceivedUnreadCommentsQty(userId));

        userStatistic.setFriendsQty(userStatisticService.getFriendsQty(userId));
        userStatistic.setFavoriteMembersQty(userStatisticService.getFavoriteMembersQty(userId));
        userStatistic.setPhotosOfFavoriteMembersQty(userStatisticService.getPhotosQtyOfUserFavoriteMembers(userId));
        userStatistic.setBlackListEntriesQty(userStatisticService.getBackListEntriesQty(userId));
        userStatistic.setNotificationsAboutNewPhotosQty(userStatisticService.getNotificationsAboutNewPhotosQty(userId));
        userStatistic.setNotificationsAboutNewCommentsQty(userStatisticService.getNotificationsAboutNewCommentsQty(userId));

        model.setUserStatistic(userStatistic);
    }

    @Override
    public Map<Genre, UserCardGenreInfo> getUserCardGenreInfoMap(final User user, final User accessor) {
        final Map<Genre, UserCardGenreInfo> photosByGenresMap = newLinkedHashMap();

        final List<Genre> genres = genreService.loadAll();

        for (final Genre genre : genres) {
            if (photoService.getPhotosCountByUserAndGenre(user.getId(), genre.getId()) > 0 || (securityService.isSuperAdminUser(accessor) && userRankService.getUserRankInGenre(user.getId(), genre.getId()) > 0)) {
                final UserCardGenreInfo userCardGenreInfo = getUserCardGenreInfo(user, genre, accessor);
                photosByGenresMap.put(genre, userCardGenreInfo);
            }
        }

        return photosByGenresMap;
    }

    @Override
    public void setMarksByCategoryInfos(final UserCardModel model) {
        model.setMarksByCategoryInfos(photoVotingService.getUserSummaryVoicesByPhotoCategories(getUser(model)));
    }

    @Override
    public void setUserTeam(final UserCardModel model) {
        final UserTeam userTeam = userTeamService.loadUserTeam(getUserId(model));
        model.setUserTeam(userTeam);

        final Map<UserTeamMember, Integer> teamMemberPhotosQtyMap = newHashMap();
        for (final UserTeamMember userTeamMember : userTeam.getUserTeamMembers()) {
            final int photosQty = userTeamService.getTeamMemberPhotosQty(userTeamMember.getId());
            teamMemberPhotosQtyMap.put(userTeamMember, photosQty);
        }
        model.setTeamMemberPhotosQtyMap(teamMemberPhotosQtyMap);
    }

    @Override
    public void setPhotoAlbums(final UserCardModel model) {
        final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadAllForEntry(getUserId(model));
        model.setUserPhotoAlbums(userPhotoAlbums);
        final Map<UserPhotoAlbum, Integer> userPhotoAlbumsQtyMap = newLinkedHashMap();
        for (final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums) {
            userPhotoAlbumsQtyMap.put(userPhotoAlbum, userPhotoAlbumService.getUserPhotoAlbumPhotosQty(userPhotoAlbum.getId()));
        }
        model.setUserPhotoAlbumsQtyMap(userPhotoAlbumsQtyMap);
    }

    @Override
    public void setLastUserActivityTime(final UserCardModel model) {
        model.setLastUserActivityTime(usersSecurityService.getLastUserActivityTime(getUserId(model)));
    }

    @Override
    public void setUserPhotosByGenresPhotoList(final UserCardModel model) {
        final User user = getUser(model);

        final List<PhotoList> photoLists = newArrayList();

        final List<Genre> genres = newArrayList(photoService.getUserPhotoGenres(getUserId(model)));

        Collections.sort(genres, new Comparator<Genre>() {
            @Override
            public int compare(final Genre o1, final Genre o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (final Genre genre : genres) {
            photoLists.add(getUserPhotosByGenrePhotoList(user, genre));
        }

        model.setPhotoLists(photoLists);

        model.setUserPhotosByGenres(photoService.getUserPhotosByGenres(user.getId()));
    }

    @Override
    public EntryMenu getUserMenu(final User user, final User userWhoIsCallingMenu) {
        return entryMenuService.getUserMenu(user, userWhoIsCallingMenu);
    }

    @Override
    public List<AbstractActivityStreamEntry> getUserLastActivities(int userId, final int activityTypeId, final PagingModel pagingModel) {
        final SqlTable activityStreamTable = new SqlTable(ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM);
        final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery(activityStreamTable);

        final SqlColumnSelectable tActivityColUserId = new SqlColumnSelect(activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_USER_ID);
        final SqlLogicallyJoinable whereUserId = new SqlCondition(tActivityColUserId, SqlCriteriaOperator.EQUALS, userId, dateUtilsService);
        selectQuery.addWhereAnd(whereUserId);

        if (activityTypeId > 0) {
            final SqlColumnSelectable tActivityColActivityTypeId = new SqlColumnSelect(activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TYPE);
            final SqlLogicallyJoinable whereActivityTypeId = new SqlCondition(tActivityColActivityTypeId, SqlCriteriaOperator.EQUALS, activityTypeId, dateUtilsService);
            selectQuery.addWhereAnd(whereActivityTypeId);
        }

        final SqlColumnSelectable timeCol = new SqlColumnSelect(activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME);
        selectQuery.addSortingDesc(timeCol);
        baseSqlUtilsService.initLimitAndOffset(selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage());

        final SqlSelectIdsResult idsResult = activityStreamService.load(selectQuery);

        pagingModel.setTotalItems(idsResult.getRecordQty());

        final List<AbstractActivityStreamEntry> activities = newArrayList();
        for (final int activityId : idsResult.getIds()) {
            activities.add(activityStreamService.load(activityId));
        }

        return activities;
    }

    @Override
    public List<UserPhotoAlbum> getUserPhotoAlbums(final int userId) {
        return userPhotoAlbumService.loadAllForEntry(userId);
    }

    @Override
    public Map<Integer, Integer> setUserPhotosCountByAlbums(final int userId) {
        final Map<Integer, Integer> userPhotoAlbumsQtyMap = newLinkedHashMap();
        for (final UserPhotoAlbum userPhotoAlbum : getUserPhotoAlbums(userId)) {
            userPhotoAlbumsQtyMap.put(userPhotoAlbum.getId(), userPhotoAlbumService.getUserPhotoAlbumPhotosQty(userPhotoAlbum.getId()));
        }
        return userPhotoAlbumsQtyMap;
    }

    @Override
    public AbstractPhotoListFactory getUserTeamMemberLastPhotos(final User user, final UserTeamMember userTeamMember, final User accessor) {
        return photoListFactoryService.userTeamMemberPhotosLast(user, userTeamMember, accessor);
    }

    @Override
    public AbstractPhotoListFactory getUserPhotoAlbumLastPhotos(final User user, final UserPhotoAlbum userPhotoAlbum, final User accessor) {
        return photoListFactoryService.userAlbumPhotosLast(user, userPhotoAlbum, accessor);
    }

    private UserCardGenreInfo getUserCardGenreInfo(final User user, final Genre genre, final User accessor) {
        final int userId = user.getId();
        final int genreId = genre.getId();

        final UserCardGenreInfo genreInfo = new UserCardGenreInfo();

        genreInfo.setPhotosQty(photoService.getPhotosCountByUserAndGenre(userId, genreId));
        genreInfo.setVotingModel(userRankService.getVotingModel(userId, genreId, accessor, dateUtilsService.getCurrentTime()));
        genreInfo.setUserRankInGenre(userRankService.getUserRankInGenre(userId, genreId));

        final int userVotePointsForRankInGenre = userRankService.getUserVotePointsForRankInGenre(userId, genreId);
        genreInfo.setVotePointsForRankInGenre(userVotePointsForRankInGenre);

        genreInfo.setVotePointsToGetNextRankInGenre(userRankService.getVotePointsToGetNextRankInGenre(userVotePointsForRankInGenre));

        genreInfo.setUserRankIconContainer(userRankService.getUserRankIconContainer(user, genre));

        return genreInfo;
    }

    private PhotoList getUserPhotosByGenrePhotoList(final User user, final Genre genre) {
        final User currentUser = EnvironmentContext.getCurrentUser();

        final SqlIdsSelectQuery idsSQL = new PhotoListQueryBuilder(dateUtilsService).filterByAuthor(user).filterByGenre(genre).forPage(1, getPhotosInLine()).sortByUploadTimeDesc().getQuery();

        final List<Integer> ids = photoService.load(idsSQL).getIds();

        final int photosByGenre = photoService.getPhotosCountByUserAndGenre(user.getId(), genre.getId());
        final String title = translatorService.translate("User card: $1: last photos. Total $2."
                , EnvironmentContext.getLanguage()
                , entityLinkUtilsService.getPhotosByUserByGenreLink(user, genre, currentUser.getLanguage())
                , String.valueOf(photosByGenre)
        );

        final String link = urlUtilsService.getPhotosByUserByGenreLink(user.getId(), genre.getId());

        return getPhotoList(0, ids, link, title);
    }

    @Override
    public AbstractPhotoListFactory getUserCardPhotoListBest(final User user, final User accessor) {
        return photoListFactoryService.userCardPhotosBest(user, accessor);
    }

    @Override
    public AbstractPhotoListFactory getUserCardPhotoListLast(final User user, final User accessor) {
        return photoListFactoryService.userCardPhotosLast(user, accessor);
    }

    @Override
    public AbstractPhotoListFactory getUserCardPhotoListLastAppraised(final User user, final User accessor) {
        return photoListFactoryService.userCardPhotosLastAppraised(user, accessor);
    }

    private PhotoList getPhotoList(final int photoListId, final List<Integer> photosIds, final String linkToFullPhotoList, final String listTitle) {

        final PhotoList photoList = new PhotoList(photosIds, listTitle, false);
        photoList.setPhotoListId(photoListId);
        photoList.setLinkToFullList(linkToFullPhotoList);
        photoList.setPhotoGroupOperationMenuContainer(groupOperationService.getNoPhotoGroupOperationMenuContainer());

        return photoList;
    }

    private int getPhotosInLine() {
        return configurationService.getInt(ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY);
    }

    protected User getUser(final UserCardModel model) {
        return model.getUser();
    }

    protected int getUserId(final UserCardModel model) {
        return model.getUser().getId();
    }

    @Override
    public DateUtilsService getDateUtilsService() {
        return dateUtilsService;
    }

    @Override
    public List<UserCardTabDTO> getUserCardTabDTOs(final User user) {
        final int userId = user.getId();

        final List<UserCardTabDTO> userCardTabDTOs = newArrayList();

        for (final UserCardTab cardTab : UserCardTab.values()) {

            int itemsCount = 0;

            switch (cardTab) {
                case PHOTOS_OVERVIEW:
                    if (photoService.getPhotosCountByUser(userId) == 0) {
                        continue;
                    }
                    break;
                case ALBUMS:
                    final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadAllForEntry(userId);
                    CollectionUtils.filter(userPhotoAlbums, new Predicate<UserPhotoAlbum>() {
                        @Override
                        public boolean evaluate(final UserPhotoAlbum userPhotoAlbum) {
                            return userPhotoAlbumService.getUserPhotoAlbumPhotosQty(userPhotoAlbum.getId()) > 0;
                        }
                    });
                    itemsCount = userPhotoAlbums.size();
                    if (itemsCount == 0) {
                        continue;
                    }
                    break;
                case TEAM:
                    itemsCount = userTeamService.loadUserTeam(userId).getUserTeamMembers().size();
                    if (itemsCount == 0) {
                        continue;
                    }
                    break;
                case ACTIVITY_STREAM:
                    itemsCount = activityStreamService.getUserActivities(userId).size();
                    if (itemsCount == 0) {
                        continue;
                    }
                    break;
            }

            userCardTabDTOs.add(new UserCardTabDTO(cardTab, itemsCount));
        }

        return userCardTabDTOs;
    }
}
