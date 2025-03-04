package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.ChangePasswordModelResponse;
import com.salim.ta3limes.viewmodels.ChangePasswordViewModel;

public class ChangePasswordModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    ChangePasswordModelResponse.DataBean dataBean;

    public ChangePasswordModelFactory(Context context, ChangePasswordModelResponse.DataBean dataBean) {
        this.context = context;
        this.dataBean = dataBean;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChangePasswordViewModel(context, dataBean);
    }
}
