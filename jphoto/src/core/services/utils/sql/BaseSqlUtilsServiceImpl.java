package core.services.utils.sql;

import core.services.dao.GenreDaoImpl;
import core.services.dao.UserDaoImpl;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;
import utils.PagingUtils;

public class BaseSqlUtilsServiceImpl implements BaseSqlUtilsService {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public void initLimitAndOffset( final BaseSqlSelectQuery sqlSelectQuery, final int page, final int itemsOnPage ) {
		final int firstElementIndex = PagingUtils.getPageItemStartIndex( page, itemsOnPage );
		sqlSelectQuery.setLimit( itemsOnPage );
		sqlSelectQuery.setOffset( firstElementIndex );
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
	public void addUserSortByName( final BaseSqlSelectQuery selectQuery ) {
		final SqlTable tUser = new SqlTable( UserDaoImpl.TABLE_USERS );
		final SqlColumnSelectable sortColumn = new SqlColumnSelect( tUser, UserDaoImpl.TABLE_COLUMN_NAME );
		selectQuery.addSortingAsc( sortColumn );
	}

	private SqlCondition getCondition( final String tableName, final String columnName, final long value, final SqlCriteriaOperator operator ) {
		final SqlTable table = new SqlTable( tableName );
		final SqlColumnSelectable column = new SqlColumnSelect( table, columnName );

		return new SqlCondition( column, operator, value, dateUtilsService );
	}
}
