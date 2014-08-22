package core.services.utils.sql;

import core.general.data.PhotoListCriterias;
import sql.builder.BaseSqlSelectQuery;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;

public interface PhotoCriteriasSqlService {

	SqlIdsSelectQuery getForCriteriasPagedIdsSQL( final PhotoListCriterias criterias, final int page, final int itemsOnPage );

	void addUploadTimeCriteria( final Date timeFrom, final Date timeTo, final BaseSqlSelectQuery selectQuery );
}
