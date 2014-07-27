package admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;

import java.util.List;

public class Photo35ContentDataExtractor extends AbstractRemotePhotoSitePageContentDataExtractor {

	@Override
	public String extractImageUrl( final int photosightPhotoId, final String photoPageContent ) {
		return null;
	}

	@Override
	public int extractRemotePhotoSitePhotoId( final String group ) {
		return 0;
	}

	@Override
	public String extractImageUrlByNewRules( final int photosightPhotoId, final String photoPageContent ) {
		return null;
	}

	@Override
	public String extractImageUrlByOldRules( final int photosightPhotoId, final String photoPageContent ) {
		return null;
	}

	@Override
	public String getPhotoIdRegex() {
		return null;
	}

	@Override
	public String extractPhotosightUserName( final String userPageContent ) {
		return null;
	}

	@Override
	public int extractPhotosightUserPhotosCount( final String photosightUserId ) {
		return 0;
	}

	@Override
	public int getTotalPagesQty( final String userCardContent, final String photosightUserId ) {
		return 0;
	}

	@Override
	public int extractPhotoCategoryId( final String photoPageContent ) {
		return 0;
	}

	@Override
	public String extractPhotoName( final String photoPageContent ) {
		return null;
	}

	@Override
	public List<String> extractComments( final String photoPageContent ) {
		return null;
	}
}
