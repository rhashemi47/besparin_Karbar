package com.besparina.it.karbar;

import android.app.ProgressDialog;
import android.content.Context;
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

class SyncServicesPic {	DatabaseHelper dbh;
    SQLiteDatabase db;
    PublicVariable PV;
    InternetConnection IC;
    private Context activity;
    private String WsResponse;
    private String PicCode;
    private boolean CuShowDialog=false;
    //Contractor
    public SyncServicesPic(Context activity,String PicCode) {
        this.activity = activity;
        this.PicCode = PicCode;
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
                SyncServicesPic.AsyncCallWS task = new SyncServicesPic.AsyncCallWS(this.activity);
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
                CallWsMethod("GetBsServicesDetailsPic");
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
        //****************************************
        PropertyInfo PicCode = new PropertyInfo();
        //Set Name
        PicCode.setName("PicCode");
        //Set Value
        PicCode.setValue(this.PicCode);
        //Set dataType
        PicCode.setType(String.class);
        //Add the property to request object
        request.addProperty(PicCode);
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
            db.execSQL("INSERT INTO PicServices (code,Pic) VALUES('"+this.PicCode+"','"+AllRecord+"')");
        try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
    }
}
