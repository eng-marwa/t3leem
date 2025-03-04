package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.LoginModelResponse;
import com.salim.ta3limes.viewmodels.LoginViewModel;

public class LoginModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context context;
    private LoginModelResponse.DataBean.UserBean loginModelResponse;

    public LoginModelFactory(Context context, LoginModelResponse.DataBean.UserBean loginModelResponse) {
        this.context = context;
        this.loginModelResponse = loginModelResponse;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new LoginViewModel(context, loginModelResponse);
    }
}
