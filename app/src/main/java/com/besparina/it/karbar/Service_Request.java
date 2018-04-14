package com.besparina.it.karbar;

import android.app.Activity;
import android.app.TimePickerDialog;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Service_Request extends AppCompatActivity {
	private String karbarCode;
	private String DetailCode;
	private TextView tvTitleService;
	private TextView tvTitleString;
	private TextView tvTitleFromDate;
	private TextView tvTitleToDate;
	private TextView tvTitleFromTime;
	private TextView tvTitleToTime;
	private TextView tvTitleTypePeriodService;
	private TextView tvTitleStatus;
	private TextView tvTitleCountWoman;
	private TextView tvTitleCountMan;
	private TextView tvTitleLearning;
	private TextView tvGraid;
	private TextView tvFieldEducation;
	private TextView tvFieldArt;
	private TextView tvTitleFieldArtOther;
	private TextView tvTitleGenderStudent;
	private TextView tvTitleGenderTeacher;
	private TextView tvTitleTypeService;
	private TextView tvTitleTypeCar;
	private TextView tvLanguage;
	private TextView tvTitleDescription;
	private TextView tvTitleAddres;
	//**************************************************************
	private EditText etFromDate;
	private EditText etToDate;
	private EditText etFromTime;
	private EditText etToTime;
	private EditText etCountWoman;
	private EditText etCountMan;
	private EditText etDoesnotmatter;
	private EditText etAddres;
	private EditText etDescription;
	private EditText etTitleLearning;
	private EditText etFieldArtOther;
	//**************************************************************
	private Spinner spGraid;
	private Spinner spFieldEducation;
	private Spinner spFieldArt;
	private Spinner spLanguage;
	//**************************************************************
	private RadioGroup rgTypePeriodService;
	private RadioGroup rgStatus;
	private RadioGroup rgGenderStudent;
	private RadioGroup rgGenderTeacher;
	private RadioGroup rgTypeService;
	private RadioGroup rgTypeCar;
	//**************************************************************
	private CheckBox chbDoesnotmatter;
	//**************************************************************
	private Button btnSave;
	private LinearLayout LinearFromDate;
	private LinearLayout LinearToDate;
	private LinearLayout LinearFromTime;
	private LinearLayout LinearToTime;
	private LinearLayout LinearTypePeriodService;
	private LinearLayout LinearStatus;
	private LinearLayout LinearStatusCountWoman;
	private LinearLayout LinearCountMan;
	private LinearLayout LinearCountDoenotmatter;
	private LinearLayout LinearLearning;
	private LinearLayout LinearGraid;
	private LinearLayout LinearFieldEducation;
	private LinearLayout LinearFieldArt;
	private LinearLayout LinearFieldArtOther;
	private LinearLayout LinearGenderStudent;
	private LinearLayout LinearGenderTeacher;
	private LinearLayout LinearTypeService;
	private LinearLayout LinearTypeCar;
	private LinearLayout LinearLanguage;
	private LinearLayout LinearAddres;
	private LinearLayout LinearDescription;
	//**************************************************************
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private GoogleMap map;
	private String typeForm;
	private String CodeService;
	///*************************************
	private String MaleCount ;
	private String FemaleCount ;
	private String HamyarCount ;
	private String StartYear ;
	private String StartMonth ;
	private String StartDay ;
	private String StartHour ;
	private String StartMinute ;
	private String EndYear ;
	private String EndMonth ;
	private String EndDay ;
	private String EndHour ;
	private String EndMinute ;
	private String AddressCode ;
	private String Description ;
	private String IsEmergency ;
	private String PeriodicServices ;
	private String EducationGrade ;
	private String FieldOfStudy ;
	private String StudentGender ;
	private String TeacherGender ;
	private String EducationTitle ;
	private String ArtField ;
	private String CarWashType ;
	private String CarType ;
	private String Language ;
	private RadioButton rdbDaily;
	private RadioButton rdbWeekly;
	private RadioButton rdbMiddle_of_the_week;
	private RadioButton rdbMonthly;
	private RadioButton rdbNormal;
	private RadioButton rdbEmergency;
	private RadioButton rdbMaleStudent;
	private RadioButton rdbFemaleStudent;
	private RadioButton rdbMaleTeacher;
	private RadioButton rdbFemaleTeacher;
	private RadioButton rdbDoesnotmatter;
	private RadioButton rdbRoshoie;
	private RadioButton rdbRoshoieAndToShoie;
	private RadioButton rdbSavari;
	private RadioButton rdbShasi;
	private RadioButton rdbVan;
	private RadioButton radioStatusButton;
	private RadioButton radioTypePeriodServiceButton;
	private RadioButton radioStudentGenderButton;
	private RadioButton radioTeacherGenderButton;
	private RadioButton radioCarWashTypeButton;
	private RadioButton radiorgTypeCarButton;
	private Spinner spAddress;
	private LatLng point;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private Button btnCansel;
	private Button btnAddAdres;
	private Typeface FontFace;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.service_request);
	//***************************************************************
		FontFace = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");
		int textSize=18;
	//****************************************************************
		btnOrder=(Button)findViewById(R.id.btnOrderBottom);
		btnAddAdres=(Button)findViewById(R.id.btnAddAdres);
		btnOrder.setTypeface(FontFace);
		btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
		btnAcceptOrder.setTypeface(FontFace);
		btncredite=(Button)findViewById(R.id.btncrediteBottom);
		btncredite.setTypeface(FontFace);
		btnSave=(Button)findViewById(R.id.btnSave);
		btnCansel=(Button)findViewById(R.id.btnCansel);
		btnSave.setTypeface(FontFace);
		btnCansel.setTypeface(FontFace);
		btnSave.setTextSize(textSize);
		btnCansel.setTextSize(textSize);
		tvTitleService=(TextView) findViewById(R.id.tvTitleService);
		tvTitleService.setTypeface(FontFace);
		tvTitleService.setTextSize(textSize);
		//**************************************************************************************
		tvTitleService=(TextView)findViewById(R.id.tvTitleService);
		tvTitleString=(TextView)findViewById(R.id.tvTitleString);
		tvTitleFromDate=(TextView)findViewById(R.id.tvTitleFromDate);
		tvTitleToDate=(TextView)findViewById(R.id.tvTitleToDate);
		tvTitleFromTime=(TextView)findViewById(R.id.tvTitleFromTime);
		tvTitleToTime=(TextView)findViewById(R.id.tvTitleToTime);
		tvTitleTypePeriodService=(TextView)findViewById(R.id.tvTitleTypePeriodService);
		tvTitleStatus=(TextView)findViewById(R.id.tvTitleStatus);
		tvTitleCountWoman=(TextView)findViewById(R.id.tvTitleCountWoman);
		tvTitleCountMan=(TextView)findViewById(R.id.tvTitleCountMan);
		tvTitleLearning=(TextView)findViewById(R.id.tvTitleLearning);
		tvGraid=(TextView)findViewById(R.id.tvGraid);
		tvFieldEducation=(TextView)findViewById(R.id.tvFieldEducation);
		tvFieldArt=(TextView)findViewById(R.id.tvFieldArt);
		tvTitleFieldArtOther=(TextView)findViewById(R.id.tvTitleFieldArtOther);
		tvTitleGenderStudent=(TextView)findViewById(R.id.tvTitleGenderStudent);
		tvTitleGenderTeacher=(TextView)findViewById(R.id.tvTitleGenderTeacher);
		tvTitleTypeService=(TextView)findViewById(R.id.tvTitleTypeService);
		tvTitleTypeCar=(TextView)findViewById(R.id.tvTitleTypeCar);
		tvLanguage=(TextView)findViewById(R.id.tvLanguage);
		tvTitleDescription=(TextView)findViewById(R.id.tvTitleDescription);
		tvTitleAddres=(TextView)findViewById(R.id.tvTitleAddres);
		//**************************************************************************************
		tvTitleService.setTypeface(FontFace);
		tvTitleString.setTypeface(FontFace);
		tvTitleFromDate.setTypeface(FontFace);
		tvTitleToDate.setTypeface(FontFace);
		tvTitleFromTime.setTypeface(FontFace);
		tvTitleToTime.setTypeface(FontFace);
		tvTitleTypePeriodService.setTypeface(FontFace);
		tvTitleStatus.setTypeface(FontFace);
		tvTitleCountWoman.setTypeface(FontFace);
		tvTitleCountMan.setTypeface(FontFace);
		tvTitleLearning.setTypeface(FontFace);
		tvGraid.setTypeface(FontFace);
		tvFieldEducation.setTypeface(FontFace);
		tvFieldArt.setTypeface(FontFace);
		tvTitleFieldArtOther.setTypeface(FontFace);
		tvTitleGenderTeacher.setTypeface(FontFace);
		tvTitleTypeService.setTypeface(FontFace);
		tvTitleTypeCar.setTypeface(FontFace);
		tvLanguage.setTypeface(FontFace);
		tvTitleDescription.setTypeface(FontFace);
		tvTitleAddres.setTypeface(FontFace);
		//**************************************************************************************
		tvTitleService.setTextSize(18);
		tvTitleString.setTextSize(18);
		tvTitleFromDate.setTextSize(18);
		tvTitleToDate.setTextSize(18);
		tvTitleFromTime.setTextSize(18);
		tvTitleToTime.setTextSize(18);
		tvTitleTypePeriodService.setTextSize(18);
		tvTitleStatus.setTextSize(18);
		tvTitleCountWoman.setTextSize(18);
		tvTitleCountMan.setTextSize(18);
		tvTitleLearning.setTextSize(18);
		tvGraid.setTextSize(18);
		tvFieldEducation.setTextSize(18);
		tvFieldArt.setTextSize(18);
		tvTitleFieldArtOther.setTextSize(18);
		tvTitleGenderTeacher.setTextSize(18);
		tvTitleTypeService.setTextSize(18);
		tvTitleTypeCar.setTextSize(18);
		tvLanguage.setTextSize(18);
		tvTitleDescription.setTextSize(18);
		tvTitleAddres.setTextSize(18);
		//**************************************************************************************
	  etFromDate=(EditText)findViewById(R.id.etFromDate);
	  etToDate=(EditText)findViewById(R.id.etToDate);
	  etFromTime=(EditText)findViewById(R.id.etFromTime);
	  etToTime=(EditText)findViewById(R.id.etToTime);
	  etCountWoman=(EditText)findViewById(R.id.etCountWoman);
	  etCountMan=(EditText)findViewById(R.id.etCountMan);
	  etDoesnotmatter=(EditText)findViewById(R.id.etDoesnotmatter);
	  etAddres=(EditText)findViewById(R.id.etAddres);
	  etDescription=(EditText)findViewById(R.id.etDescription);
	  etTitleLearning=(EditText)findViewById(R.id.etTitleLearning);
	  etFieldArtOther=(EditText)findViewById(R.id.etFieldArtOther);
	  //*******************************************************************
		etFromDate.setTypeface(FontFace);
		etToDate.setTypeface(FontFace);
		etFromTime.setTypeface(FontFace);
		etToTime.setTypeface(FontFace);
		etCountWoman.setTypeface(FontFace);
		etCountMan.setTypeface(FontFace);
		etDoesnotmatter.setTypeface(FontFace);
		etAddres.setTypeface(FontFace);
		etDescription.setTypeface(FontFace);
		etTitleLearning.setTypeface(FontFace);
		etFieldArtOther.setTypeface(FontFace);
	//*************************************************************************
		etFromDate.setTextSize(textSize);
		etToDate.setTextSize(textSize);
		etFromTime.setTextSize(textSize);
		etToTime.setTextSize(textSize);
		etCountWoman.setTextSize(textSize);
		etCountMan.setTextSize(textSize);
		etDoesnotmatter.setTextSize(textSize);
		etAddres.setTextSize(textSize);
		etDescription.setTextSize(textSize);
		etTitleLearning.setTextSize(textSize);
		etFieldArtOther.setTextSize(textSize);

		//***********************************************************************
	  spGraid=(Spinner)findViewById(R.id.spGraid);
	  spFieldEducation=(Spinner)findViewById(R.id.spFieldEducation);
	  spFieldArt=(Spinner)findViewById(R.id.spFieldArt);
	  spLanguage=(Spinner)findViewById(R.id.spLanguage);
	  //*****************************************************************************
	  rgTypePeriodService=(RadioGroup)findViewById(R.id.rgTypePeriodService);
	  rgStatus=(RadioGroup)findViewById(R.id.rgStatus);
	  rgGenderStudent=(RadioGroup)findViewById(R.id.rgGenderStudent);
	  rgGenderTeacher=(RadioGroup)findViewById(R.id.rgGenderTeacher);
	  rgTypeService=(RadioGroup)findViewById(R.id.rgTypeService);
	  rgTypeCar=(RadioGroup)findViewById(R.id.rgTypeCar);
	  chbDoesnotmatter=(CheckBox)findViewById(R.id.chbDoesnotmatter);
	  LinearFromDate=(LinearLayout)findViewById(R.id.LinearFromDate);
	  LinearToDate=(LinearLayout)findViewById(R.id.LinearToDate);
	  LinearFromTime=(LinearLayout)findViewById(R.id.LinearFromTime);
	  LinearToTime=(LinearLayout)findViewById(R.id.LinearToTime);
	  LinearTypePeriodService=(LinearLayout)findViewById(R.id.LinearTypePeriodService);
	  LinearStatus=(LinearLayout)findViewById(R.id.LinearStatus);
	  LinearStatusCountWoman=(LinearLayout)findViewById(R.id.LinearStatusCountWoman);
	  LinearCountMan=(LinearLayout)findViewById(R.id.LinearCountMan);
	  LinearCountDoenotmatter=(LinearLayout)findViewById(R.id.LinearCountDoenotmatter);
	  LinearLearning=(LinearLayout)findViewById(R.id.LinearLearning);
	  LinearGraid=(LinearLayout)findViewById(R.id.LinearGraid);
	  LinearFieldEducation=(LinearLayout)findViewById(R.id.LinearFieldEducation);
	  LinearFieldArt=(LinearLayout)findViewById(R.id.LinearFieldArt);
	  LinearFieldArtOther=(LinearLayout)findViewById(R.id.LinearFieldArtOther);
	  LinearGenderStudent=(LinearLayout)findViewById(R.id.LinearGenderStudent);
	  LinearGenderTeacher=(LinearLayout)findViewById(R.id.LinearGenderTeacher);
	  LinearTypeService=(LinearLayout)findViewById(R.id.LinearTypeService);
	  LinearTypeCar=(LinearLayout)findViewById(R.id.LinearTypeCar);
	  LinearLanguage=(LinearLayout)findViewById(R.id.LinearLanguage);
	  LinearAddres=(LinearLayout)findViewById(R.id.LinearAddres);
	  LinearDescription=(LinearLayout)findViewById(R.id.LinearDescription);

		//*********************************************************
		chbDoesnotmatter.setTypeface(FontFace);
		chbDoesnotmatter.setTextSize(textSize);
		//***********************************************************************
		rdbDaily=(RadioButton)findViewById(R.id.rdbDaily);
		rdbWeekly=(RadioButton)findViewById(R.id.rdbWeekly);
		rdbMiddle_of_the_week=(RadioButton)findViewById(R.id.rdbMiddle_of_the_week);
		rdbMonthly=(RadioButton)findViewById(R.id.rdbMonthly);
		rdbNormal=(RadioButton)findViewById(R.id.rdbNormal);
		rdbEmergency=(RadioButton)findViewById(R.id.rdbEmergency);
		rdbMaleStudent=(RadioButton)findViewById(R.id.rdbMaleStudent);
		rdbFemaleStudent=(RadioButton)findViewById(R.id.rdbFemaleStudent);
		rdbMaleTeacher=(RadioButton)findViewById(R.id.rdbMaleTeacher);
		rdbFemaleTeacher=(RadioButton)findViewById(R.id.rdbFemaleTeacher);
		rdbDoesnotmatter=(RadioButton)findViewById(R.id.rdbDoesnotmatter);
		rdbRoshoie=(RadioButton)findViewById(R.id.rdbRoshoie);
		rdbRoshoieAndToShoie=(RadioButton)findViewById(R.id.rdbRoshoieAndToShoie);
		rdbSavari=(RadioButton)findViewById(R.id.rdbSavari);
		rdbShasi=(RadioButton)findViewById(R.id.rdbShasi);
		rdbVan=(RadioButton)findViewById(R.id.rdbVan);
		//***********************************************************************
		rdbDaily.setTypeface(FontFace);
		rdbWeekly.setTypeface(FontFace);
		rdbMiddle_of_the_week.setTypeface(FontFace);
		rdbMonthly.setTypeface(FontFace);
		rdbNormal.setTypeface(FontFace);
		rdbEmergency.setTypeface(FontFace);
		rdbMaleStudent.setTypeface(FontFace);
		rdbFemaleStudent.setTypeface(FontFace);
		rdbMaleTeacher.setTypeface(FontFace);
		rdbFemaleTeacher.setTypeface(FontFace);
		rdbDoesnotmatter.setTypeface(FontFace);
		rdbRoshoie.setTypeface(FontFace);
		rdbRoshoieAndToShoie.setTypeface(FontFace);
		rdbSavari.setTypeface(FontFace);
		rdbShasi.setTypeface(FontFace);
		rdbVan.setTypeface(FontFace);
		//***********************************************************************rdbDaily.setTypeface(FontFace);
		rdbWeekly.setTextSize(textSize);
		rdbMiddle_of_the_week.setTextSize(textSize);
		rdbMonthly.setTextSize(textSize);
		rdbNormal.setTextSize(textSize);
		rdbEmergency.setTextSize(textSize);
		rdbMaleStudent.setTextSize(textSize);
		rdbFemaleStudent.setTextSize(textSize);
		rdbMaleTeacher.setTextSize(textSize);
		rdbFemaleTeacher.setTextSize(textSize);
		rdbDoesnotmatter.setTextSize(textSize);
		rdbRoshoie.setTextSize(textSize);
		rdbRoshoieAndToShoie.setTextSize(textSize);
		rdbSavari.setTextSize(textSize);
		rdbShasi.setTextSize(textSize);
		rdbVan.setTextSize(textSize);
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
		DetailCode = getIntent().getStringExtra("DetailCode").toString();
	}
	catch (Exception ex)
	{
		DetailCode="0";
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
	//*********************************************************************
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
		//**************************************************************
	spAddress=(Spinner)findViewById(R.id.spAddress);
	FillSpinner("address","Name",spAddress);
	spAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			db=dbh.getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM address WHERE Name='"+spAddress.getItemAtPosition(position).toString()+"'",null);
			if(cursor.getCount()>0)
			{
				cursor.moveToNext();
				etAddres.setText(cursor.getString(cursor.getColumnIndex("AddressText")));
				etAddres.setTag(cursor.getString(cursor.getColumnIndex("Code")));
				String latStr=cursor.getString(cursor.getColumnIndex("Lat"));
				String lonStr=cursor.getString(cursor.getColumnIndex("Lng"));
				double lat=Double.parseDouble(latStr);
				double lon=Double.parseDouble(lonStr);
				if (latStr.compareTo("0")!=0 && lonStr.compareTo("0")!=0) {
					point = new LatLng(lat, lon);

				}
				else
				{
					point = new LatLng(35.691063, 51.407941);

				}
				map.clear();
				map.addMarker(new MarkerOptions().position(point).title("آدرس").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,17));
			}
			db.close();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	});

	//**************************************************************************************
	((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2)).getMapAsync(new OnMapReadyCallback() {
		@Override

		public void onMapReady(GoogleMap googleMap) {
			map = googleMap;
			db=dbh.getReadableDatabase();
			Cursor coursors = db.rawQuery("SELECT * FROM Profile",null);
			if(coursors.getCount()>0)
			{
				coursors.moveToNext();
				String latStr=coursors.getString(coursors.getColumnIndex("Lat"));
				String lonStr=coursors.getString(coursors.getColumnIndex("Lon"));
				double lat=Double.parseDouble(latStr);
				double lon=Double.parseDouble(lonStr);
				if (latStr.compareTo("0")!=0 && lonStr.compareTo("0")!=0) {
					point = new LatLng(lat, lon);
				}
				else
					{
						point = new LatLng(35.691063, 51.407941);
					}
			}
			else {
				point = new LatLng(35.691063, 51.407941);
			}
			db.close();

			map.addMarker(new MarkerOptions().position(point).title("آدرس").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,17));


			map.getUiSettings().setZoomControlsEnabled(true);
		}
	});

