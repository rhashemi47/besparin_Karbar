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

import com.besparina.it.karbar.Date.ChangeDate;

import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * Created by hashemi on 02/18/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SchaduleServiceSyncMessage extends JobService {
    Handler mHandler;
    boolean continue_or_stop = true;
//    boolean createthread=true;
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
            continue_or_stop = true;
//            if (createthread) {
                mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        while (continue_or_stop) {
                            try {
                                sleep(60000); // every 60 seconds
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (PublicVariable.theard_Message) {

                                            db = dbh.getReadableDatabase();
                                            Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                                            for (int i = 0; i < coursors.getCount(); i++) {
                                                coursors.moveToNext();

                                                karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
                                            }
                                            try {	if (db.isOpen()) {	db.close();		if(!coursors.isClosed())
                                                coursors.close();}}	catch (Exception ex){	}
                                            db = dbh.getReadableDatabase();
                                            Cursor cursor = db.rawQuery("SELECT * FROM messages WHERE IsSend='0' AND IsReade='1'", null);
                                            for (int i = 0; i < cursor.getCount(); i++) {
                                                cursor.moveToNext();
                                                String DateSp[] = ChangeDate.getCurrentDate().split("/");
                                                String Year = DateSp[0];
                                                String Month = DateSp[1];
                                                String Day = DateSp[2];
                                                String code = cursor.getString(cursor.getColumnIndex("Code"));
                                                while (PublicVariable.theard_ReadMessage) {
                                                    SyncReadMessage readMessage = new SyncReadMessage(getApplicationContext(), karbarCode, code, Year, Month, Day,dbh,db);
                                                    readMessage.AsyncExecute();
//                                                    try {
////                                                        sleep(6000);
//                                                    } catch (InterruptedException e) {
//                                                        e.printStackTrace();
//                                                    }
                                                }
                                            }
                                            String LastMessageCode = "0";
                                            try {	if (db.isOpen()) {	db.close();		if(!coursors.isClosed())
                                                coursors.close();}}	catch (Exception ex){	}
                                            db = dbh.getReadableDatabase();
                                            cursor = db.rawQuery("SELECT ifnull(MAX(CAST (code AS INT)),0)as code FROM messages", null);
                                            if (cursor.getCount() > 0) {
                                                cursor.moveToNext();
                                                LastMessageCode = cursor.getString(cursor.getColumnIndex("code"));
                                            }
                                            try {	if (db.isOpen()) {	db.close();if(!cursor.isClosed())
                                                cursor.close();	}}	catch (Exception ex){	}
                                            SyncMessage syncMessage = new SyncMessage(getApplicationContext(), karbarCode, LastMessageCode,dbh,db);
                                            syncMessage.AsyncExecute();
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
                try {	if (db.isOpen()) {	db.close();		if(!cursor.isClosed())
                    cursor.close();}}	catch (Exception ex){	}
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
