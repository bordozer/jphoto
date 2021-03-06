package com.bordozer.jphoto.core.services.conversion;

import com.bordozer.jphoto.core.general.conversion.ConversionOptions;
import com.bordozer.jphoto.core.general.user.User;

import java.io.File;
import java.io.IOException;

public interface PreviewGenerationService {

    File generatePreviewSync(final int photoId) throws IOException, InterruptedException;

    File generatePreviewSync(final int photoId, final ConversionOptions conversionOptions) throws IOException, InterruptedException;

    File generatePreviewSync(final User photoAuthor, final File photoFile) throws IOException, InterruptedException;

    File generatePreviewSync(final File photoImageFile, final File photoPreviewFile, final ConversionOptions conversionOptions) throws IOException, InterruptedException;
}
