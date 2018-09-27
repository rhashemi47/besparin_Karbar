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
public class AdapterHistory extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;

    private String karbarCode;

    public AdapterHistory(Activity activity, ArrayList<HashMap<String, String>> list, String karbarCode) {
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
        TextView txtValues;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/IRANSans.ttf");
            convertView = inflater.inflate(R.layout.list_item_visit, null);
            holder = new ViewHolder();
            holder.txtValues = (TextView) convertView.findViewById(R.id.txtContentVisit);
            holder.txtValues.setTypeface(faceh);
            holder.txtValues.setTextSize(24);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = map.get("name");
        String code = map.get("Code");
        holder.txtValues.setText(PersianDigitConverter.PerisanNumber(name));
        holder.txtValues.setTag(code);
//        holder.txtValues.setOnClickListener(TextViewItemOnclick);

        return convertView;
    }


//    private OnClickListener TextViewItemOnclick = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            String BsUserServicesID="";
//            BsUserServicesID = ((TextView)v).getTag().toString();
//            Intent intent = new Intent(activity.getApplicationContext(),ViewJob.class);
//
//            intent.putExtra("karbarCode",karbarCode);
//            intent.putExtra("BsUserServicesID",BsUserServicesID);
//            intent.putExtra("tab","0");
//            activity.startActivity(intent);
//        }
//    };
}

