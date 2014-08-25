package core.services.utils.sql;

import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.dao.BaseEntityDao;
import core.services.dao.PhotoDaoImpl;
import core.services.dao.PhotoVotingDaoImpl;
import core.services.dao.UserDaoImpl;
import core.services.utils.DateUtilsService;
import sql.builder.*;
import utils.PagingUtils;

import java.util.Date;

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

	public PhotoListQueryBuilder filterByVotingTime( final Date votingTimeFrom, final Date votingTimeTo ) {

		addJoinVotingTable();

		final SqlTable tVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tVotingColVotingTime = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME );

		final SqlCondition moreThenDateFrom = new SqlCondition( tVotingColVotingTime, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, dateUtilsService.getFirstSecondOfDay( votingTimeFrom ), dateUtilsService );
		query.addWhereAnd( moreThenDateFrom );

		final SqlCondition lessThenDateTo = new SqlCondition( tVotingColVotingTime, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, dateUtilsService.getLastSecondOfDay( votingTimeTo ), dateUtilsService );
		query.addWhereAnd( lessThenDateTo );

		return this;
	}

	public PhotoListQueryBuilder votingForLastDays( final int days ) {
		return filterByVotingTime( dateUtilsService.getDatesOffsetFromCurrentDate( days ), dateUtilsService.getCurrentTime() );
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

	private boolean hasVotingTableJoin() {
		for ( final SqlJoin sqlJoin : query.getJoins() ) {
			if ( sqlJoin.getJoinTable().getName().equals( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING ) ) {
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

	private static SqlTable table() {
		return new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
	}
}
