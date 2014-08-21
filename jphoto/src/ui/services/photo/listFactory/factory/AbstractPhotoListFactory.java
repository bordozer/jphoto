package ui.services.photo.listFactory.factory;

import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.elements.PhotoList;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractPhotoListFactory {

	protected PhotoListCriterias criterias;

	protected final User accessor;
	protected AbstractPhotoFilteringStrategy photoFilteringStrategy;

	protected final Services services;

	protected abstract TranslatableMessage getTitle();

	protected abstract TranslatableMessage getPhotoListBottomText();

	protected AbstractPhotoListFactory( final PhotoListCriterias criterias, final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		this.accessor = accessor;
		this.services = services;

		this.criterias = criterias;
		this.photoFilteringStrategy = photoFilteringStrategy;
	}

	public PhotoList getPhotoList( final int photoListId, final PagingModel pagingModel, final Language language, final Date time ) {
		final PhotoListMetrics metrics = getPhotosIdsToShow( services.getPhotoCriteriasSqlService().getForCriteriasPagedIdsSQL( criterias, pagingModel ), time );

		final PhotoList photoList = new PhotoList( metrics.getPhotoIds(), getTitle().build( language ), showPaging() );

		photoList.setLinkToFullListText( services.getPhotoListCriteriasService().getLinkToFullListText() );
		photoList.setPhotosCriteriasDescription( getDescription().build( language ) );
		photoList.setLinkToFullList( getLinkToFullList() );

		photoList.setPhotoGroupOperationMenuContainer( metrics.hasPhotos() ? getGroupOperationMenuContainer() : services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer() );

		photoList.setPhotoListId( photoListId );
		photoList.setBottomText( getPhotoListBottomText().build( language ) );

		pagingModel.setTotalItems( metrics.getPhotosCount() );

		return photoList;
	}

	protected String getLinkToFullList() {
		return StringUtils.EMPTY;
	}

	protected TranslatableMessage getDescription() {
		return new TranslatableMessage( "", services );
	}

	protected boolean showPaging() {
		return false;
	}

	protected PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
		return services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer();
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

		final int photosCountToShow = criterias.getPhotoQtyLimit(); // TODO: Min( criterias.getPhotoQtyLimit(), selectedPhotosCount )
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
}
