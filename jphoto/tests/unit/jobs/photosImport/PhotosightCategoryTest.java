package jobs.photosImport;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSitePhotoImageFileUtils;
import common.AbstractTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotosightCategoryTest extends AbstractTestCase {

	public static final String WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING = "Wrong photosight photo category mapping";

	@Test
	public void photosightCategoryMappingTest() {
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.NUDE ), GenreDiscEntry.NUDE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.GLAMOUR ), GenreDiscEntry.GLAMOUR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.CITY ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.CHILDREN ), GenreDiscEntry.CHILDREN );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.GENRE ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.GENRE_PORTRAIT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.ANIMALS ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.DIGITAL_ART ), GenreDiscEntry.DIGITAL_ART );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.MACRO ), GenreDiscEntry.MACRO );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.STILL ), GenreDiscEntry.STILL );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.LANDSCAPE ), GenreDiscEntry.LANDSCAPE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.UNDERWATER ), GenreDiscEntry.UNDERWATER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.PORTRAIT ), GenreDiscEntry.PORTRAIT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.TRAVELLING ), GenreDiscEntry.TRAVELLING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.ADVERTISING ), GenreDiscEntry.ADVERTISING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.REPORTING ), GenreDiscEntry.REPORTING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.WEDDING ), GenreDiscEntry.WEDDING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.SPORT ), GenreDiscEntry.SPORT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.PHOTOSIGHT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.PHOTO_HUNTING ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.ARCHITECTURE ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.MODELS ), GenreDiscEntry.MODELS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.HUMOR ), GenreDiscEntry.HUMOR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.MOBILE_PHOTO ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.MUSEUM ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.NATURE ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.TECH ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.REST ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, utils( PhotosightCategory.PAPARAZZI ), GenreDiscEntry.OTHER );
	}

	private GenreDiscEntry utils( final PhotosightCategory photosightCategory ) {
		return new RemotePhotoSitePhotoImageFileUtils( PhotosImportSource.PHOTOSIGHT, systemVarsServiceMock.getRemotePhotoSitesCacheFolder() ).getGenreDiscEntry( photosightCategory );
	}
}
