package com.besparina.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import java.sql.Time;
import java.sql.Timestamp;

public class SyncGetUserServices {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
	InternetConnection IC;
	private Context activity;
	private String pUserCode;
	private String WsResponse;
	private String LastUserServiceCode;
	private boolean CuShowDialog = false;

	//Contractor
	public SyncGetUserServices(Context activity, String pUserCode, String LastUserServiceCode) {
		this.activity = activity;
		this.pUserCode = pUserCode;
		this.LastUserServiceCode = LastUserServiceCode;

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
				//Toast.makeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
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
				CallWsMethod("GetUserService");
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
				}
				else if (WsResponse.toString().compareTo("0") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
				}
				else if (WsResponse.toString().compareTo("2") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "کاربر شناسایی نشد", Toast.LENGTH_LONG).show();
				}
				else
				{
					InsertDataFromWsToDb(WsResponse);
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
		PropertyInfo pUserCodePI = new PropertyInfo();
		//Set Name
		pUserCodePI.setName("pUserCode");
		//Set Value
		pUserCodePI.setValue(pUserCode);
		//Set dataType
		pUserCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(pUserCodePI);
		//****************************************************************
		PropertyInfo LastUserServiceCodePI = new PropertyInfo();
		//Set Name
		LastUserServiceCodePI.setName("LastUserServiceCode");
		//Set Value
		LastUserServiceCodePI.setValue(LastUserServiceCode);
		//Set dataType
		LastUserServiceCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(LastUserServiceCodePI);
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


	public void InsertDataFromWsToDb(String AllRecord) {
		String[] res;
		String[] value;
		String[] DateStart, DateEnd;
		String[] TimeStart, TimeEnd;
		boolean isFirst=IsFristInsert();
		res = WsResponse.split("@@");
		db = dbh.getWritableDatabase();
		for (int i = 0; i < res.length; i++) {
			value = res[i].split("##");
			try {
				boolean check = checkStatus(value[0], value[32]);
				if (!check) {
					db.execSQL("DELETE FROM OrdersService WHERE Code='" + value[0] + "'");
					DateStart = value[7].split("/");
					DateEnd = value[8].split("/");
					TimeStart = value[19].split(":");
					TimeEnd = value[20].split(":");
					String query = "INSERT INTO OrdersService (" +
							"Code," +
							"pUserCode," +
							"ServiceDetaileCode," +
							"MaleCount," +
							"FemaleCount," +
							"HamyarCount," +
							"StartYear," +
							"StartMonth," +
							"StartDay," +
							"StartHour," +
							"StartMinute," +
							"EndYear," +
							"EndMonth," +
							"EndDay," +
							"EndHour," +
							"EndMinute," +
							"AddressCode," +
							"Description," +
							"IsEmergency," +
							"PeriodicServices," +
							"EducationGrade," +
							"FieldOfStudy," +
							"StudentGender," +
							"TeacherGender," +
							"EducationTitle," +
							"ArtField," +
							"CarWashType," +
							"CarType," +
							"Language," +
							"Status) VALUES('" +
							value[0] + "','" +
							value[1] + "','" +
							value[4] + "','" +
							value[5] + "','" +
							value[6] + "','" +
							value[21] + "','" +
							DateStart[0] + "','" +
							DateStart[1] + "','" +
							DateStart[2] + "','" +
							TimeStart[0] + "','" +
							TimeStart[1] + "','" +
							DateEnd[0] + "','" +
							DateEnd[1] + "','" +
							DateEnd[2] + "','" +
							TimeEnd[0] + "','" +
							TimeEnd[1] + "','" +
							value[9] + "','" +
							value[15] + "','" +
							value[16] + "','" +
							value[22] + "','" +
							value[23] + "','" +
							value[24] + "','" +
							value[25] + "','" +
							value[26] + "','" +
							value[27] + "','" +
							value[28] + "','" +
							value[29] + "','" +
							value[30] + "','" +
							value[31] + "','" +
							value[32] + "')";
					db.execSQL(query);
					db.close();
					SyncGetUserServiceHamyar syncGetUserServiceHamyar=new SyncGetUserServiceHamyar(activity.getApplicationContext(),value[0]);
					syncGetUserServiceHamyar.AsyncExecute();
					if (!isFirst) {
						runNotification("بسپارینا", value[4], i, value[0], Service_Request_Saved.class, value[32]);
					}

				}
			}
			catch (Exception ex)
			{

			}
		}
	}

	public boolean checkStatus(String codeStr,String statusStr)
	{
		db=dbh.getReadableDatabase();
		String query = "SELECT * FROM OrdersService WHERE Code='"+codeStr+"' AND Status='"+statusStr+"'";
		Cursor cursor= db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public String getDetailname(String detailCode)
	{
		db = dbh.getReadableDatabase();
		String query = "SELECT * FROM Servicesdetails  WHERE code=" + detailCode;
		Cursor coursors = db.rawQuery(query, null);
		if (coursors.getCount() > 0)
		{
			coursors.moveToNext();
			return coursors.getString(coursors.getColumnIndex("name"));
		}
		else
		{
			return "";
		}
	}
	public void runNotification(String title,String detail,int id,String OrderCode,Class<?> Cls,String status)
	{
		String StrStatus="";
		switch (status)
		{
			case "0":
				StrStatus="آزاد شد";
				break;
			case "1":
				StrStatus="در نوبت انجام قرار گرفت";
				break;
			case "2":
				StrStatus="در حال انجام است";
				break;
			case "3":
				StrStatus="لغو شد";
				break;
			case "4":
				StrStatus="اتمام و تسویه شده است";
				break;
			case "5":
				StrStatus="اتمام و تسویه نشده است";
				break;
			case "6":
				StrStatus="اعلام شکایت شده است";
				break;
			case "7":
				StrStatus="درحال پیگیری شکایت و یا رفع خسارت می باشد";
				break;
			case "8":
				StrStatus=" توسط همیار رفع عیب و خسارت شده است";
				break;
			case "9":
				StrStatus="پرداخت خسارت";
				break;
			case "10":
				StrStatus="پرداخت جریمه";
				break;
			case "11":
				StrStatus="تسویه حساب با همیار";
				break;
			case "12":
				StrStatus="متوقف و تسویه شده است";
				break;
			case "13":
				StrStatus="متوقف و تسویه نشده است";
				break;
		}
		NotificationClass notifi=new NotificationClass();
		notifi.Notificationm(this.activity,title,getDetailname(detail)+" "+ StrStatus,OrderCode,id,Cls);
	}
	public boolean IsFristInsert()
	{
		db=dbh.getReadableDatabase();
		String query = "SELECT * FROM OrdersService";
		Cursor cursor= db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}

