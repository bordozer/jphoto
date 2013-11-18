package core.services.conversion;

import core.general.photo.PhotoPreview;
import core.interfaces.BaseEntityService;

import java.util.List;

public interface PhotoPreviewService extends BaseEntityService<PhotoPreview> {

	String BEAN_NAME = "photoPreviewService";

	boolean hasUserAlreadySeenThisPhoto( final int photoId, final int userId );

	boolean save( final PhotoPreview entry );

	PhotoPreview load( final int photoId, final int userId );

	void deletePreviews( final int photoId );

	int getPreviewCount( final int photoId );

	List<PhotoPreview> getPreviews( final int photoId );

	List<Integer> getLastUsersWhoViewedUserPhotos( final int userId, final int qty );
}
