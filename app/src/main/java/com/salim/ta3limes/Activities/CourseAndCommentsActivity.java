
package com.salim.ta3limes.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.salim.ta3limes.Adapters.AdapterVideoComments;
import com.salim.ta3limes.Models.request.EditCommentRequest;
import com.salim.ta3limes.Models.response.VideoCommentsModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.CustomEditText;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.VideoDetailsViewModel;
import com.salim.ta3limes.viewmodels.factory.VideoDetailsModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseAndCommentsActivity extends AppCompatActivity implements AdapterVideoComments.ProgressMediaPlayer, PopupMenu.OnMenuItemClickListener, AdapterVideoComments.adapterInterface {


    @BindView(R.id.commentLayout)
    RelativeLayout commentLayout;
    @BindView(R.id.rel_top)
    RelativeLayout rel_top;
    @BindView(R.id.containerLayout)
    RelativeLayout containerLayout;
    @BindView(R.id.header)
    TextView header;

    @BindView(R.id.user_id)
    TextView user_id;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_id2)
    TextView userId2;
    @BindView(R.id.user_name2)
    TextView userName2;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.course_pdf)
    ImageView course_pdf;
    @BindView(R.id.playerView)
    PlayerView playerView;
    @BindView(R.id.prodress_bar)
    ProgressBar prodressBar;
    @BindView(R.id.record_imageView)
    ImageView recordImageView;
    @BindView(R.id.send_imageView)
    ImageView send_imageView;
    @BindView(R.id.img_profile)
    CircleImageView imgProfile;
    @BindView(R.id.comment_EditText)
    CustomEditText commentEditText;
    @BindView(R.id.recyclerView_RecyclerView)
    RecyclerView recyclerViewRecyclerView;
    @BindView(R.id.loading)
    MKLoader loading;
    YouTubePlayerView youtubePlayerView;
    ImageView youtubePlayerIcon;
    FloatingActionButton floatingActionButton;
    @BindView(R.id.youtubeWebView)
    WebView youtubeWebView;
    private SimpleExoPlayer.Builder builder;
    SimpleExoPlayer simpleExoPlayer;
    boolean flag = false;
    ImageView speedUp, speedDown, setting, ivQuality;
    String access_Token = "c435106d15cceac1336a27fd52251264";
    String video_ID, title, videoUrl, path, file_id, quality;
    int id;
    String Url = "";

    String pdfUrl = "";
    String pdfStatus = "";
    public static final String TAG = "CourseCommentsActivity";

    private boolean BLUETOOTH_HEADPHONE_CONNECT = false;
    private boolean Microphone_Plugged_in = false;
    private BroadcastReceiver broadcastReceiver;
    private MediaSource mediaSource;
    private ExtractorsFactory extractorsFactory;
    // private DefaultHttpDataSourceFactory factory;
    private TrackSelector trackSelector;
    private BandwidthMeter bandwidthMeter;
    private LoadControl loadControl;
    private Uri videoUri;
    private String url;
    private String webview_url;

    private int RECORD_AUDIO_REQUEST_CODE = 123;

    VideoDetailsViewModel videoDetailsViewModel;
    AdapterVideoComments adapterVideoComments;
    LinearLayoutManager linearLayoutManager;
    ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean> commentsBeans;
    SharedPreferencesUtilities preferencesUtilities;

    MediaItem mediaItem;
    Chronometer chronometer;
    ImageView imageViewPlay, imageViewClose;
    TextView imageViewStop;
    SeekBar seekBar;
    LinearLayout linearLayoutRecorder, linearLayoutPlay;
    TextView sendBtn, deleteBtn;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private Handler animHandle = new Handler();
    private Timer otherAppAudioTimer = new Timer();
    private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    MultipartBody.Part voiceFile;
    Animation slide_in, slide_out;
    private MediaPlayer mPlayer2;
    SeekBar seekBar2;
    BottomSheetDialog bottomSheetDialogForHeadSet;
    float speeding = 1f;

    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_course_and_comments);
        ButterKnife.bind(this);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(view -> {
            if (flag) {
                floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                rel_top.setVisibility(View.VISIBLE);
                commentLayout.setVisibility(View.VISIBLE);
                recyclerViewRecyclerView.setVisibility(View.VISIBLE);

                flag = false;
                animIDText();
            } else {
                floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                rel_top.setVisibility(View.GONE);
                commentLayout.setVisibility(View.GONE);
                recyclerViewRecyclerView.setVisibility(View.GONE);

                flag = true;
                animIDText();
            }


        });
        youtubePlayerView = findViewById(R.id.youtubePlayerView);
        youtubePlayerIcon = findViewById(R.id.youtube_player_fullscreen_icon);
        preferencesUtilities = new SharedPreferencesUtilities(this);
        Intent i = getIntent();
        if (i.getExtras() != null) {
            video_ID = i.getStringExtra("videoId");
            id = i.getIntExtra("id", 0);
            url = i.getStringExtra("url");
            webview_url = i.getStringExtra("webview_url");
            file_id = i.getStringExtra("fileId");
            title = i.getStringExtra("videoTitle");
            pdfUrl = i.getStringExtra("pdfUrl");
            pdfStatus = i.getStringExtra("pdfStatus");

            Log.e(TAG, "pdfUrl >> " + pdfUrl);
            Log.e(TAG, "pdfStatus >> " + pdfStatus);

            if (pdfStatus != null && pdfStatus.equals("yes"))
                course_pdf.setVisibility(View.VISIBLE);
            else course_pdf.setVisibility(View.INVISIBLE);

            Log.e(TAG, "onCreate: videoId >> " + video_ID);
        }
        getAudioRouteDeviceName();
        checkHeadPhone();
        builder = new SimpleExoPlayer.Builder(CourseAndCommentsActivity.this);

        builder.setSeekBackIncrementMs(10000);
        builder.setSeekForwardIncrementMs(10000);

        simpleExoPlayer = builder.build();


        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        slide_in = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        slide_out = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Cairo-Regular.ttf", true);


        String userId = preferencesUtilities.getUserId();
        String userName = preferencesUtilities.getUserName();
        String image = preferencesUtilities.getPROFILEIMAGE();
        file_id = preferencesUtilities.getFileId();
        Log.e(TAG, "onCreate: fileId >>> " + file_id);

        Glide.with(getApplicationContext())
                .load(image)
                .error(R.drawable.profile_man)
                .into(imgProfile);


        user_id.setText("ID: " + userId);
        user_name.setText("Name: " + userName);
        userId2.setText("ID: " + userId);
        userName2.setText("Name: " + userName);
        header.setText(title);

        animIDText();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         getPermissionToRecordAudio();
            //requestPermissions();
        }

