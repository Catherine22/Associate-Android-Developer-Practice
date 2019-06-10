package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.utils.CBridge;
import com.catherine.materialdesignapp.utils.LocationHelper;
import com.catherine.materialdesignapp.utils.SafetyUtils;

import java.util.Locale;

public class MainFragment extends Fragment {
    private final static String TAG = MainFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //This keypair stores in your keystore. You can also see the same information by "keytool -list -v -keystore xxx.keystore  -alias xxx  -storepass xxx -keypass xxx" command
        //keytool -list -v -keystore debug.keystore -alias androiddebugkey -storepass android -keypass android

        LocationHelper locationHelper = new LocationHelper();
        TextView tv_location = view.findViewById(R.id.tv_location);
        String sb =
                "From NDK:" +
                        new CBridge().stringFromJNI() +
                        "\nPackage name:" +
                        MyApplication.INSTANCE.getPackageName() +
                        "\nMD5:" +
                        SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "md5") +
                        "\nFingerprint:\n{\nSHA1:" +
                        SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "sha1") +
                        "\nSHA256:" +
                        SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "sha256") +
                        "\n}\nApkCertificateDigestSha256:" +
                        SafetyUtils.calcApkCertificateDigests(MyApplication.INSTANCE, MyApplication.INSTANCE.getPackageName()) +
                        "\nApkDigest:" +
                        SafetyUtils.calcApkDigest(MyApplication.INSTANCE);
        tv_location.setText(String.format(Locale.US, "%s\nPreferred language: %s", sb, locationHelper.getPreferredLanguage()));
    }
}