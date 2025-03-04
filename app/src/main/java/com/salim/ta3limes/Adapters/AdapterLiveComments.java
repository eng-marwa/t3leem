package com.salim.ta3limes.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.salim.ta3limes.Models.response.LiveCommentsModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterLiveComments extends RecyclerView.Adapter<AdapterLiveComments.ViewHolder> {

    Context context;
    ArrayList<LiveCommentsModelResponse.DataBean.CommentsBean> commentsBeans;
    LayoutInflater inflater;
    private Typeface custom_font;

    public AdapterLiveComments(Context context, ArrayList<LiveCommentsModelResponse.DataBean.CommentsBean> commentsBeans) {
        this.context = context;
        this.commentsBeans = commentsBeans;
        inflater = LayoutInflater.from(context);
    }

    public ArrayList<LiveCommentsModelResponse.DataBean.CommentsBean> getCommentsBeans() {
        return commentsBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.live_comment_row, parent, false);
        try {
            custom_font = Typeface.createFromAsset(parent.getContext().getAssets(), "Cairo-Regular.ttf");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LiveCommentsModelResponse.DataBean.CommentsBean commentsBean = commentsBeans.get(position);

        Glide.with(context)
                .load(commentsBean.getUser().getImage())
                .error(R.drawable.profile_man)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imageView);

        holder.name.setText(commentsBean.getUser().getName());
        holder.comment.setText(commentsBean.getComment());
    }

    @Override
    public int getItemCount() {
        return commentsBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomTextView name, comment;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_textView);
            comment = itemView.findViewById(R.id.comment_textView);
            imageView = itemView.findViewById(R.id.img_profile);

            name.setTypeface(custom_font);
            comment.setTypeface(custom_font);
        }
    }
}
