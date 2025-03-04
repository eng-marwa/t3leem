package com.salim.ta3limes.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.LoginModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.utilities.StaticMethods;
import com.salim.ta3limes.viewmodels.LoginViewModel;
import com.salim.ta3limes.databinding.ActivityLoginBinding;
import com.salim.ta3limes.utilities.CustomEditText;
import com.salim.ta3limes.viewmodels.factory.LoginModelFactory;

import java.util.UUID;

import butterknife.BindView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    @BindView(R.id.phone_EditText)
    CustomEditText phoneEditText;
    @BindView(R.id.password_EditText)
    CustomEditText passwordEditText;
    @BindView(R.id.forgetPassword_textView)
    TextView forgetPasswordTextView;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    LoginViewModel loginViewModel;
    LoginModelResponse.DataBean.UserBean loginModel;
    String mImeiId = "";
    SharedPreferencesUtilities preferencesUtilities;
    private int RECORD_AUDIO_REQUEST_CODE = 123;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtilities = new SharedPreferencesUtilities(this);

        mImeiId = StaticMethods.getIMEIDeviceId(this);
        preferencesUtilities.setDeviceId(mImeiId);
        Log.e(TAG, "onCreate: mImeiId >> " + mImeiId);

        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginModel = new LoginModelResponse.DataBean.UserBean();
        loginViewModel = new ViewModelProvider(this, new LoginModelFactory(this, loginModel)).get(LoginViewModel.class);
   loginViewModel.enableCreateRegister();
        loginViewModel.showRegisterButton.observe(this, show -> {
            if (show) binding.tvRegister.setVisibility(View.VISIBLE);
            else  binding.tvRegister.setVisibility(View.INVISIBLE);
        });
        loginViewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                binding.loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        loginViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
        loginViewModel.message.observe(this, s -> {
            if (s != null && s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                alertDialog.setTitle("تنبيه !");
                alertDialog.setMessage(s);
                alertDialog.setPositiveButton("حسناً", (dialog, which) -> {
                    dialog.dismiss();
                });
                alertDialog.create();
                alertDialog.show();
            } else if (s != null && s.equals("ﺗﻢ ﺗﺴﺠﻴﻞ اﻟﺪﺧﻮﻝ ﺑﻨﺠﺎﺡ")) {
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("changePassword", "success");
                startActivity(intent);
                finish();
            }
        });

        binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            loginViewModel.checked.setValue(isChecked);


        });

        binding.setViewmodel(loginViewModel);
   binding.loginBtn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           if (binding.checkBox.isChecked())
               loginViewModel.onLoginClicked();
           else
               Toast.makeText(LoginActivity.this, "يجب الموافقه علي الشروط والاحكام", Toast.LENGTH_SHORT).show();
       }
   });
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Cairo-Regular.ttf", true);
        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (binding.checkBox.isChecked())
                   startActivity(new Intent(new Intent(LoginActivity.this,RegisterActivity.class)));
               else
                   Toast.makeText(LoginActivity.this, "يجب الموافقه علي الشروط والاحكام", Toast.LENGTH_SHORT).show();
           }
       });
    }

    //    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void getPermissionToRecordAudio() {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    RECORD_AUDIO_REQUEST_CODE);
//
//        }
//    }
    private void checkLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mImeiId = StaticMethods.getIMEIDeviceId(this);
            preferencesUtilities.setDeviceId(mImeiId);
            Log.e(TAG, "onRequestPermissionsResult: mImeiId >> " + mImeiId);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("يجب الموافقة علي الاذن السابق لاتمام عملية الدخول");
            builder.setPositiveButton("حسناً", (dialog, which) -> {
                mImeiId = StaticMethods.getIMEIDeviceId(this);
                preferencesUtilities.setDeviceId(mImeiId);
                Log.e(TAG, "onRequestPermissionsResult: mImeiId >> " + mImeiId);
            });
            builder.create();
            builder.show();
        }
    }

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
                Log.e(TAG, "id: uniqueID >> " + uniqueID);
            }
        }
        return uniqueID;
    }

}
