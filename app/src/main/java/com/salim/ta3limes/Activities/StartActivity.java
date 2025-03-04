package com.salim.ta3limes.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import com.salim.ta3limes.Adapters.CustomViewPagerAdapter;
import com.salim.ta3limes.Models.response.ServerStatusResponse;
import com.salim.ta3limes.Models.response.StartModel;
import com.salim.ta3limes.Models.response.StartResponse;
import com.salim.ta3limes.Models.response.StartResponseModel;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.tuyenmonkey.mkloader.MKLoader;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    public static final String TAG = "SplashActivity";
    private static final String MyPREFERENCES = "TaalimPrefrance";
    private SharedPreferences sp;
    SharedPreferencesUtilities preferencesUtilities;
    private String fireBase_Token, pushtoken;
    private ViewPager viewPager;
    private Button btnContinue;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private TextView tvDummy;
    MKLoader loading;
    private CustomViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loading = findViewById(R.id.loading);
        /* splash code*/
        preferencesUtilities = new SharedPreferencesUtilities(this);

        sp = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Cairo-Regular.ttf", true);


        viewPager=(ViewPager) findViewById(R.id.viewpager);
        btnContinue=findViewById(R.id.btnContinue);
        img1=findViewById(R.id.ivIndicatorOne);
        img2=findViewById(R.id.ivIndicatorTwo);
        img3=findViewById(R.id.ivIndicatorThree);
        tvDummy=findViewById(R.id.tvDummy);
        adapter=new CustomViewPagerAdapter(this);
        getBaseUrl();
       btnContinue.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             switch (viewPager.getCurrentItem())
             {
                 case 0:{
                     viewPager.setCurrentItem(1);
                     break;
                 }
                 case 1:{
                     viewPager.setCurrentItem(2);
                     break;
                 }
                 default: {
                    // startActivity(new Intent(StartActivity.this,RegisterActivity.class));
                     loading.setVisibility(View.VISIBLE);
                     getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                     if (isEmulator()) {
                         loading.setVisibility(View.INVISIBLE);
                      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                         checkDevice();
                     } else {
                         if (preferencesUtilities.getToken() != null && !preferencesUtilities.getToken().isEmpty() &&
                                 preferencesUtilities.getPHONE() != null && !preferencesUtilities.getPHONE().isEmpty())
                             checkUser();
                         else
                             openMain();

                             }
                     }
                 }
             }

     });

    }

    private void initViewPager(List<StartModel> list) {
       adapter.setList(list);
     viewPager.setAdapter(adapter);

     viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
       switch (position)
       {
           case 0:
           {
               changeIndicatorColor(img1, img2, img3);
               btnContinue.setText(getString(R.string.continu));
              // tvDummy.setVisibility(View.VISIBLE);
               break;
           }
           case 1:{
               changeIndicatorColor(img2, img1, img3);
               btnContinue.setText(getString(R.string.continu));
               //tvDummy.setVisibility(View.VISIBLE);

               break;
           }
           case 2: {
               changeIndicatorColor(img3, img2, img1);
               btnContinue.setText(getString(R.string.start));
              // tvDummy.setVisibility(View.INVISIBLE);
               break;
           }
           default:break;
       }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    });

    }
    private void changeIndicatorColor( ImageView select, ImageView unSelectOne,ImageView unSelectTwo) {
        select.setImageResource(R.drawable.selected);
        unSelectOne.setImageResource(R.drawable.unselected);
        unSelectTwo.setImageResource(R.drawable.unselected);
    }
//    private fun goToLoginScreen() {
//        // SharedPreferencesManager.saveSkipType(SKIP)
//        startActivity(Intent(this@StartActivity, LoginActivity::class.java))
//    }
    public void getBaseUrl() {
     loading.setVisibility(View.VISIBLE);
    ServiceBuilder builder = new ServiceBuilder("https://t3liim.com");
    ServiceInterfaces.ServerStatus serverStatus =builder.serverStatus();
    Call<ServerStatusResponse.Root> call = serverStatus.serverStatus();
    Log.e("ahmed","a");
    call.enqueue(new Callback<ServerStatusResponse.Root>() {
        @Override
        public void onResponse(Call<ServerStatusResponse.Root> call, Response<ServerStatusResponse.Root> response) {
            loading.setVisibility(View.GONE);
            if (response.code() == 200) {
                preferencesUtilities.setBaseUrl(response.body().data.domain);
                getSlider();

            }else if (response.code() == 400){
                String errorororororr;
                try {
                    errorororororr = response.errorBody().string();
                    Log.e(TAG, "onResponse: Error >> " + errorororororr);
                    JSONObject object = new JSONObject(errorororororr);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onFailure(Call<ServerStatusResponse.Root> call, Throwable t) {
            loading.setVisibility(View.GONE);
        }
    });
}
    public void checkUser() {

        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.UserBlocked userBlocked = builder.userBlocked();
        Call<UserBlockedModelResponse.Root> call = userBlocked.userBlocked("Bearer " + preferencesUtilities.getToken(), preferencesUtilities.getPHONE());
        call.enqueue(new Callback<UserBlockedModelResponse.Root>() {
            @Override
            public void onResponse(Call<UserBlockedModelResponse.Root> call, Response<UserBlockedModelResponse.Root> response) {
                loading.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Log.e("ahmed", "onResponse: CheckBlocked >> " + response.code());
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
                else
                {
                    Toast.makeText(StartActivity.this,""+response.code(),Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<UserBlockedModelResponse.Root> call, Throwable t) {
              Toast.makeText(StartActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                Log.i("ahmed",t.getMessage());
                loading.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                Toast.makeText(StartActivity.this, "value connect", Toast.LENGTH_SHORT).show();
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
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            //Intent i = new Intent(getBaseContext(), StartActivity.class);
            startActivity(i);
            finish();
        } else {
            checkToken(preferencesUtilities);
        }
    }

    public static boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.HARDWARE.equals("vbox86")
                || Build.HARDWARE.toLowerCase().contains("nox")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
                || Build.PRODUCT.toLowerCase().contains("nox")
                || Build.BOARD.toLowerCase().contains("nox")
                || Build.BOOTLOADER.toLowerCase().contains("nox")
                ;
    }

    private void checkDevice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
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

    private void getSlider(){
        loading.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.Start start=builder.getSLider();
        Call<StartResponseModel> call=start.getSlider();
        call.enqueue(new Callback<StartResponseModel>() {
            @Override
            public void onResponse(Call<StartResponseModel> call, Response<StartResponseModel> response) {
                loading.setVisibility(View.GONE);
                Log.i("Ahmed",response.code()+"");
                if (response.body()!=null)
                {
                    initViewPager(response.body().getData().getSliders());
                }
            }

            @Override
            public void onFailure(Call<StartResponseModel> call, Throwable t) {
                loading.setVisibility(View.GONE);
            }
        });
    }
}