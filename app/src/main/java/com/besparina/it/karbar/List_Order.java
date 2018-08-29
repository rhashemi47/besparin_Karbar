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

    public class List_Order extends Activity {
        private String karbarCode;
        private String QueryCustom;
        final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
        private ListView lvServices;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private Button btnOrder;
        private Button btnAcceptOrder;
        private Button btncredite;	private Button btnServiceEmergency;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_orders);
        lvServices=(ListView)findViewById(R.id.listViewOrders);
            btnOrder=(Button)findViewById(R.id.btnOrderBottom);
            btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
            btncredite=(Button)findViewById(R.id.btncrediteBottom);
            btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);
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
                QueryCustom = getIntent().getStringExtra("QueryCustom").toString();

            }
            catch (Exception ex)
            {
                QueryCustom="";
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
            Cursor coursors;
            if(QueryCustom.compareTo("")==0) {
                 coursors = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                        "LEFT JOIN " +
                        "Servicesdetails ON " +
                        "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0' ORDER BY CAST(OrdersService.Code AS int) desc", null);
            }
            else {
                 coursors = db.rawQuery(QueryCustom, null);
            }
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                String StartDate,EndDate;
                String StartTime,EndTime;
                StartDate=coursors.getString(coursors.getColumnIndex("StartYear"))+"/"+
                        coursors.getString(coursors.getColumnIndex("StartMonth"))+"/"+
                        coursors.getString(coursors.getColumnIndex("StartDay"));
                EndDate=coursors.getString(coursors.getColumnIndex("EndYear"))+"/"+
                        coursors.getString(coursors.getColumnIndex("EndMonth"))+"/"+
                        coursors.getString(coursors.getColumnIndex("EndDay"));
                StartTime=coursors.getString(coursors.getColumnIndex("StartHour"))+":"+
                        coursors.getString(coursors.getColumnIndex("StartMinute"));
                EndTime=coursors.getString(coursors.getColumnIndex("EndHour"))+":"+
                        coursors.getString(coursors.getColumnIndex("EndMinute"));
                String StrStatus="";
                String Address="";
                switch (coursors.getString(coursors.getColumnIndex("Status")))
                {
                    case "0":
                        StrStatus="آزاد";
                        break;
                    case "1":
                        StrStatus="در نوبت انجام";
                        break;
                    case "2":
                        StrStatus="در حال انجام";
                        break;
                    case "3":
                        StrStatus="لغو شده";
                        break;
                    case "4":
                        StrStatus="اتمام و تسویه شده";
                        break;
                    case "5":
                        StrStatus="اتمام و تسویه نشده";
                        break;
                    case "6":
                        StrStatus="اعلام شکایت";
                        break;
                    case "7":
                        StrStatus="درحال پیگیری شکایت و یا رفع خسارت";
                        break;
                    case "8":
                        StrStatus="رفع عیب و خسارت شده توسط همیار";
                        break;
                    case "9":
                        StrStatus="پرداخت خسارت";
                        break;
                    case "10":
                        StrStatus="پرداخت جریمه";
                        break;
                    case "11":
                        StrStatus="تسویه حساب با همیار";
                        break;
                    case "12":
                        StrStatus="متوقف و تسویه شده";
                        break;
                    case "13":
                        StrStatus="متوقف و تسویه نشده";
                        break;
                }
                Cursor coursors_address = db.rawQuery("SELECT * FROM address WHERE Code ='"+coursors.getString(coursors.getColumnIndex("AddressCode"))+"'", null);
                if(coursors_address.getCount()>0) {
                    coursors_address.moveToNext();
                    map.put("TitleOrder",coursors.getString(coursors.getColumnIndex("name")));
                    map.put( "NumberOrder" , coursors.getString(coursors.getColumnIndex("Code")));
                    map.put( "DateOrder" ,StartDate + " - " + EndDate);
                    map.put( "TimeOrder" ,StartTime + " - " + EndTime);
                    map.put( "AddressOrder" ,coursors_address.getString(coursors_address.getColumnIndex("AddressText")));
                    map.put( "EmergencyOrder" ,((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0") == 0 ? "عادی" : "فوری")));
                    map.put( "StatusOrder" ,StrStatus);
                    valuse.add(map);
                }
                else
                {
                    Toast.makeText(List_Order.this,"خطا در بارگزاری اطلاعات",Toast.LENGTH_LONG).show();
                }
            }
            db.close();

            AdapterServices dataAdapter=new AdapterServices(List_Order.this,valuse,karbarCode);
            lvServices.setAdapter(dataAdapter);



            db=dbh.getReadableDatabase();
            Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'  ORDER BY CAST(OrdersService.Code AS int) desc", null);
            if (cursor2.getCount() > 0) {
                btnOrder.setText("درخواست ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
            }
            cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13) ORDER BY CAST(Code AS int) desc", null);
            if (cursor2.getCount() > 0) {
                btnAcceptOrder.setText("پذیرفته شده ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
            }
            cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
            if (cursor2.getCount() > 0) {
                cursor2.moveToNext();
                try {
			String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
			if(splitStr[1].compareTo("00")==0)
			{
				btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(splitStr[0])+")");
			}
			else
			{
				btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber(cursor2.getString(cursor2.getColumnIndex("Amount")))+")");
			}

		} catch (Exception ex) {
			btncredite.setText(PersianDigitConverter.PerisanNumber("اعتبار( " + "0")+")");
		}
            }
            db.close();
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

                    if (ActivityCompat.checkSelfPermission(List_Order.this,
                            android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(List_Order.this, android.Manifest.permission.CALL_PHONE))
                        {
                            ActivityCompat.requestPermissions(List_Order.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(List_Order.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
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
            List_Order.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);

            List_Order.this.startActivity(intent);
        }
        public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
                , String VariableName2, String VariableValue2) {
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
