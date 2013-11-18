package core.services.utils;

import core.general.img.Dimension;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ImageFileUtilsService {

	String BEAN_NAME = "imageFileUtilsService";

	String getContentType( File file );

	float getFileSizeInKb( float fileLength );

	Dimension getImageDimension( File file );

	Dimension resizePhotoImage( Dimension dimension, Dimension toDimension );

	Dimension resizePhotoImage( Dimension dimension );

	Dimension resizePhotoFileToDimensionAndReturnResultDimension( File photoFile );

	Dimension resizeImageToDimensionAndReturnResultDimension( File imageFile, Dimension toDimension );

	void validateUploadedFile( Errors errors, MultipartFile multipartFile, long maxFileSizeKb, Dimension maxDimension, String fileControlName );
}
