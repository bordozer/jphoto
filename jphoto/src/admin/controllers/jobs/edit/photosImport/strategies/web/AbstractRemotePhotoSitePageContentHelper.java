package admin.controllers.jobs.edit.photosImport.strategies.web;

import java.util.List;

public abstract class AbstractRemotePhotoSitePageContentHelper {

	public static final String NO_PHOTO_NAME = "-no name-";

	public abstract int getTotalPagesQty( String userCardContent, String photosightUserId );

	public abstract int extractPhotoCategoryId( String photoPageContent );

	public abstract String extractPhotoName( String photoPageContent );

	public abstract List<String> extractComments( String photoPageContent );
}
