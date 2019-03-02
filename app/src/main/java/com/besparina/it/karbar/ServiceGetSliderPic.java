package com.besparina.it.karbar;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;

import com.google.android.gms.games.leaderboard.LeaderboardVariant;

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

public class ServiceGetSliderPic extends Service {
    Handler mHandler;
    private Thread thread;
    private Runnable runnable;
    boolean continue_or_stop = true;
    //boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode="0";
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public boolean stopService(Intent name) {
        if(PublicVariable.stopthread_GetSliderPic)
        {
            thread.interrupt();
        }
        return super.stopService(name);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(PublicVariable.stopthread_GetSliderPic)
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
            if (PublicVariable.createthread_GetSliderPic) {
                mHandler = new Handler();
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        if (PublicVariable.theard_GetSliderPic) {
                            while (continue_or_stop) {
                                try {
                                    mHandler.post(new Runnable() {

                                        @Override
                                        public void run() {


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
                                            if (karbarCode.compareTo("0") != 0) {

                                                SyncSliderPic syncSliderPic = new SyncSliderPic(getApplicationContext(), karbarCode,dbh,db);
                                                syncSliderPic.AsyncExecute();
                                            }
                                            try {	if (db.isOpen()) {	db.close();if(!coursors.isClosed())
                                                coursors.close();	}}	catch (Exception ex){	}


                                        }
                                    });
                                    db = dbh.getReadableDatabase();
                                    Cursor cursor = db.rawQuery("SELECT * FROM Slider", null);

                                    if (cursor.getCount() > 0) {
                                        Thread.sleep(43200000); // every 12 hour
                                    } else {
                                        Thread.sleep(6000); // every 6 Second
                                    }

                                    try {	if (db.isOpen()) {	db.close();if(!cursor.isClosed())
                                        cursor.close();	}}	catch (Exception ex){	}
                                } catch (Exception e) {
                                    String error = "";
                                    error = e.getMessage().toString();
                                }

                            }
                        }
                    }
                };
                thread=new Thread(runnable);
                if(PublicVariable.stopthread_GetSliderPic)
                {
                    thread.interrupt();
                }
                else {
                    thread.start();
                }
                PublicVariable.createthread_GetSliderPic= false;
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
