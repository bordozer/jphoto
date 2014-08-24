package ui.controllers.users.card.data;

import core.general.base.PagingModel;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.services.dao.ActivityStreamDaoImpl;
import core.services.entry.ActivityStreamService;
import core.services.entry.GenreService;
import core.services.entry.GroupOperationService;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.photo.list.PhotoListFactoryService;
import core.services.photo.list.factory.AbstractPhotoListFactory;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import core.services.translator.TranslatorService;
import core.services.user.*;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.sql.BaseSqlUtilsService;
import core.services.utils.sql.PhotoListQueryBuilder;
import core.services.utils.sql.PhotoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.*;
import ui.activity.AbstractActivityStreamEntry;
import ui.context.EnvironmentContext;
import ui.controllers.users.card.UserCardGenreInfo;
import ui.controllers.users.card.UserCardModel;
import ui.controllers.users.card.UserStatistic;
import ui.elements.PhotoList;
import ui.services.menu.entry.EntryMenuService;
import ui.services.menu.entry.items.EntryMenu;
import ui.services.security.UsersSecurityService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class UserCardModelFillServiceImpl implements UserCardModelFillService {

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoListCriteriasService photoListCriteriasService;

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
	private PhotoQueryService photoQueryService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

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
	private Services services;

	@Override
	public void setUserAvatar( final UserCardModel model ) {
		model.setUserAvatar( userService.getUserAvatar( getUserId( model ) ) );
	}

	@Override
	public void setUserStatistic( final UserCardModel model ) {

		final int userId = getUserId( model );

		final UserStatistic userStatistic = new UserStatistic();

		userStatistic.setFavoritePhotosQty( userStatisticService.getFavoritePhotosQty( userId ) );
		userStatistic.setBookmarkedPhotosQty( userStatisticService.getBookmarkedPhotosQty( userId ) );
		userStatistic.setUsersQtyWhoAddedInFavoriteMembers( userStatisticService.getUsersQtyWhoAddedInFavoriteMembers( userId ) );

		userStatistic.setWrittenCommentsQty( userStatisticService.getWrittenCommentsQty( userId ) );
		userStatistic.setReceivedCommentsQty( userStatisticService.getReceivedCommentsQty( userId ) );
		userStatistic.setReceivedUnreadCommentsQty( userStatisticService.setReceivedUnreadCommentsQty( userId ) );

		userStatistic.setFriendsQty( userStatisticService.getFriendsQty( userId ) );
		userStatistic.setFavoriteMembersQty( userStatisticService.getFavoriteMembersQty( userId ) );
		userStatistic.setPhotosOfFavoriteMembersQty( userStatisticService.getPhotosQtyOfUserFavoriteMembers( userId ) );
		userStatistic.setBlackListEntriesQty( userStatisticService.getBackListEntriesQty( userId ) );
		userStatistic.setNotificationsAboutNewPhotosQty( userStatisticService.getNotificationsAboutNewPhotosQty( userId ) );
		userStatistic.setNotificationsAboutNewCommentsQty( userStatisticService.getNotificationsAboutNewCommentsQty( userId ) );

		model.setUserStatistic( userStatistic );
	}

	@Override
	public Map<Genre, UserCardGenreInfo> getUserCardGenreInfoMap( final User user, final User accessor ) {
		final Map<Genre, UserCardGenreInfo> photosByGenresMap = newLinkedHashMap();

		final List<Genre> genres = genreService.loadAll();

		for ( final Genre genre : genres ) {
			if ( photoService.getPhotosCountByUserAndGenre( user.getId(), genre.getId() ) > 0 || userRankService.getUserRankInGenre( user.getId(), genre.getId() ) > 0 ) {
				final UserCardGenreInfo userCardGenreInfo = getUserCardGenreInfo( user, genre, accessor );
				photosByGenresMap.put( genre, userCardGenreInfo );
			}
		}

		return photosByGenresMap;
	}

	@Override
	public void setMarksByCategoryInfos( final UserCardModel model ) {
		model.setMarksByCategoryInfos( photoVotingService.getUserSummaryVoicesByPhotoCategories( getUser( model ) ) );
	}

	@Override
	public void setUserTeam( final UserCardModel model ) {
		final UserTeam userTeam = userTeamService.loadUserTeam( getUserId( model ) );
		model.setUserTeam( userTeam );

		final Map<UserTeamMember, Integer> teamMemberPhotosQtyMap = newHashMap();
		for ( final UserTeamMember userTeamMember : userTeam.getUserTeamMembers() ) {
			final int photosQty = userTeamService.getTeamMemberPhotosQty( userTeamMember.getId() );
			teamMemberPhotosQtyMap.put( userTeamMember, photosQty );
		}
		model.setTeamMemberPhotosQtyMap( teamMemberPhotosQtyMap );
	}

	@Override
	public void setPhotoAlbums( final UserCardModel model ) {
		final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadAllForEntry( getUserId( model ) );
		model.setUserPhotoAlbums( userPhotoAlbums );
		final Map<UserPhotoAlbum, Integer> userPhotoAlbumsQtyMap = newLinkedHashMap();
		for ( final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums ) {
			userPhotoAlbumsQtyMap.put( userPhotoAlbum, userPhotoAlbumService.getUserPhotoAlbumPhotosQty( userPhotoAlbum.getId() ) );
		}
		model.setUserPhotoAlbumsQtyMap( userPhotoAlbumsQtyMap );
	}

	@Override
	public void setLastUserActivityTime( final UserCardModel model ) {
		model.setLastUserActivityTime( usersSecurityService.getLastUserActivityTime( getUserId( model ) ) );
	}

	@Override
	public void setUserPhotosByGenresPhotoList( final UserCardModel model ) {
		final User user = getUser( model );

		final List<PhotoList> photoLists = newArrayList();

		final List<Genre> genres = newArrayList( photoService.getUserPhotoGenres( getUserId( model ) ) );

		Collections.sort( genres, new Comparator<Genre>() {
			@Override
			public int compare( final Genre o1, final Genre o2 ) {
				return o1.getName().compareTo( o2.getName() );
			}
		} );

		for ( final Genre genre : genres ) {
			photoLists.add( getUserPhotosByGenrePhotoList( user, genre ) );
		}

		model.setPhotoLists( photoLists );

		model.setUserPhotosByGenres( photoService.getUserPhotosByGenres( user.getId() ) );
	}

	@Override
	public EntryMenu getUserMenu( final User user, final User userWhoIsCallingMenu ) {
		return entryMenuService.getUserMenu( user, userWhoIsCallingMenu );
	}

	@Override
	public List<AbstractActivityStreamEntry> getUserLastActivities( int userId, final int activityTypeId, final PagingModel pagingModel ) {
		final SqlTable activityStreamTable = new SqlTable( ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM );
		final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery( activityStreamTable );

		final SqlColumnSelectable tActivityColUserId = new SqlColumnSelect( activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_USER_ID );
		final SqlLogicallyJoinable whereUserId = new SqlCondition( tActivityColUserId, SqlCriteriaOperator.EQUALS, userId, dateUtilsService );
		selectQuery.addWhereAnd( whereUserId );

		if ( activityTypeId > 0 ) {
			final SqlColumnSelectable tActivityColActivityTypeId = new SqlColumnSelect( activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TYPE );
			final SqlLogicallyJoinable whereActivityTypeId = new SqlCondition( tActivityColActivityTypeId, SqlCriteriaOperator.EQUALS, activityTypeId, dateUtilsService );
			selectQuery.addWhereAnd( whereActivityTypeId );
		}

		final SqlColumnSelectable timeCol = new SqlColumnSelect( activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME );
		selectQuery.addSortingDesc( timeCol );
		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel );

		final SqlSelectIdsResult idsResult = activityStreamService.load( selectQuery );

		pagingModel.setTotalItems( idsResult.getRecordQty() );

		final List<AbstractActivityStreamEntry> activities = newArrayList();
		for ( final int activityId : idsResult.getIds() ) {
			activities.add( activityStreamService.load( activityId ) );
		}

		return activities;
	}

	@Override
	public List<UserPhotoAlbum> getUserPhotoAlbums( final int userId ) {
		return userPhotoAlbumService.loadAllForEntry( userId );
	}

	@Override
	public Map<Integer, Integer> setUserPhotosCountByAlbums( final int userId ) {
		final Map<Integer, Integer> userPhotoAlbumsQtyMap = newLinkedHashMap();
		for ( final UserPhotoAlbum userPhotoAlbum : getUserPhotoAlbums( userId ) ) {
			userPhotoAlbumsQtyMap.put( userPhotoAlbum.getId(), userPhotoAlbumService.getUserPhotoAlbumPhotosQty( userPhotoAlbum.getId() ) );
		}
		return userPhotoAlbumsQtyMap;
	}

	@Override
	public AbstractPhotoListFactory getUserTeamMemberLastPhotos( final User user, final UserTeamMember userTeamMember, final User accessor ) {
		return photoListFactoryService.userTeamMemberPhotosLast( user, userTeamMember, accessor );
	}

	@Override
	public AbstractPhotoListFactory getUserPhotoAlbumLastPhotos( final User user, final UserPhotoAlbum userPhotoAlbum, final User accessor ) {
		return photoListFactoryService.userAlbumPhotosLast( user, userPhotoAlbum, accessor );
	}

	private UserCardGenreInfo getUserCardGenreInfo( final User user, final Genre genre, final User accessor ) {
		final int userId = user.getId();
		final int genreId = genre.getId();

		final UserCardGenreInfo genreInfo = new UserCardGenreInfo();

		genreInfo.setPhotosQty( photoService.getPhotosCountByUserAndGenre( userId, genreId ) );
		genreInfo.setVotingModel( userRankService.getVotingModel( userId, genreId, accessor, dateUtilsService.getCurrentTime() ) );
		genreInfo.setUserRankInGenre( userRankService.getUserRankInGenre( userId, genreId ) );

		final int userVotePointsForRankInGenre = userRankService.getUserVotePointsForRankInGenre( userId, genreId );
		genreInfo.setVotePointsForRankInGenre( userVotePointsForRankInGenre );

		genreInfo.setVotePointsToGetNextRankInGenre( userRankService.getVotePointsToGetNextRankInGenre( userVotePointsForRankInGenre ) );

		genreInfo.setUserRankIconContainer( userRankService.getUserRankIconContainer( user, genre ) );

		return genreInfo;
	}

	private PhotoList getUserPhotosByGenrePhotoList( final User user, final Genre genre ) {
		final User currentUser = EnvironmentContext.getCurrentUser();

		final SqlIdsSelectQuery idsSQL = new PhotoListQueryBuilder( dateUtilsService ).filterByAuthor( user ).filterByGenre( genre ).forPage( 1, getPhotosInLine() ).sortByUploadTimeDesc().getQuery();

		final List<Integer> ids = photoService.load( idsSQL ).getIds();

		final int photosByGenre = photoService.getPhotosCountByUserAndGenre( user.getId(), genre.getId() );
		final String title = translatorService.translate( "User card: $1: last photos. Total $2.", EnvironmentContext.getLanguage(), translatorService.translateGenre( genre, currentUser.getLanguage() ), String.valueOf( photosByGenre ) );

		final String link = urlUtilsService.getPhotosByUserByGenreLink( user.getId(), genre.getId() );

		return getPhotoList( 0, ids, link, title );
	}

	@Override
	public AbstractPhotoListFactory getUserPhotoListBest( final User user, final User accessor ) {
		return photoListFactoryService.userCardPhotosBest( user, accessor );
	}

	@Override
	public AbstractPhotoListFactory getUserPhotoListLast( final User user, final User accessor ) {
		return photoListFactoryService.userCardPhotosLast( user, accessor );
	}

	@Override
	public AbstractPhotoListFactory getPhotoListLastAppraised( final User user, final User accessor ) {
		return photoListFactoryService.userCardPhotosLastAppraised( user, accessor );
	}

	private PhotoList getPhotoList( final int photoListId, final List<Integer> photosIds, final String linkToFullPhotoList, final String listTitle ) {

		final PhotoList photoList = new PhotoList( photosIds, listTitle, false );
		photoList.setPhotoListId( photoListId );
		photoList.setLinkToFullList( linkToFullPhotoList );
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getNoPhotoGroupOperationMenuContainer() );

		return photoList;
	}

	private int getPhotosInLine() {
		return configurationService.getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY );
	}

	protected User getUser( final UserCardModel model ) {
		return model.getUser();
	}

	protected int getUserId( final UserCardModel model ) {
		return model.getUser().getId();
	}

	@Override
	public DateUtilsService getDateUtilsService() {
		return dateUtilsService;
	}
}
