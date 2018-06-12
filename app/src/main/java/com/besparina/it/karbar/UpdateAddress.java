package com.besparina.it.karbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by hashemi on 01/23/2018.
 */

public class UpdateAddress extends AppCompatActivity {
    private String karbarCode;

    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Button btnSaveLocation;
    private EditText NameAddres;
    private EditText AddAddres;
    public double lat;
    private double lon;
    private GoogleMap map;
    private String backToActivity;
    private String AddressCode;
    private EditText etEmail;
    private String IsDefault="0";
    private CheckBox chbIsDefaultAddres;
    private Spinner spState;
    private Spinner spCity;
    private List<String> labels_State = new ArrayList<String>();
    private List<String> labels_City = new ArrayList<String>();



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        etEmail=(EditText)findViewById(R.id.etEmail);
        spState=(Spinner)findViewById(R.id.spState);
        spCity=(Spinner)findViewById(R.id.spCity);
        btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
        NameAddres=(EditText)findViewById(R.id.NameAddres);
        AddAddres=(EditText)findViewById(R.id.AddAddres);
        chbIsDefaultAddres=(CheckBox) findViewById(R.id.chbIsDefaultAddres);
        try {
            karbarCode = getIntent().getStringExtra("karbarCode").toString();
        }
        catch (Exception e) {
            karbarCode = "";
        }
        try {
            AddressCode = getIntent().getStringExtra("AddressCode").toString();
        }
        catch (Exception e) {
            AddressCode = "";
        }
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

        //Fill Spinner State
        db=dbh.getReadableDatabase();
        Cursor cursors = db.rawQuery("SELECT * FROM State ",null);
        String str;
        for(int i=0;i<cursors.getCount();i++){
            cursors.moveToNext();
            str=cursors.getString(cursors.getColumnIndex("Name"));
            labels_State.add(str);
        }
        db.close();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels_State);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spState.setAdapter(dataAdapter);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Fill Spinner City
                db = dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM State WHERE Name='"+parent.getItemAtPosition(position).toString()+"'", null);
                if (coursors.getCount() > 0) {
                    coursors.moveToNext();
                    String Code=coursors.getString(coursors.getColumnIndex("Code"));
                    FillSpinnerChild(Code);
                }
                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CodeState,CodeCity;
                db=dbh.getWritableDatabase();
                String StrnameAddress=NameAddres.getText().toString().trim();
                String StrAddAddres=AddAddres.getText().toString().trim();
                String StrError="";
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
                if(StrError.length()==0 || StrError.compareTo("")==0)
                {
                    String latStr=Double.toString(lat);
                    String lonStr=Double.toString(lon);

                    SyncUpdateAddress syncUpdateAddress =new SyncUpdateAddress(UpdateAddress.this,karbarCode,AddressCode,IsDefault,StrnameAddress,CodeState,CodeCity,StrAddAddres,etEmail.getText().toString(),latStr,lonStr,"1","1");
                    syncUpdateAddress.AsyncExecute();
                }

                db.close();
            }
        });
        //*************************************************************************************************
        db=dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM address WHERE Status='1' AND Code='"+AddressCode+"'",null);
        if(coursors.getCount()>0)
        {

            coursors.moveToNext();
            NameAddres.setText(PersianDigitConverter.PerisanNumber(coursors.getString(coursors.getColumnIndex("Name"))));
            AddAddres.setText(PersianDigitConverter.PerisanNumber(coursors.getString(coursors.getColumnIndex("AddressText"))));
            etEmail.setText(coursors.getString(coursors.getColumnIndex("Email")));
            if(coursors.getString(coursors.getColumnIndex("IsDefault")).compareTo("0")==0)
            {
                chbIsDefaultAddres.setChecked(false);
            }
            else
            {
                chbIsDefaultAddres.setChecked(true);
            }
            for (int i = 0; i < spState.getCount(); i++) {
                if (spState.getItemAtPosition(i).equals(coursors.getString(coursors.getColumnIndex("State")))) {
                    spState.setSelection(i);
                    break;
                }
            }
            for (int j = 0; j < spCity.getCount(); j++) {
                if (spCity.getItemAtPosition(j).equals(coursors.getString(coursors.getColumnIndex("City")))) {
                    spCity.setSelection(j);
                    break;
                }
            }
            lat=Double.parseDouble(coursors.getString(coursors.getColumnIndex("Lat")));
            lon=Double.parseDouble(coursors.getString(coursors.getColumnIndex("Lng")));
        }
        db.close();
        //*************************************************************************************************
//*************************************************************************************************
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3)).getMapAsync(new OnMapReadyCallback() {
            @Override

            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                LatLng point;
                point = new LatLng(lat, lon);
                map.addMarker(new MarkerOptions().position(point).title("آدرس").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));


                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        String str = latLng.toString();
                        //  Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
                        map.clear();
                        map.addMarker(new MarkerOptions().position(latLng).title("آدرس").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                        lat=latLng.latitude;
                        lon=latLng.longitude;
                    }
                });
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
        //******************************************************************************************************
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                LoadActivity(Profile.class, "karbarCode", karbarCode);
        }

        return super.onKeyDown(keyCode, event);
    }

    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        db.close();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels_City);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(dataAdapter);
    }

}

