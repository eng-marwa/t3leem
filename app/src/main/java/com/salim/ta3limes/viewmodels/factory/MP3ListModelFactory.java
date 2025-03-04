package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.CoursePdfListModel;
import com.salim.ta3limes.Models.response.MP3FilesModelResponse;
import com.salim.ta3limes.viewmodels.Mp3FilesViewModel;
import com.salim.ta3limes.viewmodels.MyPDFsViewModel;

import java.util.List;

public class MP3ListModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    private List<MP3FilesModelResponse.Course> filesBeans;

    public MP3ListModelFactory(Context context, List<MP3FilesModelResponse.Course> courseList) {
        this.context = context;
        this.filesBeans = courseList;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new Mp3FilesViewModel(filesBeans , context);
    }
}
