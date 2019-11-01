package com.example.mediaplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
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

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    Button next,prev,pause;
    TextView songname,artistname,current,totaltext;
    SeekBar seekBar;
    int position;
    Song song;
    ImageView imageView;
    private MediaPlayer mediaPlayer;
    MediaMetadataRetriever metadataRetriever;
    byte art[];
    int Duration;

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

        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }

        Intent i=getIntent();
        Bundle bundle=i.getExtras();

        position= bundle.getInt("index");
        stopPlaying();
       // mediaPlayer=new MediaPlayer();
       setData(position);
        playSong(getApplicationContext());

     /*   new Thread(new Runnable() {
            @Override
            public void run() {
                int currentPosition = 0;
                while (currentPosition<Duration) {
                    try {

                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setMax(Duration);
                        seekBar.setProgress(currentPosition);
                        System.out.println(millisToMinutesAndSeconds(seekBar.getMax()));
                        Thread.sleep(500);
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
        */
     new Thread(new Runnable() {
         @Override
         public void run() {
             int currentPosition=0;
                 while (mediaPlayer!=null&&currentPosition<Duration){
             try{
                         Message message=new Message();
                         currentPosition=mediaPlayer.getCurrentPosition();
                         message.what=mediaPlayer.getCurrentPosition();
                         handler.sendMessage(message);
                         Thread.sleep(1000);
                 }catch (InterruptedException e){
                     e.printStackTrace();
                 }
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
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    Duration=mediaPlayer.getDuration();
                    totaltext.setText(millisToMinutesAndSeconds(Duration));
                    mediaPlayer.start();
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
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            current.setText(millisToMinutesAndSeconds(msg.what));
                seekBar.setProgress(msg.what);
                //totaltext.setText(millisToMinutesAndSeconds(Duration));
        }
    };
    private void stopPlaying(){
            if (mediaPlayer!= null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
    }
    public void playSong(Context context){
        boolean checl=false;
        while (!checl){
            try{
                mediaPlayer=MediaPlayer.create(context, Uri.parse(song.getPath()));
                Duration=mediaPlayer.getDuration();
                checl=true;
            }catch (Exception e){
                mediaPlayer.release();
                mediaPlayer=MediaPlayer.create(context, Uri.parse(song.getPath()));
                Duration=mediaPlayer.getDuration();
                Log.d("TTry", "playSong: Not Reading");
            }
        }
      /*  try {
            mediaPlayer=MediaPlayer.create(context, Uri.parse(song.getPath()));
            Duration=mediaPlayer.getDuration();
        }
        catch (Exception e){
            mediaPlayer.release();
            mediaPlayer=MediaPlayer.create(context, Uri.parse(song.getPath()));
            Duration=mediaPlayer.getDuration();
            e.printStackTrace();
        }*/

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
            Glide.with(getApplicationContext())
                    .load(BitmapFactory.decodeByteArray(art, 0, art.length))
                    .thumbnail(0.5f)
                    .into(imageView);
           // imageView.setImageBitmap();
        }catch (Exception e){
            Glide.with(this).load(R.drawable.track).into(imageView);
        }
    }

    String  millisToMinutesAndSeconds(int millis) {
        int minutes = (int)Math.floor(millis / 60000);
        int seconds = ((millis % 60000) / 1000);
        return minutes + ":" + (seconds < 10 ? '0' : "") + seconds;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
