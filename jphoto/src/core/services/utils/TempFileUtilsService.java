package core.services.utils;

import java.io.File;
import java.io.IOException;

public interface TempFileUtilsService {

	String getTmpDir() throws IOException;

	File getTempFileWithOriginalExtension( File file ) throws IOException;

	File getTempFile() throws IOException;

	String getTempFileName() throws IOException;
}
