package com.salim.ta3limes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.salim.ta3limes.Activities.PDFViewerActivity;
import com.salim.ta3limes.Models.response.CoursePdfListModel;
import com.salim.ta3limes.Models.response.LibraryModelResponse;
import com.salim.ta3limes.Models.response.PDFsModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.ContantsUtils;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import java.util.ArrayList;
import java.util.List;

public class AdapterCoursePDFs extends RecyclerView.Adapter<AdapterCoursePDFs.ViewHolder> {

    Context context;
    List<CoursePdfListModel.Lessone> filesBeans;
    LayoutInflater inflater;
    private Typeface custom_font;
//    onPLayClicked onPLayClicked;

    SharedPreferencesUtilities preferencesUtilities;

    public AdapterCoursePDFs(Context context, List<CoursePdfListModel.Lessone> centersBeans) {
        this.context = context;
        this.filesBeans = centersBeans;
//        this.onPLayClicked = onPLayClicked;
        inflater = LayoutInflater.from(context);
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public List<CoursePdfListModel.Lessone> getCentersBeans() {
        return filesBeans;
    }

    public void setCoursePDFs(List<CoursePdfListModel.Lessone> filesBeansList) {
        if (filesBeansList != null)
            filesBeans.addAll(filesBeansList);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.pdf_mp3_row, parent, false);

        try {
            custom_font = Typeface.createFromAsset(parent.getContext().getAssets(), "Cairo-Regular.ttf");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CoursePdfListModel.Lessone filesBean = filesBeans.get(position);

        holder.txtName.setText(filesBean.name);
        holder.txtDate.setText(filesBean.create);

        holder.layout.setOnClickListener(v ->
                context.startActivity(new Intent(context, PDFViewerActivity.class).putExtra("fileLink", filesBean.file)
                        .putExtra("download" , filesBean.download).putExtra("title", filesBean.name)));

    }

    @Override
    public int getItemCount() {
        return filesBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView layout;
        TextView txtName, txtDate;
        ImageView img_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.pdf_card);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDate = itemView.findViewById(R.id.txt_date);
            img_type = itemView.findViewById(R.id.img_type);

            txtName.setTypeface(custom_font);
            txtDate.setTypeface(custom_font);

        }
    }

//    public interface onPLayClicked{
//        void onPlayClicked(int id, String voiceId);
//    }

    public void setFilter(ArrayList<CoursePdfListModel.Lessone> newlist) {
        filesBeans = new ArrayList<>();
        filesBeans.addAll(newlist);
        notifyDataSetChanged();
    }
}
