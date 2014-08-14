package ui.controllers.photos.list.factory;

import core.general.base.PagingModel;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.dao.BaseEntityDao;
import core.services.dao.PhotoDaoImpl;
import core.services.system.Services;
import core.services.translator.Language;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import sql.SqlSelectIdsResult;
import sql.builder.*;
import ui.controllers.photos.list.title.PhotoListTitleTopBest;
import ui.elements.PhotoList;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoListFactoryTopBest extends AbstractPhotoListFactory {

	public PhotoListFactoryTopBest( final Services services, final User user ) {
		super( user, services );

		criterias = services.getPhotoListCriteriasService().getForAllPhotosTopBest( user );
		photoListTitle = new PhotoListTitleTopBest( criterias, services );
	}

	@Override
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
}
