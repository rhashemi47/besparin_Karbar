package com.besparina.it.karbar;

import android.*;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class About extends AppCompatActivity {
	private String karbarCode;

	private DatabaseHelper dbh;
	private TextView txtContent;
	private SQLiteDatabase db;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private Button btnServiceEmergency;
	private GoogleMap map;
	private Typeface FontMitra;
	private LatLng point;
	private ImageView imgEmail;
	private ImageView imgWathsUp;
	private ImageView imgInstagram;
	private ImageView imgSkyp;
	private ImageView imgTelegram;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.about);
	btnOrder=(Button)findViewById(R.id.btnOrderBottom);
	btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
	btncredite=(Button)findViewById(R.id.btncrediteBottom);
	btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);
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
		Cursor coursors = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();

			karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
		}
	}

	ImageView imgview = (ImageView)findViewById(R.id.BesparinaLogo);
	imgEmail=(ImageView) findViewById(R.id.imgEmail);
	imgWathsUp=(ImageView) findViewById(R.id.imgWathsUp);
	imgInstagram=(ImageView) findViewById(R.id.imgInstagram);
	imgSkyp=(ImageView) findViewById(R.id.imgSkyp);
	imgTelegram=(ImageView) findViewById(R.id.imgTelegram);
	imgview.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity(MainMenu.class,"","");
		}
	});

	FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");//set font for page
	txtContent=(TextView)findViewById(R.id.tvTextAbout);
	txtContent.setTypeface(FontMitra);
//	db=dbh.getReadableDatabase();
//	Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//			"LEFT JOIN " +
//			"Servicesdetails ON " +
//			"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0' ORDER BY CAST(OrdersService.Code AS int) desc", null);
//	if (cursor2.getCount() > 0) {
//		btnOrder.setText("درخواست ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
//	}
//	cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13) ORDER BY CAST(Code AS int) desc", null);
//	if (cursor2.getCount() > 0) {
//		btnAcceptOrder.setText("پذیرفته شده ها(" + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
//	}
//	cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
//	if (cursor2.getCount() > 0) {
//		cursor2.moveToNext();
//		try {
//			String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
//			if(splitStr[1].compareTo("00")==0)
//			{
//				btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(splitStr[0])+")");
//			}
//			else
//			{
//				btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(cursor2.getString(cursor2.getColumnIndex("Amount")))+")");
//			}
//
//		} catch (Exception ex) {
//			btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber("0")+")");
//		}
//	}
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

			if (ActivityCompat.checkSelfPermission(About.this,
					android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
				if(ActivityCompat.shouldShowRequestPermissionRationale(About.this, android.Manifest.permission.CALL_PHONE))
				{
					ActivityCompat.requestPermissions(About.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
				}
				else
				{
					ActivityCompat.requestPermissions(About.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
				}

			}
			db = dbh.getReadableDatabase();
			Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
			if (cursorPhone.getCount() > 0) {
				cursorPhone.moveToNext();
				dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("Tel")));
			}
			db.close();
		}
	});
	((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3)).getMapAsync(new OnMapReadyCallback() {
		@Override

		public void onMapReady(GoogleMap googleMap) {
			map = googleMap;
			point = new LatLng(36.30781906006661, 59.59622842793897);
			map.addMarker(new MarkerOptions().position(point).title("بسپارینا").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,17));
			map.getUiSettings().setZoomControlsEnabled(true);
		}
	});
	imgEmail.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			imgEmail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("plain/text");
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "info@besparin.ir" });
					intent.putExtra(Intent.EXTRA_SUBJECT, "");
					intent.putExtra(Intent.EXTRA_TEXT, "");
					startActivity(Intent.createChooser(intent, ""));
				}
			});
		}
	});
	imgInstagram.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			openWebPage("https://www.instagram.com/besparina");
		}
	});
	imgSkyp.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				//Intent sky = new Intent("android.intent.action.CALL_PRIVILEGED");
				//the above line tries to create an intent for which the skype app doesn't supply public api

				Intent sky = new Intent("android.intent.action.VIEW");
				sky.setData(Uri.parse("skype:" + "besparina.official"));
				startActivity(sky);
			} catch (ActivityNotFoundException e) {
				Log.e("SKYPE CALL", "Skype failed", e);
			}
		}
	});
	imgTelegram.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			openWebPage("https://telegram.me/besparina_co");
		}
	});
	imgWathsUp.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//openWebPage("09335678976");
			Intent sendIntent = new Intent("android.intent.action.MAIN");
			sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
			sendIntent.putExtra("jid", "09335678976" + "@s.whatsapp.net");
			startActivity(sendIntent);
		}
	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	LoadActivity(MainMenu.class, "karbarCode", karbarCode);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);

		this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		this.startActivity(intent);
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
	public void openWebPage(String url) {
		Uri webpage = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		}
	}
}
