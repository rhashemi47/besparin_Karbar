package com.besparina.it.karbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenu extends AppCompatActivity {
    private String karbarCode;

    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Drawer drawer = null;
    private String countMessage;
//    private android.support.v7.widget.GridLayout gridlayout;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    protected void onResume() {

        super.onResume();
        try
        {
            String status="0";
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
        }
        catch(Exception e)
        {
            throw new Error("Error Opne Activity");
        }
        //startService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        faceh = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");
        btnOrder = (Button) findViewById(R.id.btnOrderBottom);
        btnAcceptOrder = (Button) findViewById(R.id.btnAcceptOrderBottom);
        btncredite = (Button) findViewById(R.id.btncrediteBottom);
        btnServiceEmergency=(Button)findViewById(R.id.btnServiceEmergency);

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
        db.close();
        db = dbh.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM OrdersService WHERE Status ='1'", null);
        if (cursor.getCount() > 0) {
            countOrder = String.valueOf(cursor.getCount());
        }
        db.close();
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
            db.close();
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
                    db.close();
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
                    db.close();
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
            db.close();
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

//*******************************************************************************************************************
        db = dbh.getReadableDatabase();
        Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                "LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'", null);
        if (cursor2.getCount() > 0) {
            btnOrder.setText("درخواست ها( " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount()))+")");
        }
        cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
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
        btnServiceEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MainMenu.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainMenu.this, android.Manifest.permission.CALL_PHONE))
                    {
                        ActivityCompat.requestPermissions(MainMenu.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(MainMenu.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
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
        //****************************************************************************************
        CreateMenu(toolbar);
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
                stopService(new Intent(getBaseContext(), ServiceGetLocation.class));
                stopService(new Intent(getBaseContext(), ServiceGetServiceSaved.class));
                stopService(new Intent(getBaseContext(), ServiceGetServicesAndServiceDetails.class));
                stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
                stopService(new Intent(getBaseContext(), ServiceSyncMessage.class));
                stopService(new Intent(getBaseContext(), ServiceGetPerFactor.class));
                stopService(new Intent(getBaseContext(), ServiceGetServiceVisit.class));
                stopService(new Intent(getBaseContext(), ServiceGetStateAndCity.class));
                db = dbh.getWritableDatabase();
                db.execSQL("DELETE FROM address");
                db.execSQL("DELETE FROM AmountCredit");
                db.execSQL("DELETE FROM android_metadata");
                db.execSQL("DELETE FROM Arts");
                db.execSQL("DELETE FROM BsFaktorUserDetailes");
                db.execSQL("DELETE FROM BsFaktorUsersHead");
//                db.execSQL("DELETE FROM City");
                db.execSQL("DELETE FROM credits");
                db.execSQL("DELETE FROM DateTB");
                db.execSQL("DELETE FROM FieldofEducation");
//                db.execSQL("DELETE FROM Grade");
                db.execSQL("DELETE FROM Hamyar");
                db.execSQL("DELETE FROM InfoHamyar");
                db.execSQL("DELETE FROM Language");
                db.execSQL("DELETE FROM login");
                db.execSQL("DELETE FROM messages");
                db.execSQL("DELETE FROM OrdersService");
                db.execSQL("DELETE FROM Profile");
//                db.execSQL("DELETE FROM services");
//                db.execSQL("DELETE FROM servicesdetails");
//                db.execSQL("DELETE FROM Slider");
                db.execSQL("DELETE FROM sqlite_sequence");
                db.execSQL("DELETE FROM State");
//                db.execSQL("DELETE FROM Unit");
                db.execSQL("DELETE FROM UpdateApp");
                db.execSQL("DELETE FROM visit");
                db.close();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CreateMenu(Toolbar toolbar) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
        String name = "";
        String family = "";
        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
        if (coursors.getCount() > 0) {
            coursors.moveToNext();
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Name")).compareTo("null")!=0){
                    name = coursors.getString(coursors.getColumnIndex("Name"));
                }
                else
                {
                    name = "کاربر";
                }

            }
            catch (Exception ex){
                name = "کاربر";
            }
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Fam")).compareTo("null")!=0){
                    family = coursors.getString(coursors.getColumnIndex("Fam"));
                }
                else
                {
                    family = "مهمان";
                }

            }
            catch (Exception ex){
                family = "مهمان";
            }
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Pic")).compareTo("null")!=0){
                    bmp = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
                }
                else
                {
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
                }

            }
            catch (Exception ex){
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
            }
        }
        else
        {
            name = "کاربر";
            family = "مهمان";
        }
        db.close();
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
//                        return false;
//                    }
//                })
                .build();
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
                        new SecondaryDrawerItem().withName(R.string.Logout).withIcon(R.drawable.logout).withSelectable(false)
                        // new SecondaryDrawerItem().withName(R.string.Credits).withIcon(R.drawable.job).withBadge(countOrder).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
