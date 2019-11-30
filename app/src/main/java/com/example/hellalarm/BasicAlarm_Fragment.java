package com.example.hellalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BasicAlarm_Fragment extends Fragment {
    public static AlarmAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.basicalarm_fragment,container,false);

        RecyclerView recyclerView=v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new AlarmAdapter(getActivity(),((MainActivity)getActivity()).getAllItems());
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ((MainActivity)getActivity()).removeItem((long) viewHolder.itemView.getTag());
                ((MainActivity)getActivity()).cancelAlarm((long) viewHolder.itemView.getTag());
            }


        }).attachToRecyclerView(recyclerView);


        Button btnthem=v.findViewById(R.id.btnthem);
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //additem();
                Alarm_Dialog dialog= new Alarm_Dialog();
                dialog.show(getFragmentManager(),"Edit ALarm");
            }
        });

        return v;
    }
}
