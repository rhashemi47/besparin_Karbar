package com.besparina.it.karbar;

import android.app.Activity;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

/**
 * Created by hashemi on 02/06/2018.
 */

public class Update {
    private String phonenumber;
    private String acceptcode;

    private String karbarCode;
    private Activity activity;
    private	DatabaseHelper dbh;
    private SQLiteDatabase db;
    public void Update()
    {
        dbh=new DatabaseHelper(this.activity.getApplicationContext());
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
        this.phonenumber=phonenumber;
        this.acceptcode=acceptcode;
        this.karbarCode=karbarCode;
        this.activity=activity;
    }
    public void getEducation()
    {
//        SyncEducation syncEducation=new SyncEducation(this.activity,this.phonenumber,this.acceptcode);
//        syncEducation.AsyncExecute();
    }
    public void getServices()
    {
//        SyncServices syncservices=new SyncServices(this.activity,this.phonenumber,this.acceptcode,"2");
//        syncservices.AsyncExecute();
    }
    public void getJob()
    {
        //todo
    }
    public void getSelectJob()
    {
        //todo
    }
    public void getMessage(String LastMessageCode,String LastHamyarUserServiceCode)
    {
        SyncMessage syncMessage=new SyncMessage(this.activity,karbarCode,LastMessageCode);
        syncMessage.AsyncExecute();
    }
    public void getProfile()
    {
        //todo
    }
    public void getUnit()
    {
        //todo
    }
    public void HmLogin()
    {
//        HmLogin hm=new HmLogin(this.activity, phonenumber, acceptcode);
//        hm.AsyncExecute();
    }
}
