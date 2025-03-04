package com.salim.ta3limes.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.ForgetPasswordActivity;
import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Models.response.CheckRegisterResponse;
import com.salim.ta3limes.Models.response.LoginModelResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.utilities.StaticMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    public static final String TAG = "LoginViewModel";
    public MutableLiveData<String> phoneMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> passwordMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> deviceId = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();

    public MutableLiveData<Boolean> progress = new MutableLiveData<>();
    public MutableLiveData<Boolean> checked = new MutableLiveData<>(false);

    private LoginModelResponse.DataBean.UserBean loginModel;
    private Context context;
    private Activity activity;
    private SharedPreferencesUtilities preferencesUtilities;

    public LoginViewModel(Context context, LoginModelResponse.DataBean.UserBean loginModel) {
        this.loginModel = loginModel;
        this.context = context;
        this.activity = (Activity) context;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void onLoginClicked() {

        loginModel.setPhone(phoneMutableLiveData.getValue());
        loginModel.setPassword(passwordMutableLiveData.getValue());
        deviceId.setValue(preferencesUtilities.getDeviceId());
        progress.setValue(true);

        Log.e(TAG, "onLoginClicked: LoginPhone >> " + loginModel.getPhone());
        Log.e(TAG, "onLoginClicked: LoginPassword >> " + loginModel.getPassword());

        if (loginModel.getPhone() != null && !loginModel.getPhone().isEmpty()) {
            if (loginModel.getPassword() != null && !loginModel.getPassword().isEmpty()) {
                if (deviceId.getValue() != null && !deviceId.getValue().isEmpty()) {

                    ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
                    ServiceInterfaces.LoginUser loginUser = builder.loginUser();
                    Call<LoginModelResponse> call = loginUser.loginUser(loginModel.getPhone(), loginModel.getPassword(), preferencesUtilities.getDeviceId());
                    call.enqueue(new Callback<LoginModelResponse>() {
                        @Override
                        public void onResponse(Call<LoginModelResponse> call, Response<LoginModelResponse> response) {
                            Log.e(TAG, "onResponse: LoginResponse >> " + response.toString());
                            if (response.code() == 200) {
                                Log.e(TAG, "onResponse: LoginResponse >> " + response.toString());
                                progress.setValue(false);
                                message.setValue(response.body().getMessage());
                                Toast.makeText(context, message.getValue(), Toast.LENGTH_SHORT).show();
                                String token = response.body().getData().getToken();
                                String studentId = response.body().getData().getUser().getStuedntID();
                                String name = response.body().getData().getUser().getName();
                                String phone = response.body().getData().getUser().getPhone();
                                String image = response.body().getData().getUser().getPicture();

                                Log.e(TAG, "onResponse: Token >> " + token);
                                preferencesUtilities.setToken(token);
                                preferencesUtilities.setUserId(studentId);
                                preferencesUtilities.setUserName(name);
                                preferencesUtilities.setPHONE(phone);
                                preferencesUtilities.setPROFILEIMAGE(image);
                                preferencesUtilities.setLoggedIn(true);

//                                Intent intent = new Intent(context, MainActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(intent);

                            } else if (response.code() == 400) {
                                progress.setValue(false);
                                String errorororororr;
                                try {
                                    errorororororr = response.errorBody().string();
                                    Log.e(TAG, "onResponse: Error >> " + errorororororr);
                                    JSONObject object = new JSONObject(errorororororr);
                                    message.setValue(object.getString("message"));
                                    Toast.makeText(context, message.getValue(), Toast.LENGTH_SHORT).show();
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                progress.setValue(false);
                                String msg = "خطأ في رقم الموبايل او كلمة المرور";
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onResponse: MSG >> " + msg);
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModelResponse> call, Throwable t) {
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("يجب الموافقة علي الاذن السابق لاتمام عملية الدخول");
                    builder.setPositiveButton("حسناً", (dialog, which) -> {
                        StaticMethods.getIMEIDeviceId(activity);
                    });
                    builder.create();
                    builder.show();
                }

            } else {
                progress.setValue(false);
                Toast.makeText(context, "كلمة المرور مطلوبة", Toast.LENGTH_SHORT).show();
            }
        } else {
            progress.setValue(false);
            Toast.makeText(context, "رقم الموبايل مطلوب", Toast.LENGTH_SHORT).show();
        }
    }

    public void onForgetPassword() {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }

    public MutableLiveData<Boolean> blocked = new MutableLiveData<>();
    public MutableLiveData<Boolean> showRegisterButton = new MutableLiveData<>();

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
                } else if (response.code() == 401) {
                    Intent i = new Intent(context, LoginActivity.class);
                    context.startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<UserBlockedModelResponse.Root> call, Throwable t) {

            }
        });
    }
    public void enableCreateRegister() {
        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.EnableCreateAccount checkRegister = builder.enableCreateRegister();
        Call<CheckRegisterResponse.Root> call = checkRegister.enableCreateAccount();
        call.enqueue(new Callback<CheckRegisterResponse.Root>() {
            @Override
            public void onResponse(Call<CheckRegisterResponse.Root> call, Response<CheckRegisterResponse.Root> response) {
                progress.setValue(false);
                if (response.code() == 200) {
                    showRegisterButton.setValue(response.body().data.enable);
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
            public void onFailure(Call<CheckRegisterResponse.Root> call, Throwable t) {
                progress.setValue(false);
            }
        });
    }
    private void checkLogin() {


    }

}
