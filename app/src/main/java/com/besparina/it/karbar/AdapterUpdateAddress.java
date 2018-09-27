package com.besparina.it.karbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
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
public class AdapterUpdateAddress extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode;

    public AdapterUpdateAddress(Activity activity, ArrayList<HashMap<String, String>> list, String karbarCode) {
        super();
        this.activity = activity;
        this.list = list;

        this.karbarCode = karbarCode;
        dbh = new DatabaseHelper(this.activity.getApplicationContext());
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
        TextView txtNameAddress;
        TextView txtContentAddress;
        ImageView imgPointer;
        ImageView imgEdit;
        ImageView imgDelete;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/IRANSans.ttf");
            convertView = inflater.inflate(R.layout.list_item_address, null);
            holder = new ViewHolder();
            holder.txtNameAddress = (TextView) convertView.findViewById(R.id.txtNameAddress);
            holder.txtNameAddress.setTypeface(faceh);
            holder.txtNameAddress.setTextSize(16);
            holder.txtContentAddress = (TextView) convertView.findViewById(R.id.txtContentAddress);
            holder.txtContentAddress.setTypeface(faceh);
            holder.txtContentAddress.setTextSize(16);
            holder.imgPointer=(ImageView)convertView.findViewById(R.id.imgPointer);
            holder.imgEdit=(ImageView)convertView.findViewById(R.id.imgEdit);
            holder.imgDelete=(ImageView)convertView.findViewById(R.id.imgDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String default_address = map.get("default_address");
        String name = map.get("name");
        String AddressText = map.get("AddressText");
        String code = map.get("Code");
        if(default_address.compareTo("1")==0)
        {
            holder.imgPointer.setImageResource(R.drawable.defualt_pointer);
        }
        else
        {
            holder.imgPointer.setImageResource(R.drawable.pointer);
        }
        holder.imgEdit.setImageResource(R.drawable.edit);
        holder.imgDelete.setImageResource(R.drawable.delete);
        holder.txtNameAddress.setText(PersianDigitConverter.PerisanNumber(name));
        holder.imgEdit.setTag(code);
        holder.imgEdit.setOnClickListener(ImageItemOnclickEdit);
        holder.txtContentAddress.setText(PersianDigitConverter.PerisanNumber(AddressText));
        holder.imgDelete.setTag(code);
        holder.imgDelete.setOnClickListener(ImageItemOnclickDelete);

        return convertView;
    }

    private OnClickListener ImageItemOnclickEdit = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String item = ((ImageView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),UpdateAddress.class);
            intent.putExtra("AddressCode",item);
            intent.putExtra("status","1");
            intent.putExtra("karbarCode",karbarCode);
            activity.startActivity(intent);
        }
    };
    private OnClickListener ImageItemOnclickDelete = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String item = ((ImageView)v).getTag().toString();
//            Intent intent = new Intent(activity.getApplicationContext(),UpdateAddress.class);
//            intent.putExtra("Code",item);
//            intent.putExtra("status","0");
//            activity.startActivity(intent);
            alert_delete(item);
        }
    };
    private OnClickListener TextViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String AddressCode="";
            AddressCode = ((TextView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),UpdateAddress.class);

            intent.putExtra("karbarCode",karbarCode);
            intent.putExtra("AddressCode",AddressCode);
            activity.startActivity(intent);
        }
    };
    private void alert_delete(final String code)
    {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this.activity);
        // set the message to display
        alertbox.setMessage("آیا می خواهید این آدرس را حذف کنید ؟");

        // set a negative/no button and create a listener
        alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        // set a positive/yes button and create a listener
        alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                String name;
                String codeState;
                String CodeCity;
                String StrAddAddres;
                String DetailAddress;
                String latStr;
                String lonStr;
                db=dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM address WHERE Code='"+code+"'",null);
                if(coursors.getCount()>0)
                {
                    coursors.moveToNext();
                    name=coursors.getString(coursors.getColumnIndex("Name"));
                    codeState=coursors.getString(coursors.getColumnIndex("State"));
                    CodeCity=coursors.getString(coursors.getColumnIndex("City"));
                    StrAddAddres=coursors.getString(coursors.getColumnIndex("AddressText"));;
                    DetailAddress="";
                    latStr=coursors.getString(coursors.getColumnIndex("Lat"));
                    lonStr=coursors.getString(coursors.getColumnIndex("Lng"));
                    SyncUpdateAddress syncUpdateAddress =new SyncUpdateAddress(activity,karbarCode,code,"0",name,codeState,CodeCity,StrAddAddres,DetailAddress,latStr,lonStr,"0","1");
                    syncUpdateAddress.AsyncExecute();
                }
                arg0.dismiss();

            }
        });

        alertbox.show();
    }
}

