package com.salim.ta3limes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.salim.ta3limes.Adapters.AdapterNotification;
import com.salim.ta3limes.Models.response.NotificationModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.NotificationViewModel;
import com.salim.ta3limes.viewmodels.factory.NotificationModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity {

    public static final String TAG = "NotificationActivity";

    @BindView(R.id.recyclerView_RecyclerView)
    RecyclerView recyclerViewRecyclerView;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.loading)
    MKLoader loading;

    AdapterNotification adapterNotification;
    List<NotificationModelResponse.DataBeanX.NotificationsBean> notificationModels;
    LinearLayoutManager linearLayoutManager;

    NotificationViewModel viewModel;
    SharedPreferencesUtilities preferencesUtilities;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        preferencesUtilities = new SharedPreferencesUtilities(this);
        token = preferencesUtilities.getToken();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(TAG, "onMessageBundle: notification " + bundle.toString());
        }

        viewModel = new ViewModelProvider(this, new NotificationModelFactory(this, notificationModels)).get(NotificationViewModel.class);
        viewModel.GetNotificationsList();
        viewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }else {
                loading.setVisibility(View.GONE);
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

        notificationModels = new ArrayList<>();
        adapterNotification = new AdapterNotification(this, notificationModels);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewRecyclerView.setAdapter(adapterNotification);

        viewModel.NotiesMutableLiveData.observe(this, notificationsBeans -> {
            if (notificationsBeans != null && !notificationsBeans.isEmpty()){
                adapterNotification.getNotificationModel().clear();
                adapterNotification.setNotificationModel(notificationsBeans);
                adapterNotification.notifyDataSetChanged();
            }else {
                nestedScrollView.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
