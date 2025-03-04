package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.NotificationModelResponse;
import com.salim.ta3limes.viewmodels.NotificationViewModel;

import java.util.List;

public class NotificationModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    private List<NotificationModelResponse.DataBeanX.NotificationsBean> notificationsBeans;

    public NotificationModelFactory(Context context, List<NotificationModelResponse.DataBeanX.NotificationsBean> notificationsBeans) {
        this.context = context;
        this.notificationsBeans = notificationsBeans;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NotificationViewModel(context, notificationsBeans);
    }
}
