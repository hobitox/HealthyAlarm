package com.example.hellalarm;

import android.provider.BaseColumns;

public class AlarmContract  {

    private AlarmContract(){}
    public static final class AlarmEntry implements BaseColumns{
        public static final String TABLE_NAME="AlarmList";
        public static final String COLUMN_HOUR="HOUR";
        public static final String COLUMN_MINUTE="MINUTE";
        public static final String COLUMN_MON="MON";
        public static final String COLUMN_TUES="TUES";
        public static final String COLUMN_WED="WED";
        public static final String COLUMN_THURS="THURS";
        public static final String COLUMN_FRI="FRI";
        public static final String COLUMN_SAT="SAT";
        public static final String COLUMN_SUN="SUN";
        public static final String COLUMN_ONE_TIME="ONETIME";
        public static final String COLUMN_LABEL="LABEL";
        public static final String COLUMN_SOUND="SOUND";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
