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
	public boolean convertSync( final File sourceFile, final File destinationFile, final ConversionOptions conversionOptions ) throws IOException, InterruptedException {

		final String[] cmd = getCommand( sourceFile, destinationFile, conversionOptions );

		ShellUtils.executeCommandSync( cmd );

		return true;
	}

	@Override
	public void convertAsync( final File sourceFile, final File destinationFile, final ConversionOptions conversionOptions ) throws IOException, InterruptedException {
		final String[] cmd = getCommand( sourceFile, destinationFile, conversionOptions );

		new Thread(){
			@Override
			public void run() {
				final String result;
				try {
					result = ShellUtils.executeCommandSync( cmd );
					log.debug( result );
				} catch ( final Exception e ) {
					log.error( e );
				}
			}
		}.start();
	}

	private String[] getCommand( final File sourceFile, final File destinationFile, final ConversionOptions conversionOptions ) {

		return new String[]{
			"convert"
			, "-alpha"
			, "off"
			, "-strip"
			, "+profile"
			, "iptc"
			, "-density"
			, String.valueOf( conversionOptions.getDensity() )
			, "-units"
			, "PixelsPerInch"
			, "-resize"
			, dimensionToCovertString( conversionOptions.getDimension() )
			, sourceFile.getPath()
			, destinationFile.getPath()
		};

		/*return String.format( "convert -alpha off -strip +profile iptc -density %s -units PixelsPerInch -resize %s '%s' '%s'"
			, conversionOptions.getDensity()
			, dimensionToCovertString( conversionOptions.getDimension() )
			, sourceFile.getPath()
			, destinationFile.getPath()
		);*/
	}

	private static String dimensionToCovertString( final Dimension dimension ) {
		return String.format( "%sx%s", dimension.getWidth(), dimension.getHeight() );
	}
}
