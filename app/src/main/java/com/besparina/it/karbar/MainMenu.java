package com.besparina.it.karbar;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenu extends AppCompatActivity {
    private String karbarCode;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap = null;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Drawer drawer = null;
    private String countMessage;
    private CreateOrUpdateTable dbCOrU;
    private GridView GridViewServices;
    private boolean IsActive = true;
    private ArrayList<HashMap<String, String>> valuse;
    private EditText etSearch;
    private ListView lstSearchDetailService;
    private Button btnOrder;
    private Button btnAcceptOrder;
    private Button btncredite;
    private Button btnServiceEmergency;
    private boolean doubleBackToExitPressedOnce = false;
    private Typeface faceh;
    ImageView imageView;
    Custom_ViewFlipper viewFlipper;
    GestureDetector mGestureDetector;
    private String countOrder;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private String AppVersion;
    private JobScheduler jobScheduler_SchaduleServiceGetLocation = null;
    private JobScheduler jobScheduler_SchaduleServiceGetServiceSaved = null;
    private JobScheduler jobScheduler_SchaduleServiceGetPerFactor = null;
    private JobScheduler jobScheduler_SchaduleServiceGetServicesAndServiceDetails = null;
    private JobScheduler jobScheduler_SchaduleServiceGetServiceVisit = null;
    private JobScheduler jobScheduler_SchaduleServiceGetSliderPic = null;
    private JobScheduler jobScheduler_SchaduleServiceGetStateAndCity = null;
    private JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
    private JobScheduler jobScheduler_SchaduleServiceSyncMessage = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void onResume() {

        super.onResume();
        try {
            String status = "0";
            db = dbh.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Profile", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                try {
                    if (cursor.getString(cursor.getColumnIndex("Status")).compareTo("null") != 0) {
                        status = cursor.getString(cursor.getColumnIndex("Status"));
                        if (status.compareTo("0") == 0) {
                            status = "غیرفعال";
                        } else {
                            status = "فعال";
                        }
                    } else {
                        status = "غیرفعال";
                    }

                } catch (Exception ex) {
                    status = "غیرفعال";
                }
            }
            karbarCode = getIntent().getStringExtra("karbarCode");
            Check_Login(karbarCode);
        } catch (Exception e) {
            throw new Error("Error Opne Activity");
        }
        //startService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));

    }
