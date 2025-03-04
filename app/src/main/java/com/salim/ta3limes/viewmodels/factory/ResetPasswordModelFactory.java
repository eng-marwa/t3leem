package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.ResetPasswordModelResponse;
import com.salim.ta3limes.viewmodels.ResetPasswordViewModel;

public class ResetPasswordModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    ResetPasswordModelResponse.DataBean dataBean;

    public ResetPasswordModelFactory(Context context, ResetPasswordModelResponse.DataBean dataBean) {
        this.context = context;
        this.dataBean = dataBean;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ResetPasswordViewModel(context, dataBean);
    }
}
