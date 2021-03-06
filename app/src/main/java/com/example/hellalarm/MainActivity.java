package com.example.hellalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.util.Calendar;


import static com.example.hellalarm.AlarmReceiver.setAlarm;

public class MainActivity extends AppCompatActivity implements Alarm_Dialog.AlarmDialogListener {
    private SQLiteDatabase mDatabase;
    private AlarmManager alarmManager;
    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNav = findViewById(R.id.nav);
        bottomNav.setOnNavigationItemSelectedListener(listener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BasicAlarm_Fragment()).commit();

        /**
         * Khai bao cac bien
         */
        AlarmDBHelper dbHelper= new AlarmDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);



        registerReceiver(broadcastReceiver,new IntentFilter("CANCELALARM"));
        registerReceiver(broadcastReceiver2,new IntentFilter("ON"));
        registerReceiver(broadcastReceiver3,new IntentFilter("OFF"));

    }

    public void additem(int hourtoset, int minutetoset, CharSequence label,int sound, boolean MON, boolean TUES, boolean WED, boolean THURS, boolean FRI, boolean SAT,boolean SUN, boolean onetime, boolean EDIT, int idtoedit,int step){

        Calendar c= Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourtoset);
        c.set(Calendar.MINUTE,minutetoset);
        c.set(Calendar.SECOND,0);

        /**
         * Add thong tin vao bang trong sqlite va cap nhat lai man hinh
         */
        ContentValues cv= new ContentValues();
        String templabel= "" + label;
        cv.put(AlarmContract.AlarmEntry.COLUMN_HOUR,hourtoset);
        cv.put(AlarmContract.AlarmEntry.COLUMN_MINUTE,minutetoset);
        cv.put(AlarmContract.AlarmEntry.COLUMN_LABEL,templabel);
        cv.put(AlarmContract.AlarmEntry.COLUMN_MON,MON ? 1: 0);
        cv.put(AlarmContract.AlarmEntry.COLUMN_TUES,TUES ? 1 : 0);
        cv.put(AlarmContract.AlarmEntry.COLUMN_WED,WED ? 1 : 0);
        cv.put(AlarmContract.AlarmEntry.COLUMN_THURS,THURS ? 1 : 0);
        cv.put(AlarmContract.AlarmEntry.COLUMN_FRI,FRI ? 1: 0);
        cv.put(AlarmContract.AlarmEntry.COLUMN_SAT,SAT ? 1 : 0);
        cv.put(AlarmContract.AlarmEntry.COLUMN_SUN,SUN ? 1: 0);
        cv.put(AlarmContract.AlarmEntry.COLUMN_ONE_TIME,onetime ? 1: 0);
        cv.put(AlarmContract.AlarmEntry.COLUMN_SOUND,sound);
        cv.put(AlarmContract.AlarmEntry.COLUMN_ENABLE,1);
        cv.put(AlarmContract.AlarmEntry.COLUMN_STEP,step);

        long id=0;
        if(EDIT==false) {
            id = mDatabase.insert(AlarmContract.AlarmEntry.TABLE_NAME, null, cv);
            BasicAlarm_Fragment fragment= (BasicAlarm_Fragment) getSupportFragmentManager().findFragmentById(R.id.basic_alarm);
            fragment.mAdapter.swapCursor(getAllItems());
        }
        else if(EDIT == true)
        {
            final String where = AlarmContract.AlarmEntry._ID + "=?";
            final String[] whereArgs = new String[] { String.valueOf(idtoedit) };

            mDatabase.update(AlarmContract.AlarmEntry.TABLE_NAME,cv,where,whereArgs);
            id=(long)idtoedit;
            BasicAlarm_Fragment fragment= (BasicAlarm_Fragment) getSupportFragmentManager().findFragmentById(R.id.basic_alarm);
            fragment.mAdapter.swapCursor(getAllItems());
        }


        /**
         * set Alarm manager
         */
        setAlarm(this,c.getTimeInMillis(),label,sound,MON,TUES,WED,THURS,FRI,SAT,SUN,onetime,(int)id,step);



        Toast.makeText(getApplicationContext(),"Add alarm succeed",Toast.LENGTH_SHORT).show();

    }

    public void removeItem(long id) {
        /**
         * Xoa item khoi bang SQLite va cap nhat lai man hinh
         */
        mDatabase.delete(AlarmContract.AlarmEntry.TABLE_NAME,
                AlarmContract.AlarmEntry._ID + "=" + id, null);
        BasicAlarm_Fragment fragment= (BasicAlarm_Fragment) getSupportFragmentManager().findFragmentById(R.id.basic_alarm);
        fragment.mAdapter.swapCursor(getAllItems());

    }

    public Cursor getAllItems() {
        /**
         * Lay tat ca thong tin trong bang, sap xep theo thoi gian them vao bang
         */
        return mDatabase.query(
                AlarmContract.AlarmEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                AlarmContract.AlarmEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    public void cancelAlarm(long id) {
        /**
         * Tao lai itent giong voi itent luc set manager de huy pending intent trong alarm manager
         */
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,AlarmReceiver.class);
        intent.putExtra("extra","off");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,(int)id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        sendBroadcast(intent);
        alarmManager.cancel(pendingIntent);

        ContentValues cv= new ContentValues();
        cv.put(AlarmContract.AlarmEntry.COLUMN_ENABLE,0);
        final String where = AlarmContract.AlarmEntry._ID + "=?";
        final String[] whereArgs = new String[] { String.valueOf(id) };
        mDatabase.update(AlarmContract.AlarmEntry.TABLE_NAME,cv,where,whereArgs);

        Toast.makeText(getApplicationContext(),"Alarm cancled",Toast.LENGTH_SHORT).show();
    }



    @Override
    public void applyTime(int hourtoset, int minutetoset, CharSequence label, int sound, boolean MON, boolean TUES, boolean WED, boolean THURS, boolean FRI, boolean SAT,boolean SUN,boolean onetime, boolean EDIT,int id, int step) {
        additem(hourtoset, minutetoset, label,sound, MON, TUES, WED, THURS, FRI, SAT, SUN,onetime, EDIT, id, step);
    }

    public BottomNavigationView.OnNavigationItemSelectedListener listener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.basic_alarm:
                    selectedFragment = new BasicAlarm_Fragment();
                    break;
                case R.id.protect_yoursleep:
                    selectedFragment = new ProtectSleep_Fragment();
                    break;
                case R.id.music_beforesleep:
                    selectedFragment = new Music_beforesSleep();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ContentValues cv= new ContentValues();
            cv.put(AlarmContract.AlarmEntry.COLUMN_ENABLE,0);
            final String where = AlarmContract.AlarmEntry._ID + "=?";
            final String[] whereArgs = new String[] { String.valueOf(intent.getExtras().getInt("id")) };
            mDatabase.update(AlarmContract.AlarmEntry.TABLE_NAME,cv,where,whereArgs);
        }
    };

    BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getBundleExtra("BUNDLE");
            int hourtoset=bundle.getInt("HOUR");
            int minutetoset=bundle.getInt("MINUTE");
            String label= bundle.getString("LABEL");
            int sound = bundle.getInt("SOUND");
            boolean MON=false;
            if(bundle.getInt("MON")==1){
                MON=true;
            }
            boolean TUES=false;
            if(bundle.getInt("TUES")==1){
                TUES=true;
            }
            boolean WED=false;
            if(bundle.getInt("WED")==1){
                WED=true;
            }
            boolean THURS=false;
            if(bundle.getInt("THURS")==1){
                THURS=true;
            }
            boolean FRI=false;
            if(bundle.getInt("FRI")==1){
                FRI=true;
            }
            boolean SAT=false;
            if(bundle.getInt("SAT")==1){
                SAT=true;
            }
            boolean SUN=false;
            if(bundle.getInt("SUN")==1){
                SUN=true;
            }
            boolean onetime=false;
            if(bundle.getInt("ONETIME")==1){
                onetime=true;
            }

            boolean EDIT=bundle.getBoolean("EDIT");

            int id= bundle.getInt("ID");

            int step = bundle.getInt("STEP");

            additem(hourtoset, minutetoset, label,sound, MON, TUES, WED, THURS, FRI, SAT, SUN,onetime, EDIT, id,step);
        }
    };

    BroadcastReceiver broadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            cancelAlarm((long)intent.getExtras().getInt("id"));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiver2);
        unregisterReceiver(broadcastReceiver3);
    }
}
