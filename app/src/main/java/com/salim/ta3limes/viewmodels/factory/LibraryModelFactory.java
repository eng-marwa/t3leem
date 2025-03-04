package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.LibraryModelResponse;
import com.salim.ta3limes.Models.response.SearchResultModelResponse;
import com.salim.ta3limes.viewmodels.MyLibraryViewModel;
import com.salim.ta3limes.viewmodels.SearchViewModel;

import java.util.List;

public class LibraryModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    private List<LibraryModelResponse.Course> mList;

    public LibraryModelFactory(Context context, List<LibraryModelResponse.Course> courseList) {
        this.context = context;
        this.mList = courseList;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MyLibraryViewModel(mList , context);
    }
}
