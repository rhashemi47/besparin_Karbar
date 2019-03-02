package com.besparina.it.karbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterServiceDetails extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;

    private String karbarCode;

    public AdapterServiceDetails(Activity activity, ArrayList<HashMap<String, String>> list, String karbarCode) {
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
        ImageView imgValues;
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
            holder.imgValues = (ImageView) convertView.findViewById(R.id.imgIcon);
            holder.txtValues.setTypeface(faceh);
            holder.txtValues.setTextSize(16);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = map.get("name");
        String code = map.get("Code");
        String Pic_Code = map.get("Pic_Code");
        holder.txtValues.setText(PersianDigitConverter.PerisanNumber(name));
        dbh=new DatabaseHelper(activity);
        try {

            dbh.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            dbh.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;
        }
        try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
        Cursor cursor = db.rawQuery("SELECT * FROM PicServices WHERE Code='"+Pic_Code+"'",null);
        if(cursor.getCount()>0)
        {
            cursor.moveToNext();
            if(cursor.getString(cursor.getColumnIndex("Pic")).length()>5){
                holder.imgValues.setImageBitmap(convertToBitmap(cursor.getString(cursor.getColumnIndex("Pic"))));
                holder.txtValues.setTag(code);
                holder.imgValues.setTag(code);
            }
            else
            {
                holder.imgValues.setImageResource(R.drawable.job);
                holder.txtValues.setTag(code);
                holder.imgValues.setTag(code);
            }

        }
        else
        {
            holder.imgValues.setImageResource(R.drawable.job);
        }
        try {
            if (db.isOpen())
        {
            db.close();
            if(!cursor.isClosed())
            {
                cursor.close();
            }
        }
        }	catch (Exception ex){	}
        holder.txtValues.setOnClickListener(TextViewItemOnclick);
        holder.imgValues.setOnClickListener(ImageViewItemOnclick);
        cursor.close();
        return convertView;
    }


    private OnClickListener TextViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String DetailCode="";
            DetailCode = ((TextView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),Service_Request.class);//Goto Page Form Order Service
            intent.putExtra("karbarCode",karbarCode);
            intent.putExtra("DetailCode",DetailCode);
            activity.startActivity(intent);
        }
    };
    private OnClickListener ImageViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String DetailCode="";
            DetailCode = ((ImageView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),Service_Request.class);//Goto Page Form Order Service
            intent.putExtra("karbarCode",karbarCode);
            intent.putExtra("DetailCode",DetailCode);
            activity.startActivity(intent);
        }
    };
    public Bitmap convertToBitmap(String base){
        Bitmap Bmp=null;
        try
        {
            byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
            Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
            return Bmp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Bmp;
        }
    }
}

