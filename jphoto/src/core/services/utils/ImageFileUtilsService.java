package core.services.utils;

import core.general.img.Dimension;
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

	void validateUploadedFile( Errors errors, MultipartFile multipartFile, long maxFileSizeKb, Dimension maxDimension, String fileControlName );
}