protected  void onStop() {

    super.onStop();
    try {
        String status = "0";
        db = dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Profile", null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            try {
                if (cursor.getString(cursor.getColumnIndex("Status")).compareTo("null") != 0) {
                    status = cursor.getString(cursor.getColumnIndex("Status"));
                    if (status.compareTo("0") == 0) {
                        status = "غیرفعال";
                    } else {
                        status = "فعال";
                    }
                } else {
                    status = "غیرفعال";
                }

            } catch (Exception ex) {
                status = "غیرفعال";
            }
        }
        karbarCode = getIntent().getStringExtra("karbarCode");
        Check_Login(karbarCode);
    } catch (Exception e) {
        throw new Error("Error Opne Activity");
    }
}
protected void onPause() {

    super.onPause();
    try {
        String status = "0";
        db = dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Profile", null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            try {
                if (cursor.getString(cursor.getColumnIndex("Status")).compareTo("null") != 0) {
                    status = cursor.getString(cursor.getColumnIndex("Status"));
                    if (status.compareTo("0") == 0) {
                        status = "غیرفعال";
                    } else {
                        status = "فعال";
                    }
                } else {
                    status = "غیرفعال";
                }

            } catch (Exception ex) {
                status = "غیرفعال";
            }
        }
        karbarCode = getIntent().getStringExtra("karbarCode");
        Check_Login(karbarCode);
    } catch (Exception e) {
        throw new Error("Error Opne Activity");
    }
}
    protected void onDestroy() {

        super.onDestroy();
        try {
            String status = "0";
            db = dbh.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Profile", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                try {
                    if (cursor.getString(cursor.getColumnIndex("Status")).compareTo("null") != 0) {
                        status = cursor.getString(cursor.getColumnIndex("Status"));
                        if (status.compareTo("0") == 0) {
                            status = "غیرفعال";
                        } else {
                            status = "فعال";
                        }
                    } else {
                        status = "غیرفعال";
                    }

                } catch (Exception ex) {
                    status = "غیرفعال";
                }
            }
            karbarCode = getIntent().getStringExtra("karbarCode");
            Check_Login(karbarCode);
        } catch (Exception e) {
            throw new Error("Error Opne Activity");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
//        dbCOrU=new CreateOrUpdateTable(this);
//        if(dbCOrU.isFieldExistTable("address1"))
//        {
//            Toast.makeText(this,"Exist",Toast.LENGTH_LONG).show();
//        }
//        else
//        {
//            Toast.makeText(this,"Not Exist",Toast.LENGTH_LONG).show();
//        }
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        if (version.length() > 0) {
            AppVersion = version;
            WsDownLoadUpdate wsDownLoadUpdate = new WsDownLoadUpdate(MainMenu.this, AppVersion, PublicVariable.LinkFileTextCheckVersion, PublicVariable.DownloadAppUpdateLinkAPK);
            wsDownLoadUpdate.AsyncExecute();
        }
        faceh = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");
        btnOrder = (Button) findViewById(R.id.btnOrderBottom);
        btnAcceptOrder = (Button) findViewById(R.id.btnAcceptOrderBottom);
        btncredite = (Button) findViewById(R.id.btncrediteBottom);
        btnServiceEmergency = (Button) findViewById(R.id.btnServiceEmergency);

        etSearch = (EditText) findViewById(R.id.etSearch);
        lstSearchDetailService = (ListView) findViewById(R.id.lstSearchDetailService);
        GridViewServices = (GridView) findViewById(R.id.GridViewServices);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainMenu.this));
        dbh = new DatabaseHelper(getApplicationContext());
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
        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM messages WHERE IsReade='0' AND IsDelete='0'", null);
        if (coursors.getCount() > 0) {
            countMessage = String.valueOf(coursors.getCount());
        }
        try {
            if (db.isOpen()) {
                db.close();
            }
        } catch (Exception ex) {
        }
        db = dbh.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM OrdersService WHERE Status ='1'", null);
        if (cursor.getCount() > 0) {
            countOrder = String.valueOf(cursor.getCount());
        }
        try {
            if (db.isOpen()) {
                db.close();
            }
        } catch (Exception ex) {
        }
        try {
            karbarCode = getIntent().getStringExtra("karbarCode");
            Check_Login(karbarCode);

        } catch (Exception e) {
            Check_Login(karbarCode);
        }
        //***************************************************************************************************
        lstSearchDetailService.setVisibility(View.GONE);
        GridViewServices.setVisibility(View.VISIBLE);
        //Create Button For Services in Grid View

        db = dbh.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT * FROM services", null);
        if (cursor1.getCount() > 0) {
            valuse = new ArrayList<HashMap<String, String>>();
            for (int x = 0; x < cursor1.getCount(); x++) {
                cursor1.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", cursor1.getString(cursor1.getColumnIndex("servicename")));
                map.put("Code", cursor1.getString(cursor1.getColumnIndex("code")));
                valuse.add(map);
            }
            try {
                if (db.isOpen()) {
                    db.close();
                }
            } catch (Exception ex) {
            }
            AdapterGridServices adapterGridServices = new AdapterGridServices(MainMenu.this, valuse, karbarCode);
            GridViewServices.setAdapter(adapterGridServices);
        }
        //*****************************************************************************************************
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strSeach;
                strSeach = s.toString().trim();
                if (strSeach.length() == 0) {
                    lstSearchDetailService.setVisibility(View.GONE);
                    GridViewServices.setVisibility(View.VISIBLE);
                    //Create Button For Services in Grid View
                    db = dbh.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM services", null);
                    valuse = new ArrayList<HashMap<String, String>>();
                    for (int x = 0; x < cursor.getCount(); x++) {
                        cursor.moveToNext();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", cursor.getString(cursor.getColumnIndex("servicename")));
                        map.put("Code", cursor.getString(cursor.getColumnIndex("code")));
                        valuse.add(map);
                    }
                    try {
                        if (db.isOpen()) {
                            db.close();
                        }
                    } catch (Exception ex) {
                    }
                    AdapterGridServices adapterGridServices = new AdapterGridServices(MainMenu.this, valuse, karbarCode);
                    GridViewServices.setAdapter(adapterGridServices);
                } else {
                    lstSearchDetailService.setVisibility(View.VISIBLE);
                    GridViewServices.setVisibility(View.GONE);
                    valuse = new ArrayList<HashMap<String, String>>();
                    //Create Button For Services in Grid View
                    db = dbh.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM servicesdetails WHERE name LIKE '%" + strSeach + "%'", null);
                    for (int x = 0; x < cursor.getCount(); x++) {
                        cursor.moveToNext();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", cursor.getString(cursor.getColumnIndex("name")));
                        map.put("Code", cursor.getString(cursor.getColumnIndex("code")));
                        valuse.add(map);
                    }
                    try {
                        if (db.isOpen()) {
                            db.close();
                        }
                    } catch (Exception ex) {
                    }
                    AdapterServiceDetails adapterServiceDetails = new AdapterServiceDetails(MainMenu.this, valuse, karbarCode);
                    lstSearchDetailService.setAdapter(adapterServiceDetails);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //********************************************************************Start And Stop Service BackGround

        //**************************************************************************
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        viewFlipper = (Custom_ViewFlipper) findViewById(R.id.vf);
        db = dbh.getReadableDatabase();
        coursors = db.rawQuery("SELECT * FROM Slider", null);
        if (coursors.getCount() > 0) {
            Bitmap bpm[] = new Bitmap[coursors.getCount()];
            String link[] = new String[coursors.getCount()];
            for (int j = 0; j < coursors.getCount(); j++) {

                coursors.moveToNext();
                viewFlipper.setVisibility(View.VISIBLE);
                //slides.add();
                bpm[j] = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
                link[j] = coursors.getString(coursors.getColumnIndex("Link"));
            }
            try {
                if (db.isOpen()) {
                    db.close();
                }
            } catch (Exception ex) {
            }
            int i = 0;
            while (i < bpm.length) {
                imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //ImageLoader.getInstance().displayImage(slides.get(i),imageView);
                imageView.setImageBitmap(bpm[i]);
                imageView.setTag(link[i]);
                viewFlipper.addView(imageView);
                i++;
            }


            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            viewFlipper.setPaintCurrent(paint);
            paint = new Paint();

            paint.setColor(ContextCompat.getColor(this, android.R.color.white));
            viewFlipper.setPaintNormal(paint);

            viewFlipper.setRadius(10);
            viewFlipper.setMargin(5);

            CustomGestureDetector customGestureDetector = new CustomGestureDetector();
            mGestureDetector = new GestureDetector(MainMenu.this, customGestureDetector);

            viewFlipper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mGestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
        } else {
            viewFlipper.setVisibility(View.GONE);
        }
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String QueryCustom;
                QueryCustom = "SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                        "LEFT JOIN " +
                        "Servicesdetails ON " +
                        "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'  ORDER BY CAST(OrdersService.Code AS int) ";
                LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
            }
        });
        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String QueryCustom;
                QueryCustom = "SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                        "LEFT JOIN " +
                        "Servicesdetails ON " +
                        "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (1,2,5,6,7,12,13) ORDER BY CAST(OrdersService.Code AS int) ";
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

                if (ActivityCompat.checkSelfPermission(MainMenu.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainMenu.this, android.Manifest.permission.CALL_PHONE)) {
                        ActivityCompat.requestPermissions(MainMenu.this, new String[]{android.Manifest.permission.CALL_PHONE}, 2);
                    } else {
                        ActivityCompat.requestPermissions(MainMenu.this, new String[]{android.Manifest.permission.CALL_PHONE}, 2);
                    }

                }
                db = dbh.getReadableDatabase();
                Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
                if (cursorPhone.getCount() > 0) {
                    cursorPhone.moveToNext();
                    dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("Tel")));
                }
                try {
                    if (db.isOpen()) {
                        db.close();
                    }
                } catch (Exception ex) {
                }
            }
        });
        //****************************************************************************************
        if (imageBitmap != null) {
            CreateMenu(toolbar, imageBitmap);
        } else {
            CreateMenu(toolbar, null);
        }

        //***************************************************************************************************************************
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
                viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);

                viewFlipper.showNext();
            } else if (e1.getX() < e2.getX()) {
                viewFlipper.setInAnimation(MainMenu.this, R.anim.right_in);
                viewFlipper.setOutAnimation(MainMenu.this, R.anim.right_out);

                viewFlipper.showPrevious();
            }
            viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
            viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public void Logout() {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        // set the message to display
        alertbox.setMessage("آیا می خواهید از کاربری خارج شوید ؟");

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
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    jobScheduler_SchaduleServiceGetLocation.cancelAll();
//                    jobScheduler_SchaduleServiceGetServiceSaved.cancelAll();
//                    jobScheduler_SchaduleServiceGetPerFactor.cancelAll();
//                    jobScheduler_SchaduleServiceGetServicesAndServiceDetails.cancelAll();
//                    jobScheduler_SchaduleServiceGetServiceVisit.cancelAll();
//                    jobScheduler_SchaduleServiceGetSliderPic.cancelAll();
//                    jobScheduler_SchaduleServiceGetStateAndCity.cancelAll();
//                    jobScheduler_SchaduleServiceGetUserServiceStartDate.cancelAll();
//                    jobScheduler_SchaduleServiceSyncMessage.cancelAll();
//                } else {
                    stopService(new Intent(getBaseContext(), ServiceGetLocation.class));
                    stopService(new Intent(getBaseContext(), ServiceGetServiceSaved.class));
                    stopService(new Intent(getBaseContext(), ServiceGetServicesAndServiceDetails.class));
                    stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
                    stopService(new Intent(getBaseContext(), ServiceSyncMessage.class));
                    stopService(new Intent(getBaseContext(), ServiceGetPerFactor.class));
                    stopService(new Intent(getBaseContext(), ServiceGetServiceVisit.class));
                    stopService(new Intent(getBaseContext(), ServiceGetStateAndCity.class));
                    stopService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
//                }
                try {
                    if (!db.isOpen()) {
                        db = dbh.getWritableDatabase();
                    }
                } catch (Exception ex) {
                    db = dbh.getWritableDatabase();
                }
                db.execSQL("DELETE FROM address");
                db.execSQL("DELETE FROM AmountCredit");
                db.execSQL("DELETE FROM android_metadata");
                db.execSQL("DELETE FROM Arts");
                db.execSQL("DELETE FROM BsFaktorUserDetailes");
                db.execSQL("DELETE FROM BsFaktorUsersHead");
                db.execSQL("DELETE FROM credits");
                db.execSQL("DELETE FROM DateTB");
                db.execSQL("DELETE FROM FieldofEducation");
                db.execSQL("DELETE FROM Hamyar");
                db.execSQL("DELETE FROM InfoHamyar");
                db.execSQL("DELETE FROM Language");
                db.execSQL("DELETE FROM login");
                db.execSQL("DELETE FROM messages");
                db.execSQL("DELETE FROM OrdersService");
                db.execSQL("DELETE FROM Profile");
                db.execSQL("DELETE FROM sqlite_sequence");
                db.execSQL("DELETE FROM State");
                db.execSQL("DELETE FROM UpdateApp");
                db.execSQL("DELETE FROM visit");
                try {
                    if (db.isOpen()) {
                        db.close();
                    }
                } catch (Exception ex) {
                }
                Intent startMain = new Intent(MainMenu.this, MainMenu.class);


                startMain.addCategory(Intent.CATEGORY_HOME);

                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(startMain);

                finish();
                arg0.dismiss();
            }
        });
        alertbox.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CreateMenu(Toolbar toolbar, Bitmap bmp) {

        String name = "";
        String family = "";
        boolean isPicNull = false;
        if (bmp == null) {
            isPicNull = true;
        }
        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
        if (coursors.getCount() > 0) {
            coursors.moveToNext();
            try {
                if (coursors.getString(coursors.getColumnIndex("Name")).compareTo("null") != 0) {
                    name = coursors.getString(coursors.getColumnIndex("Name"));
                } else {
                    name = "کاربر";
                }

            } catch (Exception ex) {
                name = "کاربر";
            }
            try {
                if (coursors.getString(coursors.getColumnIndex("Fam")).compareTo("null") != 0) {
                    family = coursors.getString(coursors.getColumnIndex("Fam"));
                } else {
                    family = "مهمان";
                }

            } catch (Exception ex) {
                family = "مهمان";
            }
            try {
                if (isPicNull) {
                    if (coursors.getString(coursors.getColumnIndex("Pic")).compareTo("null") != 0) {
                        bmp = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
                    } else {
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
                    }
                }

            } catch (Exception ex) {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
            }
        } else {
            name = "کاربر";
            family = "مهمان";
        }
        try {
            if (db.isOpen()) {
                db.close();
            }
        } catch (Exception ex) {
        }
        int drawerGravity = Gravity.END;
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            drawerGravity = Gravity.START;
        }

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.menu_header)
                .addProfiles(new ProfileDrawerItem().withName("سلام " + name + " " + family).withIcon(bmp)).withSelectionListEnabledForSingleProfile(false).withTypeface(faceh)
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
//                        take_photo();
//                        return false;
//                    }
//                })
                .withProfileImagesClickable(true).build();
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withDrawerGravity(drawerGravity)
                .withShowDrawerOnFirstLaunch(true)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.Profile).withIcon(R.drawable.profile).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.Credits).withIcon(R.drawable.creditinmenu).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.Invite_friends).withIcon(R.drawable.invit_friend).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.History).withIcon(R.drawable.history).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.Messages).withIcon(R.drawable.messages).withBadge(countMessage).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.TermsـandـConditions).withIcon(R.drawable.terms_and_conditions).withSelectable(false).withEnabled(IsActive),
                        new SecondaryDrawerItem().withName(R.string.About).withIcon(R.drawable.about).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Help).withIcon(R.drawable.help).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.Logout).withIcon(R.drawable.logout).withSelectable(false)
                        // new SecondaryDrawerItem().withName(R.string.Credits).withIcon(R.drawable.job).withBadge(countOrder).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
