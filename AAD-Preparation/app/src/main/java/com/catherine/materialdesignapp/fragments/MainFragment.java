package com.catherine.materialdesignapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.utils.CBridge;
import com.catherine.materialdesignapp.utils.LocationHelper;
import com.catherine.materialdesignapp.utils.SafetyUtils;
import com.google.android.material.textfield.TextInputLayout;

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

        TextInputLayout til_package_name = view.findViewById(R.id.til_package_name);
        til_package_name.getEditText().setText(MyApplication.INSTANCE.getPackageName());

        TextInputLayout til_md5 = view.findViewById(R.id.til_md5);
        til_md5.getEditText().setText(SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "md5"));

        TextInputLayout til_sha1 = view.findViewById(R.id.til_sha1);
        til_sha1.getEditText().setText(SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "sha1"));

        TextInputLayout til_sha256 = view.findViewById(R.id.til_sha256);
        til_sha256.getEditText().setText(SafetyUtils.getSigningKeyFingerprint(MyApplication.INSTANCE, "sha256"));

        TextInputLayout til_crt_digest = view.findViewById(R.id.til_crt_digest);
        til_crt_digest.getEditText().setText(SafetyUtils.calcApkCertificateDigests(MyApplication.INSTANCE, MyApplication.INSTANCE.getPackageName()) + "");

        TextInputLayout til_digest = view.findViewById(R.id.til_digest);
        til_digest.getEditText().setText(SafetyUtils.calcApkDigest(MyApplication.INSTANCE));

        TextInputLayout til_location = view.findViewById(R.id.til_location);
        LocationHelper locationHelper = new LocationHelper();
        til_location.getEditText().setText(locationHelper.getPreferredLanguage());

        TextInputLayout til_jni = view.findViewById(R.id.til_jni);
        CBridge cBridge = new CBridge();
        til_jni.getEditText().setText(cBridge.stringFromJNI() + cBridge.plus(45, 55));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}