//**************************************************************************************
		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Servicesdetails WHERE code='"+DetailCode+"'",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			typeForm=coursors.getString(coursors.getColumnIndex("type"));
			CodeService=coursors.getString(coursors.getColumnIndex("servicename"));
			tvTitleService.setText(":"+coursors.getString(coursors.getColumnIndex("name")));
		}
		else
		{
			typeForm="0";
			Toast.makeText(getBaseContext(), "نوع فرم ثبت نشده", Toast.LENGTH_LONG).show();
		}
		db.close();
	switch (typeForm)
	{
		case "0":
			form1();
			break;
		case "1":
			form1();
			break;
		case "2":
			form2();
			break;
		case "3":
			form3();
			break;
		case "4":
			form4();
			break;
		case "5":
			form5();
			break;
		case "6":
			form6();
			break;
		case "7":
			form7();
			break;
		case "8":
			form8();
			break;
		case "9":
			form9();
			break;
	}
	chbDoesnotmatter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked)
			{

				LinearStatusCountWoman.setVisibility(View.GONE);
				LinearCountMan.setVisibility(View.GONE);
				etDoesnotmatter.setVisibility(View.VISIBLE);
			}
			else
			{
				LinearStatusCountWoman.setVisibility(View.VISIBLE);
				LinearCountMan.setVisibility(View.VISIBLE);
				etDoesnotmatter.setVisibility(View.GONE);
			}
		}
	});
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String ErrorStr="";
				FemaleCount=etCountWoman.getText().toString();
				HamyarCount=etDoesnotmatter.getText().toString();
				MaleCount=etCountMan.getText().toString();
				try {
					if (etAddres.getTag().toString().length() <= 0) {
						ErrorStr += "آدرس را در قسمت تنظیمات حساب کاربری وارد نمایید." + "\n";
					} else {
						AddressCode = etAddres.getTag().toString();
					}
				}
				catch (Exception ex)
				{
					ErrorStr += "آدرس را در قسمت تنظیمات حساب کاربری وارد نمایید." + "\n";
				}
				if(etFromDate.length()==0)
				{
					ErrorStr+="تاریخ شروع را وارد نمایید"+"\n";
				}
				if(etToDate.length()==0)
				{
					ErrorStr+="تاریخ خاتمه را وارد نمایید"+"\n";
				}
				if(etFromTime.length()==0)
				{
					ErrorStr+="ساعت شروع را وارد نمایید"+"\n";
				}
				if(etToTime.length()==0)
				{
					ErrorStr+="ساعت خاتمه را وارد نمایید"+"\n";
				}
				if(etFromDate.getText().toString().compareTo(etToDate.getText().toString())>0)
				{
					ErrorStr+="تاریخ شروع نمی تواند بزرگتر از تاریخ خاتمه باشد."+"\n";
				}
				if(etFromDate.length()<8 && etFromDate.length()>10)
				{
					ErrorStr+="تاریخ شروع را صحیح وارد نمایید"+"\n";
				}
				if(etToDate.length()<8 && etToDate.length()>10)
				{
					ErrorStr+="تاریخ خاتمه را صحیح وارد نمایید"+"\n";
				}
				if(etFromTime.length()<3 && etFromTime.length()>5)
				{
					ErrorStr+="زمان شروع را صحیح وارد نمایید"+"\n";
				}
				if(etToTime.length()<3 && etToTime.length()>5)
				{
					ErrorStr+="زمان خاتمه را صحیح وارد نمایید"+"\n";
				}
