package com.catherine.materialdesignapp;

import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;
import com.catherine.materialdesignapp.utils.CBridge;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class NativeUnitTest {
    private CBridge cBridge;

    @Before
    public void loadCBridge() {
        cBridge = new CBridge();
    }

    @Test
    public void nativeCalculate() {
        assertEquals(cBridge.plus(5, 9), 14);
    }

    @Test
    public void readNativeVariable() {
        assertNotNull(cBridge.stringFromJNI());
    }
}
