package com.salim.ta3limes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;

import com.salim.ta3limes.Models.response.ResetPasswordModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.databinding.ActivityResetPasswordBinding;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.ResetPasswordViewModel;
import com.salim.ta3limes.viewmodels.factory.ResetPasswordModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetPasswordActivity extends AppCompatActivity {

    Button sendBtn;
    ResetPasswordViewModel viewModel;
    ResetPasswordModelResponse.DataBean dataBean;

    SharedPreferencesUtilities preferencesUtilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityResetPasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);

        preferencesUtilities = new SharedPreferencesUtilities(this);
        viewModel = new ViewModelProvider(this, new ResetPasswordModelFactory(this, dataBean)).get(ResetPasswordViewModel.class);
        viewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                binding.loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.ResetPassword(preferencesUtilities.getBaseUrl());

            }
        });
        if (preferencesUtilities.getToken() != null && !preferencesUtilities.getToken().isEmpty()
                && preferencesUtilities.getPHONE() != null && !preferencesUtilities.getPHONE().isEmpty())
            viewModel.checkUser(preferencesUtilities.getToken(), preferencesUtilities.getPHONE()
                    ,preferencesUtilities.getBaseUrl());
        viewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        viewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
        binding.setResetpasswordview(viewModel);
    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferencesUtilities.getToken() != null && !preferencesUtilities.getToken().isEmpty()
                && preferencesUtilities.getPHONE() != null && !preferencesUtilities.getPHONE().isEmpty())
            viewModel.checkUser(preferencesUtilities.getToken(),
                    preferencesUtilities.getPHONE(),preferencesUtilities.getBaseUrl());
        viewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        viewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }
}