//				if(etFromTime.getText().toString().compareTo(etToTime.getText().toString())>0)
//				{
//					ErrorStr+="ساعت شروع نمی تواند بزرگتر از ساعت خاتمه باشد."+"\n";
//				}

//				if(AddressCode.length()!=10)
//				{
//					ErrorStr+="آدرس را وارد نمایید"+"\n";
//				}
				Description =etDescription.getText().toString();
				//**************************************************************
				int selectedId = rgStatus.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioStatusButton = (RadioButton) findViewById(selectedId);
				IsEmergency =radioStatusButton.getText().toString();
				if(IsEmergency.compareTo("عادی")==0)
				{
					IsEmergency="0";
				}
				else
				{
					IsEmergency="1";
				}
				//***************************************************************
				
				selectedId = rgTypePeriodService.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioTypePeriodServiceButton = (RadioButton) findViewById(selectedId);
				PeriodicServices =radioTypePeriodServiceButton.getText().toString();
				if(PeriodicServices.compareTo("روزانه")==0)
				{
					PeriodicServices ="1";
				}
				else if(PeriodicServices.compareTo("هفته در میان")==0)
				{
					PeriodicServices ="2";
				}
				else if(PeriodicServices.compareTo("هفتگی")==0)
				{
					PeriodicServices ="3";
				}
				else
				{
					PeriodicServices ="4";
				}
				//***************************************************************

				try
				{
					EducationGrade =spGraid.getSelectedItem().toString();
				}
				catch (Exception ex)
				{
					EducationGrade ="0";
				}
				try
				{
					FieldOfStudy =etTitleLearning.getText().toString();
					if(FieldOfStudy.length()==0)
					{
						FieldOfStudy="0";
					}
				}
				catch (Exception ex)
				{
					FieldOfStudy ="0";
				}

				//**************************************************************
				selectedId = rgGenderStudent.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioStudentGenderButton = (RadioButton) findViewById(selectedId);
				StudentGender  =radioStudentGenderButton.getText().toString();
				if(LinearGenderStudent.getVisibility()==View.VISIBLE) {
					if (StudentGender.compareTo("زن") == 0) {
						StudentGender = "1";
					} else {
						StudentGender = "2";
					}
				}
				else
				{
					StudentGender="0";
				}
				//***************************************************************
				selectedId = rgGenderTeacher.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioTeacherGenderButton = (RadioButton) findViewById(selectedId);
				TeacherGender  =radioTeacherGenderButton.getText().toString();
				if(LinearGenderTeacher.getVisibility()==View.VISIBLE) {
					if (TeacherGender.compareTo("زن") == 0) {
						TeacherGender = "1";
					} else if (TeacherGender.compareTo("مرد") == 0) {
						TeacherGender = "2";
					} else {
						TeacherGender = "3";
					}
				}
				else
				{
					TeacherGender="0";
				}
				//***************************************************************

				try
				{
					EducationTitle =spFieldEducation.getSelectedItem().toString();
				}
				catch (Exception ex)
				{
					EducationTitle ="0";
				}
				try
				{
					ArtField =spFieldArt.getSelectedItem().toString();
				}
				catch (Exception ex)
				{
					ArtField ="0";
				}
				//***************************************************************
				//***************************************************************
				selectedId = rgTypeService.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioCarWashTypeButton = (RadioButton) findViewById(selectedId);
				CarWashType  =radioCarWashTypeButton.getText().toString();
				if(LinearTypeService.getVisibility()==View.VISIBLE)
				{
					if(CarWashType.compareTo("روشویی")==0)
					{
						CarWashType ="1";
					}
					else
					{
						CarWashType="2";
					}
				}
				else
				{
					CarWashType="0";
				}

				//***************************************************************
				selectedId = rgTypeCar.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radiorgTypeCarButton = (RadioButton) findViewById(selectedId);
				CarType  =radiorgTypeCarButton.getText().toString();
				if(LinearTypeCar.getVisibility()==View.VISIBLE) {
					if (CarType.compareTo("سواری") == 0) {
						CarType = "1";
					} else if (CarType.compareTo("ون") == 0) {
						CarType = "3";
					} else {
						CarType = "2";
					}
				}
				else
					{
						CarType="0";
					}
				//***************************************************************


				try
				{
					Language =spLanguage.getSelectedItem().toString();
				}
				catch (Exception ex)
				{
					Language ="0";
				}
				if(LinearCountDoenotmatter.getVisibility()==View.VISIBLE ||
						LinearCountMan.getVisibility()== View.VISIBLE ||
						LinearStatusCountWoman.getVisibility()==View.VISIBLE) {
					if (chbDoesnotmatter.isChecked()) {
						MaleCount = "0";
						FemaleCount = "0";
						HamyarCount = etDoesnotmatter.getText().toString();
						if (HamyarCount.length() == 0) {

							ErrorStr += "تعداد همیار را مشخص نمایید" + "\n";
						}
					} else {
						HamyarCount = "0";
						if (MaleCount.length() == 0) {
							MaleCount = "0";
						}
						if (FemaleCount.length() == 0) {
							FemaleCount = "0";
						}
						if (MaleCount.compareTo("0") == 0 && FemaleCount.compareTo("0") == 0) {
							ErrorStr += "تعداد همیار مرد یا زن را مشخص نمایید" + "\n";
						}
					}
				}
				else {
					MaleCount="0";
					FemaleCount="0";
					HamyarCount="0";
				}
				if(ErrorStr.length()==0)
				{
					SyncInsertUserServices syncInsertUserServices = new SyncInsertUserServices(Service_Request.this,
							karbarCode, DetailCode, MaleCount, FemaleCount, HamyarCount, StartYear, StartMonth,
							StartDay, StartHour, StartMinute, EndYear, EndMonth, EndDay, EndHour, EndMinute,
							AddressCode, Description, IsEmergency, PeriodicServices, EducationGrade,
							FieldOfStudy, StudentGender, TeacherGender, EducationTitle, ArtField, CarWashType, CarType, Language);
					syncInsertUserServices.AsyncExecute();
				}
				else
				{
						Toast.makeText(Service_Request.this, ErrorStr, Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnCansel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LoadActivity2(List_ServiceDerails.class, "karbarCode", karbarCode,"codeService",CodeService);
			}
		});
		spFieldArt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(spFieldArt.getItemAtPosition(position).toString().compareTo("سایر")==0)
				{
					LinearFieldArtOther.setVisibility(View.VISIBLE);
					ArtField=etFieldArtOther.getText().toString();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		etFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
				{
					PersianCalendar now = new PersianCalendar();
					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
							new DatePickerDialog.OnDateSetListener() {
								@Override
								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
									etFromDate.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
									StartYear=String.valueOf(year);
									StartMonth=String.valueOf(monthOfYear+1);
									StartDay=String.valueOf(dayOfMonth);

								}
							}, now.getPersianYear(),
							now.getPersianMonth(),
							now.getPersianDay());
					datePickerDialog.setThemeDark(true);
					datePickerDialog.show(getFragmentManager(), "tpd");
				}
			}
		});
		etFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersianCalendar now = new PersianCalendar();
				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
								etFromDate.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
								StartYear=String.valueOf(year);
								StartMonth=String.valueOf(monthOfYear+1);
								StartDay=String.valueOf(dayOfMonth);

							}
						}, now.getPersianYear(),
						now.getPersianMonth(),
						now.getPersianDay());
				datePickerDialog.setThemeDark(true);
				datePickerDialog.show(getFragmentManager(), "tpd");

			}
		});
		etToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					PersianCalendar now = new PersianCalendar();
					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
							new DatePickerDialog.OnDateSetListener() {
								@Override
								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
									etToDate.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));
									EndYear = String.valueOf(year);
									EndMonth = String.valueOf(monthOfYear + 1);
									EndDay = String.valueOf(dayOfMonth);
								}
							}, now.getPersianYear(),
							now.getPersianMonth(),
							now.getPersianDay());
					datePickerDialog.setThemeDark(true);
					datePickerDialog.show(getFragmentManager(), "tpd");
				}
			}
		});
		etToDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersianCalendar now = new PersianCalendar();
				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
								etToDate.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
								EndYear=String.valueOf(year);
								EndMonth=String.valueOf(monthOfYear+1);
								EndDay=String.valueOf(dayOfMonth);
							}
						}, now.getPersianYear(),
						now.getPersianMonth(),
						now.getPersianDay());
				datePickerDialog.setThemeDark(true);
				datePickerDialog.show(getFragmentManager(), "tpd");
			}
		});
		etFromTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					Calendar mcurrentTime = Calendar.getInstance();
					final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
					int minute = mcurrentTime.get(Calendar.MINUTE);

					TimePickerDialog mTimePicker;
					mTimePicker = new TimePickerDialog(Service_Request.this, new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
							String AM_PM;
							if (selectedHour >= 0 && selectedHour < 12) {
								AM_PM = "AM";
							} else {
								AM_PM = "PM";
							}
							etFromTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
							StartHour = String.valueOf(selectedHour);
							StartMinute = String.valueOf(selectedMinute);
						}
					}, hour, minute, false);
					mTimePicker.setTitle("Select Time");
					mTimePicker.show();
				}
			}
		});
		etFromTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mcurrentTime = Calendar.getInstance();
				final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);

				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(Service_Request.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						String AM_PM;
						if (selectedHour >=0 && selectedHour < 12){
							AM_PM = "AM";
						} else {
							AM_PM = "PM";
						}
						etFromTime.setText(String.valueOf(selectedHour)+":"+String.valueOf(selectedMinute));
						StartHour=String.valueOf(selectedHour);
						StartMinute=String.valueOf(selectedMinute);
					}
				}, hour, minute, false);
				mTimePicker.setTitle("Select Time");
				mTimePicker.show();
			}
		});
		etToTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					Calendar mcurrentTime = Calendar.getInstance();
					final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
					int minute = mcurrentTime.get(Calendar.MINUTE);

					TimePickerDialog mTimePicker;
					mTimePicker = new TimePickerDialog(Service_Request.this, new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
							String AM_PM;
							if (selectedHour >= 0 && selectedHour < 12) {
								AM_PM = "AM";
							} else {
								AM_PM = "PM";
							}
							etToTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
							EndHour = String.valueOf(selectedHour);
							EndMinute = String.valueOf(selectedMinute);
						}
					}, hour, minute, false);
					mTimePicker.setTitle("Select Time");
					mTimePicker.show();
				}
			}
		});
		etToTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mcurrentTime = Calendar.getInstance();
				final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);

				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(Service_Request.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						String AM_PM;
						if (selectedHour >=0 && selectedHour < 12){
							AM_PM = "AM";
						} else {
							AM_PM = "PM";
						}
						etToTime.setText(String.valueOf(selectedHour)+":"+String.valueOf(selectedMinute));
						EndHour=String.valueOf(selectedHour);
						EndMinute=String.valueOf(selectedMinute);
					}
				}, hour, minute, false);
				mTimePicker.setTitle("Select Time");
				mTimePicker.show();
			}
		});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	LoadActivity2(List_ServiceDerails.class, "karbarCode", karbarCode,"codeService",CodeService);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		Service_Request.this.startActivity(intent);
	}
