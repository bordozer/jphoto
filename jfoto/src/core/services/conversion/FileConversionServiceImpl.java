package core.services.conversion;

import core.general.conversion.ConversionOptions;
import utils.ShellUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileConversionServiceImpl implements FileConversionService {

	@Override
	public void convert( final File sourceFile, final File destinationFile, final ConversionOptions conversionOptions ) throws IOException, InterruptedException {

		String resize = dimensionToCovertString( conversionOptions.getDimension() );
		final String cmd = String.format( "convert -alpha off -strip +profile iptc -density %s -units PixelsPerInch -resize %s %s %s"
				, conversionOptions.getDensity(), resize, sourceFile.getPath(), destinationFile.getPath() );

		ShellUtils.executeCommand( cmd );
	}

	private static String dimensionToCovertString( Dimension dimension ) {
		return String.format( "%sx%s", dimension.getWidth(), dimension.getHeight() );
	}
}
