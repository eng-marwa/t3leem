package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Models.response.ChangePasswordModelResponse;
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

public class ChangePasswordViewModel extends ViewModel {

    public static final String TAG = "ChangePasswordViewModel";

    public MutableLiveData<String> currentPassword = new MutableLiveData<>();
    public MutableLiveData<String> newpassword = new MutableLiveData<>();
    public MutableLiveData<String> passwordconfirmation = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    Context context;
    ChangePasswordModelResponse.DataBean dataBean;
    SharedPreferencesUtilities preferencesUtilities;
//    private FragmentManager supportFragmentManager;

    public ChangePasswordViewModel(Context context, ChangePasswordModelResponse.DataBean dataBean) {
        this.context = context;
        this.dataBean = dataBean;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void ChangePassword() {
        String curPassword = currentPassword.getValue();
        String NewPassword = newpassword.getValue();
        String configPassword = passwordconfirmation.getValue();
        progress.setValue(true);


        if (curPassword != null && !curPassword.isEmpty()) {
            if (NewPassword != null && !NewPassword.isEmpty()) {
                if (configPassword != null && !configPassword.isEmpty()) {

                    ServiceBuilder builde = new ServiceBuilder(preferencesUtilities.getBaseUrl());
                    ServiceInterfaces.ChangePassword changePassword = builde.changePassword();
                    Call<ChangePasswordModelResponse> call = changePassword.changePassword("Bearer " + preferencesUtilities.getToken(), curPassword
                            , NewPassword, configPassword);
                    call.enqueue(new Callback<ChangePasswordModelResponse>() {
                        @Override
                        public void onResponse(Call<ChangePasswordModelResponse> call, Response<ChangePasswordModelResponse> response) {
                            Log.e(TAG, "onResponse: Response >> " + response);
                            if (response.code() == 200) {
                                progress.setValue(false);
                                Log.e(TAG, "onResponse: Response >> " + response);
                                message.setValue(response.body().getMessage());

                            } else {
                                progress.setValue(false);
                                String msg = "كلمة المرور الحالية غير صحيحة";
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangePasswordModelResponse> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(context, "تأكيد كلمة المرور الجديدة مطلوبة", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "كلمة المرور الجديدة مطلوبة", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "كلمة المرور الحالية مطلوبة", Toast.LENGTH_SHORT).show();
        }

    }


    public MutableLiveData<String> msg = new MutableLiveData<>();
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
                        msg.setValue(object.getString("message"));

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
