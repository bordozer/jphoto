package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.RemoteImageLocalEntry;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import core.exceptions.BaseRuntimeException;
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

	private static final String USER_INFO_FILE_REMOTE_CATEGORY_ID = "categoryId";
	private static final String USER_INFO_FILE_REMOTE_CATEGORY_FOLDER_NAME = "categoryName";
	private static final String USER_INFO_FILE_LOCAL_CATEGORY = "localCategoryName";

	private static final String USER_INFO_FILE_PHOTO_NAME = "name";
	private static final String USER_INFO_FILE_PHOTO_UPLOAD_TIME = "uploadTime";
	private static final String USER_INFO_FILE_PHOTO_IMAGE_URL = "imageUrl";

	private static final String USER_INFO_FILE_PHOTO_COMMENT_ELEMENT_NAME = "comments";
	private static final String USER_INFO_FILE_PHOTO_COMMENT_TEXT = "commentText";

	private static final String XML_FILE_PHOTO_UPLOAD_TIME_FORMAT = "EEE MMM d HH:mm:ss Z yyyy";

	private PhotosImportSource photosImportSource;
	private final RemotePhotoCategoryService remotePhotoCategoryService;
	private final File remotePhotoSitesCachePath;

	private final static LogHelper log = new LogHelper( RemotePhotoSiteCacheXmlUtils.class );

	public RemotePhotoSiteCacheXmlUtils( final PhotosImportSource photosImportSource, final File remotePhotoSitesCachePath, final RemotePhotoCategoryService remotePhotoCategoryService ) {
		this.photosImportSource = photosImportSource;
		this.remotePhotoCategoryService = remotePhotoCategoryService;
		this.remotePhotoSitesCachePath = remotePhotoSitesCachePath;
	}

	public void initRemoteUserFileStructure( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		createUserFolderForPhotoDownloading( remotePhotoSiteUser );
		createUserInfoFile( remotePhotoSiteUser );
	}

	public void createUserInfoFile( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {

		final Document document = DocumentHelper.createDocument();
		final Element rootElement = document.addElement( USER_INFO_FILE_ROOT_ELEMENT );

		rootElement.addElement( USER_INFO_FILE_USER_ID ).addText( String.valueOf( remotePhotoSiteUser.getId() ) );
		rootElement.addElement( USER_INFO_FILE_USER_NAME ).addText( StringEscapeUtils.escapeXml( remotePhotoSiteUser.getName() ) );

		final File userInfoFile = getUserInfoFile( remotePhotoSiteUser );
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

	public void cacheLocallyPhotos( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> remotePhotoSitePhotos, final DateUtilsService dateUtilsService ) throws IOException {

		final Document document = DocumentHelper.createDocument();
		final Element rootElement = document.addElement( USER_INFO_FILE_ROOT_ELEMENT );

		rootElement.addElement( USER_INFO_FILE_USER_ID ).addText( String.valueOf( remotePhotoSiteUser.getId() ) );
		rootElement.addElement( USER_INFO_FILE_USER_NAME ).addText( StringEscapeUtils.escapeXml( remotePhotoSiteUser.getName() ) );

		for ( final RemotePhotoSitePhoto remotePhotoSitePhoto : remotePhotoSitePhotos ) {

			if ( remotePhotoSitePhoto.isHasError() ) {
				continue;
			}

			final Element photoElement = rootElement.addElement( USER_INFO_FILE_PHOTO_ELEMENT_NAME );
			photoElement.addElement( USER_INFO_FILE_PHOTO_ID ).addText( String.valueOf( remotePhotoSitePhoto.getPhotoId() ) );

			photoElement.addElement( USER_INFO_FILE_REMOTE_CATEGORY_ID ).addText( String.valueOf( remotePhotoSitePhoto.getRemotePhotoSiteCategory().getId() ) );
			photoElement.addElement( USER_INFO_FILE_REMOTE_CATEGORY_FOLDER_NAME ).addText( remotePhotoSitePhoto.getRemotePhotoSiteCategory().getFolder() );
			photoElement.addElement( USER_INFO_FILE_LOCAL_CATEGORY ).addText( remotePhotoCategoryService.getGenreDiscEntryOrOther( remotePhotoSitePhoto.getRemotePhotoSiteCategory() ).getName() );

			photoElement.addElement( USER_INFO_FILE_PHOTO_NAME ).addText( StringEscapeUtils.escapeXml( remotePhotoSitePhoto.getName() ) );
			photoElement.addElement( USER_INFO_FILE_PHOTO_UPLOAD_TIME ).addText( dateUtilsService.formatDateTime( remotePhotoSitePhoto.getUploadTime(), XML_FILE_PHOTO_UPLOAD_TIME_FORMAT ) );
			photoElement.addElement( USER_INFO_FILE_PHOTO_IMAGE_URL ).addText( remotePhotoSitePhoto.getRemotePhotoSiteImage().getImageUrl() );

			final List<String> comments = remotePhotoSitePhoto.getComments();
			if ( comments != null ) {
				final Element commentElement = photoElement.addElement( USER_INFO_FILE_PHOTO_COMMENT_ELEMENT_NAME );
				for ( final String comment : comments ) {
					commentElement.addElement( USER_INFO_FILE_PHOTO_COMMENT_TEXT ).addText( StringEscapeUtils.escapeXml( comment ) );
				}
			}
		}

		final FileWriter fileWriter = new FileWriter( getUserInfoFile( remotePhotoSiteUser ) );
		final OutputFormat format = OutputFormat.createPrettyPrint();
		final XMLWriter output = new XMLWriter( fileWriter, format );
		output.write( document );
		output.close();
	}

	public List<RemotePhotoSitePhoto> getPhotosFromRemoteSiteUserInfoFile( final PhotosImportSource importSource, final RemotePhotoSiteUser remotePhotoSiteUser, final Services services, final Language language ) throws IOException, DocumentException {
		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final TranslatorService translatorService = services.getTranslatorService();

		final SAXReader reader = new SAXReader( false );
		final File userInfoFile = getUserInfoFile( remotePhotoSiteUser );
		final Document document = reader.read( userInfoFile );

		final Iterator photosIterator = document.getRootElement().elementIterator( USER_INFO_FILE_PHOTO_ELEMENT_NAME );
		final List<RemotePhotoSitePhoto> result = newArrayList();
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

			final RemotePhotoSitePhoto remotePhotoSitePhoto = new RemotePhotoSitePhoto( remotePhotoSiteUser, remoteUserPhotoId, category );
			remotePhotoSitePhoto.setName( photoName );
			remotePhotoSitePhoto.setUploadTime( uploadTime );
			remotePhotoSitePhoto.setRemotePhotoSiteImage( new RemotePhotoSiteImage( imageUrl ) );

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
			remotePhotoSitePhoto.setComments( comments );

			remotePhotoSitePhoto.setCached( true );

			result.add( remotePhotoSitePhoto );
		}


		return result;
	}

	public void prepareUserGenreFolders( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> remotePhotoSitePhotos ) throws IOException {

		final File userFolder = getUserFolderForPhotoDownloading( remotePhotoSiteUser );

		for ( final RemotePhotoSitePhoto remotePhotoSitePhoto : remotePhotoSitePhotos ) {
			final GenreDiscEntry genreDiscEntry = remotePhotoCategoryService.getGenreDiscEntryOrOther( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );

			final File userGenrePath = new File( userFolder, genreDiscEntry.getName() );
			if ( ! userGenrePath.exists() ) {
				userGenrePath.mkdirs();
			}
		}
	}

	public RemoteImageLocalEntry createRemotePhotoSiteDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final String imageContent ) throws IOException {

		final RemoteImageLocalEntry remoteImageLocalEntry = writeImageContentOnDiskAndReturnDiskEntry( remotePhotoSitePhoto, imageContent );

		log.debug( String.format( "Photo %s has been saved on disc: %s", remotePhotoSitePhoto, remoteImageLocalEntry.getImageFile().getCanonicalPath() ) );

		return remoteImageLocalEntry;
	}

	public File getRemoteSitePhotoLocalImageFile( final RemotePhotoSitePhoto remotePhotoSitePhoto ) throws IOException {
		final String imageFileName = getRemoteSitePhotoFileName( remotePhotoSitePhoto );

		final GenreDiscEntry genreDiscEntry = remotePhotoCategoryService.getGenreDiscEntryOrOther( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );
		final File userFolderForPhotoDownloading = getUserFolderForPhotoDownloading( remotePhotoSitePhoto.getRemotePhotoSiteUser() );
		final File imageFolder = new File( userFolderForPhotoDownloading, genreDiscEntry.getName() );
		return new File( imageFolder, imageFileName );
	}

	private RemoteImageLocalEntry writeImageContentOnDiskAndReturnDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final String imageContent ) throws IOException {
		final RemotePhotoSiteCategory remotePhotoSiteCategory = remotePhotoSitePhoto.getRemotePhotoSiteCategory();

		final GenreDiscEntry genreDiscEntry = remotePhotoCategoryService.getGenreDiscEntryOrOther( remotePhotoSiteCategory );

		final File imageFile = getRemoteSitePhotoLocalImageFile( remotePhotoSitePhoto );

		writeImageContentToFile( imageFile, imageContent, "ISO-8859-1" );

		return new RemoteImageLocalEntry( imageFile, genreDiscEntry );
	}

	public File getUserFolderForPhotoDownloading( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		final String userFolderName = String.format( "%s", remotePhotoSiteUser.getId() );
		return new File( getPhotoStorage().getPath(), userFolderName );
	}

	public void createUserFolderForPhotoDownloading( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		final File userFolder = getUserFolderForPhotoDownloading( remotePhotoSiteUser );
		if ( ! userFolder.exists() ) {
			userFolder.mkdirs();
		}
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

	public File getUserInfoFile( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		return new File( getUserFolderForPhotoDownloading( remotePhotoSiteUser ), getUserInfoFileName( remotePhotoSiteUser ) );
	}

	private  static String getRemoteSitePhotoFileName( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
		return String.format( "%d_%d.jpg", remotePhotoSitePhoto.getPhotoId(), remotePhotoSitePhoto.getNumberInSeries() );
	}

	private static String escapeFileName( final String param ) {
		final String currentEncoding = "UTF-8";
		try {
			return new String( Charset.forName( currentEncoding ).encode( param ).array(), currentEncoding ).trim().replaceAll( "[\\\\/*?|!-&:-@']", "_" );
		} catch ( UnsupportedEncodingException e ) {
			throw new BaseRuntimeException( e );
		}
	}

	private static String getUserInfoFileName( final RemotePhotoSiteUser remotePhotoSiteUser ) {
		return String.format( "%s.xml", remotePhotoSiteUser.getId() );
	}
}
