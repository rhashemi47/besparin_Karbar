package com.besparina.it.karbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterServices extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;

    private String karbarCode;
    private String QueryCustom;

    public AdapterServices(Activity activity, ArrayList<HashMap<String, String>> list, String karbarCode) {
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
        TextView txtTitleOrder;
        TextView txtNumberOrder;
        TextView txtDate;
        TextView txtTime;
        TextView txtAddres;
        TextView txtEmergency;
        TextView txtStatus;
        LinearLayout mainLayout;
        LinearLayout LinearTitle;
    }

    // @Override
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
//        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/IRANSans.ttf");
            convertView = inflater.inflate(R.layout.list_item_order, null);
            holder = new ViewHolder();
            holder.mainLayout=(LinearLayout)convertView.findViewById(R.id.MainLayout);
            holder.LinearTitle=(LinearLayout)convertView.findViewById(R.id.LinearTitle);
            holder.txtTitleOrder = (TextView) convertView.findViewById(R.id.txtTitleOrder);
            holder.txtTitleOrder.setTypeface(faceh);
//            holder.txtTitleOrder//.setTextSize(16);
            holder.txtNumberOrder = (TextView) convertView.findViewById(R.id.txtNumberOrder);
            holder.txtNumberOrder.setTypeface(faceh);
//            holder.txtNumberOrder//.setTextSize(16);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtDate.setTypeface(faceh);
//            holder.txtDate//.setTextSize(16);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtTime.setTypeface(faceh);
//            holder.txtTime//.setTextSize(16);
            holder.txtAddres = (TextView) convertView.findViewById(R.id.txtAddres);
            holder.txtAddres.setTypeface(faceh);
//            holder.txtAddres//.setTextSize(16);
            holder.txtEmergency = (TextView) convertView.findViewById(R.id.txtEmergency);
            holder.txtEmergency.setTypeface(faceh);
//            holder.txtEmergency//.setTextSize(16);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.txtStatus.setTypeface(faceh);
//            holder.txtStatus//.setTextSize(16);
            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        String TitleOrder = map.get("TitleOrder");
        String NumberOrder = map.get("NumberOrder");
        String DateOrder = map.get("DateOrder");
        String TimeOrder = map.get("TimeOrder");
        String AddressOrder = map.get("AddressOrder");
        String EmergencyOrder = map.get("EmergencyOrder");
        String StatusOrder = map.get("StatusOrder");
        QueryCustom = map.get("QueryCustom");
        holder.txtTitleOrder .setText(TitleOrder);
        holder.txtNumberOrder .setText(PersianDigitConverter.PerisanNumber(NumberOrder));
        holder.txtDate .setText(PersianDigitConverter.PerisanNumber(DateOrder));
        holder.txtTime .setText(PersianDigitConverter.PerisanNumber(TimeOrder));
        holder.txtAddres .setText(PersianDigitConverter.PerisanNumber(AddressOrder));
        holder.txtEmergency .setText(PersianDigitConverter.PerisanNumber(EmergencyOrder));
        holder.txtStatus .setText(PersianDigitConverter.PerisanNumber(StatusOrder));
        holder.mainLayout.setTag(NumberOrder);
        holder.mainLayout.setOnClickListener(TextViewItemOnclick);
        if(EmergencyOrder.compareTo("فوری")==0)
        {
            holder.LinearTitle.setBackgroundColor(Color.parseColor("#d97170"));
        }
        else
        {
            holder.LinearTitle.setBackgroundColor(Color.parseColor("#70bd85"));
        }
        return convertView;
    }


    private OnClickListener TextViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String OrderCode="";
            OrderCode = ((LinearLayout)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),Service_Request_Saved.class);

            intent.putExtra("karbarCode",karbarCode);
            intent.putExtra("OrderCode",OrderCode);
            intent.putExtra("QueryCustom",QueryCustom);
            activity.startActivity(intent);
        }
    };
}

