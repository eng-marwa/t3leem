package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.viewmodels.CoursesViewModel;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;

import java.util.List;

public class MainModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context context;
    private List<PersonalDataModelResponse.DataBean.CentersBean> dataBeanList;


    public MainModelFactory(Context context, List<PersonalDataModelResponse.DataBean.CentersBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CoursesViewModel(context, dataBeanList);
    }
}
