package com.catherine.materialdesignapp.listeners;

public interface OnItemMoveListener {

    void onDragged(int oldPosition, int newPosition);

    void onSwiped(int position);
}
