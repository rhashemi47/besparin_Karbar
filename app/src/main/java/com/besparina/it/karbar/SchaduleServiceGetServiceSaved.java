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
public class SchaduleServiceGetServiceSaved extends JobService {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // Let it continue running until it is stopped.
//        akeText(this, "Service Started", Toast.LENGTH_LONG).show();
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
            continue_or_stop=true;
                mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (continue_or_stop) {
                            try {
                                sleep(6000); // every 6 seconds
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetServiceSaved) {
                                            db = dbh.getReadableDatabase();
                                            Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {
                                                coursors.moveToNext();

                                                karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
                                            }
                                            try {
                                                if (db.isOpen()) {
                                                    db.close();
                                                }
                                            } catch (Exception ex) {
                                            }
                                            SyncGetUserServices syncGetUserServices = new SyncGetUserServices(getApplicationContext(), karbarCode, "0",dbh,db);
                                            syncGetUserServices.AsyncExecute();
                                        }
                                    }
                                });

//                                Thread.sleep(5000); // every 6 seconds
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();
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
