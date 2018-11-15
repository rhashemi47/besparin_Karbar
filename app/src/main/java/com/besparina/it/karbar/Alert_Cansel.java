package com.besparina.it.karbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hashemi on 08/04/2018.
 */

public class Alert_Cansel extends Dialog implements View.OnClickListener {


    private final String karbarCode;
    private final String OrderCode;
    public Activity activity;
    public TextView yes, no;
//    private final Alert_Cansel.OnTimeSetListener mCallback;
    private EditText etCansel;
    private CheckBox chb1,chb2,chb3,chb4,chb5;
    private LinearLayout LinearDescription;

    public Alert_Cansel(Activity activity, String karbarCode,String OrderCode) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.karbarCode = karbarCode;
        this.OrderCode = OrderCode;
//        this.mCallback = mCallback;
    }
//    public interface OnTimeSetListener {
//
//        /**
//         * @param hourOfDay The hour that was set.
//         * @param minute The minute that was set.
//         */
//        void onTimeSet(String hourOfDay, String minute);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cansel);
        yes = (TextView) findViewById(R.id.btnAccept);
        no = (TextView) findViewById(R.id.btnCansel);
        etCansel= (EditText) findViewById(R.id.etCansel);
        LinearDescription= (LinearLayout) findViewById(R.id.LinearDescription);
        chb1= (CheckBox) findViewById(R.id.chb1);
        chb2= (CheckBox) findViewById(R.id.chb2);
        chb3= (CheckBox) findViewById(R.id.chb3);
        chb4= (CheckBox) findViewById(R.id.chb4);
        chb5= (CheckBox) findViewById(R.id.chb5);
        chb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    LinearDescription.setVisibility(View.VISIBLE);
                }
                else {
                    LinearDescription.setVisibility(View.GONE);
                }
            }
});
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        boolean NoError=true;
        switch (v.getId()) {
            case R.id.btnAccept:
                String description="";
                if(chb1.isChecked())
                {
                    if(description.length()<=0) {
                        description = description + this.activity.getResources().getString(R.string.Do_not_go_all_the_way);
                    }
                    else
                    {
                        description = description +","+ this.activity.getResources().getString(R.string.Do_not_go_all_the_way);
                    }
                }
                if(chb2.isChecked())
                {
                    if(description.length()<=0) {
                        description=description+this.activity.getResources().getString(R.string.Refused_hamyar);
                    }
                    else
                    {
                        description=description+","+this.activity.getResources().getString(R.string.Refused_hamyar);
                    }
                }
                if(chb3.isChecked())
                {
                    if(description.length()<=0) {
                        description=description+this.activity.getResources().getString(R.string.disagreement_Hamyar);
                    }
                    else
                    {
                        description=description+","+this.activity.getResources().getString(R.string.disagreement_Hamyar);
                    }
                }
                if(chb4.isChecked())
                {
                    if(description.length()<=0) {
                        description=description+this.activity.getResources().getString(R.string.The_cost_of_service_is_high);
                    }
                    else
                    {
                        description=description+","+this.activity.getResources().getString(R.string.The_cost_of_service_is_high);
                    }

                }
                if(chb5.isChecked())
                {
                    if(etCansel.getText().toString().length()>0) {
                        if (description.length() <= 0) {
                            description = description + etCansel.getText().toString();
                        } else {
                            description = description + "," + etCansel.getText().toString();
                        }
                    }
                    else
                    {
                        NoError=false;
                        Toast.makeText(this.activity,"فیلد توضیحات خالی است",Toast.LENGTH_LONG).show();
                    }

                }
                if(description.length()>0 && NoError) {
                    SyncCanselServices syncCanselServices = new SyncCanselServices(this.activity,
                            karbarCode,
                            OrderCode,
                            description);
                    syncCanselServices.AsyncExecute();
                }
                else
                {
                    Toast.makeText(this.activity,"علت انصراف را اعلام فرمایید",Toast.LENGTH_LONG).show();
                }
//                mCallback.onTimeSet(PersianDigitConverter.PerisanNumber(hour),
//                        PersianDigitConverter.PerisanNumber(min));
                break;
            case R.id.btnCansel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}