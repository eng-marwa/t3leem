package com.salim.ta3limes.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.salim.ta3limes.Models.response.CheckRegisterResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.utilities.StaticMethods;
import com.salim.ta3limes.viewmodels.AboutUsViewModel;
import com.salim.ta3limes.viewmodels.LoginViewModel;
import com.salim.ta3limes.viewmodels.factory.AboutUsModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = "SplashActivity";
    Animation fromsmall, fromnothing, togo;
    @BindView(R.id.logo_image)
    ImageView logoImage;
    @BindView(R.id.loading)
    TextView loading;

    private static final String MyPREFERENCES = "TaalimPrefrance";
    private SharedPreferences sp;
    SharedPreferencesUtilities preferencesUtilities;
    private String fireBase_Token, pushtoken;
  Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


         Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
           retrofit = new Retrofit.Builder()
                .baseUrl("https://t3liim.com/api/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofit.create(ServiceInterfaces.EnableCreateAccount.class);
        preferencesUtilities = new SharedPreferencesUtilities(this);

//        mImeiId = StaticMethods.getIMEIDeviceId(this);
//        preferencesUtilities.setDeviceId(mImeiId);
//        Log.e(TAG, "onCreate: mImeiId >> " + mImeiId);


        sp = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Cairo-Regular.ttf", true);

        fromsmall = AnimationUtils.loadAnimation(this, R.anim.fromsmall);
        fromnothing = AnimationUtils.loadAnimation(this, R.anim.fromnothing);
        togo = AnimationUtils.loadAnimation(this, R.anim.togo);

        logoImage.startAnimation(fromsmall);
        loading.startAnimation(fromnothing);

        if (isEmulator()) {
            checkDevice();
        } else {
            Thread th = new Thread() {
                @Override
                public void run() {

                    try {

                        sleep(4000);

                        if (preferencesUtilities.getToken() != null && !preferencesUtilities.getToken().isEmpty() &&
                                preferencesUtilities.getPHONE() != null && !preferencesUtilities.getPHONE().isEmpty())
                            checkUser();
                        else
                            openMain();

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            };
            th.start();
        }
    }


    public void checkUser() {
        ServiceBuilder builder = new ServiceBuilder("");
        ServiceInterfaces.UserBlocked userBlocked = builder.userBlocked();
        Call<UserBlockedModelResponse.Root> call = userBlocked.userBlocked("Bearer " + preferencesUtilities.getToken(), preferencesUtilities.getPHONE());
        call.enqueue(new Callback<UserBlockedModelResponse.Root>() {
            @Override
            public void onResponse(Call<UserBlockedModelResponse.Root> call, Response<UserBlockedModelResponse.Root> response) {
                Log.e(TAG, "onResponse: CheckBlocked >> " + response);
                if (response.code() == 200) {
                    boolean blocked = response.body().data.blocked;
                    if (blocked) checkLogin();
                    else openMain();

                } else if (response.code() == 400) {
                    String errorororororr;
                    try {
                        errorororororr = response.errorBody().string();
                        Log.e(TAG, "onResponse: Error >> " + errorororororr);
                        JSONObject object = new JSONObject(errorororororr);
                        String message = object.getString("message");
                        if (message.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                            checkLogin();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    checkLogin();
                }
            }

            @Override
            public void onFailure(Call<UserBlockedModelResponse.Root> call, Throwable t) {

            }
        });
    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void checkToken(SharedPreferencesUtilities preferences) {

        fireBase_Token = preferences.getRegToken();
        if (fireBase_Token == null || fireBase_Token.isEmpty() || TextUtils.isEmpty(fireBase_Token)) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                fireBase_Token = instanceIdResult.getToken();
                preferencesUtilities.setRegToken(fireBase_Token);
                Toast.makeText(SplashActivity.this, "value connect", Toast.LENGTH_SHORT).show();
                checkLogin();
            });
        } else {
            checkLogin();
        }

    }



    private void openMain() {
        if (preferencesUtilities.getLoggedIn()) {
            Log.e("run: Logged >> ", "done");
//            getToken();
            //Intent i = new Intent(getBaseContext(), MainActivity.class);
            Intent i = new Intent(getBaseContext(), StartActivity.class);
            startActivity(i);
            finish();
        } else {
            checkToken(preferencesUtilities);
        }
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    private void checkDevice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setMessage("التطبيق غير مدعم علي هذا الجهاز برجاء استخدام موبايل حقيقي");
        builder.setPositiveButton("حسناً", (dialog, which) -> {
            finish();
        });
        builder.create();
        builder.show();
    }



    private void sendRegTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }

}
