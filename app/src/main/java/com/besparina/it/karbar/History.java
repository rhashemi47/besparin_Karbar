	package com.besparina.it.karbar;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Typeface;
    import android.os.Bundle;
    import android.support.annotation.NonNull;

    import android.view.KeyEvent;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.TextView;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class History extends Activity {
        private String karbarCode;

        private TextView tvHistory;
        private ListView lstHistory;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private Button btnOrder;
        private Button btnAcceptOrder;
        private Button btncredite;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.history);
            btnOrder=(Button)findViewById(R.id.btnOrderBottom);
            btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
            btncredite=(Button)findViewById(R.id.btncrediteBottom);
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
            try {
                karbarCode = getIntent().getStringExtra("karbarCode").toString();

            } catch (Exception e) {
                db=dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                for (int i = 0; i < coursors.getCount(); i++) {
                    coursors.moveToNext();

                    karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
                }
                db.close();
            }
            Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
            tvHistory=(TextView)findViewById(R.id.tvHistory);
            tvHistory.setTypeface(FontMitra);
            tvHistory.setTextSize(24);
            lstHistory=(ListView)findViewById(R.id.lstHistory);
            String Query = "UPDATE UpdateApp SET Status='1'";
            db = dbh.getWritableDatabase();
            db.execSQL(Query);
            String query = "SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=OrdersService.ServiceDetaileCode";
            Cursor coursors = db.rawQuery(query, null);
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();
                String Content = "";
                HashMap<String, String> map = new HashMap<String, String>();
                try {
                    Content += coursors.getString(coursors.getColumnIndex("name")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "نام صاحبکار: " + coursors.getString(coursors.getColumnIndex("UserName")) + " " + coursors.getString(coursors.getColumnIndex("UserFamily")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "تاریخ شروع: " + coursors.getString(coursors.getColumnIndex("StartDate")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "تاریخ پایان: " + coursors.getString(coursors.getColumnIndex("EndDate")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "از ساعت: " + coursors.getString(coursors.getColumnIndex("StartTime")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "تا ساعت: " + coursors.getString(coursors.getColumnIndex("EndTime")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("1") == 0) {
                        Content += "خدمت دوره ای: " + "روزانه" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("2") == 0) {
                        Content += "خدمت دوره ای: " + "هفتگی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("3") == 0) {
                        Content += "خدمت دوره ای: " + "هفته در میان" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("PeriodicServices")).toString().compareTo("4") == 0) {
                        Content += "خدمت دوره ای: " + "ماهانه" + "\n";
                    }

                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("MaleCount")).toString().compareTo("0") != 0) {
                        Content += "تعداد همیار مرد: " + coursors.getString(coursors.getColumnIndex("MaleCount")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("FemaleCount")).toString().compareTo("0") != 0) {
                        Content += "تعداد همیار زن: " + coursors.getString(coursors.getColumnIndex("FemaleCount")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("HamyarCount")).toString().compareTo("0") != 0) {
                        Content += "تعداد همیار: " + coursors.getString(coursors.getColumnIndex("HamyarCount")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("EducationTitle")).toString().compareTo("") != 0) {
                        Content += "عنوان آموزش: " + coursors.getString(coursors.getColumnIndex("EducationTitle")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("EducationGrade")).toString().compareTo("0") != 0) {
                        Content += "پایه تحصیلی: " + coursors.getString(coursors.getColumnIndex("EducationGrade")) + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("1") == 0) {
                        Content += "رشته تحصیلی: " + "ابتدایی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("2") == 0) {
                        Content += "رشته تحصیلی: " + "متوسطه اول" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("3") == 0) {
                        Content += "رشته تحصیلی: " + "علوم تجربی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("4") == 0) {
                        Content += "رشته تحصیلی: " + "ریاضی و فیزیک" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("FieldOfStudy")).toString().compareTo("5") == 0) {
                        Content += "رشته تحصیلی: " + "انسانی" + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("0") != 0) {
                        if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("2") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("3") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("4") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("5") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("6") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else if (coursors.getString(coursors.getColumnIndex("ArtField")).toString().compareTo("7") == 0) {
                            Content += "رشته هنری: " + "موسیقی" + "\n";
                        } else {
                            Content += "رشته هنری: " + coursors.getString(coursors.getColumnIndex("ArtField")) + "\n";
                        }
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("1") == 0) {
                        Content += "زبان: " + "انگلیسی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("2") == 0) {
                        Content += "زبان: " + "روسی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("3") == 0) {
                        Content += "زبان: " + "آلمانی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("4") == 0) {
                        Content += "زبان: " + "فرانسه" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("5") == 0) {
                        Content += "زبان: " + "ترکی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("Language")).toString().compareTo("6") == 0) {
                        Content += "زبان: " + "عربی" + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("1") == 0) {
                        Content += "جنسیت دانش آموز: " + "زن" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("StudentGender")).toString().compareTo("2") == 0) {
                        Content += "جنسیت دانش آموز: " + "مرد" + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("1") == 0) {
                        Content += "نوع سرویس: " + "روشویی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("CarWashType")).toString().compareTo("2") == 0) {
                        Content += "نوع سرویس: " + "روشویی و توشویی" + "\n";
                    }
                } catch (Exception ex) {
                    //todo
                }
                try {
                    if (coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("1") == 0) {
                        Content += "نوع خودرو: " + "سواری" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("2") == 0) {
                        Content += "نوع سرویس: " + "شاسی و نیم شاسی" + "\n";
                    } else if (coursors.getString(coursors.getColumnIndex("CarType")).toString().compareTo("3") == 0) {
                        Content += "نوع سرویس: " + "ون" + "\n";
                    }

                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "توضیحات: " + coursors.getString(coursors.getColumnIndex("Description")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try {
                    Content += "آدرس: " + coursors.getString(coursors.getColumnIndex("AddressText")) + "\n";
                } catch (Exception ex) {
                    //todo
                }
                try
                {
                    Content += "فوریت: " + ((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0") == 1 ? "عادی" : "فوری"))+ "\n";
                } catch (Exception ex) {
                    //todo
                }
                String StrStatus="";
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
                Content+="وضعیت: "+StrStatus;
                map.put("name",Content);
                map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
                valuse.add(map);
            }
            db.close();
            if(valuse.size()==0)
            {
                lstHistory.setVisibility(View.GONE);
                tvHistory.setVisibility(View.VISIBLE);
                tvHistory.setText("موردی جهت نمایش وجود ندارد");
            }
            else
            {
                lstHistory.setVisibility(View.VISIBLE);
                tvHistory.setVisibility(View.GONE);
                AdapterHistory dataAdapter=new AdapterHistory(this,valuse,karbarCode);
                lstHistory.setAdapter(dataAdapter);
            }

            db=dbh.getReadableDatabase();
            Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'", null);
            if (cursor2.getCount() > 0) {
                btnOrder.setText("درخواست ها: " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount())));
            }
            cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
            if (cursor2.getCount() > 0) {
                btnAcceptOrder.setText("پذیرفته شده ها: " + PersianDigitConverter.PerisanNumber(String.valueOf(cursor2.getCount())));
            }
            cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
            if (cursor2.getCount() > 0) {
                cursor2.moveToNext();
                try {
			String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
			if(splitStr[1].compareTo("00")==0)
			{
				btncredite.setText("اعتبار: " + PersianDigitConverter.PerisanNumber(splitStr[0]));
			}
			else
			{
				btncredite.setText("اعتبار: " + PersianDigitConverter.PerisanNumber(cursor2.getString(cursor2.getColumnIndex("Amount"))));
			}

		} catch (Exception ex) {
			btncredite.setText(PersianDigitConverter.PerisanNumber("اعتبار: " + "0"));
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
            History.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            History.this.startActivity(intent);
        }
        public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
                , String VariableName2, String VariableValue2) {
            Intent intent = new Intent(getApplicationContext(), Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);

            this.startActivity(intent);
        }
    }
