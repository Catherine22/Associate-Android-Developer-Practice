package com.catherine.materialdesignapp.listeners;

import android.view.View;

public interface OnPlaylistItemClickListener<E> {
    void onItemClicked(View view, int position);

    void onAddButtonClicked(View view, int position);

    void onDragged(int oldPosition, int newPosition);

    void onSwiped(E e);
}
