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
import com.salim.ta3limes.Activities.PdfAndMp3Activity;
import com.salim.ta3limes.Models.response.CenterCoursesModelResponse;
import com.salim.ta3limes.Models.response.LibraryModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import org.minidns.record.A;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCoursePDF extends RecyclerView.Adapter<AdapterCoursePDF.ViewHolder> {

    Context context;
    ArrayList<LibraryModelResponse.Course> courseBeans;
    LayoutInflater inflater;
    private Typeface custom_font;
    SharedPreferencesUtilities preferencesUtilities;
    public onItemClicked onItemClicked;

    public AdapterCoursePDF(Context context, ArrayList<LibraryModelResponse.Course> courseBeans, onItemClicked onItemClicked) {
        this.context = context;
        this.courseBeans = courseBeans;
        this.onItemClicked = onItemClicked;
        inflater = LayoutInflater.from(context);
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public ArrayList<LibraryModelResponse.Course>  getCourseBeans() {
        return courseBeans;
    }

    public void setCourseList(List<LibraryModelResponse.Course> coursesList) {
        if (coursesList != null)
            courseBeans.addAll(coursesList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.library_item_row, parent, false);
        try {
            custom_font = Typeface.createFromAsset(parent.getContext().getAssets(), "Cairo-Regular.ttf");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LibraryModelResponse.Course dataBean = courseBeans.get(position);

        holder.courseName.setText(dataBean.name);
        holder.teacherName.setText(dataBean.teacher);
        holder.time.setText(dataBean.create);
        holder.count.setText(dataBean.pdf_count+"");

        Glide.with(context)
                .load(dataBean.image)
                .error(R.drawable.ic_image)
                .into(holder.couseLogo);

        holder.layout.setOnClickListener(v -> onItemClicked.itemClicked(dataBean.id, dataBean.name, dataBean.phone, dataBean.whatsapp));

    }

    @Override
    public int getItemCount() {
        return courseBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView layout;
        TextView courseName, teacherName;
        CircleImageView couseLogo;
        TextView time, count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.parent_layout);
            courseName = itemView.findViewById(R.id.tvCourseName);
            teacherName = itemView.findViewById(R.id.tvDoctorName);
            couseLogo = itemView.findViewById(R.id.img_profile);
            time = itemView.findViewById(R.id.tvDate);
            count = itemView.findViewById(R.id.tvPDFFiles);

            courseName.setTypeface(custom_font);
            teacherName.setTypeface(custom_font);
            time.setTypeface(custom_font);
            count.setTypeface(custom_font);
        }
    }

    public interface onItemClicked{
        void itemClicked(int id, String name, String phone, String whatsapp);
    }

    public void setFilter(ArrayList<LibraryModelResponse.Course> newlist) {
        courseBeans = new ArrayList<>();
        courseBeans.addAll(newlist);
        notifyDataSetChanged();
    }
}
