package core.services.photo.list.factory;

import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import ui.elements.PhotoList;

import java.util.Date;

public abstract class PhotoListFactoryTopBest extends AbstractPhotoListFactory {

	public PhotoListFactoryTopBest( final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( photoFilteringStrategy, accessor, services );
	}

	@Override
	public PhotoList getPhotoList( final int photoListId, final int page, final Language language, final Date time ) {
		return page <= 1 ? super.getPhotoList( photoListId, page, language, time ) : null;
	}
}
