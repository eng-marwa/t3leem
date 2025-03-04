package com.salim.ta3limes.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.salim.ta3limes.Models.response.VideoCommentsModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.CustomTextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterVideoComments extends RecyclerView.Adapter<AdapterVideoComments.ViewHolder> {

    public static final String TAG = "AdapterVideoComments";
    Context context;
    ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean> commentsBeans;
    Typeface customfont;
    LayoutInflater inflater;
    MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    File file;
    String fileName = null;
    ImageView mTempPlay;
    int mediaPosition = -1;
    private ProgressMediaPlayer progressMediaPlayer;
    private adapterInterface listener;
     public interface adapterInterface{
        void delete(int id);
        void edit(int id,String s);
    }
    public AdapterVideoComments(Context context, ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean> commentsBeans) {
        this.context = context;
        this.commentsBeans = commentsBeans;
        inflater = LayoutInflater.from(context);
        this.progressMediaPlayer = (ProgressMediaPlayer) context;
    }
 public void setListener(adapterInterface listener)
 {
     this.listener=listener;
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

    public ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean> getCommentsBeans() {
        return commentsBeans;
    }
   public void setComment(ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean> commentsList)
   {
       commentsBeans=commentsList;
       notifyDataSetChanged();
   }
    public void setCommentsBeans(List<VideoCommentsModelResponse.DataBean.CommentsBean> commentsListBeans) {
        if (commentsListBeans != null)
            commentsBeans.addAll(commentsListBeans);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View v;
       if (viewType==1)
       {
           v = inflater.inflate(R.layout.my_comment_row, parent, false);
       }else
         v = inflater.inflate(R.layout.comment_row, parent, false);
        try {
            customfont = Typeface.createFromAsset(parent.getContext().getAssets(), "Cairo-Regular.ttf");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VideoCommentsModelResponse.DataBean.CommentsBean commentsBean = commentsBeans.get(position);

        String content = commentsBean.getContent();
        if (commentsBean.getType().equals("text")) {
            holder.play.setVisibility(View.GONE);
            holder.txtComment.setText(commentsBean.getContent());
        } else {
            holder.txtComment.setVisibility(View.GONE);
            holder.seekBar.setVisibility(View.VISIBLE);
            holder.tvEdit.setVisibility(View.GONE);
            Log.e(TAG, "onBindViewHolder: voice_comment >> " + content);
        }

        holder.txtName.setText(commentsBean.getStudent().getName());
        holder.txtDate.setText(commentsBean.getDate());

        Glide.with(context)
                .load(commentsBean.getStudent().getPicture())
                .error(R.drawable.profile_man)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.circleImageView);


        holder.play.setOnClickListener(v -> {

            if (mediaPlayer != null) {
                isPlaying = false;
                mTempPlay.setImageResource(R.drawable.playicon);
                try {
                    mediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer();
            mTempPlay = holder.play;

            Log.d(TAG, "filename >> " + content);

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(content);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!isPlaying && content != null) {
                isPlaying = true;

                mediaPlayer.setOnCompletionListener(mp -> {
                    isPlaying = false;
                    holder.play.setImageResource(R.drawable.playicon);
                });

                if (mediaPosition == position) {
                    mediaPosition = -1;
                    progressMediaPlayer.mediaStop(mediaPlayer);
                    stopAudio(holder);
                } else {
                    progressMediaPlayer.mediaProgress(mediaPlayer, holder.seekBar);
                    mediaPosition = position;
                    holder.play.setImageResource(R.drawable.pauseicon);
                }
            } else {
                stopAudio(holder);
            }

        });
      holder.tvDelete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              if (listener!=null)
              {
                  listener.delete(commentsBeans.get(holder.getAdapterPosition()).getId());
              }
          }
      });
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener!=null)
                {
                    listener.edit(commentsBeans.get(holder.getAdapterPosition()).getId()
                    ,commentsBean.getStudent().getPicture());
                }
            }
        });
    }

    private void stopAudio(ViewHolder holder){
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
    public int getItemViewType(int position) {

        return commentsBeans.get(position).getStatus();
    }

    @Override
    public int getItemCount() {
        return commentsBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView tvDelete,tvEdit;
        CircleImageView circleImageView;
        CustomTextView txtName, txtDate, txtComment;
        ImageView play;
        SeekBar seekBar;
        RelativeLayout player_container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           tvDelete=itemView.findViewById(R.id.tvDelete);
           tvEdit=itemView.findViewById(R.id.tvEdit);
            player_container = itemView.findViewById(R.id.player_container);
            circleImageView = itemView.findViewById(R.id.img_profile);
            txtName = itemView.findViewById(R.id.name_textView);
            txtDate = itemView.findViewById(R.id.date_textView);
            txtComment = itemView.findViewById(R.id.comment_textView);
            play = itemView.findViewById(R.id.play_imageView);
            seekBar = itemView.findViewById(R.id.playerSeekBar);

            txtName.setTypeface(customfont);
            txtDate.setTypeface(customfont);
            txtComment.setTypeface(customfont);
        }
    }

    public interface ProgressMediaPlayer{

        void mediaProgress(MediaPlayer mediaPlayer, SeekBar seekBar);

        void mediaStop(MediaPlayer mediaPlayer);
    }
}
