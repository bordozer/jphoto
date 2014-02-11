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

		final String projectUrl = systemVarsService.getProjectUrlClosed();
		final String workerName = systemVarsService.getTomcatWorkerName();
		final String appPrefix = systemVarsService.getApplicationPrefix();
		final String photoStoragePath = systemVarsService.getPhotoStoragePath();

		final int userId = 111;
		final User user = new User();
		user.setId( userId );
		user.setName( "User name" );

		final int photoId = 444;
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setUserId( userId );
		photo.setName( "Photo name" );
		photo.setFile( new File( "/photoDir", "photoFileName" ) );

		assertEquals( String.format( "%s/%d", photoStoragePath, userId ), userPhotoFilePathUtilsService.getUserPhotoDir( userId ).getPath() );
		assertEquals( String.format( "%s%s/%s/download/photos/%d/preview/", projectUrl, workerName, appPrefix, photoId ), userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );
		assertEquals( String.format( "%s%s/%s/download/photos/%d/", projectUrl, workerName, appPrefix, photoId ), userPhotoFilePathUtilsService.getPhotoUrl( photo ) );
		assertEquals( String.format( "photoFileName_preview.jpg" ), userPhotoFilePathUtilsService.generateUserPhotoPreviewFileName( photo ) );
		assertEquals( String.format( "%s/%d/previews/photoFileName_preview.jpg", photoStoragePath, userId ), userPhotoFilePathUtilsService.getPhotoPreviewFile( photo ).getPath() );
		assertEquals( String.format( "_avatar_%d.jpg", userId ), userPhotoFilePathUtilsService.getUserAvatarFileName( userId ) );
		assertEquals( String.format( "%s%s/%s/download/file/?filePath=%s/%d/_avatar_%d.jpg"
			, projectUrl, workerName, appPrefix, photoStoragePath, userId, userId ), userPhotoFilePathUtilsService.getUserAvatarFileUrl( userId ) );
	}
}
