package com.salim.ta3limes.Fragments;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Activities.NotificationActivity;
import com.salim.ta3limes.Activities.SearchResultActivity;

import com.salim.ta3limes.Adapters.AdapterCenterCourses;
import com.salim.ta3limes.Adapters.AdapterSpinnerCenters;
import com.salim.ta3limes.Adapters.AdapterSuggestionCourses;
import com.salim.ta3limes.Models.response.CenterCoursesModelResponse;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.ContantsUtils;
import com.salim.ta3limes.utilities.CustomEditText;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.CoursesViewModel;
import com.salim.ta3limes.viewmodels.factory.MainModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class CoursesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "CoursesFragment";
    private CoursesViewModel mViewModel;
    String access_Token = "c435106d15cceac1336a27fd52251264";
    private int RECORD_AUDIO_REQUEST_CODE = 123;
    ImageButton notification, search;
    CircleImageView imgProfile;
    TextView nameTextView;
    CustomEditText centerNameEditText;
    Spinner centerNameSpinner;
    FrameLayout centerNameFrameLayout;
    TextView coursesNumber;
    RecyclerView recyclerViewRecyclerView, rvSuggestions;
    MKLoader loading;
    RelativeLayout relativeLayout;

    ArrayList<PersonalDataModelResponse.DataBean.CentersBean> centersBeans;
    ArrayList<CenterCoursesModelResponse.DataBean.CourseBean> courseBeans;
    AdapterSpinnerCenters adapterSpinnerCenters;
    AdapterCenterCourses adapterCenterCourses;
    AdapterSuggestionCourses adapterSuggestionCourses;
    LinearLayoutManager linearLayoutManager;
    SharedPreferencesUtilities preferencesUtilities;

    String token;
    int centerId = 0;
    public int position = 4;
    String deviceName = android.os.Build.MODEL;
    String deviceMan = android.os.Build.MANUFACTURER;
    String build = android.os.Build.PRODUCT;
    String firebaseToken, HuawieToken;

    public static CoursesFragment newInstance() {
        return new CoursesFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getPermissionToRecordAudio();
     //   requestPermissions();
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "Cairo-Regular.ttf", true);

        preferencesUtilities = new SharedPreferencesUtilities(getActivity());
        token = preferencesUtilities.getToken();
        firebaseToken = preferencesUtilities.getRegToken();
        HuawieToken = preferencesUtilities.getHuawie_TOKEN();

        View rootView = inflater.inflate(R.layout.courses_fragment, container, false);

        Log.e(TAG, "onCreateView: centerId >> " + centerId);

        recyclerViewRecyclerView = rootView.findViewById(R.id.recyclerView_RecyclerView);
//        rvSuggestions = rootView.findViewById(R.id.rvSuggestions);
        loading = rootView.findViewById(R.id.loading);
