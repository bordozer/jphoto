package com.bordozer.jphoto.core.services.utils;

import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service("userPhotoFilePathUtilsService")
public class UserPhotoFilePathUtilsServiceImpl implements UserPhotoFilePathUtilsService {

    private static final String PREVIEW_FOLDER_NAME = "preview";

    @Autowired
    private SystemFilePathUtilsService systemFilePathUtilsService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private ImageFileUtilsService imageFileUtilsService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public File getUserPhotoDir(final int userId) {
        return new File(getUserBasePhotoStoragePath(userId), String.valueOf(userId));
    }

    @Override
    public String getPhotoPreviewUrl(final Photo photo) {
        return String.format("%s/download/photos/%s/preview/", urlUtilsService.getBaseURL(), photo.getId());
    }

    @Override
    public void createUserPhotoPreviewDirIfNeed(final int userId) {
        final File userPhotoPreviewDir = getUserPhotoPreviewDir(userId);
        if (!userPhotoPreviewDir.exists()) {
            userPhotoPreviewDir.mkdirs();
        }
    }

    @Override
    public File getPhotoPreviewFile(final Photo photo) {
        return new File(getUserPhotoPreviewDir(photo.getUserId()).getPath(), photo.getPhotoPreviewName());
    }

    @Override
    public String getPhotoImageUrl(final Photo photo) {
        switch (photo.getPhotoImageLocationType()) {
            case FILE:
                return getPhotoUrl(photo);
            default:
                return String.format("http://%s", photo.getPhotoImageUrl());
        }
    }

    @Override
    public File generatePhotoPreviewName(final int userId) {
        // Invoke from synchronised context only

        final File userPhotoPreviewDir = getUserPhotoPreviewDir(userId);

        class PreviewNameGenerator {

            private File generate(final int number) {
                return new File(userPhotoPreviewDir.getPath(), String.format("%d_%05d.jpg", userId, number));
            }

            private boolean doesPreviewWithNumberExists(final int number) {
                return generate(number).exists();
            }
        }

        final PreviewNameGenerator generator = new PreviewNameGenerator();

        final File[] existingPreviews = userPhotoPreviewDir.listFiles();

        if (existingPreviews == null) {
            return generator.generate(1);
        }

        int number = existingPreviews.length + 1;

        do {

            if (!generator.doesPreviewWithNumberExists(number)) {
                return generator.generate(number);
            }

            number++;

        } while (true);
    }

    @Override
    public void createUserPhotoDirIfNeed(final int userId) {

        final File userPhotoDir = new File(getUserBasePhotoStoragePath(userId), String.valueOf(userId));
        if (!userPhotoDir.exists()) {
            userPhotoDir.mkdir();
        }
    }

    @Override
    public void deletePhotoFileWithPreview(final Photo photo) {

        final File previewFile = new File(getUserPhotoPreviewDir(photo.getUserId()).getPath(), photo.getPhotoPreviewName());

        if (previewFile.exists() && previewFile.isFile()) {
            FileUtils.deleteQuietly(previewFile);
        }

        final File file = photo.getPhotoImageFile();
        if (file != null && file.exists() && file.isFile()) {
            FileUtils.deleteQuietly(file);
        }
    }

    @Override
    public String getUserAvatarFileName(final int userId) {
        return String.format("_avatar_%s.jpg", userId);
    }

    @Override
    public File getUserAvatarDir(final int userId) {
        return getUserPhotoDir(userId);
    }

    @Override
    public File getUserAvatarFile(final int userId) {
        return new File(getUserPhotoDir(userId).getPath(), getUserAvatarFileName(userId));
    }

    @Override
    public String getUserAvatarFileUrl(final int userId) {
        return String.format("%s/download/file/?filePath=%s", urlUtilsService.getBaseURL(), getUserAvatarFile(userId));
    }

    @Override
    public boolean isUserHasAvatar(final int userId) {
        return getUserAvatarFile(userId).exists();
    }

    @Override
    public String getUserAvatarImage(final int userId, final int width, final int height, final String imageId, final String onClick, final String cssStyle) throws IOException {
        final File userAvatarFile = getUserAvatarFile(userId);
        final String avatarImageUrl = getUserAvatarFileUrl(userId);

        final Dimension toDimension = new Dimension(width, height);
        final Dimension dimension = imageFileUtilsService.resizeImageToDimensionAndReturnResultDimension(userAvatarFile, toDimension);

        final String onclick = StringUtils.isEmpty(onClick) ? "" : String.format("onClick='%s'", onClick);
        final String css = StringUtils.isEmpty(cssStyle) ? "" : String.format("style='%s'", cssStyle);

        final String imageId1 = StringUtils.isEmpty(imageId) ? "" : String.format("id='%s'", imageId);
        return String.format("<img %s src='%s' width='%d' height='%d' %s %s/>", imageId1, avatarImageUrl, dimension.getWidth(), dimension.getHeight(), onclick, css);
    }

    @Override
    public File copyPhotoImageFileToUserFolder(final User user, final File photoImageFile) throws IOException {

        if (!isUserPhotoDirExist(user.getId())) {
            createUserPhotoDirIfNeed(user.getId());
        }

        final File userPhotoImageFile = getUniqueUserPhotoFile(user);

        try {
            FileUtils.copyFile(photoImageFile, userPhotoImageFile);
        } catch (final IOException e) {
            new LogHelper().error(String.format("Can not copy photo file: '%s' to '%s'", photoImageFile.getPath(), userPhotoImageFile.getPath()), e);
            throw e;
        }

        return userPhotoImageFile;
    }

    private String getUserBasePhotoStoragePath(int userId) {
        return String.format("%s/%s/%s"
                , systemFilePathUtilsService.getSystemPhotoDir().getPath()
                , String.valueOf(userId).substring(0, 1)
                , String.valueOf(userId).length() > 3 ? String.valueOf(userId).substring(0, 3) : String.format("%03d", userId)
        );
    }

    private String generateUserPhotoFileName(final User user) {
        return String.format("u%s_%s.jpg", user.getId(), dateUtilsService.getCurrentTime().getTime());
    }

    private File getUniqueUserPhotoFile(final User user) {
        final String photoFileName = generateUserPhotoFileName(user);
        return new File(getUserPhotoDir(user.getId()), photoFileName);
    }

    private File getUserPhotoPreviewDir(final int userId) {
        return new File(getUserPhotoDir(userId).getPath(), PREVIEW_FOLDER_NAME);
    }

    private String getUserPhotoPathPrefix(final int userId) {
        return String.valueOf(userId);
    }

    private boolean isUserPhotoDirExist(final int userId) {
        return getUserPhotoDir(userId).exists();
    }

    private String getPhotoUrl(final Photo photo) {
        return String.format("%s/download/photos/%s/", urlUtilsService.getBaseURL(), photo.getId());
    }

    public void setSystemFilePathUtilsService(final SystemFilePathUtilsService systemFilePathUtilsService) {
        this.systemFilePathUtilsService = systemFilePathUtilsService;
    }

    public void setUrlUtilsService(final UrlUtilsService urlUtilsService) {
        this.urlUtilsService = urlUtilsService;
    }

    public void setImageFileUtilsService(final ImageFileUtilsService imageFileUtilsService) {
        this.imageFileUtilsService = imageFileUtilsService;
    }

    public void setDateUtilsService(final DateUtilsService dateUtilsService) {
        this.dateUtilsService = dateUtilsService;
    }
}
