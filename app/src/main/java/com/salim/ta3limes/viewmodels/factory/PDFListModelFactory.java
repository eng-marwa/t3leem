package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.CoursePdfListModel;
import com.salim.ta3limes.Models.response.LibraryModelResponse;
import com.salim.ta3limes.viewmodels.MyLibraryViewModel;
import com.salim.ta3limes.viewmodels.MyPDFsViewModel;

import java.util.List;

public class PDFListModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    private List<CoursePdfListModel.Lessone> filesBeans;

    public PDFListModelFactory(Context context, List<CoursePdfListModel.Lessone> courseList) {
        this.context = context;
        this.filesBeans = courseList;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MyPDFsViewModel(filesBeans , context);
    }
}
