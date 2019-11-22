package com.example.mediaplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayer.R;
import com.example.mediaplayer.activities.MainActivity;
import com.example.mediaplayer.activities.PlayerActivity;
import com.example.mediaplayer.adapters.SongAdapter;

import Interfaces.OnClickListen;

import static com.example.mediaplayer.adapters.SongAdapter.songs;

public class SongsFragment extends Fragment implements OnClickListen {
    View v;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mmanager;
    private static SongAdapter songAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.songs_fragment,container,false);
        recyclerView = v.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        mmanager=new LinearLayoutManager(getContext());
        songAdapter = new SongAdapter(MainActivity.getInstance(), songs,this);
        recyclerView.setLayoutManager(mmanager);
        recyclerView.setAdapter(songAdapter);
        return v;
    }

    public SongsFragment() {
    }

    @Override
    public void onClick(int position) {
        Intent intent=new Intent(MainActivity.getInstance(), PlayerActivity.class).putExtra("index",position).putExtra("val",0);
        startActivity(intent);
    }
    public static void search(String text){
        songAdapter.getFilter().filter(text);
    }
}
