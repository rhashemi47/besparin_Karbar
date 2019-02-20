package com.besparina.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Info_Person extends Activity {
	private Button btnSendInfo;
	private String phonenumber;
	private String Acceptcode;
	private EditText fname;
	private EditText lname;
	private	DatabaseHelper dbh;
	private SQLiteDatabase db;
	private EditText etReagentCode;
	private String ReagentCode="";
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.info_personal);
		super.onCreate(savedInstanceState);		
		
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
   		try
        {
        	phonenumber = getIntent().getStringExtra("phonenumber").toString();
        	
        }
        catch (Exception e) {
			db = dbh.getReadableDatabase();
			String query = "SELECT * FROM Profile";
			Cursor cursor=db.rawQuery(query,null);
			if(cursor.getCount()>0)
			{
				cursor.moveToNext();
				phonenumber=cursor.getString(cursor.getColumnIndex("Mobile"));
			}
			else {
				phonenumber="0";
			}

			try {	if (db.isOpen()) {	db.close();		if(!cursor.isClosed())
				cursor.close();}}	catch (Exception ex){	}
		}		
   		try
        {
   			Acceptcode = getIntent().getStringExtra("acceptcode").toString();
        }
        catch (Exception e) {
			Acceptcode="0";
		}
		etReagentCode=(EditText)findViewById(R.id.etReagentCodeInsertInfo);
		fname=(EditText)findViewById(R.id.etFname);
		lname=(EditText)findViewById(R.id.etLname);
		btnSendInfo=(Button)findViewById(R.id.btnSendInfo);
		btnSendInfo.setOnClickListener(new OnClickListener() {
		public void onClick(View arg0) {
			InternetConnection ic=new InternetConnection(getApplicationContext());
			if(ic.isConnectingToInternet())
			{
				ReagentCode=etReagentCode.getText().toString().trim();
				ReagentCode=ReagentCode.replace(" ","");
				if(ReagentCode.length()>0 && ReagentCode.length()<=5)
				{
					Toast.makeText(getApplicationContext(), "کد معرف به درستی وارد نشده!", Toast.LENGTH_LONG).show();
				}
				else
				{
					insertKarbar();
				}
			}

		}
	});
}

		public void insertKarbar() {
		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
		String errorStr="";
		if(fname.getText().toString().compareTo("")==0){
			errorStr+="لطفا نام خود راوارد نمایید\n";
		}
		if(lname.getText().toString().compareTo("")==0){
			errorStr+="لطفا نام خانوادگی خود راوارد نمایید\n";
		}
		if(ReagentCode.compareTo("")==0)
		{
			ReagentCode="0";
		}
		if(errorStr.compareTo("")==0)
		{
			InsertKarbar insertKarbar = new InsertKarbar(Info_Person.this, phonenumber, Acceptcode, fname.getText().toString(), lname.getText().toString(),ReagentCode);
			insertKarbar.AsyncExecute();
		}
		else
		{
			Toast.makeText(Info_Person.this, errorStr, Toast.LENGTH_SHORT).show();
		}
	}
}
