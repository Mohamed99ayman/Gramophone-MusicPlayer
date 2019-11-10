package com.example.mediaplayer;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static com.example.mediaplayer.SongAdapter.songs;


public class PlayerActivity extends AppCompatActivity {
    SeekBar mSeekBar;
    static MediaPlayer mMediaPlayer;
    static boolean playin;

    public int getPosition() {
        return position;
    }

    int position;
    TextView curTime,totTime,songTitle,artistname;
    ImageView pause,prev,next;
    ImageView imageView;
    MediaMetadataRetriever metadataRetriever;
    byte art[];
    private NofiticationCenter nofiticationCenter;
    private static PlayerActivity instance;


    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        nofiticationCenter=new NofiticationCenter();
        instance=this;
        mSeekBar = findViewById(R.id.seek);
        songTitle = findViewById(R.id.song_name);
        artistname=findViewById(R.id.artist_name);
        totTime = findViewById(R.id.total_time);
        pause = findViewById(R.id.pause);

        prev = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        curTime=findViewById(R.id.current_time);
        imageView=findViewById(R.id.imageplayer);

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        position = bundle.getInt("index");
        initPlayer(position);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0) {
                    position = songs.size() - 1;
                } else {
                    position--;
                }
                initPlayer(position);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=(position+1)% songs.size();
                initPlayer(position);
            }
        });

    }

    public static PlayerActivity getInstance() {
        return instance;
    }

     void initPlayer(final int position) {
        playin=true;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
        }

        String name= songs.get(position).getName();
        String artist= songs.get(position).getArtist();
        songTitle.setText(name);
        artistname.setText(artist);
        metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(songs.get(position).getPath());
        try {
            art = metadataRetriever.getEmbeddedPicture();
            Glide
                    .with(getApplicationContext())
                    .load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),songs.get(position).getAlbumID()).toString())
                    .thumbnail(0.2f)
                    .centerCrop()
                    .placeholder(R.drawable.track_1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
        catch (Exception e){

            Glide.with(this).load(R.drawable.track).into(imageView);
        }
        MainActivity.getInstance().sendOnChannel(art,name,artist,position);

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(songs.get(position).getPath())); // create and load mediaplayer with song resources
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                String totalTime = createTimeLabel(mMediaPlayer.getDuration());
                totTime.setText(totalTime);
                mSeekBar.setMax(mMediaPlayer.getDuration());
                mMediaPlayer.start();
                pause.setBackgroundResource(R.drawable.pause_24dp);

            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int curSongPoition = position;
                curSongPoition = (curSongPoition + 1) % (songs.size());
                initPlayer(curSongPoition);

            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mSeekBar.setProgress(progress);
                }

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    try {
                        if (mMediaPlayer.isPlaying()) {
                            Message msg = new Message();
                            msg.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int current_position = msg.what;
            mSeekBar.setProgress(current_position);
            String cTime = createTimeLabel(current_position);
            curTime.setText(cTime);
        }
    };


     void play() {

        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            playin=true;
            mMediaPlayer.start();
            pause.setBackgroundResource(R.drawable.pause_24dp);
            MainActivity.getInstance().sendOnChannel(art, songs.get(position).getName(), songs.get(position).getArtist(),position);
        } else {
            pause();
        }

    }

     void pause() {
        if (mMediaPlayer.isPlaying()) {
            playin=false;
            mMediaPlayer.pause();
            pause.setBackgroundResource(R.drawable.play_arrow_24dp);
            MainActivity.getInstance().sendOnChannel(art, songs.get(position).getName(), songs.get(position).getArtist(),position);


        }

    }

    public String createTimeLabel(int duration) {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timeLabel += min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;


    }
}