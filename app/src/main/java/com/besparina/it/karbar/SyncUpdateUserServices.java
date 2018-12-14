package com.besparina.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class SyncUpdateUserServices {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String pUserCode;
	private String WsResponse;
	private String ServiceDetaileCode ;
	private String MaleCount ;
	private String FemaleCount ;
	private String HamyarCount ;
	private String  StartYear ;
	private String  StartMonth ;
	private String  StartDay ;
	private String  StartHour ;
	private String  StartMinute ;
	private String  EndYear ;
	private String  EndMonth ;
	private String  EndDay ;
	private String EndHour ;
	private String EndMinute ;
	private String AddressCode ;
	private String Description ;
	private String IsEmergency ;
	private String PeriodicServices ;
	private String EducationGrade ;
	private String FieldOfStudy ;
	private String StudentGender ;
	private String TeacherGender ;
	private String EducationTitle ;
	private String ArtField ;
	private String CarWashType ;
	private String CarType ;
	private String UserServices ;
	private String Language ;
	//private String acceptcode;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncUpdateUserServices(Activity activity, String pUserCode, String UserServices, String ServiceDetaileCode, String MaleCount, String FemaleCount, String HamyarCount,
                                  String StartYear, String StartMonth, String StartDay, String StartHour, String StartMinute,
                                  String EndYear, String EndMonth, String EndDay, String EndHour, String EndMinute,
                                  String AddressCode, String Description, String IsEmergency, String PeriodicServices, String EducationGrade,
                                  String FieldOfStudy, String StudentGender, String TeacherGender, String EducationTitle, String ArtField, String CarWashType,
                                  String CarType, String Language) {
		this.activity = activity;
		this.pUserCode = pUserCode;
		this.ServiceDetaileCode = ServiceDetaileCode;
		this.MaleCount = MaleCount;
		this.FemaleCount = FemaleCount;
		this.HamyarCount = HamyarCount;
		this.StartYear = StartYear;
		this.StartMonth = StartMonth;
		this.StartDay = StartDay;
		this.StartHour = StartHour;
		this.StartMinute = StartMinute;
		this.EndYear = EndYear;
		this.EndMonth = EndMonth;
		this.EndDay = EndDay;
		this.EndHour = EndHour;
		this.EndMinute = EndMinute;
		this.AddressCode = AddressCode;
		this.Description = Description;
		this.IsEmergency = IsEmergency;
		this.PeriodicServices = PeriodicServices;
		this.EducationGrade = EducationGrade;
		this.FieldOfStudy = FieldOfStudy;
		this.StudentGender = StudentGender;
		this.TeacherGender = TeacherGender;
		this.EducationTitle = EducationTitle;
		this.ArtField = ArtField;
		this.CarWashType = CarWashType;
		this.CarType = CarType;
		this.Language = Language;
		this.UserServices = UserServices;
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
        		CallWsMethod("UpdateUserServices");
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
		try {
			if (this.CarType.length() < 0 || this.CarType.compareTo("")==0) {
				this.CarType = "0";
			}
		}
		catch (Exception e)
		{
			this.CarType = "0";
		}
		try {
			if (this.CarWashType.length() < 0 || this.CarWashType.compareTo("")==0) {
				this.CarWashType = "0";
			}
		}
		catch (Exception e)
		{
			this.CarWashType = "0";
		}
		try {
			if (this.PeriodicServices.length() < 0 || this.PeriodicServices.compareTo("")==0) {
				this.PeriodicServices = "0";
			}
		}
		catch (Exception e)
		{
			this.PeriodicServices = "0";
		}
		try {
			if (this.MaleCount.length() < 0 || this.MaleCount.compareTo("")==0) {
				this.MaleCount = "0";
			}
		}
		catch (Exception e)
		{
			this.MaleCount = "0";
		}
		try {
			if (this.FemaleCount.length() < 0 || this.FemaleCount.compareTo("")==0) {
				this.FemaleCount = "0";
			}
		}
		catch (Exception e)
		{
			this.FemaleCount = "0";
		}
		try {
			if (this.HamyarCount.length() < 0 || this.HamyarCount.compareTo("")==0) {
				this.HamyarCount = "0";
			}
		}
		catch (Exception e)
		{
			this.HamyarCount = "0";
		}
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
		//****************************************************************
		PropertyInfo ServiceDetaileCodePI = new PropertyInfo();
		//Set Name
		ServiceDetaileCodePI.setName("ServiceDetaileCode");
		//Set Value
		ServiceDetaileCodePI.setValue(ServiceDetaileCode);
		//Set dataType
		ServiceDetaileCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(ServiceDetaileCodePI);
		//****************************************************************
		PropertyInfo UserServicesPI = new PropertyInfo();
		//Set Name
		UserServicesPI.setName("UserServices");
		//Set Value
		UserServicesPI.setValue(UserServices);
		//Set dataType
		UserServicesPI.setType(String.class);
		//Add the property to request object
		request.addProperty(UserServicesPI);
		//****************************************************************
		PropertyInfo MaleCountPI = new PropertyInfo();
		//Set Name
		MaleCountPI.setName("MaleCount");
		//Set Value
		MaleCountPI.setValue(MaleCount);
		//Set dataType
		MaleCountPI.setType(String.class);
		//Add the property to request object
		request.addProperty(MaleCountPI);
		//****************************************************************
		PropertyInfo FemaleCountPI = new PropertyInfo();
		//Set Name
		FemaleCountPI.setName("FemaleCount");
		//Set Value
		FemaleCountPI.setValue(FemaleCount);
		//Set dataType
		FemaleCountPI.setType(String.class);
		//Add the property to request object
		request.addProperty(FemaleCountPI);
		//****************************************************************
		PropertyInfo HamyarCountPI = new PropertyInfo();
		//Set Name
		HamyarCountPI.setName("HamyarCount");
		//Set Value
		HamyarCountPI.setValue(HamyarCount);
		//Set dataType
		HamyarCountPI.setType(String.class);
		//Add the property to request object
		request.addProperty(HamyarCountPI);
		//****************************************************************
		PropertyInfo StartYearPI = new PropertyInfo();
		//Set Name
		StartYearPI.setName("StartYear");
		//Set Value
		StartYearPI.setValue(StartYear);
		//Set dataType
		StartYearPI.setType(String.class);
		//Add the property to request object
		request.addProperty(StartYearPI);
		//****************************************************************
		PropertyInfo StartMonthPI = new PropertyInfo();
		//Set Name
		StartMonthPI.setName("StartMonth");
		//Set Value
		StartMonthPI.setValue(StartMonth);
		//Set dataType
		StartMonthPI.setType(String.class);
		//Add the property to request object
		request.addProperty(StartMonthPI);
		//****************************************************************
		PropertyInfo StartDayPI = new PropertyInfo();
		//Set Name
		StartDayPI.setName("StartDay");
		//Set Value
		StartDayPI.setValue(StartDay);
		//Set dataType
		StartDayPI.setType(String.class);
		//Add the property to request object
		request.addProperty(StartDayPI);
		//****************************************************************
		PropertyInfo StartHourPI = new PropertyInfo();
		//Set Name
		StartHourPI.setName("StartHour");
		//Set Value
		StartHourPI.setValue(StartHour);
		//Set dataType
		StartHourPI.setType(String.class);
		//Add the property to request object
		request.addProperty(StartHourPI);
		//****************************************************************
		PropertyInfo StartMinutePI = new PropertyInfo();
		//Set Name
		StartMinutePI.setName("StartMinute");
		//Set Value
		StartMinutePI.setValue(StartMinute);
		//Set dataType
		StartMinutePI.setType(String.class);
		//Add the property to request object
		request.addProperty(StartMinutePI);
		//****************************************************************
		PropertyInfo EndYearPI = new PropertyInfo();
		//Set Name
		EndYearPI.setName("EndYear");
		//Set Value
		EndYearPI.setValue(EndYear);
		//Set dataType
		EndYearPI.setType(String.class);
		//Add the property to request object
		request.addProperty(EndYearPI);
		//****************************************************************
		PropertyInfo EndMonthPI = new PropertyInfo();
		//Set Name
		EndMonthPI.setName("EndMonth");
		//Set Value
		EndMonthPI.setValue(EndMonth);
		//Set dataType
		EndMonthPI.setType(String.class);
		//Add the property to request object
		request.addProperty(EndMonthPI);
		//****************************************************************
		PropertyInfo EndDayPI = new PropertyInfo();
		//Set Name
		EndDayPI.setName("EndDay");
		//Set Value
		EndDayPI.setValue(EndDay);
		//Set dataType
		EndDayPI.setType(String.class);
		//Add the property to request object
		request.addProperty(EndDayPI);
		//****************************************************************
		PropertyInfo EndHourPI = new PropertyInfo();
		//Set Name
		EndHourPI.setName("EndHour");
		//Set Value
		EndHourPI.setValue(EndHour);
		//Set dataType
		EndHourPI.setType(String.class);
		//Add the property to request object
		request.addProperty(EndHourPI);
		//****************************************************************
		PropertyInfo EndMinutePI = new PropertyInfo();
		//Set Name
		EndMinutePI.setName("EndMinute");
		//Set Value
		EndMinutePI.setValue(EndMinute);
		//Set dataType
		EndMinutePI.setType(String.class);
		//Add the property to request object
		request.addProperty(EndMinutePI);
		//****************************************************************
		PropertyInfo AddressCodePI = new PropertyInfo();
		//Set Name
		AddressCodePI.setName("AddressCode");
		//Set Value
		AddressCodePI.setValue(AddressCode);
		//Set dataType
		AddressCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(AddressCodePI);
		//****************************************************************
		PropertyInfo DescriptionPI = new PropertyInfo();
		//Set Name
		DescriptionPI.setName("Description");
		//Set Value
		DescriptionPI.setValue(Description);
		//Set dataType
		DescriptionPI.setType(String.class);
		//Add the property to request object
		request.addProperty(DescriptionPI);
		//****************************************************************
		PropertyInfo IsEmergencyPI = new PropertyInfo();
		//Set Name
		IsEmergencyPI.setName("IsEmergency");
		//Set Value
		IsEmergencyPI.setValue(IsEmergency);
		//Set dataType
		IsEmergencyPI.setType(String.class);
		//Add the property to request object
		request.addProperty(IsEmergencyPI);
		//****************************************************************
		PropertyInfo PeriodicServicesPI = new PropertyInfo();
		//Set Name
		PeriodicServicesPI.setName("PeriodicServices");
		//Set Value
		PeriodicServicesPI.setValue(PeriodicServices);
		//Set dataType
		PeriodicServicesPI.setType(String.class);
		//Add the property to request object
		request.addProperty(PeriodicServicesPI);
		//****************************************************************
		PropertyInfo EducationGradePI = new PropertyInfo();
		//Set Name
		EducationGradePI.setName("EducationGrade");
		//Set Value
		EducationGradePI.setValue(EducationGrade);
		//Set dataType
		EducationGradePI.setType(String.class);
		//Add the property to request object
		request.addProperty(EducationGradePI);
		//****************************************************************
		PropertyInfo FieldOfStudyPI = new PropertyInfo();
		//Set Name
		FieldOfStudyPI.setName("FieldOfStudy");
		//Set Value
		FieldOfStudyPI.setValue(FieldOfStudy);
		//Set dataType
		FieldOfStudyPI.setType(String.class);
		//Add the property to request object
		request.addProperty(FieldOfStudyPI);
		//****************************************************************
		PropertyInfo StudentGenderPI = new PropertyInfo();
		//Set Name
		StudentGenderPI.setName("StudentGender");
		//Set Value
		StudentGenderPI.setValue(StudentGender);
		//Set dataType
		StudentGenderPI.setType(String.class);
		//Add the property to request object
		request.addProperty(StudentGenderPI);
		//****************************************************************
		PropertyInfo TeacherGenderPI = new PropertyInfo();
		//Set Name
		TeacherGenderPI.setName("TeacherGender");
		//Set Value
		TeacherGenderPI.setValue(TeacherGender);
		//Set dataType
		TeacherGenderPI.setType(String.class);
		//Add the property to request object
		request.addProperty(TeacherGenderPI);
		//****************************************************************
		PropertyInfo EducationTitlePI = new PropertyInfo();
		//Set Name
		EducationTitlePI.setName("EducationTitle");
		//Set Value
		EducationTitlePI.setValue(EducationTitle);
		//Set dataType
		EducationTitlePI.setType(String.class);
		//Add the property to request object
		request.addProperty(EducationTitlePI);
		//****************************************************************
		PropertyInfo ArtFieldPI = new PropertyInfo();
		//Set Name
		ArtFieldPI.setName("ArtField");
		//Set Value
		ArtFieldPI.setValue(ArtField);
		//Set dataType
		ArtFieldPI.setType(String.class);
		//Add the property to request object
		request.addProperty(ArtFieldPI);
		//****************************************************************
		PropertyInfo CarWashTypePI = new PropertyInfo();
		//Set Name
		CarWashTypePI.setName("CarWashType");
		//Set Value
		CarWashTypePI.setValue(CarWashType);
		//Set dataType
		CarWashTypePI.setType(String.class);
		//Add the property to request object
		request.addProperty(CarWashTypePI);
		//****************************************************************
		PropertyInfo CarTypePI = new PropertyInfo();
		//Set Name
		CarTypePI.setName("CarType");
		//Set Value
		CarTypePI.setValue(CarType);
		//Set dataType
		CarTypePI.setType(String.class);
		//Add the property to request object
		request.addProperty(CarTypePI);
		//****************************************************************
		PropertyInfo LanguagePI = new PropertyInfo();
		//Set Name
		LanguagePI.setName("Language");
		//Set Value
		LanguagePI.setValue(Language);
		//Set dataType
		LanguagePI.setType(String.class);
		//Add the property to request object
		request.addProperty(LanguagePI);

//****************************************************************************************
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
		try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
		db.execSQL("DELETE FROM OrdersService WHERE Code='" + UserServices + "'");
		db.execSQL("INSERT INTO OrdersService ("+
				"Code,"+
				"pUserCode,"+
				"ServiceDetaileCode,"+
				"MaleCount,"+
				"FemaleCount,"+
				"HamyarCount,"+
				"StartYear,"+
				"StartMonth,"+
				"StartDay,"+
				"StartHour,"+
				"StartMinute,"+
				"EndYear,"+
				"EndMonth,"+
				"EndDay,"+
				"EndHour,"+
				"EndMinute,"+
				"AddressCode,"+
				"Description,"+
				"IsEmergency,"+
				"PeriodicServices,"+
				"EducationGrade,"+
				"FieldOfStudy,"+
				"StudentGender,"+
				"TeacherGender,"+
				"EducationTitle,"+
				"ArtField,"+
				"CarWashType,"+
				"CarType,"+
				"Language," +
				"Status) VALUES('"+
				UserServices+"','"+
				pUserCode+"','"+
				ServiceDetaileCode+"','"+
				MaleCount+"','"+
				FemaleCount+"','"+
				HamyarCount+"','"+
				StartYear+"','"+
				StartMonth+"','"+
				StartDay+"','"+
				StartHour+"','"+
				StartMinute+"','"+
				EndYear+"','"+
				EndMonth+"','"+
				EndDay+"','"+
				EndHour+"','"+
				EndMinute+"','"+
				AddressCode+"','"+
				Description+"','"+
				IsEmergency+"','"+
				PeriodicServices+"','"+
				EducationGrade+"','"+
				FieldOfStudy+"','"+
				StudentGender+"','"+
				TeacherGender+"','"+
				EducationTitle+"','"+
				ArtField+"','"+
				CarWashType+"','"+
				CarType+"','"+
				Language+"','0')");
//		db.execSQL("UPDATE OrdersService SET "+
//					"MaleCount='"+MaleCount+"',"+
//					"FemaleCount='"+FemaleCount+"',"+
//					"HamyarCount='"+HamyarCount+"',"+
//					"StartYear='"+StartYear+"',"+
//					"StartMonth='"+StartMonth+"',"+
//					"StartDay='"+StartDay+"',"+
//					"StartHour='"+StartHour+"',"+
//					"StartMinute='"+StartMinute+"',"+
//					"EndYear='"+EndYear+"',"+
//					"EndMonth='"+EndMonth+"',"+
//					"EndDay='"+EndDay+"',"+
//					"EndHour='"+EndHour+"',"+
//					"EndMinute='"+EndMinute+"',"+
//					"AddressCode='"+AddressCode+"',"+
//					"Description='"+Description+"',"+
//					"IsEmergency='"+IsEmergency+"',"+
//					"PeriodicServices='"+PeriodicServices+"',"+
//					"EducationGrade='"+EducationGrade+"',"+
//					"FieldOfStudy='"+FieldOfStudy+"',"+
//					"StudentGender='"+StudentGender+"',"+
//					"TeacherGender='"+TeacherGender+"',"+
//					"EducationTitle='"+EducationTitle+"',"+
//					"ArtField='"+ArtField+"',"+
//					"CarWashType='"+CarWashType+"',"+
//					"CarType='"+CarType+"',"+
//					"Language='"+Language+"'," +
//					"Status='0' WHERE Code='"+UserServices+"'");
		try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
		Toast.makeText(activity, "سرویس ویرایش شد.", Toast.LENGTH_LONG).show();
		LoadActivity(MainMenu.class, "karbarCode", pUserCode);
    }
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(this.activity,Cls);
		intent.putExtra(VariableName, VariableValue);
		activity.startActivity(intent);
	}

}

