package com.besparina.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
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
    private Button btncredite;	private Button btnServiceEmergency;
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
        btncredite=(Button)findViewById(R.id.btncrediteBottom);            btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);
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
            btnOrder.setText("درخواست ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
        }
        cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
        if (cursor2.getCount() > 0) {
            btnAcceptOrder.setText("پذیرفته شده ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
        }
        cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
        if (cursor2.getCount() > 0) {
            cursor2.moveToNext();
            try {
                String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
                if(splitStr[1].compareTo("00")==0)
                {
                    btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(splitStr[0])+")");
                }
                else
                {
                    btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(cursor2.getString(cursor2.getColumnIndex("Amount")))+")");
                }

            } catch (Exception ex) {
                btncredite.setText(PersianDigitConverter.PerisanNumber("اعتبار( " + "0")+")");
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
        btnServiceEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(ShowMessage.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(ShowMessage.this, android.Manifest.permission.CALL_PHONE))
                    {
                        ActivityCompat.requestPermissions(ShowMessage.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(ShowMessage.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
                    }

                }
                db = dbh.getReadableDatabase();
                Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
                if (cursorPhone.getCount() > 0) {
                    cursorPhone.moveToNext();
                    dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
                }
                db.close();
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        ShowMessage.this.startActivity(intent);
    }
    public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
            , String VariableName2, String VariableValue2) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        this.startActivity(intent);
    }
    public void dialContactPhone(String phoneNumber) {
        //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        startActivity(callIntent);
    }
}
