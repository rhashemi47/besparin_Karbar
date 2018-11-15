package com.besparina.it.karbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterFactor extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private String karbarCode;

    public AdapterFactor(Activity activity, ArrayList<HashMap<String, String>> list, String karbarCode) {
        super();
        this.activity = activity;
        this.list = list;
        this.karbarCode = karbarCode;
    }

    // @Override
    public int getCount() {
        return list.size();
    }

    // @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtservices;
        TextView txtValueOrUnit;
        TextView txtSum;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/IRANSans.ttf");
            convertView = inflater.inflate(R.layout.list_item_factor, null);
            holder = new ViewHolder();
            holder.txtservices = (TextView) convertView.findViewById(R.id.txtservices);
            holder.txtservices.setTypeface(faceh);
            holder.txtValueOrUnit = (TextView) convertView.findViewById(R.id.txtValueOrUnit);
            holder.txtValueOrUnit.setTypeface(faceh);
            holder.txtSum = (TextView) convertView.findViewById(R.id.txtSum);
            holder.txtSum.setTypeface(faceh);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String services = map.get("services");
        String ValueOrUnit = map.get("ValueOrUnit");
        String Sum = map.get("Sum");
        holder.txtservices.setText(PersianDigitConverter.PerisanNumber(services));
        holder.txtValueOrUnit.setText(PersianDigitConverter.PerisanNumber(ValueOrUnit));
        holder.txtSum.setText(PersianDigitConverter.PerisanNumber(Sum));
        return convertView;
    }
}

