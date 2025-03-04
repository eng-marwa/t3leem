package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.ForgetPasswordModelResponse;
import com.salim.ta3limes.viewmodels.ForgetPasswordViewModel;

public class ForgetPasswordModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    ForgetPasswordModelResponse.DataBean dataBean;

    public ForgetPasswordModelFactory(Context context, ForgetPasswordModelResponse.DataBean dataBean) {
        this.context = context;
        this.dataBean = dataBean;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ForgetPasswordViewModel(context, dataBean);
    }
}
