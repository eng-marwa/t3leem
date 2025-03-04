package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Models.response.CourseVideosModelResponse;
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

public class VideosViewModel extends ViewModel {

    public static final String TAG = "CourseVideosViewModel";

    public MutableLiveData<List<CourseVideosModelResponse.Lesson>> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CourseVideosModelResponse.Suggestlessone>> suggestmutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> videotitle = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    Context context;
    List<CourseVideosModelResponse.Lesson> lessonsBeans;
    List<CourseVideosModelResponse.Suggestlessone> suggestlessones;
    SharedPreferencesUtilities preferencesUtilities;

    public VideosViewModel(Context context, List<CourseVideosModelResponse.Lesson> lessonsBeans, List<CourseVideosModelResponse.Suggestlessone> suggestlessones) {
        this.context = context;
        this.lessonsBeans = lessonsBeans;
        this.suggestlessones = suggestlessones;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void GetVideos(int id){


        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetCourseVideos getCourseVideos = builder.getCourseVideos();
        Call<CourseVideosModelResponse.Root> call = getCourseVideos.getCourseVideos("Bearer " + preferencesUtilities.getToken() , id);
        call.enqueue(new Callback<CourseVideosModelResponse.Root>() {
            @Override
            public void onResponse(Call<CourseVideosModelResponse.Root> call, Response<CourseVideosModelResponse.Root> response) {
                Log.e(TAG, "onResponse: Response >> " + response);
                if (response.code() == 200){
                    progress.setValue(false);
                    Log.e(TAG, "onResponse: Response >> " + response);
                    mutableLiveData.setValue(response.body().data.lessons);
                    suggestmutableLiveData.setValue(response.body().data.suggestlessone);
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
            public void onFailure(Call<CourseVideosModelResponse.Root> call, Throwable t) {

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
