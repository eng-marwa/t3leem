package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.SearchResultModelResponse;
import com.salim.ta3limes.viewmodels.AboutUsViewModel;
import com.salim.ta3limes.viewmodels.SearchViewModel;

import java.util.List;

public class SearchModelFactory extends ViewModelProvider.NewInstanceFactory {
    Context context;
    private List<SearchResultModelResponse.Result> searchResult;

    public SearchModelFactory(Context context, List<SearchResultModelResponse.Result> searchResult) {
        this.context = context;
        this.searchResult = searchResult;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(searchResult , context);
    }
}
