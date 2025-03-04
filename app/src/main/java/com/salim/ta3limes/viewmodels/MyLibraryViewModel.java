package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Models.response.CoursePdfListModel;
import com.salim.ta3limes.Models.response.LibraryModelResponse;
import com.salim.ta3limes.Models.response.SearchResultModelResponse;
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

public class MyLibraryViewModel extends ViewModel {

    public static final String TAG = "MyLibraryViewModel";

    public MutableLiveData<List<LibraryModelResponse.Course>> courseList = new MutableLiveData<>();
    public MutableLiveData<List<CoursePdfListModel.Lessone>> lessonsList = new MutableLiveData<>();

    public MutableLiveData<Boolean> progress = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();

    Context context;
    SharedPreferencesUtilities preferencesUtilities;
    List<LibraryModelResponse.Course> mList;
    private List<CoursePdfListModel.Data> filesBeans;

    public MyLibraryViewModel(List<LibraryModelResponse.Course> courseList, Context context) {
        this.mList = courseList;
        this.context = context;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void getCoursesPDF(){
        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetLibrary getLibrary = builder.getLibrary();
        Call<LibraryModelResponse.Root> call = getLibrary.getLibrary("Bearer " + preferencesUtilities.getToken());
        call.enqueue(new Callback<LibraryModelResponse.Root>() {
            @Override
            public void onResponse(Call<LibraryModelResponse.Root> call, Response<LibraryModelResponse.Root> response) {
                if (response.code() == 200){
                    progress.setValue(false);
                    courseList.setValue(response.body().data.course);
                }else {
                    progress.setValue(false);
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
//                        message.setValue(jsonObject.getString("message"));
//                        Log.e(TAG, "onErrorResponse: msg >> " + message.getValue());
//                        Toast.makeText(context, message.getValue(), Toast.LENGTH_SHORT).show();
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }

            @Override
            public void onFailure(Call<LibraryModelResponse.Root> call, Throwable t) {

            }
        });
    }

    public void getPdfOneCourse(String id, String code){
        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetCoursePDF getCoursePDF = builder.getCoursePDF();
        Call<CoursePdfListModel.Root> call = getCoursePDF.getCoursePdf("Bearer " + preferencesUtilities.getToken(), id, code);
        call.enqueue(new Callback<CoursePdfListModel.Root>() {
            @Override
            public void onResponse(Call<CoursePdfListModel.Root> call, Response<CoursePdfListModel.Root> response) {
                if (response.code() == 200 ){
                    progress.setValue(false);
                    message.setValue(response.body().status);
                    lessonsList.setValue(response.body().data.course.lessone);
                }else {
                    progress.setValue(false);
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        message.setValue(jsonObject.getString("message"));
                        Toast.makeText(context, message.getValue(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CoursePdfListModel.Root> call, Throwable t) {

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