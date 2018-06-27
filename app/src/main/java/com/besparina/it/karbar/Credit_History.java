package com.besparina.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Credit_History extends Activity {
	private String karbarCode;

	private DatabaseHelper dbh;
	private TextView txtContent;
	private SQLiteDatabase db;
	private ListView lstHistoryCredit;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.credits_history);
	btnOrder=(Button)findViewById(R.id.btnOrderBottom);
	btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
	btncredite=(Button)findViewById(R.id.btncrediteBottom);
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
	}

	lstHistoryCredit=(ListView) findViewById(R.id.lstHistoryCredit);
	Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
	txtContent=(TextView)findViewById(R.id.tvHistoryCredits);
	txtContent.setTypeface(FontMitra);

	try
	{
		Cursor coursors = db.rawQuery("SELECT * FROM credits", null);
		String Content="";
		for (int i=0;i<coursors.getCount();i++) {
			coursors.moveToNext();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name","مبلغ: " +coursors.getString(coursors.getColumnIndex("Price"))+ " ریال " +"\n"
			+"عملیات: " + coursors.getString(coursors.getColumnIndex("TransactionType"))+ "\n"
			+"نوع تراکنش: " + coursors.getString(coursors.getColumnIndex("PaymentMethod"))+ "\n"
			+"تاریخ: " + coursors.getString(coursors.getColumnIndex("TransactionDate"))+ "\n"
			+"شماره سند: " + coursors.getString(coursors.getColumnIndex("DocNumber"))+ "\n"
			+"توضیحات: " + coursors.getString(coursors.getColumnIndex("Description")));
			map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
			valuse.add(map);
		}
		AdapterCredit dataAdapter=new AdapterCredit(Credit_History.this,valuse,karbarCode);
		lstHistoryCredit.setAdapter(dataAdapter);
		if(valuse.size()==0){
			lstHistoryCredit.setVisibility(View.GONE);
			txtContent.setVisibility(View.VISIBLE);
			txtContent.setText("موردی جهت نمایش وجود ندارد");
		}
		else
		{
			lstHistoryCredit.setVisibility(View.VISIBLE);
			txtContent.setVisibility(View.GONE);
		}
	}
	catch (Exception ex){
		lstHistoryCredit.setVisibility(View.GONE);
		txtContent.setVisibility(View.VISIBLE);
	}

	db=dbh.getReadableDatabase();
	Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
			"LEFT JOIN " +
			"Servicesdetails ON " +
			"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'", null);
	if (cursor2.getCount() > 0) {
		btnOrder.setText("درخواست ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+"(");
	}
	cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
	if (cursor2.getCount() > 0) {
		btnAcceptOrder.setText("پذیرفته شده ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+"(");
	}
	cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
	if (cursor2.getCount() > 0) {
		cursor2.moveToNext();
		try {
			String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
			if(splitStr[1].compareTo("00")==0)
			{
				btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(splitStr[0])+"(");
			}
			else
			{
				btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(cursor2.getString(cursor2.getColumnIndex("Amount")))+"(");
			}

		} catch (Exception ex) {
			btncredite.setText(PersianDigitConverter.PerisanNumber("اعتبار( " + "0")+"(");
		}
	}
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

			LoadActivity(Credit_History.class, "karbarCode", karbarCode);
		}
	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	Credit_History.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		Credit_History.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);

		this.startActivity(intent);
	}
}
