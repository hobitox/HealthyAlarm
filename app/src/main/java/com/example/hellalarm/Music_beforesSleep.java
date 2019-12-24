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
import android.widget.TimePicker;
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
    private CheckBox btnPlay;
    private SeekBar seekBar;
    private Runnable runnable;
    private Runnable stopat;
    private Handler handler;
    private TextView timeMusicRun;
    private TextView timeMusicLength;
    private int timetostop;
    private  int positionMusic;
    private static ArrayList<MediaPlayer> mediaPlayerArrayList;
    private RecyclerView listMusic;
    private RecyclerView.Adapter mAdapter;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver broadcastReceiver2;
    private CheckBox loop;
    private CheckBox timer;

    boolean OnLoop;

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().unregisterReceiver(broadcastReceiver2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mediaPlayerArrayList.get(positionMusic).isPlaying()){
            mediaPlayerArrayList.get(positionMusic).setLooping(false);
            mediaPlayerArrayList.get(positionMusic).stop();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.music_fragment, container, false);

        OnLoop=false;
        btnPlay = v.findViewById(R.id.btnPlay);
        btnBack = v.findViewById(R.id.btnBack);
        btnFor = v.findViewById(R.id.btnFor);
        handler = new Handler();
        seekBar = v.findViewById(R.id.seekbar);
        timeMusicRun = v.findViewById(R.id.textViewMusicLength);
        timeMusicLength = v.findViewById(R.id.textMusicTime);
        loop = v.findViewById(R.id.loop);
        timer = v.findViewById(R.id.timer);

        listMusic = v.findViewById(R.id.listMusic);
        listMusic.setHasFixedSize(true);
        mediaPlayerArrayList = new ArrayList<>();
        listMusic.setLayoutManager(new LinearLayoutManager(getActivity()));

        mediaPlayerArrayList.add(MediaPlayer.create(v.getContext(),R.raw.khongloi1));
        mediaPlayerArrayList.add(MediaPlayer.create(v.getContext(),R.raw.khongloi2));
        mediaPlayerArrayList.add(MediaPlayer.create(v.getContext(),R.raw.khongloi3 ));
        mAdapter=new SoundAdapter(mediaPlayerArrayList,getActivity());
        listMusic.setAdapter(mAdapter);

        timetostop=0;




        timer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked== true){
                    BottomSheetStepDialog bottomSheetDialog = new BottomSheetStepDialog();
                    bottomSheetDialog.show(getActivity().getSupportFragmentManager(),"ssss");
                }
                else {
                    timetostop = 0;
                }
            }
        });

        loop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    OnLoop=true;
                }
                else {
                    OnLoop=false;
                }
            }
        });

        positionMusic=0;

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(positionMusic != intent.getExtras().getInt("posM")){
                    if(mediaPlayerArrayList.get(positionMusic).isPlaying()){
                        mediaPlayerArrayList.get(positionMusic).pause();
                        mediaPlayerArrayList.get(positionMusic).seekTo(0);
                    }
                    else {
                        mediaPlayerArrayList.get(positionMusic).seekTo(0);
                    }
                    positionMusic = intent.getExtras().getInt("posM");
                    btnPlay.setChecked(false);
                    mediaPlayerArrayList.get(positionMusic).start();
                    mediaPlayerArrayList.get(positionMusic).setLooping(true);
                }
                else {
                    btnPlay.setChecked(false);
                    mediaPlayerArrayList.get(positionMusic).start();
                    mediaPlayerArrayList.get(positionMusic).setLooping(true);
                }

            }
        };
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("Select Music"));

        broadcastReceiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timetostop = intent.getExtras().getInt("time") *1000 * 60;
            }
        };
        getActivity().registerReceiver(broadcastReceiver2,new IntentFilter("Select Timer"));


        btnPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    if (mediaPlayerArrayList.get(positionMusic).isPlaying()) {

                        mediaPlayerArrayList.get(positionMusic).pause();
                    }
                }
                else {

                    mediaPlayerArrayList.get(positionMusic).start();
                    mediaPlayerArrayList.get(positionMusic).setLooping(true);

                    changeSeekbar();
                }
            }
        });
        btnFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerArrayList.get(positionMusic).seekTo(mediaPlayerArrayList.get(positionMusic).getCurrentPosition() + 5000);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerArrayList.get(positionMusic).seekTo(mediaPlayerArrayList.get(positionMusic).getCurrentPosition() - 5000);
            }
        });

        mediaPlayerArrayList.get(positionMusic).setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax( mediaPlayerArrayList.get(positionMusic).getDuration());
                changeSeekbar();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {

                    mediaPlayerArrayList.get(positionMusic).seekTo(progress);
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
                mediaPlayerArrayList.get(positionMusic).pause();
                btnPlay.setChecked(true);
            }
        };
        return v;
    }

    private void changeSeekbar() {

        seekBar.setProgress(mediaPlayerArrayList.get(positionMusic).getCurrentPosition());
        if (mediaPlayerArrayList.get(positionMusic).getCurrentPosition() >= mediaPlayerArrayList.get(positionMusic).getDuration() - 1000) {
            if(OnLoop == true) {
                mediaPlayerArrayList.get(positionMusic).pause();
                mediaPlayerArrayList.get(positionMusic).seekTo(0);
                if(positionMusic < mediaPlayerArrayList.size()-1){
                    positionMusic += 1;
                }
                else {
                    positionMusic = 0;
                }
                mediaPlayerArrayList.get(positionMusic).start();
                mediaPlayerArrayList.get(positionMusic).setLooping(true);
            }
        }
        int duration0 = mediaPlayerArrayList.get(positionMusic).getDuration();
        String Musiclength0 = String.format("%02d : %02d ",
                TimeUnit.MILLISECONDS.toMinutes(duration0),
                TimeUnit.MILLISECONDS.toSeconds(duration0) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration0)));
        timeMusicLength.setText(Musiclength0);
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
