package core.services.conversion;

import core.general.conversion.ConversionOptions;

import java.io.IOException;

public interface PreviewGenerationService {

	void generatePreview( final int photoId ) throws IOException, InterruptedException;

	void generatePreview( final int photoId, final ConversionOptions options ) throws IOException, InterruptedException;
}
