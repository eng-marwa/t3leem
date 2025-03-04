package com.salim.ta3limes.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.salim.ta3limes.Adapters.AdapterLiveComments;
import com.salim.ta3limes.Models.response.LiveCommentsModelResponse;
import com.salim.ta3limes.Models.response.PostLiveCommentModelResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.CustomEditText;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoChatActivity extends AppCompatActivity {
    public static final String TAG = "VideoChatActivity";

    private static final int PERMISSION_REQ_ID = 22;
    private Boolean permissionGrant=false;

    @BindView(R.id.student_id)
    TextView studentId;
    @BindView(R.id.student_name)
    TextView studentName;
    @BindView(R.id.icon_padding)
    RelativeLayout iconPadding;
    @BindView(R.id.remote_video_view_container)
    RelativeLayout mRemoteContainer;
    @BindView(R.id.local_video_view_container)
    FrameLayout mLocalContainer;
    @BindView(R.id.btn_call)
    ImageView mCallBtn;
    @BindView(R.id.btn_mute)
    ImageView mMuteBtn;
    @BindView(R.id.control_panel)
    RelativeLayout controlPanel;
    @BindView(R.id.activity_video_chat_view)
    RelativeLayout activityVideoChatView;
    @BindView(R.id.comments_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.send_imageView)
    ImageView sendImageView;
    @BindView(R.id.comment_EditText)
    CustomEditText commentEditText;
    @BindView(R.id.loading)
    MKLoader loader;

    AdapterLiveComments adapterLiveComments;
    LinearLayoutManager linearLayoutManager;
    ArrayList<LiveCommentsModelResponse.DataBean.CommentsBean> commentsBeans;

    private VideoCanvas mLocalVideo;
    private VideoCanvas mRemoteVideo;

    private boolean mCallEnd;
    private boolean mMuted;

    private String name, student_ID, token, channel_name, live_video_id, live_token;
    SharedPreferencesUtilities preferencesUtilities;

    private RtcEngine mRtcEngine;

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            Log.e(TAG, "setupRemoteVideo: uid >> " + (uid & 0xFFFFFFFFL));
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(() -> {

                setupRemoteVideo(uid);
            });
        }

        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(() -> {
                onRemoteUserLeft(uid);
            });
        }
    };

    private void setupRemoteVideo(int uid) {
        Log.e(TAG, "setupRemoteVideo: uid >> " + uid);
        ViewGroup parent = mRemoteContainer;
        if (parent.indexOfChild(mLocalVideo.view) > -1) {
            parent = mLocalContainer;
        }
        if (mRemoteVideo != null) {
            return;
        }
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == mLocalContainer);
        parent.addView(view);
        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid);
        // Initializes the video view of a remote user.
        mRtcEngine.setupRemoteVideo(mRemoteVideo);
    }

    private void onRemoteUserLeft(int uid) {
        if (mRemoteVideo != null && mRemoteVideo.uid == uid) {
            removeFromParent(mRemoteVideo);
            // Destroys remote view
            mRemoteVideo = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_video_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        channel_name = intent.getStringExtra("channel_name");
        live_video_id = intent.getStringExtra("live_video_id");
        live_token = intent.getStringExtra("live_token");

        Log.e(TAG, "onCreate: live_video_id >> " + live_video_id);

        preferencesUtilities = new SharedPreferencesUtilities(this);
        token = preferencesUtilities.getToken();
        name = preferencesUtilities.getUserName();
        student_ID = preferencesUtilities.getUserId();

        studentId.setText("ID: " + student_ID);
        studentName.setText("Name: " + name);

        commentsBeans = new ArrayList<>();
        adapterLiveComments = new AdapterLiveComments(this, commentsBeans);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterLiveComments);

        getComments(token);
        requestPermissions();
        // If all the permissions are granted, initialize the RtcEngine object and join a channel.


        checkUser();
    }

    private void getComments(String token) {
        loader.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.GetLiveComments getLiveComments = builder.getLiveComments();
        Call<LiveCommentsModelResponse> call = getLiveComments.getLiveComments("Bearer " + token, live_video_id);
        call.enqueue(new Callback<LiveCommentsModelResponse>() {
            @Override
            public void onResponse(Call<LiveCommentsModelResponse> call, Response<LiveCommentsModelResponse> response) {
                if (response.code() == 200){
                    loader.setVisibility(View.GONE);
                    adapterLiveComments.getCommentsBeans().clear();
                    adapterLiveComments.getCommentsBeans().addAll(response.body().getData().getComments());
                    adapterLiveComments.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<LiveCommentsModelResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getComments(token);
        checkUser();
    }
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

                if (isGranted)
                {
                    initEngineAndJoinChannel();
                }
                else  {
                    showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                            "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    finish();
                }
            });

    private void requestPermissions() {
        List<String> permissionsToRequest = new ArrayList<>();
        permissionsToRequest.add(Manifest.permission.RECORD_AUDIO);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
        requestPermissionLauncher.launch(String.valueOf(permissionsToRequest));
    }


    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initEngineAndJoinChannel() {
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.setClientRole(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        mRtcEngine.enableVideo();

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        mRtcEngine.enableVideo();
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        mLocalContainer.addView(view);
        mLocalVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(mLocalVideo);
    }

    private void joinChannel() {

        Log.e(TAG, "joinChannel: Token >> " + live_token);
        mRtcEngine.joinChannel(live_token, channel_name, "Extra Optional Data", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCallEnd) {
            leaveChannel();
        }
        RtcEngine.destroy();
    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        // Stops/Resumes sending the local audio stream.
        mRtcEngine.muteLocalAudioStream(mMuted);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }

    public void onCallClicked(View view) {
        if (mCallEnd) {
            startCall();
            mCallEnd = false;
            mCallBtn.setImageResource(R.drawable.btn_endcall);
        } else {
            endCall();
            mCallEnd = true;
            mCallBtn.setImageResource(R.drawable.btn_startcall);
        }

    }

    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        removeFromParent(mLocalVideo);
        mLocalVideo = null;
        removeFromParent(mRemoteVideo);
        mRemoteVideo = null;
        leaveChannel();
        onBackPressed();
    }

    private ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }

    private void switchView(VideoCanvas canvas) {
        ViewGroup parent = removeFromParent(canvas);
        if (parent == mLocalContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(false);
            }
            mRemoteContainer.addView(canvas.view);
        } else if (parent == mRemoteContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(true);
            }
            mLocalContainer.addView(canvas.view);
        }
    }

    public void onLocalContainerClick(View view) {
        switchView(mLocalVideo);
        switchView(mRemoteVideo);
    }

    public void SendComment(View view) {
        String comment = commentEditText.getText().toString();
        Log.e(TAG, "SendComment: comment >> " + comment);
        sendComment(token, comment, live_video_id);
    }

    private void sendComment(String token, String comment, String live_video_id){

        loader.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.PostLiveComment postLiveComment = builder.postLiveComment();
        Call<PostLiveCommentModelResponse> call = postLiveComment.postLiveComment("Bearer " + token, comment, live_video_id);
        call.enqueue(new Callback<PostLiveCommentModelResponse>() {
            @Override
            public void onResponse(Call<PostLiveCommentModelResponse> call, Response<PostLiveCommentModelResponse> response) {
                Log.e(TAG, "onResponse: PostComment >> " + response);
                if (response.code() == 200){
                    Log.e(TAG, "onResponse: PostComment >> " + response);
                    loader.setVisibility(View.GONE);
                    commentEditText.setText("");
                    String msg = response.body().getMessage();
                    Toast.makeText(VideoChatActivity.this, msg, Toast.LENGTH_SHORT).show();

                    getComments(token);
                    recyclerView.scrollToPosition(commentsBeans.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<PostLiveCommentModelResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

}