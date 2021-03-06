package com.besparina.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class SyncGetFactorUsersHead {

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
	public SyncGetFactorUsersHead(Context activity,String karbarCode,
								  DatabaseHelper dbh,
								  SQLiteDatabase db) {
		this.activity = activity;
		this.karbarCode = karbarCode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		this.dbh = dbh;
		this.db = db;
		PublicVariable.theard_GetPerFactor=false;
//		dbh = new DatabaseHelper(this.activity.getApplicationContext());
//		try {
//
//			dbh.createDataBase();
//
//		} catch (IOException ioe) {
//			PublicVariable.theard_GetPerFactor=true;
//			throw new Error("Unable to create database");
//
//		}
//
//		try {
//
//			dbh.openDataBase();
//
//		} catch (SQLException sqle) {
//			PublicVariable.theard_GetPerFactor=true;
//			throw sqle;
//		}
	}

	public void AsyncExecute() {
		if (IC.isConnectingToInternet() == true) {
			try {
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			} catch (Exception e) {

				PublicVariable.theard_GetPerFactor=true;
				e.printStackTrace();
			}
		} else {
			PublicVariable.theard_GetPerFactor=true;
		}
	}

	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;

		public AsyncCallWS(Context activity) {
			this.activity = activity;
			this.dialog = new ProgressDialog(activity);
			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				CallWsMethod("GetFaktorUsersHead");
			} catch (Exception e) {
				PublicVariable.theard_GetPerFactor=true;
				result = e.getMessage().toString();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			PublicVariable.theard_GetPerFactor=true;
			if (result == null) {
				if (WsResponse.toString().compareTo("ER") == 0) {

				} else if (WsResponse.toString().compareTo("0") == 0) {
					PublicVariable.theard_GetPerFactor=true;

				} else if (WsResponse.toString().compareTo("-1") == 0) {
					PublicVariable.theard_GetPerFactor=true;

				} else {
					InsertDataFromWsToDb();
				}

			} else {

			}
			try {
				if (this.dialog.isShowing()) {
					this.dialog.dismiss();
				}
			} catch (Exception e) {
				PublicVariable.theard_GetPerFactor=true;
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

		//Set Name

		//Set Value

		//Set dataType

		//Add the property to request object

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
		for (int i = 0; i < res.length; i++) {
			value=res[i].split("##");
			boolean check=checkCode(value[0]);
			if(check) {
				try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
				db.execSQL("DELETE FROM BsFaktorUsersHead WHERE UserServiceCode='" + value[2] + "'");
				String query="INSERT INTO BsFaktorUsersHead (" +
						"Code," +
						"karbarCode," +
						"UserServiceCode," +
						"Type," +
						"FaktorDate," +
						"Description," +
						"Status," +
						"AcceptPreInvoiceByUsers," +
						"AcceptDatePreInvoide," +
						"AcceptInvoiceByUsers," +
						"AcceptDateInvoide," +
						"InsertDate) VALUES('" +
						value[0] +
						"','" + value[1] +
						"','" + value[2] +
						"','" + value[3] +
						"','" + value[4] +
						"','" + value[5] +
						"','" + value[6] +
						"','" + value[7] +
						"','" + value[8] +
						"','" + value[9] +
						"','" + value[10] +
						"','" + value[11] +
						"')";
				db.execSQL(query);
				try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}

			}
			else
			{
				try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
				String query="INSERT INTO BsFaktorUsersHead (" +
						"Code," +
						"karbarCode," +
						"UserServiceCode," +
						"Type," +
						"FaktorDate," +
						"Description," +
						"Status," +
						"AcceptPreInvoiceByUsers," +
						"AcceptDatePreInvoide," +
						"AcceptInvoiceByUsers," +
						"AcceptDateInvoide," +
						"InsertDate) VALUES('" +
						value[0] +
						"','" + value[1] +
						"','" + value[2] +
						"','" + value[3] +
						"','" + value[4] +
						"','" + value[5] +
						"','" + value[6] +
						"','" + value[7] +
						"','" + value[8] +
						"','" + value[9] +
						"','" + value[10] +
						"','" + value[11] +
						"')";
				db.execSQL(query);
				try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
			}
		}
		SyncGetFaktorUserDetailes syncGetFaktorUserDetailes = new SyncGetFaktorUserDetailes(activity, this.karbarCode);
		syncGetFaktorUserDetailes.AsyncExecute();
	}
	public boolean checkCode(String codeStr)
	{
		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
		String query = "SELECT * FROM BsFaktorUsersHead WHERE Code='"+codeStr+"'";
		Cursor cursor= db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			try {
				if (db.isOpen())
				{
					db.close();
				}}	catch (Exception ex){	}
			try {
				if (!cursor.isClosed())
				{
					cursor.close();
				}
			}
			catch (Exception ex){
				String error=ex.toString();
			}
			return true;
		}
		else
		{
			try {	if (db.isOpen()) {	db.close();if(!cursor.isClosed())
				cursor.close();	}}	catch (Exception ex){	}
			return false;
		}
	}
}
