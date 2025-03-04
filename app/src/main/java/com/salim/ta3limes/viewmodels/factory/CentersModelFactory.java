package com.salim.ta3limes.viewmodels.factory;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.viewmodels.CentersViewModel;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;

import java.util.List;

public class CentersModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Activity context;
    private List<PersonalDataModelResponse.DataBean.CentersBean> dataBeanList;


    public CentersModelFactory(Activity context, List<PersonalDataModelResponse.DataBean.CentersBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CentersViewModel(context, dataBeanList);
    }
}
