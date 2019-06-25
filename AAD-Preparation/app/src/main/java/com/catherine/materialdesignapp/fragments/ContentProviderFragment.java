package com.catherine.materialdesignapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.FunctionAdapter;
import com.catherine.materialdesignapp.listeners.ContentProviderFragmentListener;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;

public class ContentProviderFragment extends Fragment {
    public final static String TAG = ContentProviderFragment.class.getSimpleName();
    private int[] colourMatrix = new int[]{
            Color.rgb(28, 160, 170),
            Color.rgb(103, 153, 170),
            Color.rgb(141, 218, 247),
            Color.rgb(99, 161, 247),
            Color.rgb(112, 219, 219),
            Color.rgb(0, 178, 238)
    };
    private ContentProviderFragmentListener contentProviderFragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_provider, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentProviderFragmentListener = (ContentProviderFragmentListener) getActivity();
        String[] titles = getResources().getStringArray(R.array.content_provider_functions);
        Pair[] items = new Pair[titles.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Pair<>(titles[i], colourMatrix[i]);
        }
        RecyclerView recyclerView = view.findViewById(R.id.rv_functions);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        FunctionAdapter adapter = new FunctionAdapter(items, getActivity(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        contentProviderFragmentListener.popUpFragment(position);
                        break;
                    case 1:

                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }
}
