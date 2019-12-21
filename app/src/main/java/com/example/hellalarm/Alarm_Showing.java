package com.example.hellalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Alarm_Showing extends AppCompatActivity {
    private int curStep=0;
    private int stepCount=0;

    boolean first_num=false;
    TextView stepcount_view;
    SensorManager sensorManager;
    SensorEventListener stepDetector;
    Sensor sensor;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm__showing);
        stepcount_view=findViewById(R.id.stepcount);
        first_num=false;
        progressBar=findViewById(R.id.progress_countstep);
        progressBar.setMax(15);
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event != null){
                    if(first_num==false){
                        stepCount= (int) event.values[0];
                        first_num=true;
                    }
                    curStep = (int) event.values[0];
                   stepcount_view.setText(String.valueOf(curStep- stepCount));

                   progressBar.setProgress(curStep-stepCount);

                   if((curStep- stepCount)>= 15)
                   {
                       //sensorManager.unregisterListener((SensorEventListener) Alarm_Showing.this);
                       Intent intent=new Intent(getBaseContext(),AlarmReceiver.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.putExtra("extra","off");
                       sendBroadcast(intent);
                       finish();
                   }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };


        Button start= findViewById(R.id.Startcountstep);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.registerListener(stepDetector, sensor, sensorManager.SENSOR_DELAY_FASTEST);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(stepDetector,sensor);
    }
}
