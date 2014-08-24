package core.services.photo.list.factory;

import core.general.configuration.ConfigurationKey;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.sql.PhotoListQueryBuilder;
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

	protected SqlSelectIdsResult getPhotosId( final SqlIdsSelectQuery selectIdsQuery ) {
		return services.getPhotoService().load( selectIdsQuery );
	}

	protected int getAccessorPhotosOnPage() {
		return services.getUtilsService().getPhotosOnPage( accessor );
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

		int counter = selectedPhotosCount;
		while( notRestrictedPhotosIds.size() < selectedPhotosCount ) {
			final int diff = selectedPhotosCount - notRestrictedPhotosIds.size();

			selectIdsQuery.setOffset( counter );
			selectIdsQuery.setLimit( diff);

			final List<Integer> additionalPhotosIds = getPhotosId( selectIdsQuery ).getIds();

			if ( additionalPhotosIds.size() == 0 ) {
				break;
			}

			notRestrictedPhotosIds.addAll( filterOutHiddenPhotos( additionalPhotosIds, time ) );

			counter += diff;
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

	public AbstractPhotoFilteringStrategy getPhotoFilteringStrategy() {
		return photoFilteringStrategy;
	}
}
