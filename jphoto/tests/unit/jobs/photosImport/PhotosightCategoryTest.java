package jobs.photosImport;

import admin.controllers.jobs.edit.photosImport.LocalCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
import common.AbstractTestCase;
import core.services.remotePhotoSite.RemotePhotoCategoryServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotosightCategoryTest extends AbstractTestCase {

	public static final String WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING = "Wrong photosight photo category mapping";

	private RemotePhotoCategoryServiceImpl remotePhotoCategoryService = new RemotePhotoCategoryServiceImpl();

	@Test
	public void photosightCategoryMappingTest() {
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.NUDE ), LocalCategory.NUDE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.GLAMOUR ), LocalCategory.GLAMOUR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.CITY ), LocalCategory.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.CHILDREN ), LocalCategory.CHILDREN );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.GENRE ), LocalCategory.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.GENRE_PORTRAIT ), LocalCategory.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.ANIMALS ), LocalCategory.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.DIGITAL_ART ), LocalCategory.DIGITAL_ART );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.MACRO ), LocalCategory.MACRO );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.STILL ), LocalCategory.STILL );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.LANDSCAPE ), LocalCategory.LANDSCAPE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.UNDERWATER ), LocalCategory.UNDERWATER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.PORTRAIT ), LocalCategory.PORTRAIT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.TRAVELLING ), LocalCategory.TRAVELLING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.ADVERTISING ), LocalCategory.ADVERTISING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.REPORTING ), LocalCategory.REPORTING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.WEDDING ), LocalCategory.WEDDING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.SPORT ), LocalCategory.SPORT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.PHOTOSIGHT ), LocalCategory.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.PHOTO_HUNTING ), LocalCategory.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.ARCHITECTURE ), LocalCategory.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.MODELS ), LocalCategory.MODELS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.HUMOR ), LocalCategory.HUMOR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.MOBILE_PHOTO ), LocalCategory.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.MUSEUM ), LocalCategory.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.NATURE ), LocalCategory.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.TECH ), LocalCategory.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.REST ), LocalCategory.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, getDiscEntry( PhotosightCategory.PAPARAZZI ), LocalCategory.OTHER );
	}

	private LocalCategory getDiscEntry( final RemotePhotoSiteCategory photosightCategory ) {
		return remotePhotoCategoryService.getMappedGenreDiscEntry( photosightCategory );
	}
}
