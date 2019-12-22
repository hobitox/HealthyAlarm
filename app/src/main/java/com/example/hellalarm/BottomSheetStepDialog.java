package com.example.hellalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hellalarm.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetStepDialog extends BottomSheetDialogFragment {
    private SeekBar seekBar;
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_step,container,false);

        seekBar = v.findViewById(R.id.seekBar);

        textView = v.findViewById(R.id.tvprogress);

        textView.setText(String.valueOf(seekBar.getProgress()*5));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<4) {
                    textView.setText(String.valueOf(progress * 5));
                }
                else {
                    textView.setText(String.valueOf(progress * 10));
                }
                getActivity().sendBroadcast(new Intent("Select Timer").putExtra("time",Integer.parseInt(String.valueOf(textView.getText()))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return v;

    }
}
