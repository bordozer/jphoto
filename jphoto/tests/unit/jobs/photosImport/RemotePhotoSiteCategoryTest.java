package jobs.photosImport;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSitePhotoImageFileUtils;
import common.AbstractTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RemotePhotoSiteCategoryTest extends AbstractTestCase {

	public static final String WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING = "Wrong photosight photo category mapping";

	@Test
	public void photosightCategoryMappingTest() {
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.NUDE ), GenreDiscEntry.NUDE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.GLAMOUR ), GenreDiscEntry.GLAMOUR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.CITY ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.CHILDREN ), GenreDiscEntry.CHILDREN );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.GENRE ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.GENRE_PORTRAIT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.ANIMALS ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.DIGITAL_ART ), GenreDiscEntry.DIGITAL_ART );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.MACRO ), GenreDiscEntry.MACRO );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.STILL ), GenreDiscEntry.STILL );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.LANDSCAPE ), GenreDiscEntry.LANDSCAPE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.UNDERWATER ), GenreDiscEntry.UNDERWATER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.PORTRAIT ), GenreDiscEntry.PORTRAIT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.TRAVELLING ), GenreDiscEntry.TRAVELLING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.ADVERTISING ), GenreDiscEntry.ADVERTISING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.REPORTING ), GenreDiscEntry.REPORTING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.WEDDING ), GenreDiscEntry.WEDDING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.SPORT ), GenreDiscEntry.SPORT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.PHOTOSIGHT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.PHOTO_HUNTING ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.ARCHITECTURE ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.MODELS ), GenreDiscEntry.MODELS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.HUMOR ), GenreDiscEntry.HUMOR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.MOBILE_PHOTO ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.MUSEUM ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.NATURE ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.TECH ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.REST ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.PAPARAZZI ), GenreDiscEntry.OTHER );
	}
}
