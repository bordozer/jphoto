package core.services.photo.list.factory;

import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.utils.sql.PhotoListQueryBuilder;
import ui.elements.PhotoList;

import java.util.Date;

public abstract class PhotoListFactoryTopBest extends AbstractPhotoListFactoryBest {

	public PhotoListFactoryTopBest( final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( photoFilteringStrategy, accessor, services );
	}

	@Override
	public PhotoList getPhotoList( final int photoListId, final int page, final Language language, final Date time ) {
		return page <= 1 ? super.getPhotoList( photoListId, page, language, time ) : null;
	}

	@Override
	public PhotoListQueryBuilder getBaseQuery() {
		return super.getBaseQuery().forPage( 1, getTopListPhotosCount() );
	}

	/*public PhotoListQueryBuilder getBaseQueryForUserCard() {
		return super.getBaseQuery( 1 ).forPage( 1, services.getConfigurationService().getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY ) ); // TODO: PHOTO_LIST_PHOTO_TOP_QTY can be replaced with apropriate parameter for a user card
	}*/
}
