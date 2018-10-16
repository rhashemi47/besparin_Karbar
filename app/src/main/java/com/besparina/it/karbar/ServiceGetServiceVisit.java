package com.besparina.it.karbar;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

public class ServiceGetServiceVisit extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode="0";
    private String LastVersion="0";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
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
            continue_or_stop = true;
            if (createthread) {
                mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        while (continue_or_stop) {
                            try {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetServiceVisit) {
                                            db = dbh.getReadableDatabase();
                                            Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {
                                                coursors.moveToNext();

                                                karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
                                            }
                                            coursors = db.rawQuery("SELECT ifnull(MAX(CAST (Code AS INT)),0)as Code FROM visit", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {
                                                coursors.moveToNext();
                                                LastVersion = coursors.getString(coursors.getColumnIndex("Code"));
                                            }
                                            if (db.isOpen()) {
                                                db.close();
                                            }
                                            SyncGetUserServiceVisit syncGetUserServiceVisit = new SyncGetUserServiceVisit(getApplicationContext(), karbarCode, LastVersion);
                                            syncGetUserServiceVisit.AsyncExecute();
                                        }
                                    }
                                });
                                Thread.sleep(6000); // every 6 seconds
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                }).start();
                createthread = false;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // akeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        continue_or_stop=false;
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
                    db.close();
                return false;
            }
            else
            {
                if(db.isOpen())
                    db.close();
                return true;
            }
        }
        else
        {
            if(db.isOpen())
                db.close();
            return false;
        }
    }
}