//                        new SecondaryDrawerItem().withName(R.string.GiftBank).withIcon(R.drawable.gift).withSelectable(false).withEnabled(IsActive),
                        // new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
//                        new SecondaryDrawerItem().withName(R.string.Yourcommitment).withIcon(R.drawable.yourcommitment).withSelectable(false),
//                        new SecondaryDrawerItem().withName(R.string.Ourcommitment).withIcon(R.drawable.ourcommitment).withSelectable(false),
                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),


                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
//                        new SecondaryDrawerItem().withName(R.string.Help).withIcon(R.drawable.help).withSelectable(false),

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
                                db.close();
                                break;
                            case 2:
                                db = dbh.getReadableDatabase();
                                Cursor c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    Cursor creditCussor=db.rawQuery("SELECT * FROM credits",null);
                                            if(creditCussor.getCount()>0) {
                                                LoadActivity(Credit.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                            }
                                            else
                                            {
                                                SyncGettUserCreditHistory syncGettUserCreditHistory =new SyncGettUserCreditHistory(MainMenu.this,c.getString(c.getColumnIndex("karbarCode")),"0");
                                                syncGettUserCreditHistory.AsyncExecute();
                                            }

                                }
                                db.close();
                                break;
                            case 3:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM Profile", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    sharecode(c.getString(c.getColumnIndex("karbarCodeForReagent")));
                                   // LoadActivity(GiftBank.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                db.close();
                                break;
//                            case 4:
//                                db = dbh.getReadableDatabase();
//                                c = db.rawQuery("SELECT * FROM login", null);
//                                if (c.getCount() > 0) {
//                                    c.moveToNext();
//
//                                    LoadActivity(GiftBank.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
//                                }
//                                db.close();
//                                break;
                            case 4:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();
                                    String QueryCustom;
                                    QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                                            "LEFT JOIN " +
                                            "Servicesdetails ON " +
                                            "Servicesdetails.code=OrdersService.ServiceDetaileCode";
                                    LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
                                    //LoadActivity(History.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                db.close();
                                break;
                            case 5:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();

                                    LoadActivity(List_Messages.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                db.close();
                                break;
                            case 6:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();

                                    LoadActivity(Contact.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                db.close();
                                break;
                            case 7:
//                                Toast.makeText(MainMenu.this, "تنظیمات", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(MainMenu.this);
                                // set the message to display
                                alertbox.setMessage("قوانین و مقررات");

                                // set a negative/no button and create a listener
                                alertbox.setPositiveButton("تعهدات ما", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        db = dbh.getReadableDatabase();
                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
                                        if (c.getCount() > 0) {
                                            c.moveToNext();

                                            LoadActivity(OurCommitment.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                        }
                                        db.close();
                                        arg0.dismiss();
                                    }
                                });

                                // set a positive/yes button and create a listener
                                alertbox.setNegativeButton("تعهدات شما", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //Declare Object From Get Internet Connection Status For Check Internet Status
                                        db = dbh.getReadableDatabase();
                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
                                        if (c.getCount() > 0) {
                                            c.moveToNext();

                                            LoadActivity(YourCommitment.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                        }
                                        db.close();
                                        arg0.dismiss();

                                    }
                                });
                                alertbox.show();
                                break;
                            case 8:
                                db = dbh.getReadableDatabase();
                                c = db.rawQuery("SELECT * FROM login", null);
                                if (c.getCount() > 0) {
                                    c.moveToNext();

                                    LoadActivity(About.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                                }
                                db.close();
                                break;
//                            case 10:
//
//                                db = dbh.getReadableDatabase();
//                                c = db.rawQuery("SELECT * FROM login", null);
//                                if (c.getCount() > 0) {
//                                    c.moveToNext();
//
//                                    LoadActivity(Help.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
//                                }
//                                break;
//                            case 11:
////                                Toast.makeText(MainMenu.this, "خروج از برنامه", Toast.LENGTH_SHORT).show();
//                                ExitApplication();
//                                break;
                            case 9:
//                                Toast.makeText(MainMenu.this, "خروج از کاربری", Toast.LENGTH_SHORT).show();
                                Logout();
                                break;
//                            case 14:
//                                Toast.makeText(MainMenu.this, "تلگرام", Toast.LENGTH_SHORT).show();
//                                break;
//                            case 15:
//                                Toast.makeText(MainMenu.this, "اینستاگرام", Toast.LENGTH_SHORT).show();
//                                break;
                        }
                        return true;
                    }
                })
                .build();
    }

