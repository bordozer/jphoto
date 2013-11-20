package core.general.photo;

import java.io.File;

public class PhotoFile {

	private final File file;
	private final long fileSize;

	public PhotoFile( final File file ) {
		this.file = file;
		fileSize = file.length();
	}

	public File getFile() {
		return file;
	}

	public float getFileSize() {
		return fileSize;
	}
}
