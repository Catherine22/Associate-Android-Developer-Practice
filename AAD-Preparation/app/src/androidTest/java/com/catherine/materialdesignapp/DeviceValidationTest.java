package com.catherine.materialdesignapp;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;
import com.catherine.materialdesignapp.utils.LocationHelper;
import com.catherine.materialdesignapp.utils.SafetyUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * In this case, we need the instance of MyApplication
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class DeviceValidationTest {
    private LocationHelper locationHelper;
    private MyApplication myApplication;

    @Before
    public void setUp() {
        myApplication = ApplicationProvider.getApplicationContext();
        locationHelper = new LocationHelper();
    }

    // Happy cases
    @Test
    public void loadPreferredLanguage() {
        assertNotNull(locationHelper.getPreferredLanguage());
    }

    @Test
    public void loadPackageName() {
        assertNotNull(myApplication.getPackageName());
    }

    @Test
    public void loadFingerprintMd5() {
        assertNotNull(SafetyUtils.getSigningKeyFingerprint(myApplication, "md5"));
    }

    @Test
    public void loadFingerprintMD5() {
        assertNotNull(SafetyUtils.getSigningKeyFingerprint(myApplication, "MD5"));
    }

    @Test
    public void loadFingerprintSHA1() {
        assertNotNull(SafetyUtils.getSigningKeyFingerprint(myApplication, "sha1"));
    }

    @Test
    public void loadFingerprintSHA256() {
        assertNotNull(SafetyUtils.getSigningKeyFingerprint(myApplication, "sha256"));
    }

    @Test
    public void loadApkCrtDigests() {
        List<String> digests = SafetyUtils.calcApkCertificateDigests(myApplication, myApplication.getPackageName());
        assertNotNull(digests);
        assertFalse(digests.isEmpty());
    }

    // Failure conditions
    @Test
    public void loadUnknownAlgorithm() {
        assertNull(SafetyUtils.getSigningKeyFingerprint(myApplication, "md100"));
    }

    @Test
    public void loadApkCrtDigestsWithTamperedPackageName() {
        List<String> digests = SafetyUtils.calcApkCertificateDigests(myApplication, "com.eric.materialdesignapp");
        assertTrue(digests == null || digests.isEmpty());
    }

    @Test
    public void loadDigest() {
        assertNotNull(SafetyUtils.calcApkDigest(myApplication));
    }
}