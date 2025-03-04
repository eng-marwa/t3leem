package com.salim.ta3limes.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.CustomTextView;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCenters extends RecyclerView.Adapter<AdapterCenters.ViewHolder> {

    Context context;
    ArrayList<PersonalDataModelResponse.DataBean.CentersBean> centersBeans;
    LayoutInflater inflater;
    private Typeface custom_font;

    SharedPreferencesUtilities preferencesUtilities;

    public AdapterCenters(Context context, ArrayList<PersonalDataModelResponse.DataBean.CentersBean> centersBeans) {
        this.context = context;
        this.centersBeans = centersBeans;
        inflater = LayoutInflater.from(context);
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public ArrayList<PersonalDataModelResponse.DataBean.CentersBean> getCentersBeans() {
        return centersBeans;
    }

    public void setCentersBeans(List<PersonalDataModelResponse.DataBean.CentersBean> centersBeansList) {
        if (centersBeansList != null)
            centersBeans.addAll(centersBeansList);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.center_row, parent, false);
        try {
            custom_font = Typeface.createFromAsset(parent.getContext().getAssets(), "Cairo-Regular.ttf");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PersonalDataModelResponse.DataBean.CentersBean centersBean = centersBeans.get(position);

        if (preferencesUtilities.getCenterId().equals(String.valueOf(centersBean.getId()))){
            holder.done.setVisibility(View.VISIBLE);
        }else {
            holder.done.setVisibility(View.GONE);
        }

        holder.centerName.setText(centersBean.getName());
        holder.videos.setText(String.valueOf(centersBean.getCourse_count()));
        Glide.with(context)
                .load(centersBean.getLogo())
                .error(R.drawable.profile_man)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.centerLogo);
    }

    @Override
    public int getItemCount() {
        return centersBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView centerLogo;
        CustomTextView centerName, videos;
        ImageView done;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            centerLogo = itemView.findViewById(R.id.img_profile);
            centerName = itemView.findViewById(R.id.txt_name);
            videos = itemView.findViewById(R.id.txt_folder);
            done = itemView.findViewById(R.id.done_imageView);

            centerName.setTypeface(custom_font);
            videos.setTypeface(custom_font);

        }
    }
}
