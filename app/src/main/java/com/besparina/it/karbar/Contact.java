package com.besparina.it.karbar;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Contact extends Activity {
	private String karbarCode;
	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	final private int REQUEST_CODE_ASK_PERMISSIONS_SendSMS = 124;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private EditText etSendMessage;
	private Button btnOrder;
	private Button btnAcceptOrder;
	private Button btncredite;
	private Button btnServiceEmergency;
	private Button btnCallSupporter;
	private Button btnSendMessage;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		btnOrder=(Button)findViewById(R.id.btnOrderBottom);
		btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
		btncredite=(Button)findViewById(R.id.btncrediteBottom);
		btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);
		btnCallSupporter=(Button)findViewById(R.id.btnCallSupporter);
		btnSendMessage=(Button)findViewById(R.id.btnSendMessage);
		etSendMessage=(EditText) findViewById(R.id.etSendMessage);
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
		imgview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity(MainMenu.class,"","");
			}
		});

		String Query="UPDATE UpdateApp SET Status='1'";
		db=dbh.getWritableDatabase();
		db.execSQL(Query);
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

				if (ActivityCompat.checkSelfPermission(Contact.this,
						android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					if(ActivityCompat.shouldShowRequestPermissionRationale(Contact.this, android.Manifest.permission.CALL_PHONE))
					{
						ActivityCompat.requestPermissions(Contact.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
					}
					else
					{
						ActivityCompat.requestPermissions(Contact.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
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
				db = dbh.getReadableDatabase();
				Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
				if (cursorPhone.getCount() > 0) {
					cursorPhone.moveToNext();
					dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
				}
				db.close();
			}
		});
		btnSendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				db = dbh.getReadableDatabase();
				Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
				if (cursorPhone.getCount() > 0) {
					cursorPhone.moveToNext();
					String MessageStr="کد کاربر: "+karbarCode+"\n"+etSendMessage.getText().toString();
					SendMessage(MessageStr, cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
//					SendMessage(MessageStr,"09155210697");
				}
				db.close();
			}
		});
	}



	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
	    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
	    	Contact.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
	    }

	    return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

			Contact.this.startActivity(intent);
		}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);

		this.startActivity(intent);
	}
	public void dialContactPhone(String phoneNumber) {
		//startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
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
					Toast.makeText(Contact.this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
							.show();
				}
				break;
			case REQUEST_CODE_ASK_PERMISSIONS_SendSMS:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					db = dbh.getReadableDatabase();
					Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
					if (cursorPhone.getCount() > 0) {
						cursorPhone.moveToNext();
						String MessageStr="کد کاربر: "+karbarCode+"\n"+etSendMessage.getText().toString();
						SendMessage(MessageStr,cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
//						SendMessage(MessageStr,"09155210697");
					}
					db.close();
				}
				else
				{
					Toast.makeText(Contact.this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
							.show();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	public void SendMessage(String message ,String phoneNumber) {
		SmsManager smsManager = SmsManager.getDefault();
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		SmsManager sms = SmsManager.getDefault();
		ArrayList<String> parts = sms.divideMessage(message);
		int messageCount = parts.size();

		ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
		ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
		for (int j = 0; j < messageCount; j++) {
			sentIntents.add(sentPI);
			deliveryIntents.add(deliveredPI);
		}

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
					case Activity.RESULT_OK:

						Toast.makeText(getBaseContext(), "پیام ارسال شد",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(getBaseContext(), "ارسال پیام با خطا مواجه شد",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(getBaseContext(), "سرویس ارسال پیامک در دسترس نیست",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(getBaseContext(), "خظایی رخ داده است",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(getBaseContext(), "آنتن ضعیف است",
								Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {

					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "پیام تحویل شد",
								Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(getBaseContext(), "پیام تحویل نشد",
								Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}, new IntentFilter(DELIVERED));
		smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
           /* sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents); */
	}
}