public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		startActivity(intent);
	}
	public void form1()
	{
		etDoesnotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearFromDate.setVisibility(View.VISIBLE);
		LinearToDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		LinearTypePeriodService.setVisibility(View.VISIBLE);
		//**********************************************
		LinearStatusCountWoman.setVisibility(View.VISIBLE);
		LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearStatus.setVisibility(View.VISIBLE);
	}
	public void form2()
	{
		etDoesnotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearFromDate.setVisibility(View.VISIBLE);
		LinearToDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		LinearTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountWoman.setVisibility(View.GONE);
		LinearCountMan.setVisibility(View.GONE);
		LinearCountDoenotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearStatus.setVisibility(View.VISIBLE);
	}

	public void form3()
	{
		etDoesnotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearFromDate.setVisibility(View.VISIBLE);
		LinearToDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		LinearTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountWoman.setVisibility(View.VISIBLE);
		LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearStatus.setVisibility(View.VISIBLE);
	}
	public void form4()
	{
		etDoesnotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearFromDate.setVisibility(View.VISIBLE);
		LinearToDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		LinearTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountWoman.setVisibility(View.VISIBLE);
		LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearStatus.setVisibility(View.VISIBLE);
	}
	public void form5()
	{
		etDoesnotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearFromDate.setVisibility(View.VISIBLE);
		LinearToDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		LinearTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountWoman.setVisibility(View.VISIBLE);
		LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.VISIBLE);
		LinearGraid.setVisibility(View.VISIBLE);
		FillSpinner("Grade","Title",spGraid);
		LinearFieldEducation.setVisibility(View.VISIBLE);
		FillSpinner("FieldofEducation","Title",spFieldEducation);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.VISIBLE);
		LinearGenderTeacher.setVisibility(View.VISIBLE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearStatus.setVisibility(View.VISIBLE);
	}
	public void form6()
	{
		etDoesnotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearFromDate.setVisibility(View.VISIBLE);
		LinearToDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		LinearTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountWoman.setVisibility(View.VISIBLE);
		LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.VISIBLE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.VISIBLE);
		LinearStatus.setVisibility(View.VISIBLE);
	}
	public void form7()
	{
		etDoesnotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearFromDate.setVisibility(View.VISIBLE);
		LinearToDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		LinearTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountWoman.setVisibility(View.VISIBLE);
		LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.VISIBLE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.VISIBLE);
		LinearGenderTeacher.setVisibility(View.VISIBLE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearStatus.setVisibility(View.VISIBLE);
	}
	public void form8()
	{
		etDoesnotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearFromDate.setVisibility(View.VISIBLE);
		LinearToDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		LinearTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountWoman.setVisibility(View.VISIBLE);
		LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.VISIBLE);
		FillSpinner("Arts","Title",spFieldArt);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.VISIBLE);
		LinearGenderTeacher.setVisibility(View.VISIBLE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearStatus.setVisibility(View.VISIBLE);
	}
	public void form9()
	{
		etDoesnotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearFromDate.setVisibility(View.VISIBLE);
		LinearToDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		LinearTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountWoman.setVisibility(View.GONE);
		LinearCountMan.setVisibility(View.GONE);
		LinearCountDoenotmatter.setVisibility(View.GONE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.VISIBLE);
		LinearTypeCar.setVisibility(View.VISIBLE);
		LinearLanguage.setVisibility(View.GONE);
		LinearStatus.setVisibility(View.VISIBLE);
	}
	public void FillSpinner(String tableName,String ColumnName,Spinner spinner){
		List<String> labels = new ArrayList<String>();
		db=dbh.getReadableDatabase();
		String query="SELECT * FROM " + tableName ;
		Cursor cursors = db.rawQuery(query,null);
		String str;
		for(int i=0;i<cursors.getCount();i++){
			cursors.moveToNext();
			str=cursors.getString(cursors.getColumnIndex(ColumnName));
			labels.add(str);
		}
		db.close();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels){
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

				Typeface typeface=Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");
				((TextView) v).setTypeface(typeface);

				return v;
			}

			public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
				View v =super.getDropDownView(position, convertView, parent);


				Typeface typeface=Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");
				((TextView) v).setTypeface(typeface);

				return v;
			}
		};
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
}
