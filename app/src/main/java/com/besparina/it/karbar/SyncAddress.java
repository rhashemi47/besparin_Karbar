package com.besparina.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class SyncAddress {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String pUserCode;
	private String Name;
	private String State;
	private String City;
	private String AddressText;
	private String Email;
	private String Lat;
	private String Lng;
	private String IsDefault;
	private String WsResponse;
	//private String acceptcode;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncAddress(Activity activity, String pUserCode,
					   String IsDefault,
					   String Name,
					   String State,
					   String City,
					   String AddressText,
					   String Email,
					   String Lat,
					   String Lng) {
		this.activity = activity;
		this.pUserCode = pUserCode;
		this.IsDefault = IsDefault;
		this.Name = Name;
		this.State = State;
		this.City = City;
		this.AddressText = AddressText;
		this.Email = Email;
		this.Lat = Lat;
		this.Lng = Lng;
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
				//Toast.makeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
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
        		CallWsMethod("InsertUserAddress");
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
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
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
		PropertyInfo pUserCodePI = new PropertyInfo();
		//Set Name
		pUserCodePI.setName("pUserCode");
		//Set Value
		pUserCodePI.setValue(pUserCode);
		//Set dataType
		pUserCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(pUserCodePI);
		//***************************************************
		PropertyInfo IsDefaultPI = new PropertyInfo();
		//Set Name
		IsDefaultPI.setName("IsDefault");
		//Set Value
		IsDefaultPI.setValue(IsDefault);
		//Set dataType
		IsDefaultPI.setType(String.class);
		//Add the property to request object
		request.addProperty(IsDefaultPI);
		//***************************************************
	    PropertyInfo NamePI = new PropertyInfo();
	    //Set Name
		NamePI.setName("Name");
	    //Set Value
		NamePI.setValue(Name);
	    //Set dataType
		NamePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(NamePI);
	    //***************************************************
		PropertyInfo StatePI = new PropertyInfo();
		//Set Name
		StatePI.setName("State");
		//Set Value
		StatePI.setValue(State);
		//Set dataType
		StatePI.setType(String.class);
		//Add the property to request object
		request.addProperty(StatePI);
	    //***************************************************
		PropertyInfo CityPI = new PropertyInfo();
		//Set Name
		CityPI.setName("City");
		//Set Value
		CityPI.setValue(City);
		//Set dataType
		CityPI.setType(String.class);
		//Add the property to request object
		request.addProperty(CityPI);
	    //***************************************************
		PropertyInfo AddressTextPI = new PropertyInfo();
		//Set Name
		AddressTextPI.setName("AddressText");
		//Set Value
		AddressTextPI.setValue(AddressText);
		//Set dataType
		AddressTextPI.setType(String.class);
		//Add the property to request object
		request.addProperty(AddressTextPI);
	    //***************************************************
		PropertyInfo EmailPI = new PropertyInfo();
		//Set Name
		EmailPI.setName("Email");
		//Set Value
		EmailPI.setValue(Email);
		//Set dataType
		EmailPI.setType(String.class);
		//Add the property to request object
		request.addProperty(EmailPI);
	    //***************************************************
		PropertyInfo LatPI = new PropertyInfo();
		//Set Name
		LatPI.setName("Lat");
		//Set Value
		LatPI.setValue(Lat);
		//Set dataType
		LatPI.setType(String.class);
		//Add the property to request object
		request.addProperty(LatPI);
	    //***************************************************
		PropertyInfo LngPI = new PropertyInfo();
		//Set Name
		LngPI.setName("Lng");
		//Set Value
		LngPI.setValue(Lng);
		//Set dataType
		LngPI.setType(String.class);
		//Add the property to request object
		request.addProperty(LngPI);
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
		Toast.makeText(activity, "ثبت شد", Toast.LENGTH_SHORT).show();
		SyncGetUserAddress syncGetUserAddress=new SyncGetUserAddress(activity,pUserCode,"0");
		syncGetUserAddress.AsyncExecute();
    }
	
}