//                        new SecondaryDrawerItem().withName(R.string.GiftBank).withIcon(R.drawable.gift).withSelectable(false).withEnabled(IsActive),
                        // new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
//                        new SecondaryDrawerItem().withName(R.string.Yourcommitment).withIcon(R.drawable.yourcommitment).withSelectable(false),
//                        new SecondaryDrawerItem().withName(R.string.Ourcommitment).withIcon(R.drawable.ourcommitment).withSelectable(false),
                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),


                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),

                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
                        //new SecondaryDrawerItem().withName(R.string.Exit).withIcon(R.drawable.exit).withSelectable(false),
                )
//                .addStickyDrawerItems(new PrimaryDrawerItem().withName(R.string.RelateUs).withSelectable(false).withEnabled(false),
//                        new PrimaryDrawerItem().withName(R.string.telegram).withIcon(R.drawable.telegram).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.instagram).withIcon(R.drawable.instagram).withSelectable(false))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 1://Profile
                                db = dbh.getReadableDatabase();
                                Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
                                if (coursors.getCount() > 0) {
                                    coursors.moveToNext();
                                    String Status_check = coursors.getString(coursors.getColumnIndex("Status"));
                                    if (Status_check.compareTo("0") == 0) {
                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
                                        if (c.getCount() > 0) {
                                            c.moveToNext();
                                            SyncProfile profile = new SyncProfile(MainMenu.this, c.getString(c.getColumnIndex("karbarCode")), c.getString(c.getColumnIndex("AcceptCode")));
                                            profile.AsyncExecute();
                                        }
                                    } else {
                                        LoadActivity(Profile.class, "karbarCode", karbarCode);
                                    }
                                }
                                try {
                                    if (db.isOpen()) {
                                        db.close();
                                    }
                                } catch (Exception ex) {
                                }
                                break;
                            case 2:
                                db = dbh.getReadableDatabase();
                                Cursor c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    Cursor creditCussor = db.rawQuery("SELECT * FROM credits", null);
                                    if (creditCussor.getCount() > 0) {
                                        LoadActivity(Credit.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                    } else {
                                        SyncGettUserCreditHistory syncGettUserCreditHistory = new SyncGettUserCreditHistory(MainMenu.this, c.getString(c.getColumnIndex("karbarCode")), "0");
                                        syncGettUserCreditHistory.AsyncExecute();
                                    }

                                }
                                try {
                                    if (db.isOpen()) {
                                        db.close();
                                    }
                                } catch (Exception ex) {
                                }
                                break;
                            case 3:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM Profile", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    sharecode(c.getString(c.getColumnIndex("karbarCodeForReagent")));
                                    // LoadActivity(GiftBank.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                try {
                                    if (db.isOpen()) {
                                        db.close();
                                    }
                                } catch (Exception ex) {
                                }
                                break;
