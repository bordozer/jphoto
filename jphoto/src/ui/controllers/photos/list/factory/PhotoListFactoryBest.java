package ui.controllers.photos.list.factory;

import core.general.genre.Genre;
import core.general.user.User;
import core.services.system.Services;
import ui.controllers.photos.list.title.PhotoListTitleBest;

import java.util.Date;

public class PhotoListFactoryBest extends AbstractPhotoListFactory {

	public PhotoListFactoryBest( final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForAbsolutelyBest( accessor );
		photoListTitle = getPhotoListTitle( services );
	}

	public PhotoListFactoryBest( final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForGenreBestForPeriod( genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		this.genre = genre;
	}

	@Override
	protected boolean showPaging() {
		return false;
	}

	@Override
	protected boolean isPhotoHidden( final int photoId, final Date currentTime ) {
		if ( user != null ) {
			return false;
		}

		if ( services.getSecurityService().isSuperAdminUser( accessor ) ) {
			return false;
		}

		return services.getRestrictionService().isPhotoShowingInTopBestRestrictedOn( photoId, currentTime );
	}

	private PhotoListTitleBest getPhotoListTitle( final Services services ) {
		return new PhotoListTitleBest( criterias, services );
	}
}