package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.CourseMp3ListFiles;
import com.salim.ta3limes.Models.response.MP3FilesModelResponse;
import com.salim.ta3limes.viewmodels.Mp3FilesViewModel;
import com.salim.ta3limes.viewmodels.MyMP3ViewModel;

import java.util.List;

public class MyMP3ListModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    private List<CourseMp3ListFiles.Lessone> filesBeans;

    public MyMP3ListModelFactory(Context context, List<CourseMp3ListFiles.Lessone> courseList) {
        this.context = context;
        this.filesBeans = courseList;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MyMP3ViewModel(filesBeans , context);
    }
}
