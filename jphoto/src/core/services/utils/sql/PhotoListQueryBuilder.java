package core.services.utils.sql;

import core.general.data.PhotoSort;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserMembershipType;
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

	public PhotoListQueryBuilder uploadedAt( final Date uploadTime ) {
		final SqlColumnSelectable column = new SqlColumnSelect( table(), PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		query.addWhereAnd( new SqlCondition( column, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, dateUtilsService.getFirstSecondOfDay( uploadTime ), dateUtilsService ) );
		query.addWhereAnd( new SqlCondition( column, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, dateUtilsService.getLastSecondOfDay( uploadTime ), dateUtilsService ) );

		return this;
	}

	public PhotoListQueryBuilder uploadedBetween( final Date timeFrom, final Date timeTo ) {
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

	public PhotoListQueryBuilder forPage( final int page, final int itemsOnPage ) {
		query.setLimit( itemsOnPage );
		query.setOffset( PagingUtils.getPageItemStartIndex( page, itemsOnPage ) );

		return this;
	}

	public PhotoListQueryBuilder sortByUploadTime() {
		final SqlTable fPhoto = new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
		final SqlColumnSelect column = new SqlColumnSelect( fPhoto, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		query.addSortingDesc( column );

		return this;
	}

	public PhotoListQueryBuilder sortBySumMarks() {
		final SqlTable tPhotoVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tPhotoVotingColMark = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_MARK );
		final SqlColumnAggregate tPhotoVotingColSumMark = new SqlColumnAggregate( tPhotoVotingColMark, SqlFunctions.SUM, PhotoQueryServiceImpl.SUM_MARK_COLUMN_ALIAS );
		query.addSortingDesc( tPhotoVotingColSumMark );

		return this;
	}

	public PhotoListQueryBuilder sortByVotingTime() {
		final SqlTable tVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelectable sortColumn = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME );
		query.addSortingDesc( sortColumn );

		return this;
	}

/*	public PhotoListQueryBuilder sortBy( final PhotoSort sort ) {
		switch ( sort ) {
			case SUM_MARKS:
				final SqlTable tPhotoVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
				final SqlColumnSelect tPhotoVotingColMark = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_MARK );
				final SqlColumnAggregate tPhotoVotingColSumMark = new SqlColumnAggregate( tPhotoVotingColMark, SqlFunctions.SUM, PhotoQueryServiceImpl.SUM_MARK_COLUMN_ALIAS );
				query.addSortingDesc( tPhotoVotingColSumMark );
				// AND upload time - no return here!
			case UPLOAD_TIME:
				final SqlTable fPhoto = new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
				final SqlColumnSelect column = new SqlColumnSelect( fPhoto, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
				query.addSortingDesc( column );

				return this;
			case VOTING_TIME:
				final SqlTable tVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
				final SqlColumnSelectable sortColumn = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME );
				query.addSortingDesc( sortColumn );

				return this;
		}

		throw new IllegalArgumentException( String.format( "Illegal sort field: %s", sort ) );
	}*/

	private static SqlTable table() {
		return new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
	}
}
