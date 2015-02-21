package core.services.photo.list.factory;

import core.general.configuration.ConfigurationKey;
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
import ui.viewModes.PhotoListViewMode;
import utils.UserUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public abstract class AbstractPhotoListFactory {

	protected final User accessor;
	protected AbstractPhotoFilteringStrategy photoFilteringStrategy;

	protected final Services services;

	public abstract SqlIdsSelectQuery getSelectIdsQuery();

	public abstract TranslatableMessage getTitle();

	public AbstractPhotoListFactory( final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {

		this.photoFilteringStrategy = photoFilteringStrategy;

		this.accessor = accessor;
		this.services = services;
	}

	public PhotoList getPhotoList( final int photoListId, final int page, final Language language, final Date time ) {
		final PhotoListMetrics metrics = getPhotosIdsToShow( getSelectIdsQuery(), time );

		final PhotoList photoList = new PhotoList( metrics.getPhotoIds(), getTitle().build( language ), showPaging() );
		photoList.setPhotoListId( photoListId );

		photoList.setLinkToFullListText( "PhotoList: All photos" );
		photoList.setLinkToFullList( getLinkToFullList() );

		photoList.setPhotoGroupOperationMenuContainer( metrics.hasPhotos() ? getGroupOperationMenuContainer() : services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer() );

		photoList.setPhotosCriteriasDescription( getCriteriaDescription().build( language ) );
		photoList.setBottomText( getPhotoListBottomText().build( language ) );

		photoList.setPhotosCount( metrics.getPhotosCount() );

		photoList.setAccessiblePhotoListViewModes( getAccessiblePhotoListViewModes() );

		photoList.setHiddenPhotoIds( metrics.getHiddenPhotoIds() );

		return photoList;
	}

	public String getLinkToFullList() {
		return StringUtils.EMPTY;
	}

	public TranslatableMessage getCriteriaDescription() {
		return new TranslatableMessage( "", services );
	}

	public TranslatableMessage getPhotoListBottomText() {
		return new TranslatableMessage( "", services );
	}

	public boolean showPaging() {
		return false;
	}

	public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
		return services.getGroupOperationService().getNoPhotoGroupOperationMenuContainer();
	}

	public List<PhotoListViewMode> getAccessiblePhotoListViewModes() {
		return newArrayList();
	}

	protected int getTopListPhotosCount() {
		return services.getConfigurationService().getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY );
	}

	protected SqlSelectIdsResult getPhotosId( final SqlIdsSelectQuery selectIdsQuery ) {
		return services.getPhotoService().load( selectIdsQuery );
	}

	protected int getAccessorPhotosOnPage() {
		return services.getUtilsService().getPhotosOnPage( accessor );
	}

	protected PhotoListMetrics getPhotosIdsToShow( final SqlIdsSelectQuery selectIdsQuery, Date time ) {
		final Set<Integer> hiddenPhotoIds = newHashSet();

		final SqlSelectIdsResult selectResult = getPhotosId( selectIdsQuery );

		final List<Integer> selectedPhotosIds = selectResult.getIds();
		final int selectedPhotosCount = selectedPhotosIds.size();
		final int totalPhotosCount = selectResult.getRecordQty();

		final PhotoHolder holder = filterOutHiddenPhotos( selectedPhotosIds, time );
		final List<Integer> notRestrictedPhotosIds = holder.visiblePhotoIds;
		hiddenPhotoIds.addAll( holder.hiddenPhotoIds );

		if ( selectedPhotosCount == totalPhotosCount ) {
			return new PhotoListMetrics( notRestrictedPhotosIds, notRestrictedPhotosIds.size(), hiddenPhotoIds );
		}

		int counter = selectedPhotosCount;
		while( notRestrictedPhotosIds.size() < selectedPhotosCount ) {
			final int diff = selectedPhotosCount - notRestrictedPhotosIds.size();

			selectIdsQuery.setOffset( counter );
			selectIdsQuery.setLimit( diff);

			final List<Integer> additionalPhotosIds = getPhotosId( selectIdsQuery ).getIds();

			if ( additionalPhotosIds.size() == 0 ) {
				break;
			}

			final PhotoHolder photoHolder = filterOutHiddenPhotos( additionalPhotosIds, time );
			notRestrictedPhotosIds.addAll( photoHolder.visiblePhotoIds );

			counter += diff;

			hiddenPhotoIds.addAll( photoHolder.hiddenPhotoIds );
		}

		return new PhotoListMetrics( notRestrictedPhotosIds, totalPhotosCount, hiddenPhotoIds );
	}

	public PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainerForUserCard( final User user ) {

		if ( UserUtils.isUsersEqual( user, accessor ) ) {
			return new PhotoGroupOperationMenuContainer( services.getGroupOperationService().getUserOwnPhotosGroupOperationMenus() );
		}

		return services.getGroupOperationService().getPhotoListPhotoGroupOperationMenuContainer( accessor );
	}

	private PhotoHolder filterOutHiddenPhotos( final List<Integer> idsToShow, final Date time ) {
		final List<Integer> visiblePhotoIds = newArrayList( idsToShow );
		final Set<Integer> hiddenPhotoIds = newHashSet();

		CollectionUtils.filter( visiblePhotoIds, new Predicate<Integer>() {
			@Override
			public boolean evaluate( final Integer photoId ) {

				final boolean isVisible = !photoFilteringStrategy.isPhotoHidden( photoId, time );

				if ( ! isVisible ) {
					hiddenPhotoIds.add( photoId );
				}

				return isVisible;
			}
		} );

		return new PhotoHolder( visiblePhotoIds, hiddenPhotoIds );
	}

	public AbstractPhotoFilteringStrategy getPhotoFilteringStrategy() {
		return photoFilteringStrategy;
	}

	private class PhotoHolder {

		private List<Integer> visiblePhotoIds;
		private Set<Integer> hiddenPhotoIds;

		private PhotoHolder( final List<Integer> visiblePhotoIds, final Set<Integer> hiddenPhotoIds ) {
			this.visiblePhotoIds = visiblePhotoIds;
			this.hiddenPhotoIds = hiddenPhotoIds;
		}
	}
}
