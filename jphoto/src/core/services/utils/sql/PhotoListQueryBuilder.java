package core.services.utils.sql;

import core.enums.FavoriteEntryType;
import core.general.data.TimeRange;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.dao.*;
import core.services.photo.PhotoPreviewServiceImpl;
import core.services.utils.DateUtilsService;
import sql.builder.*;
import utils.PagingUtils;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoListQueryBuilder {

	private final DateUtilsService dateUtilsService;
	private SqlIdsSelectQuery query = new SqlIdsSelectQuery( table() );

	public PhotoListQueryBuilder( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}

	public SqlIdsSelectQuery getQuery() {
		return query;
	}

	public PhotoListQueryBuilder filterByAuthor( final User user ) {
		final SqlColumnSelectable column = new SqlColumnSelect( table(), PhotoDaoImpl.TABLE_COLUMN_USER_ID );
		query.addWhereAnd( new SqlCondition( column, SqlCriteriaOperator.EQUALS, user.getId(), dateUtilsService ) );

		return this;
	}

	public PhotoListQueryBuilder filterByGenre( final Genre genre ) {
		final SqlColumnSelectable column = new SqlColumnSelect( table(), PhotoDaoImpl.TABLE_COLUMN_GENRE_ID );
		query.addWhereAnd( new SqlCondition( column, SqlCriteriaOperator.EQUALS, genre.getId(), dateUtilsService ) );

		return this;
	}

	public PhotoListQueryBuilder uploadedOn( final Date uploadTime ) {
		final SqlColumnSelectable column = new SqlColumnSelect( table(), PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		query.addWhereAnd( new SqlCondition( column, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, dateUtilsService.getFirstSecondOfDay( uploadTime ), dateUtilsService ) );
		query.addWhereAnd( new SqlCondition( column, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, dateUtilsService.getLastSecondOfDay( uploadTime ), dateUtilsService ) );

		return this;
	}

	public PhotoListQueryBuilder filterByUploadTime( final Date timeFrom, final Date timeTo ) {
		final SqlColumnSelectable column = new SqlColumnSelect( table(), PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		query.addWhereAnd( new SqlCondition( column, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, dateUtilsService.getFirstSecondOfDay( timeFrom ), dateUtilsService ) );
		query.addWhereAnd( new SqlCondition( column, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, dateUtilsService.getLastSecondOfDay( timeTo ), dateUtilsService ) );
		return this;
	}

	public PhotoListQueryBuilder filterByMembershipType( final UserMembershipType userMembershipType ) {
		final SqlTable tPhotos = query.getMainTable();
		final SqlTable tUsers = new SqlTable( UserDaoImpl.TABLE_USERS );

		final SqlColumnSelect tPhotosColUserId = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tUsersColId = new SqlColumnSelect( tUsers, UserDaoImpl.ENTITY_ID );
		final SqlJoinCondition joinCondition = new SqlJoinCondition( tPhotosColUserId, tUsersColId );
		final SqlJoin join = SqlJoin.inner( tUsers, joinCondition );
		query.joinTable( join );

		final SqlColumnSelectable tUsersColMemberType = new SqlColumnSelect( tUsers, UserDaoImpl.TABLE_COLUMN_MEMBERSHIP_TYPE );
		final SqlLogicallyJoinable condition = new SqlCondition( tUsersColMemberType, SqlCriteriaOperator.EQUALS, userMembershipType.getId(), dateUtilsService );
		query.addWhereAnd( condition );

		return this;
	}

	public PhotoListQueryBuilder filterByUserAlbum( final UserPhotoAlbum userPhotoAlbum ) {
		final SqlTable tPhoto = query.getMainTable();
		final SqlTable tUserPhotoAlbumPhotos = new SqlTable( UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS );

		final SqlColumnSelect tPhotoColId = new SqlColumnSelect( tPhoto, BaseEntityDao.ENTITY_ID );
		final SqlColumnSelect tUserPhotoAlbumColPhotoId = new SqlColumnSelect( tUserPhotoAlbumPhotos, UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS_COL_PHOTO_ID );

		final SqlJoin join = SqlJoin.leftOuter( tUserPhotoAlbumPhotos, new SqlJoinCondition( tPhotoColId, tUserPhotoAlbumColPhotoId ) );
		query.joinTable( join );

		final SqlColumnSelectable tUserPhotoAlbumColAlbumId = new SqlColumnSelect( tUserPhotoAlbumPhotos, UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS_COL_ALBUM_ID );
		final SqlLogicallyJoinable condition = new SqlCondition( tUserPhotoAlbumColAlbumId, SqlCriteriaOperator.EQUALS, userPhotoAlbum.getId(), dateUtilsService );
		query.addWhereAnd( condition );

		return this;
	}

	public PhotoListQueryBuilder filterByUserTeamMember( final UserTeamMember userTeamMember ) {
		final SqlTable tPhoto = query.getMainTable();
		final SqlTable tPhotoTeam = new SqlTable( UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM );

		final SqlColumnSelect tPhotoColId = new SqlColumnSelect( tPhoto, BaseEntityDao.ENTITY_ID );
		final SqlColumnSelect tPhotoTeamColPhotoId = new SqlColumnSelect( tPhotoTeam, UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM_COL_PHOTO_ID );

		final SqlJoin join = SqlJoin.leftOuter( tPhotoTeam, new SqlJoinCondition( tPhotoColId, tPhotoTeamColPhotoId ) );
		query.joinTable( join );

		final SqlColumnSelectable tPhotoTeamColUserTeamMemberId = new SqlColumnSelect( tPhotoTeam, UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM_COL_USER_TEAM_MEMBER_ID );
		final SqlLogicallyJoinable condition = new SqlCondition( tPhotoTeamColUserTeamMemberId, SqlCriteriaOperator.EQUALS, userTeamMember.getId(), dateUtilsService );
		query.addWhereAnd( condition );

		return this;
	}

	public PhotoListQueryBuilder filterOnlyPhotosAddedByUserToBookmark( final User user, final FavoriteEntryType favoriteEntryType ) {
		final SqlTable tPhotos = query.getMainTable();
		final SqlTable tFavor = new SqlTable( FavoritesDaoImpl.TABLE_FAVORITES );

		final SqlColumnSelect tPhotosColId = new SqlColumnSelect( tPhotos, BaseEntityDao.ENTITY_ID );
		final SqlColumnSelect tFavColEntryId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_FAVORITE_ENTRY_ID );
		final SqlJoinCondition joinCondition = new SqlJoinCondition( tPhotosColId, tFavColEntryId );
		final SqlJoin join = SqlJoin.inner( tFavor, joinCondition );
		query.joinTable( join );

		final SqlColumnSelect tFavColEntryUserId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tFavColEntryType = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_ENTRY_TYPE );
		final SqlLogicallyJoinable con1 = new SqlCondition( tFavColEntryUserId, SqlCriteriaOperator.EQUALS, user.getId(), dateUtilsService );
		final SqlLogicallyJoinable con2 = new SqlCondition( tFavColEntryType, SqlCriteriaOperator.EQUALS, favoriteEntryType.getId(), dateUtilsService );
		final SqlLogicallyJoinable condList = new SqlLogicallyAnd( con1, con2 );
		query.setWhere( condList );

		final SqlColumnSelect column = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_CREATED );
		query.addSortingDesc( column );

		return this;
	}

	public PhotoListQueryBuilder filterByVotingTime( final TimeRange timeRange ) {
		return filterByVotingTime( timeRange.getTimeFrom(), timeRange.getTimeTo() );
	}

	public PhotoListQueryBuilder filterByVotingTime( final Date votingTimeFrom, final Date votingTimeTo ) {

		if ( ! hasVotingTableJoin() ) {
			addJoinVotingTable();
		}

		final SqlTable tVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tVotingColVotingTime = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME );

		final SqlCondition moreThenDateFrom = new SqlCondition( tVotingColVotingTime, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, dateUtilsService.getFirstSecondOfDay( votingTimeFrom ), dateUtilsService );
		query.addWhereAnd( moreThenDateFrom );

		final SqlCondition lessThenDateTo = new SqlCondition( tVotingColVotingTime, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, dateUtilsService.getLastSecondOfDay( votingTimeTo ), dateUtilsService );
		query.addWhereAnd( lessThenDateTo );

		return this;
	}

	public PhotoListQueryBuilder filterByPreviewTime( final TimeRange timeRange ) {
		return filterByPreviewTime( timeRange.getTimeFrom(), timeRange.getTimeTo() );
	}

	public PhotoListQueryBuilder filterByPreviewTime( final Date votingTimeFrom, final Date votingTimeTo ) {

		if ( ! hasPreviewsTableJoin() ) {
			addJoinPreviewsTable();
		}

		final SqlTable tPreviews = new SqlTable( PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW );
		final SqlColumnSelect tPreviewsColViewTime = new SqlColumnSelect( tPreviews, PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW_COLUMN_PREVIEW_TIME );

		final SqlCondition moreThenDateFrom = new SqlCondition( tPreviewsColViewTime, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, dateUtilsService.getFirstSecondOfDay( votingTimeFrom ), dateUtilsService );
		query.addWhereAnd( moreThenDateFrom );

		final SqlCondition lessThenDateTo = new SqlCondition( tPreviewsColViewTime, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, dateUtilsService.getLastSecondOfDay( votingTimeTo ), dateUtilsService );
		query.addWhereAnd( lessThenDateTo );

		return this;
	}

	public PhotoListQueryBuilder filterByMinimalMarks( final int marks ) {

		if ( ! hasVotingTableJoin() ) {
			addJoinVotingTable();
		}

		final SqlTable tPhotoVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tPhotoVotingColMark = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_MARK );
		final SqlColumnAggregate tPhotoVotingColSumMark = new SqlColumnAggregate( tPhotoVotingColMark, SqlFunctions.SUM, PhotoQueryServiceImpl.SUM_MARK_COLUMN_ALIAS );
		final SqlCondition havingSumMarkMoreThen = new SqlCondition( tPhotoVotingColSumMark, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, marks, dateUtilsService );
		query.setHaving( havingSumMarkMoreThen );

		return this;
	}

	public PhotoListQueryBuilder filterByVotingCategory( final PhotoVotingCategory votingCategory ) {

		if ( ! hasVotingTableJoin() ) {
			addJoinVotingTable();
		}

		final SqlTable tVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );

		final SqlColumnSelect tVotingColVotingCategory = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_VOTING_CATEGORY_ID );
		final SqlCondition condition = new SqlCondition( tVotingColVotingCategory, SqlCriteriaOperator.EQUALS, votingCategory.getId(), dateUtilsService );
		query.addWhereAnd( condition );

		return this;
	}

	public PhotoListQueryBuilder filterByVotedUser( final User user ) {

		if ( ! hasVotingTableJoin() ) {
			addJoinVotingTable();
		}

		final SqlTable tVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );

		final SqlColumnSelect tVotingColVotedUserId = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_USER_ID );
		final SqlCondition condition = new SqlCondition( tVotingColVotedUserId, SqlCriteriaOperator.EQUALS, user.getId(), dateUtilsService );
		query.addWhereAnd( condition );

		return this;
	}

	public PhotoListQueryBuilder forPage( final int page, final int itemsOnPage ) {
		query.setLimit( itemsOnPage );
		query.setOffset( PagingUtils.getPageItemStartIndex( page, itemsOnPage ) );

		return this;
	}

	public PhotoListQueryBuilder sortByUploadTimeAsc() {
		final SqlTable fPhoto = new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
		final SqlColumnSelect column = new SqlColumnSelect( fPhoto, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		query.addSortingAsc( column );

		return this;
	}

	public PhotoListQueryBuilder sortByPreviewsCountDesc() {

		if ( ! hasPreviewsTableJoin() ) {
			addJoinPreviewsTable();
		}

		final SqlTable tPhotos = new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );

		final SqlColumnSelect tPhotoColId = new SqlColumnSelect( tPhotos, UserDaoImpl.ENTITY_ID );

		final SqlTable tPreviews = new SqlTable( PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW );
//		final SqlColumnSelect tPreviewsColPhotoId = new SqlColumnSelect( tPreviews, PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID );

//		final SqlJoin join2 = SqlJoin.inner( tPreviews, new SqlJoinCondition( tPhotoColId, tPreviewsColPhotoId ) );
//		query.joinTable( join2 );

		final SqlColumnSelect tPreviewsColId = new SqlColumnSelect( tPreviews, PhotoCommentDaoImpl.ENTITY_ID );
		final SqlColumnSelectable tableSortColumn = new SqlColumnAggregate( tPreviewsColId, SqlFunctions.COUNT, "viewsCount" );

		final List<SqlBuildable> groupBy2 = newArrayList();
		groupBy2.add( tPhotoColId );

		query.setGroupColumns( groupBy2 );

		query.addSorting( tableSortColumn, SqlSortOrder.DESC );

		return this;
	}

	public PhotoListQueryBuilder sortByUploadTimeDesc() {
		final SqlTable fPhoto = new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
		final SqlColumnSelect column = new SqlColumnSelect( fPhoto, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		query.addSortingDesc( column );

		return this;
	}

	public PhotoListQueryBuilder sortBySumMarksDesc() {
		final SqlTable tPhotoVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tPhotoVotingColMark = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_MARK );
		final SqlColumnAggregate tPhotoVotingColSumMark = new SqlColumnAggregate( tPhotoVotingColMark, SqlFunctions.SUM, PhotoQueryServiceImpl.SUM_MARK_COLUMN_ALIAS );
		query.addSortingDesc( tPhotoVotingColSumMark );

		sortByUploadTimeDesc();

		return this;
	}

	public PhotoListQueryBuilder sortByVotingTimeDesc() {

		if ( ! hasVotingTableJoin() ) {
			addJoinVotingTable();
		}

		final SqlTable tVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelectable sortColumn = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME );
		query.addSortingDesc( sortColumn );

		return this;
	}

	public SqlIdsSelectQuery getPhotosOfUserFavoritesMembers( final User user, final int page, final int itemsOnPage ) {
		final SqlIdsSelectQuery selectQuery = new PhotoListQueryBuilder( dateUtilsService ).forPage( page, itemsOnPage ).sortByUploadTimeDesc().getQuery();

		final SqlTable tPhotos = selectQuery.getMainTable();
		final SqlTable tFavor = new SqlTable( FavoritesDaoImpl.TABLE_FAVORITES );

		final SqlColumnSelect tPhotosColUserId = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tFavColEntryId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_FAVORITE_ENTRY_ID );
		final SqlJoinCondition joinCondition = new SqlJoinCondition( tPhotosColUserId, tFavColEntryId );
		final SqlJoin join = SqlJoin.inner( tFavor, joinCondition );
		selectQuery.joinTable( join );

		final SqlColumnSelect tFavColEntryUserId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tFavColEntryType = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_ENTRY_TYPE );
		final SqlLogicallyJoinable con1 = new SqlCondition( tFavColEntryUserId, SqlCriteriaOperator.EQUALS, user.getId(), dateUtilsService );
		final SqlLogicallyJoinable con2 = new SqlCondition( tFavColEntryType, SqlCriteriaOperator.EQUALS, FavoriteEntryType.FAVORITE_MEMBERS.getId(), dateUtilsService );
		final SqlLogicallyJoinable condList = new SqlLogicallyAnd( con1, con2 );
		selectQuery.setWhere( condList );

		return selectQuery;
	}

	private boolean hasPreviewsTableJoin() {
		return hasJoinedTable( PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW );
	}

	private boolean hasVotingTableJoin() {
		return hasJoinedTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
	}

	private boolean hasJoinedTable( final String tablePhotoVoting ) {
		for ( final SqlJoin sqlJoin : query.getJoins() ) {
			if ( sqlJoin.getJoinTable().getName().equals( tablePhotoVoting ) ) {
				return true;
			}
		}

		return false;
	}

	private void addJoinVotingTable() {
		final SqlTable tPhotoVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tPhotoColId = new SqlColumnSelect( query.getMainTable(), BaseEntityDao.ENTITY_ID );
		final SqlColumnSelect tPhotoVotingColPhotoId = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_PHOTO_ID );
		final SqlJoin joinVotingTable = SqlJoin.inner( tPhotoVoting, new SqlJoinCondition( tPhotoColId, tPhotoVotingColPhotoId ) );
		query.joinTable( joinVotingTable );
		query.addGrouping( tPhotoColId );
	}

	private void addJoinPreviewsTable() {
		final SqlTable tPhotoPreviews = new SqlTable( PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW );
		final SqlColumnSelect tPhotoColId = new SqlColumnSelect( query.getMainTable(), BaseEntityDao.ENTITY_ID );
		final SqlColumnSelect tPhotoPreviewsColPhotoId = new SqlColumnSelect( tPhotoPreviews, PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID );
		final SqlJoin joinPreviewsTable = SqlJoin.inner( tPhotoPreviews, new SqlJoinCondition( tPhotoColId, tPhotoPreviewsColPhotoId ) );
		query.joinTable( joinPreviewsTable );
		query.addGrouping( tPhotoColId );
	}

	private static SqlTable table() {
		return new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
	}
}
