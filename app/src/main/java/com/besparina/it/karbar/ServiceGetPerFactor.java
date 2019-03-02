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
    private Thread thread;
    private Runnable runnable;
    boolean continue_or_stop = true;
    //boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String Code;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public boolean stopService(Intent name) {
        if(PublicVariable.stopthread_GetPerFactor)
        {
            thread.interrupt();
        }
        return super.stopService(name);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(PublicVariable.stopthread_GetPerFactor)
        {
            thread.interrupt();
        }
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
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
            if (PublicVariable.createthread_GetPerFactor) {
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
                                        if (PublicVariable.theard_GetPerFactor) {
                                            try {
                                                if (!db.isOpen()) {
                                                    db = dbh.getReadableDatabase();
                                                }
                                            }catch (Exception ex)
                                            {
                                                db = dbh.getReadableDatabase();
                                            }
                                            Cursor coursors = db.rawQuery("SELECT * FROM OrdersService WHERE  Status in (1,2,6,7,12,13)", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {
                                                coursors.moveToNext();

                                                Code = coursors.getString(coursors.getColumnIndex("Code"));
                                                SyncGetFactorUsersHead syncGetFactorUsersHead = new SyncGetFactorUsersHead(getApplicationContext(), Code,dbh,db);
                                                syncGetFactorUsersHead.AsyncExecute();
                                            }
                                            try {	if (db.isOpen()) {	db.close();if(!coursors.isClosed())
                                                coursors.close();	}}	catch (Exception ex){	}
                                        }
                                    }
                                });
                                Thread.sleep(6000); // every 6 seconds
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                };
                thread=new Thread(runnable);
                if(PublicVariable.stopthread_GetPerFactor)
                {
                    thread.interrupt();
                }
                else {
                    thread.start();
                }
                PublicVariable.createthread_GetPerFactor= false;
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
