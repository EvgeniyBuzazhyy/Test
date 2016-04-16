package com.example.evgeniy_pc.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;


public class FirstActivity extends Activity implements SoundPool.OnLoadCompleteListener
{
    ImageView[] im = new ImageView[9];
    Animation animComboClic = null;
    Handler h;
    final int STATUS_ADD = 1;

    int[] forma = {0,4,1,2,6,3,8,5,7};


    SoundPool sp;
    int soundIdShot;
    int soundIdShotSpp;
    final int MAX_STREAMS = 5;

    //-----------------------------------
    SharedPreferences musicBg;
    final String STATUS_MUSIK = "status_musik";
    final String STATUS_SOUND = "status_sound";

    boolean savedStatusSound;
    boolean savedStatusMusic;

//--------------------------------- Метод создания Активити ----------------------------------------
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        // Анимацыя
        animComboClic = AnimationUtils.loadAnimation(this, R.anim.mco);


        // Короткая музыка при нажатии на кнопки
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener( this);
        // Присвоение треков
        soundIdShot = sp.load(this, R.raw.sou, 1);
        soundIdShotSpp = sp.load(this, R.raw.winn, 1);

        // ------- Нахождение Изображений
        im[0] = (ImageView) findViewById(R.id.imageView1);
        im[1] = (ImageView) findViewById(R.id.imageView2);
        im[2] = (ImageView) findViewById(R.id.imageView3);
        im[3] = (ImageView) findViewById(R.id.imageView4);
        im[4] = (ImageView) findViewById(R.id.imageView5);
        im[5] = (ImageView) findViewById(R.id.imageView6);
        im[6] = (ImageView) findViewById(R.id.imageView7);
        im[7] = (ImageView) findViewById(R.id.imageView8);
        im[8] = (ImageView) findViewById(R.id.imageView9);
        //------------------------------------------------------------------------------------------
        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_MUSIK если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusMusic = musicBg.getBoolean(STATUS_MUSIK, true);

        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_SOUND если true то мы будем играть ЗВУКИ
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusSound = musicBg.getBoolean(STATUS_SOUND, true);
        //------------------------------------------------------------------------------------------

        playMusic(); // запуск фоновой музыки

        //------------------------- Хендлер с потока принемает сообщения ---------------------------
        h = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                switch (msg.what)
                {// приходит сообжение с потока с ID
                    case STATUS_ADD:
                        // присваеваем атрибут arg1
                        int data = msg.arg1;
                        // если атрибут не равен 10 то передаем данные для отображения
                        if (msg.arg1!=10)
                        {
                            sedIm(data);
                        }
                        // если атрибут равен 10 то
                        if (msg.arg1==10)
                        {
                            Intent intent = new Intent();
                            intent.putExtra("rez",8);
                            // результат вывода активити ОК
                            setResult(RESULT_OK, intent);
                            // Закрываем активити
                            finish();
                            // Останавливаем проигравание музыки Выиграша
                            sp.autoPause();
                        }
                        break;
                }
            };
        };
//------------------------------------- Сам поток --------------------------------------------------
        Thread t = new Thread(new Runnable()
        {
            Message msg;
            public void run()
            {
                //--------------------------- Ждем 1 секунду ---------------------------------------
                try
                {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                //--------------------------- 9 раз отрабатываем с паузой --------------------------
                try
                {
                    for (int i=0; i<9; i++)
                    {
                        msg = h.obtainMessage(STATUS_ADD, i, 0);
                        h.sendMessage(msg);
                        TimeUnit.MILLISECONDS.sleep(500);
                    }

                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                //--------------------------- Ждем 4 секунды и отсылаем сообщение ------------------
                try
                {
                    TimeUnit.SECONDS.sleep(4);
                    msg = h.obtainMessage(STATUS_ADD, 10, 0);
                    h.sendMessage(msg);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        // старт этого потока
        t.start();
    }

    //---------------------------- Проигрование музыки в фоне --------------------------------------
    public void playMusic()
    {
        if (savedStatusMusic)
            startService(new Intent(this, SoundBgraund.class)); //старт сервис с музыкой фоновой

    }
    //----- Проигрование музыки в фоне ---
    public void playSoundClic()
    {
        if (savedStatusSound)
            sp.play(soundIdShot, 1, 1, 0, 0, 1); // проигрование короткого трека
    }//----- Проигрование звуков ----
    public void playSoundWin()
    {
        if (savedStatusSound)
            sp.play(soundIdShotSpp, 1, 1, 0, 0, 1); // Проигрование короткого Выйграшного трека
    }

    //------------------ Метод вызываеться перед тем как будет отображено это активити -----------------
    @Override
    protected void onResume()
    {
        super.onResume();
        playMusic(); // запуск фоновой музыки

    }
    //----------------------- Метод вызываеться перед тем как будет свернуто ---------------------------
    @Override
    protected void onPause()
    {
        super.onPause();
        stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
        sp.autoPause();

    }


//------------------------------------ Метод который присваивает картинки --------------------------
    public void sedIm(int data)
    {
        // Если дата при делени на 2 равно 0 то рисуем крестик
        if (data%2==0)
        {
            im[forma[data]].setImageResource(R.drawable.kr300x300);  // На кнопу изображение
            im[forma[data]].startAnimation(animComboClic);    // устанавка анимаци
            playSoundClic(); // Проинрование звуков
        }
        else
        {
            im[forma[data]].setImageResource(R.drawable.nolik300x300);  // На кнопу изображение
            im[forma[data]].startAnimation(animComboClic);       // устанавка анимаци
            playSoundClic(); // Проинрование звуков
        }
        // при последнем ходе зарисовуем все иконки
        if (data == 8)
        {
            //Блок красных крестиков
            im[3].setImageResource(R.drawable.nolik_w300x300);
            im[4].setImageResource(R.drawable.nolik_w300x300);
            im[5].setImageResource(R.drawable.nolik_w300x300);
            //Блок затемненых
            im[0].setImageResource(R.drawable.kr_n1300x300);
            im[1].setImageResource(R.drawable.kr_n1300x300);
            im[2].setImageResource(R.drawable.nolik_nichya300x300);
            im[6].setImageResource(R.drawable.kr_n1300x300);
            im[7].setImageResource(R.drawable.kr_n1300x300);
            im[8].setImageResource(R.drawable.kr_n1300x300);

            stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
            // Запуск короткого трека при выграше
            playSoundWin(); // Проигрование выйграшного трека
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
    {

    }
}
