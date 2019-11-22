package com.example.mediaplayer.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.mediaplayer.R;
import com.example.mediaplayer.activities.MainActivity;
import com.example.mediaplayer.models.Song;

import java.util.ArrayList;

import Interfaces.OnClickListen;

import static com.example.mediaplayer.adapters.SongAdapter.myfont;

public class SongAlbumAdapter extends RecyclerView.Adapter<SongAlbumAdapter.ViewHolder> implements Filterable {

    OnClickListen alclicklisten;
    public static ArrayList<Song> albumSong;
    private static LayoutInflater inflater=null;
    public SongAlbumAdapter(ArrayList<Song>albumSong,OnClickListen alclicklisten){
        this.alclicklisten = alclicklisten;
        inflater=(LayoutInflater) MainActivity.getInstance().getSystemService(MainActivity.getInstance().LAYOUT_INFLATER_SERVICE);
        this.albumSong=albumSong;
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_song_layout,viewGroup,false);
        return new SongAlbumAdapter.ViewHolder(view,alclicklisten);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Song song= albumSong.get(i);
        viewHolder.textView.setText(" -  "+song.getName());

    }

    @Override
    public int getItemCount() {
        return albumSong.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       public TextView textView;
        OnClickListen onClickListen;
        public ViewHolder(@NonNull View itemView, OnClickListen onClickListen) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_song);
            textView.setTypeface(myfont);
            this.onClickListen=onClickListen;
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            onClickListen.onClick(getAdapterPosition());
        }
    }
}
