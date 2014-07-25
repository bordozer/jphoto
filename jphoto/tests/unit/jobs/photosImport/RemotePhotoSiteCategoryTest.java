package jobs.photosImport;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightImageFileUtils;
import common.AbstractTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RemotePhotoSiteCategoryTest extends AbstractTestCase {

	public static final String WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING = "Wrong photosight photo category mapping";

	@Test
	public void photosightCategoryMappingTest() {
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.NUDE ), GenreDiscEntry.NUDE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.GLAMOUR ), GenreDiscEntry.GLAMOUR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.CITY ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.CHILDREN ), GenreDiscEntry.CHILDREN );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.GENRE ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.GENRE_PORTRAIT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.ANIMALS ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.DIGITAL_ART ), GenreDiscEntry.DIGITAL_ART );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.MACRO ), GenreDiscEntry.MACRO );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.STILL ), GenreDiscEntry.STILL );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.LANDSCAPE ), GenreDiscEntry.LANDSCAPE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.UNDERWATER ), GenreDiscEntry.UNDERWATER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.PORTRAIT ), GenreDiscEntry.PORTRAIT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.TRAVELLING ), GenreDiscEntry.TRAVELLING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.ADVERTISING ), GenreDiscEntry.ADVERTISING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.REPORTING ), GenreDiscEntry.REPORTING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.WEDDING ), GenreDiscEntry.WEDDING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.SPORT ), GenreDiscEntry.SPORT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.PHOTOSIGHT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.PHOTO_HUNTING ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.ARCHITECTURE ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.MODELS ), GenreDiscEntry.MODELS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.HUMOR ), GenreDiscEntry.HUMOR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.MOBILE_PHOTO ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.MUSEUM ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.NATURE ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.TECH ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.REST ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( RemotePhotoSiteCategory.PAPARAZZI ), GenreDiscEntry.OTHER );
	}
}
