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

import java.util.List;

public abstract class AbstractPhotoListFactory {

	protected PhotoListCriterias criterias;
	protected AbstractPhotoListTitle photoListTitle;

	protected Services services;
	protected final User user;

	protected abstract PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer();

	protected abstract String getLinkToFullList();

	protected abstract boolean showPaging();

	public AbstractPhotoListFactory( final User user, final Services services ) {
		this.services = services;
		this.user = user;
	}

	public PhotoList getPhotoList( final int photoListId, final PagingModel pagingModel, final Language language ) {

		final SqlIdsSelectQuery selectQuery = services.getPhotoCriteriasSqlService().getForCriteriasPagedIdsSQL( criterias, pagingModel );

		final SqlSelectIdsResult selectResult = services.getPhotoService().load( selectQuery );

		final List<Integer> photoIds = selectResult.getIds();

		final PhotoList photoList = new PhotoList( photoIds, photoListTitle.getPhotoListTitle().build( language ), showPaging() );

		photoList.setLinkToFullListText( services.getPhotoListCriteriasService().getLinkToFullListText( criterias ) );
		photoList.setPhotosCriteriasDescription( photoListTitle.getPhotoListDescription().build( EnvironmentContext.getLanguage() ) );
		photoList.setLinkToFullList( getLinkToFullList() );

		photoList.setPhotoGroupOperationMenuContainer( photoIds.size() > 0 ? getPhotoGroupOperationMenuContainer() : services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer() );

		photoList.setPhotoListId( photoListId );

		pagingModel.setTotalItems( selectResult.getRecordQty() );

		return photoList;
	}
}
