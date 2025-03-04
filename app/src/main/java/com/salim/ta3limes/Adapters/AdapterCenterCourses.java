package com.salim.ta3limes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.salim.ta3limes.Activities.CourseVideosActivity;
import com.salim.ta3limes.Models.response.CenterCoursesModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCenterCourses extends RecyclerView.Adapter<AdapterCenterCourses.ViewHolder> {

    Context context;
    ArrayList<CenterCoursesModelResponse.DataBean.CourseBean> courseBeans;
    LayoutInflater inflater;
    private Typeface custom_font;
    SharedPreferencesUtilities preferencesUtilities;

    public AdapterCenterCourses(Context context, ArrayList<CenterCoursesModelResponse.DataBean.CourseBean> courseBeans) {
        this.context = context;
        this.courseBeans = courseBeans;
        inflater = LayoutInflater.from(context);
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public ArrayList<CenterCoursesModelResponse.DataBean.CourseBean> getCourseBeans() {
        return courseBeans;
    }

    public void setCarsList(List<CenterCoursesModelResponse.DataBean.CourseBean> coursesList) {
        if (coursesList != null)
            courseBeans.addAll(coursesList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.course_row, parent, false);
        try {
            custom_font = Typeface.createFromAsset(parent.getContext().getAssets(), "Cairo-Regular.ttf");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CenterCoursesModelResponse.DataBean.CourseBean courseBean = courseBeans.get(position);

        preferencesUtilities.setCenterId(String.valueOf(courseBean.getId()));
        holder.courseName.setText(courseBean.getName());
        holder.teacherName.setText(courseBean.getTeacher());
        holder.time.setText(courseBean.getStarts_at());
        if(courseBean.getStudent_count()!=-1)
        holder.student.setText(String.valueOf(courseBean.getStudent_count()));
        else
            holder.student.setVisibility(View.GONE);
        holder.videos.setText(String.valueOf(courseBean.getVideos_count()));

        Glide.with(context)
                .load(courseBean.getImage())
                .error(R.drawable.profile_man)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.couseLogo);

        holder.layout.setOnClickListener(v -> {

            preferencesUtilities.setCourseAirbodsStatus(courseBean.getAirbods());

                Intent intent = new Intent(context, CourseVideosActivity.class);

                intent.putExtra("courseId", courseBean.getId());
                intent.putExtra("courseTitle", courseBean.getName());
                intent.putExtra("students", String.valueOf(courseBean.getStudent_count()));
                context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return courseBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView layout;
        TextView courseName, teacherName;
        CircleImageView couseLogo;
        TextView time, student, videos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.parent_layout);
            courseName = itemView.findViewById(R.id.txt_name);
            teacherName = itemView.findViewById(R.id.teacher_name);
            couseLogo = itemView.findViewById(R.id.img_profile);
            time = itemView.findViewById(R.id.txt_time);
            student = itemView.findViewById(R.id.txt_student);
            videos = itemView.findViewById(R.id.txt_folder);

            courseName.setTypeface(custom_font);
            teacherName.setTypeface(custom_font);
            time.setTypeface(custom_font);
            student.setTypeface(custom_font);
            videos.setTypeface(custom_font);
        }
    }
}
