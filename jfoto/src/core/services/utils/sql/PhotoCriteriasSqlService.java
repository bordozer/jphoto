package core.services.utils.sql;

import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import sql.builder.BaseSqlSelectQuery;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;

public interface PhotoCriteriasSqlService {

	SqlIdsSelectQuery getForCriteriasPagedIdsSQL( PhotoListCriterias criterias, PagingModel pagingModel );

	void addUserCriteria( PhotoListCriterias criterias, SqlIdsSelectQuery selectQuery );

	void addGenreCriteria( PhotoListCriterias criterias, SqlIdsSelectQuery selectQuery );

	void addUploadTimeCriteria( Date timeFrom, Date timeTo, BaseSqlSelectQuery selectQuery );

	void addVotingCriteria( PhotoListCriterias criterias, SqlIdsSelectQuery selectQuery );

	void addFilterByMembershipType( PhotoListCriterias criterias, SqlIdsSelectQuery selectQuery );

	void addSortCriterias( PhotoListCriterias criterias, SqlIdsSelectQuery selectQuery );

	void addLimitCriterias( PhotoListCriterias criterias, SqlIdsSelectQuery selectQuery, PagingModel pagingModel );
}
