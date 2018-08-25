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

public class ServiceGetPerFactor extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String Code;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        continue_or_stop=true;
        if(createthread) {
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
                                    dbh=new DatabaseHelper(getApplicationContext());
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
                                    db=dbh.getReadableDatabase();
                                    Cursor coursors = db.rawQuery("SELECT * FROM OrdersService WHERE  Status in (1,2,6,7,12,13)",null);
                                    for(int i=0;i<coursors.getCount();i++){
                                        coursors.moveToNext();

                                        Code=coursors.getString(coursors.getColumnIndex("Code"));
                                    }
                                    db.close();
                                    SyncGetFactorUsersHead syncGetFactorUsersHead=new SyncGetFactorUsersHead(getApplicationContext(),Code);
                                    syncGetFactorUsersHead.AsyncExecute();
                                    SyncGetFaktorUserDetailes syncGetFaktorUserDetailes=new SyncGetFaktorUserDetailes(getApplicationContext(),Code);
                                    syncGetFaktorUserDetailes.AsyncExecute();
                                }
                            });
                            Thread.sleep(10000); // every 60 seconds
                        } catch (Exception e) {
                            // TODO: handle exception
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
       // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        continue_or_stop=false;
    }
}
