package ui.controllers.photos.list.factory;

import core.general.data.PhotoListCriterias;
import core.general.user.User;
import core.services.system.Services;

public abstract class PhotoListFactoryBest extends AbstractPhotoListFactory {

	public PhotoListFactoryBest( final PhotoListCriterias criterias, final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( criterias, photoFilteringStrategy, accessor, services );
	}

	@Override
	protected boolean showPaging() {
		return true;
	}

	/*public PhotoListFactoryBest( final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForAbsolutelyBest( accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().bestFilteringStrategy( accessor );
	}

	public PhotoListFactoryBest( final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		this.genre = genre;

		criterias = services.getPhotoListCriteriasService().getForGenreBestForPeriod( genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().bestFilteringStrategy( accessor );
	}

	public PhotoListFactoryBest( final User user, final User accessor, final Services services ) {
		super( accessor, services );

		this.user = user;

		criterias = services.getPhotoListCriteriasService().getForUserAbsolutelyBest( user, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().userCardFilteringStrategy( user, accessor );
	}

	public PhotoListFactoryBest( final User user, final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		this.user = user;
		this.genre = genre;

		criterias = services.getPhotoListCriteriasService().getForUserAndGenreAbsolutelyBest( user, genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().userCardFilteringStrategy( user, accessor );
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
	}*/
}
