package com.besparina.it.karbar;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * Created by hashemi on 02/18/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SchaduleServiceGetLocation extends JobService {
    Handler mHandler;
    boolean continue_or_stop = true;
//    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;

    private double latitude;
    private double longitude;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        dbh = new DatabaseHelper(getApplicationContext());
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
        if(Check_Login()) {
            continue_or_stop = true;
//            if (createthread) {
                mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (continue_or_stop) {
                            try {
                                sleep(60000); // every 60 seconds
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        GPSTracker gps = new GPSTracker(getApplicationContext());

                                        // check if GPS enabled
                                        if (gps.canGetLocation()) {
                                            db = dbh.getReadableDatabase();
                                            Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
                                            if (coursors.getCount() > 0) {
                                                Cursor c = db.rawQuery("SELECT * FROM login", null);
                                                if (c.getCount() > 0) {
                                                    c.moveToNext();
                                                    latitude = gps.getLatitude();
                                                    longitude = gps.getLongitude();
                                                    String query = "UPDATE Profile SET Lat='" + Double.toString(latitude) + "',Lon='" + Double.toString(longitude) + "'";
                                                    try {
                                                        if (!db.isOpen()) {
                                                            db = dbh.getWritableDatabase();
                                                        }
                                                    } catch (Exception ex) {
                                                        db = dbh.getWritableDatabase();
                                                    }
                                                    db.execSQL(query);
                                                    try {
                                                        if (db.isOpen()) {
                                                            db.close();
                                                        }
                                                    } catch (Exception ex) {
                                                    }
                                                }
                                            }
                                            try {
                                                if (db.isOpen()) {
                                                    db.close();
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }

                                    }
                                });
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();
//                createthread = false;
//            }
        }
        return false;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        continue_or_stop=false;
        return false;
    }
    public boolean Check_Login()
    {
        Cursor cursor;
        if(db==null)
        {
            db = dbh.getReadableDatabase();
        }
        if(!db.isOpen()) {
            db = dbh.getReadableDatabase();
        }
        cursor = db.rawQuery("SELECT * FROM login", null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            String Result = cursor.getString(cursor.getColumnIndex("islogin"));
            if (Result.compareTo("0") == 0)
            {
                if(db.isOpen())
                    try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
                return false;
            }
            else
            {
                if(db.isOpen())
                    try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
                return true;
            }
        }
        else
        {
            if(db.isOpen())
                try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
            return false;
        }
    }

}