//
                            case 4:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    String QueryCustom;
                                    QueryCustom = "SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                                            "LEFT JOIN " +
                                            "Servicesdetails ON " +
                                            "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (3,4,12,13) ORDER BY CAST(OrdersService.Code AS int) ";
                                    LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
                                    //LoadActivity(History.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                try {
                                    if (db.isOpen()) {
                                        db.close();
                                    }
                                } catch (Exception ex) {
                                }
                                break;
                            case 5:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();

                                    LoadActivity(List_Messages.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                try {
                                    if (db.isOpen()) {
                                        db.close();
                                    }
                                } catch (Exception ex) {
                                }
                                break;
                            case 6:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();

                                    LoadActivity(Contact.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                try {
                                    if (db.isOpen()) {
                                        db.close();
                                    }
                                } catch (Exception ex) {
                                }
                                break;
                            case 7:
                                final Dialog dialog = new Dialog(MainMenu.this);
                                dialog.setContentView(R.layout.custome_dialog_role);
                                Button btnOurCommitment = dialog.findViewById(R.id.btnOurCommitment);
                                Button btnYourcommitmentt = dialog.findViewById(R.id.btnYourcommitmentt);
                                Button btnRoleBesparina = dialog.findViewById(R.id.btnRoleBesparina);
                                btnOurCommitment.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent urlCall = new Intent(Intent.ACTION_VIEW);
                                        urlCall.setData(Uri.parse("http://besparina.ir/?page_id=164"));
                                        startActivity(urlCall);
                                        dialog.dismiss();
                                    }
                                });
                                btnYourcommitmentt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent urlCall = new Intent(Intent.ACTION_VIEW);
                                        urlCall.setData(Uri.parse("http://besparina.ir/?page_id=174"));
                                        startActivity(urlCall);
                                        dialog.dismiss();
                                    }
                                });
                                btnRoleBesparina.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent urlCall = new Intent(Intent.ACTION_VIEW);
                                        urlCall.setData(Uri.parse("http://besparina.ir/?page_id=186"));
                                        startActivity(urlCall);
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                break;
                            case 8:
