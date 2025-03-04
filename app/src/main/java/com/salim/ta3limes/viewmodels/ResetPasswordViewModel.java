package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Models.response.ResetPasswordModelResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordViewModel extends ViewModel {

    public static final String TAG = "ResetPasswordViewModel";

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> code = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> passwordConfirmation = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    Context context;
    ResetPasswordModelResponse.DataBean dataBean;

    public ResetPasswordViewModel(Context context, ResetPasswordModelResponse.DataBean dataBean) {
        this.context = context;
        this.dataBean = dataBean;
    }

    public void ResetPassword(String baseUrl){
        progress.setValue(true);

        String Email = email.getValue();
        String Code = code.getValue();
        String Password = password.getValue();
        String PasswordConfig = passwordConfirmation.getValue();

        ServiceBuilder builder = new ServiceBuilder(baseUrl);
        ServiceInterfaces.ResetPassword resetPassword = builder.resetPassword();
        Call<ResetPasswordModelResponse> call = resetPassword.resetPassword(Email, Code, Password, PasswordConfig);
        call.enqueue(new Callback<ResetPasswordModelResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordModelResponse> call, Response<ResetPasswordModelResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);

                if (response.code() == 200){
                    Log.e(TAG, "onResponse: Response >> " + response);
                    progress.setValue(false);
                    String msg = "تم تغيير كلمة المرور بنجاح .. قم بتسجيل الدخول الآن";
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }else {
                    progress.setValue(false);
                    String msg = response.body().getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordModelResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<Boolean> blocked = new MutableLiveData<>();

    public void checkUser(String token, String phone,String baseUrl) {
        ServiceBuilder builder = new ServiceBuilder(baseUrl);
        ServiceInterfaces.UserBlocked userBlocked = builder.userBlocked();
        Call<UserBlockedModelResponse.Root> call = userBlocked.userBlocked("Bearer " + token, phone);
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
