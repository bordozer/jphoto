package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.ImageToImport;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import core.exceptions.BaseRuntimeException;
import core.general.photo.PhotoImageSourceType;
import core.log.LogHelper;
import core.services.remotePhotoSite.RemotePhotoCategoryService;
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

public class RemotePhotoSiteCacheXmlUtils {

	private static final String USER_INFO_FILE_ROOT_ELEMENT = "author";
	private static final String USER_INFO_FILE_USER_ID = "id";
	private static final String USER_INFO_FILE_USER_NAME = "name";

	private static final String USER_INFO_FILE_PHOTO_ELEMENT_NAME = "photo";
	private static final String USER_INFO_FILE_PHOTO_ID = "photoId";

	private static final String USER_INFO_FILE_REMOTE_CATEGORY_ID = "remoteCategoryId";
	private static final String USER_INFO_FILE_REMOTE_CATEGORY_FOLDER_NAME = "remoteCategoryName";
	private static final String USER_INFO_FILE_LOCAL_CATEGORY = "localCategoryName";

	private static final String USER_INFO_FILE_PHOTO_NAME = "name";
	private static final String USER_INFO_FILE_PHOTO_UPLOAD_TIME = "uploadTime";
	private static final String USER_INFO_FILE_PHOTO_IMAGE_URL = "imageUrl";
	private static final String USER_INFO_FILE_PHOTO_IMAGE_FILE = "fileName";
	private static final String USER_INFO_FILE_PHOTO_NUMBER_IN_SERIES = "numberInSeries";

	private static final String USER_INFO_FILE_PHOTO_COMMENT_ELEMENT_NAME = "comments";
	private static final String USER_INFO_FILE_PHOTO_COMMENT_TEXT = "commentText";

	private static final String XML_FILE_PHOTO_UPLOAD_TIME_FORMAT = "EEE MMM d HH:mm:ss Z yyyy";

	private PhotosImportSource photosImportSource;
	private final RemotePhotoCategoryService remotePhotoCategoryService;
	private final File remotePhotoSitesCachePath;
	private final PhotoImageSourceType photoImageSourceType;

	private final static LogHelper log = new LogHelper( RemotePhotoSiteCacheXmlUtils.class );

	public RemotePhotoSiteCacheXmlUtils( final PhotosImportSource photosImportSource, final File remotePhotoSitesCachePath, final RemotePhotoCategoryService remotePhotoCategoryService, final PhotoImageSourceType photoImageSourceType ) {
		this.photosImportSource = photosImportSource;
		this.remotePhotoCategoryService = remotePhotoCategoryService;
		this.remotePhotoSitesCachePath = remotePhotoSitesCachePath;
		this.photoImageSourceType = photoImageSourceType;
	}

	public void initRemoteUserCacheFileStructure( final RemoteUser remoteUser ) throws IOException {
		createUserFolderForPhotoDownloading( remoteUser );
		createUserInfoFile( remoteUser );
	}

	public void createUserInfoFile( final RemoteUser remoteUser ) throws IOException {

		final Document document = DocumentHelper.createDocument();
		final Element rootElement = document.addElement( USER_INFO_FILE_ROOT_ELEMENT );

		rootElement.addElement( USER_INFO_FILE_USER_ID ).addText( String.valueOf( remoteUser.getId() ) );
		rootElement.addElement( USER_INFO_FILE_USER_NAME ).addText( StringEscapeUtils.escapeXml( remoteUser.getName() ) );

		final File userInfoFile = getUserInfoFile( remoteUser );
		if ( userInfoFile.exists() ) {
			return;
		}

		final FileWriter fileWriter = new FileWriter( userInfoFile );
		final OutputFormat format = OutputFormat.createPrettyPrint();
		final XMLWriter output = new XMLWriter( fileWriter, format );
		output.write( document );
		output.close();
	}