//    private void ExitApplication() {
//        //Exit All Activity And Kill Application
//        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
//        // set the message to display
//        alertbox.setMessage("آیا می خواهید از برنامه خارج شوید ؟");
//
//        // set a negative/no button and create a listener
//        alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
//            // do something when the button is clicked
//            public void onClick(DialogInterface arg0, int arg1) {
//                arg0.dismiss();
//            }
//        });
//
//        // set a positive/yes button and create a listener
//        alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
//            // do something when the button is clicked
//            public void onClick(DialogInterface arg0, int arg1) {
//                //Declare Object From Get Internet Connection Status For Check Internet Status
//                Intent startMain = new Intent(Intent.ACTION_MAIN);
//
//                startMain.addCategory(Intent.CATEGORY_HOME);
//
//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                startActivity(startMain);
//
//                finish();
//
//                arg0.dismiss();
//
//            }
//        });
//
//        alertbox.show();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //ExitApplication();
        }

        return super.onKeyDown(keyCode, event);
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
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
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
                    Toast.makeText(MainMenu.this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    void sharecode(String shareStr)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "بسپارینا" + "\n"+"کد معرف: "+shareStr+"\n"+"آدرس سایت: " + PublicVariable.site;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "عنوان");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری با"));
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            Intent startMain = new Intent(Intent.ACTION_MAIN);


            startMain.addCategory(Intent.CATEGORY_HOME);

//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(startMain);

            finish();
            super.onBackPressed();
            return;
        }
        drawer.closeDrawer();
        this.doubleBackToExitPressedOnce = true;

//        Snackbar.make(findViewById(R.id.background_place_holder_image_view), "Please click BACK again to exit", Snackbar.LENGTH_SHORT).show();
        Toast.makeText(this, "جهت خروج از برنامه مجددا دکمه برگشت را لمس کنید", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    public void Check_Login(String karbarCode)
    {
        if (karbarCode == null) {

            Cursor cursor;
            db = dbh.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM login", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                String Result = cursor.getString(cursor.getColumnIndex("islogin"));
                if (Result.compareTo("0") == 0)
                {
                    LoadActivity(Login.class, "karbarCode", "0");
                }
                else
                {
                    StartServiceApp();
                }
            }
            else
            {
                LoadActivity(Login.class, "karbarCode", "0");
            }
        } else if (karbarCode.compareTo("0") == 0) {
            IsActive = false;
            StartServiceApp();
        }
        else
        {
            StartServiceApp();
        }
        db.close();
    }
    public void StartServiceApp()
    {
        startService(new Intent(getBaseContext(), ServiceGetServiceSaved.class));
        startService(new Intent(getBaseContext(), ServiceGetLocation.class));
        startService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
        startService(new Intent(getBaseContext(), ServiceSyncMessage.class));
        startService(new Intent(getBaseContext(), ServiceGetServicesAndServiceDetails.class));
        startService(new Intent(getBaseContext(), ServiceGetPerFactor.class));
        startService(new Intent(getBaseContext(), ServiceGetServiceVisit.class));
        startService(new Intent(getBaseContext(), ServiceGetStateAndCity.class));
    }
}
