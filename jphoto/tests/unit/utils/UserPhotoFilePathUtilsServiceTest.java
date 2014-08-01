package utils;

import common.AbstractTestCase;
import core.general.photo.Photo;
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
		photo.setPhotoImageFile( new File( "/photoDir", "photoFileName" ) );

		assertEquals( "photo/storage/path/111", userPhotoFilePathUtilsService.getUserPhotoDir( userId ).getPath() );
		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/preview/", userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );
		assertEquals( "http://127.0.0.1:8085/worker/download/photos/444/", userPhotoFilePathUtilsService.getPhotoUrl( photo ) );
		assertEquals( "photoFileName_preview.jpg", userPhotoFilePathUtilsService.generatePhotoPreviewName( photo.getUserId() ) );
		assertEquals( "photo/storage/path/111/preview/photoFileName_preview.jpg", userPhotoFilePathUtilsService.getPhotoPreviewFile( photo ).getPath() );
		assertEquals( "_avatar_111.jpg", userPhotoFilePathUtilsService.getUserAvatarFileName( userId ) );
		assertEquals( "http://127.0.0.1:8085/worker/download/file/?filePath=photo/storage/path/111/_avatar_111.jpg", userPhotoFilePathUtilsService.getUserAvatarFileUrl( userId ) );
	}
}
