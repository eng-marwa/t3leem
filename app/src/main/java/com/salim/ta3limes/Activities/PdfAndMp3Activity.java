package com.salim.ta3limes.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.media.SyncParams;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.salim.ta3limes.Adapters.AdapterCourseMp3Files;
import com.salim.ta3limes.Adapters.AdapterCoursePDFs;
import com.salim.ta3limes.Adapters.AdapterVideoComments;
import com.salim.ta3limes.Models.response.CourseMp3ListFiles;
import com.salim.ta3limes.Models.response.CoursePdfListModel;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.ContantsUtils;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.utilities.TimeUtils;
import com.salim.ta3limes.viewmodels.Mp3FilesViewModel;
import com.salim.ta3limes.viewmodels.MyMP3ViewModel;
import com.salim.ta3limes.viewmodels.MyPDFsViewModel;
import com.salim.ta3limes.viewmodels.factory.MP3ListModelFactory;
import com.salim.ta3limes.viewmodels.factory.MyMP3ListModelFactory;
import com.salim.ta3limes.viewmodels.factory.PDFListModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@RequiresApi(api = Build.VERSION_CODES.M)
public class PdfAndMp3Activity extends AppCompatActivity implements AdapterCourseMp3Files.ProgressMediaPlayer {
    BottomSheetDialog bottomSheetDialogForHeadSet;
    private MyPDFsViewModel mViewModel;
    private MyMP3ViewModel viewModel;
    List<CoursePdfListModel.Lessone> filesBeans;
    List<CourseMp3ListFiles.Lessone> mp3Beans;
    ImageView back;
    EditText search, search1;
    MKLoader loading;
    TextView tvCourseName;
    private BroadcastReceiver broadcastReceiver;
    RecyclerView rvPdfMp3Files;
    LinearLayoutManager layoutManager;
    AdapterCoursePDFs adapterCoursePDFs;
    AdapterCourseMp3Files adapterCourseMp3Files;
    private boolean BLUETOOTH_HEADPHONE_CONNECT = false;
    private boolean HEADPHONE_CONNECT = false;
    private boolean ALL_TRUE = true;
    SharedPreferencesUtilities preferencesUtilities;
    int id = 0;
    int position = 0;
    int lastProgress = 0;
    String progress;
    MediaPlayer mMediaPlayer;

    private boolean isVisible = false;
    private Handler mHandler = new Handler();
    SeekBar mSeekBar;
    float speed = 1f;

