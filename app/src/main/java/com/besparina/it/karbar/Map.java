package com.besparina.it.karbar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by hashemi on 01/23/2018.
 */

public class Map extends AppCompatActivity {
    private String karbarCode;
    private String area="";
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Button btnSaveLocation;
    private EditText NameAddres;
    private EditText AddAddres;
    public double lat;
    private double lang;
    private GoogleMap map;
    private String backToActivity;
    private String DetailCode;
    private EditText etArea;
    private String IsDefault = "0";
    private CheckBox chbIsDefaultAddres;
    private Spinner spState;
    private Spinner spCity;
    private List<String> labels_State = new ArrayList<String>();
    private List<String> labels_City = new ArrayList<String>();
    public Handler mHandler;
    public boolean continue_or_stop=true;
    private GPSTracker gps;
    //*************************************
    private String MaleCount ;
    private String FemaleCount ;
    private String HamyarCount ;
    private String StartYear ;
    private String StartMonth ;
    private String StartDay ;
    private String StartHour ;
    private String EndYear ;
    private String EndHour ;
    private String AddressCode ;
    private String Description ;
    private String EducationGrade ;
    private String FieldOfStudy ;
    private String EducationTitle ;
    private String ArtField ;
    private String Language ;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        etArea = (EditText) findViewById(R.id.etArea);
        spState = (Spinner) findViewById(R.id.spState);
        spCity = (Spinner) findViewById(R.id.spCity);
        btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
        NameAddres = (EditText) findViewById(R.id.NameAddres);
        AddAddres = (EditText) findViewById(R.id.AddAddres);
        chbIsDefaultAddres = (CheckBox) findViewById(R.id.chbIsDefaultAddres);
        gps = new GPSTracker(Map.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            //nothing
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        setMap();
        mHandler = new Handler();
        continue_or_stop = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (continue_or_stop) {
                    try {
                        Thread.sleep(5000); // every 5 seconds
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {

                                if(etArea.length()>0){
                                    String AreaAddress=etArea.getText().toString().trim();
                                    if(AreaAddress.length()>0){
                                        getLatLongLocation(AreaAddress);
                                        area=getStringLocation();
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
//******************************************************************************
        try {
            karbarCode = getIntent().getStringExtra("karbarCode").toString();
        } catch (Exception e) {
            karbarCode = "";
        }
        try {
            backToActivity = getIntent().getStringExtra("nameActivity").toString();
        } catch (Exception e) {
            backToActivity = "";
        }
        try {
            DetailCode = getIntent().getStringExtra("DetailCode").toString();
        } catch (Exception e) {
            DetailCode = "";
        }try {
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
//******************************************************************************
        dbh = new DatabaseHelper(getApplicationContext());
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

        //*************************************************************************************************
        //Fill Spinner State
        db = dbh.getReadableDatabase();
        final Cursor cursors = db.rawQuery("SELECT * FROM State ", null);
        String str;
        for (int i = 0; i < cursors.getCount(); i++) {
            cursors.moveToNext();
            str = cursors.getString(cursors.getColumnIndex("Name"));
            labels_State.add(str);
        }
        try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels_State);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spState.setAdapter(dataAdapter);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Fill Spinner City
                db = dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM State WHERE Name='" + parent.getItemAtPosition(position).toString() + "'", null);
                if (coursors.getCount() > 0) {
                    coursors.moveToNext();
                    String Code = coursors.getString(coursors.getColumnIndex("Code"));
                    FillSpinnerChild(Code);
                }
                try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*************************************************************************************************

        //******************************************************************************************************
        etArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        chbIsDefaultAddres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    IsDefault="1";
                }
                else
                {
                    IsDefault="0";
                }
            }
        });
        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CodeState,CodeCity;
                try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
                String StrnameAddress=NameAddres.getText().toString().trim();
                String StrAddAddres=AddAddres.getText().toString().trim();
                String StrError="";
                String email="";
                if(StrAddAddres.length()==0 || StrAddAddres.compareTo("")==0)
                {
                    StrError="آدرس دقیق محل را وارد نمایید"+"\n";
                }
                if(StrnameAddress.length()==0 || StrnameAddress.compareTo("")==0)
                {
                    StrError="نامی دلخواه برای آدرس محل وارد نمایید."+"\n";
                }
                db = dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM State WHERE Name='"+spState.getSelectedItem().toString()+"'", null);
                if (coursors.getCount() > 0) {
                    coursors.moveToNext();
                    CodeState = coursors.getString(coursors.getColumnIndex("Code"));
                }
                else{
                    CodeState="";
                }
                coursors = db.rawQuery("SELECT * FROM City WHERE Name='"+spCity.getSelectedItem().toString()+"'", null);
                if (coursors.getCount() > 0) {
                    coursors.moveToNext();
                    CodeCity = coursors.getString(coursors.getColumnIndex("Code"));
                }
                else{
                    CodeCity="";
                }
//                try
//                {
//                   email= etEmail.getText().toString();
//                }
//                catch (Exception e)
//                {
//                    email="";
//                }
                if(StrError.length()==0 || StrError.compareTo("")==0)
                {
                    String latStr=Double.toString(lat);
                    String lonStr=Double.toString(lang);
                    try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                    Cursor cursor = db.rawQuery("SELECT * FROM address WHERE Status='1' AND Name='"+StrnameAddress+"'",null);
                    if(cursor.getCount()>0) {
                        Toast.makeText(Map.this,"نام تکراری است",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        SyncAddress syncAddress = new SyncAddress(Map.this, karbarCode, IsDefault, StrnameAddress, CodeState, CodeCity, StrAddAddres, email, latStr, lonStr);//todo send Area
                        syncAddress.AsyncExecute();
                    }
                    if(!cursor.isClosed())
                    {
                        cursor.close();
                    }
                }
                else
                {
                    Toast.makeText(Map.this,StrError,Toast.LENGTH_LONG).show();
                }
                if(db.isOpen()) {
                    try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
                }
                if(!cursors.isClosed())
                {
                    cursors.close();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(backToActivity.compareTo("Profile")==0){
                continue_or_stop=false;
                LoadActivity(Profile.class, "karbarCode", karbarCode);
            }else
            {
                continue_or_stop=false;
                LoadActivity2(Service_Request.class,
                        "karbarCode", karbarCode,
                        "DetailCode",DetailCode,
                        "MaleCount", MaleCount,
                        "FemaleCount", FemaleCount,
                        "HamyarCount", HamyarCount,
                        "StartYear", StartYear,
                        "StartHour", StartHour,
                        "EndYear", EndYear,
                        "EndHour", EndHour ,
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
                        "Language", Language
                );
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
    }
    public void LoadActivity2(Class<?> Cls,
                              String VariableName, String VariableValue,
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
        this.startActivity(intent);
    }
    private void FillSpinnerChild(String StateId) {
        labels_City  = new ArrayList<String>();
        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM City WHERE ParentCode='"+StateId+"'", null);
        if (coursors.getCount() > 0) {
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();
                labels_City.add(coursors.getString(coursors.getColumnIndex("Name")));
            }
        }
        try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels_City);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(dataAdapter);
    }
    private  String getStringLocation()
    {
        Locale locale = new Locale("fa");

        Geocoder geocoder = new Geocoder(getApplicationContext(), locale);

        List<Address> list;

        try {
            list = geocoder.getFromLocation(lat, lang, 2);

            Address address = list.get(0);


            //Toast.makeText(getApplicationContext(), "CountryCode: " + address.getCountryCode() +
//                    " ,AdminArea : " + address.getAdminArea() +
//                    " ,CountryName : " + address.getCountryName() +
//                    " ,SubLocality : " + address.getSubLocality()+address.getFeatureName(), Toast.LENGTH_SHORT).show();
            if(address.getSubLocality()!=null)
            {
                return address.getSubLocality();
            }
            else {
                return address.getThoroughfare();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        catch (Exception e){
            return "";
        }
    }
    private  void getLatLongLocation(String addressStr)
    {
        Locale locale = new Locale("fa");

        Geocoder geocoder = new Geocoder(getApplicationContext(), locale);

        List<Address> list;

        try {
            String StateAndCity="";
            StateAndCity=spState.getSelectedItem().toString()+","+spCity.getSelectedItem().toString();
            list = geocoder.getFromLocationName(StateAndCity+","+addressStr,3);

            Address address = list.get(0);

            LatLng mlatLng;
            double mlat,mlong;
            mlat=address.getLatitude();
            mlong=address.getLongitude();
            if(mlat>0 && mlong>0) {
                mlatLng = new LatLng(address.getLatitude(), address.getLongitude());
                lat=mlat;
                lang=mlong;
                map.clear();
                map.addMarker(new MarkerOptions().position(mlatLng).title("مکان من").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(mlatLng, 14));
                //  Toast.makeText(getApplicationContext(), "Lat: " + address.getLatitude() +
//                        "\n" +
//                        "Long: " + address.getLongitude(), Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){

        }
    }
    public  void setMap()
    {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3)).getMapAsync(new OnMapReadyCallback() {
            @Override

            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        lat = gps.getLatitude();
                        lang = gps.getLongitude();
                        LatLng latLng;
                        if(lat>0 && lang>0){
                            latLng = new LatLng(lat,lang);
                        }
                        else {
                            latLng = new LatLng(0,0);
                        }
                        map.clear();
                        map.addMarker(new MarkerOptions().position(latLng).title("مکان من").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                        area=getStringLocation();
                        return false;
                    }
                });
                LatLng point;
                lat=35.691063;
                lang=51.407941;
                point = new LatLng(lat, lang);
                db = dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
                if (coursors.getCount() > 0) {
                    coursors.moveToNext();
                    String latStr = coursors.getString(coursors.getColumnIndex("Lat"));
                    String lonStr = coursors.getString(coursors.getColumnIndex("Lon"));
                    lat = Double.parseDouble(latStr);
                    lang = Double.parseDouble(lonStr);
                    if (latStr.compareTo("0")!=0 && lonStr.compareTo("0")!=0) {
                        point = new LatLng(lat, lang);
                    }
                }
                try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
                map.addMarker(new MarkerOptions().position(point).title("سرویس").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));


                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                //***************************************************************
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        String str = latLng.toString();
                        //  Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
                        map.clear();
                        map.addMarker(new MarkerOptions().position(latLng).title("محل سرویس دهی").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                        lat=latLng.latitude;
                        lang=latLng.longitude;
                        area=getStringLocation();
                    }
                });
            }
        });
    }
}

