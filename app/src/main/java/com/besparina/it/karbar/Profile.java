package com.besparina.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
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

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
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
//	private TextView tvCodeMoaref;
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
	private Button btncredite;	private Button btnServiceEmergency;
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
		btncredite=(Button)findViewById(R.id.btncrediteBottom);            btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);
		imgUser=(ImageView)findViewById(R.id.imgUser);
		brithday=(EditText)findViewById(R.id.etBrithday);
		tvProfileRegentCode=(TextView)findViewById(R.id.etReagentCodeProfile);
		tvPhoneNumber=(TextView) findViewById(R.id.tvNumberPhone);
		tvUserName=(TextView) findViewById(R.id.tvUserName);
		tvUserFName=(TextView) findViewById(R.id.tvUserFName);
//		tvCodeMoaref=(TextView) findViewById(R.id.tvCodeMoaref);
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
		Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
		tvPhoneNumber.setText(PersianDigitConverter.PerisanNumber(phonenumber));
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
//		tvCodeMoaref.setTypeface(FontMitra);
		//******************************************
//		tvCodeMoaref.setTextSize(18);
		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Profile",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();
			tvUserCode.setText(PersianDigitConverter.PerisanNumber(coursors.getString(coursors.getColumnIndex("Code"))));
			tvUserName.setText(coursors.getString(coursors.getColumnIndex("Name")));
			tvUserFName.setText(coursors.getString(coursors.getColumnIndex("Fam")));
			try {
				if (coursors.getString(coursors.getColumnIndex("BthDate")).length() > 0) {
					brithday.setText(PersianDigitConverter.PerisanNumber(coursors.getString(coursors.getColumnIndex("BthDate"))));
				}
			}
			catch (Exception e)
			{

			}
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

				PersianDatePickerDialog picker = new PersianDatePickerDialog(Profile.this);
				picker.setPositiveButtonString("تایید");
				picker.setNegativeButton("انصراف");
				picker.setTodayButton("امروز");
				picker.setTodayButtonVisible(true);
				//  picker.setInitDate(initDate);
				picker.setMaxYear(PersianDatePickerDialog.THIS_YEAR);
				picker.setMinYear(1300);
				picker.setActionTextColor(Color.GRAY);
				//picker.setTypeFace(FontMitra);
				picker.setListener(new Listener() {

					@Override
					public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {
						//Toast.makeText(getApplicationContext(), persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
						String DateStr=persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay();
						brithday.setText(PersianDigitConverter.PerisanNumber(DateStr));
						String splitDate[]=DateStr.split("/");
						yearStr=splitDate[0];
						monStr=splitDate[1];
						dayStr=splitDate[2];
					}

					@Override
					public void onDismissed() {

					}
				});
				picker.show();

			}

		});
		brithday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
				{
					PersianDatePickerDialog picker = new PersianDatePickerDialog(Profile.this);
					picker.setPositiveButtonString("تایید");
					picker.setNegativeButton("انصراف");
					picker.setTodayButton("امروز");
					picker.setTodayButtonVisible(true);
					//  picker.setInitDate(initDate);
					picker.setMaxYear(PersianDatePickerDialog.THIS_YEAR);
					picker.setMinYear(1300);
					picker.setActionTextColor(Color.GRAY);
					//picker.setTypeFace(FontMitra);
					picker.setListener(new Listener() {

						@Override
						public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {
							//Toast.makeText(getApplicationContext(), persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
							String DateStr=persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay();
							brithday.setText(PersianDigitConverter.PerisanNumber(DateStr));
							String splitDate[]=DateStr.split("/");
							yearStr=splitDate[0];
							monStr=splitDate[1];
							dayStr=splitDate[2];
						}

						@Override
						public void onDismissed() {

						}
					});
					picker.show();
				}
			}
		});
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

				if (ActivityCompat.checkSelfPermission(Profile.this,
						android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					if(ActivityCompat.shouldShowRequestPermissionRationale(Profile.this, android.Manifest.permission.CALL_PHONE))
					{
						ActivityCompat.requestPermissions(Profile.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
					}
					else
					{
						ActivityCompat.requestPermissions(Profile.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
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

