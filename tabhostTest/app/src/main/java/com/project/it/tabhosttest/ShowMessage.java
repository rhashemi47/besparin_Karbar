package com.besparina.it.karbar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.besparina.it.karbar.Date.ChangeDate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hashemi on 01/23/2018.
 */

public class ShowMessage extends Activity{
    private String hamyarcode;
    private String guid;
    private	DatabaseHelper dbh;
    private SQLiteDatabase db;
    private TextView content;
    private Button btnDelete;
    private String Year;
    private String Month;
    private String Day;
    private String code;
    private String Isread;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_message);
        content = (TextView) findViewById(R.id.tvContentMessage);
        btnDelete = (Button) findViewById(R.id.btnDelete);
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
        try
        {
            hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
            guid = getIntent().getStringExtra("guid").toString();
        }
        catch (Exception e)
        {
            try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
            Cursor coursors = db.rawQuery("SELECT * FROM login",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                guid=coursors.getString(coursors.getColumnIndex("guid"));
                hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
            }
            try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
        }
        String query=null;
        String[] DateSp=null;
        code=getIntent().getStringExtra("Code").toString();
        try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
        query="SELECT * FROM messages WHERE Code='"+code+"'";
        Cursor cursor= db.rawQuery(query,null);
        if(cursor.getCount()>0) {
            cursor.moveToNext();
            Isread=cursor.getString(cursor.getColumnIndex("IsReade"));
        }
        try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
        if(Isread.compareTo("0")==0)
        {
            DateSp= ChangeDate.getCurrentDate().split("/");
            this.Year=DateSp[0];
            this.Month=DateSp[1];
            this.Day=DateSp[2];
            SyncReadMessage readMessage=new SyncReadMessage(ShowMessage.this,guid,hamyarcode,code,  Year,  Month,  Day);
            readMessage.AsyncExecute();
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query=null;
                try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
                query="UPDATE  messages" +
                        " SET  IsDelete='1' " +
                        "WHERE Code='"+getIntent().getStringExtra("Code") + "'";
                db.execSQL(query);
                try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
                LoadActivity(MainActivity.class, "guid", guid, "hamyarcode", hamyarcode);
            }
        });
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            ShowMessage.this.LoadActivity(List_Messages.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        ShowMessage.this.startActivity(intent);
    }
}
