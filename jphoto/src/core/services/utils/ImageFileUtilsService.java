package core.services.utils;

import core.general.img.Dimension;
import core.services.translator.Language;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface ImageFileUtilsService {

	String BEAN_NAME = "imageFileUtilsService";

	String getContentType( File file );

	float getFileSizeInKb( float fileLength );

	Dimension getImageDimension( File file ) throws IOException;

	Dimension resizePhotoImage( Dimension dimension, Dimension toDimension );

	Dimension resizePhotoImage( Dimension dimension );

	Dimension resizePhotoFileToDimensionAndReturnResultDimension( File photoFile ) throws IOException;

	Dimension resizeImageToDimensionAndReturnResultDimension( File imageFile, Dimension toDimension ) throws IOException;

	void validateUploadedFile( final Errors errors, final MultipartFile multipartFile, final long maxFileSizeKb, final Dimension maxDimension, final String fileControlName, final Language language );
}