//        btFullSreen = playerView.findViewById(R.id.bt_fullscreen);
        setting = playerView.findViewById(R.id.bt_setting);
        ivQuality = playerView.findViewById(R.id.bt_quality);

        commentsBeans = new ArrayList<>();
        videoDetailsViewModel = new ViewModelProvider(this, new VideoDetailsModelFactory(this, commentsBeans)).get(VideoDetailsViewModel.class);
        videoDetailsViewModel.GetComments(id);
        videoDetailsViewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        adapterVideoComments = new AdapterVideoComments(this, commentsBeans);
        adapterVideoComments.setListener(CourseAndCommentsActivity.this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewRecyclerView.setAdapter(adapterVideoComments);

        videoDetailsViewModel.checkUser();
        videoDetailsViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        videoDetailsViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });

        videoDetailsViewModel.max_views.observe(this, s -> {
            if (s.equals("0")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseAndCommentsActivity.this);
                builder.setMessage("لقد استنفذت عدد المشاهدات الخاصة بك");
                builder.setTitle("تحذير");
                builder.setNegativeButton("حسناً", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });
                builder.create();
                builder.show();
            } else {
                if (url == null) {
                    youtubePlayerView.setVisibility(View.GONE);
                    youtubePlayerIcon.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);
                    Url = ("https://player.vimeo.com/video/" + video_ID + "/config");
                    getUrl(Url);
                } else {
                    if (preferencesUtilities.getUserAirbodsStatus() != 1
                            && preferencesUtilities.getCourseAirbodsStatus() != 1) {
                        showYouTubeWebView();
                    }
//                    youtubePlayerView.setVisibility(View.VISIBLE);
//                    youtubePlayerIcon.setVisibility(View.VISIBLE);
//                    playerView.setVisibility(View.GONE);
//                    getLifecycle().addObserver(youtubePlayerView);
//                    List<String> youtubeId = Arrays.asList(url.split("="));
//                    youtubePlayerView.initializeWithWebUi(new YouTubePlayerListener() {
//                        @Override
//                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                            String videoId = youtubeId.get(1);
////                        String videoId ="txLL2l3yIV8";
//                            youTubePlayer.loadVideo(videoId, 0f);
//                            youTubePlayer.pause();
//                        }
//
//                        @Override
//                        public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState playerState) {
//
//                        }
//
//                        @Override
//                        public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {
//
//                        }
//
//                        @Override
//                        public void onPlaybackRateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackRate playbackRate) {
//
//                        }
//
//                        @Override
//                        public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError playerError) {
//
//                        }
//
//                        @Override
//                        public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float v) {
//
//                        }
//
//                        @Override
//                        public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float v) {
//
//                        }
//
//                        @Override
//                        public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float v) {
//
//                        }
//
//                        @Override
//                        public void onVideoId(@NonNull YouTubePlayer youTubePlayer, @NonNull String s) {
//
//                        }
//
//                        @Override
//                        public void onApiChange(@NonNull YouTubePlayer youTubePlayer) {
//
//                        }
//                    }, true);
//
//                    handelYoutubePlayer();
                }
                videoDetailsViewModel.mutableLiveData.observe(this, commentsBeans1 -> {
                    if (commentsBeans1 != null && commentsBeans1.size() > 0) {
                        adapterVideoComments.setComment((ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean>) commentsBeans1);
                    }
                });

                videoDetailsViewModel.status.observe(this, s1 -> {
                    Log.e(TAG, "onCreate: s >> " + s1);

                    if (s.equals("success")) {
                        Toast.makeText(this, "تم اضافة تعليقك بنجاح", Toast.LENGTH_SHORT).show();
                        commentEditText.setText("");
                        // videoDetailsViewModel.GetComments(id);
                        recyclerViewRecyclerView.scrollToPosition(commentsBeans.size() - 1);
                    }
                });
            }
        });

        videoDetailsViewModel.message.observe(this, s -> {
            if (s.equals("غير مسموح لك ..انت غير مشترك فى هذا الكورس"))
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            finish();
        });

        loadControl = new DefaultLoadControl();

        //  bandwidthMeter = new DefaultBandwidthMeter();

        //  trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        //simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        // factory = new DefaultHttpDataSourceFactory("exoplayer_video");

        extractorsFactory = new DefaultExtractorsFactory();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getUrl(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.e(TAG, "onResponse: VimeoResponse >> " + response.toString());

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
                        Log.e(TAG, "getUrl: URL >> " + videoUrl);

                        videoUri = Uri.parse(videoUrl);
                        //   mediaSource = new ExtractorMediaSource(videoUri, factory, extractorsFactory, null, null);

                        mediaItem = MediaItem.fromUri(videoUri);
                        simpleExoPlayer.addMediaItem(mediaItem);
                        simpleExoPlayer.prepare();
                    }
                    if (quality.equals("540p")) {
                        videoUrl = finalObject.getString("url");
                        Log.e(TAG, "getUrl: URL >> " + videoUrl);
                        videoUri = Uri.parse(videoUrl);
                        //  mediaSource = new ExtractorMediaSource(videoUri, factory, extractorsFactory, null, null);
                        mediaItem = MediaItem.fromUri(videoUri);
                        simpleExoPlayer.addMediaItem(mediaItem);
                        simpleExoPlayer.prepare();
                    }
                    if (quality.equals("720p")) {
                        videoUrl = finalObject.getString("url");
                        Log.e(TAG, "getUrl: URL >> " + videoUrl);
                        videoUri = Uri.parse(videoUrl);
                        //  mediaSource = new ExtractorMediaSource(videoUri, factory, extractorsFactory, null, null);
                        mediaItem = MediaItem.fromUri(videoUri);
                        simpleExoPlayer.addMediaItem(mediaItem);
                        simpleExoPlayer.prepare();
                    }
                    if (quality.equals("1080p")) {
                        videoUrl = finalObject.getString("url");
                        Log.e(TAG, "getUrl: URL >> " + videoUrl);
                        videoUri = Uri.parse(videoUrl);
                        //    mediaSource = new ExtractorMediaSource(videoUri, factory, extractorsFactory, null, null);
                        mediaItem = MediaItem.fromUri(videoUri);
                        simpleExoPlayer.addMediaItem(mediaItem);
                        simpleExoPlayer.prepare();
                    }
                    Log.e("Ahmed", "Ahmed");
                    if (preferencesUtilities.getCourseAirbodsStatus() == 0) {
                        playerView.setPlayer(simpleExoPlayer);
                        playerView.setKeepScreenOn(true);
                        simpleExoPlayer.setPlayWhenReady(true);
                    }

                    simpleExoPlayer.addListener(new Player.Listener() {
                        @Override
                        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                            if (playbackState == Player.STATE_BUFFERING) {
                                prodressBar.setVisibility(View.VISIBLE);
                            } else if (playbackState == Player.STATE_READY) {
                                prodressBar.setVisibility(View.GONE);
                                animIDText();
                            }
                            if (playbackState == Player.STATE_ENDED)
                                simpleExoPlayer.setPlaybackParameters(new PlaybackParameters(1f, 1f));
                        }
                    });


                    setting.setOnClickListener(v -> {
                        PopupMenu popup = new PopupMenu(CourseAndCommentsActivity.this, setting);

                        popup.getMenu().add("Normal");
                        popup.getMenu().add("1.25");
                        popup.getMenu().add("1.5");
                        popup.getMenu().add("2");

                        popup.setOnMenuItemClickListener(item -> {
                            String s = String.valueOf(item.getTitle());
                            if (s.equals("Normal")) s = "1";

                            speeding = Float.parseFloat(s);
                            //  pitching = Float.parseFloat(s);
                            simpleExoPlayer.setPlaybackParameters(new PlaybackParameters(speeding, 1f));
                            return true;

                        });

                        popup.show();
                    });

                    ivQuality.setOnClickListener(v -> {
                        //Creating the instance of PopupMenu
                        PopupMenu popupMenu = new PopupMenu(CourseAndCommentsActivity.this, ivQuality);

                        popupMenu.getMenu().add("360p");
                        popupMenu.getMenu().add("540p");
                        popupMenu.getMenu().add("720p");

                        popupMenu.setOnMenuItemClickListener(item -> {
                            String s1 = String.valueOf(item.getTitle());
                            Toast.makeText(this, "Quality is >> " + s1, Toast.LENGTH_SHORT).show();

                            if (s1.equals("360p")) {
                                quality = "360p";
                                try {
                                    videoUrl = finalObject.getString("url");
                                    Log.e(TAG, "getUrl: URL >> " + videoUrl);
                                    videoUri = Uri.parse(videoUrl);
                                    //  mediaSource = new ExtractorMediaSource(videoUri, factory, extractorsFactory, null, null);
                                    mediaItem = MediaItem.fromUri(videoUri);
                                    simpleExoPlayer.addMediaItem(mediaItem);
                                    simpleExoPlayer.prepare();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                playerView.setPlayer(simpleExoPlayer);
                                playerView.setKeepScreenOn(true);
                                simpleExoPlayer.setPlayWhenReady(true);


                            }
                            if (s1.equals("540p")) {
                                quality = "540p";
                                try {
                                    videoUrl = finalObject.getString("url");
                                    Log.e(TAG, "getUrl: URL >> " + videoUrl);
                                    videoUri = Uri.parse(videoUrl);
                                    //  mediaSource = new ExtractorMediaSource(videoUri, factory, extractorsFactory, null, null);
                                    mediaItem = MediaItem.fromUri(videoUri);
                                    simpleExoPlayer.addMediaItem(mediaItem);
                                    simpleExoPlayer.prepare();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                playerView.setPlayer(simpleExoPlayer);
                                playerView.setKeepScreenOn(true);
                                simpleExoPlayer.setPlayWhenReady(true);

                            }
                            if (s1.equals("720p")) {
                                quality = "720p";
                                try {
                                    videoUrl = finalObject.getString("url");
                                    Log.e(TAG, "getUrl: URL >> " + videoUrl);
                                    videoUri = Uri.parse(videoUrl);
                                    //    mediaSource = new ExtractorMediaSource(videoUri, factory, extractorsFactory, null, null);
                                    mediaItem = MediaItem.fromUri(videoUri);
                                    simpleExoPlayer.addMediaItem(mediaItem);
                                    simpleExoPlayer.prepare();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                playerView.setPlayer(simpleExoPlayer);
                                playerView.setKeepScreenOn(true);
                                simpleExoPlayer.setPlayWhenReady(true);
                            }

                            return true;

                        });

                        popupMenu.show(); //showing popup menu
                    });

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

    void handelYoutubePlayer() {
        youtubePlayerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                youtubePlayerView.toggleFullScreen();
                if (flag) {
                    youtubePlayerIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    rel_top.setVisibility(View.VISIBLE);
                    commentLayout.setVisibility(View.VISIBLE);
                    recyclerViewRecyclerView.setVisibility(View.VISIBLE);

                    flag = false;
                    animIDText();
                } else {
                    youtubePlayerIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    rel_top.setVisibility(View.GONE);
                    commentLayout.setVisibility(View.GONE);
                    recyclerViewRecyclerView.setVisibility(View.GONE);

                    flag = true;
                    animIDText();
                }
            }
        });

        youtubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(@NonNull YouTubePlayer youTubePlayer) {


            }
        });

    }


    private void checkHeadPhone() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.i("Ahmed", action);
                int i;
                Log.e("Ahmed", "Ahmed" + preferencesUtilities.getUserAirbodsStatus());
                Log.e("Ahmed", "Ahmed" + preferencesUtilities.getCourseAirbodsStatus());

                if (preferencesUtilities.getUserAirbodsStatus() == 1 || preferencesUtilities.getCourseAirbodsStatus() == 1) {
                    if (action.equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)) {
                        int state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, 0);
                        Log.i("Ahmed", "" + state);
                        switch (state) {
                            case BluetoothHeadset.A2DP: {
                                Log.i("Ahmed", "A2DP");
                                resumeYouTubeVideo();
                                break;
                            }
                            case BluetoothHeadset.STATE_DISCONNECTED: {
                                Log.i("Ahmed", "stop");
                                BLUETOOTH_HEADPHONE_CONNECT = false;
                                stopYouTubeVideo(true);
                            }
                        }


                    } else if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                        Log.i("Ahmed", "here4");
                        i = intent.getIntExtra("state", -1);
                        Log.i("Ahmed", "" + i);

                        if (BLUETOOTH_HEADPHONE_CONNECT) {
                            Log.i("Ahmed", "bluethooth");
                            resumeYouTubeVideo();
                        } else {
                            if (i == 0) {
                                Log.i("Ahmed", "stop");
                                stopYouTubeVideo(true);

                            }
                            if (i == 1) {
                                Log.i("Ahmed", "resume");
                                resumeYouTubeVideo();

                            }
                        }
                    }
                }


            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(broadcastReceiver, intentFilter);
    }
    private void stopYouTubeVideo(Boolean toast) {
        youtubeWebView.loadUrl("about:blank");
        Microphone_Plugged_in = false;
        if (toast)
            showDialogForHeadSet();
    }

    private void resumeYouTubeVideo() {
        youtubeWebView.setVisibility(View.VISIBLE);
        youtubePlayerIcon.setVisibility(View.GONE);
        youtubeWebView.requestFocus();
        youtubeWebView.getSettings().setLightTouchEnabled(true);
        youtubeWebView.getSettings().setJavaScriptEnabled(true);
        youtubeWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        youtubeWebView.setSoundEffectsEnabled(true);
        youtubeWebView.getSettings().setAllowFileAccess(true);
        youtubeWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        youtubeWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        youtubeWebView.setWebChromeClient(new WebChromeClient());
        youtubeWebView.setWebViewClient(new WebViewClient());
        youtubeWebView.getSettings().setDomStorageEnabled(true);
        if (webview_url != null && !webview_url.equals(""))
            youtubeWebView.loadUrl(webview_url);
        Microphone_Plugged_in = true;
        for (int i = 0; i < 3; i++) {
            if (bottomSheetDialogForHeadSet != null)
                bottomSheetDialogForHeadSet.dismiss();
        }

    }
    private void stopTheVideo(Boolean toast) {
        playerView.setPlayer(null);
        playerView.setKeepScreenOn(false);
        simpleExoPlayer.setPlayWhenReady(false);

        Microphone_Plugged_in = false;
        if (toast)
            showDialogForHeadSet();
    }

    private void resumeTheVideo() {
        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.setPlayWhenReady(true);
        Microphone_Plugged_in = true;
        for (int i = 0; i < 3; i++) {
            if (bottomSheetDialogForHeadSet != null)
                bottomSheetDialogForHeadSet.dismiss();
        }

    }
