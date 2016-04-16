package com.example.evgeniy_pc.tictactoe;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


public class SoundBgraund extends Service
{
    private static final String LOG_TAG = "MyService";
    MediaPlayer player;


    String STATUS_MUSIC = "status_music";
    @Override
    public void onCreate()
    {
        //Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();

        player = MediaPlayer.create(this, R.raw.sound);
        player.setLooping(true); // зацикливаем


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Intent myint = new Intent();
        myint = intent;
        int st = myint.getIntExtra(STATUS_MUSIC,1);

       if (st==1)
       {
           player.start();
       }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        //Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        player.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startid)
    {
       // Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();

    }
}