//        navView = rootView.findViewById(R.id.nav_view_profile);
        notification = rootView.findViewById(R.id.notification);
        search = rootView.findViewById(R.id.search);
        imgProfile = rootView.findViewById(R.id.img_profile);
        nameTextView = rootView.findViewById(R.id.name_textView);
        centerNameEditText = rootView.findViewById(R.id.center_name_EditText);
        centerNameSpinner = rootView.findViewById(R.id.center_name_Spinner);
        centerNameFrameLayout = rootView.findViewById(R.id.center_name_FrameLayout);
        coursesNumber = rootView.findViewById(R.id.courses_number);
        relativeLayout = rootView.findViewById(R.id.non_courses_layout);

        notification.setOnClickListener(v -> startActivity(new Intent(getActivity(), NotificationActivity.class)));

        search.setOnClickListener(v -> {
            ContantsUtils.TYPE = "videos";
            startActivity(new Intent(getActivity(), SearchResultActivity.class).putExtra("type", "videos"));
        });

        centersBeans = new ArrayList<>();

        Log.i(TAG, "get deviceName:" + deviceName);
        Log.i(TAG, "get deviceMan:" + deviceMan);
        Log.i(TAG, "get build:" + build);

        mViewModel = new ViewModelProvider(this, new MainModelFactory(getActivity(), centersBeans)).get(CoursesViewModel.class);

        if (loginUser()) {
            mViewModel.getInfo();
            if (deviceMan.toLowerCase().contains("huawei") || deviceMan.toLowerCase().contains("HUAWEI") || deviceMan.toLowerCase().contains("honor"))
                mViewModel.UpdateToken(HuawieToken, deviceMan);
            else
                mViewModel.UpdateToken(firebaseToken, "android");
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

        mViewModel.progress.observe(getActivity(), aBoolean -> {
            if (getActivity() == null) {
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

        mViewModel.name.observe(getActivity(), s -> {
            if (getActivity() == null) {
                return;
            }
            nameTextView.setText(s);
        });

        mViewModel.checkUser();
        mViewModel.message.observe(getActivity(), s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        mViewModel.blocked.observe(getActivity(), aBoolean -> {
            if (getActivity() == null) {
                return;
            }
            if (aBoolean) checkLogin();
        });

        mViewModel.image.observe(getActivity(), s -> {
            if (getActivity() == null) {
                return;
            }
            Glide.with(this)
                    .load(s)
                    .error(R.drawable.profile_man)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imgProfile);
        });

        mViewModel.beanLiveData.observe(getActivity(), centersBeans1 -> {
            if (getActivity() == null) {
                return;
            }
            adapterSpinnerCenters.setDataBeanList(centersBeans1);
        });

        centerNameSpinner.setOnItemSelectedListener(this);

        adapterSpinnerCenters = new AdapterSpinnerCenters(getActivity(), centersBeans);
        centerNameSpinner.setAdapter(adapterSpinnerCenters);

        courseBeans = new ArrayList<>();
        adapterCenterCourses = new AdapterCenterCourses(getActivity(), courseBeans);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewRecyclerView.setAdapter(adapterCenterCourses);

//        courseBeans = new ArrayList<>();
//        adapterSuggestionCourses = new AdapterSuggestionCourses(getActivity(), courseBeans);
//        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
//        rvSuggestions.setLayoutManager(linearLayoutManager);
//        rvSuggestions.setAdapter(adapterSuggestionCourses);

        mViewModel.listMutableLiveData.observe(getActivity(), courseBeans1 -> {
            if (getActivity() == null) {
                return;
            }
            if (courseBeans1 != null && !courseBeans1.isEmpty()) {
                adapterCenterCourses.getCourseBeans().clear();
                adapterCenterCourses.setCarsList(courseBeans1);
            } else {
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.coursesnumber.observe(getActivity(), s -> {
            if (getActivity() == null) {
                return;
            }
            if (s.equals("0"))
                relativeLayout.setVisibility(View.VISIBLE);
            else
                coursesNumber.setText(s);
        });
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == centerNameSpinner.getId()) {
            centerNameEditText.setText(adapterSpinnerCenters.getDataBeanList().get(position).getName());
            centerId = adapterSpinnerCenters.getDataBeanList().get(position).getId();

            mViewModel.getCenterCourses(centerId);
            preferencesUtilities.setCenterId(String.valueOf(centerId));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private boolean loginUser() {
        if (preferencesUtilities.getToken() != null && !preferencesUtilities.getToken().isEmpty() && preferencesUtilities.getLoggedIn())
            return true;
        else return false;

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

//    private final ActivityResultLauncher<String> requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (!isGranted) {
//                    Toast.makeText(requireContext(), "يجب الموافقة علي الاذن السابق لاكمال استخدام التطبيق ", Toast.LENGTH_LONG).show();
//                    requireActivity().finishAffinity();
//                }
//            });

//    private void requestPermissions() {
//        List<String> permissionsToRequest = new ArrayList<>();
//        permissionsToRequest.add(Manifest.permission.RECORD_AUDIO);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//
//            permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES);
//            permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO);
//        } else {
//            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        requestPermissionLauncher.launch(String.valueOf(permissionsToRequest));
//    }

    public void getPermissionToRecordAudio() {
        List<String> permissionsToRequest = new ArrayList<>();
        permissionsToRequest.add(Manifest.permission.RECORD_AUDIO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES);
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO);
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(permissionsToRequest.toArray(new String[0]),
                        RECORD_AUDIO_REQUEST_CODE);

            }
        } else {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(permissionsToRequest.toArray(new String[0]),
                        RECORD_AUDIO_REQUEST_CODE);

            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(requireContext(), "يجب الموافقة علي الاذن السابق لاكمال استخدام التطبيق ", Toast.LENGTH_LONG).show();
                requireActivity().finishAffinity();
            }
        }

    }

}
