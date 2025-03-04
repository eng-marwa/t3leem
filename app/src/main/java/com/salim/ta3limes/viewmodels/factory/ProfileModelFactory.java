package com.salim.ta3limes.viewmodels.factory;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.viewmodels.ProfileViewModel;

public class ProfileModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Activity context;

    public ProfileModelFactory(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProfileViewModel(context);
    }
}
