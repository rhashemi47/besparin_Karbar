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
public class SchaduleServiceGetSliderPic extends JobService {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode="0";

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
            continue_or_stop = true;
            if (createthread) {
                mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (PublicVariable.theard_GetSliderPic) {
                            while (continue_or_stop) {
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

                                                SyncSliderPic syncSliderPic = new SyncSliderPic(getApplicationContext(), karbarCode,dbh,db);
                                                syncSliderPic.AsyncExecute();
                                            }
                                            try {	if (db.isOpen()) {	db.close();		if(!coursors.isClosed())
                                                coursors.close();}}	catch (Exception ex){	}


                                        }
                                    });
                                    db = dbh.getReadableDatabase();
                                    Cursor cursor = db.rawQuery("SELECT * FROM Slider", null);

                                    if (cursor.getCount() > 0) {
                                        if(!cursor.isClosed())
                                            cursor.close();
                                        Thread.sleep(43200000); // every 12 hour
                                    } else {
                                        if(!cursor.isClosed())
                                            cursor.close();
                                        Thread.sleep(6000); // every 6 Second
                                    }

                                    try {	if (db.isOpen()) {	db.close();		if(!cursor.isClosed())
                                        cursor.close();}}	catch (Exception ex){	}
                                } catch (Exception e) {
                                    String error = "";
                                    error = e.getMessage().toString();
                                }

                            }
                        }
                    }
                }).start();
                createthread = false;
            }
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        continue_or_stop=false; return false;
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
                    try {	if (db.isOpen()) {	db.close();if(!cursor.isClosed())
                        cursor.close();	}}	catch (Exception ex){	}
                return false;
            }
            else
            {
                if(db.isOpen())
                    try {	if (db.isOpen()) {	db.close();if(!cursor.isClosed())
                        cursor.close();	}}	catch (Exception ex){	}
                return true;
            }
        }
        else
        {
            if(db.isOpen())
                try {	if (db.isOpen()) {	db.close();	if(!cursor.isClosed())
                    cursor.close();}}	catch (Exception ex){	}
            return false;
        }
    }
}
