package com.besparina.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class SyncAcceptPreInvoiceByUsers {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;

	private String Accpept;
	private String WsResponse;
	private String pAcceptPreInvoiceByUsersCode;
	private String OrderCode;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncAcceptPreInvoiceByUsers(Activity activity, String Accpept, String pAcceptPreInvoiceByUsersCode, String OrderCode) {
		this.activity = activity;

		this.pAcceptPreInvoiceByUsersCode=pAcceptPreInvoiceByUsersCode;
		this.Accpept=Accpept;
		this.OrderCode=OrderCode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		
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
			Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}
	
	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Activity activity;
		
		public AsyncCallWS(Activity activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("AcceptPreInvoiceByUsers");
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
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "سرویس جدیدی اعلام نشده", Toast.LENGTH_LONG).show();
	            }
				else if(WsResponse.toString().compareTo("2") == 0)
				{
					Toast.makeText(this.activity.getApplicationContext(), "کاربر شناسایی نشد!", Toast.LENGTH_LONG).show();
				}
	            else
	            {
	            	InsertDataFromWsToDb(WsResponse);
	            }
        	}
        	else
        	{
        		//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
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
		PropertyInfo AccpeptPI = new PropertyInfo();
		//Set Name
		AccpeptPI.setName("Accpept");
		//Set Value
		AccpeptPI.setValue(this.Accpept);
		//Set dataType
		AccpeptPI.setType(String.class);
		//Add the property to request object
		request.addProperty(AccpeptPI);
		//*****************************************************
		PropertyInfo pAcceptPreInvoiceByUsersCodePI = new PropertyInfo();
		//Set Name
		pAcceptPreInvoiceByUsersCodePI.setName("pAcceptPreInvoiceByUsersCode");
		//Set Value
		pAcceptPreInvoiceByUsersCodePI.setValue(this.pAcceptPreInvoiceByUsersCode);
		//Set dataType
		pAcceptPreInvoiceByUsersCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(pAcceptPreInvoiceByUsersCodePI);
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
		PersianCalendar cal=new PersianCalendar();
		String DateNow=cal.getPersianLongDate();
		db=dbh.getWritableDatabase();
			String query="UPDATE BsFaktorUsersHead SET AcceptPreInvoiceByUsers='"+this.Accpept+
					"', AcceptDatePreInvoide='"+DateNow+"' WHERE Code='"+this.pAcceptPreInvoiceByUsersCode+"'" ;
			db.execSQL(query);
			if(this.Accpept.compareTo("1")==0)
			{
				Toast.makeText(this.activity.getApplicationContext(), "پیش فاکتور تایید شد.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(this.activity.getApplicationContext(), "پیش فاکتور رد شد.", Toast.LENGTH_SHORT).show();
			}
		LoadActivity(Service_Request_Saved.class,"OrderCode",OrderCode);
		db.close();
    }
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(activity.getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		activity.startActivity(intent);
	}
}
