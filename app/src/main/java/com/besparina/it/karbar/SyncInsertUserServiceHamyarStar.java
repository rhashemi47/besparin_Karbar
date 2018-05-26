package com.besparina.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class SyncInsertUserServiceHamyarStar {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String WsResponse;
	private String UserServiceCode;
	private String HamyarCode;
	private String Zaher;
	private String Barkhord;
	private String HozooreBeMoghe;
	private String Gheymat;
	private String Tahvil;
	private String Keyfiyat;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncInsertUserServiceHamyarStar(Activity activity,
										   String UserServiceCode,
										   String HamyarCode,
										   String Zaher,
										   String Barkhord,
										   String HozooreBeMoghe,
										   String Gheymat,
										   String Keyfiyat,
										   String Tahvil) {
		this.activity = activity;
		this.UserServiceCode = UserServiceCode;
		this.HamyarCode = HamyarCode;
		this.Zaher = Zaher;
		this.Barkhord = Barkhord;
		this.HozooreBeMoghe = HozooreBeMoghe;
		this.Gheymat = Gheymat;
		this.Tahvil = Tahvil;
		this.Keyfiyat = Keyfiyat;
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
		    this.dialog = new ProgressDialog(activity);
		    this.dialog.setCanceledOnTouchOutside(false);
		}

        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("InsertUserServiceHamyarStar");
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
	    PropertyInfo UserServiceCodePI = new PropertyInfo();
	    //Set Name
		UserServiceCodePI.setName("UserServiceCode");
	    //Set Value
		UserServiceCodePI.setValue(UserServiceCode);
	    //Set dataType
		UserServiceCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(UserServiceCodePI);
	    //*************************************************************
	    PropertyInfo HamyarCodePI = new PropertyInfo();
	    //Set Name
		HamyarCodePI.setName("HamyarCode");
	    //Set Value
		HamyarCodePI.setValue(HamyarCode);
	    //Set dataType
		HamyarCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(HamyarCodePI);
	    //*************************************************************
	    PropertyInfo ZaherPI = new PropertyInfo();
	    //Set Name
		ZaherPI.setName("Zaher");
	    //Set Value
		ZaherPI.setValue(Zaher);
	    //Set dataType
		ZaherPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(ZaherPI);
	    //*************************************************************
	    PropertyInfo BarkhordPI = new PropertyInfo();
	    //Set Name
		BarkhordPI.setName("Barkhord");
	    //Set Value
		BarkhordPI.setValue(Barkhord);
	    //Set dataType
		BarkhordPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(BarkhordPI);
	    //*************************************************************
	    PropertyInfo HozooreBeMoghePI = new PropertyInfo();
	    //Set Name
		HozooreBeMoghePI.setName("HozooreBeMoghe");
	    //Set Value
		HozooreBeMoghePI.setValue(HozooreBeMoghe);
	    //Set dataType
		HozooreBeMoghePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(HozooreBeMoghePI);
	    //*************************************************************
	    PropertyInfo GheymatPI = new PropertyInfo();
	    //Set Name
		GheymatPI.setName("Gheymat");
	    //Set Value
		GheymatPI.setValue(Gheymat);
	    //Set dataType
		GheymatPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(GheymatPI);
	    //*************************************************************
	    PropertyInfo TahvilPI = new PropertyInfo();
	    //Set Name
		TahvilPI.setName("Tahvil");
	    //Set Value
		TahvilPI.setValue(Tahvil);
	    //Set dataType
		TahvilPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(TahvilPI);
	    //*************************************************************
	    PropertyInfo KeyfiyatPI = new PropertyInfo();
	    //Set Name
		KeyfiyatPI.setName("Keyfiyat");
	    //Set Value
		KeyfiyatPI.setValue(Keyfiyat);
	    //Set dataType
		KeyfiyatPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(KeyfiyatPI);
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
		res=WsResponse.split("@@");
		db=dbh.getWritableDatabase();
		for(int i=0;i<res.length;i++){
			value=res[i].split("##");
			db.execSQL("");//todo
		}
		db.close();
    }
	
}
