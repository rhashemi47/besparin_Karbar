package com.besparina.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import java.util.regex.Pattern;

public class SyncServicesForService {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Context activity;
	private String WsResponse;
	private String flag;
	//private String acceptcode;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncServicesForService(Context activity,
								  DatabaseHelper dbh,
								  SQLiteDatabase db) {
		this.activity = activity;
		this.flag = flag;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		this.dbh = dbh;
		this.db = db;
		PublicVariable.theard_GetServicesAndServiceDetails=false;
//		dbh=new DatabaseHelper(this.activity.getApplicationContext());
//		try {
//
//			dbh.createDataBase();
//
//   		} catch (IOException ioe) {
//			PublicVariable.theard_GetServicesAndServiceDetails=true;
//   			throw new Error("Unable to create database");
//
//   		}
//
//   		try {
//
//   			dbh.openDataBase();
//
//   		} catch (SQLException sqle) {
//			PublicVariable.theard_GetServicesAndServiceDetails=true;
//   			throw sqle;
//   		}
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
				 PublicVariable.theard_GetServicesAndServiceDetails=true;
	            e.printStackTrace();
			 }
		}
		else
		{
			PublicVariable.theard_GetServicesAndServiceDetails=true;
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
        	try
        	{
        		CallWsMethod("GetServices");
        	}
	    	catch (Exception e) {
				PublicVariable.theard_GetServicesAndServiceDetails=true;
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
				PublicVariable.theard_GetServicesAndServiceDetails=true;
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	//akeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//akeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else
	            {
	            	InsertDataFromWsToDb(WsResponse);
	            }
        	}
        	else
        	{
				PublicVariable.theard_GetServicesAndServiceDetails=true;
        	}
            try
            {
            	if (this.dialog.isShowing()) {
            		this.dialog.dismiss();
            	}
            }
            catch (Exception e) {
				PublicVariable.theard_GetServicesAndServiceDetails=true;
			}
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
	    PropertyInfo VerifyCode = new PropertyInfo();
	    //Set Name
	    VerifyCode.setName("VerifyCode");
	    //Set Value
	    VerifyCode.setValue("Besparin#$1");
	    //Set dataType
	    VerifyCode.setType(String.class);
	    //Add the property to request object
	    request.addProperty(VerifyCode);	    
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
		res=WsResponse.split(Pattern.quote("[Besparina@@]"));
//		db.execSQL("DELETE FROM services");
		try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
		for(int i=0;i<res.length;i++){
			value=res[i].split(Pattern.quote("[Besparina##]"));
			if(!check_existService(value[0])) {
				try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
				db.execSQL("INSERT INTO services (code,servicename,Pic_Code) VALUES('" + value[0] + "','" + value[1] + "','" + value[2] + "')");
			}
			else
			{
				try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
				db.execSQL("UPDATE services Set code='"+value[0]+"',servicename='"+value[1]+"',Pic_Code='"+value[2]+"' WHERE code='"+value[0]+"'");
			}
            if(!check_existPic(value[2])) {
				SyncServicesPic syncServicesPic=new SyncServicesPic(this.activity,value[2]);
				syncServicesPic.AsyncExecute();
			}
		}
		try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
		SyncServicesDetailsForService syncServicesDetailsForService=new SyncServicesDetailsForService(this.activity);
		syncServicesDetailsForService.AsyncExecute();
    }
    private boolean check_existService(String Code)
	{
		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
		String Query="SELECT * FROM services WHERE code='"+Code+"'";
		Cursor cursor=db.rawQuery(Query,null);
		if(cursor.getCount()>0)
		{
			try {	if (!cursor.isClosed()) {	cursor.close();	}}	catch (Exception ex){	}
			try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
			return true;
		}
		else {
			try {	if (!cursor.isClosed()) {	cursor.close();	}}	catch (Exception ex){	}
			try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
			return false;
		}
	}
	private boolean check_existPic(String Pic_Code)
	{
		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
		String Query="SELECT * FROM PicServices WHERE Code='"+Pic_Code+"'";
		Cursor cursor=db.rawQuery(Query,null);
		if(cursor.getCount()>0)
		{
			try {	if (!cursor.isClosed()) {	cursor.close();	}}	catch (Exception ex){	}
			try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
			return true;
		}
		else {
			try {	if (!cursor.isClosed()) {	cursor.close();	}}	catch (Exception ex){	}
			try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
			return false;
		}
	}
	
}
