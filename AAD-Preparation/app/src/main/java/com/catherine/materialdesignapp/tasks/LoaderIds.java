package com.catherine.materialdesignapp.tasks;

public enum LoaderIds {
    SLEEP_TASK(0);
    private final int value;

    LoaderIds(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}