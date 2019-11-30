package com.example.hellalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras().getString("extra").equals("on")) {

            /**
             * Tao nofitication
             */

            NotificationHelper notificationHelper = new NotificationHelper(context);
            String templabel=""+intent.getExtras().getCharSequence("LABEL");
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification(templabel);
            /**
             * Intent kich hoat khi an vao nofitication
             */
            Intent intent_resumeapp = new Intent(context, Alarm_Showing.class);
            intent_resumeapp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, intent.getExtras().getInt("id"), intent_resumeapp, PendingIntent.FLAG_UPDATE_CURRENT);
            nb.setContentIntent(pendingIntent);
            notificationHelper.getManger().notify(intent.getExtras().getInt("id"), nb.build());

            context.startActivity(intent_resumeapp);
            /**
             * Itent toi class music de bat music service
             */
            int soundtoset= intent.getExtras().getInt("SOUND");
            Intent music_intent = new Intent(context, Music.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            music_intent.putExtra("extra", "on");
            music_intent.putExtra("SOUND",soundtoset);
            context.startService(music_intent);

            Toast.makeText(context.getApplicationContext(),"Broadcast nhan intent",Toast.LENGTH_SHORT).show();

            /**
             * Dat lai alarm, ham nay chuan bi de sau nay set alarm theo tung ngay
             */
            long timetoset=intent.getExtras().getLong("timetoset");
            int idtoset=intent.getExtras().getInt("id");
            boolean MON = intent.getExtras().getBoolean("MON");
            boolean TUES = intent.getExtras().getBoolean("TUES");
            boolean WED = intent.getExtras().getBoolean("WED");
            boolean THURS = intent.getExtras().getBoolean("THURS");
            boolean FRI = intent.getExtras().getBoolean("FRI");
            boolean SAT = intent.getExtras().getBoolean("SAT");
            boolean SUN = intent.getExtras().getBoolean("SUN");
            boolean onetime = intent.getExtras().getBoolean("ONETIME");
            CharSequence label=intent.getExtras().getCharSequence("LABEL");

            if(!onetime){
                setAlarm(context,timetoset,label,soundtoset ,MON,TUES,WED,THURS,FRI,SAT,SUN, onetime,idtoset, true);
            }
            else {
                context.sendBroadcast(new Intent("CANCELALARM").putExtra("id",idtoset));
            }
        }
        else if(intent.getExtras().getString("extra").equals("off")){
            /**
             * Tat music service
             */
            Toast.makeText(context.getApplicationContext(),"vao off",Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(context, Music.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("extra",intent.getExtras().getString("extra"));
            context.startService(intent1);
        }
    }

    public static void setAlarm(Context context, long timetoset, CharSequence label ,int sound, boolean MON, boolean TUES, boolean WED, boolean THURS, boolean FRI, boolean SAT,boolean SUN, boolean onetime, int id, boolean onreset){

        Toast.makeText(context.getApplicationContext(),"vao set alarm",Toast.LENGTH_SHORT).show();
        final Calendar c;
        if(!onetime) {
            if (MON == TUES == WED == THURS == FRI == SAT == SUN == false) {
                Toast.makeText(context.getApplicationContext(), "Khong co ngay duoc set", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AlarmReceiver.class);
                PendingIntent pIntent = PendingIntent.getBroadcast(
                        context,
                        id,
                        intent,
                        FLAG_UPDATE_CURRENT
                );
                final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                manager.cancel(pIntent);
                return;
            }
             c = getTimeForNextAlarm(timetoset, MON, TUES, WED, THURS, FRI, SAT, SUN);
        }
        else{
            c= Calendar.getInstance();
            c.setTimeInMillis(timetoset);
            if(c.before(Calendar.getInstance())){
                c.add(Calendar.DAY_OF_MONTH,1);
            }
        }

        AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
        AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(context,AlarmReceiver.class);
        intent.putExtra("LABEL",label);
        intent.putExtra("extra","on");
        intent.putExtra("id",id);
        intent.putExtra("timetoset",timetoset);
        intent.putExtra("MON",MON);
        intent.putExtra("TUES",TUES);
        intent.putExtra("WED",WED);
        intent.putExtra("THURS",THURS);
        intent.putExtra("FRI",FRI);
        intent.putExtra("SAT",SAT);
        intent.putExtra("SUN",SUN);
        intent.putExtra("ONETIME", onetime);
        intent.putExtra("SOUND",sound);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
           alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        }
        else{
            alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        }
    }



    public static boolean checkexist(Context context,int id){
        AlarmDBHelper dbHelper= new AlarmDBHelper(context);
        SQLiteDatabase mDatabase = dbHelper.getWritableDatabase();
        String query="Select * from " + AlarmContract.AlarmEntry.TABLE_NAME + " where " + AlarmContract.AlarmEntry._ID + " = \'" + id + "\'";
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            Toast.makeText(context.getApplicationContext(),"Chua ton tai",Toast.LENGTH_SHORT).show();
            return false;
        }
        int temp = cursor.getCount();
        cursor.close();
        Toast.makeText(context.getApplicationContext(),"Da ton tai, " + temp,Toast.LENGTH_SHORT).show();
        return true;
    }

    private static int getStartIndexFromTime(Calendar c) {

        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        int startIndex = 0;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                startIndex = 0;
                break;
            case Calendar.TUESDAY:
                startIndex = 1;
                break;
            case Calendar.WEDNESDAY:
                startIndex = 2;
                break;
            case Calendar.THURSDAY:
                startIndex = 3;
                break;
            case Calendar.FRIDAY:
                startIndex = 4;
                break;
            case Calendar.SATURDAY:
                startIndex = 5;
                break;
            case Calendar.SUNDAY:
                startIndex = 6;
                break;
        }
        return startIndex;
    }

    private static Calendar getTimeForNextAlarm(long timetoset, boolean MON, boolean TUES, boolean WED, boolean THURS, boolean FRI, boolean SAT,boolean SUN) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timetoset);

        final long temptime= calendar.getTimeInMillis();
        final long currentTime = System.currentTimeMillis();
        final int startIndex = getStartIndexFromTime(calendar);

        int count = 0;
        boolean isAlarmSetForDay;

        final SparseBooleanArray daysArray = new SparseBooleanArray();
        daysArray.put(0,MON);
        daysArray.put(1,TUES);
        daysArray.put(2,WED);
        daysArray.put(3,THURS);
        daysArray.put(4,FRI);
        daysArray.put(5,SAT);
        daysArray.put(6,SUN);

        int temp;
        do {
            final int index = (startIndex + count) % 7;
            isAlarmSetForDay =
                    daysArray.valueAt(index) && (calendar.getTimeInMillis() > currentTime);
            if(!isAlarmSetForDay) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                count++;
            }
            temp=index;
        } while(!isAlarmSetForDay && count < 7);

        Log.e("day set", String.valueOf(temp));
        return calendar;
    }
}
