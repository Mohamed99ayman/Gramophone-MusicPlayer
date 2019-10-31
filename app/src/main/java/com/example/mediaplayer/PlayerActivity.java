package com.example.mediaplayer;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

public class PlayerActivity extends AppCompatActivity {

    Button next,prev,pause;
    TextView songname,artistname;
    SeekBar seekBar;
    int position;
    Song song;
    ImageView imageView;
    static MediaPlayer mediaPlayer;
    Thread updateseekbar;
    MediaMetadataRetriever metadataRetriever;
    byte art[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        next=findViewById(R.id.next);
        prev=findViewById(R.id.previous);
        imageView=findViewById(R.id.imageplayer);
        pause=findViewById(R.id.pause);
        songname=findViewById(R.id.song_name);
        artistname=findViewById(R.id.artist_name);
        seekBar=findViewById(R.id.seek);
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        position= bundle.getInt("index");
        mediaPlayer=new MediaPlayer();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
       setData(position);
        mediaPlayer=MediaPlayer.create(getApplicationContext(), Uri.parse(song.getPath().toString()));
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        updateseekbar=new Thread(){
            @Override
            public void run() {
                 int duration=mediaPlayer.getDuration();
                 int currentposition=0;
                 while (currentposition<duration){


                     try{
                         sleep(100);
                         currentposition=mediaPlayer.getCurrentPosition();
                         seekBar.setProgress(currentposition);
                     }catch (Exception e){

                     }
                 }
            }
        };


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    pause.setBackgroundResource(R.drawable.play_arrow_24dp);
                }else{
                    mediaPlayer.start();
                    pause.setBackgroundResource(R.drawable.pause_24dp);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if(position+1>SongAdapter.allSongs.size()){
                    position=0;
                }else position++;
                setData(position);
                mediaPlayer=MediaPlayer.create(getApplicationContext(), Uri.parse(song.getPath().toString()));
                mediaPlayer.start();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if(position==0)position=SongAdapter.allSongs.size()-1;
                else position--;
                setData(position);
                mediaPlayer=MediaPlayer.create(getApplicationContext(), Uri.parse(song.getPath().toString()));
                mediaPlayer.start();

            }
        });





    }
    public void setData(int position){
        song=SongAdapter.allSongs.get(position);
        songname.setText(song.getName());
        artistname.setText(song.getArtist());
        try {
            metadataRetriever= new MediaMetadataRetriever();
            metadataRetriever.setDataSource(song.getPath());
            art=metadataRetriever.getEmbeddedPicture();
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(art, 0, art.length));
        }catch (Exception e){
            Glide.with(this).load(R.drawable.track).into(imageView);
        }
    }
}
