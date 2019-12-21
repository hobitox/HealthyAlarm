package com.example.hellalarm;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Music_beforesSleep extends Fragment {
    private Button btnPlay,btnBack,btnFor;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Runnable runnable;
    private Runnable stopat;
    private Handler handler;
    private EditText time;
    private int timetostop;
    private int music;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.music_fragment, container, false);

        btnPlay = v.findViewById(R.id.btnPlay);
        btnBack = v.findViewById(R.id.btnBack);
        btnFor = v.findViewById(R.id.btnFor);
        handler = new Handler();
        seekBar = v.findViewById(R.id.seekbar);
        time = v.findViewById(R.id.timetostop);

        mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.nhacchuong1);

        timetostop = 0;

        Spinner spinner = v.findViewById(R.id.sound_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.Sound_Relax, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                music = position;
                switch (music) {
                    case 0:
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.nhacchuong1);
                            mediaPlayer.start();
                            mediaPlayer.setLooping(true);
                        } else {
                            mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.nhacchuong1);
                        }


                        break;
                    case 1:
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.nhacchuong2);
                            mediaPlayer.start();
                            mediaPlayer.setLooping(true);
                        } else {
                            mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.nhacchuong2);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setText(">");
                } else {

                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    btnPlay.setText("||");
                    if (!String.valueOf(time.getText()).matches("")) {
                        timetostop = Integer.parseInt(String.valueOf(time.getText()));
                        timetostop = timetostop * 1000 * 60;
                    }
                    changeSeekbar();
                }
            }
        });

        btnFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaPlayer.getDuration());
                changeSeekbar();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        stopat = new Runnable() {
            @Override
            public void run() {
                mediaPlayer.pause();
                btnPlay.setText(">");
            }
        };
        return v;
    }

    private void changeSeekbar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if(mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();
                }
            };
            handler.postDelayed(runnable,0);
            if(timetostop != 0) {
                handler.postDelayed(stopat, timetostop);
            }
        }
    }
}
