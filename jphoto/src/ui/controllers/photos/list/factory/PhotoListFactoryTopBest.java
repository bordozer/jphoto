package ui.controllers.photos.list.factory;

import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import ui.controllers.photos.list.title.PhotoListTitleTopBest;
import ui.elements.PhotoList;

import java.util.Date;

public class PhotoListFactoryTopBest extends AbstractPhotoListFactory {

	public PhotoListFactoryTopBest( final User user, final Services services ) {
		super( user, services );

		criterias = services.getPhotoListCriteriasService().getForAllPhotosTopBest( user );
		photoListTitle = getPhotoListTitle( services );
	}

	public PhotoListFactoryTopBest( final Genre genre, final User user, final Services services ) {
		super( user, services );

		criterias = services.getPhotoListCriteriasService().getForGenreTopBest( genre, user );
		photoListTitle = getPhotoListTitle( services );
	}

	@Override
	protected boolean isPhotoHidden( final int photoId, final Date currentTime ) {
		return services.getRestrictionService().isPhotoBeingInTopRestrictedOn( photoId, currentTime );
	}

	@Override
	public PhotoList getPhotoList( final int photoListId, final PagingModel pagingModel, final Language language ) {
		return pagingModel.getCurrentPage() == 1 ? super.getPhotoList( photoListId, pagingModel, language ) : null;
	}

	@Override
	protected String getLinkToFullList() {
		return services.getUrlUtilsService().getPhotosBestInPeriodUrl( criterias.getVotingTimeFrom(), criterias.getVotingTimeTo() );
	}

	@Override
	protected boolean showPaging() {
		return false;
	}

	@Override
	protected PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer() {
		return services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer();
	}

	private PhotoListTitleTopBest getPhotoListTitle( final Services services ) {
		return new PhotoListTitleTopBest( criterias, services );
	}
}
