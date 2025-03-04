package com.salim.ta3limes.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.Downloader;
import com.salim.ta3limes.utilities.FileDownloader;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.VideoPDFViewModel;
import com.salim.ta3limes.viewmodels.factory.VideoPDFModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PDFViewerActivity extends AppCompatActivity {

    public static final String TAG = "PDFViewerActivity";

    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.download)
    ImageView download;
    @BindView(R.id.rel_top)
    RelativeLayout relTop;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.loading)
    MKLoader loading;
    @BindView(R.id.student_id)
    TextView studentId;
    @BindView(R.id.student_name)
    TextView studentName;

    Integer pageNumber = 0;
    String pdfFileName;

    String video_ID, title, name, student_ID, fileLink, txtDownload;
    String Url = "";
    VideoPDFViewModel viewModel;

    SharedPreferencesUtilities preferencesUtilities;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_pdfviewer);
        ButterKnife.bind(this);

        preferencesUtilities = new SharedPreferencesUtilities(this);
        name = preferencesUtilities.getUserName();
        student_ID = preferencesUtilities.getUserId();

        studentId.setText("ID: " + student_ID);
        studentName.setText("Name: " + name);

        Intent i = getIntent();
        if (i.getExtras() != null) {
            fileLink = i.getStringExtra("fileLink");
            title = i.getStringExtra("title");
            txtDownload = i.getStringExtra("download");
            header.setText(title);
        }



        pdfFileName = fileLink;
        Log.e(TAG, "onCreate: fileUrl >> " + fileLink);
        new RetrivePDFfromUrl().execute(fileLink);

        if (txtDownload.equals("yes"))
            download.setVisibility(View.VISIBLE);
        else download.setVisibility(View.GONE);

    }

    public String getName() {
        String name = "";
        String url = pdfFileName;
        name = url.substring(url.lastIndexOf('/') + 1);
        return name;
    }

    @OnClick({R.id.back, R.id.download})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                finish();
                break;
            case R.id.download:
                new DownloadFile().execute(pdfFileName, getName());
                break;
        }
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
        }
    }

    public void checkUser() {
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.UserBlocked userBlocked = builder.userBlocked();
        Call<UserBlockedModelResponse.Root> call = userBlocked.userBlocked("Bearer " + preferencesUtilities.getToken(), preferencesUtilities.getPHONE());
        call.enqueue(new Callback<UserBlockedModelResponse.Root>() {
            @Override
            public void onResponse(Call<UserBlockedModelResponse.Root> call, Response<UserBlockedModelResponse.Root> response) {
                Log.e(TAG, "onResponse: CheckBlocked >> " + response);
                if (response.code() == 200) {
                    boolean blocked = response.body().data.blocked;
                    if (blocked) checkLogin();

                }else if (response.code() == 400){
                    String errorororororr;
                    try {
                        errorororororr = response.errorBody().string();
                        Log.e(TAG, "onResponse: Error >> " + errorororororr);
                        JSONObject object = new JSONObject(errorororororr);
                        String message = object.getString("message");
                        if (message.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                            checkLogin();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserBlockedModelResponse.Root> call, Throwable t) {

            }
        });
    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Ta3lim-es Pdf");
            Log.e(TAG, "doInBackground: folder >> " + folder.getPath());
            if (!folder.exists())
                folder.mkdir();

            File pdfFile = new File(folder, fileName);
            if (!pdfFile.exists()) {
                try {
                    pdfFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(PDFViewerActivity.this, "تم تحميل الملف ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUser();
    }
}

