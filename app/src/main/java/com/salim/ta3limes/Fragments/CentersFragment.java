package com.salim.ta3limes.Fragments;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Adapters.AdapterCenters;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.CentersViewModel;
import com.salim.ta3limes.viewmodels.factory.CentersModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

public class CentersFragment extends Fragment {

    public static final String TAG = "CentersFragment";
    private CentersViewModel mViewModel;
    ArrayList<PersonalDataModelResponse.DataBean.CentersBean> centersBeans;
    AdapterCenters adapterCenters;
    LinearLayoutManager linearLayoutManager;
//    BottomNavigationView navView;
    TextView msg_tv;
    RecyclerView recyclerViewRecyclerView;
    NestedScrollView nestedScrollView;
    MKLoader loading;
    SharedPreferencesUtilities preferencesUtilities;

   public int position = 1;
   String centerId;

    public static CentersFragment newInstance() {
        return new CentersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "Cairo-Regular.ttf", true);

        preferencesUtilities = new SharedPreferencesUtilities(getActivity());
        centerId = preferencesUtilities.getCenterId();

        View rootView = inflater.inflate(R.layout.centers_fragment, container, false);

        Log.e(TAG, "init: centers >> ");
        msg_tv = rootView.findViewById(R.id.msg_tv);
        nestedScrollView = rootView.findViewById(R.id.nestedScrollView);
        recyclerViewRecyclerView = rootView.findViewById(R.id.recyclerView_RecyclerView);
        loading = rootView.findViewById(R.id.loading);
//        navView = rootView.findViewById(R.id.nav_view_profile);

        centersBeans = new ArrayList<>();
        mViewModel = new ViewModelProvider(getActivity(), new CentersModelFactory(getActivity(), centersBeans)).get(CentersViewModel.class);
        mViewModel.getInfo();
        mViewModel.progress.observe(getActivity(), aBoolean -> {
            if (getActivity() == null){
                return;
            }
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }else {
                loading.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        adapterCenters = new AdapterCenters(getActivity(), centersBeans);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewRecyclerView.setAdapter(adapterCenters);

        mViewModel.beanLiveData.observe(getActivity(), centersBeans1 -> {
            if (getActivity() == null){
                return;
            }
            if (centersBeans1 != null && !centersBeans1.isEmpty()) {
                adapterCenters.getCentersBeans().clear();
                adapterCenters.setCentersBeans(centersBeans1);
                adapterCenters.notifyDataSetChanged();
            }else {
                nestedScrollView.setVisibility(View.GONE);
                msg_tv.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.checkUser();
        mViewModel.message.observe(getActivity(), s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        mViewModel.blocked.observe(getActivity(), aBoolean -> {
            if (getActivity() == null){
                return;
            }
            if (aBoolean) checkLogin();
        });

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.checkUser();
        mViewModel.message.observe(getActivity(), s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

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
