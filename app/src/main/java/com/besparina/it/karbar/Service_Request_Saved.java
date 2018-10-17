package com.besparina.it.karbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	//************************************************************
	private Button btnCansel;
	private Button btnshowFactor;
	private Button btnEditOrder;
	private Button btnRefreshOrder;
	private Button btnCallHamyar;
	private Button btnCallSupporter;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private Button btnServiceEmergency;
	//************************************************************
	private LinearLayout LinearIfoHamyar;
	private LinearLayout LinearTitle;
	private LinearLayout LinearDateStart;
	private LinearLayout LinearDateEnd;
	private LinearLayout LinearTime;
	private LinearLayout LinearPeriodAndEmergency;
	private LinearLayout LinearCountHamyar;
	private LinearLayout LinearAddres;
	private LinearLayout LinearDescription;
	private LinearLayout LinearLearning;
	private LinearLayout LinearGraid;
	private LinearLayout LinearFieldEducation;
	private LinearLayout LinearFieldArt;
	private LinearLayout LinearGenderStudent;
	private LinearLayout LinearGenderTeacher;
	private LinearLayout LinearCarWash;
	private LinearLayout LinearCarType;
	private LinearLayout LinearLanguage;
	private LinearLayout LinearStatus;

	//************************************************************
	private TextView tvCodeRequset;
	private TextView txtTitleOrder;
	private TextView txtDateStart;
	private TextView txtDateEnd;
	private TextView txtTime;
	private TextView txtPeriodAndEmergency;
	private TextView txtCountHamyar;
	private TextView txtAddres;
	private TextView txtDescription;
	private TextView txtLearning;
	private TextView txtGraid;
	private TextView txtFieldEducation;
	private TextView txtFieldArt;
	private TextView txtGenderStudent;
	private TextView txtGenderTeacher;
	private TextView txtCarWash;
	private TextView txtCarType;
	private TextView txtLanguage;
	private TextView txtStatus;
	//************************************************************
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private GoogleMap map;
	private Typeface FontMitra;
	private LatLng point;
	private ListView lvHamyar;
	private String swStyle="1";
	private CharSequence ContentText="";
	private String QueryCustom;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.service_request_saved);
		btnCansel=(Button)findViewById(R.id.btnCansel);
		btnshowFactor=(Button)findViewById(R.id.btnshowFactor);
		btnEditOrder=(Button)findViewById(R.id.btnEditOrder);
		btnRefreshOrder=(Button)findViewById(R.id.btnRefreshOrder);
		btnCallSupporter=(Button)findViewById(R.id.btnCallSupporter);
		btnCallHamyar=(Button)findViewById(R.id.btnCallHamyar);
		lvHamyar=(ListView)findViewById(R.id.lvHamyar);
		btnOrder=(Button)findViewById(R.id.btnOrderBottom);
		btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
		btncredite=(Button)findViewById(R.id.btncrediteBottom);
		btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);

		//*********************************************************************
		LinearIfoHamyar=(LinearLayout)findViewById(R.id.LinearIfoHamyar);
		LinearTitle=(LinearLayout)findViewById(R.id.LinearTitle);
		LinearDateStart=(LinearLayout)findViewById(R.id.LinearDateStart);
		LinearDateEnd=(LinearLayout)findViewById(R.id.LinearDateEnd);
		LinearTime=(LinearLayout)findViewById(R.id.LinearTime);
		LinearPeriodAndEmergency=(LinearLayout)findViewById(R.id.LinearPeriodAndEmergency);
		LinearCountHamyar=(LinearLayout)findViewById(R.id.LinearCountHamyar);
		LinearAddres=(LinearLayout)findViewById(R.id.LinearAddres);
		LinearDescription=(LinearLayout)findViewById(R.id.LinearDescription);
		LinearLearning=(LinearLayout)findViewById(R.id.LinearLearning);
		LinearGraid=(LinearLayout)findViewById(R.id.LinearGraid);
		LinearFieldEducation=(LinearLayout)findViewById(R.id.LinearFieldEducation);
		LinearFieldArt=(LinearLayout)findViewById(R.id.LinearFieldArt);
		LinearGenderStudent=(LinearLayout)findViewById(R.id.LinearGenderStudent);
		LinearGenderTeacher=(LinearLayout)findViewById(R.id.LinearGenderTeacher);
		LinearCarWash=(LinearLayout)findViewById(R.id.LinearCarWash);
		LinearCarType=(LinearLayout)findViewById(R.id.LinearCarType);
		LinearLanguage=(LinearLayout)findViewById(R.id.LinearLanguage);
		LinearStatus=(LinearLayout)findViewById(R.id.LinearStatus);
		//*********************************************************************
		tvCodeRequset=(TextView)findViewById(R.id.tvCodeRequset);
		txtTitleOrder=(TextView)findViewById(R.id.txtTitleOrder);
		txtDateStart=(TextView)findViewById(R.id.txtDateStart);
		txtDateEnd=(TextView)findViewById(R.id.txtDateEnd);
		txtTime=(TextView)findViewById(R.id.txtTime);
		txtPeriodAndEmergency=(TextView)findViewById(R.id.txtPeriodAndEmergency);
		txtCountHamyar=(TextView)findViewById(R.id.txtCountHamyar);
		txtAddres=(TextView)findViewById(R.id.txtAddres);
		txtDescription=(TextView)findViewById(R.id.txtDescription);
		txtLearning=(TextView)findViewById(R.id.txtLearning);
		txtGraid=(TextView)findViewById(R.id.txtGraid);
		txtFieldEducation=(TextView)findViewById(R.id.txtFieldEducation);
		txtFieldArt=(TextView)findViewById(R.id.txtFieldArt);
		txtGenderStudent=(TextView)findViewById(R.id.txtGenderStudent);
		txtGenderTeacher=(TextView)findViewById(R.id.txtGenderTeacher);
		txtCarWash=(TextView)findViewById(R.id.txtCarWash);
		txtCarType=(TextView)findViewById(R.id.txtCarType);
		txtLanguage=(TextView)findViewById(R.id.txtLanguage);
		txtStatus=(TextView)findViewById(R.id.txtStatus);
		//*********************************************************************
		FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");//set font for page
		//*********************************************************************
		tvCodeRequset.setTypeface(FontMitra);
		txtTitleOrder.setTypeface(FontMitra);
		txtDateStart.setTypeface(FontMitra);
		txtDateEnd.setTypeface(FontMitra);
		txtTime.setTypeface(FontMitra);
		txtPeriodAndEmergency.setTypeface(FontMitra);
		txtCountHamyar.setTypeface(FontMitra);
		txtAddres.setTypeface(FontMitra);
		txtDescription.setTypeface(FontMitra);
		txtLearning.setTypeface(FontMitra);
		txtGraid.setTypeface(FontMitra);
		txtFieldEducation.setTypeface(FontMitra);
		txtFieldArt.setTypeface(FontMitra);
		txtGenderStudent.setTypeface(FontMitra);
		txtGenderTeacher.setTypeface(FontMitra);
		txtCarWash.setTypeface(FontMitra);
		txtCarType.setTypeface(FontMitra);
		txtLanguage.setTypeface(FontMitra);
		txtStatus.setTypeface(FontMitra);
		//*********************************************************************
		tvCodeRequset.setTextSize(16);
		txtTitleOrder.setTextSize(16);
		txtDateStart.setTextSize(16);
		txtDateEnd.setTextSize(16);
		txtTime.setTextSize(16);
		txtPeriodAndEmergency.setTextSize(16);
		txtCountHamyar.setTextSize(16);
		txtAddres.setTextSize(16);
		txtDescription.setTextSize(16);
		txtLearning.setTextSize(16);
		txtGraid.setTextSize(16);
		txtFieldEducation.setTextSize(16);
		txtFieldArt.setTextSize(16);
		txtGenderStudent.setTextSize(16);
		txtGenderTeacher.setTextSize(16);
		txtCarWash.setTextSize(16);
		txtCarType.setTextSize(16);
		txtLanguage.setTextSize(16);
		txtStatus.setTextSize(16);
		//*********************************************************************
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
		QueryCustom = getIntent().getStringExtra("QueryCustom").toString();
	}
	catch (Exception ex)
	{
		QueryCustom="";
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

		ImageView imgview = (ImageView)findViewById(R.id.BesparinaLogo);
		imgview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity(MainMenu.class,"","");
			}
		});
	//**************************************************************************************

	btnCansel.setTypeface(FontMitra);
	btnshowFactor.setTypeface(FontMitra);
	btnEditOrder.setTypeface(FontMitra);
	btnRefreshOrder.setTypeface(FontMitra);
	btnCallSupporter.setTypeface(FontMitra);
	btnCallHamyar.setTypeface(FontMitra);
	btnOrder.setTypeface(FontMitra);
	btnAcceptOrder.setTypeface(FontMitra);
	btncredite.setTypeface(FontMitra);
	//**************************************************************************
		prepareData();
	//**************************************************************************
		db=dbh.getReadableDatabase();
		final Cursor coursors = db.rawQuery("SELECT * FROM OrdersService A WHERE A.Status='1' AND A.Code='"+OrderCode+"' AND " +
				"A.Code IN (SELECT BsUserServiceCode FROM StartDateService WHERE UserCode='0')",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			AlertDialog.Builder alertbox = new AlertDialog.Builder(Service_Request_Saved.this);
			//set view
			// set the message to display
                alertbox.setMessage("آیا همیار شروع به کار کرده است؟");

                // set a negative/no button and create a listener
                alertbox.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });

                // set a positive/yes button and create a listener

                alertbox.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Declare Object From Get Internet Connection Status For Check Internet Status
						db=dbh.getReadableDatabase();
						String Query="SELECT Code FROM StartDateService WHERE BsUserServiceCode='"+OrderCode+"'";
						Cursor c = db.rawQuery(Query,null);
						if(c.getCount()>0) {
							c.moveToNext();
							SyncAcceptServiceStartDate acceptServiceStartDate = new SyncAcceptServiceStartDate(Service_Request_Saved.this, karbarCode, c.getString(c.getColumnIndex("Code")));
							acceptServiceStartDate.AsyncExecute();
						}
                        arg0.dismiss();

                    }
                });
                alertbox.show();
		}
		db.close();
	//**************************************************************************

		db=dbh.getReadableDatabase();
		Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
				"LEFT JOIN " +
				"Servicesdetails ON " +
				"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'  ORDER BY CAST(OrdersService.Code AS int) desc", null);
		if (cursor2.getCount() > 0) {
			btnOrder.setText("درخواست ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
		}
		cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13) ORDER BY CAST(Code AS int) desc", null);
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
						"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'  ORDER BY CAST(OrdersService.Code AS int) desc";
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
						"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (1,2,6,7,12,13) ORDER BY CAST(OrdersService.Code AS int) desc";
				LoadActivity3(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
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

				if (ActivityCompat.checkSelfPermission(Service_Request_Saved.this,
						android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					if(ActivityCompat.shouldShowRequestPermissionRationale(Service_Request_Saved.this, android.Manifest.permission.CALL_PHONE))
					{
						ActivityCompat.requestPermissions(Service_Request_Saved.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
					}
					else
					{
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
							"CodeOrderService",cursor.getString(cursor.getColumnIndex("Code")),"OrderCode",OrderCode);
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
			Cursor cursorPhone = db.rawQuery("SELECT ifnull(Mobile,0)Mobile FROM InfoHamyar WHERE Code=(SELECT CodeHamyarInfo FROM Hamyar WHERE CodeOrder="+OrderCode+")", null);
			if (cursorPhone.getCount() > 0) {
				cursorPhone.moveToNext();
				try {
					dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("Mobile")));
				}catch (Exception e){ Toast.makeText(Service_Request_Saved.this,"شماره تماس همیار شما ثبت نشده است",Toast.LENGTH_LONG).show(); }
			}
			else
			{
				Toast.makeText(Service_Request_Saved.this,"سرویس شما توسط هیچ همیاری انتخاب نشده است",Toast.LENGTH_LONG).show();
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
    	LoadActivity3(List_Order.class, "karbarCode", karbarCode,"QueryCustom",QueryCustom);
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
							  String VariableName3, String VariableValue3,
							  String VariableName4, String VariableValue4)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.putExtra(VariableName3, VariableValue3);
		intent.putExtra(VariableName4, VariableValue4);
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
	@RequiresApi(api = Build.VERSION_CODES.M)
	public void prepareData()
	{

		db = dbh.getReadableDatabase();
		String query="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
		"LEFT JOIN " +
				"Servicesdetails ON " +
				"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE OrdersService.Code="+OrderCode;
		Cursor cursor = db.rawQuery(query,null);
		for(int i=0;i<cursor.getCount();i++) {
			cursor.moveToNext();
			try {
				tvCodeRequset.setText(cursor.getString(cursor.getColumnIndex("Code")));
			} catch (Exception ex) {
				//todo
			}
			try {
				LinearTitle.setVisibility(View.VISIBLE);
				LinearTitle.setBackgroundColor(getStyleLinear());
				txtTitleOrder.setText(cursor.getString(cursor.getColumnIndex("name")));
			} catch (Exception ex) {
				//todo
			}
			try {
				if (cursor.getString(cursor.getColumnIndex("StartYear")).length() > 0) {
					LinearDateStart.setVisibility(View.VISIBLE);
					LinearDateStart.setBackgroundColor(getStyleLinear());
					txtDateStart.setText(cursor.getString(cursor.getColumnIndex("StartYear")) + "/" +
							cursor.getString(cursor.getColumnIndex("StartMonth")) + "/" + cursor.getString(cursor.getColumnIndex("StartDay")));
				}
			} catch (Exception ex) {
				//todo
			}
			try {
				if (cursor.getString(cursor.getColumnIndex("EndYear")).length() > 0) {
					LinearDateEnd.setVisibility(View.VISIBLE);
					LinearDateEnd.setBackgroundColor(getStyleLinear());
					txtDateEnd.setText(cursor.getString(cursor.getColumnIndex("EndYear")) + "/" +
							cursor.getString(cursor.getColumnIndex("EndMonth")) + "/" + cursor.getString(cursor.getColumnIndex("EndDay")));
				}
			} catch (Exception ex) {
				//todo
			}
			try {
				String Content = "";
				if (cursor.getString(cursor.getColumnIndex("StartHour")).length() > 0) {
					LinearTime.setVisibility(View.VISIBLE);
					LinearTime.setBackgroundColor(getStyleLinear());
					Content += cursor.getString(cursor.getColumnIndex("StartHour")) + ":" + cursor.getString(cursor.getColumnIndex("StartMinute")) + " - ";
				}
				if (cursor.getString(cursor.getColumnIndex("EndHour")).length() > 0) {
					Content += cursor.getString(cursor.getColumnIndex("EndHour")) + ":" + cursor.getString(cursor.getColumnIndex("EndMinute"));
					txtTime.setText(Content);
				}
			} catch (Exception ex) {
				//todo
			}
			try {

			} catch (Exception ex) {
				//todo
			}
			String PAndE="";
			try {
				LinearPeriodAndEmergency.setVisibility(View.VISIBLE);
				LinearPeriodAndEmergency.setBackgroundColor(getStyleLinear());
				if (cursor.getString(cursor.getColumnIndex("PeriodicServices")).toString().compareTo("1") == 0) {
					PAndE="روزانه";
				} else if (cursor.getString(cursor.getColumnIndex("PeriodicServices")).toString().compareTo("2") == 0) {
					PAndE="هفتگی";
				} else if (cursor.getString(cursor.getColumnIndex("PeriodicServices")).toString().compareTo("3") == 0) {
					PAndE="هفته در میان";
				} else if (cursor.getString(cursor.getColumnIndex("PeriodicServices")).toString().compareTo("4") == 0) {
					PAndE="ماهانه";
				}

			} catch (Exception ex) {
				//todo
			}
			try {
				if(PAndE.length()>0) {
					txtPeriodAndEmergency.setText(PAndE + "/" + ((cursor.getString(cursor.getColumnIndex("IsEmergency")).compareTo("0") == 0 ? "عادی" : "فوری")));
				}
				else
				{
					txtPeriodAndEmergency.setText(((cursor.getString(cursor.getColumnIndex("IsEmergency")).compareTo("0") == 0 ? "عادی" : "فوری")));
				}
			} catch (Exception ex) {
				//todo
			}
			String CountStr = "";
			LinearCountHamyar.setVisibility(View.VISIBLE);
			LinearCountHamyar.setBackgroundColor(getStyleLinear());
			try {
					if (cursor.getString(cursor.getColumnIndex("MaleCount")).toString().compareTo("0") != 0) {
						if (CountStr.length() == 0) {
							CountStr = cursor.getString(cursor.getColumnIndex("MaleCount")) + "آقا ";
						}
						else
						{
							CountStr += " - " + cursor.getString(cursor.getColumnIndex("MaleCount")) + "آقا ";
						}
					}
					if (cursor.getString(cursor.getColumnIndex("FemaleCount")).toString().compareTo("0") != 0)
					{
						if (CountStr.length() == 0) {
							CountStr = cursor.getString(cursor.getColumnIndex("FemaleCount")) + "خانم ";
						}
						else
						{
							CountStr += " - " + cursor.getString(cursor.getColumnIndex("FemaleCount")) + "خانم ";
						}
					}
					if (cursor.getString(cursor.getColumnIndex("HamyarCount")).toString().compareTo("0") != 0) {
						if (CountStr.length() == 0) {
							CountStr = cursor.getString(cursor.getColumnIndex("HamyarCount"));
						}
						else
						{
							CountStr += " - " + cursor.getString(cursor.getColumnIndex("HamyarCount"));
						}
					}
					txtCountHamyar.setText(CountStr);
			}
			catch (Exception e)
			{

			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("EducationTitle")).toString().compareTo("0")!=0) {
					LinearLearning.setVisibility(View.VISIBLE);
					LinearLearning.setBackgroundColor(getStyleLinear());
					txtLearning.setText(cursor.getString(cursor.getColumnIndex("EducationTitle")));
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("EducationGrade")).toString().compareTo("0")!=0) {
					LinearGraid.setVisibility(View.VISIBLE);
					LinearGraid.setBackgroundColor(getStyleLinear());
					txtGraid.setText(cursor.getString(cursor.getColumnIndex("EducationGrade")));
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("1")==0) {
					LinearFieldEducation.setVisibility(View.VISIBLE);
					LinearFieldEducation.setBackgroundColor(getStyleLinear());
					txtFieldEducation.setText("ابتدایی");
				}
				else if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("2")==0) {
					LinearFieldEducation.setVisibility(View.VISIBLE);
					LinearFieldEducation.setBackgroundColor(getStyleLinear());
					txtFieldEducation.setText("متوسطه اول");
				}
				else if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("3")==0) {
					LinearFieldEducation.setVisibility(View.VISIBLE);
					LinearFieldEducation.setBackgroundColor(getStyleLinear());
					txtFieldEducation.setText("علوم تجربی");
				}
				else if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("4")==0) {
					LinearFieldEducation.setVisibility(View.VISIBLE);
					LinearFieldEducation.setBackgroundColor(getStyleLinear());
					txtFieldEducation.setText("ریاضی و فیزیک");
				}
				else if(cursor.getString(cursor.getColumnIndex("FieldOfStudy")).toString().compareTo("5")==0) {
					LinearFieldEducation.setVisibility(View.VISIBLE);
					LinearFieldEducation.setBackgroundColor(getStyleLinear());
					txtFieldEducation.setText("انسانی");
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
					LinearFieldArt.setVisibility(View.VISIBLE);
					LinearFieldArt.setBackgroundColor(getStyleLinear());
					if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("1")==0) {
						txtFieldArt.setText("موسیقی");
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("2")==0) {
						txtFieldArt.setText("طراحی و نقاشی");
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("3")==0) {
						txtFieldArt.setText("خوشنویسی");
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("4")==0) {
						txtFieldArt.setText("عکاسی");
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("5")==0) {
						txtFieldArt.setText("بافندگی");
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("6")==0) {
						txtFieldArt.setText("خیاطی");
					}
					else if(cursor.getString(cursor.getColumnIndex("ArtField")).toString().compareTo("7")==0) {
						txtFieldArt.setText("سایر");
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
					LinearLanguage.setVisibility(View.VISIBLE);
					LinearLanguage.setBackgroundColor(getStyleLinear());
					txtLanguage.setText("انگلیسی");
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("2")==0) {
					LinearLanguage.setVisibility(View.VISIBLE);
					LinearLanguage.setBackgroundColor(getStyleLinear());
					txtLanguage.setText("روسی");
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("3")==0) {
					LinearLanguage.setVisibility(View.VISIBLE);
					LinearLanguage.setBackgroundColor(getStyleLinear());
					txtLanguage.setText("آلمانی");
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("4")==0) {
					LinearLanguage.setVisibility(View.VISIBLE);
					LinearLanguage.setBackgroundColor(getStyleLinear());
					txtLanguage.setText("فرانسه");
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("5")==0) {
					LinearLanguage.setVisibility(View.VISIBLE);
					LinearLanguage.setBackgroundColor(getStyleLinear());
					txtLanguage.setText("ترکی");
				}
				else if(cursor.getString(cursor.getColumnIndex("Language")).toString().compareTo("6")==0) {
					LinearLanguage.setVisibility(View.VISIBLE);
					LinearLanguage.setBackgroundColor(getStyleLinear());
					txtLanguage.setText("عربی");
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("StudentGender")).toString().compareTo("1")==0) {
					LinearGenderStudent.setVisibility(View.VISIBLE);
					LinearGenderStudent.setBackgroundColor(getStyleLinear());
					txtGenderStudent.setText("خانم");
				}
				else if(cursor.getString(cursor.getColumnIndex("StudentGender")).toString().compareTo("2")==0) {
					LinearGenderStudent.setVisibility(View.VISIBLE);
					LinearGenderStudent.setBackgroundColor(getStyleLinear());
					txtGenderStudent.setText("آقا");
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("TeacherGender")).toString().compareTo("1")==0) {
					LinearGenderTeacher.setVisibility(View.VISIBLE);
					LinearGenderTeacher.setBackgroundColor(getStyleLinear());
					txtGenderTeacher.setText("خانم");
				}
				else if(cursor.getString(cursor.getColumnIndex("StudentGender")).toString().compareTo("2")==0) {
					LinearGenderTeacher.setVisibility(View.VISIBLE);
					LinearGenderTeacher.setBackgroundColor(getStyleLinear());
					txtGenderTeacher.setText("آقا");
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("CarWashType")).toString().compareTo("1")==0) {
					LinearCarWash.setVisibility(View.VISIBLE);
					LinearCarWash.setBackgroundColor(getStyleLinear());
					txtCarWash.setText("روشویی");
				}
				else if(cursor.getString(cursor.getColumnIndex("CarWashType")).toString().compareTo("2")==0) {
					LinearCarWash.setVisibility(View.VISIBLE);
					LinearCarWash.setBackgroundColor(getStyleLinear());
					txtCarWash.setText("روشویی و توشویی");
				}
			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				if(cursor.getString(cursor.getColumnIndex("CarType")).toString().compareTo("1")==0) {
					LinearCarType.setVisibility(View.VISIBLE);
					LinearCarType.setBackgroundColor(getStyleLinear());
					txtCarType.setText("سواری");
				}
				else if(cursor.getString(cursor.getColumnIndex("CarType")).toString().compareTo("2")==0) {
					LinearCarType.setVisibility(View.VISIBLE);
					LinearCarType.setBackgroundColor(getStyleLinear());
					txtCarType.setText("شاسی و نیم شاسی");
				}
				else if(cursor.getString(cursor.getColumnIndex("CarType")).toString().compareTo("3")==0) {
					LinearCarType.setVisibility(View.VISIBLE);
					LinearCarType.setBackgroundColor(getStyleLinear());
					txtCarType.setText("ون");
				}

			}
			catch (Exception ex)
			{
				//todo
			}
			try
			{
				LinearDescription.setVisibility(View.VISIBLE);
				LinearDescription.setBackgroundColor(getStyleLinear());
				txtDescription.setText(cursor.getString(cursor.getColumnIndex("Description")));
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
					LinearAddres.setVisibility(View.VISIBLE);
					LinearAddres.setBackgroundColor(getStyleLinear());
					txtAddres.setText(cursorAddress.getString(cursorAddress.getColumnIndex("AddressText")));
				}

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
					LinearStatus.setVisibility(View.VISIBLE);
					LinearStatus.setBackgroundColor(getStyleLinear());
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
			txtStatus.setText(StrStatus);
			//******************************************************************
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
						map.put("Visit","تاریخ بازدید: " + cursorVisit.getString(cursorVisit.getColumnIndex("VisitDate")) + "\n" +
						"ساعت بازدید: "+ cursorVisit.getString(cursorVisit.getColumnIndex("VisitTime")) + "\n" +
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
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,12));
				map.getUiSettings().setZoomControlsEnabled(true);
			}
		});
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
				default:
					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.M)
	public int getStyleLinear()
	{
		if(swStyle.compareTo("0")==0)
		{
			swStyle="1";

			return getColor(R.color.gray);
		}
		else
		{
			swStyle="0";
			return Color.TRANSPARENT;
		}
	}
}