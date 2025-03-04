package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;

import com.salim.ta3limes.Models.response.AboutUsModelResponse;
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

public class AboutUsViewModel extends ViewModel {

    public static final String TAG = "AboutUsViewModel";

    public MutableLiveData<String> termsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();


    Context context;
    SharedPreferencesUtilities preferencesUtilities;

    public AboutUsViewModel(Context context) {
        this.context = context;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void GetAboutUs(){
        String token = preferencesUtilities.getToken();

        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetAboutUs getAboutUs = builder.getAboutUs();
        Call<AboutUsModelResponse> call = getAboutUs.getAboutUs("Bearer " + token);
        call.enqueue(new Callback<AboutUsModelResponse>() {
            @Override
            public void onResponse(Call<AboutUsModelResponse> call, Response<AboutUsModelResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);
                if (response.code() == 200){
                    progress.setValue(false);
                    String content = response.body().getData().getAbout().getContent();
                    Log.e(TAG, "onResponse: Content >> " + content);
                    termsMutableLiveData.setValue(content);
                }else {
                    progress.setValue(false);
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<AboutUsModelResponse> call, Throwable t) {

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
