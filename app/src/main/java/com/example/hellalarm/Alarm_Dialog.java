package com.example.hellalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Alarm_Dialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    int TimePicker_hour;
    int Timepicker_minute;
    TextView textView;
    TimePicker  timePicker;
    CheckBox onetime;
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

    private AlarmDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addalarm_dialog,null);

        timePicker= view.findViewById(R.id.timepick);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
        textView =view.findViewById(R.id.label);
        onetime=view.findViewById(R.id.onetime);

        Spinner spinner = view.findViewById(R.id.sound_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.Sound, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        onetime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    MON.setChecked(false);
                    TUES.setChecked(false);
                    WED.setChecked(false);
                    THURS.setChecked(false);
                    FRI.setChecked(false);
                    SAT.setChecked(false);
                    SUN.setChecked(false);

                    MON.setClickable(false);
                    TUES.setClickable(false);
                    WED.setClickable(false);
                    THURS.setClickable(false);
                    FRI.setClickable(false);
                    SAT.setClickable(false);
                    SUN.setClickable(false);
                }
                else{
                    MON.setClickable(true);
                    TUES.setClickable(true);
                    WED.setClickable(true);
                    THURS.setClickable(true);
                    FRI.setClickable(true);
                    SAT.setClickable(true);
                    SUN.setClickable(true);
                }
            }
        });
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
            if(bundle.getInt("ONETIME")==1){
                onetime.setChecked(true);
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
                                onetime.isChecked(),
                                EDIT,
                                id);
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
