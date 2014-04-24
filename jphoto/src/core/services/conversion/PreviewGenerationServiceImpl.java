package core.services.conversion;

import core.general.conversion.ConversionOptions;
import core.general.photo.Photo;
import core.services.photo.PhotoService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

public class PreviewGenerationServiceImpl implements PreviewGenerationService {

	@Autowired
	private FileConversionService fileConversionService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Override
	public boolean generatePreviewSync( final int photoId ) throws IOException, InterruptedException {
		return generatePreviewSync( photoId, ConversionOptions.DEFAULT_PREVIEW_OPTION );
	}

	@Override
	public boolean generatePreviewSync( final int photoId, final ConversionOptions conversionOptions ) throws IOException, InterruptedException {
		final Photo photo = photoService.load( photoId );

		userPhotoFilePathUtilsService.createUserPhotoPreviewDirIfNeed( photo.getUserId() );

		final File destinationFile = userPhotoFilePathUtilsService.getPhotoPreviewFile( photo );

		if ( destinationFile.exists() ) {
			FileUtils.deleteQuietly( destinationFile );
		}

		return fileConversionService.convertSync( photo.getFile(), destinationFile, conversionOptions );
	}
}
