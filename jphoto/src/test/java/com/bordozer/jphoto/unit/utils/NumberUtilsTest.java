package com.bordozer.jphoto.utils;

import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberUtilsTest extends AbstractTestCase {

    public NumberUtilsTest() {
    }

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void round() {

        final float actualResult = NumberUtils.round(10.49f, 1);
        final float expectedResult = 10.5f;

        assertEquals(expectedResult, actualResult, 0);
    }

    @Test
    public void round1() {

        final float actualResult = NumberUtils.round(10.49f, 0);
        final float expectedResult = 10.0f;

        assertEquals(expectedResult, actualResult, 0);
    }

    @Test
    public void round2() {

        final float actualResult = NumberUtils.round(10.5f, 0);
        final float expectedResult = 11.0f;

        assertEquals(expectedResult, actualResult, 0);
    }

    @Test
    public void round3() {

        final float actualResult = NumberUtils.round(10.51f, 0);
        final float expectedResult = 11.0f;

        assertEquals(expectedResult, actualResult, 0);
    }
}
