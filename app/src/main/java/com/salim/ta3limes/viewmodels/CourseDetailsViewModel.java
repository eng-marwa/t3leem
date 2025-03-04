package com.salim.ta3limes.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.salim.ta3limes.Models.response.SuggesstionCourseModelResponse;
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

public class CourseDetailsViewModel extends ViewModel {

    public MutableLiveData<Boolean> progress = new MutableLiveData<>();
    public MutableLiveData<String> courseName = new MutableLiveData<>();
    public MutableLiveData<String> courseDesc = new MutableLiveData<>();
    public MutableLiveData<String> courseTeacher = new MutableLiveData<>();
    public MutableLiveData<String> courseImage = new MutableLiveData<>();
    public MutableLiveData<String> courseOrganisation = new MutableLiveData<>();
    public MutableLiveData<String> videoId = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> whatsapp = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();
    private String TAG = "CourseDetailsViewModel";

    public void getCourseDetails(String token, String id, SharedPreferencesUtilities preferencesUtilities){
        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetSuggesstionCourse getSuggesstionCourse = builder.getSuggesstionCourse();
        Call<SuggesstionCourseModelResponse.Root> call = getSuggesstionCourse.getSuggesstionCourse("Bearer "+ token, id);
        call.enqueue(new Callback<SuggesstionCourseModelResponse.Root>() {
            @Override
            public void onResponse(Call<SuggesstionCourseModelResponse.Root> call, Response<SuggesstionCourseModelResponse.Root> response) {
                if (response.code() == 200){
                    progress.setValue(false);
                    courseName.setValue(response.body().data.course.name);
                    courseDesc.setValue(response.body().data.course.description);
                    courseImage.setValue(response.body().data.course.image);
                    courseOrganisation.setValue(response.body().data.course.organisation);
                    courseTeacher.setValue(response.body().data.course.teacher);
                    videoId.setValue(response.body().data.course.video_id);
                    phone.setValue(response.body().data.course.phone);
                    whatsapp.setValue(response.body().data.course.whatsapp);
                }else {
                    progress.setValue(false);
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        message.setValue(jsonObject.getString("message"));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuggesstionCourseModelResponse.Root> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<Boolean> blocked = new MutableLiveData<>();

    public void checkUser(String token, String phone, String preferencesUtilitiesToken) {
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilitiesToken);
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
