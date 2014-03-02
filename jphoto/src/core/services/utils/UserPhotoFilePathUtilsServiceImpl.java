package core.services.utils;

import core.general.img.Dimension;
import core.general.photo.Photo;
import core.general.user.User;
import core.log.LogHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

public class UserPhotoFilePathUtilsServiceImpl implements UserPhotoFilePathUtilsService {

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private SystemFilePathUtilsService systemFilePathUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private ImageFileUtilsService imageFileUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public File getUserPhotoDir( final int userId ) {
		return new File( systemFilePathUtilsService.getSystemPhotoDir().getPath(), getUserPhotoPathPrefix( userId ) );
	}

	@Override
	public String getUserPhotoPathPrefix( final int userId ) {
		return String.valueOf( userId );
	}

	@Override
	public File getPhotoFile( final Photo photo ) {
		return new File ( String.format( "%s", photo.getFile() ) );
	}

	@Override
	public String getPhotoPreviewUrl( final Photo photo ) {
		return String.format( "%s/download/photos/%s/preview/", urlUtilsService.getBaseURLWithPrefix(), photo.getId() );
	}

	@Override
	public String getPhotoUrl( final Photo photo ) {
		return String.format( "%s/download/photos/%s/", urlUtilsService.getBaseURLWithPrefix(), photo.getId() );
	}

	@Override
	public void createUserPhotoPreviewDirIfNeed( final int userId ) {
		final File userPhotoPreviewDir = getUserPhotoPreviewDir( userId );
		if ( !userPhotoPreviewDir.exists() ) {
			userPhotoPreviewDir.mkdirs();
		}
	}

	@Override
	public File getUserPhotoPreviewDir( final int userId ) {
		return new File( getUserPhotoDir( userId ).getPath(), systemVarsService.getPreviewFolderName() );
	}

	@Override
	public boolean isUserPhotoDirExist( final int userId ) {
		return getUserPhotoDir( userId ).exists();
	}

	@Override
	public String generateUserPhotoPreviewFileName( final Photo photo ) {
		return String.format( "%s_preview.jpg", FilenameUtils.getBaseName( photo.getFile().getName() ) );
	}

	@Override
	public File getPhotoPreviewFile( final Photo photo ) {
		return new File( getUserPhotoPreviewDir( photo.getUserId() ), generateUserPhotoPreviewFileName( photo ) );
	}

	@Override
	public String generateUserPhotoFileName( final User user, final int photoId ) {
		return String.format( "u%s_p%s_%s.jpg", user.getId(), photoId, dateUtilsService.getCurrentTime().getTime() );
	}

	@Override
	public File getUserPhotoFile( final User user, final Photo photo ) {
		final String photoFileName = generateUserPhotoFileName( user, photo.getId() );
		return new File( getUserPhotoDir( user.getId() ), photoFileName );
	}

	@Override
	public void createUserPhotoDirIfNeed( final int userId ) {
		final File photoStorageDir = systemFilePathUtilsService.createSystemPhotoDirIfNeed();

		final File userPhotoDir = new File( photoStorageDir.getPath(), getUserPhotoPathPrefix( userId ) );
		if ( !userPhotoDir.exists() ) {
			userPhotoDir.mkdir();
		}
	}

	@Override
	public void deletePhotoFileWithPreview( final Photo photo ) {
		final File previewFile = getPhotoPreviewFile( photo );
		if ( previewFile.exists() && previewFile.isFile() ) {
			FileUtils.deleteQuietly( previewFile );
		}

		final File file = photo.getFile();
		if ( file.exists() && file.isFile() ) {
			FileUtils.deleteQuietly( file );
		}
	}

	@Override
	public String getUserAvatarFileName( final int userId ) {
		return String.format( "_avatar_%s.jpg", userId );
	}

	@Override
	public File getUserAvatarDir( final int userId ) {
		return getUserPhotoDir( userId );
	}

	@Override
	public File getUserAvatarFile( final int userId ) {
		return new File( getUserPhotoDir( userId ).getPath(), getUserAvatarFileName( userId ) );
	}

	@Override
	public String getUserAvatarFileUrl( final int userId ) {
		return String.format( "%s/download/file/?filePath=%s", urlUtilsService.getBaseURLWithPrefix(), getUserAvatarFile( userId ) );
	}

	@Override
	public boolean isUserHasAvatar( final int userId ) {
		return getUserAvatarFile( userId ).exists();
	}

	@Override
	public String getUserAvatarImage( final int userId, final int width, final int height, final String imageId, final String onClick, final String cssStyle ) throws IOException {
		final File userAvatarFile = getUserAvatarFile( userId );
		final String avatarImageUrl = getUserAvatarFileUrl( userId );

		final Dimension toDimension = new Dimension( width, height );
		final Dimension dimension = imageFileUtilsService.resizeImageToDimensionAndReturnResultDimension( userAvatarFile, toDimension );

		final String onclick = StringUtils.isEmpty( onClick ) ? "" : String.format( "onClick='%s'", onClick );
		final String css = StringUtils.isEmpty( cssStyle ) ? "" : String.format( "style='%s'", cssStyle );

		final String imageId1 = StringUtils.isEmpty( imageId ) ? "" : String.format( "id='%s'", imageId );
		return String.format( "<img %s src='%s' width='%d' height='%d' %s %s/>", imageId1, avatarImageUrl, dimension.getWidth(), dimension.getHeight(), onclick, css );
	}

	@Override
	public File copyFileToUserFolder( final File picture, final Photo photo, final User user ) throws IOException {
		if ( !isUserPhotoDirExist( user.getId() ) ) {
			createUserPhotoDirIfNeed( user.getId() );
		}

		final File destFile = getUserPhotoFile( user, photo );

		try {
			FileUtils.copyFile( picture, destFile );
			photo.setFile( destFile );
		} catch ( final IOException e ) {
			new LogHelper( UserPhotoFilePathUtilsServiceImpl.class ).error( String.format( "Can not copy photo file: '%s' to '%s'", picture.getPath(), destFile.getPath() ), e );
			throw e;
		}

		return destFile;
	}

	public void setSystemVarsService( final SystemVarsService systemVarsService ) {
		this.systemVarsService = systemVarsService;
	}

	public void setSystemFilePathUtilsService( final SystemFilePathUtilsService systemFilePathUtilsService ) {
		this.systemFilePathUtilsService = systemFilePathUtilsService;
	}

	public void setUrlUtilsService( final UrlUtilsService urlUtilsService ) {
		this.urlUtilsService = urlUtilsService;
	}

	public void setImageFileUtilsService( final ImageFileUtilsService imageFileUtilsService ) {
		this.imageFileUtilsService = imageFileUtilsService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}
}
