package com.salim.ta3limes.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Models.response.UpdateFireBaseTokenModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.ProfileViewModel;
import com.salim.ta3limes.viewmodels.factory.ProfileModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "ProfileFragment";
    TextView personalTv;
    TextView changeTv;
    TextView aboutTv;
    TextView complainTv;
    TextView termsTv;
    TextView logoutTv;
    BottomNavigationView navViewProfile;
    MKLoader loading;

    public int position = 0;
    String platform = "android";

    private ProfileViewModel mViewModel;
    SharedPreferencesUtilities preferencesUtilities;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "Cairo-Regular.ttf", true);

        preferencesUtilities = new SharedPreferencesUtilities(getActivity());
//        position = getArguments().getInt("position");
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        Log.e(TAG, "init: profile >> ");
        personalTv = rootView.findViewById(R.id.personal_tv);
        changeTv = rootView.findViewById(R.id.change_tv);
        aboutTv = rootView.findViewById(R.id.about_tv);
        complainTv = rootView.findViewById(R.id.complain_tv);
        termsTv = rootView.findViewById(R.id.terms_tv);
        logoutTv = rootView.findViewById(R.id.logout_tv);

        personalTv.setOnClickListener(this);
        changeTv.setOnClickListener(this);
        aboutTv.setOnClickListener(this);
        complainTv.setOnClickListener(this);
        termsTv.setOnClickListener(this);
        logoutTv.setOnClickListener(this);

//        navViewProfile = rootView.findViewById(R.id.nav_view_profile);
        loading = rootView.findViewById(R.id.loading);

        mViewModel = new ViewModelProvider(getActivity(), new ProfileModelFactory(getActivity())).get(ProfileViewModel.class);

        mViewModel.checkUser();
        mViewModel.message.observe(getActivity(), s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق")) {
                checkLogin();
            }else if (s.equals("ﺗﻢ ﺗﺴﺠﻴﻞ اﻟﺨﺮﻭﺝ ﺑﻨﺠﺎﺡ")){
                checkLogin();
            }
        });

        mViewModel.blocked.observe(getActivity(), aBoolean -> {
            if (aBoolean) checkLogin();
        });

        mViewModel.progress.observe(getActivity(), aBoolean -> {
            if (getActivity() == null){
                return;
            }
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                loading.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_tv:
                mViewModel.gotoPersonalData();
                break;
            case R.id.change_tv:
                mViewModel.gotoChangePassword();
                break;
            case R.id.about_tv:
                mViewModel.gotoAboutUs();
                break;
            case R.id.complain_tv:
                mViewModel.gotoSendComplain();
                break;
            case R.id.terms_tv:
                mViewModel.gotoTerms();
                break;
            case R.id.logout_tv:
                mViewModel.logOut();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mViewModel.checkUser();

        mViewModel.blocked.observe(getActivity(), aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }

    private void checkLogin() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
