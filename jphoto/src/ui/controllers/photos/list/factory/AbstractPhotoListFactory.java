package ui.controllers.photos.list.factory;

import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import core.general.genre.Genre;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.controllers.photos.list.title.AbstractPhotoListTitle;
import ui.elements.PhotoList;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractPhotoListFactory {

	protected PhotoListCriterias criterias;
	protected AbstractPhotoListTitle photoListTitle;

	protected final User accessor;
	protected final PhotoFilter photoFilter;
	protected AbstractPhotoFilteringStrategy photoFilteringStrategy;

	protected Services services;

	protected User user;
	protected Genre genre;

	protected abstract boolean showPaging();

	public AbstractPhotoListFactory( final User accessor, final Services services ) {
		this.accessor = accessor;
		this.services = services;

		this.photoFilter = new PhotoFilter( accessor, services );
	}

	public PhotoList getPhotoList( final int photoListId, final PagingModel pagingModel, final Language language, final Date time ) {
		final PhotoListMetrics metrics = getPhotosIdsToShow( services.getPhotoCriteriasSqlService().getForCriteriasPagedIdsSQL( criterias, pagingModel ), time );

		final PhotoList photoList = new PhotoList( metrics.getPhotoIds(), photoListTitle.getPhotoListTitle().build( language ), showPaging() );

		photoList.setLinkToFullListText( services.getPhotoListCriteriasService().getLinkToFullListText( criterias ) );
		photoList.setPhotosCriteriasDescription( photoListTitle.getPhotoListDescription().build( language ) );
		photoList.setLinkToFullList( getLinkToFullList() );

		photoList.setPhotoGroupOperationMenuContainer( metrics.hasPhotos() ? getPhotoGroupOperationMenuContainer() : services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer() );

		photoList.setPhotoListId( photoListId );

		pagingModel.setTotalItems( metrics.getPhotosCount() );

		return photoList;
	}

	protected String getPhotoListBottomText() {
		return StringUtils.EMPTY;
	}

	protected PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer() {
		return services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer();
	}

	protected String getLinkToFullList() {
		return null;
	}

	protected PhotoListMetrics getPhotosIdsToShow( final SqlIdsSelectQuery selectIdsQuery, Date time ) {

		final SqlSelectIdsResult selectResult = getPhotosId( selectIdsQuery );

		final List<Integer> selectedPhotosIds = selectResult.getIds();
		final int selectedPhotosCount = selectedPhotosIds.size();
		final int totalPhotosCount = selectResult.getRecordQty();

		final List<Integer> notRestrictedPhotosIds = filterOutHiddenPhotos( selectedPhotosIds, time );

		if ( selectedPhotosCount == totalPhotosCount ) {
			return new PhotoListMetrics( notRestrictedPhotosIds, notRestrictedPhotosIds.size() );
		}

		final int photosCountToShow = criterias.getPhotoQtyLimit();
		while( notRestrictedPhotosIds.size() < photosCountToShow ) {
			final int diff = photosCountToShow - notRestrictedPhotosIds.size();

			selectIdsQuery.setOffset( selectedPhotosCount );
			selectIdsQuery.setLimit( diff);

			final List<Integer> additionalPhotosIds = getPhotosId( selectIdsQuery ).getIds();
			notRestrictedPhotosIds.addAll( filterOutHiddenPhotos( additionalPhotosIds, services.getDateUtilsService().getCurrentTime() ) );
		}

		return new PhotoListMetrics( notRestrictedPhotosIds, totalPhotosCount );
	}

	private List<Integer> filterOutHiddenPhotos( final List<Integer> idsToShow, Date time ) {
		final List<Integer> notRestrictedIds = newArrayList( idsToShow );

		CollectionUtils.filter( notRestrictedIds, new Predicate<Integer>() {
			@Override
			public boolean evaluate( final Integer photoId ) {
				return ! photoFilteringStrategy.isPhotoHidden( photoId, time );
			}
		} );

		return notRestrictedIds;
	}

	protected SqlSelectIdsResult getPhotosId( final SqlIdsSelectQuery selectIdsQuery ) {
		return services.getPhotoService().load( selectIdsQuery );
	}

	protected boolean isAccessorSuperAdmin() {
		return services.getSecurityService().isSuperAdminUser( accessor );
	}

	protected boolean isUserCard() {
		return user != null;
	}
}
