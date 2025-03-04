package com.salim.ta3limes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.salim.ta3limes.Activities.CourseAndCommentsActivity;
import com.salim.ta3limes.Activities.CourseVideosActivity;
import com.salim.ta3limes.Activities.PDFViewerActivity;
import com.salim.ta3limes.Activities.VideoChatActivity;
import com.salim.ta3limes.Models.response.NotificationModelResponse;
import com.salim.ta3limes.Models.response.SearchResultModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.ContantsUtils;

import java.util.List;

public class AdapterSearchResults extends RecyclerView.Adapter<AdapterSearchResults.ViewHolder> {

    Context context;
    List<SearchResultModelResponse.Result> searchModel;
    LayoutInflater inflater;
    int students = 0;
    int course_id = 0;
    int id = 0;
    String video_id = "";
    public onItemClicked onItemClicked;

    public AdapterSearchResults(Context context, List<SearchResultModelResponse.Result> resultModel) {
        this.context = context;
        this.searchModel = resultModel;
        this.onItemClicked = (AdapterSearchResults.onItemClicked) context;
        inflater = LayoutInflater.from(context);
        this.onItemClicked = (AdapterSearchResults.onItemClicked) context;
    }

    public List<SearchResultModelResponse.Result> getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(List<SearchResultModelResponse.Result> resultModel) {
        if (resultModel != null)
            searchModel.addAll(resultModel);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.search_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = searchModel.get(position).name;
        String title = searchModel.get(position).name;

        if (ContantsUtils.TYPE.equals("videos")) {
            holder.txtTitle.setText(name);

        } else if (ContantsUtils.TYPE.equals("library")) {
            holder.txtTitle.setText(title);
        }else {
            holder.txtTitle.setText(title);
        }

        if (searchModel.get(holder.getAdapterPosition()).videos != null) {
            video_id = searchModel.get(holder.getAdapterPosition()).videos.video_id;
            id = searchModel.get(holder.getAdapterPosition()).videos.id;
        }



        holder.viewSearch.setOnClickListener(v -> {
            course_id = searchModel.get(holder.getAdapterPosition()).id;

            if (searchModel.get(holder.getAdapterPosition()).videos != null) {
                video_id = searchModel.get(holder.getAdapterPosition()).videos.video_id;
                id = searchModel.get(holder.getAdapterPosition()).videos.id;
            }
            switch (ContantsUtils.TYPE) {
                case "videos":
                    onItemClicked.itemClicked(course_id, name, searchModel.get(position).student_count+"", "");
//                    Intent intent = new Intent(context, CourseVideosActivity.class);
//                    intent.putExtra("courseTitle", name);
//                    intent.putExtra("students", String.valueOf(students));
//                    intent.putExtra("courseId", course_id);
//                    context.startActivity(intent);
                    break;

                case "library":
                    onItemClicked.itemClicked(course_id, name, searchModel.get(position).phone, searchModel.get(position).whatsapp);
                    break;

                case "fileVoice":
                    onItemClicked.itemClicked(course_id, name, searchModel.get(position).phone, searchModel.get(position).whatsapp);
                    break;
            }

        });
    }

    @Override
    public int getItemCount() {
        return searchModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        RelativeLayout viewSearch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.tvTitle);
            viewSearch = itemView.findViewById(R.id.viewSearch);

        }
    }

    public interface onItemClicked {
        void itemClicked(int id, String name, String phone, String whatsapp);
    }
}
