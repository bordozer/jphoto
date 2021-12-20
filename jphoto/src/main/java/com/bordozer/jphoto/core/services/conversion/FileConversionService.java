package com.bordozer.jphoto.core.services.conversion;

import com.bordozer.jphoto.core.general.conversion.ConversionOptions;

import java.io.File;
import java.io.IOException;

public interface FileConversionService {

    boolean convertSync(final File sourceFile, final File destinationFile, final ConversionOptions conversionOptions) throws IOException, InterruptedException;

    void convertAsync(final File sourceFile, final File destinationFile, final ConversionOptions conversionOptions) throws IOException, InterruptedException;
}
