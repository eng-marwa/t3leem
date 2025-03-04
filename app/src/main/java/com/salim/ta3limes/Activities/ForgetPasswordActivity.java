package com.salim.ta3limes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.salim.ta3limes.Models.response.ForgetPasswordModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.databinding.ActivityForgetPasswordBinding;
import com.salim.ta3limes.viewmodels.ForgetPasswordViewModel;
import com.salim.ta3limes.viewmodels.factory.ForgetPasswordModelFactory;

public class ForgetPasswordActivity extends AppCompatActivity {

    ForgetPasswordViewModel viewModel;
    ForgetPasswordModelResponse.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityForgetPasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        viewModel = new ViewModelProvider(this, new ForgetPasswordModelFactory(this, dataBean)).get(ForgetPasswordViewModel.class);
        viewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                binding.loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }else {
                binding.loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            });

        viewModel.checkUser();
        viewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        viewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });

        binding.setForgetpasswordview(viewModel);
    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkUser();
        viewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        viewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }
}
