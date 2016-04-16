package com.example.evgeniy_pc.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class InternetServer extends Activity implements View.OnClickListener
{
    Button chat;
    Intent intentchat;

    final String LOG_TAG = "myLogs";
    int mas[] = new int [9];       // масив из 9 кнопок

    int arrayVrem[] = new int [9]; // масив для сверки

    int  pustoyChenterOrNot = 0 ;
    int sh=0;

    int statusCompHoda=0;

    int queue = 0; // порядок хода игроков

    // константы которые опредиляют победителя
    int win1=0,win2=0,win3=0;
    int tri=3;

    // результат и остановка игры
    int rezult=0;
    int stopIgra=0;

    int one,two, tray;  // переменные для заполнения в масив данных
//-----------------------------------------------------------------------------------
    Ser serv;
    int krest[] = new int[9];

    ImageView im[] = new ImageView[9];

    FrameLayout frameL1[] = new FrameLayout[9];
    Animation animCombo = null;
    Animation animComboClic = null;
    TextView text_who_play;

//----------------------------------------------------------------------
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_server);

        animCombo =  AnimationUtils.loadAnimation(this, R.anim.mycombo);
        animComboClic =  AnimationUtils.loadAnimation(this ,R.anim.mco);

        frameL1[0] = (FrameLayout) findViewById(R.id.frameL1);
        frameL1[1] = (FrameLayout) findViewById(R.id.frameL2);
        frameL1[2] = (FrameLayout) findViewById(R.id.frameL3);
        frameL1[3] = (FrameLayout) findViewById(R.id.frameL4);
        frameL1[4] = (FrameLayout) findViewById(R.id.frameL5);
        frameL1[5] = (FrameLayout) findViewById(R.id.frameL6);
        frameL1[6] = (FrameLayout) findViewById(R.id.frameL7);
        frameL1[7] = (FrameLayout) findViewById(R.id.frameL8);
        frameL1[8] = (FrameLayout) findViewById(R.id.frameL9);

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

        text_who_play = (TextView) findViewById(R.id.text_who_play);

       //-------------------------------------------------------------------------------------------
        for (int i=0; i<9; i++)
        {
           // im[i].setOnClickListener(cl);           // На каждую кнопку вешаем слушателя
            //  im[i].setBackgroundColor(R.color.my);  // На кнопу изображение
            // im[i].startAnimation(animCombo);        // и анимацыю
            frameL1[i].startAnimation(animCombo);     // анимацыя на наш внешный фрейм
        }
        //------------------------------------------------------------------------------------------

        // Иницыализацыя нашего класса слушателя


        intentchat = new Intent(this, ChatSer.class);
        chat = (Button) findViewById(R.id.call_chat);

        //------------------------------ Вызываем Чат ---------------------------------------------
                chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intentchat.putExtra("mes", 1);
                startActivity(intentchat);
            }
        });//-------------------------------------------------------------------- Конец вызова чата


        serv = new Ser(this); // создание сервера
    }




    @Override
    public void onClick(View v)
    {
        String poz;


        switch (v.getId())
        {

            case R.id.newGame:
                //--------------- Новая игра Обновление всех переменных ---------------------------
                text_who_play.setText("Игрок - 1 ваш ход!");
                statusCompHoda=0;
                win1=0; win2=0; win3=0;
                rezult=0;
                pustoyChenterOrNot = 0 ;
                sh=0;
                stopIgra=0;
                queue = 0; // порядок хода игроков


                // ----- Очистка масива значений
                for (int nov = 0; nov<mas.length; nov++)
                {
                    mas[nov] = 0;
                    arrayVrem[nov]=0;

                    im[nov].setImageResource(0);
                }

                break;
            // ------------------------------- Конец нашой очистки и игры снова -----------------


            case R.id.imageView1:
                // защита чтоб игра была закончина

                if (stopIgra==0&&mas[0]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша и ЯЧЕЙКА ДОЛЖНА БЫТЬ ПУСТОЙ == 0
                {
                    if (queue==0)
                    {
                       im[0].setImageResource(R.drawable.nolik);
                       im[0].startAnimation(animComboClic);// устанавка анимаци
                       mas[0] = -1;
                       queue=1;
                       text_who_play.setText("Игрок - 2 ваш ход!");
                        poz = "hhhh";
                       serv.sendKr(poz);
                    }
                   Otres();
                }
                break;
            case R.id.imageView2:
                if (stopIgra==0&&mas[1]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[1].setImageResource(R.drawable.nolik);
                        im[1].startAnimation(animComboClic);// устанавка анимаци
                        mas[1] = -1;
                        queue=1;
                        text_who_play.setText("Игрок - 2 ваш ход!");
                        poz = "g12";
                        serv.sendKr(poz);
                    }

                    Otres();
                }
                break;
            case R.id.imageView3:
                if (stopIgra==0&&mas[2]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[2].setImageResource(R.drawable.nolik);
                        im[2].startAnimation(animComboClic);// устанавка анимаци
                        mas[2] = -1;
                        queue=1;
                        text_who_play.setText("Игрок - 2 ваш ход!");
                        poz = "g22";
                        serv.sendKr(poz);
                    }

                    Otres();
                }
                break;
            //----------------------------------------------

            case R.id.imageView4:
                if (stopIgra==0&&mas[3]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[3].setImageResource(R.drawable.nolik);
                       im[3].startAnimation(animComboClic);// устанавка анимаци
                        mas[3] = -1;
                        queue=1;
                        text_who_play.setText("Игрок - 2 ваш ход!");
                        poz = "g32";
                        serv.sendKr(poz);
                    }

                    Otres();
                }
                break;
            case R.id.imageView5:
                if (stopIgra==0&&mas[4]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[4].setImageResource(R.drawable.nolik);
                        im[4].startAnimation(animComboClic);// устанавка анимаци
                        mas[4] = -1;
                        queue=1;
                        text_who_play.setText("Игрок - 2 ваш ход!");
                        poz = "g42";
                        serv.sendKr(poz);
                    }
                    Otres();
                }
                break;
            case R.id.imageView6:
                if (stopIgra==0&&mas[5]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[5].setImageResource(R.drawable.nolik);
                        im[5].startAnimation(animComboClic);// устанавка анимаци
                        mas[5] = -1;
                        queue=1;
                        text_who_play.setText("Игрок - 2 ваш ход!");
                        poz = "g52";
                        serv.sendKr(poz);

                    }

                    Otres();
                }
                break;
            //----------------------------------------------

            case R.id.imageView7:
                if (stopIgra==0&&mas[6]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[6].setImageResource(R.drawable.nolik);
                        im[6].startAnimation(animComboClic);// устанавка анимаци
                        mas[6] = -1;
                        queue=1;
                        text_who_play.setText("Игрок - 2 ваш ход!");
                        poz = "g62";
                       serv.sendKr(poz);

                    }

                    Otres();
                }
                break;
            case R.id.imageView8:
                if (stopIgra==0&&mas[7]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[7].setImageResource(R.drawable.nolik);
                        im[7].startAnimation(animComboClic);// устанавка анимаци
                        mas[7] = -1;
                        queue=1;
                        text_who_play.setText("Игрок - 2 ваш ход!");
                        poz = "g72";
                        serv.sendKr(poz);

                    }

                    Otres();
                }
                break;
            case R.id.imageView9:
                if (stopIgra==0&&mas[8]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[8].setImageResource(R.drawable.nolik);
                        im[8].startAnimation(animComboClic);// устанавка анимаци
                        mas[8] = -1;
                        queue=1;
                        text_who_play.setText("Игрок - 2 ваш ход!");
                        poz = "g82";
                        serv.sendKr(poz);
                    }

                    Otres();
                }
                break;
        }


    }
    public void Otres()
    {

        //  ОТРИСОВКА НЕПОСРЕДСТВЕНО ПО ЯЧЕЙКИ КОТОРУЮ ВЫБРАЛ КОМП
        //---------------------- Отрисовка выбора компа --------------------------------------------

        if (tray==1)
        {
            mas[one] = two;
        }


        for (int pr=0; pr<mas.length; pr++)
        {
            if (arrayVrem[pr]!= mas[pr]&&mas[pr] == 1)
            {
                im[pr].setImageResource(R.drawable.kr);  // НОЛИКИ
                //im[pr].startAnimation(animComboClic);  // устанавка анимаци
                arrayVrem[pr]=mas[pr];                           // запись елимента которого небыло (сделано для того чтоб не перерисовувать все елименты)
                queue = 0; // чтоб не можно было зделать выбор в поле пока этого не зделает 2 игрок
                tray=0;

            }
        }


        int one_string_full=0;
        tri=3;
        for (int y = 0;y<2; y++)
        {
            if (mas[0] + mas[1] + mas[2] == tri)
            {
                win1=0;win2=1;win3=2; one_string_full=1;
            }
            if (mas[3] + mas[4] + mas[5] == tri)
            {
                win1=3;win2=4;win3=5;  one_string_full=1;
            }
            if (mas[6] + mas[7] + mas[8] == tri)
            {
                win1=6;win2=7;win3=8;  one_string_full=1;
            }
            if (mas[0] + mas[4] + mas[8] == tri)
            {
                win1=0;win2=4;win3=8; one_string_full=1;
            }
            if (mas[2] + mas[4] + mas[6] == tri)
            {
                win1=2;win2=4;win3=6; one_string_full=1;
            }
            if (mas[0] + mas[3] + mas[6] == tri)
            {
                win1=0;win2=3;win3=6; one_string_full=1;
            }
            if (mas[1] + mas[4] + mas[7] == tri)
            {
                win1=1;win2=4;win3=7; one_string_full=1;
            }
            if (mas[2] + mas[5] + mas[8] == tri)
            {
                win1=2;win2=5;win3=8; one_string_full=1;
            }
            tri=-3;
        }

        if (mas[0]!=0&&mas[1]!=0&&mas[2]!=0&&mas[3]!=0&&mas[4]!=0&&mas[5]!=0&&mas[6]!=0&&mas[7]!=0&&mas[8]!=0)
        {
            rezult=3;
            text_who_play.setText("Ничья!");
            if (rezult==3&&stopIgra!=1)
            {
                for (int nichya= 0; nichya<mas.length; nichya++)
                {
                    if(mas[nichya]==1)
                    {
                        im[nichya].setImageResource(R.drawable.kr_n1);
                       // im[nichya].startAnimation(animComboClic);// устанавка анимаци
                    }
                    if(mas[nichya]==-1)
                    {
                        im[nichya].setImageResource(R.drawable.nolik_nichya);
                        //im[nichya].startAnimation(animComboClic);// устанавка анимаци
                    }
                }

            }// конец ничьии
            stopIgra=1;
        }

///////////////////////////////////////////////////////////////////////////////
        // если находим выйграшную комбинацыю отрисовуем и СТОП игра
        if (mas[win1]+mas[win2]+mas[win3]==3&&one_string_full==1)
        {
            im[win1].setImageResource(R.drawable.kr_w);
            im[win2].setImageResource(R.drawable.kr_w);
            im[win3].setImageResource(R.drawable.kr_w);

            text_who_play.setText("Выиграл 1 игрок");
            stopIgra=1;

        }
        if (mas[win1]+mas[win2]+mas[win3]==-3&&one_string_full==1)
        {
            im[win1].setImageResource(R.drawable.nolik_w);
            im[win2].setImageResource(R.drawable.nolik_w);
            im[win3].setImageResource(R.drawable.nolik_w);
            text_who_play.setText("Выиграл 2 игрок");
            stopIgra=1;

        }

        // если у нас ничья то зарисовуем все поля

    }
}
