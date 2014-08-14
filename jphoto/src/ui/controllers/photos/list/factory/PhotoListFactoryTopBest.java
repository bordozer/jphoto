package ui.controllers.photos.list.factory;

import core.general.base.PagingModel;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import sql.SqlSelectIdsResult;
import ui.controllers.photos.list.title.PhotoListTitleTopBest;
import ui.elements.PhotoList;

import java.util.Date;
import java.util.List;

public class PhotoListFactoryTopBest extends AbstractPhotoListFactory {

	public PhotoListFactoryTopBest( final Services services, final User user ) {
		super( user, services );

		criterias = services.getPhotoListCriteriasService().getForAllPhotosTopBest( user );
		photoListTitle = new PhotoListTitleTopBest( criterias, services );
	}

	@Override
	protected PhotoListMetrics filterOutRestrictedPhotos( final SqlSelectIdsResult selectIdsResult ) {

		final List<Integer> ids = selectIdsResult.getIds();
		final int count = selectIdsResult.getRecordQty();

		final Date currentTime = services.getDateUtilsService().getCurrentTime();
		CollectionUtils.filter( ids, new Predicate<Integer>() {
			@Override
			public boolean evaluate( final Integer photoId ) {
				return ! services.getRestrictionService().isPhotoBeingInTopRestrictedOn( photoId, currentTime );
			}
		} );

		return new PhotoListMetrics( ids, count );
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
