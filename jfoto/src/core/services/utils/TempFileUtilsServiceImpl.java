package core.services.utils;

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
	public File getTempFileWithOriginalExtension( final File file ) throws IOException {
		final String extension = FilenameUtils.getExtension( file.getName() );
		final String tmpFileName = String.format( "temp_%d.%s", dateUtilsService.getCurrentTime(), extension );

		return new File( getTmpDir(), tmpFileName );
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
