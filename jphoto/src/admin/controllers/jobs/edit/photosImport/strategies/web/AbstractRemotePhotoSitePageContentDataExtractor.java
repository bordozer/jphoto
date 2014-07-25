package admin.controllers.jobs.edit.photosImport.strategies.web;

import java.util.List;

public abstract class AbstractRemotePhotoSitePageContentDataExtractor {

	public static final String NO_PHOTO_NAME = "-no name-";

	public abstract String extractImageUrl( int photosightPhotoId, String photoPageContent );

	public abstract int extractPhotosightPhotoId( String group );

	public abstract String extractImageUrlByNewRules( int photosightPhotoId, String photoPageContent );

	public abstract String extractImageUrlByOldRules( int photosightPhotoId, String photoPageContent );

	public abstract String getPhotoIdRegex();

	public abstract String extractPhotosightUserName( String userPageContent );

	public abstract int extractPhotosightUserPhotosCount( String photosightUserId );

	public abstract int getTotalPagesQty( String userCardContent, String photosightUserId );

	public abstract int extractPhotoCategoryId( String photoPageContent );

	public abstract String extractPhotoName( String photoPageContent );

	public abstract List<String> extractComments( String photoPageContent );
}
