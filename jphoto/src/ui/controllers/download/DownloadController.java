package ui.controllers.download;

import core.general.photo.Photo;
import core.services.photo.PhotoService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping( "/download" )
public class DownloadController {

	private static final String CONTENT_TYPE = "image/jpeg";

	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@RequestMapping( "/photos/{photoId}/" )
	public void downloadUserPhoto( @PathVariable( "photoId" ) int photoId, HttpServletResponse response ) throws IOException {

		final Photo photo = photoService.load( photoId );

		downloadPhoto( photo.getPhotoImageFile(), response );
	}

	@RequestMapping( "/photos/{photoId}/preview/" )
	public void downloadUserPhotoPreview( @PathVariable( "photoId" ) int photoId, HttpServletResponse response ) throws IOException {

		final Photo photo = photoService.load( photoId );

		final File file = userPhotoFilePathUtilsService.getPhotoPreviewFile( photo );

		downloadPhoto( file, response );
	}

	@RequestMapping( "/file/" )
	public void downloadAnyFile( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		final String filePath = request.getParameter( "filePath" );
		final File file = new File( filePath );

		downloadPhoto( file, response );
	}

	private void downloadPhoto( final File beingDownloadedFile, final HttpServletResponse response ) throws IOException {
		File file  = beingDownloadedFile;

		if ( !file.isFile() || !file.exists() ) {
			file = new File( "../../images/imagenotfound.png" );
		}

		response.setContentLength( ( int ) file.length() );
		response.setContentType( CONTENT_TYPE );

		final OutputStream outputStream = response.getOutputStream();

		response.setHeader( "Content-Disposition", contentDisposition( file ) );

		pipe( file, outputStream );
		outputStream.close();
	}

	private static String contentDisposition( final File file ) {
		return String.format( "filename=\"%s\"", file.getName() );
	}

	private static int pipe( File file, OutputStream out ) throws IOException {
		final FileInputStream fis = new FileInputStream( file );
		int total = pipe( fis, out );
		fis.close();
		return total;
	}

	private static int pipe( InputStream inputStream, OutputStream outputStream ) throws IOException {
		final long copied = IOUtils.copyLarge( inputStream, outputStream );
		if ( copied >= 0 ) {
			return 1;
		}
		return -1;
	}
}
