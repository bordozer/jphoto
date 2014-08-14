package ui.controllers.photos.list.factory;

import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.controllers.photos.list.title.PhotoListTitleGallery;

import java.util.Date;
import java.util.List;

public class PhotoListFactoryGallery extends AbstractPhotoListFactory {

	public PhotoListFactoryGallery( final User user, final Services services ) {
		super( user, services );

		criterias = services.getPhotoListCriteriasService().getForAllPhotos( user );
		photoListTitle = new PhotoListTitleGallery( criterias, services );
	}

	@Override
	protected PhotoListMetrics getPhotosIdsToShow( final SqlIdsSelectQuery selectIdsQuery ) {

		final SqlSelectIdsResult selectResult = getPhotosId( selectIdsQuery );

		final List<Integer> idsToShow = selectResult.getIds();
		final int count = selectResult.getRecordQty();

		final Date currentTime = services.getDateUtilsService().getCurrentTime();
		CollectionUtils.filter( idsToShow, new Predicate<Integer>() {
			@Override
			public boolean evaluate( final Integer photoId ) {
				return ! services.getRestrictionService().isPhotoShowingInPhotoGalleryRestrictedOn( photoId, currentTime );
			}
		} );

		return new PhotoListMetrics( idsToShow, count );
	}

	@Override
	protected PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer() {
		return services.getGroupOperationService().getPhotoListPhotoGroupOperationMenuContainer( accessor );
	}

	@Override
	protected String getLinkToFullList() {
		return null;
	}

	@Override
	protected boolean showPaging() {
		return true;
	}
}
