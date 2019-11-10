package com.example.mediaplayer.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class NofiticationCenter extends Application {
    public static final String channel_1_ID="channel1";




    @Override
    public void onCreate() {
        super.onCreate();
        createNofitication();
     }

   private void createNofitication(){
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        NotificationChannel ch1=new NotificationChannel(channel_1_ID,"Song", NotificationManager.IMPORTANCE_LOW);
        ch1.setDescription("HEY");
        NotificationManager manager=getSystemService(NotificationManager.class);
        manager.createNotificationChannel(ch1);
    }

   }




}
