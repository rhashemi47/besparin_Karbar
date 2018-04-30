package com.besparina.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Profile extends Activity {
	private String karbarCode;
	private Button btnSaveProfile;
	private Button btnAddAdres;
	private Button btnEditAdres;
	private String phonenumber;
	private String ReagentCode="";
	private TextView tvProfileRegentCode;
	private TextView tvUserName;
	private TextView tvUserFName;
	private TextView tvUserCode;
	private TextView tvTitleUserCode;
	private TextView tvTitleName;
	private TextView tvTitleFName;
	private TextView TextViewAge;
	private TextView tvTitleNumberPhone;
	private TextView tvTitleAdressAdd;
	private TextView tvCodeMoaref;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private EditText brithday;
	private String yearStr="";
	private String monStr="";
	private String dayStr="";
	private TextView tvPhoneNumber;
	private ImageView imgUser;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
		btnOrder=(Button)findViewById(R.id.btnOrderBottom);
		btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
		btncredite=(Button)findViewById(R.id.btncrediteBottom);
		imgUser=(ImageView)findViewById(R.id.imgUser);
		brithday=(EditText)findViewById(R.id.etBrithday);
		tvProfileRegentCode=(TextView)findViewById(R.id.etReagentCodeProfile);
		tvPhoneNumber=(TextView) findViewById(R.id.tvNumberPhone);
		tvUserName=(TextView) findViewById(R.id.tvUserName);
		tvUserFName=(TextView) findViewById(R.id.tvUserFName);
		tvTitleUserCode=(TextView) findViewById(R.id.tvTitleUserCode);
		tvTitleName=(TextView) findViewById(R.id.tvTitleName);
		tvTitleFName=(TextView) findViewById(R.id.tvTitleFName);
		TextViewAge=(TextView) findViewById(R.id.TextViewAge);
		tvTitleNumberPhone=(TextView) findViewById(R.id.tvTitleNumberPhone);
		tvTitleAdressAdd=(TextView) findViewById(R.id.tvTitleAdressAdd);
		tvCodeMoaref=(TextView) findViewById(R.id.tvCodeMoaref);
		tvUserCode=(TextView) findViewById(R.id.tvUserCode);
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

			phonenumber = getIntent().getStringExtra("guid").toString();
		}
		catch (Exception e)
		{
			db=dbh.getReadableDatabase();
			Cursor coursors = db.rawQuery("SELECT * FROM login",null);
			for(int i=0;i<coursors.getCount();i++){
				coursors.moveToNext();

				karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
				phonenumber=coursors.getString(coursors.getColumnIndex("Phone"));
			}
			db.close();
		}
