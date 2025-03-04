package com.salim.ta3limes.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Activities.PdfAndMp3Activity;
import com.salim.ta3limes.Activities.SearchResultActivity;
import com.salim.ta3limes.Adapters.AdapterCoursePDF;
import com.salim.ta3limes.Models.response.LibraryModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.ContantsUtils;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.MyLibraryViewModel;
import com.salim.ta3limes.viewmodels.factory.LibraryModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

public class MyLibraryFragment extends Fragment implements AdapterCoursePDF.onItemClicked {

    private MyLibraryViewModel mViewModel;
    public int position = 3;
    ImageButton ivbSearch;
    RecyclerView rvPdfFiles;
    LinearLayoutManager layoutManager;
    AdapterCoursePDF adapterCoursePDF;
    ArrayList<LibraryModelResponse.Course> mList;

    MKLoader loading;
    int courseID;
    String pdfCode;

    SharedPreferencesUtilities preferencesUtilities;

    public static MyLibraryFragment newInstance() {
        return new MyLibraryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        preferencesUtilities = new SharedPreferencesUtilities(getActivity());
        pdfCode = preferencesUtilities.getPdfCode();

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        View view = inflater.inflate(R.layout.my_library_fragment, container, false);

        ivbSearch = view.findViewById(R.id.ivbSearch);
        rvPdfFiles = view.findViewById(R.id.rvPdfFiles);
        loading = view.findViewById(R.id.loading);

        mList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        adapterCoursePDF = new AdapterCoursePDF(getActivity(), mList, this);
        rvPdfFiles.setLayoutManager(layoutManager);
        rvPdfFiles.setAdapter(adapterCoursePDF);

        mViewModel = new ViewModelProvider(getActivity(), new LibraryModelFactory(getActivity(), mList)).get(MyLibraryViewModel.class);

        ivbSearch.setOnClickListener(v -> {
            ContantsUtils.TYPE = "library";
            startActivity(new Intent(getActivity(), SearchResultActivity.class).putExtra("type", "library"));
        });

        mViewModel.getCoursesPDF();

        mViewModel.checkUser();
        mViewModel.message.observe(getActivity(), s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
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
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                loading.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        mViewModel.courseList.observe(getActivity(), courseList -> {
            adapterCoursePDF.getCourseBeans().clear();
            adapterCoursePDF.setCourseList(courseList);
        });

        return view;
    }


    @Override
    public void itemClicked(int id, String name, String phone, String whatsapp) {

        Log.e("MyLibraryFragment", "itemClicked: >>" + id);
//        if (pdfCode != null && !pdfCode.isEmpty()) {
            Intent intent = new Intent(getActivity(), PdfAndMp3Activity.class);
            intent.putExtra("courseId", id);
            intent.putExtra("courseCode", preferencesUtilities.getPdfCode());
            intent.putExtra("courseName", name);
            startActivity(intent);
//        } else {
//            View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
//
//            BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
//            dialog.setContentView(view);
//            EditText codeEditText = dialog.findViewById(R.id.etCode);
//            Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
//            TextView tvWhatsApp = dialog.findViewById(R.id.tvWhatsApp);
//            TextView tvPhone = dialog.findViewById(R.id.tvPhone);
//            dialog.show();
//
//            btnSubmit.setOnClickListener(v -> {
//                String code = codeEditText.getText().toString();
//                Log.e("MyLibraryFragment", "itemClicked: Code >> " + code);
//                mViewModel.getPdfOneCourse(id + "", code);
//                mViewModel.message.observe(getActivity(), s -> {
//                    if (s.equals("success")) {
//                        preferencesUtilities.setPdfCode(code);
//                        Intent intent = new Intent(getActivity(), PdfAndMp3Activity.class);
//                        intent.putExtra("courseId", id);
//                        intent.putExtra("courseCode", code);
//                        intent.putExtra("courseName", name);
//                        intent.putExtra("position", 1);
//                        startActivity(intent);
//                    }
//                });
//                dialog.dismiss();
//            });
//
//            tvWhatsApp.setOnClickListener(v -> {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+2" + whatsapp)));
//                dialog.dismiss();
//            });
//
//            tvPhone.setOnClickListener(v -> {
//                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phone)));
//                dialog.dismiss();
//            });
//        }
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