package com.catherine.materialdesignapp.listeners;

import android.view.View;

public interface OnPlaylistItemClickListener {
    void onItemClicked(View view, int position);

    void onAddButtonClicked(View view, int position);

    void onDragged(int oldPosition, int newPosition);

    void onSwiped(int position);
}
