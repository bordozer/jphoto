package utils;

import common.AbstractTestCase;
import core.general.photo.Photo;
import core.general.photo.PhotoImageLocationType;
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

		final int userId = 1357;
		final User user = new User();
		user.setId( userId );
		user.setName( "User name" );

		final int photoId = 444;
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setUserId( userId );
		photo.setName( "Photo name" );

		photo.setPhotoImageLocationType( PhotoImageLocationType.FILE );
		photo.setPhotoImageFile( new File( "/photoDir", "photoFileName" ) );
		photo.setPhotoImageUrl( "some.host.ua/remote/url/image.jpg" );
		photo.setPhotoPreviewName( "preview_name_saved_in_db.jpg" );

		assertEquals( "photo/storage/path/1/1/1", userPhotoFilePathUtilsService.getUserPhotoDir( 1 ).getPath() );
		assertEquals( "photo/storage/path/1/12/12", userPhotoFilePathUtilsService.getUserPhotoDir( 12 ).getPath() );
		assertEquals( "photo/storage/path/1/123/123", userPhotoFilePathUtilsService.getUserPhotoDir( 123 ).getPath() );
		assertEquals( "photo/storage/path/1/123/1234", userPhotoFilePathUtilsService.getUserPhotoDir( 1234 ).getPath() );
		assertEquals( "photo/storage/path/1/123/12345", userPhotoFilePathUtilsService.getUserPhotoDir( 12345 ).getPath() );

		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/preview/", userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );

		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/", userPhotoFilePathUtilsService.getPhotoImageUrl( photo ) );
		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/preview/", userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );

		assertEquals( "photo/storage/path/1/135/1357/preview/1357_00001.jpg", userPhotoFilePathUtilsService.generatePhotoPreviewName( photo.getUserId() ).getPath() );
		assertEquals( "photo/storage/path/1/135/1357/preview/preview_name_saved_in_db.jpg", userPhotoFilePathUtilsService.getPhotoPreviewFile( photo ).getPath() );
		assertEquals( "_avatar_1357.jpg", userPhotoFilePathUtilsService.getUserAvatarFileName( userId ) );
		assertEquals( "http://127.0.0.1:8085/worker/download/file/?filePath=photo/storage/path/1/135/1357/_avatar_1357.jpg", userPhotoFilePathUtilsService.getUserAvatarFileUrl( userId ) );

		photo.setPhotoImageLocationType( PhotoImageLocationType.WEB );
		assertEquals( "http://some.host.ua/remote/url/image.jpg", userPhotoFilePathUtilsService.getPhotoImageUrl( photo ) );
		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/preview/", userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );
	}
}
