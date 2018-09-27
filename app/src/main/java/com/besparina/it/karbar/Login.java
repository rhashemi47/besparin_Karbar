package com.besparina.it.karbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class Login extends Activity {
	Button btnEnter;
	Button btnSignUp;
//	ImageView imgLogo;
	GifDrawable gifFromResource;
	GifImageView gifImageView;
	EditText etPhoneNumber;
	DatabaseHelper dbh;
	SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");//set font for page
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remive page title
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
		btnEnter=(Button)findViewById(R.id.btnEnter);
		btnSignUp=(Button)findViewById(R.id.btnSignUp);
        etPhoneNumber=(EditText)findViewById(R.id.etPhoneNumber);
//        imgLogo=(ImageView)findViewById(R.id.imgLogo);
        etPhoneNumber.setTypeface(FontMitra);
		btnSignUp.setTypeface(FontMitra);
		btnEnter.setTypeface(FontMitra);
		gifImageView=(GifImageView)findViewById(R.id.gifImageView);
		try {
			gifFromResource = new GifDrawable( getResources(), R.drawable.logouserlogingif );
		} catch (IOException e) {
			e.printStackTrace();
		}
		gifFromResource.setLoopCount(1);
		gifImageView.setImageDrawable(gifFromResource);
////		Glide.with(this).load(R.drawable.sample_gif).into(imgLogo);
//		GifView pGif = (GifView) findViewById(R.id.logoGif);
//		pGif.setImageResource(R.drawable.sample_gif);
		btnEnter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(Login.this, "برای استفاده از امکانات بسپارینا باید ثبت نام کنید", Toast.LENGTH_LONG).show();
				LoadActivity(MainMenu.class,"karbarCode","0");
			}
		});
		btnSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String Phone=etPhoneNumber.getText().toString();
				if(Phone.compareTo("")!=0) {
					InternetConnection ic = new InternetConnection(getApplicationContext());
					if (ic.isConnectingToInternet()) {
						String query = null;
						db = dbh.getWritableDatabase();
						query = "INSERT INTO Profile (Mobile) VALUES ('" + etPhoneNumber.getText().toString() + "')";
						db.execSQL(query);
						db.close();
						SendAcceptCode sendCode = new SendAcceptCode(Login.this, etPhoneNumber.getText().toString(), "1");
						sendCode.AsyncExecute();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "لطفا شماره همراه خود را وارد نمایید.", Toast.LENGTH_LONG).show();
				}
			}
		});
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		Login.this.startActivity(intent);
	}
	private void ExitApplication()
	{
		//Exit All Activity And Kill Application
		AlertDialog.Builder alertbox = new AlertDialog.Builder(Login.this);
		// set the message to display
		alertbox.setMessage("آیا می خواهید از برنامه خارج شوید ؟");

		// set a negative/no button and create a listener
		alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});

		// set a positive/yes button and create a listener
		alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {
				//Declare Object From Get Internet Connection Status For Check Internet Status
				Intent startMain = new Intent(Intent.ACTION_MAIN);


				startMain.addCategory(Intent.CATEGORY_HOME);

//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

				startActivity(startMain);

				finish();
				arg0.dismiss();

			}
		});

		alertbox.show();
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
		if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
			ExitApplication();
		}
		return super.onKeyDown( keyCode, event );
	}

}


