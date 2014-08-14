package ui.controllers.photos.list.factory;

import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.context.EnvironmentContext;
import ui.controllers.photos.list.title.AbstractPhotoListTitle;
import ui.elements.PhotoList;

public abstract class AbstractPhotoListFactory {

	protected PhotoListCriterias criterias;
	protected AbstractPhotoListTitle photoListTitle;

	protected Services services;
	protected final User user;

	protected abstract PhotoListMetrics filterOutRestrictedPhotos( final SqlSelectIdsResult selectIdsResult );

	protected abstract String getLinkToFullList();

	protected abstract boolean showPaging();

	protected abstract PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer();

	public AbstractPhotoListFactory( final User user, final Services services ) {
		this.services = services;
		this.user = user;
	}

	public PhotoList getPhotoList( final int photoListId, final PagingModel pagingModel, final Language language ) {

		final SqlSelectIdsResult selectResult = services.getPhotoService().load( services.getPhotoCriteriasSqlService().getForCriteriasPagedIdsSQL( criterias, pagingModel ) );

		final PhotoListMetrics metrics = filterOutRestrictedPhotos( selectResult );

		final PhotoList photoList = new PhotoList( metrics.getPhotoIds(), photoListTitle.getPhotoListTitle().build( language ), showPaging() );

		photoList.setLinkToFullListText( services.getPhotoListCriteriasService().getLinkToFullListText( criterias ) );
		photoList.setPhotosCriteriasDescription( photoListTitle.getPhotoListDescription().build( EnvironmentContext.getLanguage() ) );
		photoList.setLinkToFullList( getLinkToFullList() );

		photoList.setPhotoGroupOperationMenuContainer( metrics.getPhotoIds().size() > 0 ? getPhotoGroupOperationMenuContainer() : services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer() );

		photoList.setPhotoListId( photoListId );

		pagingModel.setTotalItems( metrics.getPhotosCount() );

		return photoList;
	}
}
