package com.besparina.it.karbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Service_Request extends AppCompatActivity {
	private String karbarCode;
	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	private String DetailCode;
	private TextView tvTitleService;
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
	private Spinner spAddress;
	private Spinner spStatus;
	private Spinner spTypeCar;
	private Spinner spTypeService;
	private Spinner spGenderStudent;
	private Spinner spGenderTeacher;
	private Spinner spTypePeriodService;
	//**************************************************************
	private Button btnSave;
	private LinearLayout LinearDate;
	private LinearLayout LinearFromTime;
	private LinearLayout LinearToTime;
	private LinearLayout LinearStatusCountHamyar;
	private LinearLayout LinearLearning;
	private LinearLayout LinearGraid;
	private LinearLayout LinearFieldEducation;
	private LinearLayout LinearFieldArt;
	private LinearLayout LinearFieldArtOther;
	private LinearLayout LinearGenderStudentAndTeacher;
	private LinearLayout LinearCarWash;
	private LinearLayout LinearLanguage;
	private LinearLayout LinearAddres;
	private LinearLayout LinearDescription;
	private LinearLayout LinerLayoutLanguege;
	//**************************************************************
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private String typeForm;
	private String CodeService;
	///*************************************
	private String MaleCount ;
	private String FemaleCount ;
	private String HamyarCount ;
	private String StartYear ;
	private String StartMonth ;
	private String StartDay ;
	public String StartHour ;
	public String StartMinute ;
	private String EndYear ;
	private String EndMonth ;
	private String EndDay ;
	public String EndHour ;
	public String EndMinute ;
	private String AddressCode ;
	private String Description ;
	private String EducationGrade ;
	private String FieldOfStudy ;
	private String EducationTitle ;
	private String ArtField ;
	private String Language ;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private Button btnServiceEmergency;
	private Button btnCansel;
	private Button btnAddAdres;
	private Typeface FontFace;
	private String StudentGender;
	private String IsEmergency;
	private String PeriodicServices;
	private String TeacherGender;
	private String CarWashType;
	private String CarType;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_request);
		//***************************************************************

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
		btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);
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
		spTypeCar=(Spinner)findViewById(R.id.spTypeCar);
		spTypeService=(Spinner)findViewById(R.id.spTypeService);
		spGenderStudent=(Spinner)findViewById(R.id.spGenderStudent);
		spGenderTeacher=(Spinner)findViewById(R.id.spGenderTeacher);
		spTypePeriodService=(Spinner)findViewById(R.id.spTypePeriodService);
		spStatus=(Spinner)findViewById(R.id.spStatus);
		//***********************************************************************
		LinearDate=(LinearLayout)findViewById(R.id.LinearDate);
		LinearFromTime=(LinearLayout)findViewById(R.id.LinearFromTime);
		LinearToTime=(LinearLayout)findViewById(R.id.LinearToTime);
		LinearStatusCountHamyar=(LinearLayout)findViewById(R.id.LinearStatusCountHamyar);
		LinearLearning=(LinearLayout)findViewById(R.id.LinearLearning);
		LinearGraid=(LinearLayout)findViewById(R.id.LinearGraid);
		LinearFieldEducation=(LinearLayout)findViewById(R.id.LinearFieldEducation);
		LinearFieldArt=(LinearLayout)findViewById(R.id.LinearFieldArt);
		LinearFieldArtOther=(LinearLayout)findViewById(R.id.LinearFieldArtOther);
		LinearGenderStudentAndTeacher=(LinearLayout)findViewById(R.id.LinearGenderStudentAndTeacher);
		LinearCarWash=(LinearLayout)findViewById(R.id.LinearCarWash);
		LinearLanguage=(LinearLayout)findViewById(R.id.LinearLanguage);
		LinearAddres=(LinearLayout)findViewById(R.id.LinearAddres);
		LinearDescription=(LinearLayout)findViewById(R.id.LinearDescription);
		LinerLayoutLanguege=(LinearLayout)findViewById(R.id.LinerLayoutLanguege);

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
//**************************************************************************
        try {
            MaleCount = getIntent().getStringExtra("MaleCount").toString();
        } catch (Exception e) {
            MaleCount = "";
        }try {
            FemaleCount = getIntent().getStringExtra("FemaleCount").toString();
        } catch (Exception e) {
            FemaleCount = "";
        }try {
            HamyarCount = getIntent().getStringExtra("HamyarCount").toString();
        } catch (Exception e) {
            HamyarCount = "";
        }try {
            StartYear = getIntent().getStringExtra("StartYear").toString();
        } catch (Exception e) {
            StartYear = "";
        }try {
            StartHour = getIntent().getStringExtra("StartHour").toString();
        } catch (Exception e) {
            StartHour = "";
        }try {
            EndYear = getIntent().getStringExtra("EndYear").toString();
        } catch (Exception e) {
            EndYear = "";
        }try {
            EndHour = getIntent().getStringExtra("EndHour").toString();
        } catch (Exception e) {
            EndHour = "";
        }try {
            AddressCode = getIntent().getStringExtra("AddressCode").toString();
        } catch (Exception e) {
            AddressCode = "";
        }try {
            Description = getIntent().getStringExtra("Description").toString();
        } catch (Exception e) {
            Description = "";
        }try {
            IsEmergency = getIntent().getStringExtra("IsEmergency").toString();
        } catch (Exception e) {
            IsEmergency = "";
        }try {
            PeriodicServices = getIntent().getStringExtra("PeriodicServices").toString();
        } catch (Exception e) {
            PeriodicServices = "";
        }try {
            EducationGrade = getIntent().getStringExtra("EducationGrade").toString();
        } catch (Exception e) {
            EducationGrade = "";
        }try {
            FieldOfStudy = getIntent().getStringExtra("FieldOfStudy").toString();
        } catch (Exception e) {
            FieldOfStudy = "";
        }try {
            StudentGender = getIntent().getStringExtra("StudentGender").toString();
        } catch (Exception e) {
            StudentGender = "";
        }try {
            TeacherGender = getIntent().getStringExtra("TeacherGender").toString();
        } catch (Exception e) {
            TeacherGender = "";
        }try {
            EducationTitle = getIntent().getStringExtra("EducationTitle").toString();
        } catch (Exception e) {
            EducationTitle = "";
        }try {
            ArtField = getIntent().getStringExtra("ArtField").toString();
        } catch (Exception e) {
            ArtField = "";
        }try {
            CarWashType = getIntent().getStringExtra("CarWashType").toString();
        } catch (Exception e) {
            CarWashType = "";
        }try {
            CarType = getIntent().getStringExtra("CarType").toString();
        } catch (Exception e) {
            CarType = "";
        }try {
            Language = getIntent().getStringExtra("Language").toString();
        } catch (Exception e) {
            Language = "";
        }
		ImageView imgview = (ImageView)findViewById(R.id.BesparinaLogo);
		imgview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity(MainMenu.class,"","");
			}
		});

		//*********************************************************************
		db=dbh.getReadableDatabase();
		Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
				"LEFT JOIN " +
				"Servicesdetails ON " +
				"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0' order by OrdersService.Code desc", null);
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
		btnAddAdres.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//LoadActivity2(Map.class,"karbarCode", karbarCode,"DetailCode",DetailCode);
                //**************************************************************
                if (spStatus.getSelectedItem().toString().compareTo("عادی") == 0) {
                    IsEmergency = "0";
                } else {
                    IsEmergency = "1";
                }
                //***************************************************************
                //**************************************************************
                if(LinearGenderStudentAndTeacher.getVisibility()== View.VISIBLE) {
                    if (spGenderStudent.getSelectedItem().toString().compareTo("خانم") == 0) {
                        StudentGender = "1";
                    } else if (spGenderStudent.getSelectedItem().toString().compareTo("آقا") == 0) {
                        StudentGender = "0";
                    } else {
                        StudentGender = "2";
                    }
                }
                else
                {
                    StudentGender = "0";
                }
                //***************************************************************
                if(LinearGenderStudentAndTeacher.getVisibility()== View.VISIBLE) {
                    if (spGenderTeacher.getSelectedItem().toString().compareTo("خانم") == 0) {
                        TeacherGender = "1";
                    } else if (spGenderTeacher.getSelectedItem().toString().compareTo("آقا") == 0) {
                        TeacherGender = "2";
                    } else if (spGenderTeacher.getSelectedItem().toString().compareTo("فرقی ندارد") == 0) {
                        TeacherGender = "3";
                    } else {
                        TeacherGender = "0";
                    }
                }
                else
                {
                    TeacherGender = "0";
                }
                //***************************************************************

                try {
                    String	FieldOfStudyName = spFieldEducation.getSelectedItem().toString();
                    FieldOfStudy = GetFieldofEducationCode(FieldOfStudyName);
                } catch (Exception ex) {
                    FieldOfStudy = "0";
                }
                try {
                    String ArtName = spFieldArt.getSelectedItem().toString();
                    ArtField = GetArtCode(ArtName);
                } catch (Exception ex) {
                    ArtField = "0";
                }
                //***************************************************************
                //***************************************************************
                if(LinearCarWash.getVisibility()==View.VISIBLE) {
                    if (spTypeService.getSelectedItem().toString().compareTo("روشویی") == 0) {
                        CarWashType = "1";
                    } else if (spTypeService.getSelectedItem().toString().compareTo(" ") == 0) {
                        CarWashType = "0";
                    } else {
                        CarWashType = "2";
                    }
                }
                else
                {
                    CarWashType = "0";
                }
                //***************************************************************
                if(LinearCarWash.getVisibility()==View.VISIBLE) {
                    if (spTypeCar.getSelectedItem().toString().compareTo("سواری") == 0) {
                        CarType = "1";
                    } else if (CarType.compareTo("ون") == 0) {
                        CarType = "3";
                    } else if (CarType.compareTo(" ") == 0) {
                        CarType = "0";
                    } else {
                        CarType = "2";
                    }
                }
                else
                {
                    CarType = "0";
                }
                //***************************************************************


                try {
                    if (LinerLayoutLanguege.getVisibility() == View.VISIBLE) {
                        Language = String.valueOf(spLanguage.getSelectedItemId());
                    } else
                    {
                        Language = "0";
                    }

                } catch (Exception ex) {
                    Language = "0";
                }
                if(spTypePeriodService.getVisibility()== View.VISIBLE) {
                    if (spTypePeriodService.getSelectedItem().toString().compareTo("روزانه") == 0) {
                        PeriodicServices = "1";
                    } else if (spTypePeriodService.getSelectedItem().toString().compareTo("هفته در میان") == 0) {
                        PeriodicServices = "2";
                    } else if (spTypePeriodService.getSelectedItem().toString().compareTo("هفتگی") == 0) {
                        PeriodicServices = "3";
                    } else {
                        PeriodicServices = "4";
                    }
                }
                else
                {
                    PeriodicServices = "1";
                }
                //***************************************************************
                LoadActivity_Map(Map.class, "karbarCode", karbarCode,
                        "DetailCode",DetailCode,
                        "MaleCount", etCountMan.getText().toString(),
                        "FemaleCount", etCountWoman.getText().toString(),
                        "HamyarCount", etDoesnotmatter.getText().toString(),
                        "StartYear", etFromDate.getText().toString(),
                        "StartHour", etFromTime.getText().toString(),
                        "EndYear", etToDate.getText().toString(),
                        "EndHour", etToTime.getText().toString(),
                        "AddressCode",AddressCode,
                        "Description", Description,
                        "IsEmergency", IsEmergency,
                        "PeriodicServices", PeriodicServices,
                        "EducationGrade", EducationGrade,
                        "FieldOfStudy", FieldOfStudy,
                        "StudentGender", StudentGender,
                        "TeacherGender", TeacherGender,
                        "EducationTitle", EducationTitle,
                        "ArtField", ArtField,
                        "CarWashType", CarWashType,
                        "CarType", CarType,
                        "Language", Language);

			}
		});
		btnOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String QueryCustom;
				QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
						"LEFT JOIN " +
						"Servicesdetails ON " +
						"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0' order by OrdersService.Code desc";
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

				if (ActivityCompat.checkSelfPermission(Service_Request.this,
						android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					if(ActivityCompat.shouldShowRequestPermissionRationale(Service_Request.this, android.Manifest.permission.CALL_PHONE))
					{
						ActivityCompat.requestPermissions(Service_Request.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
					}
					else
					{
						ActivityCompat.requestPermissions(Service_Request.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
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
		//**************************************************************
		spAddress=(Spinner)findViewById(R.id.spAddress);
		FillSpinner("address","Name",spAddress);
		FillSpinner("ServiceStatus","Title",spStatus);
		spAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				db=dbh.getReadableDatabase();
				Cursor cursor = db.rawQuery("SELECT * FROM address WHERE Name='"+spAddress.getItemAtPosition(position).toString()+"'",null);
				if(cursor.getCount()>0)
				{
					cursor.moveToNext();
					etAddres.setText(PersianDigitConverter.PerisanNumber(cursor.getString(cursor.getColumnIndex("AddressText"))));
					etAddres.setTag(cursor.getString(cursor.getColumnIndex("Code")));
					AddressCode=String.valueOf(position);

				}
				db.close();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Servicesdetails WHERE code='"+DetailCode+"'",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			typeForm=coursors.getString(coursors.getColumnIndex("type"));
			CodeService=coursors.getString(coursors.getColumnIndex("servicename"));
			tvTitleService.setText(":"+PersianDigitConverter.PerisanNumber(coursors.getString(coursors.getColumnIndex("name"))));
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

        //**************************************************************************
        if(StartYear.length()>0) {
            FillForm();
        }
        //**************************************************************************

		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String ErrorStr = "";
				FemaleCount = PersianDigitConverter.EnglishNumber(etCountWoman.getText().toString());
				HamyarCount = PersianDigitConverter.EnglishNumber(etDoesnotmatter.getText().toString());
				MaleCount = PersianDigitConverter.EnglishNumber(etCountMan.getText().toString());
				try {
					if (etAddres.getTag().toString().length() <= 0) {
						ErrorStr += "آدرس را در قسمت تنظیمات حساب کاربری وارد نمایید." + "\n";
					} else {
						AddressCode = etAddres.getTag().toString();
					}
				} catch (Exception ex) {
					ErrorStr += "آدرس را در قسمت تنظیمات حساب کاربری وارد نمایید." + "\n";
				}
				if (etFromDate.length() == 0) {
					ErrorStr += "تاریخ شروع را وارد نمایید" + "\n";
				}
				if (etToDate.length() == 0) {
					ErrorStr += "تاریخ خاتمه را وارد نمایید" + "\n";
				}
				if (etFromTime.length() == 0) {
					ErrorStr += "ساعت شروع را وارد نمایید" + "\n";
				}
				if (etToTime.length() == 0) {
					ErrorStr += "ساعت خاتمه را وارد نمایید" + "\n";
				}
				String SplitFromeDate[]=etFromDate.getText().toString().split("/");
				ir.hamsaa.persiandatepicker.util.PersianCalendar calFrom=new ir.hamsaa.persiandatepicker.util.PersianCalendar();
				calFrom.setPersianDate(Integer.parseInt(SplitFromeDate[0])
						,Integer.parseInt(SplitFromeDate[1])
								,Integer.parseInt(SplitFromeDate[2]));
				//******************
				String SplitToDate[]=etToDate.getText().toString().split("/");
				ir.hamsaa.persiandatepicker.util.PersianCalendar calTo=new ir.hamsaa.persiandatepicker.util.PersianCalendar();
				calTo.setPersianDate(Integer.parseInt(SplitToDate[0])
						,Integer.parseInt(SplitToDate[1])
								,Integer.parseInt(SplitToDate[2]));


				ir.hamsaa.persiandatepicker.util.PersianCalendar calNow=new ir.hamsaa.persiandatepicker.util.PersianCalendar();
				calNow.setPersianDate(calNow.getPersianYear(),calNow.getPersianMonth(),calNow.getPersianDay());
				String strNow = null;
				String strFrom= null;
				String strTo= null;
				
				int compateDate= calFrom.compareTo(calTo);
				if (compateDate>0) {
					ErrorStr += "تاریخ شروع نمی تواند بزرگتر از تاریخ خاتمه باشد." + "\n";
				}
				else
				{

					 strTo=String.valueOf(calTo.getPersianYear());
					if(calTo.getPersianMonth()<10)
					{
						strTo=strTo+"0"+String.valueOf(calTo.getPersianMonth());
					}
					else
					{
						strTo=strTo+String.valueOf(calTo.getPersianMonth());
					}
					if(calTo.getPersianDay()<10)
					{
						strTo=strTo+"0"+String.valueOf(calTo.getPersianDay());
					}
					else
					{
						strTo=strTo+String.valueOf(calTo.getPersianDay());
					}
					//**********************************
					 strNow=String.valueOf(calNow.getPersianYear());
					if(calNow.getPersianMonth()<10)
					{
						strNow=strNow+"0"+String.valueOf(calNow.getPersianMonth());
					}
					else
					{
						strNow=strNow+String.valueOf(calNow.getPersianMonth());
					}
					if(calNow.getPersianDay()<10)
					{
						strNow=strNow+"0"+String.valueOf(calNow.getPersianDay());
					}
					else
					{
						strNow=strNow+String.valueOf(calNow.getPersianDay());
					}

					//**********************************
					 strFrom=String.valueOf(calFrom.getPersianYear());
					if(calFrom.getPersianMonth()<10)
					{
						strFrom=strFrom+"0"+String.valueOf(calFrom.getPersianMonth());
					}
					else
					{
						strFrom=strFrom+String.valueOf(calFrom.getPersianMonth());
					}
					if(calFrom.getPersianDay()<10)
					{
						strFrom=strFrom+"0"+String.valueOf(calFrom.getPersianDay());
					}
					else
					{
						strFrom=strFrom+String.valueOf(calFrom.getPersianDay());
					}

					int temp=strTo.compareTo(strNow);

					int tempFrom=strFrom.compareTo(strNow);
					if(tempFrom<0)
					{
						ErrorStr += "تاریخ شروع نمی تواند کوچکتر از تاریخ امروز باشد." + "\n";
					}
					else if(temp<0)
					{
						ErrorStr += "تاریخ خاتمه نمی تواند کوچکتر از تاریخ امروز باشد." + "\n";
					}
					else if(temp==0)
					{
						Calendar mcurrentTime = Calendar.getInstance();
						int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
						int minute = mcurrentTime.get(Calendar.MINUTE);
						String Fhour=etFromTime.getText().toString().replace(":","");
						String Thour=etToTime.getText().toString().replace(":","");
						if(Fhour.compareTo(Thour)>0)
						{
							ErrorStr += "ساعت خاتمه نمی تواند کوچکتر از ساعت شروع باشد." + "\n";
						}
						else if(Fhour.compareTo(Thour)==0)
						{
							ErrorStr += "ارائه سرویس در این تاریخ و ساعت مقدور نیست." + "\n";
						}
					}
				}
				if (etFromDate.length() < 8 && etFromDate.length() > 10) {
					ErrorStr += "تاریخ شروع را صحیح وارد نمایید" + "\n";
				}
				if (etToDate.length() < 8 && etToDate.length() > 10) {
					ErrorStr += "تاریخ خاتمه را صحیح وارد نمایید" + "\n";
				}
				if (etFromTime.length() < 3 && etFromTime.length() > 5) {
					ErrorStr += "زمان شروع را صحیح وارد نمایید" + "\n";
				}
				if (etToTime.length() < 3 && etToTime.length() > 5) {
					ErrorStr += "زمان خاتمه را صحیح وارد نمایید" + "\n";
				}
//				if(etFromTime.getText().toString().compareTo(etToTime.getText().toString())>0)
//				{
//					ErrorStr+="ساعت شروع نمی تواند بزرگتر از ساعت خاتمه باشد."+"\n";
//				}

//				if(AddressCode.length()!=10)
//				{
//					ErrorStr+="آدرس را وارد نمایید"+"\n";
//				}
				Description = etDescription.getText().toString();
				//**************************************************************
				if (spStatus.getSelectedItem().toString().compareTo("عادی") == 0) {
					IsEmergency = "0";
				} else {
					IsEmergency = "1";
				}
				//***************************************************************
				if(spTypePeriodService.getVisibility()== View.VISIBLE) {
					if (spTypePeriodService.getSelectedItem().toString().compareTo("روزانه") == 0) {
						PeriodicServices = "1";
					} else if (spTypePeriodService.getSelectedItem().toString().compareTo("هفته در میان") == 0) {
						PeriodicServices = "2";
					} else if (spTypePeriodService.getSelectedItem().toString().compareTo("هفتگی") == 0) {
						PeriodicServices = "3";
					} else {
						PeriodicServices = "4";
					}
				}
				else
				{
					PeriodicServices = "1";
				}
				//***************************************************************
				try {
					EducationGrade = spGraid.getSelectedItem().toString();
				} catch (Exception ex) {
					EducationGrade = "0";
				}
				try {
					EducationTitle = etTitleLearning.getText().toString();
					if (EducationTitle.length() == 0) {
						EducationTitle = "0";
					}
				} catch (Exception ex) {
					EducationTitle = "0";
				}
				//**************************************************************
				if(LinearGenderStudentAndTeacher.getVisibility()== View.VISIBLE) {
					if (spGenderStudent.getSelectedItem().toString().compareTo("خانم") == 0) {
						StudentGender = "1";
					} else if (spGenderStudent.getSelectedItem().toString().compareTo("آقا") == 0) {
						StudentGender = "0";
					} else {
						StudentGender = "2";
					}
				}
				else
				{
					StudentGender = "0";
				}
				//***************************************************************
				if(LinearGenderStudentAndTeacher.getVisibility()== View.VISIBLE) {
					if (spGenderTeacher.getSelectedItem().toString().compareTo("خانم") == 0) {
						TeacherGender = "1";
					} else if (spGenderTeacher.getSelectedItem().toString().compareTo("آقا") == 0) {
						TeacherGender = "2";
					} else if (spGenderTeacher.getSelectedItem().toString().compareTo("فرقی ندارد") == 0) {
						TeacherGender = "3";
					} else {
						TeacherGender = "0";
					}
				}
				else
				{
					TeacherGender = "0";
				}
				//***************************************************************

				try {
				String	FieldOfStudyName = spFieldEducation.getSelectedItem().toString();
					FieldOfStudy = GetFieldofEducationCode(FieldOfStudyName);
				} catch (Exception ex) {
					FieldOfStudy = "0";
				}
				try {
					String ArtName = spFieldArt.getSelectedItem().toString();
					ArtField = GetArtCode(ArtName);
				} catch (Exception ex) {
					ArtField = "0";
				}
				//***************************************************************
				//***************************************************************
				if(LinearCarWash.getVisibility()==View.VISIBLE) {
					if (spTypeService.getSelectedItem().toString().compareTo("روشویی") == 0) {
						CarWashType = "1";
					} else if (spTypeService.getSelectedItem().toString().compareTo(" ") == 0) {
						CarWashType = "0";
					} else {
						CarWashType = "2";
					}
				}
				else
				{
					CarWashType = "0";
				}
				//***************************************************************
				if(LinearCarWash.getVisibility()==View.VISIBLE) {
					if (spTypeCar.getSelectedItem().toString().compareTo("سواری") == 0) {
						CarType = "1";
					} else if (CarType.compareTo("ون") == 0) {
						CarType = "3";
					} else if (CarType.compareTo(" ") == 0) {
						CarType = "0";
					} else {
						CarType = "2";
					}
				}
				else
				{
					CarType = "0";
				}
				//***************************************************************


				try {
					if (LinerLayoutLanguege.getVisibility() == View.VISIBLE) {
						Language = String.valueOf(spLanguage.getSelectedItemId());
					} else
					{
						Language = "0";
					}

				} catch (Exception ex) {
					Language = "0";
				}
				if(LinearStatusCountHamyar.getVisibility()==View.VISIBLE) {
					if (etCountWoman.getText().length() > 0 || etCountMan.getText().length() > 0 || etDoesnotmatter.getText().length() > 0) {
						if (etCountWoman.getText().toString().compareTo("0") == 0
								&& etCountMan.getText().toString().compareTo("0") == 0
								&& etDoesnotmatter.getText().toString().compareTo("0") == 0) {
							ErrorStr += "تعداد همیار را مشخص نمایید" + "\n";
						}
					} else {
						ErrorStr += "تعداد همیار را مشخص نمایید" + "\n";
					}
				}

				if (ErrorStr.length() == 0) {
					{
                        String spDateStart[]=PersianDigitConverter.EnglishNumber(etFromDate.getText().toString()).split("/");
                        StartYear=spDateStart[0];
                        StartMonth=spDateStart[1];
                        StartDay=spDateStart[2];
                        //*************************************************************
                        String spDateEnd[]=PersianDigitConverter.EnglishNumber(etToDate.getText().toString()).split("/");
                        EndYear=spDateEnd[0];
                        EndMonth=spDateEnd[1];
                        EndDay=spDateEnd[2];
                        //*************************************************************
						String SpStartTime[]=PersianDigitConverter.EnglishNumber(etFromTime.getText().toString()).split(":");
						StartHour=SpStartTime[0];
						StartMinute=SpStartTime[1];
						String FullStartTime = StartHour+StartMinute;
						String FinalStartFullDate = strFrom+StartHour+StartMinute;
						String FinalNowFullDate  = strNow + calNow.getTime().getHours()+calNow.getTime().getMinutes();
						String SpEndTime[]=PersianDigitConverter.EnglishNumber(etToTime.getText().toString()).split(":");
						EndHour=SpEndTime[0];
						EndMinute=SpEndTime[1];
						String FullEndTime = EndHour+EndMinute;
						int TempDate = strFrom.compareTo(strTo);
						int TempTime = FullEndTime.compareTo(FullStartTime);
						int TempStartDate = FinalStartFullDate.compareTo(FinalNowFullDate);
						if(TempStartDate > 0) {
							boolean Flag = false;
							if(TempDate==0 & TempTime < 0)
							{
								Flag = true;
							}
							if(Flag==false) {
								SyncInsertUserServices syncInsertUserServices = new SyncInsertUserServices(Service_Request.this,
										karbarCode, DetailCode, MaleCount, FemaleCount, HamyarCount, StartYear, StartMonth,
										StartDay, StartHour, StartMinute, EndYear, EndMonth, EndDay, EndHour, EndMinute,
										AddressCode, Description, IsEmergency, PeriodicServices, EducationGrade,
										FieldOfStudy, StudentGender, TeacherGender, EducationTitle, ArtField, CarWashType, CarType, Language);
								syncInsertUserServices.AsyncExecute();
							}
							else
							{
								Toast.makeText(Service_Request.this, "ساعت پایان نباید کوچکتر از ساعت شروع باشد", Toast.LENGTH_SHORT).show();
							}
						}
						else
						{
							Toast.makeText(Service_Request.this, "تاریخ و ساعت شروع نباید کوچکتر از زمان جاری باشد", Toast.LENGTH_SHORT).show();
						}
					}

				} else {
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
					PersianDatePickerDialog picker = new PersianDatePickerDialog(Service_Request.this);
					picker.setPositiveButtonString("تایید");
					picker.setNegativeButton("انصراف");
					picker.setTodayButton("امروز");
					picker.setTodayButtonVisible(true);
					//  picker.setInitDate(initDate);
					picker.setMaxYear(PersianDatePickerDialog.THIS_YEAR);
					picker.setMinYear(1396);
					picker.setActionTextColor(Color.GRAY);
					//picker.setTypeFace(FontMitra);
					picker.setListener(new Listener() {

						@Override
						public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {
							//Toast.makeText(getApplicationContext(), persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
							StartYear=String.valueOf(persianCalendar.getPersianYear());
							if(persianCalendar.getPersianMonth()<10)
							{
								StartMonth="0"+String.valueOf(persianCalendar.getPersianMonth());
							}
							else
							{
								StartMonth=String.valueOf(persianCalendar.getPersianMonth());
							}
							if(persianCalendar.getPersianDay()<10)
							{
								StartDay="0"+String.valueOf(persianCalendar.getPersianDay());
							}
							else
							{
								StartDay=String.valueOf(persianCalendar.getPersianDay());
							}

							etFromDate.setText(PersianDigitConverter.PerisanNumber(StartYear + "/" + StartMonth + "/" + StartDay));

						}

						@Override
						public void onDismissed() {

						}
					});
					picker.show();
				}

			}
		});
		etFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersianDatePickerDialog picker = new PersianDatePickerDialog(Service_Request.this);
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
						StartYear=String.valueOf(persianCalendar.getPersianYear());
						if(persianCalendar.getPersianMonth()<10)
						{
							StartMonth="0"+String.valueOf(persianCalendar.getPersianMonth());
						}
						else
						{
							StartMonth=String.valueOf(persianCalendar.getPersianMonth());
						}
						if(persianCalendar.getPersianDay()<10)
						{
							StartDay="0"+String.valueOf(persianCalendar.getPersianDay());
						}
						else
						{
							StartDay=String.valueOf(persianCalendar.getPersianDay());
						}

						etFromDate.setText(PersianDigitConverter.PerisanNumber(StartYear + "/" + StartMonth + "/" + StartDay));
					}

					@Override
					public void onDismissed() {

					}
				});
				picker.show();

			}
		});
		etToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					PersianDatePickerDialog picker = new PersianDatePickerDialog(Service_Request.this);
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
							EndYear=String.valueOf(persianCalendar.getPersianYear());
							if(persianCalendar.getPersianMonth()<10)
							{
								EndMonth="0"+String.valueOf(persianCalendar.getPersianMonth());
							}
							else
							{
								EndMonth=String.valueOf(persianCalendar.getPersianMonth());
							}
							if(persianCalendar.getPersianDay()<10)
							{
								EndDay="0"+String.valueOf(persianCalendar.getPersianDay());
							}
							else
							{
								EndDay=String.valueOf(persianCalendar.getPersianDay());
							}

							etToDate.setText(PersianDigitConverter.PerisanNumber(EndYear + "/" + EndMonth + "/" + EndDay));
						}

						@Override
						public void onDismissed() {

						}
					});
					picker.show();
				}

			}
		});
		etToDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersianDatePickerDialog picker = new PersianDatePickerDialog(Service_Request.this);
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
						EndYear=String.valueOf(persianCalendar.getPersianYear());
						if(persianCalendar.getPersianMonth()<10)
						{
							EndMonth="0"+String.valueOf(persianCalendar.getPersianMonth());
						}
						else
						{
							EndMonth=String.valueOf(persianCalendar.getPersianMonth());
						}
						if(persianCalendar.getPersianDay()<10)
						{
							EndDay="0"+String.valueOf(persianCalendar.getPersianDay());
						}
						else
						{
							EndDay=String.valueOf(persianCalendar.getPersianDay());
						}

						etToDate.setText(PersianDigitConverter.PerisanNumber(EndYear + "/" + EndMonth + "/" + EndDay));
					}

					@Override
					public void onDismissed() {

					}
				});
				picker.show();
			}
		});
		//***********************************************
		etFromTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {

					GetTime(etFromTime,StartHour,StartMinute);
				}
			}
		});
		etFromTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				GetTime(etFromTime,StartHour,StartMinute);
			}
		});
		etToTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					GetTime(etToTime,EndHour,EndMinute);
				}
			}
		});
		etToTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				GetTime(etToTime,EndHour,EndMinute);
			}
		});
	}



	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
		if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            LoadActivity2(List_ServiceDerails.class,
                    "karbarCode", karbarCode,
                    "codeService",DetailCode);
		}

		return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		Service_Request.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName1, String VariableValue1)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName1, VariableValue1);
		Service_Request.this.startActivity(intent);
	}
	public void LoadActivity_Map(Class<?> Cls, String VariableName, String VariableValue,
                              String VariableName2, String VariableValue2,
                              String VariableName3, String VariableValue3,
                              String VariableName4, String VariableValue4,
                              String VariableName5, String VariableValue5,
                              String VariableName6, String VariableValue6,
                              String VariableName7, String VariableValue7,
                              String VariableName8, String VariableValue8,
                              String VariableName9, String VariableValue9,
                              String VariableName10, String VariableValue10,
                              String VariableName11, String VariableValue11,
                              String VariableName12, String VariableValue12,
                              String VariableName13, String VariableValue13,
                              String VariableName14, String VariableValue14,
                              String VariableName15, String VariableValue15,
                              String VariableName16, String VariableValue16,
                              String VariableName17, String VariableValue17,
                              String VariableName18, String VariableValue18,
                              String VariableName19, String VariableValue19,
                              String VariableName20, String VariableValue20,
                              String VariableName21, String VariableValue21,
                              String VariableName22, String VariableValue22) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        intent.putExtra(VariableName3, VariableValue3);
        intent.putExtra(VariableName4, VariableValue4);
        intent.putExtra(VariableName5, VariableValue5);
        intent.putExtra(VariableName6, VariableValue6);
        intent.putExtra(VariableName7, VariableValue7);
        intent.putExtra(VariableName8, VariableValue8);
        intent.putExtra(VariableName9, VariableValue9);
        intent.putExtra(VariableName10, VariableValue10);
        intent.putExtra(VariableName11, VariableValue11);
        intent.putExtra(VariableName12, VariableValue12);
        intent.putExtra(VariableName13, VariableValue13);
        intent.putExtra(VariableName14, VariableValue14);
        intent.putExtra(VariableName15, VariableValue15);
        intent.putExtra(VariableName16, VariableValue16);
        intent.putExtra(VariableName17, VariableValue17);
        intent.putExtra(VariableName18, VariableValue18);
        intent.putExtra(VariableName19, VariableValue19);
        intent.putExtra(VariableName20, VariableValue20);
        intent.putExtra(VariableName21, VariableValue21);
        intent.putExtra(VariableName22, VariableValue22);
		startActivity(intent);
	}
	public void form1()
	{

		//**********************************************
		LinearDate.setVisibility(View.VISIBLE);
		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		tvTitleTypePeriodService.setVisibility(View.VISIBLE);
		spTypePeriodService.setVisibility(View.VISIBLE);
		FillSpinner("TypePeriodService","Title",spTypePeriodService);
		//**********************************************
		LinearStatusCountHamyar.setVisibility(View.VISIBLE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudentAndTeacher.setVisibility(View.GONE);
		LinearCarWash.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinerLayoutLanguege.setVisibility(View.GONE);
	}
	public void form2()
	{

		//**********************************************
		LinearDate.setVisibility(View.VISIBLE);

		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		tvTitleTypePeriodService.setVisibility(View.GONE);
		spTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountHamyar.setVisibility(View.GONE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudentAndTeacher.setVisibility(View.GONE);
		LinearCarWash.setVisibility(View.GONE);
		LinerLayoutLanguege.setVisibility(View.GONE);
	}

	public void form3()
	{

		//**********************************************
		LinearDate.setVisibility(View.VISIBLE);

		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		tvTitleTypePeriodService.setVisibility(View.GONE);
		spTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountHamyar.setVisibility(View.VISIBLE);

		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudentAndTeacher.setVisibility(View.GONE);
		LinearCarWash.setVisibility(View.GONE);
		LinerLayoutLanguege.setVisibility(View.GONE);
	}
	public void form4()
	{

		//**********************************************
		LinearDate.setVisibility(View.VISIBLE);

		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		tvTitleTypePeriodService.setVisibility(View.GONE);
		spTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountHamyar.setVisibility(View.VISIBLE);

		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudentAndTeacher.setVisibility(View.GONE);
		LinearCarWash.setVisibility(View.GONE);
		LinerLayoutLanguege.setVisibility(View.GONE);
	}
	public void form5()
	{

		//**********************************************
		LinearDate.setVisibility(View.VISIBLE);

		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		tvTitleTypePeriodService.setVisibility(View.GONE);
		spTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountHamyar.setVisibility(View.GONE);

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
		LinearGenderStudentAndTeacher.setVisibility(View.VISIBLE);
		FillSpinner("GenderStudent","Title",spGenderStudent);
		FillSpinner("GenderTeacher","Title",spGenderTeacher);
		LinearCarWash.setVisibility(View.GONE);
		LinerLayoutLanguege.setVisibility(View.GONE);
	}
	public void form6()
	{

		//**********************************************
		LinearDate.setVisibility(View.VISIBLE);

		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		tvTitleTypePeriodService.setVisibility(View.GONE);
		spTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountHamyar.setVisibility(View.GONE);

		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudentAndTeacher.setVisibility(View.VISIBLE);
		FillSpinner("GenderStudent","Title",spGenderStudent);
		FillSpinner("GenderTeacher","Title",spGenderTeacher);
		LinearCarWash.setVisibility(View.GONE);
		LinerLayoutLanguege.setVisibility(View.VISIBLE);
		FillSpinner("Language","Title",spLanguage);
	}
	public void form7()
	{

		//**********************************************
		LinearDate.setVisibility(View.VISIBLE);

		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		tvTitleTypePeriodService.setVisibility(View.GONE);
		spTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountHamyar.setVisibility(View.GONE);

		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.VISIBLE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudentAndTeacher.setVisibility(View.VISIBLE);
		FillSpinner("GenderStudent","Title",spGenderStudent);
		FillSpinner("GenderTeacher","Title",spGenderTeacher);
		LinearCarWash.setVisibility(View.GONE);
		LinerLayoutLanguege.setVisibility(View.GONE);
	}
	public void form8()
	{

		//**********************************************
		LinearDate.setVisibility(View.VISIBLE);

		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		tvTitleTypePeriodService.setVisibility(View.GONE);
		spTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountHamyar.setVisibility(View.GONE);

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
		LinearGenderStudentAndTeacher.setVisibility(View.VISIBLE);
		FillSpinner("GenderStudent","Title",spGenderStudent);
		FillSpinner("GenderTeacher","Title",spGenderTeacher);
		LinearCarWash.setVisibility(View.GONE);
		LinerLayoutLanguege.setVisibility(View.GONE);
	}
	public void form9()
	{
		//**********************************************
		LinearDate.setVisibility(View.VISIBLE);

		//**********************************************
		LinearFromTime.setVisibility(View.VISIBLE);
		LinearToTime.setVisibility(View.VISIBLE);
		//**********************************************
		tvTitleTypePeriodService.setVisibility(View.GONE);
		spTypePeriodService.setVisibility(View.GONE);
		//**********************************************
		LinearStatusCountHamyar.setVisibility(View.GONE);
		//**********************************************
		LinearAddres.setVisibility(View.VISIBLE);
		LinearDescription.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudentAndTeacher.setVisibility(View.GONE);
		LinearCarWash.setVisibility(View.VISIBLE);
		FillSpinner("carwash","Title",spTypeService);
		FillSpinner("TypeCar","Title",spTypeCar);
		LinerLayoutLanguege.setVisibility(View.GONE);
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
	public void GetTime(final EditText editText, final String value_Hour, final String value_Min)
	{
		Calendar now = Calendar.getInstance();
//		MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {
//
//			@Override
//			public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {
//				// TODO Auto-generated method stub
//				/*time.setText(getString(R.string.time) + String.format("%02d", hourOfDay)+
//						":" + String.format("%02d", minute) +
//						":" + String.format("%02d", seconds));	*/
////				db=dbh.getWritableDatabase();
////				String query="UPDATE  DateTB SET Time = '" +String.valueOf(hourOfDay)+":"+String.valueOf(minute)+"'";
////				db.execSQL(query);
//				String  hour,min;
//				if(hourOfDay<10)
//				{
//					hour="0"+String.valueOf(hourOfDay);
//				}
//				else
//				{
//					hour=String.valueOf(hourOfDay);
//				}
//				if(minute<10)
//				{
//					min="0"+String.valueOf(minute);
//				}
//				else
//				{
//					min=String.valueOf(minute);
//				}
//
//				editText.setText(hour + ":" + min);
//			}
//		}, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
//		mTimePicker.setTitle("انتخاب زمان");
//		mTimePicker.setButton(BUTTON_POSITIVE,"تایید",mTimePicker);
//		mTimePicker.setButton(BUTTON_NEGATIVE,"انصراف",mTimePicker);
//		mTimePicker.show();
		Alert_Clock alert_clock=new Alert_Clock(Service_Request.this, new Alert_Clock.OnTimeSetListener() {
			@Override
			public void onTimeSet(String hourOfDay, String minute) {
				editText.setText(hourOfDay + ":" + minute);
			}
		}, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
		alert_clock.show();
	}

	public String GetArtCode(String ArtName)
	{
		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Arts WHERE Title='"+ArtName+"'",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			return coursors.getString(coursors.getColumnIndex("Code"));
		}
		return"0";
	}
	public String GetFieldofEducationCode(String FieldofEducationName)
	{
		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM FieldofEducation WHERE Title='"+FieldofEducationName+"'",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			return coursors.getString(coursors.getColumnIndex("Code"));
		}
		return"0";
	}
    public void FillForm()
    {
            if(MaleCount.compareTo("0")!=0) {
                etCountMan.setText(PersianDigitConverter.PerisanNumber(MaleCount));
            }
            if(FemaleCount.compareTo("0")!=0) {
                etCountWoman.setText(PersianDigitConverter.PerisanNumber(FemaleCount));
            }
            if(HamyarCount.compareTo("0")!=0) {

                etDoesnotmatter.setText(PersianDigitConverter.PerisanNumber(HamyarCount));
            }
            if(StartYear.compareTo("")!=0 ||
                    StartYear.compareTo("null")!=0) {
                etFromDate.setText(PersianDigitConverter.PerisanNumber(StartYear));
            }
            if(StartHour.compareTo("")!=0 ||
                    StartHour.compareTo("null")!=0) {
                etFromTime.setText(PersianDigitConverter.PerisanNumber(StartHour));
            }
            if(EndYear.compareTo("")!=0 ||
                    EndYear.compareTo("null")!=0) {
                etToDate.setText(PersianDigitConverter.PerisanNumber(EndYear));
            }
            if(EndHour.compareTo("")!=0 ||
                    EndHour.compareTo("null")!=0) {
                etToTime.setText(PersianDigitConverter.PerisanNumber(EndHour));
            }
            if(Description.length()!=0) {
                etDescription.setText(PersianDigitConverter.PerisanNumber(Description));
            }
            if(IsEmergency.compareTo("0")==0) {

                spStatus.setSelection(0);
            }
            else
            {
                spStatus.setSelection(1);
            }
            if(PeriodicServices.compareTo("1")==0) {
                spTypePeriodService.setSelection(0);
            }
            else if(PeriodicServices.compareTo("2")==0)
            {
                spTypePeriodService.setSelection(1);
            }
            else if(PeriodicServices.compareTo("3")==0)
            {
                spTypePeriodService.setSelection(2);
            }
            else
            {
                spTypePeriodService.setSelection(3);
            }
            //*********************Grade**********************************************
            int lensp=spGraid.getCount();
            int positionString=0;
            for(int i=0;i<lensp;i++){
                if(EducationGrade.compareTo(spGraid.getItemAtPosition(i).toString())==0)
                {
                    positionString=i;
                    break;
                }
            }
            spGraid.setSelection(positionString);
            //*****************************************************************************
            if(FieldOfStudy.length()!=0||
                    FieldOfStudy.compareTo("null")!=0) {
                etTitleLearning.setText(FieldOfStudy);
            }
            if(StudentGender.compareTo("2")==0) {
                spGenderStudent.setSelection(1);
            }
            else
            {
                spGenderStudent.setSelection(2);
            }
            if(TeacherGender.compareTo("1")==0) {
                spGenderTeacher.setSelection(1);
            }
            else if(TeacherGender.compareTo("2")==0)
            {
                spGenderTeacher.setSelection(2);
            }
            else
            {
                spGenderTeacher.setSelection(3);
            }
            //*********************Education**********************************************
            lensp=spFieldEducation.getCount();
            positionString=0;
            for(int i=0;i<lensp;i++){
                if(EducationTitle.compareTo(spFieldEducation.getItemAtPosition(i).toString())==0)
                {
                    positionString=i;
                    break;
                }
            }
            spFieldEducation.setSelection(positionString);
            //*********************ArtField**********************************************
            lensp=spFieldArt.getCount();
            positionString=0;
            for(int i=0;i<lensp;i++){
                if(ArtField.compareTo(spFieldArt.getItemAtPosition(i).toString())==0)
                {
                    positionString=i;
                    break;
                }
            }
            spFieldArt.setSelection(positionString);
            //*****************************************************************************
            //*********************Language**********************************************
            lensp=spLanguage.getCount();
            positionString=0;
            for(int i=0;i<lensp;i++){
                if(Language.compareTo(spLanguage.getItemAtPosition(i).toString())==0)
                {
                    positionString=i;
                    break;
                }
            }
            spLanguage.setSelection(positionString);
            //*********************Address**********************************************
                spAddress.setSelection(Integer.parseInt(AddressCode));
            //*****************************************************************************
            if(CarWashType.compareTo("1")==0) {
                spTypeService.setSelection(1);
            }
            else
            {
                spTypeService.setSelection(2);
            }
            //*****************************************************************************
            if(CarType.compareTo("1")==0) {
                spTypeCar.setSelection(1);
            }
            else if(CarType.compareTo("2")==0)
            {
                spTypeCar.setSelection(2);
            }
            else
            {
                spTypeCar.setSelection(3);
            }
        }
}
