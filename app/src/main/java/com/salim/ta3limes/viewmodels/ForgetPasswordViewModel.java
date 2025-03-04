package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Activities.ResetPasswordActivity;
import com.salim.ta3limes.Models.response.ForgetPasswordModelResponse;
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

public class ForgetPasswordViewModel extends ViewModel {

    public static final String TAG = "ForgetPasswordViewModel";

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    Context context;
    ForgetPasswordModelResponse.DataBean dataBean;
    SharedPreferencesUtilities preferencesUtilities;

    public ForgetPasswordViewModel(Context context, ForgetPasswordModelResponse.DataBean dataBean) {
        this.context = context;
        this.dataBean = dataBean;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }


    public void ForgetPassword(){
        String Email = email.getValue();

        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.ForgetPassword forgetPassword = builder.forgetPassword();
        Call<ForgetPasswordModelResponse> call = forgetPassword.forgetPassword(Email);
        call.enqueue(new Callback<ForgetPasswordModelResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordModelResponse> call, Response<ForgetPasswordModelResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);

                if (response.code() == 200){
                    progress.setValue(false);
                    Log.e(TAG, "onResponse: Response >> " + response);
                    String msg = response.body().getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, ResetPasswordActivity.class);
                    intent.putExtra("email", Email);
                    context.startActivity(intent);
                }else {
                    progress.setValue(false);
                    String msg = response.body().getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgetPasswordModelResponse> call, Throwable t) {

            }
        });
    }

    public void back(){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
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