//		GPSTracker gps = new GPSTracker(Profile.this);
//
//		// check if GPS enabled
//		if (gps.canGetLocation()) {
//
//			//nothing
//		} else {
//			// can't get location
//			// GPS or Network is not enabled
//			// Ask user to enable GPS/network in settings
//			gps.showSettingsAlert();
//		}
		Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
		tvPhoneNumber.setText(phonenumber);
		Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
		tvPhoneNumber.setTextSize(18);
		tvPhoneNumber.setTypeface(FontMitra);
		tvPhoneNumber.setTextSize(18);
		tvUserName.setTypeface(FontMitra);
		tvUserName.setTextSize(18);
		tvUserCode.setTypeface(FontMitra);
		tvUserCode.setTextSize(18);
		tvUserFName.setTypeface(FontMitra);
		tvUserFName.setTextSize(18);
		brithday.setTypeface(FontMitra);
		brithday.setTextSize(18);
		tvPhoneNumber.setTypeface(FontMitra);
		tvPhoneNumber.setTextSize(18);
		tvProfileRegentCode.setTypeface(FontMitra);
		tvProfileRegentCode.setTextSize(18);
		tvTitleUserCode.setTypeface(FontMitra);
		tvTitleName.setTypeface(FontMitra);
		tvTitleFName.setTypeface(FontMitra);
		TextViewAge.setTypeface(FontMitra);
		tvTitleNumberPhone.setTypeface(FontMitra);
		tvTitleAdressAdd.setTypeface(FontMitra);
		tvCodeMoaref.setTypeface(FontMitra);
		//******************************************
		tvTitleUserCode.setTextSize(18);
		tvTitleName.setTextSize(18);
		tvTitleFName.setTextSize(18);
		TextViewAge.setTextSize(18);
		tvTitleNumberPhone.setTextSize(18);
		tvTitleAdressAdd.setTextSize(18);
		tvCodeMoaref.setTextSize(18);
		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Profile",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();
			tvUserCode.setText(coursors.getString(coursors.getColumnIndex("Code")));
			tvUserName.setText(coursors.getString(coursors.getColumnIndex("Name")));
			tvUserFName.setText(coursors.getString(coursors.getColumnIndex("Fam")));
			brithday.setText(coursors.getString(coursors.getColumnIndex("BthDate")));
			bmp=convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
		}

		imgUser.setImageBitmap(bmp);
		btnEditAdres=(Button)findViewById(R.id.btnEditAdres);
		btnEditAdres.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity(List_Address.class,"karbarCode",karbarCode);
			}
		});
		btnAddAdres=(Button)findViewById(R.id.btnAddAdres);
		btnAddAdres.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				LoadActivity2(Map.class,"karbarCode",karbarCode,"nameActivity","Profile");
			}
		});
		btnSaveProfile=(Button)findViewById(R.id.btnSendProfile);
		btnSaveProfile.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				InternetConnection ic=new InternetConnection(getApplicationContext());
				if(ic.isConnectingToInternet())
				{
					if(ReagentCode.length()>0 && ReagentCode.length()<=5)
					{
						Toast.makeText(getApplicationContext(), "کد معرف به درستی وارد نشده!", Toast.LENGTH_LONG).show();
					}
					else
					{
						insertKarbar();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
				}
				db.close();
			}
		});

		brithday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				PersianCalendar now = new PersianCalendar();
				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
								brithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
								yearStr=String.valueOf(year);
								monStr=String.valueOf(monthOfYear+1);
								dayStr=String.valueOf(dayOfMonth);
							}
						}, now.getPersianYear(),
						now.getPersianMonth(),
						now.getPersianDay());
				datePickerDialog.setThemeDark(true);
				datePickerDialog.show(getFragmentManager(), "tpd");

			}

		});
		brithday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
				{
					PersianCalendar now = new PersianCalendar();
					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
							new DatePickerDialog.OnDateSetListener() {
								@Override
								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
									brithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
									yearStr=String.valueOf(year);
									monStr=String.valueOf(monthOfYear+1);
									dayStr=String.valueOf(dayOfMonth);
								}
							}, now.getPersianYear(),
							now.getPersianMonth(),
							now.getPersianDay());
					datePickerDialog.setThemeDark(true);
					datePickerDialog.show(getFragmentManager(), "tpd");
				}
			}
		});
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

	public void insertKarbar() {
		db=dbh.getReadableDatabase();
		String errorStr="";
		if(yearStr.compareTo("")==0 || monStr.compareTo("")==0 || dayStr.compareTo("")==0){
			errorStr="لطفا تاریخ تولد را وارد نمایید\n";
		}
		if(errorStr.compareTo("")==0)
		{
			UpdateProfile updateProfile = new UpdateProfile(Profile.this, karbarCode, yearStr, monStr, dayStr,ReagentCode);
			updateProfile.AsyncExecute();
		}
		else
		{
			Toast.makeText(Profile.this, errorStr, Toast.LENGTH_SHORT).show();
		}
		db.close();
	}
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
	    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
	    	Profile.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
	    }

	    return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

			Profile.this.startActivity(intent);
		}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);
			intent.putExtra(VariableName2, VariableValue2);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

			Profile.this.startActivity(intent);
		}
	public Bitmap convertToBitmap(String base){
		Bitmap Bmp=null;
		try
		{
			byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
			Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
			return Bmp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Bmp;
		}
	}

}