//
                                Intent urlCall1 = new Intent(Intent.ACTION_VIEW);
                                urlCall1.setData(Uri.parse("http://besparina.ir/?page_id=199"));
                                startActivity(urlCall1);
                                break;
                            case 9:
                                Intent urlCall2 = new Intent(Intent.ACTION_VIEW);
                                urlCall2.setData(Uri.parse("http://besparina.ir/?page_id=399&preview=true"));
                                startActivity(urlCall2);
                                break;
                            case 10://
                                Logout();
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }

    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);

        this.startActivity(intent);
    }

    public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
            , String VariableName2, String VariableValue2) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);

        this.startActivity(intent);
    }

    public Bitmap convertToBitmap(String base) {
        Bitmap Bmp = null;
        try {
            byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
            Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
            return Bmp;
        } catch (Exception e) {
            e.printStackTrace();
            return Bmp;
        }
    }

    public void dialContactPhone(String phoneNumber) {
        //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_PERMISSIONS);
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
                            dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("Tel")));
                        }
                        try {
                            if (db.isOpen()) {
                                db.close();
                            }
                        } catch (Exception ex) {
                        }
                    } else {
                        // Permission Denied
                        Toast.makeText(MainMenu.this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
                                .show();
                    }
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    void sharecode(String shareStr) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "بسپارینا" + "\n" + "کد معرف: " + shareStr + "\n" + "آدرس سایت: " + PublicVariable.site;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "عنوان");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری با"));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            if (doubleBackToExitPressedOnce) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);

                startMain.addCategory(Intent.CATEGORY_HOME);

                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(startMain);

                finish();
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "جهت خروج از برنامه مجددا دکمه برگشت را لمس کنید", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void Check_Login(String karbarCode) {
        if (karbarCode == null) {

            Cursor cursor;
            db = dbh.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM login", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                String Result = cursor.getString(cursor.getColumnIndex("islogin"));
                if (Result.compareTo("0") == 0) {
                    LoadActivity(Login.class, "karbarCode", "0");
                } else {
                    StartServiceApp();
                }
            } else {
                LoadActivity(Login.class, "karbarCode", "0");
            }
        } else if (karbarCode.compareTo("0") == 0) {
            IsActive = false;
            StartServiceApp();
        } else {
            StartServiceApp();
        }
        try {
            if (db.isOpen()) {
                db.close();
            }
        } catch (Exception ex) {
        }
    }

    public void StartServiceApp() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            this.startForegroundService(new Intent(this, ServiceGetServiceSaved.class));
