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
	private String acceptcode;
	private String check_load;
	private String WsResponse;
	private String LastMessageCode;
	private boolean CuShowDialog=true;
	private String[] res;
	//Contractor
	public HmLogin(Activity activity, String phonenumber, String acceptcode, String check_load) {
		this.activity = activity;
		this.phonenumber = phonenumber;		
		this.acceptcode=acceptcode;
		this.check_load=check_load;
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
        		CallWsMethod("UserLogin");
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
	            else if(res[0].toString().compareTo("-2") == 0)//نیروی جدید می باشد و باید اطلاعات اولیه دریافت شود.
	            {
	            	if(check_load.compareTo("0")==0)
					{
						setloginDeactive();
					}
					else
					{
						InsertDataFromWsToDb(res);
					}

	            }
	            else if(res[0].toString().compareTo("-3") == 0)
	            {
	            	//به صفحه منو برده شود اما امکانات غیرفعال گردد.
	            	setloginDeactive();
	            }
				else//;کاربر شناسایی شده و باید به روز رسانی اطلاعات انجام شود
				{
					setlogin();
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
//		SyncServices syncservices=new SyncServices(this.activity,this.acceptcode,"0");
//		syncservices.AsyncExecute();
		db = dbh.getReadableDatabase();
		String query = "SELECT * FROM Profile";
		Cursor cursor=db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			cursor.moveToNext();
			phonenumber=cursor.getString(cursor.getColumnIndex("Mobile"));
		}
		else {
			phonenumber="0";
		}
		LoadActivity2(Info_Person.class, "phonenumber",phonenumber,"acceptcode",this.acceptcode);
    }
	public void setlogin() 
	{
		db = dbh.getReadableDatabase();
		Cursor cursors = db.rawQuery("SELECT * FROM login", null);
		if(cursors.getCount()>0)
		{
			cursors.moveToNext();
			String Result=cursors.getString(cursors.getColumnIndex("islogin"));
			if(Result.compareTo("0")==0)
			{
				db = dbh.getWritableDatabase();
				db.execSQL("UPDATE login SET karbarCode='"+res[0].toString()+"' , islogin = '1'");
			}
		}
		else
		{
			db = dbh.getWritableDatabase();
			db.execSQL("DELETE FROM login");
			String query="INSERT INTO login (karbarCode,islogin) VALUES('"+res[0].toString()+"','1')";
			db.execSQL(query);
		}
        cursors = db.rawQuery("SELECT ifnull(MAX(CAST (code AS INT)),0)as code FROM messages", null);
        if(cursors.getCount()>0)
        {
            cursors.moveToNext();
			LastMessageCode=cursors.getString(cursors.getColumnIndex("code"));
        }

		db.close();
		SyncMessage syncMessage=new SyncMessage(this.activity, res[0].toString(),LastMessageCode);
		syncMessage.AsyncExecute();
		SyncProfile syncProfile=new SyncProfile(this.activity, res[0].toString(),this.acceptcode);
		syncProfile.AsyncExecute();
//		SyncServices syncservices=new SyncServices(this.activity,res[0].toString(),"1");
//		syncservices.AsyncExecute();
//		SyncState syncState=new SyncState(this.activity);
//		syncState.AsyncExecute();
//		SyncCity syncCity=new SyncCity(this.activity);
//		syncCity.AsyncExecute();
		SyncGetUserAddress syncGetUserAddress=new  SyncGetUserAddress(this.activity,res[0].toString(),"0");
		syncGetUserAddress.AsyncExecute();
	}
	public void setloginDeactive() 
	{
		if(check_load.compareTo("0")!=0)
		{
			Toast.makeText(this.activity.getApplicationContext(), "شما فعال نشده اید", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this.activity.getApplicationContext(), "برای استفاده از امکانات بسپارینا باید ثبت نام کنید", Toast.LENGTH_LONG).show();
		}

		LoadActivity(MainMenu.class,"karbarCode","0");
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(activity,Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		activity.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(activity,Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);

		activity.startActivity(intent);
	}
	
}
