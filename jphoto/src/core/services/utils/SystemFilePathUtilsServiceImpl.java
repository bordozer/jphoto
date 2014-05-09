package core.services.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;

public class SystemFilePathUtilsServiceImpl implements SystemFilePathUtilsService {

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public Resource getTempDir() {
		return new FileSystemResource( systemVarsService.getSystemTempFolder() );
	}

	@Override
	public File getSystemPhotoDir() {
		return new File( systemVarsService.getPhotoStoragePath() );
	}

	public void initFileSystemOnWorkerStartup() {

		createSystemPhotoStorageFolderIfNeed();

		deleteSystemTempDir();

		createSystemTempFolder();
	}

	private void deleteSystemTempDir() {
		final File photoStorageDir = new File( systemVarsService.getSystemTempFolder() );
		FileUtils.deleteQuietly( photoStorageDir );
	}

	private File createSystemPhotoStorageFolderIfNeed() {
		final File photoStorageDir = getSystemPhotoDir();
		if ( !photoStorageDir.exists() ) {
			photoStorageDir.mkdir();
		}
		return photoStorageDir;
	}

	private File createSystemTempFolder() {
		final File tempFolder = new File( systemVarsService.getSystemTempFolder() );
		if ( !tempFolder.exists() ) {
			tempFolder.mkdir();
		}
		return tempFolder;
	}

	public void setSystemVarsService( final SystemVarsService systemVarsService ) {
		this.systemVarsService = systemVarsService;
	}
}
