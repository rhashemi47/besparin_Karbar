package com.besparina.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class HmLogin {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String phonenumber;
	private String id;
	private String acceptcode;
	private String WsResponse;
	private String LastMessageCode;
	private boolean CuShowDialog=true;
	private boolean CuLoadActivityAfterExecute;
	private String[] res;
	//Contractor
	public HmLogin(Activity activity, String phonenumber, String acceptcode) {
		this.activity = activity;
		this.phonenumber = phonenumber;		
		this.acceptcode=acceptcode;
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
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("HmLogin");
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
        		res=WsResponse.split("##");
	            if(res[0].toString().compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(res[0].toString().compareTo("0") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "کد اشتباه است", Toast.LENGTH_LONG).show();
	            }
	            else if(res[0].toString().compareTo("1") == 0)//همیار شناسایی شده و باید به روز رسانی اطلاعات انجام شود
	            {
	            	setlogin();
	            }
	            else if(res[0].toString().compareTo("2") == 0)//نیروی جدید می باشد و باید اطلاعات اولیه دریافت شود.
	            {
	            	InsertDataFromWsToDb(res);
	            }
	            else if(res[0].toString().compareTo("3") == 0)
	            {
	            	//به صفحه منو برده شود اما امکانات غیرفعال گردد.
	            	setloginDeactive();
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
	
//	String LastNewsId;
//	public void LoadMaxNewId()
//	{
//		db = dbh.getReadableDatabase();
//		Cursor cursors = db.rawQuery("select IFNULL(max(id),0)MID from news", null);
//		if(cursors.getCount() > 0)
//		{
//			cursors.moveToNext();
//			LastNewsId = cursors.getString(cursors.getColumnIndex("MID"));
//		}
//	}
	
	public void CallWsMethod(String METHOD_NAME) {
	    //Create request
	    SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);
	    PropertyInfo phoneNuberPI = new PropertyInfo();
	    //Set Name
	    phoneNuberPI.setName("PhoneNumber");
	    //Set Value
	    phoneNuberPI.setValue(this.phonenumber);
	    //Set dataType
	    phoneNuberPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(phoneNuberPI);
	    PropertyInfo acceptcodePI = new PropertyInfo();
	    //****************************************************************
	    //Set Name
	    acceptcodePI.setName("AcceptCode");
	    //Set Value
	    acceptcodePI.setValue(this.acceptcode);
	    //Set dataType
	    acceptcodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(acceptcodePI);	
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
		SyncEducation syncEducation=new SyncEducation(this.activity,this.phonenumber,this.acceptcode);
		syncEducation.AsyncExecute();
    }
	public void setlogin() 
	{
	    String LastHamyarUserServiceCode=null;
		db = dbh.getReadableDatabase();
		Cursor cursors = db.rawQuery("SELECT * FROM login", null);
		if(cursors.getCount()>0)
		{
			cursors.moveToNext();
			String Result=cursors.getString(cursors.getColumnIndex("islogin"));
			if(Result.compareTo("0")==0)
			{
				try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
				db.execSQL("UPDATE login SET hamyarcode='"+res[1].toString()+"' , guid='"+res[2].toString()+"' , islogin = '1'");
			}
		}
		else
		{
			try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
			db.execSQL("DELETE FROM login");
			String query="INSERT INTO login (hamyarcode,guid,islogin) VALUES('"+res[1].toString()+"','"+res[2].toString()+"','1')";
			db.execSQL(query);
			try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
		}

        cursors = db.rawQuery("SELECT ifnull(MAX(code),0)as code FROM BsUserServices", null);
        if(cursors.getCount()>0)
        {
            cursors.moveToNext();
            LastHamyarUserServiceCode=cursors.getString(cursors.getColumnIndex("code"));
        }
        cursors = db.rawQuery("SELECT ifnull(MAX(code),0)as code FROM messages", null);
        if(cursors.getCount()>0)
        {
            cursors.moveToNext();
			LastMessageCode=cursors.getString(cursors.getColumnIndex("code"));
        }
		try {	if (db.isOpen()) {	db.close();if(!cursors.isClosed())
			cursors.close();	}}	catch (Exception ex){	}
		SyncMessage syncMessage=new SyncMessage(this.activity, res[2].toString(), res[1].toString(),LastMessageCode,LastHamyarUserServiceCode);
		syncMessage.AsyncExecute();

	}
	public void setloginDeactive() 
	{
    	Toast.makeText(this.activity.getApplicationContext(), "شما فعال نشده اید", Toast.LENGTH_LONG).show();
		LoadActivity(MainActivity.class, "guid", "0","hamyarcode","0");
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(activity,Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		activity.startActivity(intent);
	}
	
}
