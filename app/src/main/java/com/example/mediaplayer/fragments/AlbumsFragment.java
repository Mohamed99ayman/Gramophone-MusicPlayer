package com.example.mediaplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayer.R;
import com.example.mediaplayer.activities.AlbumActivity;
import com.example.mediaplayer.activities.MainActivity;
import com.example.mediaplayer.adapters.AlbumAdapter;

import Interfaces.OnClickListen;

public class AlbumsFragment extends Fragment implements OnClickListen {
    View v;
    private RecyclerView recyclerView;

    ///////////////HERE
    private RecyclerView.LayoutManager mmanager;
    private static AlbumAdapter albumAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public AlbumsFragment() {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.albums_fragment,container,false);
        recyclerView = v.findViewById(R.id.albums_recycleview);
        recyclerView.setHasFixedSize(true);
        mmanager=new GridLayoutManager(getContext(),2);
        albumAdapter = new AlbumAdapter(this);
        recyclerView.setLayoutManager(mmanager);
        recyclerView.setAdapter(albumAdapter);
        return v;

    }
    @Override
    public void onClick(int position) {
        Intent intent=new Intent(MainActivity.getInstance(), AlbumActivity.class).putExtra("index",position);
        startActivity(intent);
    }


}
