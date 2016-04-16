package com.example.evgeniy_pc.tictactoe;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class PlayWithAndroid extends Activity implements View.OnClickListener, SoundPool.OnLoadCompleteListener
{

    //---------------------------------------------------------------------------------------------- Обявление переменных класса
    final int DIALOG_EXIT = 1;
    Animation animComboClic = null;
    ImageView im[] = new ImageView[9];

    ImageView whoNowPlays, im_play_android, im_play_player;
    TextView text , score_and, score_pal , level_android;
    Button newGame, back;
    //-----------------------\
    //---- Переменные щета
    int score_win_player = 0, score_win_android = 0;
    int score_newGame = 0;

    //---------------------
    SharedPreferences musicBg;
    final String STATUS_MUSIK = "status_musik";
    final String STATUS_SOUND = "status_sound";
    final String STATUS_LEVEL = "status_level";

    boolean savedStatusSound;
    boolean savedStatusMusic;
    ///-------------------------

    // --- Музыка при нажатии на кнопку
    SoundPool sp;
    int soundIdShot;
    int soundIdShotSpp;
    final int MAX_STREAMS = 5;
    //---------------------------------
    int int_status_level = 0;

    // id адреса картинок с котрыми играем
    int addres[];

    final String LOG_TAG = "myLogs";
    int mas[] = new int [9];       // масив из 9 кнопок

    int arrayVrem[] = new int [9]; // масив для сверки

    int  pustoyChenterOrNot = 0 ;
    // Счетчик рендома 1
    int sh=0;
    // Счетчик рендома 2
    int ren=0;

    int statusCompHoda=0;

    // константы которые опредиляют победителя
    int win1=0,win2=0,win3=0;
    int tri=3;

    // результат и остановка игры
    int rezult=0;
    int stopIgra=0;

    //переменная используеться в методе поиска победителя Otres
    int one_string_full=0;

    Engine engine;

    // Масив кнопок с ID
    int nameBotton[] = new int [9];

    int reandomWinPlayer;


//-------------------------------- Метод создания Активити ----------------------------------------- onCreate
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_with_android);

        whoNowPlays = (ImageView) findViewById(R.id.whoNowPlays);
        text = (TextView) findViewById(R.id.text);
        newGame = (Button) findViewById(R.id.button_newGame);
        back = (Button) findViewById(R.id.back);
        score_and = (TextView) findViewById(R.id.text_scote_android);
        score_pal = (TextView) findViewById(R.id.text_your_score);
        im_play_android = (ImageView) findViewById(R.id.im_play_android);
        im_play_player = (ImageView) findViewById(R.id.im_play_player);
        level_android = (TextView) findViewById(R.id.level_android);

        //добавление стиля шрифта
        Typeface tff = Typeface.createFromAsset(getAssets(),"fonts/pacifico.ttf");
        newGame.setTypeface(tff);
        back.setTypeface(tff);

        // ------- Анимацыя
        animComboClic =  AnimationUtils.loadAnimation(this, R.anim.mco);

        //-------- создание масива
        addres = new int[6];
        addres[0]=R.drawable.kr300x300haki;
        addres[1]=R.drawable.kr_n1300x300;
        addres[2]=R.drawable.kr_w300x300;

        addres[3]=R.drawable.nolik300x300g;
        addres[4]=R.drawable.nolik_nichya300x300;
        addres[5]=R.drawable.nolik_w300x300;

         //im_play_android.setImageResource(R.drawable.ic_you_g);
        //im_play_player.setImageResource(R.drawable.ic_and_h);

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

        //---------- Загрузка музыкальных коротких треков ----------
        // Короткая музыка при нажатии на кнопки
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener( this);
        // Присвоение треков
        soundIdShot = sp.load(this, R.raw.sou, 1);
        soundIdShotSpp = sp.load(this, R.raw.winn, 1);

        engine = new Engine();

        // Запись id адресов кнопок в масив
        nameBotton[0] = R.id.imageView1;
        nameBotton[1] = R.id.imageView2;
        nameBotton[2] = R.id.imageView3;
        nameBotton[3] = R.id.imageView4;
        nameBotton[4] = R.id.imageView5;
        nameBotton[5] = R.id.imageView6;
        nameBotton[6] = R.id.imageView7;
        nameBotton[7] = R.id.imageView8;
        nameBotton[8] = R.id.imageView9;
    }

    //------------------ Метод вызываеться перед тем как будет отображено это активити ------------- Показываем активити перед скрытием
    @Override
    protected void onResume()
    {
        super.onResume();

        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_MUSIK если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusMusic = musicBg.getBoolean(STATUS_MUSIK, true);

        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_SOUND если true то мы будем играть ЗВУКИ
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusSound = musicBg.getBoolean(STATUS_SOUND, true);
        //------------------------------------------------------------------------------------------ Считоваем значение настроек уровня игры
        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_WELCOME_SCREEN если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        int st = musicBg.getInt(STATUS_LEVEL,0);

        switch (st)
        {
            case 1: level_android.setText("Level 1");
                if (!(int_status_level == st))
                {
                    score_and.setText("0");
                    score_pal.setText("0");
                    score_win_player = 0; score_win_android=0;
                    //Если выбран новый уровень то запускаем новую игру
                    newGame();
                }
                int_status_level = st;
                break;

            case 2: level_android.setText("Level 2");
                if (!(int_status_level == st))
                {
                    score_and.setText("0");
                    score_pal.setText("0");
                    score_win_player = 0; score_win_android=0;
                    //Если выбран новый уровень то запускаем новую игру
                    newGame();
                }
                int_status_level = st;
                break;

            case 3: level_android.setText("Level 3");
                if (!(int_status_level == st))
                {
                    score_and.setText("0");
                    score_pal.setText("0");
                    score_win_player = 0; score_win_android=0;
                    //Если выбран новый уровень то запускаем новую игру
                    newGame();
                }
                int_status_level = st;
                break;
        }

        playMusic();  // Метод запуска проигрования фоновой музыки
    }
    //----------------------- Метод вызываеться перед тем как будет свернуто ----------------------- Готовим активити к свертыванию
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("myLogs", "Клиент --- Сработал перед тем свернуто--- ");
        stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой

        sp.autoPause(); // /СТОП Проигрование короткого Выйграшного трека
    }

    //----- Проигрование музыки в фоне ---
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



