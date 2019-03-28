package com.catherine.materialdesignapp.content_providers;

import java.util.List;

public abstract class Dao<T> {
    abstract List<T> read();

    abstract void insert(T newValue);

    abstract void update(T oldValue, T newValue);

    abstract void delete(T value);
}
