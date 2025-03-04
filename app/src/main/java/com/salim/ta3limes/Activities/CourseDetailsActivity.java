package com.salim.ta3limes.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.DefaultLoadControl;

import com.google.android.exoplayer2.LoadControl;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.CourseDetailsViewModel;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CourseDetailsActivity extends AppCompatActivity {

    CourseDetailsViewModel mViewModel;

    TextView tvName, tvTeacher, tvFaculty, tvWhatsapp, tvPhone, tvCourseName;
    MKLoader loading;
    VideoView videoView;
    ImageView ivPlay, back;
    boolean mVideoRun = false;

    String access_Token = "c435106d15cceac1336a27fd52251264";
    String title, videoUrl;

    String token, phone, whatsApp, videoID, courseID, quality, Url;

    SharedPreferencesUtilities preferencesUtilities;

    private MediaSource mediaSource;
    private ExtractorsFactory extractorsFactory;
   // private DefaultHttpDataSourceFactory factory;
    private TrackSelector trackSelector;
    private BandwidthMeter bandwidthMeter;
    private LoadControl loadControl;
    private Uri videoUri;
    MediaController mediaController;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_course_details);

        mViewModel = new ViewModelProvider(this).get(CourseDetailsViewModel.class);

        preferencesUtilities = new SharedPreferencesUtilities(this);
        token = preferencesUtilities.getToken();

        courseID = getIntent().getStringExtra("courseId");

        tvCourseName = findViewById(R.id.tvCourseName);
        tvName = findViewById(R.id.txt_name);
        tvTeacher = findViewById(R.id.teacher_name);
        tvFaculty = findViewById(R.id.tvFaculty);
        tvWhatsapp = findViewById(R.id.btnWhatsApp);
        tvPhone = findViewById(R.id.btnPhone);
        videoView = findViewById(R.id.vvPlayer);

      ivPlay = findViewById(R.id.ivPlay);
        back = findViewById(R.id.back);
        loading = findViewById(R.id.loading);

        back.setOnClickListener(v -> finish());

        mViewModel.courseName.observe(this, s -> {
            if (s != null) {
                tvName.setText(s);
                tvCourseName.setText(s);
            }
        });

        mViewModel.checkUser(token, preferencesUtilities.getPHONE(),preferencesUtilities.getToken());
        mViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        mViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });

        mViewModel.courseTeacher.observe(this, s -> {
            if (s != null) tvTeacher.setText(s);
        });

        mViewModel.courseOrganisation.observe(this, s -> {
            if (s != null) tvFaculty.setText(s);
        });

        mViewModel.videoId.observe(this, s -> {
            if (s != null) videoID = s;
            Log.e("TAG", "onCreate: VideoID >> " + videoID);
            Url = ("https://player.vimeo.com/video/" + videoID + "/config");
            getUrl(Url);
        });

        mViewModel.phone.observe(this, s -> {
            if (s != null) phone = s;
            tvPhone.setOnClickListener(v ->
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phone))));
        });

        mViewModel.whatsapp.observe(this, s -> {
            if (s != null) whatsApp = s;
            tvWhatsapp.setOnClickListener(v ->
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+2" + whatsApp))));
        });

        mViewModel.getCourseDetails(token, courseID,preferencesUtilities);

        loadControl = new DefaultLoadControl();
        bandwidthMeter = new DefaultBandwidthMeter();
       // trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
      //  factory = new DefaultHttpDataSourceFactory("exoplayer_video");
        extractorsFactory = new DefaultExtractorsFactory();
    }


    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getUrl(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject requests = jsonObject.getJSONObject("request");
                JSONObject files = requests.getJSONObject("files");
                JSONArray progressiveArray = files.getJSONArray("progressive");
                for (int i = 0; i < progressiveArray.length(); i++) {
                    JSONObject finalObject = progressiveArray.getJSONObject(i);
                    quality = finalObject.getString("quality");

                    if (quality.equals("360p")) {
                        videoUrl = finalObject.getString("url");
                        videoUri = Uri.parse(videoUrl);
                        runVideo();
                    }
                    if (quality.equals("540p")) {
                        videoUrl = finalObject.getString("url");
                        videoUri = Uri.parse(videoUrl);
                        runVideo();
                    }
                    if (quality.equals("720p")) {
                        videoUrl = finalObject.getString("url");
                        videoUri = Uri.parse(videoUrl);
                        runVideo();
                    }

                    ivPlay.setOnClickListener(v -> playAndPause());
                    videoView.setOnClickListener(v -> playAndPause());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + access_Token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void playAndPause() {
        mVideoRun = !mVideoRun;
        if (mVideoRun) {
            ivPlay.setVisibility(View.INVISIBLE);
            videoView.start();
        } else {
            ivPlay.setVisibility(View.VISIBLE);
            videoView.pause();
        }
    }

    private void runVideo() {
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
    }
}