    private static final String ACTION_BT_HEADSET_STATE_CHANGED  = "android.bluetooth.headset.action.STATE_CHANGED";
    private static final int STATE_CONNECTED = 0x00000002;
    private static final int STATE_DISCONNECTED  = 0x00000000;
    private static final String EXTRA_STATE = "android.bluetooth.headset.extra.STATE";
    private String TAG = PdfAndMp3Activity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getAudioRouteDeviceName();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_pdf_and_mp3);

        if (WindowManager.LayoutParams.FLAG_SECURE == 1) {
            AudioManager audioManager = (AudioManager)
                    getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE, true);
            audioManager.setMicrophoneMute(true);
            audioManager.setSpeakerphoneOn(false);
        }

        position = getIntent().getIntExtra("position", 0);
        preferencesUtilities=new SharedPreferencesUtilities(this);
        back = findViewById(R.id.ivBack);
        search = findViewById(R.id.ivSearch);
        search1 = findViewById(R.id.ivSearch1);
        loading = findViewById(R.id.loading);
        tvCourseName = findViewById(R.id.tvCourseName);
        rvPdfMp3Files = findViewById(R.id.rvPdfMp3Files);

        mViewModel = new ViewModelProvider(this, new PDFListModelFactory(this, filesBeans)).get(MyPDFsViewModel.class);
        viewModel = new ViewModelProvider(this, new MyMP3ListModelFactory(this, mp3Beans)).get(MyMP3ViewModel.class);

        id = getIntent().getIntExtra("courseId", 0);
        code = getIntent().getStringExtra("courseCode");
        courseName = getIntent().getStringExtra("courseName");

        Log.e("Mp3andPdfActivity", "onCreate: " + ContantsUtils.TYPE);

        tvCourseName.setText(courseName);

        filesBeans = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapterCoursePDFs = new AdapterCoursePDFs(this, filesBeans);
        rvPdfMp3Files.setLayoutManager(layoutManager);
        if (ContantsUtils.TYPE.equals("library")) {
            rvPdfMp3Files.setAdapter(adapterCoursePDFs);
        }

        mp3Beans = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapterCourseMp3Files = new AdapterCourseMp3Files(this, mp3Beans);
        rvPdfMp3Files.setLayoutManager(layoutManager);
        if (ContantsUtils.TYPE.equals("fileVoice")) {
            rvPdfMp3Files.setAdapter(adapterCourseMp3Files);
            search1.setVisibility(View.VISIBLE);
            search.setVisibility(View.GONE);
        }

        Log.e("PdfAndMp3Activity", "onCreate: Code >> " + code + "      " + " id >> " + id);


        if (ContantsUtils.TYPE.equals("library"))
            mViewModel.getPdfOneCourse(id + "", code);
        else
            viewModel.getMp3OneCourse(id + "", code);

        back.setOnClickListener(v -> finish());

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString();
                ArrayList<CoursePdfListModel.Lessone> newlist = new ArrayList<CoursePdfListModel.Lessone>();
                for (CoursePdfListModel.Lessone dataBean : filesBeans) {
                    String name = dataBean.name.toLowerCase();
                    if (name.contains(s))
                        newlist.add(dataBean);
                    adapterCoursePDFs.notifyDataSetChanged();
                }
                adapterCoursePDFs.setFilter(newlist);
                adapterCoursePDFs.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString();
                ArrayList<CourseMp3ListFiles.Lessone> newlist = new ArrayList<CourseMp3ListFiles.Lessone>();
                for (CourseMp3ListFiles.Lessone dataBean : mp3Beans) {
                    String name = dataBean.name.toLowerCase();
                    if (name.contains(s))
                        newlist.add(dataBean);
                    adapterCourseMp3Files.notifyDataSetChanged();
                }
                adapterCourseMp3Files.setFilter(newlist);
                adapterCourseMp3Files.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mViewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });


        mViewModel.lessonsList.observe(this, lessones -> adapterCoursePDFs.setCoursePDFs(lessones));

        viewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        viewModel.lessonsList.observe(this, lessones -> adapterCourseMp3Files.setCoursePDFs(lessones));

        viewModel.checkUser();
        viewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        viewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }

    String code, courseName;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class).putExtra("position", 2));
        finish();
    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void fromBegging()
    {
        lastProgress = 0;
        mSeekBar.setProgress(lastProgress);
        mMediaPlayer.seekTo(lastProgress);
        mSeekBar.setMax(mMediaPlayer.getDuration());
        seekUpdation();

    }
    @Override
    public void mediaProgress(String content) {

//        loading.setVisibility(View.VISIBLE);
//        new Thread(() -> {
//            try {
//                Thread.sleep(3000);
//                Log.d("TAG", "ViewHolder: progress invis");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            loading.setVisibility(View.GONE);
////            loading.setVisibility(View.INVISIBLE);
//        }).start();

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_mp3_dialog, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        ImageView imageView = dialog.findViewById(R.id.imageViewClose);
        ImageView play = dialog.findViewById(R.id.imageViewPlay);
        mSeekBar = dialog.findViewById(R.id.seekBar);
        Chronometer voicePosition = dialog.findViewById(R.id.chronometerTimer);
        TextView voiceDuration = dialog.findViewById(R.id.voiceDuration);
        TextView tvSpeed = dialog.findViewById(R.id.tvSpeed);
        LinearLayout speedLayout = dialog.findViewById(R.id.speedLayout);
        TextView x1 = dialog.findViewById(R.id.x1);
        TextView x2 = dialog.findViewById(R.id.x2);
        TextView x3 = dialog.findViewById(R.id.x3);
        TextView x4 = dialog.findViewById(R.id.x4);

        mMediaPlayer = new MediaPlayer();

        Log.d("AdapterCourseMp3Files", "voiceID >> " + content);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
           @Override
           public void onPrepared(MediaPlayer mediaPlayer) {

               mMediaPlayer.start();
               loading.setVisibility(View.GONE);
               if (mMediaPlayer.isPlaying()) {
                   play.setImageResource(R.drawable.pauseicon);
               } else {
                   play.setImageResource(R.drawable.playicon);
               }
               fromBegging();
               voicePosition.start();
               checkHeadPhone();
               play.setOnClickListener(v -> {
                     if (ALL_TRUE)
                     {
                         if (mMediaPlayer.isPlaying()) {
                             play.setImageResource(R.drawable.playicon);
                             try {
                                 mMediaPlayer.pause();
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                             voicePosition.setBase(SystemClock.elapsedRealtime());
                             voicePosition.stop();

                         } else {
                             play.setImageResource(R.drawable.pauseicon);
                             Log.e(TAG, "mediaProgress: Content >> " + content);

                             mMediaPlayer.start();
                             assert voicePosition != null;
                             voicePosition.start();
               /* if (!isPlaying && content != null) {
                    isPlaying = true;

                   */
                /* mMediaPlayer.setOnCompletionListener(mp -> {
                        isPlaying = false;
                        play.setImageResource(R.drawable.playicon);
                    });*/
                /*} else {
                    isPlaying = false;
                    play.setImageResource(R.drawable.playicon);
                    try {
                        mMediaPlayer.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mMediaPlayer = null;
                    voicePosition.stop();
                }*/
                         }
                     }

               });


           }
       });
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(content);
            mMediaPlayer.prepareAsync();
            loading.setVisibility(View.VISIBLE);
            dialog.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

   mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
      @Override
      public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

        mediaPlayer.reset();
          return false;
      }
  });
        mMediaPlayer.setOnCompletionListener(mp -> {

            fromBegging();
            play.setImageResource(R.drawable.playicon);
            voicePosition.setBase(SystemClock.elapsedRealtime());
            voicePosition.stop();

        });


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e(TAG, "mediaProgress: mCurrentPosition >>" + progress);
                if (mMediaPlayer != null && fromUser) {
                    mMediaPlayer.seekTo(progress);
                    voicePosition.setBase(SystemClock.elapsedRealtime() - mMediaPlayer.getCurrentPosition());
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

        imageView.setOnClickListener(v -> {

            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaPlayer = null;

            dialog.dismiss();
        });

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            tvSpeed.setVisibility(View.GONE);
        } else {
            tvSpeed.setOnClickListener(v -> {
                isVisible = !isVisible;
                if (isVisible)
                    speedLayout.setVisibility(View.VISIBLE);
                else speedLayout.setVisibility(View.GONE);
            });
        }

        TimeUtils totalTime = getTimeFrom(mMediaPlayer.getDuration());
        int seconds = totalTime.seconds;
        int minutes = totalTime.minutes;
        int hours = totalTime.hours;
        String labelText = hours + ":" + minutes + ":" + seconds;
        System.out.println(labelText);

        voiceDuration.setText(labelText);

        x1.setOnClickListener(v -> {
            speed = 1f;
            if (mMediaPlayer != null&&mMediaPlayer.isPlaying()) {
                mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
            }
        });
        x2.setOnClickListener(v -> {
            speed = 1.25f;
            if (mMediaPlayer != null&&mMediaPlayer.isPlaying()) {
                mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
            }
        });
        x3.setOnClickListener(v -> {
            speed = 1.5f;
            if (mMediaPlayer != null&&mMediaPlayer.isPlaying()) {
                mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
            }
        });
        x4.setOnClickListener(v -> {
            speed = 2f;
            if (mMediaPlayer != null&&mMediaPlayer.isPlaying()) {
                mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
            }
        });

    }

    @Override
    public void mediaStop(MediaPlayer mediaPlayer) {
        mMediaPlayer = null;
    }

    @Override
    public void itemClicked(Boolean clicked) {
        if (clicked) {
            runOnUiThread(() -> loading.setVisibility(View.VISIBLE));
//            Thread thread = new Thread(){
//                @Override
//                public void run() {
//
//                }
//            };
//            thread.start();
        }
    }


    Runnable runnable = () -> seekUpdation();

    private void seekUpdation() {
        if (mMediaPlayer != null) {
            int mCurrentPosition = mMediaPlayer.getCurrentPosition();
            mSeekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(runnable);
        if (broadcastReceiver!=null)
            unregisterReceiver(broadcastReceiver);
       if (mMediaPlayer!=null)
       {
           mMediaPlayer.release();

       }
        super.onDestroy();
    }

    private void getTimeType() {
        TimeUtils totalTime = getTimeFrom(500000);
        int seconds = totalTime.seconds;
        int minutes = totalTime.minutes;
        int hours = totalTime.hours;
        String labelText = "totalTime is: " + hours + ":" + minutes + ":" + seconds;
        System.out.println(labelText);
    }

    private static TimeUtils getTimeFrom(int milliseconds) {
        int seconds = milliseconds / 1000;
        return new TimeUtils(seconds / 3600, (seconds % 3600) / 60, (seconds % 3600) % 60);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkUser();
        viewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        viewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }
    private void checkHeadPhone()
    {
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                Log.i("Ahmed",action);
                int i;

                Log.e("Ahmed", "Ahmed"+preferencesUtilities.getUserAirbodsStatus());
                Log.e("Ahmed", "Ahmed"+preferencesUtilities.getCourseAirbodsStatus());
                if (preferencesUtilities.getCourseAirbodsStatus()==1)
                {
                    ALL_TRUE=false;
                  if (action.equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED) )
                  {
                    int state=intent.getIntExtra(BluetoothHeadset.EXTRA_STATE,0);
                    Log.i("Ahmed",""+state);
                    switch (state)
                    {
                        case  BluetoothHeadset.A2DP:
                        {
                            Log.i("Ahmed","A2DP");
                            BLUETOOTH_HEADPHONE_CONNECT=true;
                            resumeAudio();
                            break;
                        }
                        case BluetoothHeadset.STATE_DISCONNECTED:
                        {
                            if (HEADPHONE_CONNECT)
                            {
                                Log.i("Ahmed","stop");
                                BLUETOOTH_HEADPHONE_CONNECT=false;
                                stopAudio(true);
                            }

                        }
                    }


                }

                 else if (Intent.ACTION_HEADSET_PLUG.equals(action))
                 {
                    Log.i("Ahmed","here4");
                    i=intent.getIntExtra("state",-1);
                    if (BLUETOOTH_HEADPHONE_CONNECT)
                    {
                        resumeAudio();
                    }
                    else {
                        if (i==0)
                        {
                            HEADPHONE_CONNECT=false;
                            Log.i("Ahmed","stop");
                            stopAudio(true);

                        }
                        if (i==1)
                        {
                            HEADPHONE_CONNECT=true;
                            resumeAudio();

                        }
                    }
                }
                }
                else resumeAudio();

            }
        };
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(broadcastReceiver,intentFilter);
    }
    public void getAudioRouteDeviceName() {
        AudioManager audioManager = (AudioManager)
                getSystemService(Context.AUDIO_SERVICE);

        if (audioManager.isWiredHeadsetOn()) {
            Log.i("Ahmed","notconnected");
        } else if (audioManager.isBluetoothA2dpOn()) {
            Log.i("Ahmed","connected");
            BLUETOOTH_HEADPHONE_CONNECT=true;
        }
        else   Log.i("Ahmed","connecaefafafted");

    }
    private void showDialogForHeadSet(){
        bottomSheetDialogForHeadSet = new BottomSheetDialog(this, R.style.bottom_sheet);
        bottomSheetDialogForHeadSet.setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheetDialogForHeadSet.setContentView(R.layout.wire_headset);
        bottomSheetDialogForHeadSet.show();
    }
    public void stopAudio(Boolean toast){
    if (mMediaPlayer!=null)
    {
         mMediaPlayer.pause();
    }
           if (toast)
            showDialogForHeadSet();
    }
    public void resumeAudio(){
        ALL_TRUE=true;
        if (mMediaPlayer!=null)
        {
            mMediaPlayer.start();
        }

        if (bottomSheetDialogForHeadSet!=null)
            bottomSheetDialogForHeadSet.dismiss();
    }

}