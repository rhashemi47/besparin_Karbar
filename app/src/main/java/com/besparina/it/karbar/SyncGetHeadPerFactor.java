package com.besparina.it.karbar;

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

public class SyncGetHeadPerFactor {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
	InternetConnection IC;
	private Context activity;
	private String pUserCode;
	private String WsResponse;
	private String pUserServiceCode;
	private boolean CuShowDialog = false;

	//Contractor
	public SyncGetHeadPerFactor(Context activity, String pUserServiceCode) {
		this.activity = activity;
		this.pUserCode = pUserCode;
		this.pUserServiceCode = pUserServiceCode;

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
				CallWsMethod("GetFaktorUsersHead");
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
		PropertyInfo pUserServiceCodePI = new PropertyInfo();
		//Set Name
		pUserServiceCodePI.setName("pUserServiceCode");
		//Set Value
		pUserServiceCodePI.setValue(pUserServiceCode);
		//Set dataType
		pUserServiceCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(pUserServiceCodePI);

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
//		boolean isFirst=IsFristInsert();//todo باید چک شود که آیا پیش فاکتور جدید است و یا تغییر کرد!!!!!!!!!!!
		res = WsResponse.split("@@");
		db = dbh.getWritableDatabase();
		db.execSQL("DELETE FROM BsFaktorUsersHead");
		for (int i = 0; i < res.length; i++)
		{
			value = res[i].split("##");
				String query = "INSERT INTO BsFaktorUsersHead (" +
						"Code," +
						"HamyarCode," +
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
						value[0] + "','" +
						value[1] + "','" +
						value[2] + "','" +
						value[3] + "','" +
						value[4] + "','" +
						value[5] + "','" +
						value[6] + "','" +
						value[7] + "','" +
						value[8] + "','" +
						value[9] + "','" +
						value[10] + "','" +
						value[11] + "')";
				db.execSQL(query);
			db.close();
//				if(!isFirst) {
					runNotification("بسپارینا", i, value[2], Service_Request_Saved.class,value[6]);
//				}
		}
	}

//	public boolean checkStatus(String codeStr,String statusStr)
//	{
//		db=dbh.getReadableDatabase();
//		String query = "SELECT * FROM OrdersService WHERE Code='"+codeStr+"' AND Status='"+statusStr+"'";
//		Cursor cursor= db.rawQuery(query,null);
//		if(cursor.getCount()>0)
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
	public String getDetailname(String OrderCode)
	{

		db = dbh.getReadableDatabase();
		String query="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
				"LEFT JOIN " +
				"Servicesdetails ON " +
				"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE OrdersService.Code="+OrderCode;
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
	public void runNotification(String title,int id,String OrderCode,Class<?> Cls,String status)
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
		notifi.Notificationm(this.activity,title,getDetailname(OrderCode)+" "+ StrStatus,OrderCode,id,Cls);
	}
//	public boolean IsFristInsert()
//	{
//		db=dbh.getReadableDatabase();
//		String query = "SELECT * FROM BsFaktorUsersHead";
//		Cursor cursor= db.rawQuery(query,null);
//		if(cursor.getCount()>0)
//		{
//			return false;
//		}
//		else
//		{
//			return true;
//		}
//	}
}

