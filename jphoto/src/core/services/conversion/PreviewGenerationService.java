package core.services.conversion;

import core.general.conversion.ConversionOptions;
import core.general.user.User;

import java.io.File;
import java.io.IOException;

public interface PreviewGenerationService {

	boolean generatePreviewSync( final int photoId ) throws IOException, InterruptedException;

	boolean generatePreviewSync( final int photoId, final ConversionOptions conversionOptions ) throws IOException, InterruptedException;

	boolean generatePreviewSync( final User photoAuthor, final File photoFile ) throws IOException, InterruptedException;

	boolean generatePreviewSync( final File photoImageFile, final File photoPreviewFile, final ConversionOptions conversionOptions ) throws IOException, InterruptedException;
}