///---------------------------------------- ВЫЗОВ ДИАЛОГОВОГО ОКНА --------------------------------- Начало диалога!!!
    public Dialog onCreateDialog(int id) // метод создания \ компоновки диалогового окна
    {
        if (id == DIALOG_EXIT)              // диалоговое окно ВЫХОДА
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(R.string.exit);
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton(R.string.yes, myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton(R.string.no, myClickListener);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }
    //--------------------------- Обработчик нажатия на кнопки в диалоговом окне выхода назад ------
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    finish();
                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override                     // при нажатии кнопки назад предложение выйти из игры
    public void onBackPressed()
    {
        // вызываем диалог
        showDialog(DIALOG_EXIT);  // вызов диалогового окна при нажатии на кнопку назад
    }
//-------------------------------------------------------------------------------------------------- Конец диалога!!!


//------------------------------ Обработчик нажатия на картинки ------------------------------------ Нажатия на картинки в игровом поле
    // вызываеться при нажатии на кнопку
    @Override
    public void onClick(View v)
    {
        // ---  Условие срабатывает если была нажата кнопка НАСТРОЙКИ ---
        if (v.getId() == R.id.settings)
        {
            Intent intentSettings = new Intent(this, Settings.class);
            startActivity(intentSettings);
        }
        // --- Условие срабатывает если была нажата кнопка НОВАЯ ИГРА ---
        if (v.getId() == R.id.button_newGame)
        {
            // -- Вызов новой игры  ---
            newGame();
        }
        if (v.getId() == R.id.back)
        {
            // -- Выход из игры  ---
            onBackPressed();
        }

        // ----------  Цыкл который проверяет Кнопку которая была нажата -----
        for (int it=0; it<mas.length; it++)
        {
            if (v.getId() ==  nameBotton[it])
            {
                if (stopIgra==0&&mas[it]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    im[it].setImageResource(addres[0]);
                    im[it].startAnimation(animComboClic);// устанавка анимаци
                    mas[it] = 1;   Log.d("myLogs", "*********** ЗАПИСАЛ *********  "+it+"  ** "+ mas[it]);
                    Otres(true);
                    stepComputer(false);
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                    break;
                }

            }
        }

        // после того как игрок зделает свой выбор запуск роботы компьютера
    }//------------------------- кОНЕЦ ОБРАБОТЧИКА НАЖАТИЙ НА КНОПКУ -------------------------------
    //---------------------------------------------------------------------------------------------- НОВАЯ ИГРА
    public void newGame()
    {
        //--------------- Новая игра Обновление всех переменных ---------------------------
        sp.autoPause(); // /СТОП Проигрование короткого Выйграшного трека
        playMusic();  // Метод запуска проигрования фоновой музыки
        score_newGame++; Log.d("myLogs", "*********** НОВАЯ ИГРА  *********"+score_newGame);
        statusCompHoda=0;
        win1=0; win2=0; win3=0;
        rezult=0;
        pustoyChenterOrNot = 0 ;
        sh=0;
        ren=0;
        stopIgra=0;

        //Определение того выйграет ли игрок в этой игре
        reandomWinPlayer = reandomWinPlayer();

        // ----- Очистка масива значений
        for (int nov = 0; nov<mas.length; nov++)
        {
            mas[nov] = 0;
            arrayVrem[nov]=0;

            im[nov].setImageResource(0);
        }

        text.setText("");

        // --- Условие при котором игроки будут играть разными иконками -----
        if (score_newGame%2 == 0)
        {
            addres[0]=R.drawable.kr300x300haki;
            addres[1]=R.drawable.kr_n1300x300;
            addres[2]=R.drawable.kr_w300x300;
            addres[3]=R.drawable.nolik300x300g;
            addres[4]=R.drawable.nolik_nichya300x300;
            addres[5]=R.drawable.nolik_w300x300;
            im_play_android.setImageResource(R.drawable.ic_and_g);
            im_play_player.setImageResource(R.drawable.ic_you_h);
        }
        else
        {
            addres[0]=R.drawable.nolik300x300haki;
            addres[1]=R.drawable.nolik_nichya300x300;
            addres[2]=R.drawable.nolik_w300x300;
            addres[3]=R.drawable.kr300x300g;
            addres[4]=R.drawable.kr_n1300x300;
            addres[5]=R.drawable.kr_w300x300;
            im_play_android.setImageResource(R.drawable.ic_you_g);
            im_play_player.setImageResource(R.drawable.ic_and_h);

            // Когда очередь хода за Андроидом
            stepComputer(true);
            Otres(false);
        }

    }//---------------------------------------------------------------------------------------------КОНЕЦ НОВАЯ ИГРА
    //Метод рандомного определения пользователя как победителя
    public int reandomWinPlayer()
    {
        if (int_status_level == 3)
        {
            int proc =80;
            int i = (int) (Math.random() * 100);
            if (i <= proc)
            {
                reandomWinPlayer = 1;
            } else {
                reandomWinPlayer = 0;
            }
        }
        return reandomWinPlayer;
    }
//-------------------------------------------------------------------------------------------------- Ход компьютера
    public void stepComputer(boolean version)
    {

        ////////////////////////////////////////////////////////////////////////////////////////////
        statusCompHoda=0;                              // обнуление переменной
        // Если был вызван метод с параметром true то сработает только рендомный вариант хода (вызываеться с позицыии Андроида)
       /* if (version)
        {
            // выбераем позицыю случайно
            int re;
            do
            {
                re = (int) (Math.random() * 9 );
                sh++;
            }
            while (mas[re]!=0&sh!=9);
            pustoyChenterOrNot++;          // изменение переменной при первом запуске
            if(sh!=9)
                statusCompHoda+=1;
            mas[re]=-1;
            im[re].setImageResource(addres[3]);
            im[re].startAnimation(animComboClic);           // устанавка анимаци
            arrayVrem[re] = mas[re];  // копия масива

            playSoundClic(); //проигрование трека при  нажатии на кнопку
            Log.d("myLogs", "Первый отработал  "+ re);

        }*/
        //------------------------------------------------------------------- НАЧАЛО срабатовает один раз при новой игре

        //Random rand;

        if (pustoyChenterOrNot==0)              // переменная которая отрабатывает оди раз при запуске новой игры
        {
            if (mas[4]==0)                     // если Центр пустой
            {
                mas[4]=-1;                     // то ставим в центр
                pustoyChenterOrNot++;          // изменение переменной при первом запуске
                statusCompHoda+=1;
                im[4].setImageResource(addres[3]);
                im[4].startAnimation(animComboClic);// устанавка анимаци
                arrayVrem[4] = mas[4];  // копия масива

                playSoundClic(); //проигрование трека при  нажатии на кнопку
            }// если занято то:
            else
            {

                if (mas[6]==0& statusCompHoda==0)
                {
                    mas[6]=-1;
                    pustoyChenterOrNot++;
                    statusCompHoda+=1;
                    im[6].setImageResource(addres[3]);
                    im[6].startAnimation(animComboClic);           // устанавка анимаци
                    arrayVrem[6] = mas[6];  // копия масива

                    playSoundClic(); //проигрование трека при  нажатии на кнопку*/

                }
                if (mas[8]==0&& statusCompHoda==0)
                {
                    mas[8]=-1;
                    pustoyChenterOrNot++;
                    statusCompHoda+=1;
                    im[8].setImageResource(addres[3]);
                    im[8].startAnimation(animComboClic);           // устанавка анимаци
                    arrayVrem[8] = mas[8];  // копия масива

                    playSoundClic(); //проигрование трека при  нажатии на кнопку*/
                }
                /*
                // выбераем позицыю случайно
                int re;
                do
                {
                    re = (int) (Math.random() * 9 );
                    sh++;
                }
                while (mas[re]!=0&sh!=9);
                pustoyChenterOrNot++;          // изменение переменной при первом запуске
                if(sh!=9)
                    statusCompHoda+=1;
                mas[re]=-1;
                im[re].setImageResource(addres[3]);
                im[re].startAnimation(animComboClic);           // устанавка анимаци
                arrayVrem[re] = mas[re];  // копия масива

                playSoundClic(); //проигрование трека при  нажатии на кнопку*/
            }
            return; // условие отрабатывает и выходид из нашего МЕТОДА
        }

        for (int i=0; i<mas.length; i++)
        {
            Log.d("myLogs", "Отправил масив в движок  --- "+i+"  -  "+ mas[i]);
            Log.d("myLogs", "----------------------------------------------------------- ");
        }

        //------------------------------------------------------------------------------------------ Отправка на движок
        ///////////////////////////////////////////////////////////////////////////////////////////

        // Упаковка масива и статуса хода
        int setElemens[][] =
                {
                {mas[0],mas[1],mas[2],mas[3],mas[4],mas[5],mas[6],mas[7],mas[8]},
                {statusCompHoda},
                {int_status_level},
                {reandomWinPlayer}

                };
        // Прием              Отправка
        int getElements[][] = engine.startEngine(setElemens);

        // ---------- Роспаковка --------
        if (getElements[1][0] == 1)
        {
            statusCompHoda+=1;
            // Присвоение измененных данных в масиве
            for (int i=0; i<9; i++)
            {
                //Из  масива который пришол вытаскиваем измененные данные
                mas[i] = getElements[0][i];
            }
        }
        //------------------------------------------------------------------------------------------ Уже принял с движка
        ////////////////////////////////////////////////////////////////////////////////////////////
// --------------------- Если движок не нашол подходящей позицыии отрабатывает рендом

        if (statusCompHoda==0)
        {
            int re1;

            for (int i=0; i<mas.length; i++)
            {
                Log.d("myLogs", "Масив после хода ДВИЖКА --- "+i+"  -  "+ mas[i]);
            }

            do
            {
                re1 = (int) (Math.random() * 9 );
                ren++;
                Log.d("myLogs", "Клиент --- Рендом ищет подходящую клетку--- "+ re1);
            }
            while (mas[re1]!=0 & ren!=100);

            // mas[re1]!=0 - находит пустое место ren!=30 - сколько раз отработает рендом
            if (ren!=100)
            {
                mas[re1] = -1;
            }

            Log.d("myLogs", "Клиент ------------------------------------------- "+ re1);
        }// конец алгоритма который отрабатывает рендомно

        //  ОТРИСОВКА НЕПОСРЕДСТВЕНО ПО ЯЧЕЙКИ КОТОРУЮ ВЫБРАЛ КОМП
//---------------------- Отрисовка выбора компа ---------------------------------------------------- Отрисовка ячейки которой выбрал компьютер



        for (int pr=0; pr<mas.length; pr++)
        {
            if (arrayVrem[pr]!= mas[pr]&&mas[pr] == -1)
            {
                im[pr].setImageResource(addres[3]);  // НОЛИКИ
                im[pr].startAnimation(animComboClic);  // устанавка анимаци
                arrayVrem[pr]=mas[pr];                           // запись елимента которого небыло (сделано для того чтоб не перерисовувать все елименты)

                playSoundClic(); //проигрование трека при  нажатии на кнопку
            }
        }

        // Вызов метода который определяет победителя передаем false (чтоб определял победителя как Андроида)
        Otres(false);
    }//stepComputer
//-------------------------------------------------------------------------------------------------- Метод поиска победителя ходов
    public void Otres(boolean whoColl)
    {
        one_string_full=0;
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

///////////////////////////////////////////////////////////////////////////////
        // Условие направляет програму на показ победителя (если есть )
        // Работает с булевым значением которое пришло вместе с запроссом
        if (whoColl)
        {// true
            youWin();
        }
        else
        {// false
            androidWin();
        }

//-------------------------- Вычисление ничьии ----------------------------------------------------- Вычисление ничьии
        if (mas[0]!=0&&mas[1]!=0&&mas[2]!=0&&mas[3]!=0&&mas[4]!=0&&mas[5]!=0&&mas[6]!=0&&mas[7]!=0&&mas[8]!=0)
        {
            rezult=3;
            if (stopIgra!=1)
            {
                text.setText("Ничья!");
                nicya();
            }
            stopIgra=1;
        }
    }// Конец Otres

    // --- Метод показывваает что вы Выйграли ---
    public void youWin()
    {
        // если находим выйграшную комбинацыю отрисовуем и СТОП игра

        if (stopIgra!=1)
        {
            if (mas[win1] + mas[win2] + mas[win3] == 3 && one_string_full == 1) {
                // --- Устанавливаем щетчик выиграшей для игрока
                score_win_player++;

                score_pal.setText("" + score_win_player);

                im[win1].setImageResource(addres[2]);
                im[win2].setImageResource(addres[2]);
                im[win3].setImageResource(addres[2]);

                text.setText("You Win!");
                stopIgra = 1;

                stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
                playSoundWin(); //проигрование выйграшной музыки

            }
        }
    }
    // --- Метод показывает что выиграл Андроид ---
    public void androidWin()
    {
        if (stopIgra!=1)
        {
            if (mas[win1] + mas[win2] + mas[win3] == -3 && one_string_full == 1)
            {
                // --- Устанавливаем щетчик выиграшей для андроид
                score_win_android++;
                score_and.setText("" + score_win_android);

                im[win1].setImageResource(addres[5]);
                im[win2].setImageResource(addres[5]);
                im[win3].setImageResource(addres[5]);

                text.setText("Android Win!");
                stopIgra = 1;

                stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
                playSoundWin(); //проигрование выйграшной музыки
            }
        }
    }
    // --- Метод показывает что ничья ---
    public  void nicya()
    {
        if (stopIgra!=1)
        {
            // если у нас ничья то зарисовуем все поля

            for (int nichya = 0; nichya < mas.length; nichya++)
            {
                if (mas[nichya] == 1)
                {
                    im[nichya].setImageResource(addres[1]);
                    im[nichya].startAnimation(animComboClic);// устанавка анимаци
                }
                if (mas[nichya] == -1)
                {
                    im[nichya].setImageResource(addres[4]);
                    im[nichya].startAnimation(animComboClic);// устанавка анимаци
                }
            }
        }
    }

    //Метод который отвечает за асинхронную загрузку коротких треков для проигрования
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
    { }

    class ThreadForAndroid implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}// класс
