package core.services.photo.list.factory;

import core.general.configuration.ConfigurationKey;
import core.general.data.TimeRange;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.utils.sql.PhotoListQueryBuilder;
import ui.elements.PhotoList;

import java.util.Date;

public abstract class PhotoListFactoryTopBest extends AbstractPhotoListFactory {

	protected final int days;
	protected final TimeRange timeRange;
	protected final int photosCount;
	protected final int minMarks;

	public PhotoListFactoryTopBest( final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( photoFilteringStrategy, accessor, services );

		days = getDays();
		timeRange = getTimeRange();

		photosCount = services.getConfigurationService().getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY );
		minMarks = getMinMarks( services );
	}

	private TimeRange getTimeRange() {
		return services.getPhotoVotingService().getTopBestDateRange();
	}

	@Override
	public PhotoList getPhotoList( final int photoListId, final int page, final Language language, final Date time ) {
		return page <= 1 ? super.getPhotoList( photoListId, page, language, time ) : null;
	}

	public PhotoListQueryBuilder getTopBestBaseQuery() {
		return new PhotoListQueryBuilder( services.getDateUtilsService() ).votingBetween( timeRange.getTimeFrom(), timeRange.getTimeTo() ).filterByMinimalMarks( minMarks ).forPage( 1, photosCount ).sortBySumMarks();
	}
}
