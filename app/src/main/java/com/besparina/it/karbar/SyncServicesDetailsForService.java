package com.besparina.it.karbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import java.util.regex.Pattern;

public class SyncServicesDetailsForService {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Context activity;
	private String WsResponse;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncServicesDetailsForService(Context activity) {
		this.activity = activity;
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
			//Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
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
        		CallWsMethod("GetBsServicesDetails");
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
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
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
		//db.execSQL("DELETE FROM servicesdetails");
		try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
		for(int i=0;i<res.length;i++){
			value=res[i].split(Pattern.quote("[Besparina##]"));
			if(!check_existServiceDetail(value[0])) {
				try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
				db.execSQL("INSERT INTO servicesdetails (code,servicename,type,name,Pic_Code) VALUES('"+value[0] +"','"+value[1]+"','"+value[2]+"','"+value[3]+"','"+value[4]+"')");
			}
			else
			{
				try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
				db.execSQL("UPDATE servicesdetails Set code='"+value[0]+"',servicename='"+value[1]+"',type='"+value[2]+"',name='"+value[3]+"',Pic_Code='"+value[4]+"' WHERE code='"+value[0]+"'");
			}
			try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
			if(!check_existPic(value[4])) {
				SyncServicesPic syncServicesPic = new SyncServicesPic(this.activity,value[4]);
                syncServicesPic.AsyncExecute();
			}
		}
		try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
    }
	private boolean check_existServiceDetail(String Code)
	{
		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
		String Query="SELECT * FROM servicesdetails WHERE code='"+Code+"'";
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
