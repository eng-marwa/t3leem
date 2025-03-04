package com.salim.ta3limes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.salim.ta3limes.Adapters.AdapterCourseVideos;
import com.salim.ta3limes.Fragments.DataFragment;
import com.salim.ta3limes.Fragments.VideosFragment;
import com.salim.ta3limes.Models.response.CoursePdfListModel;
import com.salim.ta3limes.Models.response.CourseVideosModelResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.ContantsUtils;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.ProfileViewModel;
import com.salim.ta3limes.viewmodels.factory.ProfileModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseVideosActivity extends AppCompatActivity {

    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;

    int courseID;

    String title;
    String students;

    List<CourseVideosModelResponse.Lesson> lessonsBeans;
    AdapterCourseVideos adapterCourseVideos;
    private String TAG = "CourseVideosActivity";
    private ProfileViewModel mViewModel;
    SharedPreferencesUtilities preferencesUtilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_videos);
        ButterKnife.bind(this);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Cairo-Regular.ttf", true);
        preferencesUtilities = new SharedPreferencesUtilities(this);

        lessonsBeans = new ArrayList<>();
        adapterCourseVideos = new AdapterCourseVideos(this, lessonsBeans);
        mViewModel = new ViewModelProvider(this, new ProfileModelFactory(this)).get(ProfileViewModel.class);

        Intent i = getIntent();
        if (i.getExtras() != null) {
            title = i.getStringExtra("courseTitle");
            students = i.getStringExtra("students");
            header.setText(title);
            courseID = i.getIntExtra("courseId", 0);

        }
        Log.i("ahmed",""+courseID);
        mViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(final ViewPager viewPager) {
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        final String[] names = {"الفيديوهات", "معلومات"};

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("level", "" + position);
            switch (position) {
                case 0:
                    Bundle bundle = new Bundle();
                    bundle.putInt("courseId", courseID);

                    bundle.putString("students", students);
                    VideosFragment videosFragment = new VideosFragment();
                    videosFragment.setArguments(bundle);
                    return videosFragment;

                case 1:
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("courseId", courseID);
                    DataFragment dataFragment = new DataFragment();
                    dataFragment.setArguments(bundle1);
                    return dataFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return names[position];
        }

    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("position", 0);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("position", 0);
        startActivity(i);
        finish();
    }

    public void checkUser() {
        mViewModel.checkUser();
//        ServiceBuilder builder = new ServiceBuilder();
//        ServiceInterfaces.UserBlocked userBlocked = builder.userBlocked();
//        Call<UserBlockedModelResponse.Root> call = userBlocked.userBlocked("Bearer " + preferencesUtilities.getToken(), preferencesUtilities.getPHONE());
//        call.enqueue(new Callback<UserBlockedModelResponse.Root>() {
//            @Override
//            public void onResponse(Call<UserBlockedModelResponse.Root> call, Response<UserBlockedModelResponse.Root> response) {
//                Log.e(TAG, "onResponse: CheckBlocked >> " + response);
//                if (response.code() == 200) {
//                    boolean blocked = response.body().data.blocked;
//                    if (blocked) checkLogin();
//                } else if (response.code() == 400) {
//                    String errorororororr;
//                    try {
//                        errorororororr = response.errorBody().string();
//                        Log.e(TAG, "onResponse: Error >> " + errorororororr);
//                        JSONObject object = new JSONObject(errorororororr);
//                        String message = object.getString("message");
//                        if (message.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
//                            checkLogin();
//
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserBlockedModelResponse.Root> call, Throwable t) {
//
//            }
//        });
    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUser();
    }
}
