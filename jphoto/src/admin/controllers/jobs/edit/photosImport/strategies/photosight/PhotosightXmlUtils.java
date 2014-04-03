package admin.controllers.jobs.edit.photosImport.strategies.photosight;

import core.exceptions.BaseRuntimeException;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import utils.NumberUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosightXmlUtils {

	public static final String PHOTOSIGHT_USER_INFO_FILE_ROOT_ELEMENT = "author";
	public static final String PHOTOSIGHT_USER_INFO_FILE_USER_ID = "id";
	public static final String PHOTOSIGHT_USER_INFO_FILE_USER_NAME = "name";

	public static final String PHOTOSIGHT_USER_INFO_FILE_PHOTO_ELEMENT_NAME = "photo";
	public static final String PHOTOSIGHT_USER_INFO_FILE_PHOTO_ID = "photoId";
	public static final String PHOTOSIGHT_USER_INFO_FILE_PHOTO_CATEGORY_NAME = "categoryName";
	private static final String PHOTOSIGHT_USER_INFO_FILE_PHOTO_CATEGORY_ID = "categoryId";
	public static final String PHOTOSIGHT_USER_INFO_FILE_PHOTO_NAME = "name";
	public static final String PHOTOSIGHT_USER_INFO_FILE_PHOTO_UPLOAD_TIME = "uploadTime";
	public static final String PHOTOSIGHT_USER_INFO_FILE_PHOTO_IMAGE_URL = "imageUrl";

	private static final String PHOTOSIGHT_USER_INFO_FILE_PHOTO_COMMENT_ELEMENT_NAME = "comments";
	private static final String PHOTOSIGHT_USER_INFO_FILE_PHOTO_COMMENT_TEXT = "commentText";

	public static final String XML_FILE_PHOTO_UPLOAD_TIME_FORMAT = "EEE MMM d HH:mm:ss Z yyyy";

	public static void createUserInfoFile( final PhotosightUser photosightUser ) throws IOException {

		final Document document = DocumentHelper.createDocument();
		final Element rootElement = document.addElement( PHOTOSIGHT_USER_INFO_FILE_ROOT_ELEMENT );

		rootElement.addElement( PHOTOSIGHT_USER_INFO_FILE_USER_ID ).addText( String.valueOf( photosightUser.getId() ) );
		rootElement.addElement( PHOTOSIGHT_USER_INFO_FILE_USER_NAME ).addText( StringEscapeUtils.escapeXml( photosightUser.getName() ) );

		final File userInfoFile = getUserInfoFile( photosightUser );
		if ( userInfoFile.exists() ) {
			return;
		}

		final FileWriter fileWriter = new FileWriter( userInfoFile );
		final OutputFormat format = OutputFormat.createPrettyPrint();
		final XMLWriter output = new XMLWriter( fileWriter, format );
		output.write( document );
		output.close();
	}

	public static void writeImageContentToFile( final File imageFile, final String imageContent, final String charsetName ) throws IOException {
		final BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( imageFile ) );
		bos.write( imageContent.getBytes( charsetName ) );
		bos.flush();
		bos.close();
	}

	private static String escapeFileName( final String param ) {
		final String currentEncoding = "UTF-8";
		try {
			return new String( Charset.forName( currentEncoding ).encode( param ).array(), currentEncoding ).trim().replaceAll( "[\\\\/*?|!-&:-@']", "_" );
		} catch ( UnsupportedEncodingException e ) {
			throw new BaseRuntimeException( e );
		}
	}

	public static void cachePhotosightPhotosLocally( final PhotosightUser photosightUser, final List<PhotosightPhoto> photosightPhotos, final DateUtilsService dateUtilsService ) throws IOException {

		final Document document = DocumentHelper.createDocument();
		final Element rootElement = document.addElement( PHOTOSIGHT_USER_INFO_FILE_ROOT_ELEMENT );

		rootElement.addElement( PHOTOSIGHT_USER_INFO_FILE_USER_ID ).addText( String.valueOf( photosightUser.getId() ) );
		rootElement.addElement( PHOTOSIGHT_USER_INFO_FILE_USER_NAME ).addText( StringEscapeUtils.escapeXml( photosightUser.getName() ) );

		for ( final PhotosightPhoto photosightPhoto : photosightPhotos ) {
			final Element photoElement = rootElement.addElement( PHOTOSIGHT_USER_INFO_FILE_PHOTO_ELEMENT_NAME );
			photoElement.addElement( PHOTOSIGHT_USER_INFO_FILE_PHOTO_ID ).addText( String.valueOf( photosightPhoto.getPhotoId() ) );
			photoElement.addElement( PHOTOSIGHT_USER_INFO_FILE_PHOTO_CATEGORY_ID ).addText( String.valueOf( photosightPhoto.getPhotosightCategory().getId() ) );
			photoElement.addElement( PHOTOSIGHT_USER_INFO_FILE_PHOTO_CATEGORY_NAME ).addText( PhotosightImageFileUtils.getGenreDiscEntry( photosightPhoto.getPhotosightCategory() ).getName() );
			photoElement.addElement( PHOTOSIGHT_USER_INFO_FILE_PHOTO_NAME ).addText( StringEscapeUtils.escapeXml( photosightPhoto.getName() ) );
			photoElement.addElement( PHOTOSIGHT_USER_INFO_FILE_PHOTO_UPLOAD_TIME ).addText( dateUtilsService.formatDateTime( photosightPhoto.getUploadTime(), XML_FILE_PHOTO_UPLOAD_TIME_FORMAT ) );
			photoElement.addElement( PHOTOSIGHT_USER_INFO_FILE_PHOTO_IMAGE_URL ).addText( photosightPhoto.getImageUrl() );

			final List<String> comments = photosightPhoto.getComments();
			if ( comments != null ) {
				final Element commentElement = photoElement.addElement( PHOTOSIGHT_USER_INFO_FILE_PHOTO_COMMENT_ELEMENT_NAME );
				for ( final String comment : comments ) {
					commentElement.addElement( PHOTOSIGHT_USER_INFO_FILE_PHOTO_COMMENT_TEXT ).addText( StringEscapeUtils.escapeXml( comment ) );
				}
			}
		}

		final FileWriter fileWriter = new FileWriter( getUserInfoFile( photosightUser ) );
		final OutputFormat format = OutputFormat.createPrettyPrint();
		final XMLWriter output = new XMLWriter( fileWriter, format );
		output.write( document );
		output.close();
	}

	public static List<PhotosightPhoto> getPhotosFromPhotosightUserInfoFile( final PhotosightUser photosightUser, final Services services, final Language language ) throws IOException, DocumentException {
		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final TranslatorService translatorService = services.getTranslatorService();

		final SAXReader reader = new SAXReader( false );
		final File userInfoFile = getUserInfoFile( photosightUser );
		final Document document = reader.read( userInfoFile );

		final Iterator photosIterator = document.getRootElement().elementIterator( PHOTOSIGHT_USER_INFO_FILE_PHOTO_ELEMENT_NAME );
		final List<PhotosightPhoto> result = newArrayList();
		while ( photosIterator.hasNext() ) {
			final Element photoElement = ( Element ) photosIterator.next();
			final int photosightPhotoId = NumberUtils.convertToInt( photoElement.element( PHOTOSIGHT_USER_INFO_FILE_PHOTO_ID ).getText() );

			final String savedCategoryId = photoElement.element( PHOTOSIGHT_USER_INFO_FILE_PHOTO_CATEGORY_ID ).getText();
			final PhotosightCategory category = PhotosightCategory.getById( NumberUtils.convertToInt( savedCategoryId ) );
			if ( category == null ) {
				final String message = translatorService.translate( "File '$1' contains unknown photosight categoryId '$2'. Note: file deletion may solve the problem because the import will be done again", language
					, userInfoFile.getAbsolutePath()
					, savedCategoryId
				);
				throw new BaseRuntimeException( message );
			}

			final String photoName = StringEscapeUtils.unescapeXml( photoElement.element( PHOTOSIGHT_USER_INFO_FILE_PHOTO_NAME ).getText() );

			final String textUploadTime = photoElement.element( PHOTOSIGHT_USER_INFO_FILE_PHOTO_UPLOAD_TIME ).getText();
			final Date uploadTime;
			if ( NumberUtils.isNumeric( textUploadTime ) ) {
				uploadTime = new Date( Long.parseLong( textUploadTime ) ); // for backward compatibility
			} else {
				uploadTime = dateUtilsService.parseDateTimeWithFormat( textUploadTime, XML_FILE_PHOTO_UPLOAD_TIME_FORMAT );
			}
			final String imageUrl = photoElement.element( PHOTOSIGHT_USER_INFO_FILE_PHOTO_IMAGE_URL ).getText();

			final PhotosightPhoto photosightPhoto = new PhotosightPhoto( photosightUser, photosightPhotoId, category );
			photosightPhoto.setName( photoName );
			photosightPhoto.setUploadTime( uploadTime );
			photosightPhoto.setImageUrl( imageUrl );

			final List<String> comments = newArrayList();
			final Element commentsElement = photoElement.element( PHOTOSIGHT_USER_INFO_FILE_PHOTO_COMMENT_ELEMENT_NAME );

			if ( commentsElement != null ) {
				final Iterator commentsIterator = commentsElement.elementIterator( PHOTOSIGHT_USER_INFO_FILE_PHOTO_COMMENT_TEXT );
				while ( commentsIterator.hasNext() ) {
					final Element commentElement = ( Element ) commentsIterator.next();
					final String commentText = commentElement.getText();
					final String comment = StringEscapeUtils.unescapeXml( commentText );

					comments.add( comment );
				}
			}
			photosightPhoto.setComments( comments );

			photosightPhoto.setCached( true );

			result.add( photosightPhoto );
		}


		return result;
	}

	public static File getUserInfoFile( final PhotosightUser photosightUser ) throws IOException {
		return new File( PhotosightImageFileUtils.getUserFolderForPhotoDownloading( photosightUser ), getUserInfoFileName( photosightUser ) );
	}

	private static String getUserInfoFileName( final PhotosightUser photosightUser ) {
		return String.format( "%d.xml", photosightUser.getId() );
	}
}
