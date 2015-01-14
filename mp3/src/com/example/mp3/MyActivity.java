package com.example.mp3;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyActivity extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener {
    /**
     * Called when the activity is first created.
     */

    private PlugReceiver PlugReceiver;
    private ListView listView;
    private ArrayList name;
    private ImageButton ibn_play;
    MediaPlayer mediaPlayer =new MediaPlayer();
    ArrayList path_song =new ArrayList();
    private ImageButton ibn_stop;

    private Button bn_circle;
    private Button bn_random;
    boolean able =false;
    boolean can=false;
    private Handler handle;
//A method for playing
    public void mediaplay(int p){

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(String.valueOf(path_song.get(p)));
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void registerPlugReceiver() {
        PlugReceiver = new PlugReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(PlugReceiver, intentFilter);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.title);
        registerPlugReceiver();




        bn_circle=(Button)findViewById(R.id.circle);
        bn_random=(Button)findViewById(R.id.random);
        listView=(ListView)findViewById(R.id.listView);

        name=new ArrayList();
        ibn_play=(ImageButton)findViewById(R.id.play);
        ibn_stop=(ImageButton)findViewById(R.id.stop);
        bn_circle.setOnClickListener(this);
        ibn_play.setOnClickListener(this);
        ibn_stop.setOnClickListener(this);
        bn_random.setOnClickListener(this);


//To get the names and the paths of files
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            File[] files = path.listFiles();
            getFileName(files);
        }
        SimpleAdapter adapter = new SimpleAdapter(MyActivity.this, name,
                R.layout.item, new String[] { "Name" },
                new int[] { R.id.body });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
    private void getFileName(File[] files) {
        if (files != null)
        {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileName(file.listFiles());
                } else {
                    String fileName = file.getName();


                    if (fileName.endsWith(".mp3")) {
                        int i=0;
                        String filePath=file.getAbsolutePath();

                        HashMap map = new HashMap();
                        String s = fileName.substring(0,
                                fileName.lastIndexOf("."));
                        map.put("Name", fileName.substring(0,fileName.lastIndexOf(".")));
                        path_song.add(filePath);

                        name.add(map);

                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
        ibn_play.setImageResource(R.drawable.pause);
        can=true;
        mediaplay(position);
              mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                  @Override
                  public void onCompletion(MediaPlayer mediaPlayer) {
                      if(able==true){
                          if (position < (int) path_song.size() - 1) {
                             mediaplay(position+1);}
                          else if (position == (int) path_song.size() - 1) {
                              mediaplay(0);}
                      }
                      else{
                          ibn_play.setImageResource(R.drawable.play);
                      }
                  }

              });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play:
                if (can==false){
                    Toast.makeText(MyActivity.this,"请选择歌曲播放",Toast.LENGTH_SHORT).show();
                }

                else{ if (mediaPlayer.isPlaying() != false) {

                    mediaPlayer.pause();
                    ibn_play.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    ibn_play.setImageResource(R.drawable.pause);
                }}
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()!=false){
                    int i=0;
                    mediaPlayer.seekTo(i);
                    mediaPlayer.pause();
                    ibn_play.setImageResource(R.drawable.play);

                }
                break;
            case R.id.circle:
                if(able==false){
                    bn_circle.setText("不循环播放");
                    able =true;
                }
                else {
                    able=false;
                    bn_circle.setText("循环播放");
                }
                break;
            case R.id.random:
                can=true;
                int len =path_song.size();
                double a=Math.random()*len;
                ibn_play.setImageResource(R.drawable.pause);
                int position =(int)a;
                mediaplay(position);
                ibn_play.setImageResource(R.drawable.pause);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        ibn_play.setImageResource(R.drawable.play);
                    }
                });
                break;
        }
    }




    public class PlugReceiver extends BroadcastReceiver {
        private static final String TAG = "PlugReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")){
                if (intent.getIntExtra("state", 0) == 0){
                    if (mediaPlayer.isPlaying()==true){
                    mediaPlayer.pause();
                    }

                }
                else if (intent.getIntExtra("state", 0) == 1){
                    if (mediaPlayer!=null){
                    mediaPlayer.start();
                    }
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        unregisterReceiver(PlugReceiver);
        super.onDestroy();
    }


}

