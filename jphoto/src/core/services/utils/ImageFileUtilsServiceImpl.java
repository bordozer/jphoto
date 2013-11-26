package core.services.utils;

import core.exceptions.BaseRuntimeException;
import core.general.configuration.ConfigurationKey;
import core.general.img.Dimension;
import core.log.LogHelper;
import core.services.system.ConfigurationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import utils.FormatUtils;
import utils.NumberUtils;
import utils.PhotoUtils;
import utils.TranslatorUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

public class ImageFileUtilsServiceImpl implements ImageFileUtilsService {

	private final LogHelper log = new LogHelper( ImageFileUtilsServiceImpl.class );

	@Autowired
	private SystemFilePathUtilsService systemFilePathUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public String getContentType( final File file ) {
		return URLConnection.guessContentTypeFromName( file.getName() );
	}

	@Override
	public float getFileSizeInKb( final float fileLength ) {
		return NumberUtils.round( fileLength / 1024, 1 );
	}

	@Override
	public Dimension getImageDimension( final File file ) throws IOException {

		if ( ! file.exists() ) {
			throw new BaseRuntimeException( "Photo image file does not exist!" );
		}

		final BufferedImage img = ImageIO.read( file );

		if ( img == null ) {
			return new Dimension( 200, 200 );
		}

		int width = img.getWidth();
		int height = img.getHeight();

		return new Dimension( width, height );
	}

	@Override
	public Dimension resizePhotoImage( final Dimension dimension, final Dimension toDimension ) {
		final int origWidth = dimension.getWidth();
		final int origHeight = dimension.getHeight();

		final int maxImageWidth = toDimension.getWidth();
		final int maxImageHeight = toDimension.getHeight();

		float resultWidth = origWidth;
		float resultHeight = origHeight;

		if ( origWidth > maxImageWidth ) {

			float widthCoeff = ( float ) origWidth / maxImageWidth;
			resultWidth = maxImageWidth;
			resultHeight = origHeight / widthCoeff;

			if ( resultHeight > maxImageHeight ) {
				float heightCoeff = resultHeight / maxImageHeight;
				resultHeight = maxImageHeight;
				resultWidth = resultWidth / heightCoeff;
			}
		} else if ( origHeight > maxImageHeight ) {
			resultHeight = maxImageHeight;
			resultWidth = resultWidth / ( ( float ) origHeight / maxImageHeight );
		}

		return new Dimension( ( int ) resultWidth, ( int ) resultHeight );
	}

	@Override
	public Dimension resizePhotoImage( final Dimension dimension ) {
		final int maxImageWidth = configurationService.getInt( ConfigurationKey.PHOTO_CARD_MAX_WIDTH );
		final int maxImageHeight = configurationService.getInt( ConfigurationKey.PHOTO_CARD_MAX_HEIGHT );

		final Dimension toDimension = new Dimension( maxImageWidth, maxImageHeight );
		return resizePhotoImage( dimension, toDimension );
	}

	@Override
	public Dimension resizePhotoFileToDimensionAndReturnResultDimension( final File photoFile ) throws IOException {
		final Dimension originalDimension = getImageDimension( photoFile );
		return resizePhotoImage( originalDimension );
	}

	@Override
	public Dimension resizeImageToDimensionAndReturnResultDimension( final File imageFile, final Dimension toDimension ) throws IOException {
		final Dimension originalDimension = getImageDimension( imageFile );
		return resizePhotoImage( originalDimension, toDimension );
	}

	@Override
	public void validateUploadedFile( final Errors errors, final MultipartFile multipartFile, final long maxFileSizeKb, final Dimension maxDimension, final String fileControlName ) {

		final String fileName = multipartFile.getOriginalFilename();

		if ( StringUtils.isEmpty( fileName ) ) {
			errors.rejectValue( fileControlName, TranslatorUtils.translate( String.format( "Select %s.", FormatUtils.getFormattedFieldName( "File" ) ) ) );
			return;
		}

		if ( isFileToBig( multipartFile, maxFileSizeKb ) ) {
			final long actualFileSizeKiloBytes = multipartFile.getSize() / 1024;
			errors.rejectValue( fileControlName, TranslatorUtils.translate( String.format( "%s size should be less then $1 Kilobytes.<br />Attempt to upload $2 Kilobytes", FormatUtils.getFormattedFieldName( "File" ) ), String.valueOf( maxFileSizeKb ), String.valueOf( actualFileSizeKiloBytes ) ) );
			return;
		}

		final String contentType = multipartFile.getContentType();
		final List<String> allowedExtensions = configurationService.getListString( ConfigurationKey.PHOTO_UPLOAD_FILE_ALLOWED_EXTENSIONS );
		if ( !PhotoUtils.isPhotoContentTypeSupported( allowedExtensions, contentType ) ) {
			errors.rejectValue( fileControlName, TranslatorUtils.translate( String.format( "Unsupported %s type is uploaded - $1.", FormatUtils.getFormattedFieldName( "File" ) ), contentType ) );
			return;
		}

		try {
			checkFileDimension( multipartFile, maxDimension, errors, fileControlName );
		} catch ( IOException e ) {
			errors.rejectValue( fileControlName, TranslatorUtils.translate( "Error reading file dimension" ) );
		}
	}

	private void checkFileDimension( final MultipartFile multipartFile, final Dimension maxDimension, final Errors errors, final String fileControlName ) throws IOException {

		final String originalFilename = multipartFile.getOriginalFilename();
		final File uploadedFile = new File( systemFilePathUtilsService.getTempDir().getFile().getPath(), originalFilename );
		if ( !uploadedFile.createNewFile() ) {
			throw new IOException( String.format( "Can not create file '%s'", originalFilename ) );
		}

		final FileOutputStream fos = new FileOutputStream( uploadedFile );
		fos.write( multipartFile.getBytes() );
		fos.close();

		final Dimension dimension = getImageDimension( uploadedFile );

		if ( dimension.getWidth() > maxDimension.getWidth() || dimension.getHeight() > maxDimension.getHeight()) {
			errors.rejectValue( fileControlName, TranslatorUtils.translate( String.format( "Max %s dimension is %s x %s, but uploaded file is %s x %s", FormatUtils.getFormattedFieldName( "File" ), maxDimension.getWidth(), maxDimension.getHeight(), dimension.getWidth(), dimension.getHeight() ) ) );
		}
	}

	private boolean isFileToBig( final MultipartFile multipartFile, final long maxFileSizeKb ) {
		return getFileSizeInKb( multipartFile.getSize() ) > maxFileSizeKb;
	}
}
