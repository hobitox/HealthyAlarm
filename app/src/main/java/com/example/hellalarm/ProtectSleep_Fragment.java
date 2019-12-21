package com.example.hellalarm;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class ProtectSleep_Fragment extends Fragment  {
    TimePicker timePicker;

    RadioGroup radioGroup;
    RadioButton rdbtn1;
    RadioButton rdbtn2;
    RadioButton rdbtn3;
    Button add;
    int hour1;
    int hour2;
    int hour3;
    int mins1;
    int mins2;
    int mins3;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.protectsleep_fragment,container,false);

        timePicker = v.findViewById(R.id.timepick2);
        timePicker.setIs24HourView(true);
        timePicker.setIs24HourView(DateFormat.is24HourFormat((getActivity())));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                changetime(hourOfDay,minute);
            }
        });
        radioGroup=v.findViewById(R.id.radiogrp);
        rdbtn1=v.findViewById(R.id.radioButton1);
        rdbtn2=v.findViewById(R.id.radioButton2);
        rdbtn3=v.findViewById(R.id.radioButton3);
        changetime(timePicker.getCurrentHour(),timePicker.getCurrentMinute());
        add=v.findViewById(R.id.btnthem_protectsleep);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdbtn1.isChecked()==false && rdbtn2.isChecked()==false && rdbtn3.isChecked()==false){
                    Toast.makeText(v.getContext(),"Please Choose Time To Wake Up!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(rdbtn1.isChecked()){
                        ((MainActivity)getActivity()).additem(hour1,mins1, "wake up",0,false,false,false,false,false,false,false,true,false,0);
                    }
                    else if(rdbtn2.isChecked()){
                        ((MainActivity)getActivity()).additem(hour2,mins2, "wake up",0,false,false,false,false,false,false,false,true,false,0);
                    }
                    else if(rdbtn3.isChecked()){
                        ((MainActivity)getActivity()).additem(hour3,mins3, "wake up",0,false,false,false,false,false,false,false,true,false,0);
                    }
                    ((MainActivity)getActivity()).bottomNav.setSelectedItemId(R.id.basic_alarm);
                }
            }
        });
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void changetime(int hourOfDay, int minute){
        Calendar c= Calendar.getInstance();

        hour1=hourOfDay;
        mins1=minute;
        hour1 +=4;
        mins1+=30;
        if(mins1>=60){
            mins1-=60;
            hour1+=1;
        }
        if(hour1>=24){
            hour1 -=24;
        }

        c.set(Calendar.HOUR_OF_DAY,hour1);
        c.set(Calendar.MINUTE,mins1);
        c.set(Calendar.SECOND,0);

        String timetext = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.getTime());
        String timesleep="\n4.5 hours sleep";

        Spannable colorSpan = new SpannableString(timetext+timesleep);
        colorSpan.setSpan(new RelativeSizeSpan(0.5f), timetext.length(),colorSpan.length(), 0); // set size
        colorSpan.setSpan(new ForegroundColorSpan(Color.GRAY), timetext.length(), colorSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        rdbtn1.setText(colorSpan);


        hour2=hourOfDay;
        mins2=minute;
        hour2 +=6;
        if(hour2>=24){
            hour2 -=24;
        }

        c.set(Calendar.HOUR_OF_DAY,hour2);
        c.set(Calendar.MINUTE,mins2);
        c.set(Calendar.SECOND,0);

        String timetext2 = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.getTime());
        String timesleep2="\n6 hours sleep";
        colorSpan = new SpannableString(timetext2+timesleep2);
        colorSpan.setSpan(new RelativeSizeSpan(0.5f), timetext2.length(),colorSpan.length(), 0); // set size
        colorSpan.setSpan(new ForegroundColorSpan(Color.GRAY), timetext2.length(), colorSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        rdbtn2.setText(colorSpan);


        hour3=hourOfDay;
        mins3=minute;
        hour3 +=7;
        mins3+=30;
        if(mins3>=60){
            mins3-=60;
            hour3+=1;
        }
        if(hour3>=24){
            hour3 -=24;
        }
        c.set(Calendar.HOUR_OF_DAY,hour3);
        c.set(Calendar.MINUTE,mins3);
        c.set(Calendar.SECOND,0);

        String timetext3 = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.getTime());
        String timesleep3="\n7.5 hours sleep";
        colorSpan = new SpannableString(timetext3+timesleep3);
        colorSpan.setSpan(new RelativeSizeSpan(0.5f), timetext3.length(),colorSpan.length(), 0); // set size
        colorSpan.setSpan(new ForegroundColorSpan(Color.GRAY), timetext3.length(), colorSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        rdbtn3.setText(colorSpan);
    }
}
