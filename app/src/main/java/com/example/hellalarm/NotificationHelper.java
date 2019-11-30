package com.example.hellalarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String  a="abc";
    public static final String b="123";

    public PowerManager.WakeLock mWakeLock;
    private NotificationManager mManager;
    @SuppressLint("InvalidWakeLockTag")
    public NotificationHelper(Context base) {
        super(base);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock=pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "TEST_LOCK");
        mWakeLock.acquire();
        mWakeLock.release();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreateChannels();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateChannels(){
        NotificationChannel channel= new NotificationChannel(a,"Channel 1", NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManger().createNotificationChannel(channel);


    }

    public NotificationManager getManger(){
        if(mManager == null) {
            mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String label){
        return new NotificationCompat.Builder(getApplicationContext(),a)
                .setContentTitle("ALarm!")
                .setContentText(label)
                .setSmallIcon(R.drawable.ic_one)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
    }
}
