package com.salim.ta3limes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.salim.ta3limes.Models.response.SendComplainModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.databinding.ActivitySendComplainBinding;
import com.salim.ta3limes.viewmodels.SendComplainViewModel;
import com.salim.ta3limes.viewmodels.factory.SendComplainModelFactory;

public class SendComplainActivity extends AppCompatActivity {

    SendComplainViewModel viewModel;
    SendComplainModelResponse.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ActivitySendComplainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_send_complain);
        dataBean = new SendComplainModelResponse.DataBean();
        viewModel = new ViewModelProvider(this, new SendComplainModelFactory(this, dataBean)).get(SendComplainViewModel.class);
        viewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                binding.loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.loading.setVisibility(View.GONE);
                binding.complainImageView.setVisibility(View.VISIBLE);
                binding.complainEditText.setVisibility(View.GONE);
                binding.sendBtn.setVisibility(View.GONE);
                binding.title.setVisibility(View.INVISIBLE);
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

        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.setComplainview(viewModel);
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
        viewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        viewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }
}
