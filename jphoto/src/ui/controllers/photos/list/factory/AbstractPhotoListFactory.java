package ui.controllers.photos.list.factory;

import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.context.EnvironmentContext;
import ui.controllers.photos.list.title.AbstractPhotoListTitle;
import ui.elements.PhotoList;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractPhotoListFactory {

	protected PhotoListCriterias criterias;
	protected AbstractPhotoListTitle photoListTitle;

	protected Services services;
	protected final User accessor;

	protected abstract String getLinkToFullList();

	protected abstract boolean showPaging();

	protected abstract PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer();

	public AbstractPhotoListFactory( final User accessor, final Services services ) {
		this.services = services;
		this.accessor = accessor;
	}

	public PhotoList getPhotoList( final int photoListId, final PagingModel pagingModel, final Language language ) {

		final PhotoListMetrics metrics = getPhotosIdsToShow( services.getPhotoCriteriasSqlService().getForCriteriasPagedIdsSQL( criterias, pagingModel ) );

		final PhotoList photoList = new PhotoList( metrics.getPhotoIds(), photoListTitle.getPhotoListTitle().build( language ), showPaging() );

		photoList.setLinkToFullListText( services.getPhotoListCriteriasService().getLinkToFullListText( criterias ) );
		photoList.setPhotosCriteriasDescription( photoListTitle.getPhotoListDescription().build( EnvironmentContext.getLanguage() ) );
		photoList.setLinkToFullList( getLinkToFullList() );

		photoList.setPhotoGroupOperationMenuContainer( metrics.hasPhotos() ? getPhotoGroupOperationMenuContainer() : services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer() );

		photoList.setPhotoListId( photoListId );

		pagingModel.setTotalItems( metrics.getPhotosCount() );

		return photoList;
	}

	protected PhotoListMetrics getPhotosIdsToShow( final SqlIdsSelectQuery selectIdsQuery ) {

		final SqlSelectIdsResult selectResult = getPhotosId( selectIdsQuery );

		final List<Integer> selectedPhotosIds = selectResult.getIds();
		final int selectedPhotosCount = selectedPhotosIds.size();
		final int totalPhotosCount = selectResult.getRecordQty();

		if ( services.getSecurityService().isSuperAdminUser( accessor ) ) {
			return new PhotoListMetrics( selectedPhotosIds, totalPhotosCount );
		}

		final List<Integer> notRestrictedPhotosIds = getNotRestrictedPhotosIdsOnly( selectedPhotosIds );

		if ( selectedPhotosCount == totalPhotosCount ) {
			return new PhotoListMetrics( notRestrictedPhotosIds, notRestrictedPhotosIds.size() );
		}

		final int photosCountToShow = criterias.getPhotoQtyLimit();
		while( notRestrictedPhotosIds.size() < photosCountToShow ) {
			final int diff = photosCountToShow - notRestrictedPhotosIds.size();

			selectIdsQuery.setOffset( selectedPhotosCount );
			selectIdsQuery.setLimit( diff);

			final List<Integer> additionalPhotosIds = getPhotosId( selectIdsQuery ).getIds();
			notRestrictedPhotosIds.addAll( getNotRestrictedPhotosIdsOnly( additionalPhotosIds ) );
		}

		return new PhotoListMetrics( notRestrictedPhotosIds, totalPhotosCount );
	}

	private List<Integer> getNotRestrictedPhotosIdsOnly( final List<Integer> idsToShow ) {
		final Date currentTime = services.getDateUtilsService().getCurrentTime();
		final List<Integer> notRestrictedIds = newArrayList( idsToShow );

		CollectionUtils.filter( notRestrictedIds, new Predicate<Integer>() {
			@Override
			public boolean evaluate( final Integer photoId ) {
				return !services.getRestrictionService().isPhotoBeingInTopRestrictedOn( photoId, currentTime );
			}
		} );

		return notRestrictedIds;
	}

	protected SqlSelectIdsResult getPhotosId( final SqlIdsSelectQuery selectIdsQuery ) {
		return services.getPhotoService().load( selectIdsQuery );
	}
}
