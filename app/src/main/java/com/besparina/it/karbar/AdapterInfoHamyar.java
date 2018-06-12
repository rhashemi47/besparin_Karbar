package com.besparina.it.karbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterInfoHamyar extends BaseAdapter {


    private String karbarCode;
    private int check_tab;
    public ArrayList<HashMap<String, String>> list;
    Activity activity;

    public AdapterInfoHamyar(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
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
        ImageView imgHamyar;
        Button btnMobileHamyar;
    }

    // @Override
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/BMitra.ttf");
            convertView = inflater.inflate(R.layout.list_item_hamyar, null);
            holder = new ViewHolder();
            holder.imgHamyar = (ImageView)convertView.findViewById(R.id.imgHamyar);
            holder.btnMobileHamyar=(Button)convertView.findViewById(R.id.btnMobileHamyar);
            holder.btnMobileHamyar.setTypeface(faceh);
            holder.btnMobileHamyar.setTextSize(18);
            holder.txtValues = (TextView) convertView.findViewById(R.id.txtContentHamyar);
            holder.txtValues.setTypeface(faceh);
            holder.txtValues.setTextSize(18);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String Content = map.get("Content");
        String imgHamyar = map.get("imgHamyar");
        String Mobile = map.get("Mobile");
        String Visit = map.get("Visit");
        if(Visit.compareTo("")!=0)
        {
            holder.txtValues.setText(PersianDigitConverter.PerisanNumber(Content+"\n"+Visit));
        }
        else
        {
            holder.txtValues.setText(PersianDigitConverter.PerisanNumber(Content));
        }
        if(imgHamyar.compareTo("0")==0)
        {
            holder.imgHamyar.setImageResource(R.drawable.useravatar);
        }
        else
        {
            holder.imgHamyar.setImageBitmap(convertToBitmap(imgHamyar));
        }
        holder.btnMobileHamyar.setTag(Mobile);
        holder.btnMobileHamyar.setOnClickListener(ButtonItemOnClick);
        return convertView;
    }
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
    private OnClickListener ButtonItemOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String UserPhone="";
            UserPhone = ((Button)v).getTag().toString();
            if (ActivityCompat.checkSelfPermission(activity,
                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.CALL_PHONE))
                {
                    //do nothing
                }
                else{

                    ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.CALL_PHONE},2);
                }

            }
            dialContactPhone(UserPhone);
        }
    };
    public void dialContactPhone(String phoneNumber) {
        //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        activity.startActivity(callIntent);
    }
//    private OnClickListener TextViewItemOnclick = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            String item = ((TextView)v).getTag().toString();
//            Intent intent = new Intent(activity.getApplicationContext(),ShowMessage.class);
//            intent.putExtra("Code",item);
//            activity.startActivity(intent);
//        }
//    };
//    private OnClickListener ImageItemOnclick = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            String item = ((ImageView)v).getTag().toString();
//            Intent intent = new Intent(activity.getApplicationContext(),ShowMessage.class);
//            intent.putExtra("Code",item);
//            activity.startActivity(intent);
//        }
//    };

}

