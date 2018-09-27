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
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode="0";
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        akeText(this, "Service Started", Toast.LENGTH_LONG).show();
        continue_or_stop=true;
        if(createthread) {
            mHandler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (PublicVariable.theard_GetSliderPic) {
                        while (continue_or_stop) {
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
                            try {
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {


                                        db = dbh.getReadableDatabase();
                                        Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                                        for (int i = 0; i < coursors.getCount(); i++) {
                                            coursors.moveToNext();
                                            karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
                                        }
                                        if (karbarCode.compareTo("0") != 0) {

                                            SyncSliderPic syncSliderPic = new SyncSliderPic(getApplicationContext(), karbarCode);
                                            syncSliderPic.AsyncExecute();
                                        }
                                        db.close();


                                    }
                                });
                                db = dbh.getReadableDatabase();
                                Cursor cursor = db.rawQuery("SELECT * FROM Slider", null);

                                if (cursor.getCount() > 0) {
                                    Thread.sleep(43200000); // every 12 hour
                                } else {
                                    Thread.sleep(6000); // every 6 Second
                                }

                                db.close();
                            } catch (Exception e) {
                                String error = "";
                                error = e.getMessage().toString();
                            }

                        }
                    }
                }
            }).start();
            createthread=false;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // akeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        continue_or_stop=false;
    }
}
