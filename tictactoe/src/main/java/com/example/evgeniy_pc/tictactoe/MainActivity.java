package com.example.evgeniy_pc.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity implements View.OnClickListener
{

    Button play_with_android, two_players, setings, InetServer, online, bluetooth,exit;

    SharedPreferences musicBg;
    final String STATUS_MUSIK = "status_musik";
    final String STATUS_WELCOME_SCREEN = "status_welcome_screan";

    boolean savedStatusMusic;
    boolean savedStatusWelcomeScreen;

    int rez;

    final String LOG_TAG = "myLogs";
//--------------------------------- Метод создания Активити ----------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Создаем плеер с определенным треком
        //player = MediaPlayer.create(MainActivity.this, R.raw.sound);

        // Находим все кнопки
        play_with_android = (Button)findViewById(R.id.play_with_android);
        two_players = (Button)findViewById(R.id.two_players);
        online = (Button) findViewById(R.id.online);
        bluetooth = (Button) findViewById(R.id.bluetooth);
        setings = (Button)findViewById(R.id.setings);
        exit = (Button) findViewById(R.id.exit);

        //добавление стиля шрифта
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/pacifico.ttf");
        play_with_android.setTypeface(tf);
        two_players.setTypeface(tf);
        online.setTypeface(tf);
        bluetooth.setTypeface(tf);
        setings.setTypeface(tf);
        exit.setTypeface(tf);

        //------------------------------------------------------------------------------------------ Считываем значение настроек по этому активити
        // С переменной настроек читаем информацию которая хранится в
        // STATUS_MUSIK если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusWelcomeScreen = musicBg.getBoolean(STATUS_WELCOME_SCREEN, true);

        if (savedStatusWelcomeScreen)
        {
        //Вызов стартового активити
        Intent intentFirstActivity = new Intent(this, FirstActivity.class);
        startActivityForResult(intentFirstActivity, 1);
        }

    }

//------------------ Метод вызывается перед тем как будет отображено это активити -----------------
    @Override
    protected void onResume()
    {
        super.onResume();
        // С переменной настроек читаем информацию котороя хранится в
        // STATUS_MUSIK если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusMusic = musicBg.getBoolean(STATUS_MUSIK, true);

        if (savedStatusMusic)
        startService(new Intent(this, SoundBgraund.class)); //вот єто вам нужно написать!!!!!!
    }
//----------------------- Метод вызывается перед тем как будет свернуто ---------------------------
    @Override
    protected void onPause()
    {
        super.onPause();
        stopService(new Intent(this, SoundBgraund.class));
    }


//------------------------------ Обработчик нажатия на кнопки --------------------------------------
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.play_with_android:
                Intent intentPlayWithAndroid = new Intent(this, PlayWithAndroid.class);
                startActivity(intentPlayWithAndroid);
                break;
            case R.id.two_players:
                Intent intentTwoPlayrs = new Intent(this, TwoPlayrs.class);
                startActivity(intentTwoPlayrs);
                break;
            case R.id.online:
                Intent intentClient = new Intent(this, InternetClient.class);
                startActivity(intentClient);
                break;
            case R.id.bluetooth:
                Intent intentBluetooth = new Intent(this, StartBluetooth.class);
                startActivity(intentBluetooth);
                break;
            case R.id.setings:
                Intent intentSetings = new Intent(this, Settings.class);
                startActivity(intentSetings);
                break;
            case R.id.exit:
                finish();
                break;
        }
    }
//-------------------------- Наш класс Работает Синхронно проигрывает музыку в фоне ----------------

}