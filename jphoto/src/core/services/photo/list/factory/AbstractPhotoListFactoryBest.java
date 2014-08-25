package core.services.photo.list.factory;

import core.general.configuration.ConfigurationKey;
import core.general.data.TimeRange;
import core.general.user.User;
import core.services.system.Services;
import core.services.utils.sql.PhotoListQueryBuilder;

public abstract class AbstractPhotoListFactoryBest extends AbstractPhotoListFactory {

	protected final int days;
	protected final TimeRange timeRange;
	protected final int minMarks;

	public AbstractPhotoListFactoryBest( final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( photoFilteringStrategy, accessor, services );

		days = getDays();
		timeRange = getTimeRange();
		minMarks = getMinMarks( services );
	}

	protected PhotoListQueryBuilder getBaseQuery() {
		return getBaseQuery( minMarks );
	}

	protected PhotoListQueryBuilder getBaseQuery( final int minMarks ) {
		return new PhotoListQueryBuilder( services.getDateUtilsService() ).filterByVotingTime( timeRange.getTimeFrom(), timeRange.getTimeTo() ).filterByMinimalMarks( minMarks ).sortBySumMarksDesc().sortByUploadTimeDesc();
	}

	private TimeRange getTimeRange() {
		return services.getPhotoVotingService().getTopBestDateRange();
	}

	private int getMinMarks( final Services services ) {
		return services.getConfigurationService().getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO );
	}

	private int getDays() {
		return services.getConfigurationService().getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
	}
}
