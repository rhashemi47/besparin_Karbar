	package com.besparina.it.karbar;

    import android.app.Activity;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.view.KeyEvent;
    import android.view.View;
    import android.widget.ListView;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    public class List_Messages extends Activity {
        private String hamyarcode;
        private String guid;
        private ListView lvMessage;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private String[] title;
//        private String[] content;
        private Integer[] imgID;
        private Integer[] rowID;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_messages);
        lvMessage=(ListView)findViewById(R.id.listViewMessages);
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
            hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
            guid = getIntent().getStringExtra("guid").toString();
        }
        catch (Exception e)
        {
            try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
            Cursor coursors = db.rawQuery("SELECT * FROM login",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                guid=coursors.getString(coursors.getColumnIndex("guid"));
                hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
            }
            try {	if (db.isOpen()) {	db.close();if(!coursors.isClosed())
                coursors.close();}}	catch (Exception ex){	}
        }
        try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
        Cursor coursors = db.rawQuery("SELECT * FROM messages WHERE IsDelete='0'",null);
        if(coursors.getCount()>0)
        {
            title=new String[coursors.getCount()];
//            content=new String[coursors.getCount()];
            rowID=new Integer[coursors.getCount()];
            imgID=new Integer[coursors.getCount()];
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Title",coursors.getString(coursors.getColumnIndex("Title")));
//                map.put("Content",coursors.getString(coursors.getColumnIndex("Content")));
                map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
                map.put("IsReade",coursors.getString(coursors.getColumnIndex("IsReade")));
                valuse.add(map);
            }
            AdapterMessage dataAdapter=new AdapterMessage(List_Messages.this,valuse);
            lvMessage.setAdapter(dataAdapter);
        }
            try {	if (db.isOpen()) {	db.close();if(!coursors.isClosed())
                coursors.close();}}	catch (Exception ex){	}
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            List_Messages.this.LoadActivity(MainActivity.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            List_Messages.this.startActivity(intent);
        }
    }
