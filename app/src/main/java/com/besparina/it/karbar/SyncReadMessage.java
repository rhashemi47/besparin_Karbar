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

public class SyncReadMessage {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Context activity;
	private String karbarCode;

	private String WsResponse;
	private String Year;
	private String Month;
	private String Day;
	private String messagecode;
	private boolean CuShowDialog=false;
	private String[] res;
	//Contractor
	public SyncReadMessage(Context activity, String karbarCode,String messagecode, String Year, String Month, String Day) {
		this.activity = activity;

		this.karbarCode = karbarCode;
		this.Year=Year;
		this.Month=Month;
		this.Day=Day;
		this.messagecode=messagecode;
		PublicVariable.theard_ReadMessage=false;
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
				//akeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
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
        		CallWsMethod("UpdateUserMessageIsRead");
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
				PublicVariable.theard_ReadMessage=true;
        		res=WsResponse.split("##");
	            if(res[0].toString().compareTo("ER") == 0)
	            {
	            	//akeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(res[0].toString().compareTo("0") == 0)
	            {
	            	//akeText(this.activity.getApplicationContext(), "خطایی رخ داده است!", Toast.LENGTH_LONG).show();
	            }
	            else if(res[0].toString().compareTo("1") == 0)
	            {
					InsertDataFromWsToDb(res);
	            }
	            else if(res[0].toString().compareTo("2") == 0)
	            {
	            	//akeText(this.activity.getApplicationContext(), "کاربر شناسایی نشد!", Toast.LENGTH_LONG).show();
	            }
	         
	            else
	            {

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
	
	String LastNewsId;
	public void LoadMaxNewId()
	{
		db = dbh.getReadableDatabase();
		Cursor cursors = db.rawQuery("select IFNULL(max(id),0)MID from news", null);
		if(cursors.getCount() > 0)
		{
			cursors.moveToNext();
			LastNewsId = cursors.getString(cursors.getColumnIndex("MID"));
		}
		db.close();
	}
	
	public void CallWsMethod(String METHOD_NAME) {
	    //Create request
	    SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);
	    PropertyInfo MessageCodePI = new PropertyInfo();
	    //Set Name
		MessageCodePI.setName("MessageCode");
	    //Set Value
		MessageCodePI.setValue(this.messagecode);
	    //Set dataType
		MessageCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(MessageCodePI);

	    PropertyInfo karbarCodePI = new PropertyInfo();
	    //Set Name
		karbarCodePI.setName("UserCode");
	    //Set Value
		karbarCodePI.setValue(this.karbarCode);
	    //Set dataType
		karbarCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(karbarCodePI);
	    //
	    PropertyInfo YearPI = new PropertyInfo();
	    //Set Name
		YearPI.setName("Year");
	    //Set Value
		YearPI.setValue(this.Year);
	    //Set dataType
		YearPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(YearPI);
	    PropertyInfo MonthPI = new PropertyInfo();
	    //Set Name
		MonthPI.setName("Month");
	    //Set Value
		MonthPI.setValue(this.Month);
	    //Set dataType
		MonthPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(MonthPI);
	    //
	    PropertyInfo DayPI = new PropertyInfo();
	    //Set Name
		DayPI.setName("Day");
	    //Set Value
		DayPI.setValue(this.Day);
	    //Set dataType
		DayPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(DayPI);
	    
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
	
	
	public void InsertDataFromWsToDb(String[] AllRecord)
    {
		String query=null;
		db=dbh.getReadableDatabase();
		query="SELECT * FROM messages WHERE Code='"+messagecode+"'";
		Cursor cursor= db.rawQuery(query,null);
		if(cursor.getCount()>0) {
			cursor.moveToNext();
			db = dbh.getWritableDatabase();
			query = "UPDATE  messages" +
					" SET  IsReade='1',IsSend='1' " +
					"WHERE Code='" + messagecode + "'";
			db.execSQL(query);
		}
		db.close();
    }
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(activity,Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName, VariableValue);
		activity.startActivity(intent);
	}	
}
