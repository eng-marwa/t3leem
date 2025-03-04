package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.SendComplainModelResponse;
import com.salim.ta3limes.viewmodels.SendComplainViewModel;

public class SendComplainModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    SendComplainModelResponse.DataBean dataBean;

    public SendComplainModelFactory(Context context, SendComplainModelResponse.DataBean dataBean) {
        this.context = context;
        this.dataBean = dataBean;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SendComplainViewModel(context, dataBean);
    }
}
