package com.catherine.materialdesignapp.jetpack.view_models;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.catherine.materialdesignapp.jetpack.repositories.ArtistRepository;

import java.lang.reflect.InvocationTargetException;

public class ArtistViewModelFactory implements ViewModelProvider.Factory {
    private final ArtistRepository mRepository;

    public static ArtistViewModelFactory createFactory(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Not yet attached to Application");
        }
        return new ArtistViewModelFactory(ArtistRepository.getInstance(application));
    }

    private ArtistViewModelFactory(ArtistRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(ArtistRepository.class).newInstance(mRepository);
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
