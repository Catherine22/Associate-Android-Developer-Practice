package com.catherine.materialdesignapp;

import androidx.test.runner.AndroidJUnit4;
import com.catherine.materialdesignapp.utils.LocationHelper;
import com.catherine.materialdesignapp.utils.SafetyUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainFragmentTest {
    private LocationHelper locationHelper;

    @Before
    public void createLocationHelper() {
        locationHelper = new LocationHelper();
    }

    // Happy cases
    @Test
    public void loadPreferredLanguage() {
        assertNotNull(locationHelper.getPreferredLanguage());
    }

    @Test
    public void loadPackageName() {
        assertNotNull(MyApplication.INSTANCE.getPackageName());
    }

    @Test
    public void loadFingerprintMd5() {
        assertNotNull(SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "md5"));
    }

    @Test
    public void loadFingerprintMD5() {
        assertNotNull(SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "MD5"));
    }

    @Test
    public void loadFingerprintSHA1() {
        assertNotNull(SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "sha1"));
    }

    @Test
    public void loadFingerprintSHA256() {
        assertNotNull(SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "sha256"));
    }

    @Test
    public void loadApkCrtDigests() {
        List<String> digests = SafetyUtils.calcApkCertificateDigests(MyApplication.INSTANCE, MyApplication.INSTANCE.getPackageName());
        assertNotNull(digests);
        assertFalse(digests.isEmpty());
    }

    // Failure conditions
    @Test
    public void loadUnknownAlgorithm() {
        assertNull(SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "md100"));
    }

    @Test
    public void loadApkCrtDigestsWithTamperedPackageName() {
        List<String> digests = SafetyUtils.calcApkCertificateDigests(MyApplication.INSTANCE, "com.eric.materialdesignapp");
        assertTrue(digests == null || digests.isEmpty());
    }

    @Test
    public void loadDigest() {
        assertNotNull(SafetyUtils.calcApkDigest(MyApplication.INSTANCE));
    }
}