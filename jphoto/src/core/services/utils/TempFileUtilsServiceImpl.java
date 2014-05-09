package core.services.utils;

import core.general.user.User;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

public class TempFileUtilsServiceImpl implements TempFileUtilsService {

	@Autowired
	private SystemFilePathUtilsService systemFilePathUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public String getTmpDir() throws IOException {
		return systemFilePathUtilsService.getTempDir().getFile().getPath();
	}

	@Override
	public File getTempFileWithOriginalExtension( final User user, final String fileName ) throws IOException {
		final String extension = FilenameUtils.getExtension( fileName );
		final String tmpFileName = String.format( "temp_%d_%d.%s", user.getId(), dateUtilsService.getCurrentTime().getTime(), extension );

		return new File( getTmpDir(), tmpFileName );
	}

	@Override
	public File getTempFileWithOriginalExtension( final String fileName ) throws IOException {
		return getTempFileWithOriginalExtension( new User( 0 ), fileName );
	}

	@Override
	public File getTempFile() throws IOException {
		return new File( getTmpDir(), getTempFileName() );
	}

	@Override
	public String getTempFileName() throws IOException {
		return String.format( "temp_%d", dateUtilsService.getCurrentTime().getTime() );
	}
}