//            this.startForegroundService(new Intent(this, ServiceGetLocation.class));
//            this.startForegroundService(new Intent(this, ServiceGetSliderPic.class));
//            this.startForegroundService(new Intent(this, ServiceSyncMessage.class));
//            this.startForegroundService(new Intent(this, ServiceGetServicesAndServiceDetails.class));
//            this.startForegroundService(new Intent(this, ServiceGetPerFactor.class));
//            this.startForegroundService(new Intent(this, ServiceGetServiceVisit.class));
//            this.startForegroundService(new Intent(this, ServiceGetStateAndCity.class));
//            this.startForegroundService(new Intent(this, ServiceGetUserServiceStartDate.class));
//        } else {
            this.startService(new Intent(this, ServiceGetServiceSaved.class));
            this.startService(new Intent(this, ServiceGetLocation.class));
            this.startService(new Intent(this, ServiceGetSliderPic.class));
            this.startService(new Intent(this, ServiceSyncMessage.class));
            this.startService(new Intent(this, ServiceGetServicesAndServiceDetails.class));
            this.startService(new Intent(this, ServiceGetPerFactor.class));
            this.startService(new Intent(this, ServiceGetServiceVisit.class));
            this.startService(new Intent(this, ServiceGetStateAndCity.class));
            this.startService(new Intent(this, ServiceGetUserServiceStartDate.class));
