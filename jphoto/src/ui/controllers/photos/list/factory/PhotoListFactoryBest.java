package ui.controllers.photos.list.factory;

import core.general.genre.Genre;
import core.general.user.User;
import core.services.system.Services;
import ui.controllers.photos.list.title.PhotoListTitleBest;

public class PhotoListFactoryBest extends AbstractPhotoListFactory {

	public PhotoListFactoryBest( final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForAbsolutelyBest( accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = photoFilter.bestFilteringStrategy();
	}

	public PhotoListFactoryBest( final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		this.genre = genre;

		criterias = services.getPhotoListCriteriasService().getForGenreBestForPeriod( genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = photoFilter.bestFilteringStrategy();
	}

	public PhotoListFactoryBest( final User user, final User accessor, final Services services ) {
		super( accessor, services );

		this.user = user;

		criterias = services.getPhotoListCriteriasService().getForUserAbsolutelyBest( user, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = photoFilter.userCardFilteringStrategy( user );
	}

	public PhotoListFactoryBest( final User user, final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		this.user = user;
		this.genre = genre;

		criterias = services.getPhotoListCriteriasService().getForUserAndGenreAbsolutelyBest( user, genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = photoFilter.userCardFilteringStrategy( user );
	}

	@Override
	protected boolean showPaging() {
		return false;
	}

	@Override
	protected String getPhotoListBottomText() {
		if ( genre != null ) {
			return genre.getDescription();
		}

		return super.getPhotoListBottomText();
	}

	private PhotoListTitleBest getPhotoListTitle( final Services services ) {
		return new PhotoListTitleBest( criterias, services );
	}
}