	public void recreateUserCacheFile( final RemoteUser remoteUser, final List<RemotePhotoData> remotePhotoDatas, final DateUtilsService dateUtilsService ) throws IOException {

		final Document document = DocumentHelper.createDocument();
		final Element rootElement = document.addElement( USER_INFO_FILE_ROOT_ELEMENT );

		rootElement.addElement( USER_INFO_FILE_USER_ID ).addText( String.valueOf( remoteUser.getId() ) );
		rootElement.addElement( USER_INFO_FILE_USER_NAME ).addText( StringEscapeUtils.escapeXml( remoteUser.getName() ) );

		for ( final RemotePhotoData remotePhotoData : remotePhotoDatas ) {

			if ( remotePhotoData.isHasError() ) {
				continue;
			}

			final Element photoElement = rootElement.addElement( USER_INFO_FILE_PHOTO_ELEMENT_NAME );
			photoElement.addElement( USER_INFO_FILE_PHOTO_ID ).addText( String.valueOf( remotePhotoData.getPhotoId() ) );

			photoElement.addElement( USER_INFO_FILE_REMOTE_CATEGORY_ID ).addText( String.valueOf( remotePhotoData.getRemotePhotoSiteCategory().getId() ) );
			photoElement.addElement( USER_INFO_FILE_REMOTE_CATEGORY_FOLDER_NAME ).addText( remotePhotoData.getRemotePhotoSiteCategory().getKey() );
			photoElement.addElement( USER_INFO_FILE_LOCAL_CATEGORY ).addText( remotePhotoCategoryService.getMappedGenreOrNull( remotePhotoData.getRemotePhotoSiteCategory() ).getName() );

			photoElement.addElement( USER_INFO_FILE_PHOTO_NAME ).addText( StringEscapeUtils.escapeXml( remotePhotoData.getName() ) );
			photoElement.addElement( USER_INFO_FILE_PHOTO_UPLOAD_TIME ).addText( dateUtilsService.formatDateTime( remotePhotoData.getUploadTime(), XML_FILE_PHOTO_UPLOAD_TIME_FORMAT ) );
			photoElement.addElement( USER_INFO_FILE_PHOTO_IMAGE_URL ).addText( remotePhotoData.getRemotePhotoSiteImage().getImageUrl() );
			photoElement.addElement( USER_INFO_FILE_PHOTO_IMAGE_FILE ).addText( getRemotePhotoCacheFileName( remotePhotoData ) );
			photoElement.addElement( USER_INFO_FILE_PHOTO_NUMBER_IN_SERIES ).addText( String.valueOf( remotePhotoData.getNumberInSeries() ) );

			final List<String> comments = remotePhotoData.getComments();
			if ( comments != null ) {
				final Element commentElement = photoElement.addElement( USER_INFO_FILE_PHOTO_COMMENT_ELEMENT_NAME );
				for ( final String comment : comments ) {
					commentElement.addElement( USER_INFO_FILE_PHOTO_COMMENT_TEXT ).addText( StringEscapeUtils.escapeXml( comment ) );
				}
			}
		}

		final FileWriter fileWriter = new FileWriter( getUserInfoFile( remoteUser ) );
		final OutputFormat format = OutputFormat.createPrettyPrint();
		final XMLWriter output = new XMLWriter( fileWriter, format );
		output.write( document );
		output.close();
	}

	public List<RemotePhotoData> getPhotosFromRemoteSiteUserInfoFile( final PhotosImportSource importSource, final RemoteUser remoteUser, final Services services, final Language language ) throws IOException, DocumentException {
		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final TranslatorService translatorService = services.getTranslatorService();

		final SAXReader reader = new SAXReader( false );
		final File userInfoFile = getUserInfoFile( remoteUser );
		final Document document = reader.read( userInfoFile );

		final Iterator photosIterator = document.getRootElement().elementIterator( USER_INFO_FILE_PHOTO_ELEMENT_NAME );
		final List<RemotePhotoData> result = newArrayList();
		while ( photosIterator.hasNext() ) {
			final Element photoElement = ( Element ) photosIterator.next();
			final int remoteUserPhotoId = NumberUtils.convertToInt( photoElement.element( USER_INFO_FILE_PHOTO_ID ).getText() );

			final String savedCategoryId = photoElement.element( USER_INFO_FILE_REMOTE_CATEGORY_ID ).getText();
			final RemotePhotoSiteCategory category = RemotePhotoSiteCategory.getById( importSource, NumberUtils.convertToInt( savedCategoryId ) );
			if ( category == null ) {
				final String message = translatorService.translate( "File '$1' contains unknown remote photo site categoryId '$2'. Note: file deletion may solve the problem because the import will be done again", language
					, userInfoFile.getAbsolutePath()
					, savedCategoryId
				);
				throw new BaseRuntimeException( message );
			}

			final String photoName = StringEscapeUtils.unescapeXml( photoElement.element( USER_INFO_FILE_PHOTO_NAME ).getText() );

			final String textUploadTime = photoElement.element( USER_INFO_FILE_PHOTO_UPLOAD_TIME ).getText();
			final Date uploadTime;
			if ( NumberUtils.isNumeric( textUploadTime ) ) {
				uploadTime = new Date( Long.parseLong( textUploadTime ) ); // for backward compatibility
			} else {
				uploadTime = dateUtilsService.parseDateTimeWithFormat( textUploadTime, XML_FILE_PHOTO_UPLOAD_TIME_FORMAT );
			}
			final String imageUrl = photoElement.element( USER_INFO_FILE_PHOTO_IMAGE_URL ).getText();
			final int numberInSeries = Integer.parseInt( photoElement.element( USER_INFO_FILE_PHOTO_NUMBER_IN_SERIES ).getText() );

			final RemotePhotoData remotePhotoData = new RemotePhotoData( remoteUser, remoteUserPhotoId, category, imageUrl );
			remotePhotoData.setName( photoName );
			remotePhotoData.setUploadTime( uploadTime );
			remotePhotoData.setRemotePhotoSiteImage( new RemotePhotoSiteImage( imageUrl ) );
			remotePhotoData.setNumberInSeries( numberInSeries );

			final List<String> comments = newArrayList();
			final Element commentsElement = photoElement.element( USER_INFO_FILE_PHOTO_COMMENT_ELEMENT_NAME );

			if ( commentsElement != null ) {
				final Iterator commentsIterator = commentsElement.elementIterator( USER_INFO_FILE_PHOTO_COMMENT_TEXT );
				while ( commentsIterator.hasNext() ) {
					final Element commentElement = ( Element ) commentsIterator.next();
					final String commentText = commentElement.getText();
					final String comment = StringEscapeUtils.unescapeXml( commentText );

					comments.add( comment );
				}
			}
			remotePhotoData.setComments( comments );

			remotePhotoData.setCached( true );

			result.add( remotePhotoData );
		}


		return result;
	}

