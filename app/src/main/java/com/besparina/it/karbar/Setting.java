	package com.besparina.it.karbar;

    import android.app.Activity;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.support.annotation.NonNull;

    import android.view.KeyEvent;
    import android.view.MenuItem;

    import java.io.IOException;

    public class Setting extends Activity {
        private String karbarCode;

        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
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
                Cursor coursors = db.rawQuery("SELECT * FROM login",null);
                for(int i=0;i<coursors.getCount();i++){
                    coursors.moveToNext();

                    karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
                }
            }
            String Query="UPDATE UpdateApp SET Status='1'";
            db=dbh.getWritableDatabase();
            db.execSQL(Query);

    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            Setting.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);

            Setting.this.startActivity(intent);
        }
    }
