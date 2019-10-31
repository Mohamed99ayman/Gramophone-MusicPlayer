package com.example.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    TextView songname,artistname,current,totaltext;
    SeekBar seekBar;
    int position;
    Song song;
    ImageView imageView;
    private MediaPlayer mediaPlayer;
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
        current=findViewById(R.id.current_time);
        totaltext=findViewById(R.id.total_time);
       //
        //
        //
        // next.getBackground().setAlpha(64);


        Intent i=getIntent();

        Bundle bundle=i.getExtras();
        position= bundle.getInt("index");
        stopPlaying();
        mediaPlayer=new MediaPlayer();
       setData(position);
        playSong(getApplicationContext());


        new Thread(new Runnable() {
            @Override
            public void run() {
                int currentPosition = 0;
                while (currentPosition<mediaPlayer.getDuration()) {
                    try {
                        Thread.sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setMax(mediaPlayer.getDuration());
                        seekBar.setProgress(currentPosition);
                        System.out.println(millisToMinutesAndSeconds(seekBar.getMax()));


                    }
                    catch (IllegalStateException e){
                                try {
                                    mediaPlayer.reset();
                                    currentPosition = mediaPlayer.getCurrentPosition();
                                }catch (IllegalStateException ex){
                                    Log.d("SEEKBUttoN2", "run: XXXXXd");
                                    return;
                                }

                    }
                    catch (InterruptedException e) {
                        Log.d("SEEKBUTTON", "run: sss");
                        e.printStackTrace();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("SEEKBUttoN", "run: XXXXX");
                        return;
                    }
                    // int total=0;
                   // if(mediaPlayer.isPlaying()) {
                    //     total = mediaPlayer.getDuration(); //gets the duration of music file
                  //  }
                    //final String totalTime = millisToMinutesAndSeconds(total);//converts it to
                   final String curTime = millisToMinutesAndSeconds(currentPosition);


                   // musicSeekBar.setSecondaryProgress(getBufferPercentage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            totaltext.setText(millisToMinutesAndSeconds(seekBar.getMax()));
                            current.setText(curTime);
                        }
                    });

                }
            }
        }).start();

       /* updateseekbar=new Thread(){
            @Override
            public void run() {
                 int currentposition=0;
                 while (currentposition<mediaPlayer.getDuration()){

                     try{
                         sleep(500);
                         currentposition=mediaPlayer.getCurrentPosition();
                         seekBar.setProgress(currentposition);
                     }catch (Exception e){

                     }
                 }

            }
        };
        updateseekbar.start();*/

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
               stopPlaying();
                position=(position+1)%(SongAdapter.allSongs.size());
                setData(position);
                playSong(getApplicationContext());

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                if(position==0)position=SongAdapter.allSongs.size()-1;
                else position--;
                setData(position);
                playSong(getApplicationContext());
            }
        });







    }
    private void stopPlaying(){
            if (mediaPlayer!= null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
    }
    public void playSong(Context context){
        try {
            mediaPlayer=MediaPlayer.create(context, Uri.parse(song.getPath()));
        }
        catch (Exception e){
            mediaPlayer.release();
            mediaPlayer=MediaPlayer.create(context, Uri.parse(song.getPath()));
        }

        mediaPlayer.start();
        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
                position=(position+1)%(SongAdapter.allSongs.size());
                setData(position);
                playSong(getApplicationContext());
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

    String  millisToMinutesAndSeconds(int millis) {
        int minutes = (int)Math.floor(millis / 60000);
        int seconds = ((millis % 60000) / 1000);
        return minutes + ":" + (seconds < 10 ? '0' : "") + seconds;
    }

}
