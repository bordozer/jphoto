package core.services.utils;

import core.general.photo.Photo;
import core.general.user.User;

import java.io.File;
import java.io.IOException;

public interface UserPhotoFilePathUtilsService {

	String BEAN_NAME = "userPhotoFilePathUtilsService";

	File getUserPhotoDir( int userId );

	String getUserPhotoPathPrefix( int userId );

	File getPhotoFile( Photo photo );

	String getPhotoPreviewUrl( Photo photo );

	String getPhotoUrl( Photo photo );

	void createUserPhotoPreviewDirIfNeed( int userId );

	File getUserPhotoPreviewDir( int userId );

	boolean isUserPhotoDirExist( int userId );

	String generateUserPhotoPreviewFileName( Photo photo );

	File getPhotoPreviewFile( Photo photo );

	void createUserPhotoDirIfNeed( int userId );

	void deletePhotoFileWithPreview( Photo photo );

	String getUserAvatarFileName( int userId );

	File getUserAvatarDir( int userId );

	File getUserAvatarFile( int userId );

	String getUserAvatarFileUrl( int userId );

	boolean isUserHasAvatar( int userId );

	String getUserAvatarImage( int userId, int width, int height, String imageId, String onClick, String cssStyle ) throws IOException;

	File copyFileToUserFolder( final User user, final File picture ) throws IOException;
}
