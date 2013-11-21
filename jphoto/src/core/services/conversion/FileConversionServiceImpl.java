package core.services.conversion;

import core.general.conversion.ConversionOptions;
import core.log.LogHelper;
import utils.ShellUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileConversionServiceImpl implements FileConversionService {

	private final LogHelper log  = new LogHelper( FileConversionServiceImpl.class );

	@Override
	public void convert( final File sourceFile, final File destinationFile, final ConversionOptions conversionOptions ) throws IOException, InterruptedException {

		final String resize = dimensionToCovertString( conversionOptions.getDimension() );
		final String cmd = String.format( "convert -alpha off -strip +profile iptc -density %s -units PixelsPerInch -resize %s %s %s"
				, conversionOptions.getDensity(), resize, sourceFile.getPath(), destinationFile.getPath() );

		final String result = ShellUtils.executeCommand( cmd );

		log.debug( result );
	}

	private static String dimensionToCovertString( Dimension dimension ) {
		return String.format( "%sx%s", dimension.getWidth(), dimension.getHeight() );
	}
}
