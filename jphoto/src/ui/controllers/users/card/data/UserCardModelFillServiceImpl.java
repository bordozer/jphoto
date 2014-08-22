package ui.controllers.users.card.data;

import core.general.base.PagingModel;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoListCriterias;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.services.dao.ActivityStreamDaoImpl;
import core.services.dao.PhotoDaoImpl;
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
import core.services.utils.sql.PhotoCriteriasSqlService;
import core.services.utils.sql.PhotoSqlHelperService;
import org.apache.commons.lang.StringUtils;
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
import utils.StringUtilities;

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
	private PhotoSqlHelperService photoSqlHelperService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private EntryMenuService entryMenuService;

	@Autowired
	private GroupOperationService groupOperationService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PhotoCriteriasSqlService photoCriteriasSqlService;

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
			if ( photoService.getPhotoQtyByUserAndGenre( user.getId(), genre.getId() ) > 0 || userRankService.getUserRankInGenre( user.getId(), genre.getId() ) > 0 ) {
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
	public PhotoList getUserPhotoAlbumLastPhotos( final int userId, final UserPhotoAlbum userPhotoAlbum, final Map<UserPhotoAlbum, Integer> userPhotoAlbumsQtyMap ) {
		final int photosQty = userPhotoAlbumsQtyMap.get( userPhotoAlbum );

		final String photoListTitle = translatorService.translate( "User team: Last photos from album '$1' - $2 photos"
			, EnvironmentContext.getLanguage()
			, StringUtilities.escapeHtml( userPhotoAlbum.getName() )
			, String.valueOf( photosQty )
		);

		final String userTeamMemberCardLink = urlUtilsService.getUserPhotoAlbumPhotosLink( userId, userPhotoAlbum.getId() );

		final SqlIdsSelectQuery selectIdsQuery = photoSqlHelperService.getUserPhotoAlbumLastPhotosQuery( userId, userPhotoAlbum.getId(), getPagingModel() );

		final PhotoList customPhotoList = getCustomPhotoList( selectIdsQuery, photoListTitle, userTeamMemberCardLink );
		customPhotoList.setPhotoListId( userPhotoAlbum.getId() );
		return customPhotoList;
	}

	private UserCardGenreInfo getUserCardGenreInfo( final User user, final Genre genre, final User accessor ) {
		final int userId = user.getId();
		final int genreId = genre.getId();

		final UserCardGenreInfo genreInfo = new UserCardGenreInfo();

		genreInfo.setPhotosQty( photoService.getPhotoQtyByUserAndGenre( userId, genreId ) );
		genreInfo.setVotingModel( userRankService.getVotingModel( userId, genreId, accessor, dateUtilsService.getCurrentTime() ) );
		genreInfo.setUserRankInGenre( userRankService.getUserRankInGenre( userId, genreId ) );

		final int userVotePointsForRankInGenre = userRankService.getUserVotePointsForRankInGenre( userId, genreId );
		genreInfo.setVotePointsForRankInGenre( userVotePointsForRankInGenre );

		genreInfo.setVotePointsToGetNextRankInGenre( userRankService.getVotePointsToGetNextRankInGenre( userVotePointsForRankInGenre ) );

		genreInfo.setUserRankIconContainer( userRankService.getUserRankIconContainer( user, genre ) );

		return genreInfo;
	}

	private PhotoList getCustomPhotoList( final SqlIdsSelectQuery selectIdsQuery, final String photoListTitle, final String userTeamMemberCardLink ) {
		final SqlSelectIdsResult selectIdsResult = photoService.load( selectIdsQuery );

		final PhotoList photoList = new PhotoList( selectIdsResult.getIds(), photoListTitle, false );
		photoList.setLinkToFullList( userTeamMemberCardLink );
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getNoPhotoGroupOperationMenuContainer() );

		return photoList;
	}

	private PhotoList getUserPhotosByGenrePhotoList( final User user, final Genre genre ) {
		final User currentUser = EnvironmentContext.getCurrentUser();

		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenre( user, genre, currentUser );
		final SqlIdsSelectQuery idsSQL = photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, getPagingModel() );

		final List<Integer> ids = photoService.load( idsSQL ).getIds();

		final int photosByGenre = photoService.getPhotoQtyByUserAndGenre( user.getId(), genre.getId() );
		final String title = translatorService.translate( "User card: $1: last photos. Total $2.", EnvironmentContext.getLanguage(), translatorService.translateGenre( genre, currentUser.getLanguage() ), String.valueOf( photosByGenre ) );

		final String link = urlUtilsService.getPhotosByUserByGenreLink( user.getId(), genre.getId() );

		return getPhotoList( 0, ids, link, title );
	}

	private PhotoList getUserBestPhotosByGenrePhotoList( final User user, final Genre genre ) {
		final User currentUser = EnvironmentContext.getCurrentUser();

		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardUserPhotosBest( user, currentUser );
		final SqlIdsSelectQuery selectIdsQuery = photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, getPagingModel() );

		final SqlColumnSelectable tPhotoColGenreId = new SqlColumnSelect( new SqlTable( PhotoDaoImpl.TABLE_PHOTOS ), PhotoDaoImpl.TABLE_COLUMN_GENRE_ID );
		final SqlLogicallyJoinable genreCondition = new SqlCondition( tPhotoColGenreId, SqlCriteriaOperator.EQUALS, genre.getId(), dateUtilsService );
		selectIdsQuery.addWhereAnd( genreCondition );

		final String linkBest = urlUtilsService.getPhotosByUserByGenreLinkBest( user.getId(), genre.getId() );

		final int photosByGenre = photoService.getPhotoQtyByUserAndGenre( user.getId(), genre.getId() );
		final String listTitle = translatorService.translate( "User card: $1: the best photos. Total $2", EnvironmentContext.getLanguage(), entityLinkUtilsService.getPhotosByUserByGenreLink( user, genre, currentUser.getLanguage() ), String.valueOf( photosByGenre ) );

		final SqlSelectIdsResult sqlSelectIdsResult = photoService.load( selectIdsQuery );

		return getPhotoList( 2, sqlSelectIdsResult.getIds(), linkBest, listTitle );
	}

	@Override
	public PhotoList getBestUserPhotoList( final User user ) {
		final User currentUser = EnvironmentContext.getCurrentUser();
		final List<Integer> photos = photoService.getBestUserPhotosIds( user, getPhotosInLine(), currentUser );

		final String linkBest = urlUtilsService.getPhotosByUserLinkBest( user.getId() );
		final String listTitle = translatorService.translate( "User card: The very best of $1", EnvironmentContext.getLanguage(), user.getNameEscaped() );

		return getPhotoList( 3, photos, linkBest, listTitle );
	}

	@Override
	public PhotoList getLastUserPhotoList( final User user ) {
		final User currentUser = EnvironmentContext.getCurrentUser();
		final List<Integer> photos = photoService.getLastUserPhotosIds( user, getPhotosInLine(), currentUser );

		final String linkBest = urlUtilsService.getPhotosByUserLink( user.getId() );
		final String listTitle = translatorService.translate( "User card: Last photos of $1", EnvironmentContext.getLanguage(), user.getNameEscaped() );
		//		photoUIService.hidePhotoPreviewForAnonymouslyPostedPhotos( photoList.getPhotoInfos() );

		return getPhotoList( 4, photos, linkBest, listTitle );
	}

	@Override
	public PhotoList getLastVotedPhotoList( final User user ) {
		final List<Integer> photos = photoService.getLastVotedPhotosIds( user, getPhotosInLine(), EnvironmentContext.getCurrentUser() );

		final String linkBest = urlUtilsService.getPhotosVotedByUserLink( user.getId() );
		final String listTitle = translatorService.translate( "User card: The photos $1 has appraised recently", EnvironmentContext.getLanguage(), user.getNameEscaped() );
		return getPhotoList( 5, photos, linkBest, listTitle );
	}

	@Override
	public PhotoList getLastPhotosOfUserVisitors( final User user ) {
		final List<Integer> photos = photoService.getLastPhotosOfUserVisitors( user, getPhotosInLine() );

		final String linkBest = StringUtils.EMPTY;
		final String listTitle = translatorService.translate( "User card: Last photos of visitors who viewed $1's photos recently", EnvironmentContext.getLanguage(), user.getNameEscaped() );
		return getPhotoList( 6, photos, linkBest, listTitle );
	}

	private PhotoList getPhotoList( final int photoListId, final List<Integer> photosIds, final String linkToFullPhotoList, final String listTitle ) {

		final PhotoList photoList = new PhotoList( photosIds, listTitle, false );
		photoList.setPhotoListId( photoListId );
		photoList.setLinkToFullList( linkToFullPhotoList );
		photoList.setPhotoGroupOperationMenuContainer( groupOperationService.getNoPhotoGroupOperationMenuContainer() );

		return photoList;
	}

	private PagingModel getPagingModel() {
		final PagingModel pagingModel = new PagingModel( services );
		pagingModel.setCurrentPage( 1 );
		pagingModel.setItemsOnPage( getPhotosInLine() );
		return pagingModel;
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