//        }
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
////
//            //*****************************************ServiceGetLocation******************************************
//            ComponentName serviceComponent_SchaduleServiceGetLocation = new ComponentName(getBaseContext(), SchaduleServiceGetLocation.class);
//            JobInfo.Builder builder_SchaduleServiceGetLocation = null;
//            builder_SchaduleServiceGetLocation = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetLocation);
//            builder_SchaduleServiceGetLocation.setMinimumLatency(0); // wait at least
////            builder_SchaduleServiceGetLocation.setOverrideDeadline(1000); // maximum delay
//            builder_SchaduleServiceGetLocation.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetLocation.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetLocation.setRequiresCharging(false); // we don't care if the device is charging or not
////            builder_SchaduleServiceGetLocation.setPeriodic(5000);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetLocation = getBaseContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetLocation.schedule(builder_SchaduleServiceGetLocation.build());
//            //*****************************************ServiceGetServiceSaved******************************************
//            ComponentName serviceComponent_SchaduleServiceGetServiceSaved = new ComponentName(getBaseContext(), SchaduleServiceGetServiceSaved.class);
//            JobInfo.Builder builder_SchaduleServiceGetServiceSaved = null;
//            builder_SchaduleServiceGetServiceSaved = new JobInfo.Builder(1, serviceComponent_SchaduleServiceGetServiceSaved);
//            builder_SchaduleServiceGetServiceSaved.setMinimumLatency(0); // wait at least
////            builder_SchaduleServiceGetServiceSaved.setOverrideDeadline(1000); // maximum delay
//            builder_SchaduleServiceGetServiceSaved.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetServiceSaved.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetServiceSaved.setRequiresCharging(false); // we don't care if the device is charging or not
////            builder_SchaduleServiceGetServiceSaved.setPeriodic(5000);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetServiceSaved = getBaseContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetServiceSaved.schedule(builder_SchaduleServiceGetServiceSaved.build());
//
//            //*****************************************SchaduleServiceGetPerFactor************************************************
//            ComponentName serviceComponent_SchaduleServiceGetPerFactor = new ComponentName(getBaseContext(), SchaduleServiceGetPerFactor.class);
//            JobInfo.Builder builder_SchaduleServiceGetPerFactor = null;
//            builder_SchaduleServiceGetPerFactor = new JobInfo.Builder(2, serviceComponent_SchaduleServiceGetPerFactor);
//            builder_SchaduleServiceGetPerFactor.setMinimumLatency(0); // wait at least
////            builder_SchaduleServiceGetPerFactor.setOverrideDeadline(1000); // maximum delay
//            builder_SchaduleServiceGetPerFactor.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetPerFactor.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetPerFactor.setRequiresCharging(false); // we don't care if the device is charging or not
////            builder_SchaduleServiceGetPerFactor.setPeriodic(5000);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetPerFactor = getBaseContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetPerFactor.schedule(builder_SchaduleServiceGetPerFactor.build());
//
//            //*****************************************SchaduleServiceGetServicesAndServiceDetails******************************************
//            ComponentName serviceComponent_SchaduleServiceGetServicesAndServiceDetails = new ComponentName(getBaseContext(), SchaduleServiceGetServicesAndServiceDetails.class);
//            JobInfo.Builder builder_SchaduleServiceGetServicesAndServiceDetails = null;
//            builder_SchaduleServiceGetServicesAndServiceDetails = new JobInfo.Builder(3, serviceComponent_SchaduleServiceGetServicesAndServiceDetails);
//            builder_SchaduleServiceGetServicesAndServiceDetails.setMinimumLatency(0); // wait at least
////            builder_SchaduleServiceGetServicesAndServiceDetails.setOverrideDeadline(10000); // maximum delay
//            builder_SchaduleServiceGetServicesAndServiceDetails.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetServicesAndServiceDetails.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetServicesAndServiceDetails.setRequiresCharging(false); // we don't care if the device is charging or not
////            builder_SchaduleServiceGetServicesAndServiceDetails.setPeriodic(5000);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetServicesAndServiceDetails = getBaseContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetServicesAndServiceDetails.schedule(builder_SchaduleServiceGetServicesAndServiceDetails.build());
//
//            //*****************************************SchaduleServiceGetServiceVisit******************************************
//            ComponentName serviceComponent_SchaduleServiceGetServiceVisit = new ComponentName(getBaseContext(), SchaduleServiceGetServiceVisit.class);
//            JobInfo.Builder builder_SchaduleServiceGetServiceVisit = null;
//            builder_SchaduleServiceGetServiceVisit = new JobInfo.Builder(4, serviceComponent_SchaduleServiceGetServiceVisit);
//            builder_SchaduleServiceGetServiceVisit.setMinimumLatency(0); // wait at least
////            builder_SchaduleServiceGetServiceVisit.setOverrideDeadline(1000); // maximum delay
//            builder_SchaduleServiceGetServiceVisit.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetServiceVisit.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetServiceVisit.setRequiresCharging(false); // we don't care if the device is charging or not
////            builder_SchaduleServiceGetServiceVisit.setPeriodic(5000);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetServiceVisit = getBaseContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetServiceVisit.schedule(builder_SchaduleServiceGetServiceVisit.build());
//
//            //*****************************************SchaduleServiceGetSliderPic******************************************
//            ComponentName serviceComponent_SchaduleServiceGetSliderPic = new ComponentName(getBaseContext(), SchaduleServiceGetSliderPic.class);
//            JobInfo.Builder builder_SchaduleServiceGetSliderPic = null;
//            builder_SchaduleServiceGetSliderPic = new JobInfo.Builder(5, serviceComponent_SchaduleServiceGetSliderPic);
//            builder_SchaduleServiceGetSliderPic.setMinimumLatency(0); // wait at least
////            builder_SchaduleServiceGetSliderPic.setOverrideDeadline(1000); // maximum delay
//            builder_SchaduleServiceGetSliderPic.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetSliderPic.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetSliderPic.setRequiresCharging(false); // we don't care if the device is charging or not
////            builder_SchaduleServiceGetSliderPic.setPeriodic(5000);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetSliderPic = getBaseContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetSliderPic.schedule(builder_SchaduleServiceGetSliderPic.build());
//
//            //*****************************************SchaduleServiceGetStateAndCity******************************************
//            ComponentName serviceComponent_SchaduleServiceGetStateAndCity = new ComponentName(getBaseContext(), SchaduleServiceGetStateAndCity.class);
//            JobInfo.Builder builder_SchaduleServiceGetStateAndCity = null;
//            builder_SchaduleServiceGetStateAndCity = new JobInfo.Builder(6, serviceComponent_SchaduleServiceGetStateAndCity);
//            builder_SchaduleServiceGetStateAndCity.setMinimumLatency(0); // wait at least
////            builder_SchaduleServiceGetStateAndCity.setOverrideDeadline(1000); // maximum delay
//            builder_SchaduleServiceGetStateAndCity.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetStateAndCity.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetStateAndCity.setRequiresCharging(false); // we don't care if the device is charging or not
////            builder_SchaduleServiceGetStateAndCity.setPeriodic(5000);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetStateAndCity = getBaseContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetStateAndCity.schedule(builder_SchaduleServiceGetStateAndCity.build());
//
//            //*****************************************SchaduleServiceGetStateAndCity******************************************
//            ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(getBaseContext(), SchaduleServiceGetUserServiceStartDate.class);
//            JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
//            builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
//            builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(0); // wait at least
////            builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(1000); // maximum delay
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
////            builder_SchaduleServiceGetUserServiceStartDate.setPeriodic(5000);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceGetUserServiceStartDate = getBaseContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());
//
//            //*****************************************SchaduleServiceSyncMessage******************************************
//            ComponentName serviceComponent_SchaduleServiceSyncMessage = new ComponentName(getBaseContext(), SchaduleServiceSyncMessage.class);
//            JobInfo.Builder builder_SchaduleServiceSyncMessage = null;
//            builder_SchaduleServiceSyncMessage = new JobInfo.Builder(8, serviceComponent_SchaduleServiceSyncMessage);
//            builder_SchaduleServiceSyncMessage.setMinimumLatency(0); // wait at least
////            builder_SchaduleServiceSyncMessage.setOverrideDeadline(1000); // maximum delay
//            builder_SchaduleServiceSyncMessage.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder_SchaduleServiceSyncMessage.setRequiresDeviceIdle(false); // device should be idle
//            builder_SchaduleServiceSyncMessage.setRequiresCharging(false); // we don't care if the device is charging or not
////            builder_SchaduleServiceSyncMessage.setPeriodic(5000);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                jobScheduler_SchaduleServiceSyncMessage = getBaseContext().getSystemService(JobScheduler.class);
//            }
//            jobScheduler_SchaduleServiceSyncMessage.schedule(builder_SchaduleServiceSyncMessage.build());
//        } else {
//            this.startService(new Intent(this, ServiceGetServiceSaved.class));
//            this.startService(new Intent(this, ServiceGetLocation.class));
//            this.startService(new Intent(this, ServiceGetSliderPic.class));
//            this.startService(new Intent(this, ServiceSyncMessage.class));
//            this.startService(new Intent(this, ServiceGetServicesAndServiceDetails.class));
//            this.startService(new Intent(this, ServiceGetPerFactor.class));
//            this.startService(new Intent(this, ServiceGetServiceVisit.class));
//            this.startService(new Intent(this, ServiceGetStateAndCity.class));
//            this.startService(new Intent(this, ServiceGetUserServiceStartDate.class));
//        }
    }
}
