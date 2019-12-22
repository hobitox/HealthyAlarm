package com.example.hellalarm;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SoundAdapter extends  RecyclerView.Adapter<SoundAdapter.ViewHolder>{
    ArrayList<MediaPlayer> mediaPlayers;
    Context context;

    public SoundAdapter(ArrayList<MediaPlayer> mediaPlayers, Context context) {
        this.mediaPlayers = mediaPlayers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView =  layoutInflater.inflate(R.layout.sound_item,parent,false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        int duration = mediaPlayers.get(position).getDuration();
        String Musiclength = String.format("%02d : %02d ",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        holder.txtMusicLength.setText(Musiclength);
        holder.txtNameMusic.setText("ReLaxing "+ (position+1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent("Select Music").putExtra("posM",position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaPlayers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNameMusic;
        TextView txtMusicLength;


        public ViewHolder(View itemView)
        {
            super(itemView);
            txtNameMusic =  itemView.findViewById(R.id.textViewMusicName);
            txtMusicLength = itemView.findViewById(R.id.textViewMusicLength);

        }
    }
}
