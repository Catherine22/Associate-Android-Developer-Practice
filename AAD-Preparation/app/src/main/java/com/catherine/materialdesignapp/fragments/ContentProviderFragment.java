package com.catherine.materialdesignapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.activities.CursorLoaderActivity;
import com.catherine.materialdesignapp.adapters.FunctionAdapter;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_provider, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] titles = getActivity().getResources().getStringArray(R.array.content_provider_functions);
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
                        // CursorLoader
                        startCursorLoader(view);
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void startCursorLoader(View view) {
        //Shared Elements Transitions
        TextView tv_title = view.findViewById(R.id.tv_title);
        ConstraintLayout container = view.findViewById(R.id.container);
        Intent intent = new Intent(getActivity(), CursorLoaderActivity.class);
        intent.putExtra(CursorLoaderActivity.PROVIDER, CursorLoaderActivity.CALL_LOGS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Shared Elements Transitions
            String titleTransitionName = tv_title.getTransitionName();
            String bgTransitionName = container.getTransitionName();

            Pair<View, String> p1 = Pair.create(tv_title, titleTransitionName);
            Pair<View, String> p2 = Pair.create(container, bgTransitionName);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}
