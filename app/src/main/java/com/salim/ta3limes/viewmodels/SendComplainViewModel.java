package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Models.response.SendComplainModelResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendComplainViewModel extends ViewModel {

    public static final String TAG = "SendComplainViewModel";

    public MutableLiveData<String> complain = new MutableLiveData<>();

    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    Context context;
    SendComplainModelResponse.DataBean dataBean;
    SharedPreferencesUtilities preferencesUtilities;

    public SendComplainViewModel(Context context, SendComplainModelResponse.DataBean dataBean) {
        this.context = context;
        this.dataBean = dataBean;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void SendComlain(){

        dataBean.setContent(complain.getValue());

        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.SendComplain sendComplain = builder.sendComplain();
        Call<SendComplainModelResponse> call = sendComplain.sendComlain("Bearer " + preferencesUtilities.getToken(), dataBean.getContent());
        call.enqueue(new Callback<SendComplainModelResponse>() {
            @Override
            public void onResponse(Call<SendComplainModelResponse> call, Response<SendComplainModelResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);
                if (response.code() == 200){
                    Log.e(TAG, "onResponse: Response >> " + response);
                    progress.setValue(false);
                    String msg = response.body().getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                }else {
                    progress.setValue(false);
                    String msg = response.body().getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendComplainModelResponse> call, Throwable t) {

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
