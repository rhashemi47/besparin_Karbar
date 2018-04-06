package com.besparina.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.besparina.it.karbar.Date.ChangeDate;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by hashemi on 01/23/2018.
 */

public class ShowMessage extends Activity{
    private String karbarCode;

    private	DatabaseHelper dbh;
    private SQLiteDatabase db;
    private TextView content;
    private Button btnDelete;
    private String Year;
    private String Month;
    private String Day;
    private String code;
    private String Isread;
    private Button btnOrder;
    private Button btnAcceptOrder;
    private Button btncredite;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_message);
        content = (TextView) findViewById(R.id.tvContentMessage);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnOrder=(Button)findViewById(R.id.btnOrderBottom);
        btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
        btncredite=(Button)findViewById(R.id.btncrediteBottom);
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
            karbarCode = getIntent().getStringExtra("karbarCode").toString();

        }
        catch (Exception e)
        {
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM login",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();

                karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
            }
            db.close();
        }
        String query=null;
        String[] DateSp=null;
        try
        {
            code=getIntent().getStringExtra("OrderCode").toString();
        }
        catch (Exception ex)
        {

        }
        try
        {
            code=getIntent().getStringExtra("Code").toString();
        }
        catch (Exception ex)
        {

        }

        db=dbh.getReadableDatabase();
        query="SELECT * FROM messages WHERE Code='"+code+"'";
        Cursor cursor= db.rawQuery(query,null);
        if(cursor.getCount()>0) {
            cursor.moveToNext();
            content.setText(cursor.getString(cursor.getColumnIndex("Content")));
            Isread=cursor.getString(cursor.getColumnIndex("IsReade"));
        }
        db.close();
        if(Isread.compareTo("0")==0)
        {

            db = dbh.getWritableDatabase();
            query = "UPDATE  messages" +
                    " SET  IsReade='1'" +
                    "WHERE Code='" + code + "'";
            db.execSQL(query);
            db.close();
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query=null;
                query="UPDATE  messages" +
                        " SET  IsDelete='1' " +
                        "WHERE Code='"+getIntent().getStringExtra("Code") + "'";
                db.execSQL(query);
                LoadActivity(List_Messages.class, "karbarCode", karbarCode);
            }
        });
        String Query="UPDATE UpdateApp SET Status='1'";
        db=dbh.getWritableDatabase();
        db.execSQL(Query);
        db.close();
        db=dbh.getReadableDatabase();
        Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                "LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'", null);
        if (cursor2.getCount() > 0) {
            btnOrder.setText("درخواست ها: " + cursor2.getCount());
        }
        cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
        if (cursor2.getCount() > 0) {
            btnAcceptOrder.setText("پذیرفته شده ها: " + cursor2.getCount());
        }
        cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
        if (cursor2.getCount() > 0) {
            cursor2.moveToNext();
            try {
                String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
                if(splitStr[1].compareTo("00")==0)
                {
                    btncredite.setText("اعتبار: " +splitStr[0]);
                }
                else
                {
                    btncredite.setText("اعتبار: " + cursor2.getString(cursor2.getColumnIndex("Amount")));
                }

            } catch (Exception ex) {
                btncredite.setText("اعتبار: " + "0");
            }
        }
        db.close();
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String QueryCustom;
                QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                        "LEFT JOIN " +
                        "Servicesdetails ON " +
                        "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'";
                LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
            }
        });
        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String QueryCustom;
                QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                        "LEFT JOIN " +
                        "Servicesdetails ON " +
                        "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (1,2,6,7,12,13)";
                LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
            }
        });
        btncredite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadActivity(Credit.class, "karbarCode", karbarCode);
            }
        });
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            ShowMessage.this.LoadActivity(List_Messages.class, "karbarCode", karbarCode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);

        ShowMessage.this.startActivity(intent);
    }
    public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
            , String VariableName2, String VariableValue2) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);

        this.startActivity(intent);
    }
}
