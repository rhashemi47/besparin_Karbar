package com.besparina.it.karbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

public class CreateOrUpdateTable {
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    public  CreateOrUpdateTable(Activity activity)//سازنده کلاس
    {
        dbh = new DatabaseHelper(activity.getBaseContext());
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
    }
    public boolean isFieldExistTable(String table_name)// تشخیص وجود جدول
    {
        if (table_name == null)
        {
            return false;
        }
        try {
            if (db == null || !db.isOpen()) {
                db = dbh.getReadableDatabase();
            }
        }catch (Exception ex)
        {
            db = dbh.getReadableDatabase();
        }

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", table_name});
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        try {
            if (db.isOpen()) {
                db.close();
                cursor.close();
            }
        }catch (Exception ex)
        {
            Log.e("Couldn't Open DB","Couldn't Open DB");
        }
        return count > 0;
    }
    public boolean isFieldExistFieldName(String table_name,String fieldName)// تشخیص وجود فیلد در جدول
    {
        boolean isExist = false;
        if(db == null || !db.isOpen())
        {
            try {
                if (db == null || !db.isOpen()) {
                    db = dbh.getReadableDatabase();
                }
            }catch (Exception ex)
            {
                db = dbh.getReadableDatabase();
            }
        }
        @SuppressLint("Recycle") Cursor res = db.rawQuery("PRAGMA table_info("+table_name+")",null);
        res.moveToFirst();
        do {
            String currentColumn = res.getString(1);
            if (currentColumn.equals(fieldName)) {
                isExist = true;
            }
        } while (res.moveToNext());
        return isExist;
    }
    public boolean Create_Table(String table_name,String[] field_name)// ایجاد جدول
    {
        if(field_name.length>0 && table_name!=null && !table_name.equals(" ")) {
            String CREATE_TABLE = "CREATE TABLE" + " table_name " + "( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, ";
            for(int i=0;i<field_name.length;i++)
            {
                CREATE_TABLE=CREATE_TABLE + field_name[i]+ ", ";
            }
            CREATE_TABLE=CREATE_TABLE+" )";
            if(db == null || !db.isOpen())
            {
                try {
                    if (db == null || !db.isOpen()) {
                        db = dbh.getWritableDatabase();
                    }
                }catch (Exception ex)
                {
                    db = dbh.getWritableDatabase();
                }
            }
            try {
                db.execSQL(CREATE_TABLE);
                return true;
            }catch (Exception ex)
            {
                return false;
            }
        }
        return false;
    }
    public boolean Update_Table(String table_name,String[] field_name,String type)//به روز رسانی جدول
    {
        String CREATE_FIELD="ALTER TABLE "
                + table_name + " ADD COLUMN " + field_name + " " + type + ";";
        if(db == null || !db.isOpen())
        {
            try {
                if (db == null || !db.isOpen()) {
                    db = dbh.getWritableDatabase();
                }
            }catch (Exception ex)
            {
                db = dbh.getWritableDatabase();
            }
        }
        try {
            db.execSQL(CREATE_FIELD);
            return true;
        }catch (Exception ex)
        {
            return false;
        }
    }
}
