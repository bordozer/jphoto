package com.bordozer.jphoto.core.services.utils;

import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.utils.FormatUtils;
import com.bordozer.jphoto.utils.NumberUtils;
import com.bordozer.jphoto.utils.PhotoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@Service("imageFileUtilsService")
public class ImageFileUtilsServiceImpl implements ImageFileUtilsService {

    private final LogHelper log = new LogHelper();

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private TempFileUtilsService tempFileUtilsService;

    @Override
    public String getContentType(final File file) {
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    @Override
    public float getFileSizeInKb(final float fileLength) {
        return NumberUtils.round(fileLength / 1024, 1);
    }

    @Override
    public Dimension getImageDimension(final File file) throws IOException {

        if (!file.exists()) {
            throw new BaseRuntimeException("Photo image file does not exist!");
        }

        final BufferedImage img = ImageIO.read(file);

        if (img == null) {
            return new Dimension(200, 200);
        }

        int width = img.getWidth();
        int height = img.getHeight();

        return new Dimension(width, height);
    }

    @Override
    public Dimension resizePhotoImage(final Dimension dimension, final Dimension toDimension) {
        final int origWidth = dimension.getWidth();
        final int origHeight = dimension.getHeight();

        final int maxImageWidth = toDimension.getWidth();
        final int maxImageHeight = toDimension.getHeight();

        float resultWidth = origWidth;
        float resultHeight = origHeight;

        if (origWidth > maxImageWidth) {

            float widthCoeff = (float) origWidth / maxImageWidth;
            resultWidth = maxImageWidth;
            resultHeight = origHeight / widthCoeff;

            if (resultHeight > maxImageHeight) {
                float heightCoeff = resultHeight / maxImageHeight;
                resultHeight = maxImageHeight;
                resultWidth = resultWidth / heightCoeff;
            }
        } else if (origHeight > maxImageHeight) {
            resultHeight = maxImageHeight;
            resultWidth = resultWidth / ((float) origHeight / maxImageHeight);
        }

        return new Dimension((int) resultWidth, (int) resultHeight);
    }

    @Override
    public Dimension resizePhotoImage(final Dimension dimension) {
        final int maxImageWidth = configurationService.getInt(ConfigurationKey.PHOTO_CARD_MAX_WIDTH);
        final int maxImageHeight = configurationService.getInt(ConfigurationKey.PHOTO_CARD_MAX_HEIGHT);

        final Dimension toDimension = new Dimension(maxImageWidth, maxImageHeight);
        return resizePhotoImage(dimension, toDimension);
    }

    @Override
    public Dimension resizePhotoFileToDimensionAndReturnResultDimension(final File photoFile) throws IOException {
        final Dimension originalDimension = getImageDimension(photoFile);

        return resizePhotoImage(originalDimension);
    }

    @Override
    public Dimension resizeImageToDimensionAndReturnResultDimension(final File imageFile, final Dimension toDimension) throws IOException {
        final Dimension originalDimension = getImageDimension(imageFile);
        return resizePhotoImage(originalDimension, toDimension);
    }

    @Override
    public void validateUploadedFile(final Errors errors, final MultipartFile multipartFile, final long maxFileSizeKb, final Dimension maxDimension, final Dimension minDimension, final String fileControlName, final Language language) {

        final String fileName = multipartFile.getOriginalFilename();

        if (StringUtils.isEmpty(fileName)) {
            errors.rejectValue(fileControlName, translatorService.translate("Select $1", language, FormatUtils.getFormattedFieldName("File")));
            return;
        }

        if (isFileToBig(multipartFile, maxFileSizeKb)) {
            final long actualFileSizeKiloBytes = multipartFile.getSize() / 1024;
            errors.rejectValue(fileControlName, translatorService.translate("$1 size should be less then $1 Kilobytes.<br />Attempt to upload $2 Kilobytes", language, FormatUtils.getFormattedFieldName("File"), String.valueOf(maxFileSizeKb), String.valueOf(actualFileSizeKiloBytes)));
            return;
        }

        final String contentType = multipartFile.getContentType();
        final List<String> allowedExtensions = configurationService.getListString(ConfigurationKey.PHOTO_UPLOAD_FILE_ALLOWED_EXTENSIONS);
        if (!PhotoUtils.isPhotoContentTypeSupported(allowedExtensions, contentType)) {
            errors.rejectValue(fileControlName, translatorService.translate("Unsupported $1 type is uploaded - $2."
                    , language, FormatUtils.getFormattedFieldName("File")), contentType);
            return;
        }

        try {
            checkFileDimension(multipartFile, maxDimension, minDimension, errors, fileControlName, language);
        } catch (IOException e) {
            errors.rejectValue(fileControlName, translatorService.translate("Can not upload file", language));
        }
    }

    private void checkFileDimension(final MultipartFile multipartFile, final Dimension maxDimension, final Dimension minDimension, final Errors errors, final String fileControlName, final Language language) throws IOException {

        final String originalFilename = multipartFile.getOriginalFilename();
        final File tempPhotoFile = tempFileUtilsService.getTempFileWithOriginalExtension(originalFilename);

        if (!tempPhotoFile.createNewFile()) {
            throw new IOException(String.format("Can not create file '%s'", originalFilename));
        }

        final FileOutputStream fos = new FileOutputStream(tempPhotoFile);
        fos.write(multipartFile.getBytes());
        fos.close();

        final Dimension dimension = getImageDimension(tempPhotoFile);

        final String formattedFieldName = FormatUtils.getFormattedFieldName(translatorService.translate("File field name: File", language));
        if (dimension.getWidth() > maxDimension.getWidth() || dimension.getHeight() > maxDimension.getHeight()) {
            final String mess = translatorService.translate("Max $1 dimension is $2 x $3, but uploaded file is $4 x $5"
                    , language, formattedFieldName, String.valueOf(maxDimension.getWidth()), String.valueOf(maxDimension.getHeight()), String.valueOf(dimension.getWidth()), String.valueOf(dimension.getHeight()));
            errors.rejectValue(fileControlName, mess);
        }

        if (dimension.getWidth() < minDimension.getWidth() || dimension.getHeight() < minDimension.getHeight()) {
            final String mess = translatorService.translate("Min $1 dimension is $2 x $3, but uploaded file is $4 x $5"
                    , language, formattedFieldName, String.valueOf(minDimension.getWidth()), String.valueOf(minDimension.getHeight()), String.valueOf(dimension.getWidth()), String.valueOf(dimension.getHeight()));
            errors.rejectValue(fileControlName, mess);
        }
    }

    private boolean isFileToBig(final MultipartFile multipartFile, final long maxFileSizeKb) {
        return getFileSizeInKb(multipartFile.getSize()) > maxFileSizeKb;
    }

    public void setTranslatorService(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public void setTempFileUtilsService(final TempFileUtilsService tempFileUtilsService) {
        this.tempFileUtilsService = tempFileUtilsService;
    }

    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
