package com.bordozer.jphoto.utils;

import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class PagingUtilsTest extends AbstractTestCase {

    public PagingUtilsTest() {
    }

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void startPageIndex() {
        assertTrue("Condition must be TRUE but false", PagingUtils.getPageItemStartIndex(2, 7) == 7);
        assertTrue("Condition must be TRUE but false", PagingUtils.getPageItemStartIndex(3, 8) == 16);
        assertTrue("Condition must be TRUE but false", PagingUtils.getPageItemStartIndex(5, 10) == 40);
    }

    @Test
    public void endPageIndex() {
        assertTrue("Condition must be TRUE but false", PagingUtils.getPageItemEndIndex(2, 7) == 13);
        assertTrue("Condition must be TRUE but false", PagingUtils.getPageItemEndIndex(3, 8) == 23);
        assertTrue("Condition must be TRUE but false", PagingUtils.getPageItemEndIndex(5, 10) == 49);
    }
}
