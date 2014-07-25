package jobs.photosImport;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSitePhotoImageFileUtils;
import common.AbstractTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotosightCategoryTest extends AbstractTestCase {

	public static final String WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING = "Wrong photosight photo category mapping";

	@Test
	public void photosightCategoryMappingTest() {
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.NUDE ), GenreDiscEntry.NUDE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.GLAMOUR ), GenreDiscEntry.GLAMOUR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.CITY ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.CHILDREN ), GenreDiscEntry.CHILDREN );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.GENRE ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.GENRE_PORTRAIT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.ANIMALS ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.DIGITAL_ART ), GenreDiscEntry.DIGITAL_ART );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.MACRO ), GenreDiscEntry.MACRO );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.STILL ), GenreDiscEntry.STILL );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.LANDSCAPE ), GenreDiscEntry.LANDSCAPE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.UNDERWATER ), GenreDiscEntry.UNDERWATER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.PORTRAIT ), GenreDiscEntry.PORTRAIT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.TRAVELLING ), GenreDiscEntry.TRAVELLING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.ADVERTISING ), GenreDiscEntry.ADVERTISING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.REPORTING ), GenreDiscEntry.REPORTING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.WEDDING ), GenreDiscEntry.WEDDING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.SPORT ), GenreDiscEntry.SPORT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.PHOTOSIGHT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.PHOTO_HUNTING ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.ARCHITECTURE ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.MODELS ), GenreDiscEntry.MODELS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.HUMOR ), GenreDiscEntry.HUMOR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.MOBILE_PHOTO ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.MUSEUM ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.NATURE ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.TECH ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.REST ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( PhotosightCategory.PAPARAZZI ), GenreDiscEntry.OTHER );
	}
}
