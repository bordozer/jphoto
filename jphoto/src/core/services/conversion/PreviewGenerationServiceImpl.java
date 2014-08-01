package core.services.conversion;

import core.general.configuration.ConfigurationKey;
import core.general.conversion.ConversionOptions;
import core.general.photo.Photo;
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
	public boolean generatePreviewSync( final int photoId ) throws IOException, InterruptedException {

		final ConversionOptions conversionOptions = new ConversionOptions() {
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

		return generatePreviewSync( photoId, conversionOptions );
	}

	@Override
	public boolean generatePreviewSync( final int photoId, final ConversionOptions conversionOptions ) throws IOException, InterruptedException {
		final Photo photo = photoService.load( photoId );

		userPhotoFilePathUtilsService.createUserPhotoPreviewDirIfNeed( photo.getUserId() );

		final File destinationFile = userPhotoFilePathUtilsService.getPhotoPreviewFile( photo );

		if ( destinationFile.exists() ) {
			FileUtils.deleteQuietly( destinationFile );
		}

		return fileConversionService.convertSync( photo.getPhotoImageFile(), destinationFile, conversionOptions );
	}
}
