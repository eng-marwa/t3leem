package com.salim.ta3limes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.salim.ta3limes.Models.response.ChangePasswordModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.databinding.ActivityChangePasswordBinding;
import com.salim.ta3limes.viewmodels.ChangePasswordViewModel;
import com.salim.ta3limes.viewmodels.factory.ChangePasswordModelFactory;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ChangePasswordActivity extends AppCompatActivity {

    ChangePasswordModelResponse.DataBean dataBean;
    ChangePasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Cairo-Regular.ttf", true);

        ActivityChangePasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        viewModel = new ViewModelProvider(this, new ChangePasswordModelFactory(this, dataBean)).get(ChangePasswordViewModel.class);
        viewModel.checkUser();
        viewModel.msg.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        viewModel.message.observe(this, s -> {
            if (s.equals("ﺗﻢ ﺗﻐﻴﺮ ﻛﻠﻤﺔ اﻟﻤﺮﻭﺭ ﺑﻨﺠﺎﺡ")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("changePassword", "success");
                startActivity(intent);
            }
        });

        viewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });

        viewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                binding.loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.setChangepasswordview(viewModel);

    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkUser();
    }
}
