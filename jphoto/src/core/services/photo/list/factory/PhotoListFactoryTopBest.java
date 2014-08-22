package core.services.photo.list.factory;

import core.general.base.PagingModel;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import sql.builder.SqlIdsSelectQuery;
import ui.elements.PhotoList;

import java.util.Date;

public abstract class PhotoListFactoryTopBest extends AbstractPhotoListFactory {

	public PhotoListFactoryTopBest( final SqlIdsSelectQuery selectIdsQuery, final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( selectIdsQuery, photoFilteringStrategy, accessor, services );
	}

	@Override
	public PhotoList getPhotoList( final int photoListId, final PagingModel pagingModel, final Language language, final Date time ) {
		return pagingModel.getCurrentPage() <= 1 ? super.getPhotoList( photoListId, pagingModel, language, time ) : null;
	}
}
