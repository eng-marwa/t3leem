package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Models.response.NotificationModelResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {

    public static final String TAG = "TermsViewModel";

    public MutableLiveData<List<NotificationModelResponse.DataBeanX.NotificationsBean>> NotiesMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    Context context;
    SharedPreferencesUtilities preferencesUtilities;
    List<NotificationModelResponse.DataBeanX.NotificationsBean> notificationsBeans;

    public NotificationViewModel(Context context, List<NotificationModelResponse.DataBeanX.NotificationsBean> notificationsBeans) {
        this.context = context;
        this.notificationsBeans = notificationsBeans;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void GetNotificationsList (){
        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetNotification getNotification = builder.getNotification();
        Call<NotificationModelResponse> call = getNotification.getNotifcation("Bearer " + preferencesUtilities.getToken());
        call.enqueue(new Callback<NotificationModelResponse>() {
            @Override
            public void onResponse(Call<NotificationModelResponse> call, Response<NotificationModelResponse> response) {
                if (response.code() == 200){
                    progress.setValue(false);
                    NotiesMutableLiveData.setValue(response.body().getData().getNotifications());
                }else {
                    progress.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<NotificationModelResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<Boolean> blocked = new MutableLiveData<>();

    public void checkUser() {
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.UserBlocked userBlocked = builder.userBlocked();
        Call<UserBlockedModelResponse.Root> call = userBlocked.userBlocked("Bearer " + preferencesUtilities.getToken(), preferencesUtilities.getPHONE());
        call.enqueue(new Callback<UserBlockedModelResponse.Root>() {
            @Override
            public void onResponse(Call<UserBlockedModelResponse.Root> call, Response<UserBlockedModelResponse.Root> response) {
                Log.e(TAG, "onResponse: CheckBlocked >> " + response);
                if (response.code() == 200) {
                    blocked.setValue(response.body().data.blocked);
                }else if (response.code() == 400){
                    String errorororororr;
                    try {
                        errorororororr = response.errorBody().string();
                        Log.e(TAG, "onResponse: Error >> " + errorororororr);
                        JSONObject object = new JSONObject(errorororororr);
                        message.setValue(object.getString("message"));

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserBlockedModelResponse.Root> call, Throwable t) {

            }
        });
    }

}
