package core.services.dao;

import core.general.activity.AbstractActivityStreamEntry;

import java.util.Date;
import java.util.List;

public interface ActivityStreamDao extends BaseEntityDao<AbstractActivityStreamEntry> {

	List<AbstractActivityStreamEntry> getActivityForPeriod( final Date dateFrom, final Date dateTo );

	List<AbstractActivityStreamEntry> getLastActivities( final int qty );

	List<AbstractActivityStreamEntry> getUserActivities( final int userId );

	List<AbstractActivityStreamEntry> getUserLastActivities( final int userId, final int qty );

	List<AbstractActivityStreamEntry> getActivityForPhoto( final int photoId );

	void deleteEntriesOlderThen( final Date timeFrame );
}
