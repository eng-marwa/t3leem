package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
import com.salim.ta3limes.Models.response.ResetPasswordModelResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDataViewModel extends ViewModel {

    public static final String TAG = "PersonalDataViewModel";

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> image = new MutableLiveData<>();
    public MutableLiveData<String> department = new MutableLiveData<>();
    public MutableLiveData<String> fuculty = new MutableLiveData<>();
    public MutableLiveData<String> year = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();

    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    Context context;
    SharedPreferencesUtilities preferencesUtilities;
    PersonalDataModelResponse.DataBean.UserBean userBean;

    public PersonalDataViewModel(Context context, PersonalDataModelResponse.DataBean.UserBean userBean) {
        this.context = context;
        this.userBean = userBean;
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
                Log.e(TAG, "onResponse: Response >> " + response.toString());
                if (response.code() == 200) {
                    Log.e(TAG, "onResponse: Response >> " + response.toString());
                    progress.setValue(false);

                    if (response.body().getData().getUser().getName() != null)
                        name.setValue(response.body().getData().getUser().getName());
                    if (response.body().getData().getUser().getPhone() != null)
                        phone.setValue(response.body().getData().getUser().getPhone());
                    if (response.body().getData().getUser().getPicture() != null)
                        image.setValue(response.body().getData().getUser().getPicture());
                    if (response.body().getData().getUser().getFaculty() != null)
                        fuculty.setValue(response.body().getData().getUser().getFaculty().getName());
                    else
                        fuculty.setValue("الكلية : ");
                    if (response.body().getData().getUser().getDepartment() != null)
                        department.setValue(response.body().getData().getUser().getDepartment().getName());
                    else department.setValue("القسم : ");
                    year.setValue(String.valueOf(response.body().getData().getUser().getYear()));

                    Log.e(TAG, "onResponse: Data >> " + name.getValue() + " " + phone.getValue() + " " + image.getValue());
                } else {
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
                } else if (response.code() == 400) {
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
