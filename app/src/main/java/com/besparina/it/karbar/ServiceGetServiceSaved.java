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

public class ServiceGetServiceSaved extends Service {
    Handler mHandler;
    private Thread thread;
    private Runnable runnable;
    boolean continue_or_stop = true;
    //boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public boolean stopService(Intent name) {
        if(PublicVariable.stopthread_GetServiceSaved)
        {
            thread.interrupt();
        }
        return super.stopService(name);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(PublicVariable.stopthread_GetServiceSaved)
        {
            thread.interrupt();
        }
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
            if (PublicVariable.createthread_GetServiceSaved) {
                mHandler = new Handler();
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        while (continue_or_stop) {
                            try {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_GetServiceSaved) {
                                            try {
                                                if (!db.isOpen()) {
                                                    db = dbh.getReadableDatabase();
                                                }
                                            }catch (Exception ex)
                                            {
                                                db = dbh.getReadableDatabase();
                                            }
                                            Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {
                                                coursors.moveToNext();

                                                karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
                                            }
                                            try {	if (db.isOpen()) {	db.close();if(!coursors.isClosed())
                                                coursors.close();	}}	catch (Exception ex){	}
                                            SyncGetUserServices syncGetUserServices = new SyncGetUserServices(getApplicationContext(), karbarCode, "0",dbh,db);
                                            syncGetUserServices.AsyncExecute();
                                        }
                                    }
                                });

                                Thread.sleep(5000); // every 6 seconds
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }

                    }
                };
                thread=new Thread(runnable);
                if(PublicVariable.stopthread_GetServiceSaved)
                {
                    thread.interrupt();
                }
                else {
                    thread.start();
                }
                PublicVariable.createthread_GetServiceSaved= false;
            }
        }
        return START_STICKY;
    }

    public boolean Check_Login()
    {
            Cursor cursor;
        try {
            if (!db.isOpen()) {
                db = dbh.getReadableDatabase();
            }
        }catch (Exception ex)
        {
            db = dbh.getReadableDatabase();
        }
            cursor = db.rawQuery("SELECT * FROM login", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                String Result = cursor.getString(cursor.getColumnIndex("islogin"));
                if (Result.compareTo("0") == 0)
                {
                    try {	if (db.isOpen()) {	db.close();if(!cursor.isClosed())
                        cursor.close();	}}	catch (Exception ex){	}
                    return false;
                }
                else
                {
                    try {	if (db.isOpen()) {	db.close();if(!cursor.isClosed())
                        cursor.close();	}}	catch (Exception ex){	}
                    return true;
                }
            }
            else
            {
                try {	if (db.isOpen()) {	db.close();if(!cursor.isClosed())
                    cursor.close();	}}	catch (Exception ex){	}
                return false;
            }
    }
}
