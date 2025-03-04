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
import com.salim.ta3limes.viewmodels.TermsViewModel;
import com.salim.ta3limes.viewmodels.factory.TermsModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsConditionsActivity extends AppCompatActivity {

    TermsViewModel termsViewModel;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.loading)
    MKLoader loader;
    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_terms_conditions);
        ButterKnife.bind(this);

        termsViewModel = new ViewModelProvider(this, new TermsModelFactory(this)).get(TermsViewModel.class);
        termsViewModel.GetTermsAndConditions();
        termsViewModel.termsMutableLiveData.observe(this, s -> {
            text.setText(s);
        });

        termsViewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                loader.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }else {
                loader.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        termsViewModel.checkUser();
        termsViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        termsViewModel.blocked.observe(this, aBoolean -> {
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
        termsViewModel.checkUser();
        termsViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        termsViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }
}
