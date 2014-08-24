package core.services.photo.list.factory;

import core.general.configuration.ConfigurationKey;
import core.general.data.TimeRange;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.utils.sql.PhotoListQueryBuilder;
import ui.elements.PhotoList;

import java.util.Date;

public abstract class PhotoListFactoryTopBest extends AbstractPhotoListFactoryBest {

	protected final int photosCount;

	public PhotoListFactoryTopBest( final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( photoFilteringStrategy, accessor, services );

		photosCount = services.getConfigurationService().getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY );
	}

	@Override
	public PhotoList getPhotoList( final int photoListId, final int page, final Language language, final Date time ) {
		return page <= 1 ? super.getPhotoList( photoListId, page, language, time ) : null;
	}

	@Override
	public PhotoListQueryBuilder getTopBestBaseQuery() {
		return super.getTopBestBaseQuery().forPage( 1, photosCount );
	}
}
