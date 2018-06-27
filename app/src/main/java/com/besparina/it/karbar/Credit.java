package com.besparina.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Credit extends Activity {
	private String karbarCode;

	private DatabaseHelper dbh;
	private TextView txtContent;
	private TextView tvOne;
	private TextView tvTwo;
	private TextView tvThree;
	private TextView tvRecentCreditsValue;
	private SQLiteDatabase db;
	private Button btnIncreseCredit;
	private Button btnIncreseEtebar;
	private Button btnCreditHistory;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private EditText etCurrencyInsertCredit;
	private EditText etCurrencyInsertEtebar;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.credits);
	btnOrder=(Button)findViewById(R.id.btnOrderBottom);
	btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
	btncredite=(Button)findViewById(R.id.btncrediteBottom);
	btnIncreseEtebar=(Button)findViewById(R.id.btnIncreseEtebar);
	btnCreditHistory=(Button)findViewById(R.id.btnCreditHistory);
	etCurrencyInsertCredit=(EditText) findViewById(R.id.etCurrencyInsertCredit);
	etCurrencyInsertEtebar=(EditText) findViewById(R.id.etCurrencyInsertEtebar);
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

	btnIncreseCredit=(Button)findViewById(R.id.btnIncresCredit);
	Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
	txtContent=(TextView)findViewById(R.id.tvHistoryCredits);
	tvOne=(TextView)findViewById(R.id.tvOne);
	tvTwo=(TextView)findViewById(R.id.tvTwo);
	tvThree=(TextView)findViewById(R.id.tvThree);
	txtContent.setTypeface(FontMitra);
	tvRecentCreditsValue=(TextView)findViewById(R.id.tvRecentCreditsValue);
	tvRecentCreditsValue.setTypeface(FontMitra);
	String Query="UPDATE UpdateApp SET Status='1'";
	etCurrencyInsertCredit.addTextChangedListener(new NumberTextWatcherForThousand(etCurrencyInsertCredit));
	db=dbh.getWritableDatabase();
	db.execSQL(Query);
	try
	{
		String Content="";
		Cursor coursors = db.rawQuery("SELECT * FROM AmountCredit", null);
		if (coursors.getCount() > 0) {
			coursors.moveToNext();
			String splitStr[]=coursors.getString(coursors.getColumnIndex("Amount")).toString().split("\\.");
			if(splitStr[1].compareTo("00")==0)
			{
				Content+=splitStr[0];
			}
			else
			{
				Content+=coursors.getString(coursors.getColumnIndex("Amount"));
			}

		}
		if(Content.compareTo("")==0){
			tvRecentCreditsValue.setText(PersianDigitConverter.PerisanNumber("0"+" ریال"));
		}
		else {
			tvRecentCreditsValue.setText(PersianDigitConverter.PerisanNumber(Content+" ریال"));
		}
	}
	catch (Exception ex){
		tvRecentCreditsValue.setText(PersianDigitConverter.PerisanNumber("0"+" ریال"));
	}
	btnIncreseCredit.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(etCurrencyInsertCredit.getText().length()>0) {
				SyncInsertUserCredit syncInsertUserCredit = new SyncInsertUserCredit(Credit.this, etCurrencyInsertCredit.getText().toString(), karbarCode, "1", "10004", "تست");
				syncInsertUserCredit.AsyncExecute();
			}
			else
			{
				Toast.makeText(Credit.this, "لطفا مبلغ مورد نظر خود را به ریال وارد نمایید", Toast.LENGTH_SHORT).show();
			}
		}
	});
	btnIncreseEtebar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(etCurrencyInsertEtebar.getText().length()>0) {
				if(etCurrencyInsertEtebar.getText().toString().compareTo("0")==1)
				{
					Toast.makeText(Credit.this,"لطفا کد اعتبار را وارد نمایید.",Toast.LENGTH_LONG).show();
				}
				else
				{
					SyncInsertUserCredit syncInsertUserCredit = new SyncInsertUserCredit(Credit.this, etCurrencyInsertCredit.getText().toString(), karbarCode, "1", "10004", "تست");
					syncInsertUserCredit.AsyncExecute();//todo New Web Srvice For Etebar
				}
			}
			else
			{
				Toast.makeText(Credit.this,"لطفا کد اعتبار را وارد نمایید.",Toast.LENGTH_LONG).show();
			}
		}
	});
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
	btnCreditHistory.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity(Credit_History.class,"karbarCode", karbarCode);
		}
	});
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
	tvOne.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###");
			etCurrencyInsertCredit.setText(df.format("10000"));
		}
	});
	tvTwo.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###");
			etCurrencyInsertCredit.setText(df.format("20000"));
		}
	});
	tvThree.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###");
			etCurrencyInsertCredit.setText(df.format("30000"));
		}
	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	Credit.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		Credit.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);

		this.startActivity(intent);
	}
}
