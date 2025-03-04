package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.util.IOUtils;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.Models.response.VideoCommentsModelResponse;
import com.salim.ta3limes.Models.response.VideoPDFModlResponse;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPDFViewModel extends ViewModel {

    public static final String TAG = "VideoDetailsViewModel";

    public MutableLiveData<String> fileUrl = new MutableLiveData<>();
    public MutableLiveData<Boolean> download = new MutableLiveData<>();
    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    Context context;
    VideoCommentsModelResponse.DataBean.CommentsBean commentsBean;
    SharedPreferencesUtilities preferencesUtilities;

    public VideoPDFViewModel(Context context) {
        this.context = context;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void GetPDF(String id) {
        progress.setValue(true);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetVideoPDF getVideoPDF = builder.getVideoPDF();
        Call<VideoPDFModlResponse> call = getVideoPDF.getVideoPDF("Bearer " + preferencesUtilities.getToken(), id);
        call.enqueue(new Callback<VideoPDFModlResponse>() {
            @Override
            public void onResponse(Call<VideoPDFModlResponse> call, Response<VideoPDFModlResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);
                if (response.code() == 200) {
                    progress.setValue(false);
                    Log.e(TAG, "onResponse: Response >> " + response);
                    if (response.body().getData().getFile() != null) {
                        fileUrl.setValue(response.body().getData().getFile());
                        download.setValue(response.body().getData().getDownload());

                    } else {
                        fileUrl.setValue("");
                    }
                } else {
                    progress.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<VideoPDFModlResponse> call, Throwable t) {

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
