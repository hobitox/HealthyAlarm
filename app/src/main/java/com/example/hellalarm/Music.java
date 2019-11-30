package com.example.hellalarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;


public class Music extends Service {

    boolean onrunning=false;
    MediaPlayer mediaPlayer;
    Vibrator vibrator;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getExtras().getString("extra").equals("off")){
            if(mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                onrunning=false;
            }
            if(vibrator!=null){
                vibrator.cancel();
                onrunning=false;
            }
        }
        else if(intent.getExtras().getString("extra").equals("on"))
        {
            if(onrunning==false) {
                int sound= intent.getExtras().getInt("SOUND");
                switch (sound){
                    case 0:
                        mediaPlayer = MediaPlayer.create(this, R.raw.nhacchuong1);
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(this, R.raw.nhacchuong2);
                        break;
                    default:
                        break;
                }
                //mediaPlayer = MediaPlayer.create(this, R.raw.nhacchuong1);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = {0, 1000, 500, 500, 200};
                vibrator.vibrate(pattern, 0);
                onrunning = true;
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
