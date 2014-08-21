package ui.controllers.photos.list.factory;

import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import ui.controllers.photos.list.title.PhotoListTitleTopBest;
import ui.elements.PhotoList;

import java.util.Date;

public class PhotoListFactoryTopBest extends AbstractPhotoListFactory {

	public PhotoListFactoryTopBest( final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForAllPhotosTopBest( accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().topBestFilteringStrategy();
	}

	public PhotoListFactoryTopBest( final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		this.genre = genre;

		criterias = services.getPhotoListCriteriasService().getForGenreTopBest( genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().topBestFilteringStrategy();
	}

	public PhotoListFactoryTopBest( final User user, final User accessor, final Services services ) {
		super( accessor, services );

		this.user = user;

		criterias = services.getPhotoListCriteriasService().getForUserTopBest( user, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().userCardFilteringStrategy( user, accessor );
	}

	public PhotoListFactoryTopBest( final User user, final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		this.user = user;
		this.genre = genre;

		criterias = services.getPhotoListCriteriasService().getForUserAndGenreTopBest( user, genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().userCardFilteringStrategy( user, accessor );
	}

	@Override
	public PhotoList getPhotoList( final int photoListId, final PagingModel pagingModel, final Language language, final Date time ) {
		return pagingModel.getCurrentPage() <= 1 ? super.getPhotoList( photoListId, pagingModel, language, time ) : null;
	}

	@Override
	protected String getLinkToFullList() {

		if ( genre != null && isUserCard() ) {
			return services.getUrlUtilsService().getPhotosByUserByGenreLinkBest( user.getId(), genre.getId() );
		}

		if ( genre != null ) {
			return services.getUrlUtilsService().getPhotosByGenreLinkBest( genre.getId() );
		}

		if ( isUserCard() ) {
			return services.getUrlUtilsService().getPhotosByUserLinkBest( user.getId() );
		}

		return services.getUrlUtilsService().getPhotosBestInPeriodUrl( criterias.getVotingTimeFrom(), criterias.getVotingTimeTo() );
	}

	@Override
	protected boolean showPaging() {
		return false;
	}

	private PhotoListTitleTopBest getPhotoListTitle( final Services services ) {
		return new PhotoListTitleTopBest( criterias, services );
	}
}
