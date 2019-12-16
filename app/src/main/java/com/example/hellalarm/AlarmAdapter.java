package com.example.hellalarm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Calendar;


public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    public AlarmAdapter(Context context, Cursor cursor){
        mContext=context;
        mCursor=cursor;
    }
    public class AlarmViewHolder extends RecyclerView.ViewHolder{

        public TextView Timetext;
        public TextView Label;
        public Switch aSwitch;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            Timetext=itemView.findViewById(R.id.textView2);
            Label=itemView.findViewById(R.id.textView3);
//            id=itemView.findViewById(R.id.textView);
            aSwitch=itemView.findViewById(R.id.switch1);
//            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(isChecked){
//                        mContext.sendBroadcast(new Intent("ON").putExtra("id",Integer.parseInt(""+id.getText())));
//                    }else{
//                        mContext.sendBroadcast(new Intent("OFF").putExtra("id",Integer.parseInt(""+id.getText())));
//                    }
//                }
//            });
        }
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.alarm_item,parent,false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String label=mCursor.getString(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_LABEL));
        /**
         * Lay id cua alarm trong sql gan vao tung item trong recyclerview
         */
        final long id = mCursor.getLong(mCursor.getColumnIndex(AlarmContract.AlarmEntry._ID));
        holder.itemView.setTag(id);

        final int HOUR = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_HOUR));
        final int MINUTE = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_MINUTE));

        Calendar c= Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,HOUR);
        c.set(Calendar.MINUTE,MINUTE);
        c.set(Calendar.SECOND,0);

        /**
         * Add thong tin vao bang trong sqlite va cap nhat lai man hinh
         */
        String timetext = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        holder.Timetext.setText(timetext);
        holder.Label.setText(label);

        final int MON = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_MON ));
        final int TUES = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_TUES));
        final int WED = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_WED));
        final int THURS = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_THURS));
        final int FRI=mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_FRI));
        final int SAT = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_SAT));
        final int SUN = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_SUN));
        final int ONETIME = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_ONE_TIME));
        final int SOUND = mCursor.getInt(mCursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_SOUND));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarm_Dialog dialog= new Alarm_Dialog();
                Bundle bundle = new Bundle();
                bundle.putInt("SOUND",SOUND);
                bundle.putInt("HOUR",HOUR);
                bundle.putInt("MINUTE",MINUTE);
                bundle.putString("LABEL",label);
                bundle.putInt("MON",MON);
                bundle.putInt("TUES",TUES);
                bundle.putInt("WED",WED);
                bundle.putInt("THURS",THURS);
                bundle.putInt("FRI",FRI);
                bundle.putInt("SAT",SAT);
                bundle.putInt("SUN",SUN);
                bundle.putInt("ONETIME",ONETIME);
                bundle.putBoolean("EDIT",true);
                bundle.putInt("ID",(int)id);
                dialog.setArguments(bundle);
                dialog.show(((FragmentActivity) mContext).getSupportFragmentManager(),"Edit ALarm");
            }
        });

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    mContext.sendBroadcast(new Intent("OFF").putExtra("id",(int)id));
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putInt("SOUND",SOUND);
                    bundle.putInt("HOUR",HOUR);
                    bundle.putInt("MINUTE",MINUTE);
                    bundle.putString("LABEL",label);
                    bundle.putInt("MON",MON);
                    bundle.putInt("TUES",TUES);
                    bundle.putInt("WED",WED);
                    bundle.putInt("THURS",THURS);
                    bundle.putInt("FRI",FRI);
                    bundle.putInt("SAT",SAT);
                    bundle.putInt("SUN",SUN);
                    bundle.putInt("ONETIME",ONETIME);
                    bundle.putBoolean("EDIT",true);
                    bundle.putInt("ID",(int)id);
                    mContext.sendBroadcast(new Intent("ON").putExtra("BUNDLE",bundle));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
