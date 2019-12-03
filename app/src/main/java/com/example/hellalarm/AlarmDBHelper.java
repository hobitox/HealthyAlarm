package com.example.hellalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.hellalarm.AlarmContract.*;
import androidx.annotation.Nullable;

public class AlarmDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="alarmlist.db";
    public static final int DATABASE_VERSION=1;
    public AlarmDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         final String CREATE_ALARMS_TABLE = "CREATE TABLE " +
                 AlarmEntry.TABLE_NAME + " (" +
                 AlarmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 AlarmEntry.COLUMN_HOUR + " INTEGER NOT NULL, " +
                 AlarmEntry.COLUMN_MINUTE + " INTEGER NOT NULL, " +
                 AlarmEntry.COLUMN_MON + " INTEGER NOT NULL, "+
                 AlarmEntry.COLUMN_TUES + " INTEGER NOT NULL, "+
                 AlarmEntry.COLUMN_WED + " INTEGER NOT NULL, "+
                 AlarmEntry.COLUMN_THURS + " INTEGER NOT NULL, "+
                 AlarmEntry.COLUMN_FRI+ " INTEGER NOT NULL, "+
                 AlarmEntry.COLUMN_SAT + " INTEGER NOT NULL, "+
                 AlarmEntry.COLUMN_SUN + " INTEGER NOT NULL, "+
                 AlarmEntry.COLUMN_ONE_TIME + " INTEGER NOT NULL, "+
                 AlarmEntry.COLUMN_LABEL + " TEXT NOT NULL, "+
                 AlarmEntry.COLUMN_SOUND + " INTEGER NOT NULL, "+
                 AlarmEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(CREATE_ALARMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME);
        onCreate(db);
    }
}
