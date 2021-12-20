package com.bordozer.jphoto.utils;

import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsService;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsServiceImpl;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class ImageFileUtilsServiceImplTest extends AbstractTestCase {

    public ImageFileUtilsServiceImplTest() {
    }

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void noResize() {
        final Dimension origDimension = new Dimension(100, 100);
        final Dimension toDimension = new Dimension(100, 100);
        final Dimension result = getImageFileUtilsService().resizePhotoImage(origDimension, toDimension);

        assertTrue(getMessage(result, 100, 100), result.getWidth() == toDimension.getWidth() && result.getHeight() == toDimension.getHeight());
    }

    @Test
    public void noShrink() {
        final Dimension origDimension = new Dimension(100, 100);
        final Dimension toDimension = new Dimension(150, 150);
        final Dimension result = getImageFileUtilsService().resizePhotoImage(origDimension, toDimension);

        assertTrue(getMessage(result, origDimension.getWidth(), origDimension.getHeight()), result.getWidth() == origDimension.getWidth() && result.getHeight() == origDimension.getHeight());
    }

    @Test
    public void bothShrink() {
        final Dimension origDimension = new Dimension(100, 100);
        final Dimension toDimension = new Dimension(50, 50);
        final Dimension result = getImageFileUtilsService().resizePhotoImage(origDimension, toDimension);

        assertTrue(getMessage(result, origDimension.getWidth(), origDimension.getHeight()), result.getWidth() == toDimension.getWidth() && result.getHeight() == toDimension.getHeight());
    }

    @Test
    public void widthShrink() {
        final Dimension origDimension = new Dimension(100, 100);
        final Dimension toDimension = new Dimension(50, 100);
        final Dimension result = getImageFileUtilsService().resizePhotoImage(origDimension, toDimension);

        assertTrue(getMessage(result, 50, 50), result.getWidth() == 50 && result.getHeight() == 50);
    }

    @Test
    public void widthShrink2() {
        final Dimension origDimension = new Dimension(100, 100);
        final Dimension toDimension = new Dimension(50, 25);
        final Dimension result = getImageFileUtilsService().resizePhotoImage(origDimension, toDimension);

        assertTrue(getMessage(result, 25, 25), result.getWidth() == 25 && result.getHeight() == 25);
    }

    @Test
    public void widthShrink3() {
        final Dimension origDimension = new Dimension(100, 100);
        final Dimension toDimension = new Dimension(25, 50);
        final Dimension result = getImageFileUtilsService().resizePhotoImage(origDimension, toDimension);

        assertTrue(getMessage(result, 25, 25), result.getWidth() == 25 && result.getHeight() == 25);
    }

    @Test
    public void widthShrink4() {
        final Dimension origDimension = new Dimension(100, 100);
        final Dimension toDimension = new Dimension(25, 101);
        final Dimension result = getImageFileUtilsService().resizePhotoImage(origDimension, toDimension);

        assertTrue(getMessage(result, 25, 25), result.getWidth() == 25 && result.getHeight() == 25);
    }

    @Test
    public void widthShrink5() {
        final Dimension origDimension = new Dimension(100, 100);
        final Dimension toDimension = new Dimension(101, 25);
        final Dimension result = getImageFileUtilsService().resizePhotoImage(origDimension, toDimension);

        assertTrue(getMessage(result, 25, 25), result.getWidth() == 25 && result.getHeight() == 25);
    }

    private String getMessage(final Dimension result, final int width, final int height) {
        return String.format("Result dimension: %d x %d, but should be %d x %d", result.getWidth(), result.getHeight(), width, height);
    }

    private ImageFileUtilsService getImageFileUtilsService() {
        return new ImageFileUtilsServiceImpl();
    }
}
