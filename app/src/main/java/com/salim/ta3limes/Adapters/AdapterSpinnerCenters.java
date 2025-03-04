package com.salim.ta3limes.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import java.util.ArrayList;
import java.util.List;


public class AdapterSpinnerCenters extends ArrayAdapter<PersonalDataModelResponse.DataBean.CentersBean> {

    private ArrayList<PersonalDataModelResponse.DataBean.CentersBean> dataBeanList;
    private Context context;

    SharedPreferencesUtilities preferencesUtilities;

    public AdapterSpinnerCenters(Context context, ArrayList<PersonalDataModelResponse.DataBean.CentersBean> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.dataBeanList = items;
        this.context = context;
        preferencesUtilities = new SharedPreferencesUtilities(context);
    }

    public List<PersonalDataModelResponse.DataBean.CentersBean> getDataBeanList() {
        return dataBeanList;
    }

    public void setDataBeanList(List<PersonalDataModelResponse.DataBean.CentersBean> dataList) {
        if (dataList != null)
            dataBeanList.addAll(dataList);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return dataBeanList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            convertView = inflater.inflate(android.R.layout.simple_spinner_item, null);
        }
        TextView lbl = (TextView) convertView.findViewById(android.R.id.text1);
        lbl.setTextColor(Color.TRANSPARENT);
        lbl.setText(dataBeanList.get(position).getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);

        if (v == null) {
            v = new TextView(context);
        }
        v.setTextColor(Color.WHITE);
        v.setBackgroundResource(R.drawable.spinner_item);
        v.setGravity(Gravity.CENTER);
        v.setText(dataBeanList.get(position).getName());
        return v;
    }

    @Override
    public PersonalDataModelResponse.DataBean.CentersBean getItem(int position) {
        return dataBeanList.get(position);
    }

}

