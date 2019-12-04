package com.example.hellalarm;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class ProtectSleep_Fragment extends Fragment implements AdapterView.OnItemSelectedListener{
    TimePicker timePicker;
    TextView label;
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
    int Sound;

    private SoundPool soundPool;
    private int sound1, sound2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.protectsleep_fragment,container,false);
        label = v.findViewById(R.id.label_protectsleep);
        timePicker = v.findViewById(R.id.timepick2);
        timePicker.setIs24HourView(DateFormat.is24HourFormat((getActivity())));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
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

        Spinner spinner = v.findViewById(R.id.sound_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.Sound, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdbtn1.isChecked()==false && rdbtn2.isChecked()==false && rdbtn3.isChecked()==false){
                    Toast.makeText(v.getContext(),"Please Choose Time To Wake Up!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(rdbtn1.isChecked()){
                        ((MainActivity)getActivity()).additem(hour1,mins1, label.getText(),Sound,false,false,false,false,false,false,false,true,false,0);
                    }
                    else if(rdbtn2.isChecked()){
                        ((MainActivity)getActivity()).additem(hour2,mins2, label.getText(),Sound,false,false,false,false,false,false,false,true,false,0);
                    }
                    else if(rdbtn3.isChecked()){
                        ((MainActivity)getActivity()).additem(hour3,mins3, label.getText(),Sound,false,false,false,false,false,false,false,true,false,0);
                    }
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new BasicAlarm_Fragment()).commit();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        sound1 = soundPool.load(v.getContext(), R.raw.nhacchuong1, 1);
        sound2 = soundPool.load(v.getContext(), R.raw.nhacchuong2, 1);
        Button playandpause= v.findViewById(R.id.playandpause2);
        playandpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Sound) {
                    case 0:
                        soundPool.play(sound1, 1, 1, 0, 0, 1);
                        break;
                    case 1:
                        soundPool.play(sound2, 1, 1, 0, 0, 1);
                        break;
                }
            }
        });
        return v;
    }

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

        rdbtn1.setText(timetext);

        hour2=hourOfDay;
        mins2=minute;
        hour2 +=6;
        if(hour2>=24){
            hour2 -=24;
        }
        c.set(Calendar.HOUR_OF_DAY,hour2);
        c.set(Calendar.MINUTE,mins2);
        c.set(Calendar.SECOND,0);

        timetext = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.getTime());

        rdbtn2.setText(timetext);

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

        timetext = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.getTime());

        rdbtn3.setText(timetext);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Sound = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
