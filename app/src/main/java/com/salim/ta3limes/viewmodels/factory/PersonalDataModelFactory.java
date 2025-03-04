package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
import com.salim.ta3limes.viewmodels.PersonalDataViewModel;

public class PersonalDataModelFactory extends ViewModelProvider.NewInstanceFactory {

Context context;
PersonalDataModelResponse.DataBean.UserBean userBean;

    public PersonalDataModelFactory(Context context, PersonalDataModelResponse.DataBean.UserBean userBean) {
        this.context = context;
        this.userBean = userBean;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PersonalDataViewModel(context, userBean);
    }
}
