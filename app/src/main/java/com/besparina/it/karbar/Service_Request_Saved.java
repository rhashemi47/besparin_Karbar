package com.besparina.it.karbar;

import android.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Service_Request_Saved extends AppCompatActivity {
	private String karbarCode;
	private String OrderCode;
	private Button btnCansel;
	private Button btnshowFactor;
	private Button btnEditOrder;
	private Button btnRefreshOrder;
	private Button btnCallHamyar;
	private Button btnCallSupporter;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private GoogleMap map;
	private Typeface FontMitra;
	private LatLng point;
	private TextView ContentShowJob;
	private LinearLayout LinearIfoHamyar;
	private ListView lvHamyar;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.service_request_saved);
	ContentShowJob=(TextView)findViewById(R.id.ContentShowJob);
		btnCansel=(Button)findViewById(R.id.btnCansel);
		btnshowFactor=(Button)findViewById(R.id.btnshowFactor);
		btnEditOrder=(Button)findViewById(R.id.btnEditOrder);
		btnRefreshOrder=(Button)findViewById(R.id.btnRefreshOrder);
		btnCallSupporter=(Button)findViewById(R.id.btnCallSupporter);
		btnCallHamyar=(Button)findViewById(R.id.btnCallHamyar);
		LinearIfoHamyar=(LinearLayout)findViewById(R.id.LinearIfoHamyar);
		lvHamyar=(ListView)findViewById(R.id.lvHamyar);

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

	FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page

	btnCansel.setTypeface(FontMitra);
	btnshowFactor.setTypeface(FontMitra);
	btnEditOrder.setTypeface(FontMitra);
	btnRefreshOrder.setTypeface(FontMitra);
	btnCallSupporter.setTypeface(FontMitra);
	btnCallHamyar.setTypeface(FontMitra);
	btnOrder.setTypeface(FontMitra);
	btnAcceptOrder.setTypeface(FontMitra);
	btncredite.setTypeface(FontMitra);
	prepareData();
		db=dbh.getReadableDatabase();
		Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
				"LEFT JOIN " +
				"Servicesdetails ON " +
				"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'", null);
		if (cursor2.getCount() > 0) {
			btnOrder.setText("درخواست ها: " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount())));
		}
		cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
		if (cursor2.getCount() > 0) {
			btnAcceptOrder.setText("پذیرفته شده ها: " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount())));
		}
		cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
		if (cursor2.getCount() > 0) {
			cursor2.moveToNext();
			try {
				String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
				if(splitStr[1].compareTo("00")==0)
				{
					btncredite.setText("اعتبار: " + PersianDigitConverter.PerisanNumber(splitStr[0]));
				}
				else
				{
					btncredite.setText("اعتبار: " + PersianDigitConverter.PerisanNumber(cursor2.getString(cursor2.getColumnIndex("Amount"))));
				}

			} catch (Exception ex) {
				btncredite.setText(PersianDigitConverter.PerisanNumber("اعتبار: " + "0"));
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
				LoadActivity3(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
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
				LoadActivity3(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
			}
		});
		btncredite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				LoadActivity(Credit.class, "karbarCode", karbarCode);
			}
		});
	//***************************************************************************************
	btnCansel.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LayoutInflater li = LayoutInflater.from(Service_Request_Saved.this);
			View promptsView = li.inflate(R.layout.cansel, null);
			AlertDialog.Builder alertbox = new AlertDialog.Builder(Service_Request_Saved.this);
			//set view
			alertbox.setView(promptsView);
			final EditText descriptionCansel = (EditText) promptsView.findViewById(R.id.etCansel);
			alertbox
					.setCancelable(true)
					.setPositiveButton("بله",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// get user input and set it to result
									// edit text

									SyncCanselServices syncCanselServices=new SyncCanselServices(Service_Request_Saved.this,
											karbarCode,
											OrderCode,
											descriptionCansel.getText().toString());
									syncCanselServices.AsyncExecute();
								}
							})
					.setNegativeButton("خیر",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertbox.create();

			// show it
			alertDialog.show();
		}
	});
	//**************************************************************************
	btnshowFactor.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			db = dbh.getReadableDatabase();
			String query="SELECT * FROM BsFaktorUsersHead WHERE  Status='1' AND BsFaktorUsersHead.UserServiceCode="+OrderCode;
			Cursor cursor = db.rawQuery(query,null);
			if(cursor.getCount()>0) {
				LoadActivity(ShowFactor.class, "OrderCode", OrderCode);
			}
			else {
				Toast.makeText(Service_Request_Saved.this,"در حال حاضر برای این سرویس فاکتوری اعلام نشده", Toast.LENGTH_SHORT).show();
			}
			db.close();
		}
	});
	btnRefreshOrder.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			db = dbh.getReadableDatabase();
			String query="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
					"LEFT JOIN " +
					"Servicesdetails ON " +
					"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE OrdersService.Code="+OrderCode;
			Cursor cursor = db.rawQuery(query,null);
			if(cursor.getCount()>0)
			{
				cursor.moveToNext();
				if(cursor.getString(cursor.getColumnIndex("Status")).compareTo("3")!=0)
				{
					Toast.makeText(Service_Request_Saved.this, "جهت درخواست مجدد ابتدا باید سرویس جاری لغو گردد.", Toast.LENGTH_LONG).show();
				}
				else
				{
					SyncDarkhasteMojadad syncDarkhasteMojadad=new SyncDarkhasteMojadad(Service_Request_Saved.this,karbarCode,OrderCode);
					syncDarkhasteMojadad.AsyncExecute();
				}
			}
			db.close();
		}
	});
	btnEditOrder.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			db = dbh.getReadableDatabase();
			String query="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
					"LEFT JOIN " +
					"Servicesdetails ON " +
					"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE OrdersService.Code="+OrderCode;
			Cursor cursor = db.rawQuery(query,null);
			if(cursor.getCount()>0)
			{
				cursor.moveToNext();
				String Check_Status=cursor.getString(cursor.getColumnIndex("Status"));
				if(Check_Status.compareTo("0")==0)
				{
					LoadActivity2(Service_Request_Edit.class,"karbarCode",karbarCode,
							"DetailCode",cursor.getString(cursor.getColumnIndex("ServiceDetaileCode")),
							"CodeOrderService",cursor.getString(cursor.getColumnIndex("Code")));
				}
				else
				{
					Toast.makeText(Service_Request_Saved.this, "این سرویس توسط همیار انتخاب شده است.", Toast.LENGTH_LONG).show();
				}
			}
			db.close();
		}
	});
	btnCallHamyar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (ActivityCompat.checkSelfPermission(Service_Request_Saved.this,
					android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
				if(ActivityCompat.shouldShowRequestPermissionRationale(Service_Request_Saved.this, android.Manifest.permission.CALL_PHONE))
				{
					//do nothing
				}
				else{

					ActivityCompat.requestPermissions(Service_Request_Saved.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
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
	btnCallSupporter.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (ActivityCompat.checkSelfPermission(Service_Request_Saved.this,
					android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
				if(ActivityCompat.shouldShowRequestPermissionRationale(Service_Request_Saved.this, android.Manifest.permission.CALL_PHONE))
				{
					//do nothing
				}
				else{

					ActivityCompat.requestPermissions(Service_Request_Saved.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
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
    	LoadActivity(List_Order.class, "karbarCode", karbarCode);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		Service_Request_Saved.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue,
							  String VariableName2, String VariableValue2,
							  String VariableName3, String VariableValue3)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.putExtra(VariableName3, VariableValue3);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		this.startActivity(intent);
	}

	public void LoadActivity3(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		this.startActivity(intent);
	}
	public void prepareData()
	{
		db = dbh.getReadableDatabase();
		String query="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
		"LEFT JOIN " +
				"Servicesdetails ON " +
				"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE OrdersService.Code="+OrderCode;
		Cursor cursor = db.rawQuery(query,null);
		for(int i=0;i<cursor.getCount();i++){
			cursor.moveToNext();
			String Content="";
			try
			{
				Content+="شماره درخواست: "+cursor.getString(cursor.getColumnIndex("Code"))+"\n";
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				Content+="نام سرویس: "+cursor.getString(cursor.getColumnIndex("name"))+"\n";
			}
			catch (Exception ex)
			{
				//todo
			}
//			try
//			{
//				Content+="نام صاحبکار: "+cursor.getString(cursor.getColumnIndex("UserName"))+" "+cursor.getString(cursor.getColumnIndex("UserFamily"))+"\n";
//			}
//			catch_money (Exception ex)
//			{
//				//todo
//			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("StartYear")).length()>0) {
					Content += "تاریخ شروع: " + cursor.getString(cursor.getColumnIndex("StartYear")) + "/" +
							cursor.getString(cursor.getColumnIndex("StartMonth")) + "/" + cursor.getString(cursor.getColumnIndex("StartDay")) + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("EndYear")).length()>0) {
					Content += "تاریخ پایان: " + cursor.getString(cursor.getColumnIndex("EndYear")) + "/" +
							cursor.getString(cursor.getColumnIndex("EndMonth")) + "/" + cursor.getString(cursor.getColumnIndex("EndDay")) + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("StartHour")).length()>0) {
					Content += "از ساعت: " + cursor.getString(cursor.getColumnIndex("StartHour")) + ":" + cursor.getString(cursor.getColumnIndex("StartMinute"))+ "  ";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("EndHour")).length()>0) {
					Content += "تا ساعت: " + cursor.getString(cursor.getColumnIndex("EndHour")) + ":" + cursor.getString(cursor.getColumnIndex("EndMinute")) + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("PeriodicServices")).toString().compareTo("1")==0)
				{
					Content+="خدمت دوره ای: "+"روزانه"+"\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("PeriodicServices")).toString().compareTo("2")==0)
				{
					Content+="خدمت دوره ای: "+"هفتگی"+"\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("PeriodicServices")).toString().compareTo("3")==0)
				{
					Content+="خدمت دوره ای: "+"هفته در میان"+"\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("PeriodicServices")).toString().compareTo("4")==0)
				{
					Content+="خدمت دوره ای: "+"ماهانه"+"\n";
				}

			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("MaleCount")).toString().compareTo("0")!=0) {
					Content += "تعداد همیار مرد: " + cursor.getString(cursor.getColumnIndex("MaleCount")) + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("FemaleCount")).toString().compareTo("0")!=0) {
					Content += "تعداد همیار زن: " + cursor.getString(cursor.getColumnIndex("FemaleCount")) + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("HamyarCount")).toString().compareTo("0")!=0) {
					Content += "تعداد همیار: " + cursor.getString(cursor.getColumnIndex("HamyarCount")) + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("EducationTitle")).toString().compareTo("0")!=0) {
					Content += "عنوان آموزش: " + cursor.getString(cursor.getColumnIndex("EducationTitle")) + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("EducationGrade")).toString().compareTo("0")!=0) {
					Content += "پایه تحصیلی: " + cursor.getString(cursor.getColumnIndex("EducationGrade")) + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("1")==0) {
					Content += "رشته تحصیلی: " + "ابتدایی" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("2")==0) {
					Content += "رشته تحصیلی: " + "متوسطه اول" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("3")==0) {
					Content += "رشته تحصیلی: " + "علوم تجربی" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("4")==0) {
					Content += "رشته تحصیلی: " + "ریاضی و فیزیک" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("5")==0) {
					Content += "رشته تحصیلی: " + "انسانی" + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("0")!=0)
				{
					if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("2")==0) {
						Content += "رشته هنری: " + "موسیقی" + "\n";
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("3")==0) {
						Content += "رشته هنری: " + "موسیقی" + "\n";
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("4")==0) {
						Content += "رشته هنری: " + "موسیقی" + "\n";
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("5")==0) {
						Content += "رشته هنری: " + "موسیقی" + "\n";
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("6")==0) {
						Content += "رشته هنری: " + "موسیقی" + "\n";
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("7")==0) {
						Content += "رشته هنری: " + "موسیقی" + "\n";
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("7")==0) {
						Content += "رشته هنری: " + "موسیقی" + "\n";
					}
					else
					{
						Content += "رشته هنری: " + cursor.getString(cursor.getColumnIndex("ArtField")) + "\n";
					}
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("1")==0) {
					Content += "زبان: " + "انگلیسی" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("2")==0) {
					Content += "زبان: " + "روسی" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("3")==0) {
					Content += "زبان: " + "آلمانی" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("4")==0) {
					Content += "زبان: " + "فرانسه" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("5")==0) {
					Content += "زبان: " + "ترکی" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("6")==0) {
					Content += "زبان: " + "عربی" + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("StudentGender")).toString().compareTo("1")==0) {
					Content += "جنسیت دانش آموز: " + "زن" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("StudentGender")).toString().compareTo("2")==0) {
					Content += "جنسیت دانش آموز: " + "مرد" + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("CarWashType")).toString().compareTo("1")==0) {
					Content += "نوع سرویس: " + "روشویی" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("CarWashType")).toString().compareTo("2")==0) {
					Content += "نوع سرویس: " + "روشویی و توشویی" + "\n";
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("CarType")).toString().compareTo("1")==0) {
					Content+="نوع خودرو: "+"سواری"+"\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("CarType")).toString().compareTo("2")==0) {
					Content += "نوع خودرو: " + "شاسی و نیم شاسی" + "\n";
				}
				else if(cursor.getString(cursor.getColumnIndex("CarType")).toString().compareTo("3")==0) {
					Content += "نوع خودرو: " + "ون" + "\n";
				}

			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				Content+="توضیحات: "+cursor.getString(cursor.getColumnIndex("Description"))+"\n";
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				db=dbh.getReadableDatabase();
				query="SELECT OrdersService.*,address.* FROM OrdersService " +
						"LEFT JOIN " +
						"address ON " +
						"address.code=OrdersService.AddressCode WHERE OrdersService.Code="+OrderCode;
				Cursor cursorAddress = db.rawQuery(query,null);
				if(cursorAddress.getCount()>0)
				{
					cursorAddress.moveToNext();
					Content+="آدرس: "+cursorAddress.getString(cursorAddress.getColumnIndex("AddressText"))+"\n";
				}

			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				Content+="فوریت: "+((cursor.getString(cursor.getColumnIndex("IsEmergency")).compareTo("0")==0? "عادی":"فوری"))+" - ";
			}
			catch (Exception ex)
			{
				//todo
			}
			String StrStatus="";
			switch (cursor.getString(cursor.getColumnIndex("Status")))
			{
				case "0":
					StrStatus="آزاد";
					LinearIfoHamyar.setVisibility(View.GONE);
					break;
				case "1":
					StrStatus="در نوبت انجام";
					break;
				case "2":
					StrStatus="در حال انجام";
					btnCansel.setEnabled(false);
					break;
				case "3":
					StrStatus="لغو شده";
					btnCansel.setEnabled(false);
					break;
				case "4":
					StrStatus="اتمام و تسویه شده";
					btnCansel.setEnabled(false);
					break;
				case "5":
					StrStatus="اتمام و تسویه نشده";
					btnCansel.setEnabled(false);
					break;
				case "6":
					StrStatus="اعلام شکایت";
					btnCansel.setEnabled(false);
					break;
				case "7":
					StrStatus="درحال پیگیری شکایت و یا رفع خسارت";
					btnCansel.setEnabled(false);
					break;
				case "8":
					StrStatus="رفع عیب و خسارت شده توسط همیار";
					btnCansel.setEnabled(false);
					break;
				case "9":
					StrStatus="پرداخت خسارت";
					btnCansel.setEnabled(false);
					break;
				case "10":
					StrStatus="پرداخت جریمه";
					btnCansel.setEnabled(false);
					break;
				case "11":
					StrStatus="تسویه حساب با همیار";
					btnCansel.setEnabled(false);
					break;
				case "12":
					StrStatus="متوقف و تسویه شده";
					btnCansel.setEnabled(false);
					break;
				case "13":
					StrStatus="متوقف و تسویه نشده";
					btnCansel.setEnabled(false);
					break;
			}
			Content+="وضعیت: "+StrStatus;
			ContentShowJob.setText(PersianDigitConverter.PerisanNumber(Content));
			ContentShowJob.setTypeface(FontMitra);
			ContentShowJob.setTextSize(16);
			ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
			Cursor infoCursor=db.rawQuery("SELECT * FROM Hamyar WHERE CodeOrder='"+OrderCode+"'",null);
			for(int j=0;j<infoCursor.getCount();j++){
				infoCursor.moveToNext();
				Cursor getHamyar=db.rawQuery("SELECT * FROM InfoHamyar WHERE Code='"+
						infoCursor.getString(infoCursor.getColumnIndex("CodeHamyarInfo"))+"'",null);
				Cursor cursorVisit=db.rawQuery("SELECT * FROM visit WHERE UserServiceCode='"+OrderCode+"'" +
						" AND " +
						"HamyarCode='"+infoCursor.getString(infoCursor.getColumnIndex("CodeHamyarInfo"))+"'",null);
				if(getHamyar.getCount()>0) {
					HashMap<String, String> map = new HashMap<String, String>();
					getHamyar.moveToNext();
					map.put("imgHamyar", getHamyar.getString(getHamyar.getColumnIndex("img")));
					map.put("Content", getHamyar.getString(getHamyar.getColumnIndex("Fname")) + " " +
							getHamyar.getString(getHamyar.getColumnIndex("Lname")));
					map.put("Mobile", getHamyar.getString(getHamyar.getColumnIndex("Mobile")));
					if(cursorVisit.getCount()>0)
					{
						cursorVisit.moveToNext();
						map.put("Visit","تاریخ بازدید: " + cursorVisit.getString(cursorVisit.getColumnIndex("VisitDate"))+"\n"+
						"ساعت بازدید: "+ cursorVisit.getString(cursorVisit.getColumnIndex("VisitTime"))+"\n"+
						"تاریخ اعلام بازدید: "+ cursorVisit.getString(cursorVisit.getColumnIndex("InsertDate")));
					}
					else
					{
						map.put("Visit", "");
					}

					valuse.add(map);
				}
			}
			AdapterInfoHamyar dataAdapter=new AdapterInfoHamyar(Service_Request_Saved.this,valuse);
			lvHamyar.setAdapter(dataAdapter);
		}
		db.close();
		((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2)).getMapAsync(new OnMapReadyCallback() {
			@Override

			public void onMapReady(GoogleMap googleMap) {
				map = googleMap;
				db=dbh.getReadableDatabase();
				String query="SELECT OrdersService.*,address.* FROM OrdersService " +
						"LEFT JOIN " +
						"address ON " +
						"address.code=OrdersService.AddressCode WHERE OrdersService.Code="+OrderCode;
				Cursor coursors = db.rawQuery(query,null);
				if(coursors.getCount()>0)
				{
					coursors.moveToNext();
					String latStr=coursors.getString(coursors.getColumnIndex("Lat"));
					String lonStr=coursors.getString(coursors.getColumnIndex("Lng"));

					try
					{
						double lat=Double.parseDouble(latStr);
						double lon=Double.parseDouble(lonStr);
						point = new LatLng(lat, lon);
					}
					catch (Exception ex)
					{
						point= new LatLng(0, 0);
					}
				}
				else {
					point = new LatLng(0, 0);
				}
				db.close();
				map.addMarker(new MarkerOptions().position(point).title("آدرس").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,17));
				map.getUiSettings().setZoomControlsEnabled(true);
			}
		});
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
