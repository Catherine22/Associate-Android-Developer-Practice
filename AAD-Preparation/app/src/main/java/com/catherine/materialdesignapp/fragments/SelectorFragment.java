package com.catherine.materialdesignapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.activities.CursorLoaderActivity;
import com.catherine.materialdesignapp.listeners.ContentProviderFragmentListener;

public class SelectorFragment extends Fragment {
    public final static String TAG = SelectorFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ContentProviderFragmentListener listener = (ContentProviderFragmentListener) getActivity();

        int index = (getArguments() != null) ? getArguments().getInt("index", 0) : 0;
        String[] array = listener.getList(index);
        ListView listView = view.findViewById(R.id.list_functions);
        listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array));
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            if (index == 0) {
                // cursorLoader
                switch (position) {
                    case 0:
                        startCursorLoader(CursorLoaderActivity.CALL_LOGS);
                        break;
                    case 1:
                        startCursorLoader(CursorLoaderActivity.CONTACTS);
                        break;
                }
            }
        });
    }

    private void startCursorLoader(int type) {
        Intent intent = new Intent(getActivity(), CursorLoaderActivity.class);
        intent.putExtra(CursorLoaderActivity.PROVIDER, type);
        startActivity(intent);
    }
}
