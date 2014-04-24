package core.services.conversion;

import core.general.conversion.ConversionOptions;

import java.io.IOException;

public interface PreviewGenerationService {

	boolean generatePreviewSync( final int photoId ) throws IOException, InterruptedException;

	boolean generatePreviewSync( final int photoId, final ConversionOptions options ) throws IOException, InterruptedException;
}
