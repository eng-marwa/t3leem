package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Models.response.CourseDataModelResponse;
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

public class DataViewModel extends ViewModel {
    public static final String TAG = "CourseDataViewModel";

    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<String> teacherName = new MutableLiveData<>();
    public MutableLiveData<String> teacherImage = new MutableLiveData<>();
    public MutableLiveData<String> centerName = new MutableLiveData<>();
    public MutableLiveData<String> centerImage = new MutableLiveData<>();
    public MutableLiveData<String> courseDate = new MutableLiveData<>();
    public MutableLiveData<String> courseDesc = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();

    Context context;
    CourseDataModelResponse.DataBean.CourseBean courseBean;
    SharedPreferencesUtilities preferencesUtilities;
    int courseId;

    public DataViewModel(Context context, CourseDataModelResponse.DataBean.CourseBean courseBean) {
        this.context = context;
        this.courseBean = courseBean;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void GetCourseInfo(int id){

        progress.setValue(true);
        ServiceBuilder builder =  new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetCourseData getCourseData = builder.getCourseData();
        Call<CourseDataModelResponse> call = getCourseData.getCourseData("Bearer " + preferencesUtilities.getToken(), id);
        call.enqueue(new Callback<CourseDataModelResponse>() {
            @Override
            public void onResponse(Call<CourseDataModelResponse> call, Response<CourseDataModelResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);
                if (response.code() == 200){
                    progress.setValue(false);
                    Log.e(TAG, "onResponse: Response >> " + response);
                    courseId = response.body().getData().getCourse().getInfo().getId();
                    title.setValue(response.body().getData().getCourse().getInfo().getName());
//                    teacherName.setValue(response.body().getData().getCourse().getInfo().getTeacher().getName());
//                    teacherImage.setValue(response.body().getData().getCourse().getInfo().getTeacher().getPicture());
                    centerName.setValue(response.body().getData().getCourse().getInfo().getCenter().getName());
                    centerImage.setValue(response.body().getData().getCourse().getInfo().getCenter().getLogo());
                    courseDate.setValue(response.body().getData().getCourse().getInfo().getStarts_at());
                    courseDesc.setValue(response.body().getData().getCourse().getInfo().getDesc());

                }else {
                    progress.setValue(false);
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        message.setValue(jsonObject.getString("message"));
                        Log.e(TAG, "onErrorResponse: msg >> " + message.getValue());
                        Toast.makeText(context, message.getValue(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<CourseDataModelResponse> call, Throwable t) {

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
