	package com.besparina.it.karbar;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.view.KeyEvent;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.TextView;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class List_Messages extends Activity {
        private String karbarCode;

        private ListView lvMessage;
        private TextView tvNotMessage;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private Button btnOrder;
        private Button btnAcceptOrder;
        private Button btncredite;
        private String[] title;
//        private String[] content;
        private Integer[] imgID;
        private Integer[] rowID;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_messages);
        btnOrder=(Button)findViewById(R.id.btnOrderBottom);
        btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
        btncredite=(Button)findViewById(R.id.btncrediteBottom);
        lvMessage=(ListView)findViewById(R.id.listViewMessages);
        tvNotMessage=(TextView) findViewById(R.id.tvNotMessage);
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
        //******************************************************************************
            Preparedata();
        //*****************************************************************************
            db=dbh.getReadableDatabase();
            Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'", null);
            if (cursor2.getCount() > 0) {
                btnOrder.setText("درخواست ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+"(");
            }
            cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
            if (cursor2.getCount() > 0) {
                btnAcceptOrder.setText("پذیرفته شده ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+"(");
            }
            cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
            if (cursor2.getCount() > 0) {
                cursor2.moveToNext();
                try {
                    btncredite.setText("اعتبار( " +  PersianDigitConverter.PerisanNumber(cursor2.getString(cursor2.getColumnIndex("Amount")))+"(");
                } catch (Exception ex) {
                    btncredite.setText("اعتبار( " + PersianDigitConverter.PerisanNumber("0"));
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
                            "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'";
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
                            "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (1,2,6,7,12,13)";
                    LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
                }
            });
            btncredite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LoadActivity(Credit.class, "karbarCode", karbarCode);
                }
            });
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            List_Messages.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);

            List_Messages.this.startActivity(intent);
        }
        public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
                , String VariableName2, String VariableValue2) {
            Intent intent = new Intent(getApplicationContext(), Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            this.startActivity(intent);
        }
        public void Preparedata()
        {
            tvNotMessage.setVisibility(View.VISIBLE);
            lvMessage.setVisibility(View.GONE);
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM messages WHERE IsDelete='0'",null);
            if(coursors.getCount()>0)
            {
                tvNotMessage.setVisibility(View.GONE);
                lvMessage.setVisibility(View.VISIBLE);
                title=new String[coursors.getCount()];
                rowID=new Integer[coursors.getCount()];
                imgID=new Integer[coursors.getCount()];
                for(int i=0;i<coursors.getCount();i++){
                    coursors.moveToNext();
                    String subStr="";
                    int len=0,charselectlen=50;
                    HashMap<String, String> map = new HashMap<String, String>();
                    len=coursors.getString(coursors.getColumnIndex("Content")).length();
                    if(len<=charselectlen){
                        subStr=coursors.getString(coursors.getColumnIndex("Content"));
                    }
                    else
                    {
                        subStr=coursors.getString(coursors.getColumnIndex("Content")).substring(0,charselectlen);
                    }
                    map.put("Title",coursors.getString(coursors.getColumnIndex("Title")));
                    map.put("Content",subStr+"\n"+coursors.getString(coursors.getColumnIndex("InsertDate")));
                    map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
                    map.put("IsReade",coursors.getString(coursors.getColumnIndex("IsReade")));
                    valuse.add(map);
                }
                AdapterMessage dataAdapter=new AdapterMessage(List_Messages.this,valuse);
                lvMessage.setAdapter(dataAdapter);
            }
            db.close();
        }
    }

