package core.services.utils;

import core.general.photo.Photo;
import core.general.user.User;

import java.io.File;
import java.io.IOException;

public interface UserPhotoFilePathUtilsService {

	String BEAN_NAME = "userPhotoFilePathUtilsService";

	File getUserPhotoDir( final int userId );

	String getPhotoPreviewUrl( final Photo photo );

	void createUserPhotoPreviewDirIfNeed( final int userId );

	File getPhotoPreviewFile( final Photo photo );

	String getPhotoImageUrl( final Photo photo );

	File generatePhotoPreviewName( final int userId );

	void createUserPhotoDirIfNeed( final int userId );

	void deletePhotoFileWithPreview( final Photo photo );

	String getUserAvatarFileName( final int userId );

	File getUserAvatarDir( final int userId );

	File getUserAvatarFile( final int userId );

	String getUserAvatarFileUrl( final int userId );

	boolean isUserHasAvatar( final int userId );

	String getUserAvatarImage( final int userId, final int width, final int height, final String imageId, final String onClick, final String cssStyle ) throws IOException;

	File copyPhotoImageFileToUserFolder( final User user, final File photoImageFile ) throws IOException;
}
