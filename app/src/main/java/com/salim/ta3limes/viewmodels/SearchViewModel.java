package com.salim.ta3limes.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salim.ta3limes.Activities.LoginActivity;
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

public class SearchViewModel extends ViewModel {

    public static final String TAG = "SearchViewModel";

    public MutableLiveData<List<SearchResultModelResponse.Result>> searchResult = new MutableLiveData<>();

    public MutableLiveData<Boolean> progress = new MutableLiveData<>();

    Context context;
    SharedPreferencesUtilities preferencesUtilities;
    List<SearchResultModelResponse.Result> search;

    public SearchViewModel(List<SearchResultModelResponse.Result> search, Context context) {
        this.search = search;
        this.context = context;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public void getResults(String query, String type){
        progress.setValue(true);

        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.Search search = builder.search();
        Call<SearchResultModelResponse.Root> call = search.search("Bearer " + preferencesUtilities.getToken() , query, type);
        call.enqueue(new Callback<SearchResultModelResponse.Root>() {
            @Override
            public void onResponse(Call<SearchResultModelResponse.Root> call, Response<SearchResultModelResponse.Root> response) {
                if (response.code() == 200){
                    progress.setValue(false);
                    searchResult.setValue(response.body().results);
                }else {
                    progress.setValue(false);
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<SearchResultModelResponse.Root> call, Throwable t) {

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
