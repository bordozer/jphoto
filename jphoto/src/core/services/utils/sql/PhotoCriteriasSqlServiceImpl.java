package core.services.utils.sql;

import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import core.services.dao.PhotoDaoImpl;
import core.services.dao.PhotoVotingDaoImpl;
import core.services.dao.UserDaoImpl;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;

import java.util.Date;

public class PhotoCriteriasSqlServiceImpl implements PhotoCriteriasSqlService {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Autowired
	private PhotoSqlFilterService photoSqlFilterService;

	@Override
	public SqlIdsSelectQuery getForCriteriasPagedIdsSQL( final PhotoListCriterias criterias, final PagingModel pagingModel ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();

		addUserCriteria( criterias, selectQuery );

		addGenreCriteria( criterias, selectQuery );

		addUploadTimeCriteria( criterias.getUploadDateFrom(), criterias.getUploadDateTo(), selectQuery );

		addFilterByMembershipType( criterias, selectQuery );

		addVotingCriteria( criterias, selectQuery );

		addSortCriterias( criterias, selectQuery );

		addLimitCriterias( criterias, selectQuery, pagingModel );

		return selectQuery;
	}

	@Override
	public void addUserCriteria( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery ) {
		if ( criterias.getUser() != null ) {
			photoSqlFilterService.addFilterByUser( criterias.getUser().getId(), selectQuery );
		}
	}

	@Override
	public void addGenreCriteria( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery ) {
		if ( criterias.getGenre() != null ) {
			photoSqlFilterService.addFilterByGenre( criterias.getGenre().getId(), selectQuery );
		}
	}

	@Override
	public void addUploadTimeCriteria( final Date timeFrom, final Date timeTo, final BaseSqlSelectQuery selectQuery ) {
		final SqlTable tPhotos = selectQuery.getMainTable();
		final SqlColumnSelect tPhotosColUploadTime = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );

		if ( timeFrom != null && timeFrom.getTime() > 0 ) {
			final SqlCondition moreThenDateFrom = new SqlCondition( tPhotosColUploadTime, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, timeFrom, dateUtilsService );
			selectQuery.addWhereAnd( moreThenDateFrom );
		}

		if ( timeFrom != null && timeTo.getTime() > 0 ) {
			final SqlCondition lessThenDateTo = new SqlCondition( tPhotosColUploadTime, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, timeTo, dateUtilsService );
			selectQuery.addWhereAnd( lessThenDateTo );
		}
	}

	@Override
	public void addVotingCriteria( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery ) {
		final int minMarks = criterias.getMinimalMarks();

		if ( ! criterias.hasVotingCriterias() ) {
			return;
		}

		photoSqlFilterService.addJoinWithPhotoVotingTable( selectQuery );

		if ( minMarks > PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK ) {
			photoSqlFilterService.addFilterByMinVotedMark( selectQuery, minMarks );
		}

		final SqlTable tVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tVotingColVotingTime = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME );

		final Date votingTimeFrom = criterias.getVotingTimeFrom();
		if ( votingTimeFrom  != null && votingTimeFrom.getTime() > 0 ) { // TODO
			final SqlCondition moreThenDateFrom = new SqlCondition( tVotingColVotingTime, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, votingTimeFrom, dateUtilsService );
			selectQuery.addWhereAnd( moreThenDateFrom );
		}

		final Date votingTimeTo = criterias.getVotingTimeTo();
		if ( votingTimeTo != null && votingTimeTo.getTime() > 0 ) { // TODO
			final SqlCondition lessThenDateTo = new SqlCondition( tVotingColVotingTime, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, votingTimeTo, dateUtilsService );
			selectQuery.addWhereAnd( lessThenDateTo );
		}

		if ( criterias.getVotingCategory() != null ) {
			final SqlColumnSelect tVotingColVotingCategory = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_VOTING_CATEGORY_ID );
			final SqlCondition condition = new SqlCondition( tVotingColVotingCategory, SqlCriteriaOperator.EQUALS, criterias.getVotingCategory().getId(), dateUtilsService );
			selectQuery.addWhereAnd( condition );
		}

		if ( criterias.getVotedUser() != null ) {
			final SqlColumnSelect tVotingColVotedUserId = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_USER_ID );
			final SqlCondition condition = new SqlCondition( tVotingColVotedUserId, SqlCriteriaOperator.EQUALS, criterias.getVotedUser().getId(), dateUtilsService );
			selectQuery.addWhereAnd( condition );
		}
	}

	@Override
	public void addFilterByMembershipType( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery ) {
		if ( criterias.getMembershipType() == null ) {
			return;
		}
		final SqlTable tPhotos = selectQuery.getMainTable();
		final SqlTable tUsers = new SqlTable( UserDaoImpl.TABLE_USERS );

		final SqlColumnSelect tPhotosColUserId = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tUsersColId = new SqlColumnSelect( tUsers, UserDaoImpl.ENTITY_ID );
		final SqlJoinCondition joinCondition = new SqlJoinCondition( tPhotosColUserId, tUsersColId );
		final SqlJoin join = SqlJoin.inner( tUsers, joinCondition );
		selectQuery.joinTable( join );

		final SqlColumnSelectable tUsersColMemberType = new SqlColumnSelect( tUsers, UserDaoImpl.TABLE_COLUMN_MEMBERSHIP_TYPE );
		final SqlLogicallyJoinable condition = new SqlCondition( tUsersColMemberType, SqlCriteriaOperator.EQUALS, criterias.getMembershipType().getId(), dateUtilsService );
		selectQuery.addWhereAnd( condition );
	}

	@Override
	public void addSortCriterias( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery ) {

		switch ( criterias.getPhotoSort() ) {
			case UPLOAD_TIME:
				baseSqlUtilsService.addDescSortByUploadTimeDesc( selectQuery );
				return;
			case SUM_MARKS:
				baseSqlUtilsService.addSortBySumVotingMarksDesc( selectQuery );
				baseSqlUtilsService.addDescSortByUploadTimeDesc( selectQuery );
				return;
			case VOTING_TIME:
				baseSqlUtilsService.addSortBySumVotingTimeDesc( selectQuery );
				return;
		}

		throw new IllegalArgumentException( String.format( "Illegal sort field: %s", criterias.getPhotoSort() ) );
	}

	@Override
	public void addLimitCriterias( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery, final PagingModel pagingModel ) {
		if ( criterias.isTopBestPhotoList() ) {
			selectQuery.setLimit( criterias.getPhotoQtyLimit() );
		} else {
			baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel );
		}
	}
}
