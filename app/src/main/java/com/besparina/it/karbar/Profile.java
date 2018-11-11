package com.besparina.it.karbar;

import android.*;
import android.Manifest;
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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
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

import com.mikepenz.iconics.view.IconicsButton;
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
	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	Bitmap imageBitmap = null;
	private ImageView btnAddPic;
	private Button btnSaveProfile;
	private Button btnAddAdres;
	private Button btnEditAdres;
	private String phonenumber;
	private String ReagentCode="0";
	private TextView tvProfileRegentCode;
	private TextView tvUserName;
	private TextView tvUserFName;
	private TextView tvUserCode;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private EditText brithday;
	private EditText etEmail;
	private String yearStr="";
	private String monStr="";
	private String dayStr="";
	private TextView tvPhoneNumber;
	private ImageView imgUser;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private Button btnServiceEmergency;
	private boolean CheckInputRegentCode=true;

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
		etEmail=(EditText)findViewById(R.id.etEmail);
		tvProfileRegentCode=(TextView)findViewById(R.id.etReagentCodeProfile);
		tvPhoneNumber=(TextView) findViewById(R.id.tvNumberPhone);
		tvUserName=(TextView) findViewById(R.id.tvUserName);
		tvUserFName=(TextView) findViewById(R.id.tvUserFName);
		btnAddPic=(ImageView) findViewById(R.id.btnAddPic);
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

		ImageView imgview = (ImageView)findViewById(R.id.BesparinaLogo);
		imgview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity(MainMenu.class,"","");
			}
		});

		Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
		tvPhoneNumber.setText(PersianDigitConverter.PerisanNumber(phonenumber));
		Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");//set font for page
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
		etEmail.setTypeface(FontMitra);
		etEmail.setTextSize(18);
		tvPhoneNumber.setTypeface(FontMitra);
		tvPhoneNumber.setTextSize(18);
		tvProfileRegentCode.setTypeface(FontMitra);
		tvProfileRegentCode.setTextSize(18);
//		tvCodeMoaref.setTypeface(FontMitra);
		//******************************************
		btnAddPic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				take_photo();
			}
		});
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
			try{
				if (coursors.getString(coursors.getColumnIndex("ReagentName")).length() > 0) {
					tvProfileRegentCode.setText(coursors.getString(coursors.getColumnIndex("ReagentName")));
					tvProfileRegentCode.setEnabled(false);
					CheckInputRegentCode=false;
				}
			}
			catch (Exception e)
			{

			}
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
					if(CheckInputRegentCode)
					{
						ReagentCode=tvProfileRegentCode.getText().toString();
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
//		db=dbh.getReadableDatabase();
//		Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//				"LEFT JOIN " +
//				"Servicesdetails ON " +
//				"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'  ORDER BY CAST(OrdersService.Code AS int) desc", null);
//		if (cursor2.getCount() > 0) {
//			btnOrder.setText("درخواست ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
//		}
//		cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13) ORDER BY CAST(Code AS int) desc", null);
//		if (cursor2.getCount() > 0) {
//			btnAcceptOrder.setText("پذیرفته شده ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
//		}
//		cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
//		if (cursor2.getCount() > 0) {
//			cursor2.moveToNext();
//			try {
//				String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
//				if(splitStr[1].compareTo("00")==0)
//				{
//					btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(splitStr[0])+")");
//				}
//				else
//				{
//					btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(cursor2.getString(cursor2.getColumnIndex("Amount")))+")");
//				}
//
//			} catch (Exception ex) {
//				btncredite.setText(PersianDigitConverter.PerisanNumber("اعتبار( " + "0")+")");
//			}
//		}
//		db.close();
		btnOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String QueryCustom;
				QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
						"LEFT JOIN " +
						"Servicesdetails ON " +
						"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'  ORDER BY CAST(OrdersService.Code AS int) desc";
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
						"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (1,2,6,7,12,13) ORDER BY CAST(OrdersService.Code AS int) desc";
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

			if (yearStr.compareTo("") == 0 || monStr.compareTo("") == 0 || dayStr.compareTo("") == 0) {
				try
				{
					String splitDate[]=brithday.getText().toString().split("/");
					yearStr=splitDate[0];
					monStr=splitDate[1];
					dayStr=splitDate[2];
				}
				catch (Exception ex) {
					errorStr = "لطفا تاریخ تولد را وارد نمایید\n";
				}
			}
		if(errorStr.compareTo("")==0)
		{
			UpdateProfile updateProfile = new UpdateProfile(Profile.this,
					karbarCode,
					yearStr,
					monStr,
					dayStr,
					ReagentCode,
					etEmail.getText().toString());
			updateProfile.AsyncExecute();
			db=dbh.getReadableDatabase();
			Cursor cursor=db.rawQuery("SELECT * FROM TempPic",null);
			if(cursor.getCount()>0){
				cursor.moveToNext();
				SyncSetPicProfile syncSetPicProfile=new SyncSetPicProfile(Profile.this,
						karbarCode,cursor.getString(cursor.getColumnIndex("Pic")));
				syncSetPicProfile.AsyncExecute();
			}
			if(db.isOpen()) {
				db.close();
			}
			if (!(cursor.isClosed()))
				cursor.close();
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
			ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
			return;
		}
		startActivity(callIntent);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (grantResults.length > 0) {
			switch (requestCode) {
				case REQUEST_CODE_ASK_PERMISSIONS:
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						// Permission Granted
						db = dbh.getReadableDatabase();
						Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
						if (cursorPhone.getCount() > 0) {
							cursorPhone.moveToNext();
							dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
						}
						db.close();
					} else {
						// Permission Denied
						Toast.makeText(this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
								.show();
					}
					break;
				case REQUEST_IMAGE_CAPTURE:
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						// Permission Granted
						take_photo();
					} else {
						// Permission Denied
						Toast.makeText(Profile.this, "جهت تغییر عکس پروفایل باید به برنامه اجازه دسترسی به دوربین را بدهید.", Toast.LENGTH_LONG)
								.show();
					}
				default:
					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		}
	}
	public void take_photo()
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);
				return;
			}
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, "data");
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				imageBitmap = (Bitmap) extras.get("data");
				Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
					imgUser.setImageBitmap(imageBitmap);
					String base64Str=ImageConvertor.BitmapToBase64(imageBitmap);
					String Query="INSERT INTO TempPic (Pic) VALUES('"+base64Str+"')";
					db=dbh.getWritableDatabase();
					db.execSQL(Query);
				}
			}
		}
	}
}

