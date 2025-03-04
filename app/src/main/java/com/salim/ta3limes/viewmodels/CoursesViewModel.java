package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Models.response.CenterCoursesModelResponse;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
import com.salim.ta3limes.Models.response.UpdateFireBaseTokenModelResponse;
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

public class CoursesViewModel extends ViewModel {

    public static final String TAG = "MainViewModel";

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> image = new MutableLiveData<>();
    public MutableLiveData<String> coursesnumber = new MutableLiveData<>();
    public MutableLiveData<List<PersonalDataModelResponse.DataBean.CentersBean>> beanLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CenterCoursesModelResponse.DataBean.CourseBean>> listMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();
    private Context context;
    String platform = "android";

    List<PersonalDataModelResponse.DataBean.CentersBean> centersBeans;
    SharedPreferencesUtilities preferencesUtilities;

    public CoursesViewModel(Context context, List<PersonalDataModelResponse.DataBean.CentersBean> centersBeans) {
        this.centersBeans = centersBeans;
        this.context = context;
        preferencesUtilities = new SharedPreferencesUtilities(context);

    }

    public void getInfo() {

        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetInfo getInfo = builder.getInfo();
        Call<PersonalDataModelResponse> call = getInfo.getInfo("Bearer " + preferencesUtilities.getToken());
        call.enqueue(new Callback<PersonalDataModelResponse>() {
            @Override
            public void onResponse(Call<PersonalDataModelResponse> call, Response<PersonalDataModelResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);
                if (response.code() == 200) {
                    Log.e(TAG, "onResponse: Response >> " + response);
                    beanLiveData.setValue(response.body().getData().getCenters());
                    name.setValue(response.body().getData().getUser().getName());
                    image.setValue(response.body().getData().getUser().getPicture());

                    preferencesUtilities.setPROFILEIMAGE(response.body().getData().getUser().getPicture());
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

    public void getCenterCourses(int id) {
        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetCenterCourses getCenterCourses = builder.getCenterCourses();
        Call<CenterCoursesModelResponse> call = getCenterCourses.getCenterCourses("Bearer " + preferencesUtilities.getToken(), id);
        call.enqueue(new Callback<CenterCoursesModelResponse>() {
            @Override
            public void onResponse(Call<CenterCoursesModelResponse> call, Response<CenterCoursesModelResponse> response) {
                Log.e(TAG, "onResponse: coursesResponse >> " + response);
                if (response.code() == 200) {
                    progress.setValue(false);
                    listMutableLiveData.setValue(response.body().getData().getCourse());
                    coursesnumber.setValue(String.valueOf("(" + response.body().getData().getCourse_count() + ")"));
                } else {
                    progress.setValue(false);
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<CenterCoursesModelResponse> call, Throwable t) {

            }
        });
    }

    public void UpdateToken(String token, String device) {
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.UpdateFireBaseToken updateFireBaseToken = builder.updateFireBaseToken();
        Call<UpdateFireBaseTokenModelResponse> call = updateFireBaseToken.updateToken("Bearer " + preferencesUtilities.getToken(), token, platform, device);
        call.enqueue(new Callback<UpdateFireBaseTokenModelResponse>() {
            @Override
            public void onResponse(Call<UpdateFireBaseTokenModelResponse> call, Response<UpdateFireBaseTokenModelResponse> response) {
                if (response.code() == 200) {
                    String msg = response.body().getMessage();
                    Log.e("CoursesViewModel", msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                } else {
                    progress.setValue(false);
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UpdateFireBaseTokenModelResponse> call, Throwable t) {

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
