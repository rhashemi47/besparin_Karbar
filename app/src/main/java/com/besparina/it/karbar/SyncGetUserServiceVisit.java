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

public class SyncGetUserServiceVisit {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Context activity;
	private String WsResponse;
	private String pUserCode;
	private String LastServiceVisitCode;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncGetUserServiceVisit(Context activity, String pUserCode, String LastServiceVisitCode) {
		this.activity = activity;

		this.pUserCode=pUserCode;
		this.LastServiceVisitCode=LastServiceVisitCode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.theard_GetServiceVisit=false;
		dbh=new DatabaseHelper(this.activity.getApplicationContext());
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
	
	public void AsyncExecute()
	{
		if(IC.isConnectingToInternet()==true)
		{
			try
			{
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			}	
			 catch (Exception e) {

	            e.printStackTrace();
			 }
		}
		else
		{
			//akeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
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
        	try
        	{
        		CallWsMethod("GetUserServiceVisit");
        	}
	    	catch (Exception e) {
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
				PublicVariable.theard_GetServiceVisit=true;
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	//akeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//akeText(this.activity.getApplicationContext(), "سرویس جدیدی اعلام نشده", Toast.LENGTH_LONG).show();
	            }
				else if(WsResponse.toString().compareTo("2") == 0)
				{
					//akeText(this.activity.getApplicationContext(), "کاربر شناسایی نشد!", Toast.LENGTH_LONG).show();
				}
	            else
	            {
	            	InsertDataFromWsToDb(WsResponse);
	            }
        	}
        	else
        	{
        		//akeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
        	}
            try
            {
            	if (this.dialog.isShowing()) {
            		this.dialog.dismiss();
            	}
            }
            catch (Exception e) {}
        }
 
        @Override
        protected void onPreExecute() {
        	if(CuShowDialog)
        	{
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
		PropertyInfo pUserCodePI = new PropertyInfo();
		//Set Name
		pUserCodePI.setName("pUserCode");
		//Set Value
		pUserCodePI.setValue(this.pUserCode);
		//Set dataType
		pUserCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(pUserCodePI);
		//*****************************************************
		PropertyInfo LastServiceVisitCodePI = new PropertyInfo();
		//Set Name
		LastServiceVisitCodePI.setName("LastServiceVisitCode");
		//Set Value
		LastServiceVisitCodePI.setValue(this.LastServiceVisitCode);
		//Set dataType
		LastServiceVisitCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(LastServiceVisitCodePI);
		//*****************************************************
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
	        androidHttpTransport.call("http://tempuri.org/"+METHOD_NAME, envelope);
	        //Get the response
	        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	        //Assign it to FinalResultForCheck static variable
	        WsResponse = response.toString();	
	        if(WsResponse == null) WsResponse="ER";
	    } catch (Exception e) {
	    	WsResponse = "ER";
	    	e.printStackTrace();
	    }
	}
	public void InsertDataFromWsToDb(String AllRecord)
    {
		String[] res;
		String[] value;
		boolean isFirst=IsFristInsert();
		res = WsResponse.split("@@");
		db = dbh.getWritableDatabase();
		for (int i = 0; i < res.length; i++) {
			value = res[i].split("##");
			try
			{
				//db.execSQL("DELETE FROM visit WHERE Code='" + value[0] + "'");
				String query = "INSERT INTO visit (Code,UserServiceCode,VisitDate,VisitTime,HamyarCode,InsertDate)  VALUES('" +
						value[0] + "','" +
						value[1] + "','" +
						value[2] + "','" +
						value[3] + "','" +
						value[4] + "','" +
						value[5] +
						"')";
				db.execSQL(query);
				if (!isFirst) {
					runNotification("بسپارینا",  i, value[1], Service_Request_Saved.class);
				}
			} catch (Exception ex) {

			}
		}

		db.close();
    }
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(activity,Cls);
		intent.putExtra(VariableName, VariableValue);

		activity.startActivity(intent);
	}
	public void runNotification(String title,int id,String OrderCode,Class<?> Cls)
	{

		NotificationClass notifi=new NotificationClass();
		notifi.Notificationm(this.activity,title,"برای درخواست شماره: "+ OrderCode+"بازدید ثبت شده است.",OrderCode,id,Cls);
	}
	public boolean IsFristInsert()
	{
		db=dbh.getReadableDatabase();
		String query = "SELECT * FROM visit";
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
