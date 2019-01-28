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

/**
 * Created by hashemi on 02/18/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SchaduleServiceGetServicesAndServiceDetails extends JobService {
    Handler mHandler;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;

    @Override
    public void onDestroy() {
        super.onDestroy();
       // akeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

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
                mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                            try {
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetServicesAndServiceDetails) {

                                            SyncServicesForService syncServicesForService = new SyncServicesForService(getApplicationContext());
                                            syncServicesForService.AsyncExecute();

                                        }
                                    }
                                });
//                                Thread.sleep(43200000); // every 12 hour
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                }).start();
            }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
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
