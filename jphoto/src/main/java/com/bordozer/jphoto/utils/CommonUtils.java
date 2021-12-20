package com.bordozer.jphoto.utils;

import com.google.common.io.Resources;
import lombok.SneakyThrows;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class CommonUtils {

    @SneakyThrows
    public static String readResource(final String name) {
        final URL url = Resources.getResource(name);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }
}
