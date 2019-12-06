package com.example.mediaplayer.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mediaplayer.R;
import com.example.mediaplayer.adapters.SongAlbumAdapter;

import interfaces.OnClickListen;

import static com.example.mediaplayer.activities.MainActivity.al;

public class AlbumActivity extends AppCompatActivity implements OnClickListen {


    protected ImageView imageView;
    protected int position;
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mmanager;
    static SongAlbumAdapter songalbumAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        recyclerView = findViewById(R.id.album_recycler);
        imageView=findViewById(R.id.albumimage);
        recyclerView.setHasFixedSize(true);
        mmanager=new LinearLayoutManager(this);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        position = bundle.getInt("index");
        songalbumAdapter = new SongAlbumAdapter(al.get(position),this);
        Glide.with(this)
                .load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),al.get(position).get(0).getAlbumID()).toString())
                .thumbnail(0.2f)
                .centerCrop()
                .placeholder(R.drawable.track)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
        verticalDecoration.setDrawable(verticalDivider);
        recyclerView.addItemDecoration(verticalDecoration);

        recyclerView.setLayoutManager(mmanager);
        recyclerView.setAdapter(songalbumAdapter);


    }


    @Override
    public void onClick(int position) {
        Intent intent=new Intent(MainActivity.getInstance(), PlayerActivity.class).putExtra("index",position).putExtra("val",1);
        startActivity(intent);
    }

}
