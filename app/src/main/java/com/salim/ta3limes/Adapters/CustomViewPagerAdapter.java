package com.salim.ta3limes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.salim.ta3limes.Models.response.StartModel;
import com.salim.ta3limes.R;

import java.util.ArrayList;
import java.util.List;

public class CustomViewPagerAdapter extends PagerAdapter {

  private List<StartModel> list=new ArrayList<>();
  private Context mContext;
  public void setList(List<StartModel> list)
  {
      this.list=list;
      notifyDataSetChanged();
  }
    public CustomViewPagerAdapter(Context mContext) {

        this.mContext=mContext;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_start, container, false);

        ((TextView)layout.findViewById(R.id.tvStartMessage)).setText(list.get(position).getContent());
        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return  view == object;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

}
