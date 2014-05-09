package core.services.utils;

import org.springframework.core.io.Resource;

import java.io.File;

public interface SystemFilePathUtilsService {

	Resource getTempDir();

	File getSystemPhotoDir();
}
