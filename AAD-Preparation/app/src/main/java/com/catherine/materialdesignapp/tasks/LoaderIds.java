package com.catherine.materialdesignapp.tasks;

public enum LoaderIds {
    SLEEP_TASK1(1),
    SLEEP_TASK2(2);
    private final int value;

    LoaderIds(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}