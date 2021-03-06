package com.bordozer.jphoto.core.services.utils;

import com.bordozer.jphoto.core.general.user.User;

import java.io.File;
import java.io.IOException;

public interface TempFileUtilsService {

    String getTmpDir() throws IOException;

    File getTempFileWithOriginalExtension(final User user, final String fileName) throws IOException;

    File getTempFileWithOriginalExtension(final String fileName) throws IOException;

    File getTempFile() throws IOException;

    String getTempFileName() throws IOException;
}
