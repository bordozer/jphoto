package jobs.photosImport;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightCategory;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightImageFileUtils;
import common.AbstractTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotosightCategoryTest extends AbstractTestCase {

	public static final String WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING = "Wrong photosight photo category mapping";

	@Test
	public void photosightCategoryMappingTest() {
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.NUDE ), GenreDiscEntry.NUDE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.GLAMOUR ), GenreDiscEntry.GLAMOUR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.CITY ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.CHILDREN ), GenreDiscEntry.CHILDREN );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.GENRE ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.GENRE_PORTRAIT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.ANIMALS ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.DIGITAL_ART ), GenreDiscEntry.DIGITAL_ART );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.MACRO ), GenreDiscEntry.MACRO );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.STILL ), GenreDiscEntry.STILL );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.LANDSCAPE ), GenreDiscEntry.LANDSCAPE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.UNDERWATER ), GenreDiscEntry.UNDERWATER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.PORTRAIT ), GenreDiscEntry.PORTRAIT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.TRAVELLING ), GenreDiscEntry.TRAVELLING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.ADVERTISING ), GenreDiscEntry.ADVERTISING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.REPORTING ), GenreDiscEntry.REPORTING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.WEDDING ), GenreDiscEntry.WEDDING );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.SPORT ), GenreDiscEntry.SPORT );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.PHOTOSIGHT ), GenreDiscEntry.GENRE );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.PHOTO_HUNTING ), GenreDiscEntry.ANIMALS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.ARCHITECTURE ), GenreDiscEntry.CITY );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.MODELS ), GenreDiscEntry.MODELS );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.HUMOR ), GenreDiscEntry.HUMOR );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.MOBILE_PHOTO ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.MUSEUM ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.NATURE ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.TECH ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.REST ), GenreDiscEntry.OTHER );
		assertEquals( WRONG_PHOTOSIGHT_PHOTO_CATEGORY_MAPPING, PhotosightImageFileUtils.getGenreDiscEntry( PhotosightCategory.PAPARAZZI ), GenreDiscEntry.OTHER );
	}
}
