package com.besparina.it.karbar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.ToLongBiFunction;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowFactor extends AppCompatActivity {
	private String karbarCode;
	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	private String OrderCode;
	private Button btnNoFactor;
	private Button btnYesFactor;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private TextView Question;
	private TextView tvTotal;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private Button btnServiceEmergency;
	private int TypeFactor=0;
	private String headFactor="0";
	private ListView lstFactor;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	private TextView tvDescription;
	private TextView tvOrderDate;
	private TextView tvOrderCode;
	private Button btnCatchNaghd;
	private Button btnCatchSharj;

	private double Total=0;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.show_factor);
	Question=(TextView)findViewById(R.id.Question);
	tvTotal=(TextView)findViewById(R.id.tvTotal);
	tvDescription=(TextView)findViewById(R.id.tvDescription);
		tvOrderDate=(TextView)findViewById(R.id.tvOrderDate);
		tvOrderCode=(TextView)findViewById(R.id.tvOrderCode);
	btnNoFactor=(Button)findViewById(R.id.btnNoFactor);
	btnYesFactor=(Button)findViewById(R.id.btnYesFactor);
	btnOrder=(Button)findViewById(R.id.btnOrderBottom);
	btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
	btncredite=(Button)findViewById(R.id.btncrediteBottom);
	btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);
		btnCatchNaghd=(Button)findViewById(R.id.btnCatchNaghd);
		btnCatchSharj=(Button)findViewById(R.id.btnCatchSharj);
	lstFactor=(ListView) findViewById(R.id.lstFactor);
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

		ImageView imgview = (ImageView)findViewById(R.id.BesparinaLogo);
		imgview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity(MainMenu.class,"","");
			}
		});

	//**************************************************************************************
	prepareData();
	//***************************************************************************************
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

				if (ActivityCompat.checkSelfPermission(ShowFactor.this,
						android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					if(ActivityCompat.shouldShowRequestPermissionRationale(ShowFactor.this, android.Manifest.permission.CALL_PHONE))
					{
						ActivityCompat.requestPermissions(ShowFactor.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
					}
					else
					{
						ActivityCompat.requestPermissions(ShowFactor.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
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
	//***************************************************************************************
		btnNoFactor.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(TypeFactor==1) {
				SyncAcceptPreInvoiceByUsers syncAcceptPreInvoiceByUsers = new SyncAcceptPreInvoiceByUsers(ShowFactor.this, "0", headFactor,OrderCode);
				syncAcceptPreInvoiceByUsers.AsyncExecute();
			}
			else {
				SyncAcceptInvoiceByUsers syncAcceptInvoiceByUsers = new SyncAcceptInvoiceByUsers(ShowFactor.this, "0", headFactor,OrderCode);
				syncAcceptInvoiceByUsers.AsyncExecute();
			}
		}
	});
	//**************************************************************************
		btnYesFactor.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if(TypeFactor==1) {
				SyncAcceptPreInvoiceByUsers syncAcceptPreInvoiceByUsers = new SyncAcceptPreInvoiceByUsers(ShowFactor.this, "1", headFactor,OrderCode);
				syncAcceptPreInvoiceByUsers.AsyncExecute();
			}
			else {
				SyncAcceptInvoiceByUsers syncAcceptInvoiceByUsers = new SyncAcceptInvoiceByUsers(ShowFactor.this, "1", headFactor,OrderCode);
				syncAcceptInvoiceByUsers.AsyncExecute();
			}
		}
	});
	//**************************************************************************
		btnCatchNaghd.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			SyncServicePayment syncServicePayment=new SyncServicePayment(ShowFactor.this,karbarCode,OrderCode,String.valueOf(Total));
			syncServicePayment.AsyncExecute();
		}
	});
	//**************************************************************************
		btnCatchSharj.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			SyncServicePayment syncServicePayment=new SyncServicePayment(ShowFactor.this,karbarCode,OrderCode,String.valueOf(Total));
			syncServicePayment.AsyncExecute();
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
		ShowFactor.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		this.startActivity(intent);
	}
	public void prepareData()
	{
//
//		String ContentStr="";
		Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");//set font for page
		db = dbh.getReadableDatabase();
		String query="SELECT * FROM BsFaktorUsersHead WHERE Status='1' AND UserServiceCode="+OrderCode +" ORDER BY CAST(Code AS INTEGER) DESC";
		Cursor cursor = db.rawQuery(query,null);
		if(cursor.getCount()>0) {
			cursor.moveToNext();
			headFactor=cursor.getString(cursor.getColumnIndex("Code"));
//			ContentStr += "شماره درخواست: " + OrderCode + "\n";
			if (cursor.getString(cursor.getColumnIndex("Type")).compareTo("1") == 0) {
//				ContentStr += "وضعیت: "+"پیش فاکتور" + "\n";
				TypeFactor=1;
				Question.setText("آیا پیش فاکتور مورد تایید است؟");
			} else {
//				ContentStr += "وضعیت: "+"فاکتور نهایی" + "\n";
				TypeFactor=0;
				Question.setText("آیا فاکتور مورد تایید است؟");
			}
			if(cursor.getString(cursor.getColumnIndex("AcceptInvoiceByUsers")).compareTo("-1")!=0)
			{
				btnCatchSharj.setVisibility(View.VISIBLE);
				btnCatchNaghd.setVisibility(View.VISIBLE);
				btnYesFactor.setVisibility(View.GONE);
			}
			tvOrderDate.setText(cursor.getString(cursor.getColumnIndex("FaktorDate")));
			tvOrderCode.setText(OrderCode);
			String query2="SELECT * FROM BsFaktorUserDetailes WHERE FaktorUsersHeadCode='"+headFactor+"'";
			Cursor cursor2 = db.rawQuery(query2,null);
			DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###");
			double Amount,PricePerUnit,TotalPrice;
//			View[] view = new View[cursor2.getCount()];
			for (int i = 0; i < cursor2.getCount(); i++) {
				cursor2.moveToNext();
				try
				{
					Amount = df.parse(cursor2.getString(cursor2.getColumnIndex("Amount")).toString().replace(".00","")).longValue();
					PricePerUnit = df.parse(cursor2.getString(cursor2.getColumnIndex("PricePerUnit")).toString().replace(".00","")).longValue();
					TotalPrice = df.parse(cursor2.getString(cursor2.getColumnIndex("TotalPrice")).toString().replace(".00","")).longValue();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("services",cursor2.getString(cursor2.getColumnIndex("Title")));
					map.put("ValueOrUnit",df.format(Amount)+"\\"+cursor2.getString(cursor2.getColumnIndex("Unit"))+"\\"+df.format(PricePerUnit));
					map.put("Sum",df.format(TotalPrice));
					valuse.add(map);
				}
				catch (Exception ex)
				{

				}
				Total+=Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("TotalPrice")));
			}
			AdapterFactor adapterFactor=new AdapterFactor(ShowFactor.this,valuse,karbarCode);
			lstFactor.setAdapter(adapterFactor);

			tvTotal.setText((PersianDigitConverter.PerisanNumber(df.format(Total))));
			tvDescription.setText(cursor.getString(cursor.getColumnIndex("Description")));
			db.close();
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
							dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("Tel")));
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
}
