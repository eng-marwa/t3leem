package com.salim.ta3limes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.salim.ta3limes.Activities.CourseAndCommentsActivity;
import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Activities.PDFViewerActivity;
import com.salim.ta3limes.Activities.VideoChatActivity;
import com.salim.ta3limes.Models.response.NotificationModelResponse;
import com.salim.ta3limes.R;

import java.util.List;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {

    Context context;
    List<NotificationModelResponse.DataBeanX.NotificationsBean> notificationModel;
    LayoutInflater inflater;

    public AdapterNotification(Context context, List<NotificationModelResponse.DataBeanX.NotificationsBean> notificationModel) {
        this.context = context;
        this.notificationModel = notificationModel;
        inflater = LayoutInflater.from(context);
    }

    public List<NotificationModelResponse.DataBeanX.NotificationsBean> getNotificationModel() {
        return notificationModel;
    }

    public void setNotificationModel(List<NotificationModelResponse.DataBeanX.NotificationsBean> notificationModel) {
        this.notificationModel = notificationModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.notification_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txt_content.setText(notificationModel.get(position).getData().getDescription());
        holder.txt_time.setText(notificationModel.get(position).getData().getDuration());

        String file = notificationModel.get(position).getData().getFile();
        String courseID = notificationModel.get(position).getData().getCourse_id();
        String type = notificationModel.get(position).getData().getType();
        String name = notificationModel.get(position).getData().getName();
        String title = notificationModel.get(position).getData().getTitle();
        String token = notificationModel.get(position).getData().getToken();
        String channel = notificationModel.get(position).getData().getChannel_name();
        int notification_id = notificationModel.get(position).getData().getId();
        String video_id = notificationModel.get(position).getData().getVideo_id();
        String maximum_views_allowed = notificationModel.get(position).getData().getMaximum_views_allowed();
        String lesson_id = notificationModel.get(position).getData().getLesson_id();
        String read_at = String.valueOf(notificationModel.get(position).getRead_at());
        String zoom_link = notificationModel.get(position).getData().getZoom_link();

        if (read_at.equals(null)) {
            return;
        }

        holder.notification_card.setOnClickListener(v -> {

            if (token != null && !token.isEmpty() && type.equals("live_vide") && channel != null && !channel.isEmpty()) {
                Intent intent = new Intent(context, VideoChatActivity.class);
                intent.putExtra("live_token", token);
                intent.putExtra("channel_name", channel);
                intent.putExtra("live_video_id", String.valueOf(notification_id));
                context.startActivity(intent);
            } else if (type.equals("pdf")) {
                Intent intent = new Intent(context, PDFViewerActivity.class);
                intent.putExtra("id", notification_id);
                intent.putExtra("fileLink", file);
                intent.putExtra("videoTitle", title);
                context.startActivity(intent);
            } else if (type.equals("video")) {
                Log.e("NotiAdapter", "onBindViewHolder: maximum_views_allowed >> " + maximum_views_allowed);
                if (!maximum_views_allowed.equals("0")) {
                    Intent intent = new Intent(context, CourseAndCommentsActivity.class);
                    intent.putExtra("videoId", video_id);
                    intent.putExtra("id", notification_id);
                    intent.putExtra("videoTitle", name);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "انتهت عدد المشاهدات لديك لهذا الفيديو !", Toast.LENGTH_SHORT).show();
                }

            } else if (type.equals("live_vide_zoom")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(zoom_link));
                context.startActivity(intent);
            }else if (type.equals("block")){
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView notification_card;
        TextView txt_content, txt_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_content = itemView.findViewById(R.id.txt_content);
            txt_time = itemView.findViewById(R.id.txt_time);
            notification_card = itemView.findViewById(R.id.notification_card);
        }
    }
}
