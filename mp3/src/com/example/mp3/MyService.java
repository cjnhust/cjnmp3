package com.example.mp3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bathust on 14-12-19.
 */
public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Context context;
    private final static String TAG="MY_SERVICE";
    private MediaPlayer mediaPlayer;
    MyActivity myActivity=new MyActivity();
    ArrayList path_song1=myActivity.path_song;
    public void OnCreat(){
        super.onCreate();




    }
    public void OnStart(Intent intent,int startid){
        super.onStart(intent, startid);
        if (intent!=null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int position=bundle.getInt("position");
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(String.valueOf(path_song1.get(position)));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }





    }
    public void OnDestroy(){
        super.onDestroy();
    }


}
