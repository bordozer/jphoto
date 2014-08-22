package core.services.utils.sql;

import core.general.base.PagingModel;
import core.services.dao.GenreDaoImpl;
import core.services.dao.PhotoDaoImpl;
import core.services.dao.PhotoVotingDaoImpl;
import core.services.dao.UserDaoImpl;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;
import utils.PagingUtils;

public class BaseSqlUtilsServiceImpl implements BaseSqlUtilsService {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public void initLimitAndOffset( final BaseSqlSelectQuery sqlSelectQuery, final PagingModel pagingModel ) {
		initLimitAndOffset( sqlSelectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
	}

	@Override
	public void initLimitAndOffset( final BaseSqlSelectQuery sqlSelectQuery, final int page, final int itemsOnPage ) {
		final int firstElementIndex = PagingUtils.getPageItemStartIndex( page, itemsOnPage );
		sqlSelectQuery.setLimit( itemsOnPage );
		sqlSelectQuery.setOffset( firstElementIndex );
	}

	@Override
	public SqlIdsSelectQuery getPhotosIdsSQL() {
		return new SqlIdsSelectQuery( new SqlTable( PhotoDaoImpl.TABLE_PHOTOS ) );
	}

	@Override
	public SqlIdsSelectQuery getUsersIdsSQL() {
		return new SqlIdsSelectQuery( new SqlTable( UserDaoImpl.TABLE_USERS ) );
	}

	@Override
	public SqlIdsSelectQuery getGenresIdsSQL() {
		return new SqlIdsSelectQuery( new SqlTable( GenreDaoImpl.TABLE_GENRES ) );
	}

	@Override
	public SqlLogicallyJoinable equalsCondition( final String tableName, final String columnName, final long value ) {
		return getCondition( tableName, columnName, value, SqlCriteriaOperator.EQUALS );
	}

	private SqlCondition getCondition( final String tableName, final String columnName, final long value, final SqlCriteriaOperator operator ) {
		final SqlTable table = new SqlTable( tableName );
		final SqlColumnSelectable column = new SqlColumnSelect( table, columnName );

		return new SqlCondition( column, operator, value, dateUtilsService );
	}

	@Override
	public void addDescSortByUploadTimeDesc( final BaseSqlSelectQuery selectQuery ) {
		final SqlTable fPhoto = new SqlTable( PhotoDaoImpl.TABLE_PHOTOS );
		final SqlColumnSelect column = new SqlColumnSelect( fPhoto, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		selectQuery.addSortingDesc( column );
	}

	@Override
	public void addSortBySumVotingMarksDesc( final BaseSqlSelectQuery selectQuery ) {
		final SqlTable tPhotoVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tPhotoVotingColMark = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_MARK );
		final SqlColumnAggregate tPhotoVotingColSumMark = new SqlColumnAggregate( tPhotoVotingColMark, SqlFunctions.SUM, PhotoSqlHelperServiceImpl.SUM_MARK_COLUMN_ALIAS );
		selectQuery.addSortingDesc( tPhotoVotingColSumMark );
	}

	@Override
	public void addSortBySumVotingTimeDesc( final BaseSqlSelectQuery selectQuery ) {
		final SqlTable tVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelectable sortColumn = new SqlColumnSelect( tVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME );
		selectQuery.addSortingDesc( sortColumn );
	}

	@Override
	public void addUserSortByName( final BaseSqlSelectQuery selectQuery ) {
		final SqlTable tUser = new SqlTable( UserDaoImpl.TABLE_USERS );
		final SqlColumnSelectable sortColumn = new SqlColumnSelect( tUser, UserDaoImpl.TABLE_COLUMN_NAME );
		selectQuery.addSortingAsc( sortColumn );
	}
}
