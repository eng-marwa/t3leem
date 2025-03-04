package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.viewmodels.DataViewModel;
import com.salim.ta3limes.Models.response.CourseDataModelResponse;

public class CourseDataModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    CourseDataModelResponse.DataBean.CourseBean courseBean;

    public CourseDataModelFactory(Context context, CourseDataModelResponse.DataBean.CourseBean courseBean) {
        this.context = context;
        this.courseBean = courseBean;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DataViewModel(context, courseBean);
    }
}
