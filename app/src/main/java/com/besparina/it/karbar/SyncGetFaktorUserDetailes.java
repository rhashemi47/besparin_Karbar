package com.besparina.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class SyncGetFaktorUserDetailes {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
	InternetConnection IC;
	private Context activity;

	private String karbarCode;
	private String WsResponse;
	private boolean CuShowDialog = false;

	//Contractor
	public SyncGetFaktorUserDetailes(Context activity,String karbarCode) {
		this.activity = activity;
		this.karbarCode = karbarCode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();

		dbh = new DatabaseHelper(this.activity.getApplicationContext());
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
	}

	public void AsyncExecute() {
		if (IC.isConnectingToInternet() == true) {
			try {
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			} catch (Exception e) {

				e.printStackTrace();
			}
		} else {
			//Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}

	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;

		public AsyncCallWS(Context activity) {
			this.activity = activity;
			this.dialog = new ProgressDialog(activity);		    this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				CallWsMethod("GetFaktorUserDetailes");
			} catch (Exception e) {
				result = e.getMessage().toString();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				if (WsResponse.toString().compareTo("ER") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
				} else if (WsResponse.toString().compareTo("0") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "خطایی رخداده است", Toast.LENGTH_LONG).show();
					//LoadActivity(MainActivity.class,"karbarCode",karbarCode,"updateflag","1");
				} else if (WsResponse.toString().compareTo("-1") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "کاربر شناسایی نشد!", Toast.LENGTH_LONG).show();

				} else {
					InsertDataFromWsToDb();
				}

			} else {
				//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
			}
			try {
				if (this.dialog.isShowing()) {
					this.dialog.dismiss();
				}
			} catch (Exception e) {
			}
		}

		@Override
		protected void onPreExecute() {
			if (CuShowDialog) {
				this.dialog.setMessage("در حال پردازش");
				this.dialog.show();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

	}


	public void CallWsMethod(String METHOD_NAME) {
		//Create request
		SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);

		//*****************************************************
		PropertyInfo karbarCodePI = new PropertyInfo();
		//Set Name
		karbarCodePI.setName("pUserServiceCode");
		//Set Value
		karbarCodePI.setValue(this.karbarCode);
		//Set dataType
		karbarCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(karbarCodePI);
		//Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		//Set output SOAP object
		envelope.setOutputSoapObject(request);
		//Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(PV.URL);
		try {
			//Invoke web service
			androidHttpTransport.call("http://tempuri.org/" + METHOD_NAME, envelope);
			//Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			//Assign it to FinalResultForCheck static variable
			WsResponse = response.toString();
			if (WsResponse == null) WsResponse = "ER";
		} catch (Exception e) {
			WsResponse = "ER";
			e.printStackTrace();
		}
	}


	public void InsertDataFromWsToDb() {
		String[] res;
		String[] value;
		res = WsResponse.split("@@");
		db = dbh.getWritableDatabase();
		for (int i = 0; i < res.length; i++) {
			value=res[i].split("##");
			db.execSQL("DELETE FROM BsFaktorUserDetailes WHERE Code='" + value[0] + "'");
			db.execSQL("INSERT INTO BsFaktorUserDetailes (" +
					"Code," +
					"FaktorUsersHeadCode," +
					"Title," +
					"Unit," +
					"PricePerUnit," +
					"Amount," +
					"TotalPrice," +
					"InsertDate) VALUES('"+
					value[0]+
					"','"+value[1]+
					"','"+value[2]+
					"','"+value[3]+
					"','"+value[4]+
					"','"+value[5]+
					"','"+value[6]+
					"','"+value[7]+
					"')");
		}
		db.close();
	}
}
