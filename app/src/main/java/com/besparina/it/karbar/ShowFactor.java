package com.besparina.it.karbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.function.ToLongBiFunction;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowFactor extends AppCompatActivity {
	private String karbarCode;
	private String OrderCode;
	private Button btnNoFactor;
	private Button btnYesFactor;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private TextView ContentShowFactor;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private int TypeFactor=0;
	private String headFactor="0";
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.show_factor);
	ContentShowFactor=(TextView)findViewById(R.id.ContentShowFactor);
	btnNoFactor=(Button)findViewById(R.id.btnNoFactor);
	btnYesFactor=(Button)findViewById(R.id.btnYesFactor);
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
		OrderCode = getIntent().getStringExtra("OrderCode").toString();
	}
	catch (Exception ex)
	{
		OrderCode="0";
	}
	try
	{
		karbarCode = getIntent().getStringExtra("karbarCode").toString();
	}
	catch (Exception e) {
		db = dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM login", null);
		for (int i = 0; i < coursors.getCount(); i++) {
			coursors.moveToNext();

			karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
		}
		db.close();
	}
	//**************************************************************************************
	prepareData();
	//***************************************************************************************
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
	//***************************************************************************************
		btnNoFactor.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(TypeFactor==1) {
				SyncAcceptPreInvoiceByUsers syncAcceptPreInvoiceByUsers = new SyncAcceptPreInvoiceByUsers(ShowFactor.this, "0", headFactor);
				syncAcceptPreInvoiceByUsers.AsyncExecute();
			}
			else {
				SyncAcceptInvoiceByUsers syncAcceptInvoiceByUsers = new SyncAcceptInvoiceByUsers(ShowFactor.this, "0", headFactor);
				syncAcceptInvoiceByUsers.AsyncExecute();
			}
		}
	});
	//**************************************************************************
		btnYesFactor.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if(TypeFactor==1) {
				SyncAcceptPreInvoiceByUsers syncAcceptPreInvoiceByUsers = new SyncAcceptPreInvoiceByUsers(ShowFactor.this, "1", headFactor);
				syncAcceptPreInvoiceByUsers.AsyncExecute();
			}
			else {
				SyncAcceptInvoiceByUsers syncAcceptInvoiceByUsers = new SyncAcceptInvoiceByUsers(ShowFactor.this, "1", headFactor);
				syncAcceptInvoiceByUsers.AsyncExecute();
			}
		}
	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	LoadActivity(List_Order.class, "karbarCode", karbarCode);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		ShowFactor.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		this.startActivity(intent);
	}
	public void prepareData()
	{
		String ContentStr="";
		Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
		db = dbh.getReadableDatabase();
		String query="SELECT * FROM BsFaktorUsersHead WHERE Status='1' AND UserServiceCode="+OrderCode;
		Cursor cursor = db.rawQuery(query,null);
		if(cursor.getCount()>0) {
			double Total=0;
			cursor.moveToNext();
			headFactor=cursor.getString(cursor.getColumnIndex("Code"));
			ContentStr += "شماره درخواست: " + OrderCode + "\n";
			if (cursor.getString(cursor.getColumnIndex("Type")).compareTo("1") == 0) {
				ContentStr += "وضعیت: "+"پیش فاکتور" + "\n";
				TypeFactor=1;
			} else {
				ContentStr += "وضعیت: "+"فاکتور نهایی" + "\n";
				TypeFactor=0;
			}
			ContentStr += "تاریخ: " + cursor.getString(cursor.getColumnIndex("FaktorDate")) + "\n";
			String query2="SELECT * FROM BsFaktorUserDetailes WHERE FaktorUsersHeadCode="+cursor.getString(cursor.getColumnIndex("Code"));
			Cursor cursor2 = db.rawQuery(query2,null);
			for (int i = 0; i < cursor2.getCount(); i++) {
				cursor2.moveToNext();
				ContentStr += "شرح کالا/خدمات: " + cursor2.getString(cursor2.getColumnIndex("Title")) + "\n";
				ContentStr += "واحد: " + cursor2.getString(cursor2.getColumnIndex("Unit")) + "\n";
				ContentStr += "مقدار: " + cursor2.getString(cursor2.getColumnIndex("Amount")) + "\n";
				ContentStr += "قیمت هر واحد: " + cursor2.getString(cursor2.getColumnIndex("PricePerUnit")) + "\n";
				ContentStr += "جمع: " + cursor2.getString(cursor2.getColumnIndex("TotalPrice")) + "\n";
				ContentStr += "----------------" + "\n";
				Total+=Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("TotalPrice")));
			}
			ContentStr += "توضیحات: " + cursor.getString(cursor.getColumnIndex("Description")) + "\n";
			ContentStr += "جمع کل فاکتور: " + Total + "\n";
			db.close();
			ContentShowFactor.setTypeface(FontMitra);
			ContentShowFactor.setText(ContentStr);
		}
	}
}
