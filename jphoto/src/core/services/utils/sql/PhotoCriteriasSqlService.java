package core.services.utils.sql;

import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import sql.builder.BaseSqlSelectQuery;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;

public interface PhotoCriteriasSqlService {

	SqlIdsSelectQuery getForCriteriasPagedIdsSQL( final PhotoListCriterias criterias, final int page, final int itemsOnPage );

	SqlIdsSelectQuery getForCriteriasPagedIdsSQL( final PhotoListCriterias criterias, final PagingModel pagingModel );

	void addUserCriteria( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery );

	void addGenreCriteria( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery );

	void addUploadTimeCriteria( final Date timeFrom, final Date timeTo, final BaseSqlSelectQuery selectQuery );

	void addVotingCriteria( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery );

	void addFilterByMembershipType( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery );

	void addSortCriterias( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery );

	void addLimitCriterias( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery, final PagingModel pagingModel );

	void addLimitCriterias( final PhotoListCriterias criterias, final SqlIdsSelectQuery selectQuery, final int page, final int itemsOnPage );
}
