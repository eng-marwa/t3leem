package com.salim.ta3limes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.salim.ta3limes.Activities.CourseAndCommentsActivity;
import com.salim.ta3limes.Models.response.CoursePdfListModel;
import com.salim.ta3limes.Models.response.CourseVideosModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.CustomTextView;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import java.util.ArrayList;
import java.util.List;

public class AdapterCourseVideos extends RecyclerView.Adapter<AdapterCourseVideos.ViewHolder> {

    public static final String TAG = "AdapterCourseVideos";
    Context context;
    List<CourseVideosModelResponse.Lesson> lessonsBeans;
    //    List<CourseVideosModelResponse.DataBean.LessonsBean.VideosBean> videosBeans;
    LayoutInflater inflater;
    Typeface customfont;
    SharedPreferencesUtilities preferencesUtilities;

    public AdapterCourseVideos(Context context, List<CourseVideosModelResponse.Lesson> lessonsBeans) {
        this.context = context;
        this.lessonsBeans = lessonsBeans;
        inflater = LayoutInflater.from(context);
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }


    public List<CourseVideosModelResponse.Lesson> getLessonsBeans() {
        return lessonsBeans;
    }

    public void setLessonsBeans(List<CourseVideosModelResponse.Lesson> lessonsListBeans) {
        if (lessonsListBeans != null)
            lessonsBeans.addAll(lessonsListBeans);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.video_row, parent, false);
        try {
            customfont = Typeface.createFromAsset(parent.getContext().getAssets(), "Cairo-Regular.ttf");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CourseVideosModelResponse.Lesson lessonsBean = lessonsBeans.get(position);

        Log.e("onBindViewHolder", "lessonsBean.getId() >> " + lessonsBean.id);
        int views = 0;
        String videoId = "";
        String fileId = "";
        String pdfUrl = "";
        String pdfStatus = "";

        holder.txtClass.setText(lessonsBean.title);
        holder.txtTime.setText(lessonsBean.createAtrForHuman);
        if (lessonsBean.videos != null) {
            if (lessonsBean.videos.duration != null || lessonsBean.videos.image != null || lessonsBean.videos.views != -1 || lessonsBean.videos.video_id != null) {
                holder.txtLength.setText(lessonsBean.videos.duration);
                Glide.with(context)
                        .load(lessonsBean.videos.imagee)
                        .error(R.drawable.course_bg)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.imgProfile);
                views = lessonsBean.videos.views;

                videoId = lessonsBean.videos.video_id;
            } else {
                deleteItem(position);
            }
        }

        if (lessonsBean.files != null) {
            fileId = String.valueOf(lessonsBean.files.id);

            if (fileId != null || lessonsBean.files.fileUrl != null || lessonsBean.files.download != null) {
                Log.e(TAG, "Files >> " + lessonsBean.files.toString());
                pdfUrl = lessonsBean.files.fileUrl;
                pdfStatus = lessonsBean.files.download;

                Log.e(TAG, "pdfUrl >> " + pdfUrl);
                Log.e(TAG, "pdfStatus >> " + pdfStatus);
                preferencesUtilities.setFileId(fileId);
            }else {
                pdfUrl = "";
                pdfStatus = "";
                fileId = "";
            }
        }


        holder.txt_views.setText(String.valueOf(views));

        String finalVideoId = videoId;
        int finalViews = views;
        String finalFileId = fileId;
        String finalPdfUrl = pdfUrl;
        String finalPdfStatus = pdfStatus;
        holder.videoLayout.setOnClickListener(v -> {
            if (lessonsBean.videos != null) {
                if (finalViews <= 0) {
                    Toast.makeText(context, "انتهت عدد المشاهدات لديك لهذا الفيديو", Toast.LENGTH_LONG).show();
                } else {
                    if (finalPdfUrl != null && finalPdfStatus != null) {

                        Intent i = new Intent(context, CourseAndCommentsActivity.class);
                        i.putExtra("id", lessonsBean.videos.id);
                        i.putExtra("videoId", finalVideoId);
                        i.putExtra("fileId", finalFileId);
                        i.putExtra("videoTitle", lessonsBean.title);
                        i.putExtra("pdfUrl", finalPdfUrl);
                        i.putExtra("pdfStatus", finalPdfStatus);
                        i.putExtra("url", lessonsBean.videos.url);
                        i.putExtra("webview_url", lessonsBean.videos.webview_url);
                        context.startActivity(i);
                    } else {

                        Intent i = new Intent(context, CourseAndCommentsActivity.class);
                        i.putExtra("id", lessonsBean.videos.id);
                        i.putExtra("videoId", finalVideoId);
                        i.putExtra("fileId", finalFileId);
                        i.putExtra("videoTitle", lessonsBean.title);
                        i.putExtra("pdfUrl", "");
                        i.putExtra("pdfStatus", "");
                        i.putExtra("url", lessonsBean.videos.url);
                        i.putExtra("webview_url", lessonsBean.videos.webview_url);
                        context.startActivity(i);
                    }
                }

            } else if (lessonsBean.videos == null) {
                Toast.makeText(context, "محتوي هذا الفيديو غير متوفر ف الوقت الحالي", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteItem(int position) {
        lessonsBeans.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lessonsBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView videoLayout;
        ImageView imgProfile;
        TextView txtClass, txtTime;
        TextView txtLength, txt_views;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoLayout = itemView.findViewById(R.id.video_card);
            imgProfile = itemView.findViewById(R.id.img_profile);
            txtClass = itemView.findViewById(R.id.txt_class);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtLength = itemView.findViewById(R.id.txt_length);
            txt_views = itemView.findViewById(R.id.txt_views);

            txtClass.setTypeface(customfont);
            txt_views.setTypeface(customfont);
            txtTime.setTypeface(customfont);
            txtLength.setTypeface(customfont);
        }
    }

    public void setFilter(ArrayList<CourseVideosModelResponse.Lesson> newlist) {
        lessonsBeans = new ArrayList<>();
        lessonsBeans.addAll(newlist);
        notifyDataSetChanged();
    }
}
