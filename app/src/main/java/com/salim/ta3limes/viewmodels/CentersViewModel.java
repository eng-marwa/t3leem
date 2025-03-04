package com.salim.ta3limes.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
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

public class CentersViewModel extends ViewModel {

    public static final String TAG = "CentersViewModel";

    public MutableLiveData<List<PersonalDataModelResponse.DataBean.CentersBean>> beanLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    private Activity context;
    List<PersonalDataModelResponse.DataBean.CentersBean> centersBeans;
    SharedPreferencesUtilities preferencesUtilities;

    public CentersViewModel(Activity context) {
        this.context = context;
    }

    public CentersViewModel(Activity context, List<PersonalDataModelResponse.DataBean.CentersBean> centersBeans) {
        this.context = context;
        this.centersBeans = centersBeans;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void getInfo() {
        progress.setValue(true);

        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetInfo getInfo = builder.getInfo();
        Call<PersonalDataModelResponse> call = getInfo.getInfo("Bearer " + preferencesUtilities.getToken());
        call.enqueue(new Callback<PersonalDataModelResponse>() {
            @Override
            public void onResponse(Call<PersonalDataModelResponse> call, Response<PersonalDataModelResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);
                if (response.code() == 200) {
                    Log.e(TAG, "onResponse: Response >> " + response);
                    progress.setValue(false);
                    beanLiveData.setValue(response.body().getData().getCenters());
                }else {
                    progress.setValue(false);
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<PersonalDataModelResponse> call, Throwable t) {

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
