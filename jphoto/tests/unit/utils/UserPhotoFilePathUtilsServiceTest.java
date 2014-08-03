package utils;

import common.AbstractTestCase;
import core.general.photo.Photo;
import core.general.photo.PhotoImageImportStrategyType;
import core.general.user.User;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class UserPhotoFilePathUtilsServiceTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void test() {

		final int userId = 111;
		final User user = new User();
		user.setId( userId );
		user.setName( "User name" );

		final int photoId = 444;
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setUserId( userId );
		photo.setName( "Photo name" );

		photo.setPhotoImageImportStrategyType( PhotoImageImportStrategyType.FILE );
		photo.setPhotoImageFile( new File( "/photoDir", "photoFileName" ) );
		photo.setPhotoImageUrl( "some.host.ua/remote/url/image.jpg" );
		photo.setPhotoPreviewName( "preview_name_saved_in_db.jpg" );

		assertEquals( "photo/storage/path/111", userPhotoFilePathUtilsService.getUserPhotoDir( userId ).getPath() );
		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/preview/", userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );

		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/", userPhotoFilePathUtilsService.getPhotoImageUrl( photo ) );
		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/preview/", userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );

		assertEquals( "photo/storage/path/111/preview/111_00001", userPhotoFilePathUtilsService.generatePhotoPreviewName( photo.getUserId() ).getPath() );
		assertEquals( "photo/storage/path/111/preview/preview_name_saved_in_db.jpg", userPhotoFilePathUtilsService.getPhotoPreviewFile( photo ).getPath() );
		assertEquals( "_avatar_111.jpg", userPhotoFilePathUtilsService.getUserAvatarFileName( userId ) );
		assertEquals( "http://127.0.0.1:8085/worker/download/file/?filePath=photo/storage/path/111/_avatar_111.jpg", userPhotoFilePathUtilsService.getUserAvatarFileUrl( userId ) );

		photo.setPhotoImageImportStrategyType( PhotoImageImportStrategyType.WEB );
		assertEquals( "http://some.host.ua/remote/url/image.jpg", userPhotoFilePathUtilsService.getPhotoImageUrl( photo ) );
		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/preview/", userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );
	}
}