// onWindowFocusChanged will be executed
// every time when user taps on notifications
// while your app is in foreground.

//    private final ActivityResultLauncher<String> requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (!isGranted) {
//                    Toast.makeText(this, "يجب الموافقة علي الاذن السابق لاكمال استخدام التطبيق ", Toast.LENGTH_LONG).show();
//                   finishAffinity();
//                }
//            });

//    private void requestPermissions() {
//        List<String> permissionsToRequest = new ArrayList<>();
//        permissionsToRequest.add(Manifest.permission.RECORD_AUDIO);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//        {
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(permissionsToRequest.toArray(new String[0]),
                        RECORD_AUDIO_REQUEST_CODE);

            }
        } else {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsToRequest.toArray(new String[0]),
                            RECORD_AUDIO_REQUEST_CODE);
                }

            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

    }



    @Override
    protected void onResume() {
        super.onResume();

        animIDText();
        videoDetailsViewModel.checkUser();
        videoDetailsViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        videoDetailsViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });

    }


    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

//    private void setMicMuted() {
//        Boolean isMuted = false;
//        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//        if (!isMuted) {
//
//            if ((audioManager.getMode() == AudioManager.STREAM_MUSIC) || (audioManager.getMode() == AudioManager.STREAM_SYSTEM)) {
//                audioManager.setMicrophoneMute(true);
//            }
//            isMuted = true;
//
//        } else {
//
//            if ((audioManager.getMode() == AudioManager.STREAM_MUSIC) || (audioManager.getMode() == AudioManager.STREAM_SYSTEM)) {
//                audioManager.setMicrophoneMute(false);
//            }
//            isMuted = false;
//        }
//
//    }
//


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Toast.makeText(this, "Speed True", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    @OnClick({R.id.back, R.id.course_pdf, R.id.record_imageView, R.id.send_imageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.course_pdf:
                if (pdfUrl != null && !pdfUrl.isEmpty())
                    startActivity(new Intent(CourseAndCommentsActivity.this, PDFViewerActivity.class)
                            .putExtra("fileLink", pdfUrl).putExtra("download", pdfStatus)
                            .putExtra("title", title));
                break;
            case R.id.record_imageView:
                RecordVoiceComment();
                break;
            case R.id.send_imageView:
                String comment_type = "text";
                String comment = commentEditText.getText().toString();
                if (fileName != null) {

                    File file = new File(fileName);
                    try {
                        if (file.exists()) {
                            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                            voiceFile = MultipartBody.Part.createFormData("comment", file.getName(), requestBody);
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                    voiceFile = MultipartBody.Part.createFormData("comment", "", requestBody);
                }

                postComment(id, comment_type, comment, voiceFile);
                //  videoDetailsViewModel.PostComment(id, comment_type, comment, voiceFile);
                // videoDetailsViewModel.GetComments(id);
                recyclerViewRecyclerView.scrollToPosition(commentsBeans.size() - 1);

                break;

        }
    }

    private void RecordVoiceComment() {

        final Dialog dialog2 = new Dialog(CourseAndCommentsActivity.this);
        dialog2.setCancelable(false);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setContentView(R.layout.recording_row);
        dialog2.show();

        chronometer = dialog2.findViewById(R.id.chronometerTimer);
        seekBar = dialog2.findViewById(R.id.seekBar);
        linearLayoutRecorder = dialog2.findViewById(R.id.linearLayoutRecorder);
        linearLayoutPlay = dialog2.findViewById(R.id.linearLayoutPlay);
        deleteBtn = dialog2.findViewById(R.id.delete_btn);
        sendBtn = dialog2.findViewById(R.id.send_btn);
        imageViewPlay = dialog2.findViewById(R.id.imageViewPlay);
        imageViewStop = dialog2.findViewById(R.id.imageViewStop);
        imageViewClose = dialog2.findViewById(R.id.imageViewClose);

        prepareforRecording();
        startRecording();

        imageViewPlay.setOnClickListener(v -> {
            if (!isPlaying && fileName != null) {
                isPlaying = true;
                startPlaying();
            } else {
                isPlaying = false;
                stopPlaying();
            }
        });

        imageViewStop.setOnClickListener(v -> {
            prepareforStop();
            stopRecording();
        });

        imageViewClose.setOnClickListener(v -> {
            try {
                mRecorder.stop();
                mRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mRecorder = null;
            dialog2.dismiss();
        });

        deleteBtn.setOnClickListener(v -> {
            try {
                mRecorder.stop();
                mRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mRecorder = null;
            dialog2.dismiss();

        });

        sendBtn.setOnClickListener(v -> {
            prepareforStop();
            stopRecording();
            String commentType = "voice";
            String comment_text = "";
            if (fileName != null) {
                Log.e(TAG, "RecordVoiceComment: File_Name >> " + fileName);
                File file = new File(fileName);
                try {
                    if (file.exists()) {

                        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/*"), file);
                        voiceFile = MultipartBody.Part.createFormData("comment", file.getName(), requestBody);
                        Log.e(TAG, "RecordVoiceComment: Record_File_Name >> " + file.getName());
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), "");
                voiceFile = MultipartBody.Part.createFormData("comment", "", requestBody);
            }

            postComment(id, commentType, comment_text, voiceFile);
            recyclerViewRecyclerView.scrollToPosition(commentsBeans.size() - 1);
            dialog2.dismiss();
        });
    }

    private void prepareforRecording() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        linearLayoutPlay.setVisibility(View.GONE);
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/Ta3lim-es/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName = root.getAbsolutePath() + "/Ta3lim-es/Audios/" +
                String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename", fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        final int[] count = {-1};
        seekBar.setProgress(0);

        stopPlaying();

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(chronometer -> {
            count[0]++;
            if (count[0] == 59) {
                chronometer.stop();
                count[0] = 0;
                prepareforStop();
            }
        });
        chronometer.start();
    }

    private void stopPlaying() {
        try {
            mPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer = null;
        imageViewPlay.setImageResource(R.drawable.ic_play);
        chronometer.stop();
    }

    private void prepareforStop() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        linearLayoutPlay.setVisibility(View.VISIBLE);
    }

    private void stopRecording() {

        try {
            mRecorder.stop();
            mRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        imageViewPlay.setImageResource(R.drawable.ic_pause);

        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();
        chronometer.start();

        mPlayer.setOnCompletionListener(mp -> {
            imageViewPlay.setImageResource(R.drawable.ic_play);
            isPlaying = false;
            chronometer.stop();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null && fromUser) {
                    mPlayer.seekTo(progress);
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    Runnable runnable = () -> seekUpdation();

    private void seekUpdation() {
        if (mPlayer != null) {
            int mCurrentPosition = mPlayer.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private List<VideoCommentsModelResponse.DataBean.CommentsBean> getAllList
            (List<VideoCommentsModelResponse.DataBean.CommentsBean> oldList) {
        List<VideoCommentsModelResponse.DataBean.CommentsBean> list = new ArrayList<>();
        int size = oldList.size();
        boolean isAudio = false;
        for (int i = 0; i < size; i++) {
            list.add(oldList.get(i));
            if (oldList.get(i).getType().equals("voice")) {
                if (!isAudio) {
                    isAudio = true;
                } else {
                    list.remove(oldList.get(i));
                }
            }
        }
        return list;
    }

    Runnable runnable3 = () -> animIDText();

    private void animIDText() {
        userId2.startAnimation(slide_out);
        userName2.startAnimation(slide_out);
        mHandler.postDelayed(runnable3, 10000);
    }

    @Override
    protected void onDestroy() {
        adapterVideoComments.stopMedia();
        mHandler.removeCallbacks(runnable);
        mHandler.removeCallbacks(runnable2);
        mHandler.removeCallbacks(runnable3);
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
        unregisterReceiver(broadcastReceiver);
        stopTheVideo(false);
        super.onDestroy();
    }

    Runnable runnable2 = () -> seekUpdation2();

    private void seekUpdation2() {
        if (mPlayer2 != null) {
            int mCurrentPosition = mPlayer2.getCurrentPosition();
            seekBar2.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable2, 100);
    }

    @Override
    public void mediaProgress(MediaPlayer mediaPlayer, final SeekBar seek) {
        mPlayer2 = mediaPlayer;
        seekBar2 = seek;
        lastProgress = 0;
        seekBar2.setProgress(lastProgress);
        mPlayer2.seekTo(lastProgress);
        seekBar2.setMax(mPlayer2.getDuration());

        seekUpdation2();

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e(TAG, "mediaProgress: mCurrentPosition >>" + progress);
                lastProgress = progress;
                if (fromUser) {
                    mPlayer2.seekTo(progress);
                    Log.e(TAG, "mediaProgress: mediaSeekProgress >>" + progress);
                }
                Log.e(TAG, "mediaProgress: Progress >>" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "mediaProgress: Progress >>" + "start");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "mediaProgress: Progress >>" + "stop");
            }
        });
    }

    @Override
    public void mediaStop(MediaPlayer mediaPlayer) {
        mPlayer2 = null;
    }

    public void deleteComment(int i) {
        loading.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.DeleteComment deleteComment = builder.DeleteComment();
        Call<VideoCommentsModelResponse> call = deleteComment.deleteComment("Bearer " + preferencesUtilities.getToken(), i);
        call.enqueue(new Callback<VideoCommentsModelResponse>() {
            @Override
            public void onResponse(Call<VideoCommentsModelResponse> call, Response<VideoCommentsModelResponse> response) {
                loading.setVisibility(View.GONE);
                adapterVideoComments.setComment((ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean>) response.body().getData().getComments());

                // Toast.makeText(CourseAndCommentsActivity.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<VideoCommentsModelResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
            }
        });
    }

    public void postComment(int id, String comment_type, String comment, MultipartBody.Part voiceFile) {

        loading.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.PostComment2 postComment = builder.postComment2();
        Call<VideoCommentsModelResponse> call = postComment.postComment2("Bearer " + preferencesUtilities.getToken(), id, comment_type, comment, voiceFile);
        call.enqueue(new Callback<VideoCommentsModelResponse>() {
            @Override
            public void onResponse(Call<VideoCommentsModelResponse> call, Response<VideoCommentsModelResponse> response) {
                loading.setVisibility(View.INVISIBLE);
                Log.i("Ahmed", "" + response.code());
                if (response.code() == 200) {
                    adapterVideoComments.setComment((ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean>) response.body().getData().getComments());

                } else {
                    Toast.makeText(CourseAndCommentsActivity.this, "حدث خطا ما يرجي المحاوله في وقت لاحق", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<VideoCommentsModelResponse> call, Throwable t) {
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(CourseAndCommentsActivity.this, "حدث خطا ما يرجي المحاوله في وقت لاحق", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void editComment(int i, String s) {
        loading.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.EditComment editComment = builder.EditComment();
        EditCommentRequest request = new EditCommentRequest();
        request.setComment(s);
        request.setComment_type("text");
        Call<VideoCommentsModelResponse> call = editComment.editComment("Bearer " + preferencesUtilities.getToken(), i, request);
        call.enqueue(new Callback<VideoCommentsModelResponse>() {
            @Override
            public void onResponse(Call<VideoCommentsModelResponse> call, Response<VideoCommentsModelResponse> response) {
                loading.setVisibility(View.GONE);
                adapterVideoComments.setComment((ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean>) response.body().getData().getComments());
                //  videoDetailsViewModel.GetComments(id);
            }

            @Override
            public void onFailure(Call<VideoCommentsModelResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void delete(int id) {
        deleteComment(id);
    }

    @Override
    public void edit(int id, String s) {
        showDialogForEditComment(id, s);
    }

    @SuppressLint("ResourceType")
    private void showDialogForEditComment(int i, String s) {
        View v = LayoutInflater.from(CourseAndCommentsActivity.this).inflate(R.layout.edit_comment, null);

        AlertDialog infoDialog = new AlertDialog.Builder(CourseAndCommentsActivity.this).create();
        infoDialog.setView(v);
        Window window = infoDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        TextView editComment = v.findViewById(R.id.tvEditComment);
        ImageView userImage = v.findViewById(R.id.img_profile_dialog);
        ImageView imgEdit = v.findViewById(R.id.imgEdit);
        Glide.with(CourseAndCommentsActivity.this)
                .load(s)
                .error(R.drawable.profile_man)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(userImage);
        infoDialog.show();
        assert imgEdit != null;
        Objects.requireNonNull(imgEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editComment(i, editComment.getText().toString());
                infoDialog.cancel();
            }
        });

    }

    private void showDialogForHeadSet() {
        bottomSheetDialogForHeadSet = new BottomSheetDialog(this, R.style.bottom_sheet);
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheetDialogForHeadSet.setContentView(R.layout.wire_headset);
        bottomSheetDialogForHeadSet.show();

    }

    public void stopAudio() {

    }

    public void resumeAudio() {

    }

    public void getAudioRouteDeviceName() {
        AudioManager audioManager = (AudioManager)
                getSystemService(Context.AUDIO_SERVICE);

        if (audioManager.isWiredHeadsetOn()) {
            Log.i("Ahmed", "notconnected");
        } else if (audioManager.isBluetoothA2dpOn()) {
            Log.i("Ahmed", "connected");
            BLUETOOTH_HEADPHONE_CONNECT = true;
        } else Log.i("Ahmed", "connecaefafafted");

    }

    private void showYouTubeWebView() {
        youtubeWebView.setVisibility(View.VISIBLE);
        youtubePlayerIcon.setVisibility(View.GONE);
        youtubeWebView.requestFocus();
        youtubeWebView.getSettings().setLightTouchEnabled(true);
        youtubeWebView.getSettings().setJavaScriptEnabled(true);
        youtubeWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        youtubeWebView.setSoundEffectsEnabled(true);
        youtubeWebView.getSettings().setAllowFileAccess(true);
        youtubeWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        youtubeWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        youtubeWebView.setWebChromeClient(new WebChromeClient());
        youtubeWebView.setWebViewClient(new WebViewClient());
        youtubeWebView.getSettings().setDomStorageEnabled(true);

            if (webview_url != null && !webview_url.equals(""))
                youtubeWebView.loadUrl(webview_url);


    }

}

