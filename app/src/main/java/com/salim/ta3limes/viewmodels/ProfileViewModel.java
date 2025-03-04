package com.salim.ta3limes.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.AboutUsActivity;
import com.salim.ta3limes.Activities.ChangePasswordActivity;
import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Activities.PersonalDataActivity;
import com.salim.ta3limes.Activities.SendComplainActivity;
import com.salim.ta3limes.Activities.TermsConditionsActivity;
import com.salim.ta3limes.Models.response.LogOutModelResponse;
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

public class ProfileViewModel extends ViewModel {

    public static final String TAG = "MainProfileViewModel";

    private Activity context;
    private SharedPreferencesUtilities preferencesUtilities;
    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<Boolean> blocked = new MutableLiveData<>();

    public MutableLiveData<Boolean> progress = new MutableLiveData<>();
    private FragmentManager supportFragmentManager;

    public ProfileViewModel(Activity context) {
        this.context = context;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void gotoPersonalData(){
        Intent intent = new Intent(context, PersonalDataActivity.class);
        context.startActivity(intent);
    }

    public void gotoChangePassword(){
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        context.startActivity(intent);
    }

    public void gotoAboutUs(){
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }

    public void gotoSendComplain(){
        Intent intent = new Intent(context, SendComplainActivity.class);
        context.startActivity(intent);
    }

    public void gotoTerms(){
        Intent intent = new Intent(context, TermsConditionsActivity.class);
        context.startActivity(intent);
    }

    public void logOut(){
        String token = preferencesUtilities.getToken();

        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.LogOutUser logOutUser = builder.logOutUser();
        Call<LogOutModelResponse> call = logOutUser.logOutUser("Bearer " + token);

        call.enqueue(new Callback<LogOutModelResponse>() {
            @Override
            public void onResponse(Call<LogOutModelResponse> call, Response<LogOutModelResponse> response) {

                Log.e(TAG, "onResponse: Token >> " + token);
                Log.e(TAG, "onResponse: LogOutResponse >> " + response);

                if (response.code() == 200){

                    progress.setValue(false);
                    Log.e(TAG, "onResponse: LogOutResponse >> " + response);
                    message.setValue(response.body().getMessage());
                    preferencesUtilities.setPdfCode(null);
                    preferencesUtilities.setMp3Code(null);
                    preferencesUtilities.setLoggedIn(false);
                    Toast.makeText(context, message.getValue(), Toast.LENGTH_SHORT).show();

                }else {
                    progress.setValue(false);
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LogOutModelResponse> call, Throwable t) {

            }
        });
    }

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
