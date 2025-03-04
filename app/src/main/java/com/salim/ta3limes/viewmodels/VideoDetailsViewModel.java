package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Adapters.AdapterVideoComments;
import com.salim.ta3limes.Models.response.PostCommentModelResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.Models.response.VideoCommentsModelResponse;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoDetailsViewModel extends ViewModel {

    public static final String TAG = "VideoDetailsViewModel";

    public MutableLiveData<List<VideoCommentsModelResponse.DataBean.CommentsBean>> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();
    public MutableLiveData<String> status = new MutableLiveData<>();
    public MutableLiveData<String> max_views = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();

    Context context;
    ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean> commentsBeans;
    VideoCommentsModelResponse.DataBean.CommentsBean commentsBean;
    VideoCommentsModelResponse.DataBean.CommentsBean.StudentBean studentBean;
    AdapterVideoComments adapterVideoComments;
    SharedPreferencesUtilities preferencesUtilities;

    public VideoDetailsViewModel(Context context, ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean> commentsBeans) {
        this.context = context;
        this.commentsBeans = commentsBeans;
        preferencesUtilities = new SharedPreferencesUtilities(context);


    }

    public void GetComments(int id){
        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetVideoDetails getVideoDetails = builder.getVideoDetails();
        Call<VideoCommentsModelResponse> call = getVideoDetails.getVideoDetails("Bearer " + preferencesUtilities.getToken(), id);
        call.enqueue(new Callback<VideoCommentsModelResponse>() {
            @Override
            public void onResponse(Call<VideoCommentsModelResponse> call, Response<VideoCommentsModelResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);
                if (response.code() == 200){
                    progress.setValue(false);
                    Log.e(TAG, "onResponse: Response >> " + response);
                    mutableLiveData.setValue(response.body().getData().getComments());
                    max_views.setValue(response.body().getData().getVideo().getMax_views());
                }else {
                    progress.setValue(false);
                    String errorororororr;
                    try {
                        errorororororr = response.errorBody().string();
                        JSONObject object = new JSONObject(errorororororr);
                        message.setValue(object.getString("message"));
                        Toast.makeText(context, message.getValue(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onResponse: Message >> " + message.getValue());
                   
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoCommentsModelResponse> call, Throwable t) {

            }
        });
    }

    public void PostComment(int id, String comment_type, String comment, MultipartBody.Part voiceFile){

        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.PostComment postComment = builder.postComment();
        Call<PostCommentModelResponse> call = postComment.postComment("Bearer " + preferencesUtilities.getToken(), id, comment_type, comment, voiceFile);
        call.enqueue(new Callback<PostCommentModelResponse>() {
            @Override
            public void onResponse(Call<PostCommentModelResponse> call, Response<PostCommentModelResponse> response) {
                Log.e(TAG, "onResponse: ResponseComment Success >> " + response);
                if (response.code() == 200){
                    progress.setValue(false);
                    Log.e(TAG, "onResponse: ResponseComment Success >> " + response);
                    status.setValue(response.body().getStatus());
                    for (int i = 0; i < commentsBeans.size(); i++) {

                        String content = commentsBeans.get(i).getContent();
                        String date = commentsBeans.get(i).getDate();
                        String name = commentsBeans.get(i).getStudent().getName();
                        String image = commentsBeans.get(i).getStudent().getPicture();

                        commentsBean = new VideoCommentsModelResponse.DataBean.CommentsBean();
                        studentBean = new VideoCommentsModelResponse.DataBean.CommentsBean.StudentBean();
                        adapterVideoComments = new AdapterVideoComments(context, commentsBeans);

                        studentBean.setName(name);
                        studentBean.setPicture(image);

                        commentsBean.setContent(content);
                        commentsBean.setDate(date);
                        commentsBean.setStudent(studentBean);

                    }
                }else {
                    progress.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<PostCommentModelResponse> call, Throwable t) {

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
