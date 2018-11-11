	package com.besparina.it.karbar;

    import android.*;
    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.net.Uri;
    import android.os.Bundle;
    import android.support.annotation.NonNull;

    import android.support.v4.app.ActivityCompat;
    import android.view.KeyEvent;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.Toast;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class List_ServiceDerails extends Activity {
        private String karbarCode;
        final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
        private String codeService;
        private ListView lvServiceDetails;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        private Button btnOrder;
        private Button btnAcceptOrder;
        private Button btncredite;
        private Button btnServiceEmergency;

        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_servicedetails);
            btnOrder=(Button)findViewById(R.id.btnOrderBottom);
            btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
            btncredite=(Button)findViewById(R.id.btncrediteBottom);            btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);
            btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);
            lvServiceDetails=(ListView)findViewById(R.id.listViewServiceDetails);
            dbh=new DatabaseHelper(getApplicationContext());
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
            try
            {
                codeService = getIntent().getStringExtra("codeService").toString();
            }
            catch (Exception ex)
            {
                //todo
            }
        try
        {
            karbarCode = getIntent().getStringExtra("karbarCode").toString();
        }
        catch (Exception e)
        {
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM login",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();

                karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
            }
            db.close();
        }

            ImageView imgview = (ImageView)findViewById(R.id.BesparinaLogo);
            imgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(MainMenu.class,"","");
                }
            });

            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM Servicesdetails WHERE servicename='"+codeService+"'",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name",coursors.getString(coursors.getColumnIndex("name")));
                map.put("Code",coursors.getString(coursors.getColumnIndex("code")));
                valuse.add(map);
            }
            db.close();
            AdapterServiceDetails dataAdapter=new AdapterServiceDetails(this,valuse,karbarCode);
            lvServiceDetails.setAdapter(dataAdapter);



//            db=dbh.getReadableDatabase();
//            Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//                    "LEFT JOIN " +
//                    "Servicesdetails ON " +
//                    "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'  ORDER BY CAST(OrdersService.Code AS int) desc", null);
//            if (cursor2.getCount() > 0) {
//                btnOrder.setText("درخواست ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
//            }
//            cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13) ORDER BY CAST(Code AS int) desc", null);
//            if (cursor2.getCount() > 0) {
//                btnAcceptOrder.setText("پذیرفته شده ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
//            }
//            cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
//            if (cursor2.getCount() > 0) {
//                cursor2.moveToNext();
//                try {
//			String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
//			if(splitStr[1].compareTo("00")==0)
//			{
//				btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(splitStr[0])+")");
//			}
//			else
//			{
//				btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(cursor2.getString(cursor2.getColumnIndex("Amount")))+")");
//			}
//
//		} catch (Exception ex) {
//			btncredite.setText(PersianDigitConverter.PerisanNumber("اعتبار( " + "0")+")");
//		}
//            }
//            db.close();
            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String QueryCustom;
                    QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                            "LEFT JOIN " +
                            "Servicesdetails ON " +
                            "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'  ORDER BY CAST(OrdersService.Code AS int) desc";
                    LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
                }
            });
            btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String QueryCustom;
                    QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                            "LEFT JOIN " +
                            "Servicesdetails ON " +
                            "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (1,2,6,7,12,13) ORDER BY CAST(OrdersService.Code AS int) desc";
                    LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
                }
            });
            btncredite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LoadActivity(Credit.class, "karbarCode", karbarCode);
                }
            });
            btnServiceEmergency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ActivityCompat.checkSelfPermission(List_ServiceDerails.this,
                            android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(List_ServiceDerails.this, android.Manifest.permission.CALL_PHONE))
                        {
                            ActivityCompat.requestPermissions(List_ServiceDerails.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(List_ServiceDerails.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
                        }

                    }
                    db = dbh.getReadableDatabase();
                    Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
                    if (cursorPhone.getCount() > 0) {
                        cursorPhone.moveToNext();
                        dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
                    }
                    db.close();
                }
            });
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            finish();
            LoadActivity(MainMenu.class, "karbarCode", karbarCode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
        {
            finish();
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            List_ServiceDerails.this.startActivity(intent);
        }
        public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
                , String VariableName2, String VariableValue2) {
            finish();
            Intent intent = new Intent(getApplicationContext(), Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            this.startActivity(intent);
        }
        public void dialContactPhone(String phoneNumber) {
            //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
            startActivity(callIntent);
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            if (grantResults.length > 0) {
                switch (requestCode) {
                    case REQUEST_CODE_ASK_PERMISSIONS:
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            // Permission Granted
                            db = dbh.getReadableDatabase();
                            Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
                            if (cursorPhone.getCount() > 0) {
                                cursorPhone.moveToNext();
                                dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
                            }
                            db.close();
                        } else {
                            // Permission Denied
                            Toast.makeText(this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
                                    .show();
                        }
                        break;
                    default:
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
}
