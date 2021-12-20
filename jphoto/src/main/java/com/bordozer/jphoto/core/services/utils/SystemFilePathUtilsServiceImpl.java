package com.bordozer.jphoto.core.services.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

@Service("systemFilePathUtilsService")
public class SystemFilePathUtilsServiceImpl implements SystemFilePathUtilsService {

    @Value("${app.systemTempFolder}")
    private String systemTempFolder;
    @Value("${app.photoStoragePath}")
    private String photoStoragePath;

    @Override
    public Resource getTempDir() {
        return new FileSystemResource(systemTempFolder);
    }

    @Override
    public File getSystemPhotoDir() {
        return new File(photoStoragePath);
    }

    public void initFileSystemOnWorkerStartup() {
        createSystemPhotoStorageFolderIfNeed();
        deleteSystemTempDir();
        createSystemTempFolder();
    }

    private void deleteSystemTempDir() {
        final File photoStorageDir = new File(systemTempFolder);
        FileUtils.deleteQuietly(photoStorageDir);
    }

    private File createSystemPhotoStorageFolderIfNeed() {
        final File photoStorageDir = getSystemPhotoDir();
        if (!photoStorageDir.exists()) {
            photoStorageDir.mkdir();
        }
        return photoStorageDir;
    }

    private File createSystemTempFolder() {
        final File tempFolder = new File(systemTempFolder);
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }
        return tempFolder;
    }
}
