package core.services.utils;

import core.services.utils.SystemVarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import core.services.utils.SystemVarsServiceImpl;

import java.io.File;

public class SystemFilePathUtilsServiceImpl implements SystemFilePathUtilsService {

	@Autowired
	private SystemVarsService systemVarsService;

	public void createSystemDirectoriesOnStartup() {
		createSystemPhotoDirIfNeed();
		createSystemTempFolderIfNeed();
	}

	@Override
	public Resource getTempDir() {
		return new FileSystemResource( systemVarsService.getSystemTempFolder() );
	}

	@Override
	public File getSystemPhotoDir() {
		return new File( systemVarsService.getPhotoStoragePath() );
	}

	@Override
	public File createSystemPhotoDirIfNeed() {
		final File photoStorageDir = getSystemPhotoDir();
		if ( !photoStorageDir.exists() ) {
			photoStorageDir.mkdir();
		}
		return photoStorageDir;
	}

	private File createSystemTempFolderIfNeed() {
		final File tempFolder = new File( systemVarsService.getSystemTempFolder() );
		if ( !tempFolder.exists() ) {
			tempFolder.mkdir();
		}
		return tempFolder;
	}
}
