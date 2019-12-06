package com.example.mediaplayer.notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.mediaplayer.activities.MainActivity;
import com.example.mediaplayer.activities.PlayerActivity;
import com.example.mediaplayer.adapters.SongAdapter;

public class NotiService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, MainActivity.class);


        handleIncomingActions(intent);

        return START_NOT_STICKY;
    }
    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;
        int pos= PlayerActivity.getInstance().getPosition();

        String actionString = playbackAction.getAction();

            if (actionString.equalsIgnoreCase("com.mypackage.ACTION_PAUSE_MUSIC")) {
                if( PlayerActivity.playin){
                    PlayerActivity.getInstance().pause();
                }
                else{
                    PlayerActivity.getInstance().play();
                }
        } else if (actionString.equalsIgnoreCase("com.mypackage.ACTION_NEXT_MUSIC")) {
                if(SongAdapter.songs.get((pos+1)% SongAdapter.songs.size()).getName().equals("shufflee"))pos++;
                PlayerActivity.getInstance().initPlayer(((pos+1)% SongAdapter.songs.size()));
                PlayerActivity.getInstance().setPosition(((pos+1)% SongAdapter.songs.size()));
        } else if (actionString.equalsIgnoreCase("com.mypackage.ACTION_PREV_MUSIC")) {
                if(SongAdapter.songs.get((pos+1)% SongAdapter.songs.size()).getName().equals("shufflee"))pos--;
                if(pos<=0)pos=SongAdapter.songs.size()-1;
                else pos-=1;
                PlayerActivity.getInstance().setPosition(pos);
                PlayerActivity.getInstance().initPlayer((pos));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}