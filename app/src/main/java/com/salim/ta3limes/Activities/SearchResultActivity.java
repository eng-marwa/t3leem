package com.salim.ta3limes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.salim.ta3limes.Adapters.AdapterSearchResults;
import com.salim.ta3limes.Models.response.CourseMp3ListFiles;
import com.salim.ta3limes.Models.response.LibraryModelResponse;
import com.salim.ta3limes.Models.response.MP3FilesModelResponse;
import com.salim.ta3limes.Models.response.SearchResultModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.ContantsUtils;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.Mp3FilesViewModel;
import com.salim.ta3limes.viewmodels.MyLibraryViewModel;
import com.salim.ta3limes.viewmodels.SearchViewModel;
import com.salim.ta3limes.viewmodels.factory.LibraryModelFactory;
import com.salim.ta3limes.viewmodels.factory.MP3ListModelFactory;
import com.salim.ta3limes.viewmodels.factory.SearchModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity implements AdapterSearchResults.onItemClicked {

    EditText search;
    TextView tv_result;
    ImageView ivbClose;
    RecyclerView rv_search;
    MKLoader search_loading;
    SearchViewModel mViewModel;
    List<SearchResultModelResponse.Result> results;
    ArrayList<LibraryModelResponse.Course> mList;
    List<MP3FilesModelResponse.Course> mp3List;
    LinearLayoutManager linearLayoutManager;
    AdapterSearchResults adapterSearchResults;

    private MyLibraryViewModel myLibraryViewModel;
    private Mp3FilesViewModel mp3FilesViewModel;

    String keywords, type;
    String pdfCode, mp3Code;

    SharedPreferencesUtilities preferencesUtilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtilities = new SharedPreferencesUtilities(this);
        pdfCode = preferencesUtilities.getPdfCode();
        mp3Code = preferencesUtilities.getMp3Code();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_search_result);

        mList = new ArrayList<>();
        results = new ArrayList<>();
        mViewModel = new ViewModelProvider(this, new SearchModelFactory(this, results)).get(SearchViewModel.class);
        myLibraryViewModel = new ViewModelProvider(this, new LibraryModelFactory(this, mList)).get(MyLibraryViewModel.class);
        mp3FilesViewModel = new ViewModelProvider(this, new MP3ListModelFactory(this, mp3List)).get(Mp3FilesViewModel.class);

        search = findViewById(R.id.et_search_result);
        tv_result = findViewById(R.id.tv_result);
        ivbClose = findViewById(R.id.ivbClose);
        rv_search = findViewById(R.id.rv_search);
        search_loading = findViewById(R.id.search_loading);

        ivbClose.setOnClickListener(v -> finish());

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapterSearchResults = new AdapterSearchResults(this, results);
        rv_search.setLayoutManager(linearLayoutManager);
        rv_search.setAdapter(adapterSearchResults);

        type = getIntent().getStringExtra("type");

        Log.e("SearchResultActivity", "onCreate: Type >> " + type);

        search.setOnEditorActionListener((v, actionId, event) -> {
            if (v.getText().equals("")) {
                rv_search.setVisibility(View.GONE);
                search_loading.setVisibility(View.GONE);
            } else {
                keywords = v.getText().toString();
                mViewModel.getResults(keywords, type);
            }
            return true;
        });

        mViewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                search_loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                search_loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        mViewModel.checkUser();
        mViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        mViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });

        mViewModel.searchResult.observe(this, results1 -> {
            if (results1 != null && !results1.isEmpty() && results1.size() > 0) {
                adapterSearchResults.getSearchModel().clear();
                adapterSearchResults.setSearchModel(results1);
            } else {
                rv_search.setVisibility(View.GONE);
                tv_result.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void itemClicked(int id, String name, String phone, String whatsapp) {

        if (ContantsUtils.TYPE.equals("videos")) {
            Intent intent = new Intent(this, CourseVideosActivity.class);
            intent.putExtra("courseTitle", name);
            intent.putExtra("students", phone);
            intent.putExtra("courseId", id);
            startActivity(intent);
            finish();
        } else if (ContantsUtils.TYPE.equals("library")) {
            Log.e("MyLibraryFragment", "itemClicked: >>" + id);
            if (pdfCode != null && !pdfCode.isEmpty()) {
                Intent intent = new Intent(this, PdfAndMp3Activity.class);
                intent.putExtra("courseId", id);
                intent.putExtra("courseCode", preferencesUtilities.getPdfCode());
                intent.putExtra("courseName", name);
                startActivity(intent);
                finish();
            } else {
                Log.e("SearchResults", "itemClicked: >>" + id);
                View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);

                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);
                EditText codeEditText = dialog.findViewById(R.id.etCode);
                Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
                TextView tvWhatsApp = dialog.findViewById(R.id.tvWhatsApp);
                TextView tvPhone = dialog.findViewById(R.id.tvPhone);
                dialog.show();

                btnSubmit.setOnClickListener(v -> {
                    String code = codeEditText.getText().toString();
                    Log.e("MP3FilesFragment", "itemClicked: Code >> " + code);
                    if (ContantsUtils.TYPE.equals("library"))
                        myLibraryViewModel.getPdfOneCourse(id + "", code);
                    else
                        mp3FilesViewModel.getMp3OneCourse(id + "", code);
                    myLibraryViewModel.message.observe(this, s -> {
                        if (s.equals("success")) {
                            preferencesUtilities.setPdfCode(code);
                            Intent intent = new Intent(this, PdfAndMp3Activity.class);
                            intent.putExtra("courseId", id);
                            intent.putExtra("courseCode", code);
                            startActivity(intent);
                            finish();
                        }
                    });
                });

                tvWhatsApp.setOnClickListener(v ->
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+2" + whatsapp))));

                tvPhone.setOnClickListener(v ->
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phone))));
            }
        }else if (ContantsUtils.TYPE.equals("fileVoice")){
            Log.e("MyLibraryFragment", "itemClicked: >>" + id);
            if (mp3Code != null && !mp3Code.isEmpty()) {
                Intent intent = new Intent(this, PdfAndMp3Activity.class);
                intent.putExtra("courseId", id);
                intent.putExtra("courseCode", preferencesUtilities.getMp3Code());
                intent.putExtra("courseName", name);
                startActivity(intent);
                finish();
            } else {
                Log.e("SearchResults", "itemClicked: >>" + id);
                View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);

                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);
                EditText codeEditText = dialog.findViewById(R.id.etCode);
                Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
                TextView tvWhatsApp = dialog.findViewById(R.id.tvWhatsApp);
                TextView tvPhone = dialog.findViewById(R.id.tvPhone);
                dialog.show();

                btnSubmit.setOnClickListener(v -> {
                    String code = codeEditText.getText().toString();
                    Log.e("MP3FilesFragment", "itemClicked: Code >> " + code);
                    if (ContantsUtils.TYPE.equals("library"))
                        myLibraryViewModel.getPdfOneCourse(id + "", code);
                    else
                        mp3FilesViewModel.getMp3OneCourse(id + "", code);
                    myLibraryViewModel.message.observe(this, s -> {
                        if (s.equals("success")) {
                            preferencesUtilities.setMp3Code(code);
                            Intent intent = new Intent(this, PdfAndMp3Activity.class);
                            intent.putExtra("courseId", id);
                            intent.putExtra("courseCode", code);
                            startActivity(intent);
                            finish();
                        }
                    });
                });

                tvWhatsApp.setOnClickListener(v ->
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+2" + whatsapp))));

                tvPhone.setOnClickListener(v ->
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phone))));
            }
        }
    }
}