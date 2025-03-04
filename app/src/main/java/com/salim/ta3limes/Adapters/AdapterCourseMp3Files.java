package com.salim.ta3limes.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.salim.ta3limes.Activities.PDFViewerActivity;

import com.salim.ta3limes.Models.response.CourseMp3ListFiles;
import com.salim.ta3limes.Models.response.CoursePdfListModel;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.utilities.TimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterCourseMp3Files extends RecyclerView.Adapter<AdapterCourseMp3Files.ViewHolder> {

    Context context;
    List<CourseMp3ListFiles.Lessone> filesBeans;
    LayoutInflater inflater;
    private Typeface custom_font;
    MediaPlayer mediaPlayer;
    String content;
    private boolean isPlaying = false;
    private boolean isVisible = false;
    ImageView mTempPlay;
    int mediaPosition = -1;
    int progress = 0;
    float speed = 1f;
    private ProgressMediaPlayer progressMediaPlayer;
    SharedPreferencesUtilities preferencesUtilities;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public AdapterCourseMp3Files(Context context, List<CourseMp3ListFiles.Lessone> centersBeans) {
        this.context = context;
        this.filesBeans = centersBeans;
        this.progressMediaPlayer = (ProgressMediaPlayer) context;
        inflater = LayoutInflater.from(context);
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public List<CourseMp3ListFiles.Lessone> getCentersBeans() {
        return filesBeans;
    }

    public void setCoursePDFs(List<CourseMp3ListFiles.Lessone> filesBeansList) {
        if (filesBeansList != null)
            filesBeans.addAll(filesBeansList);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.mp3_view_row, parent, false);

        try {
            custom_font = Typeface.createFromAsset(parent.getContext().getAssets(), "Cairo-Regular.ttf");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseMp3ListFiles.Lessone filesBean = filesBeans.get(position);
        holder.txtName.setText(filesBean.name);
        holder.txtDate.setText(filesBean.create);
        content = filesBean.voice_id;
        content = content.replace(" ", "%20");
    }

    public void stopMedia() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer = null;
        }
    }

    private void stopAudio(ViewHolder holder) {
        isPlaying = false;
        holder.play.setImageResource(R.drawable.playicon);
        try {
            mediaPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer = null;
    }

    @Override
    public int getItemCount() {
        return filesBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout;
        TextView txtName, txtDate;
        ImageView img_type, play;
        ProgressBar pbProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.mp3_card);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDate = itemView.findViewById(R.id.txt_date);
            img_type = itemView.findViewById(R.id.img_type);
            play = itemView.findViewById(R.id.img_play);
            pbProgressBar = itemView.findViewById(R.id.pbProgressBar);

            layout.setOnClickListener(v -> {
                Log.d("TAG", "ViewHolder: progress vis");

//                final ProgressDialog progressDialog;
//                progressDialog = new ProgressDialog(context);
//                progressDialog.setMessage("Loading..."); // Setting Message
//                progressDialog.setTitle("ProgressDialog"); // Setting Title
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// Progress Dialog Style Spinner
//                progressDialog.show(); // Display Progress Dialog
//                progressDialog.setCancelable(false);
//
//                new Thread(() -> {
//                    try {
//                        Thread.sleep(3000);
//                        Log.d("TAG", "ViewHolder: progress invis");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    progressDialog.dismiss();
////            loading.setVisibility(View.INVISIBLE);
//                }).start();
//
//                mediaPlayer = new MediaPlayer();
//
//                Log.d("AdapterCourseMp3Files", "voiceID >> " + content);
//
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                try {
//                    mediaPlayer.setDataSource(content);
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                    pbProgressBar.setVisibility(View.INVISIBLE);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                progressMediaPlayer.mediaProgress(content);
            });

            txtName.setTypeface(custom_font);
            txtDate.setTypeface(custom_font);
        }
    }

    public interface ProgressMediaPlayer {
        void mediaProgress(String content);

        void mediaStop(MediaPlayer mediaPlayer);

        void itemClicked(Boolean clicked);
    }

    public void setFilter(ArrayList<CourseMp3ListFiles.Lessone> newlist) {
        filesBeans = new ArrayList<>();
        filesBeans.addAll(newlist);
        notifyDataSetChanged();
    }

}
