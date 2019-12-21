package com.example.hellalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatDialogFragment;

public class Alarm_Dialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    int TimePicker_hour;
    int Timepicker_minute;
    TextView textView;
    TimePicker  timePicker;
    CheckBox MON;
    CheckBox TUES;
    CheckBox WED;
    CheckBox THURS;
    CheckBox FRI;
    CheckBox SAT;
    CheckBox SUN;
    boolean EDIT=false;
    int Sound;
    int id;

    private SoundPool soundPool;
    private int sound1, sound2;

    private AlarmDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addalarm_dialog,null);

        timePicker= view.findViewById(R.id.timepick);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
        textView=view.findViewById(R.id.label);

        Spinner spinner = view.findViewById(R.id.sound_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.Sound, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        MON=view.findViewById(R.id.checkBox);
        TUES=view.findViewById(R.id.checkBox2);
        WED=view.findViewById(R.id.checkBox3);
        THURS=view.findViewById(R.id.checkBox4);
        FRI=view.findViewById(R.id.checkBox5);
        SAT=view.findViewById(R.id.checkBox6);
        SUN=view.findViewById(R.id.checkBox7);

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            textView.setText(bundle.getString("LABEL"));
            timePicker.setCurrentHour(bundle.getInt("HOUR"));
            timePicker.setCurrentMinute(bundle.getInt("MINUTE"));
            if(bundle.getInt("MON")==1){
                MON.setChecked(true);
            }
            if(bundle.getInt("TUES")==1){
                TUES.setChecked(true);
            }
            if(bundle.getInt("WED")==1){
                WED.setChecked(true);
            }
            if(bundle.getInt("THURS")==1){
                THURS.setChecked(true);
            }
            if(bundle.getInt("FRI")==1){
                FRI.setChecked(true);
            }
            if(bundle.getInt("SAT")==1){
                SAT.setChecked(true);
            }
            if(bundle.getInt("SUN")==1){
                SUN.setChecked(true);
            }

            EDIT=bundle.getBoolean("EDIT");
            id=bundle.getInt("ID");
        }

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean onetime;
                        if(MON.isChecked()==TUES.isChecked()==WED.isChecked()==THURS.isChecked()==FRI.isChecked()
                        ==SAT.isChecked()==SUN.isChecked()==false){
                            onetime=true;
                        }
                        else{
                            onetime=false;
                        }

                        TimePicker_hour= timePicker.getCurrentHour();
                        Timepicker_minute=timePicker.getCurrentMinute();
                        listener.applyTime(TimePicker_hour,Timepicker_minute, textView.getText(), Sound,
                                MON.isChecked(),
                                TUES.isChecked(),
                                WED.isChecked(),
                                THURS.isChecked(),
                                FRI.isChecked(),
                                SAT.isChecked(),
                                SUN.isChecked(),
                                onetime,
                                EDIT,
                                id);
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

        sound1 = soundPool.load(view.getContext(), R.raw.nhacchuong1, 1);
        sound2 = soundPool.load(view.getContext(), R.raw.nhacchuong2, 1);
        Button playandpause= view.findViewById(R.id.playandpause);
        playandpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Sound){
                    case 0:
                        soundPool.play(sound1, 1, 1, 0, 0, 1);
                        break;
                    case 1:
                        soundPool.play(sound2, 1, 1, 0, 0, 1);
                        break;
                }
            }
        });
        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AlarmDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement AlarmDialogListener");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Sound = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface AlarmDialogListener{
        void applyTime(int hourtoset, int minutetoset, CharSequence label,int sound, boolean MON, boolean TUES, boolean WED, boolean THURS, boolean FRI, boolean SAT, boolean SUN, boolean onetime, boolean EDIT, int id);
    }
}