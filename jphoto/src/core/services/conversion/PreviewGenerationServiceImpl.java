package core.services.conversion;

import core.general.configuration.ConfigurationKey;
import core.general.conversion.ConversionOptions;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.photo.PhotoService;
import core.services.system.ConfigurationService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PreviewGenerationServiceImpl implements PreviewGenerationService {

	@Autowired
	private FileConversionService fileConversionService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public File generatePreviewSync( final int photoId ) throws IOException, InterruptedException {
		return generatePreviewSync( photoId, getConversionOptions() );
	}

	@Override
	public File generatePreviewSync( final int photoId, final ConversionOptions conversionOptions ) throws IOException, InterruptedException {

		final Photo photo = photoService.load( photoId );

		userPhotoFilePathUtilsService.createUserPhotoPreviewDirIfNeed( photo.getUserId() );

		final File photoPreviewFile = userPhotoFilePathUtilsService.getPhotoPreviewFile( photo );

		return generatePreviewSync( photo.getPhotoImageFile(), photoPreviewFile, getConversionOptions() );
	}

	@Override
	public File generatePreviewSync( final User photoAuthor, final File photoFile ) throws IOException, InterruptedException {

		userPhotoFilePathUtilsService.createUserPhotoPreviewDirIfNeed( photoAuthor.getId() );

		synchronized ( photoAuthor.getName() ) {

			final File photoPreviewFile = userPhotoFilePathUtilsService.generatePhotoPreviewName( photoAuthor.getId() );
			generatePreviewSync( photoFile, photoPreviewFile, getConversionOptions() );

			return photoPreviewFile;
		}
	}

	@Override
	public File generatePreviewSync( final File photoImageFile, final File photoPreviewFile, final ConversionOptions conversionOptions ) throws IOException, InterruptedException {

		if ( photoPreviewFile.exists() ) {
			FileUtils.deleteQuietly( photoPreviewFile );
		}

		fileConversionService.convertSync( photoImageFile, photoPreviewFile, conversionOptions );

		return photoPreviewFile;
	}

	private ConversionOptions getConversionOptions() {
		return new ConversionOptions() {
			@Override
			public int getDensity() {
				return configurationService.getInt( ConfigurationKey.ADMIN_PHOTO_PREVIEW_DENSITY );
			}

			@Override
			public Dimension getDimension() {
				final int size = configurationService.getInt( ConfigurationKey.ADMIN_PHOTO_PREVIEW_DIMENSION );
				return new Dimension( size, size );
			}
		};
	}
}
