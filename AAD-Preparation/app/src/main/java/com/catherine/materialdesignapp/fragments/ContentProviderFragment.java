package com.catherine.materialdesignapp.fragments;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.components.BottomSheetItem;
import com.catherine.materialdesignapp.content_providers.CallLogDao;
import com.catherine.materialdesignapp.listeners.OnActivityEventListener;
import com.catherine.materialdesignapp.listeners.OnRequestPermissionsListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class ContentProviderFragment extends Fragment implements View.OnClickListener {
    public final static String TAG = ContentProviderFragment.class.getSimpleName();
    //    private BottomSheetBehavior behavior;

    private BottomSheetDialog bottomSheetDialog;
    private CallLogDao callLogDao;
    private OnActivityEventListener activityEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        activityEventListener = (OnActivityEventListener) getActivity();
        return inflater.inflate(R.layout.fragment_content_provider, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_content_providers = view.findViewById(R.id.btn_content_providers);
        btn_content_providers.setOnClickListener(this);


        // bottom sheet dialog
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet_app_components, null);
        bottomSheetDialog.setContentView(bottomSheet);

        String[] titles = getResources().getStringArray(R.array.app_components_bottom_sheet_array);
        BottomSheetItem item_create = bottomSheetDialog.findViewById(R.id.item_create);
        item_create.setTitle(titles[0]);
        item_create.setOnClickListener(this);
        BottomSheetItem item_read = bottomSheetDialog.findViewById(R.id.item_read);
        item_read.setTitle(titles[1]);
        item_read.setOnClickListener(this);
        BottomSheetItem item_update = bottomSheetDialog.findViewById(R.id.item_update);
        item_update.setTitle(titles[2]);
        item_update.setOnClickListener(this);
        BottomSheetItem item_delete = bottomSheetDialog.findViewById(R.id.item_delete);
        item_delete.setTitle(titles[3]);
        item_delete.setOnClickListener(this);


        // bottom sheet another version
//        View bottomSheet = findViewById(R.id.bottom_sheet);
//        behavior = BottomSheetBehavior.from(bottomSheet);
//        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                Log.d(TAG, String.format(Locale.US, "bottomSheet: %d", newState));
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            }
//        });
//        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        String[] titles = getResources().getStringArray(R.array.app_components_bottom_sheet_array);
//        TextView tv_create = findViewById(R.id.tv_create);
//        tv_create.setText(titles[0]);
//        tv_create.setOnClickListener(this);
//        TextView tv_read = findViewById(R.id.tv_read);
//        tv_read.setText(titles[1]);
//        tv_read.setOnClickListener(this);
//        TextView tv_update = findViewById(R.id.tv_update);
//        tv_update.setText(titles[2]);
//        tv_update.setOnClickListener(this);
//        TextView tv_delete = findViewById(R.id.tv_delete);
//        tv_delete.setText(titles[3]);
//        tv_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_content_providers:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                String[] permissions = {Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG};
                activityEventListener.getPermissions(permissions, new OnRequestPermissionsListener() {
                    @Override
                    public void onGranted() {
//                        if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
//                            callLogDao = new CallLogDao();
//                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        } else
//                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                        if (bottomSheetDialog.isShowing()) {
                            bottomSheetDialog.closeOptionsMenu();
                        } else {
                            callLogDao = new CallLogDao();
                            bottomSheetDialog.show();
                        }
                    }

                    @Override
                    public void onDenied(@Nullable List<String> deniedPermissions) {
                        Log.d(TAG, "onDenied");
                    }

                    @Override
                    public void onRetry() {
                        Log.d(TAG, "onRetry");
                    }
                });
                break;
            case R.id.item_create:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetDialog.closeOptionsMenu();
                break;
            case R.id.item_read:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                callLogDao.read();
                bottomSheetDialog.closeOptionsMenu();
                // TODO show call logs on RecyclerView
                break;
            case R.id.item_update:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetDialog.closeOptionsMenu();
                // TODO select call logs presented RecyclerView
                break;
            case R.id.item_delete:
//                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetDialog.closeOptionsMenu();
                // TODO select call logs presented RecyclerView
                break;
        }
    }
}
