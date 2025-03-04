package com.salim.ta3limes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.R;
import com.salim.ta3limes.viewmodels.AboutUsViewModel;
import com.salim.ta3limes.viewmodels.factory.AboutUsModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends AppCompatActivity {

    AboutUsViewModel aboutUsViewModel;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.loading)
    MKLoader loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        aboutUsViewModel = new ViewModelProvider(this, new AboutUsModelFactory(this)).get(AboutUsViewModel.class);
        aboutUsViewModel.GetAboutUs();
        aboutUsViewModel.checkUser();
        aboutUsViewModel.termsMutableLiveData.observe(this, s -> {
            text.setText(s);
        });

        aboutUsViewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        aboutUsViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        aboutUsViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        aboutUsViewModel.checkUser();
        aboutUsViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        aboutUsViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }
}
