package com.example.mediaplayer;
import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import android.support.v7.widget.SearchView;
import java.util.ArrayList;
import java.util.Collections;

import Interfaces.OnClickListen;

import static com.example.mediaplayer.NofiticationCenter.channel_1_ID;

public class MainActivity extends AppCompatActivity implements OnClickListen {
    private int Storage_Permission_code=1;
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private RecyclerView.LayoutManager mmanager;
    private DataReading dataReading;
    protected static MainActivity instance;
    private MediaSessionCompat mediaSession;
    private NotificationManagerCompat notificationManager;
    MediaMetadataRetriever metadataRetriever;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);

        notificationManager = NotificationManagerCompat.from(this);

        mediaSession = new MediaSessionCompat(this, "tag");
        instance=this;


        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
            requestStoragePermission();
        }else {
         Start();

        }

    }
    public static MainActivity getInstance() {
        return instance;
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).setTitle("Permission Needed").setMessage("Need to read songs from your storage").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Storage_Permission_code);

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                }
            }).create().show();

        }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Storage_Permission_code);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==Storage_Permission_code){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            Start();
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void Start(){
        dataReading=new DataReading(this);
        ArrayList<Song> songs = dataReading.getAllAudioFromDevice();
        Collections.sort(songs);
        mmanager=new LinearLayoutManager(this);
        songAdapter = new SongAdapter(this, songs,this);
        recyclerView.setLayoutManager(mmanager);
        recyclerView.setAdapter(songAdapter);
        //RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
       // ((DividerItemDecoration) itemDecoration).setDrawable(getResources().getDrawable(R.drawable.line_divider));
       // recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                songAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onClick(int position) {
    Intent intent=new Intent(this,PlayerActivity.class).putExtra("index",position);
    startActivity(intent);

    }

    public void sendOnChannel(byte art[],String name,String artist) {

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        int plaorpa;
        if(PlayerActivity.getInstance().playin){
            Log.d(TAG, "sendOnChannel: "+PlayerActivity.playin);
            plaorpa=R.drawable.pause_24dp;
        }else{
            plaorpa=R.drawable.play_arrow_24dp;
        }
        Bitmap artwork;
        try {
            artwork=BitmapFactory.decodeByteArray(art,0,art.length);
        }catch (Exception e){
            artwork = BitmapFactory.decodeResource(getResources(), R.drawable.track_2);
        }
        Notification notification = new NotificationCompat.Builder(this, channel_1_ID)
                .setSmallIcon(R.drawable.music_note_24dp)
                .setContentTitle(name)
                .setContentText("Song")
                .setLargeIcon(artwork)
                .addAction(R.drawable.previous_24dp, "Previous", playbackAction(3))
                .addAction(plaorpa, "Pause", playbackAction(1))
                .addAction(R.drawable.next_24dp, "Next", playbackAction(2))
                .setContentIntent(contentIntent)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSession.getSessionToken()))
                .setSubText(artist)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManager.notify(1, notification);
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, NotiService.class);
        switch (actionNumber) {
            case 1:
                // Pause
                playbackAction.setAction("com.mypackage.ACTION_PAUSE_MUSIC");
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction("com.mypackage.ACTION_NEXT_MUSIC");
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction("com.mypackage.ACTION_PREV_MUSIC");
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }





}
