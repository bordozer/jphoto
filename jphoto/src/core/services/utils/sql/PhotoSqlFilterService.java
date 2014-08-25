package core.services.utils.sql;

import sql.builder.BaseSqlSelectQuery;

import java.util.Date;

public interface PhotoSqlFilterService {

	void addFilterByUser( int userId, BaseSqlSelectQuery selectQuery );

	void addJoinWithPhotoVotingTable( BaseSqlSelectQuery selectQuery );

	void addFilterByMinVotedMark( BaseSqlSelectQuery selectQuery, int minMarks );

	void addFilterForVotingTimeForLastNDays( BaseSqlSelectQuery selectQuery, Date timeFrom, Date timeTo );
}