	public void prepareUserGenreFolders( final RemoteUser remoteUser, final List<RemotePhotoData> remotePhotoDatas ) throws IOException {

		final File userFolder = getRemoteUserCacheFolder( remoteUser );

		for ( final RemotePhotoData remotePhotoData : remotePhotoDatas ) {
//			final Genre genre = remotePhotoCategoryService.getMappedGenreOrOther( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );

			final File userGenrePath = new File( userFolder, remotePhotoData.getRemotePhotoSiteCategory().getKey() );
			if ( ! userGenrePath.exists() ) {
				userGenrePath.mkdirs();
			}
		}
	}

	public ImageToImport placeRemoteImageToCache( final RemotePhotoData remotePhotoData, final String imageContent ) throws IOException {

		final RemotePhotoSiteCategory remotePhotoSiteCategory = remotePhotoData.getRemotePhotoSiteCategory();

		final File remotePhotoCacheFile = getRemotePhotoCacheFile( remotePhotoData );

		writeImageContentToFile( remotePhotoCacheFile, imageContent, "ISO-8859-1" );

		final ImageToImport imageToImport = new ImageToImport( photosImportSource, photoImageSourceType, remotePhotoSiteCategory.getKey(), remotePhotoCacheFile, remotePhotoData.getImageUrl() );

		log.debug( String.format( "Photo %s has been saved on disc: %s", remotePhotoData, imageToImport.getImageFile().getCanonicalPath() ) );

		return imageToImport;
	}

	public File getRemotePhotoCacheFile( final RemotePhotoData remotePhotoData ) throws IOException {
		final File folderForRemoteUserCachedPhotos = getRemoteUserCacheFolder( remotePhotoData.getRemoteUser() );
		final File folderForRemoteUserCachedPhotosForGenre = new File( folderForRemoteUserCachedPhotos, remotePhotoData.getRemotePhotoSiteCategory().getKey() );

		return new File( folderForRemoteUserCachedPhotosForGenre, getRemotePhotoCacheFileName( remotePhotoData ) );
	}

	private String getRemotePhotoCacheFileName( final RemotePhotoData remotePhotoData ) {
		return String.format( "%d_%d.jpg", remotePhotoData.getPhotoId(), remotePhotoData.getNumberInSeries() );
	}

	public File getPhotoStorage() {

		if ( ! remotePhotoSitesCachePath.exists() ) {
			remotePhotoSitesCachePath.mkdir();
		}

		final File remotePhotoSiteCacheFolder = new File( String.format( "%s/%s", remotePhotoSitesCachePath.getPath(), photosImportSource.getLocalStorageFolder() ) );

		if ( ! remotePhotoSiteCacheFolder.exists() ) {
			remotePhotoSiteCacheFolder.mkdir();
		}

		return remotePhotoSiteCacheFolder;
	}

	public File getUserInfoFile( final RemoteUser remoteUser ) throws IOException {
		return new File( getRemoteUserCacheFolder( remoteUser ), getUserInfoFileName( remoteUser ) );
	}

	private static String escapeFileName( final String param ) {
		final String currentEncoding = "UTF-8";
		try {
			return new String( Charset.forName( currentEncoding ).encode( param ).array(), currentEncoding ).trim().replaceAll( "[\\\\/*?|!-&:-@']", "_" );
		} catch ( UnsupportedEncodingException e ) {
			throw new BaseRuntimeException( e );
		}
	}

	private  static void writeImageContentToFile( final File imageFile, final String imageContent, final String charsetName ) throws IOException {
		final BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( imageFile ) );
		bos.write( imageContent.getBytes( charsetName ) );
		bos.flush();
		bos.close();
	}

	private static String getUserInfoFileName( final RemoteUser remoteUser ) {
		return String.format( "%s.xml", remoteUser.getId() );
	}

	private  File getRemoteUserCacheFolder( final RemoteUser remoteUser ) throws IOException {
		return new File( getPhotoStorage().getPath(), String.format( "%s", remoteUser.getId() ) );
	}

	private  void createUserFolderForPhotoDownloading( final RemoteUser remoteUser ) throws IOException {
		final File userFolder = getRemoteUserCacheFolder( remoteUser );
		if ( ! userFolder.exists() ) {
			userFolder.mkdirs();
		}
	}

	public PhotoImageSourceType getPhotoImageSourceType() {
		return photoImageSourceType;
	}
}
