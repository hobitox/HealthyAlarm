package com.example.hellalarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Music_beforesSleep extends Fragment {
    private Button btnBack,btnFor;
    private Button btnStep;
    private CheckBox btnPlay;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Runnable runnable;
    private Runnable stopat;
    private Handler handler;
    private EditText time;
    private TextView timeMusicRun;
    private TextView timeMusicLength;
    private int timetostop;
    private  int positionMusic;
    private int music;
    private ArrayList<MediaPlayer> mediaPlayerArrayList;
    private RecyclerView listMusic;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onResume() {
        super.onResume();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.music_fragment,container,false);

        btnPlay = v.findViewById(R.id.btnPlay);
        btnBack = v.findViewById(R.id.btnBack);
        btnFor = v.findViewById(R.id.btnFor);
        handler = new android.os.Handler();
        seekBar = v.findViewById(R.id.seekbar);
        time = v.findViewById(R.id.timetostop);
        timeMusicRun = v.findViewById(R.id.textViewMusicLength);
        timeMusicLength = v.findViewById(R.id.textMusicTime);
        btnStep = v.findViewById(R.id.btnStep);


        mediaPlayer = MediaPlayer.create(v.getContext(),R.raw.nhacchuong1);
        listMusic = v.findViewById(R.id.listMusic);
        listMusic.setHasFixedSize(true);
        mediaPlayerArrayList = new ArrayList<>();
        listMusic.setLayoutManager(new LinearLayoutManager(getActivity()));


        mediaPlayerArrayList.add(mediaPlayer);
        mediaPlayerArrayList.add(MediaPlayer.create(v.getContext(),R.raw.nhacchuong2));
        mAdapter=new SoundAdapter(mediaPlayerArrayList,getActivity());
        listMusic.setAdapter(mAdapter);

        timetostop=0;


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                positionMusic = intent.getExtras().getInt("posM");
            }
        };
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("Select Music"));
        //getActivity().registerReceiver(broadcastReceiver,new IntentFilter("Select Music"));
        Spinner spinner = v.findViewById(R.id.sound_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.Sound_Relax, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                music = position;
                switch (music){
                    case 0:
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer = MediaPlayer.create(view.getContext(),R.raw.nhacchuong1);
                            mediaPlayer.start();
                            mediaPlayer.setLooping(true);
                        } else {
                            mediaPlayer = MediaPlayer.create(view.getContext(),R.raw.nhacchuong1);
                        }


                        break;
                    case 1:
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer = MediaPlayer.create(view.getContext(),R.raw.nhacchuong2);
                            mediaPlayer.start();
                            mediaPlayer.setLooping(true);
                        } else {
                            mediaPlayer = MediaPlayer.create(view.getContext(),R.raw.nhacchuong2);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetStepDialog bottomSheetDialog = new BottomSheetStepDialog();
                bottomSheetDialog.show(getActivity().getSupportFragmentManager(),"ssss");
            }
        });

        btnPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getActivity(),"Selected Music OK" +positionMusic,Toast.LENGTH_SHORT).show();
                if (buttonView.isChecked())
                {
                    if (mediaPlayerArrayList.get(positionMusic).isPlaying()) {

                        mediaPlayerArrayList.get(positionMusic).pause();
                    }
                }
                else {
                    int duration = mediaPlayerArrayList.get(positionMusic).getDuration();
                    String Musiclength = String.format("%02d : %02d ",
                            TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
                    timeMusicLength.setText(Musiclength);
                    mediaPlayerArrayList.get(positionMusic).start();
                    mediaPlayerArrayList.get(positionMusic).setLooping(true);

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
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
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
                if(fromUser){
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
            }
        };
        return v;
    }

    private void changeSeekbar() {

        seekBar.setProgress(mediaPlayerArrayList.get(positionMusic).getCurrentPosition());
        int duration = mediaPlayerArrayList.get(positionMusic).getCurrentPosition();
        String Musiclength = String.format("%02d : %02d ",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        timeMusicRun.setText(Musiclength);
        if(mediaPlayerArrayList.get(positionMusic).isPlaying()){
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
