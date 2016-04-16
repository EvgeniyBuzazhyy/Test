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
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.evgeniy_pc.tictactoe.R.drawable.frame_layout_players;


public class TwoPlayrs extends Activity implements View.OnClickListener, SoundPool.OnLoadCompleteListener
{
    final int DIALOG_EXIT = 1;
    //------------------------
    Animation animComboClic = null;
    ImageView im[] = new ImageView[9];

    ImageView im_play_plaer2, im_play_player1;

    TextView text, text_scote_player2, text_scote_player1;
    //-----------------------

    // Масив где будут храниться значения адресов рисунков для игры
    int addres[];

    Button newGame, back;


    //---------------------
    SharedPreferences musicBg;
    final String STATUS_MUSIK = "status_musik";
    final String STATUS_SOUND = "status_sound";

    //Переменные в которые записоваються значения НАСТРОЕК
    boolean savedStatusSound;
    boolean savedStatusMusic;
    ///-------------------------

    // --- Музыка при нажатии на кнопку
    SoundPool sp;
    int soundIdShot;
    int soundIdShotSpp;
    final int MAX_STREAMS = 5;
    //---------------------------------

    //---- Переменные щета
    int score_win_player1 = 0,score_win_player2 = 0;
    int score_newGame = 0;


    final String LOG_TAG = "myLogs";
    int mas[] = new int [9];       // масив из 9 кнопок

    int arrayVrem[] = new int [9]; // масив для сверки

    int  pustoyChenterOrNot = 0 ;
    int sh=0;

    int queue = 0; // порядок хода игроков

    int statusCompHoda=0;

    // константы которые опредиляют победителя
    int win1=0,win2=0,win3=0;
    int tri=3;

    // результат и остановка игры
    int rezult=0;
    int stopIgra=0;
    //-------------------------
    // Поля которые выделяються и подсказывают кто ходит сейчас
    LinearLayout frameLayout_player2;
    LinearLayout frameLayout_player1;

    // Текстовое поле подсведчиваеться красным при выйграше
    TextView text_player2;
    TextView text_player1;

    // Масив кнопок с ID
    int nameBotton[] = new int [9];

    //-------------------------------- Метод создания Активити -----------------------------------------
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_player);

        // Нахождение всех ВЬЮ задействоных в коде
        text = (TextView) findViewById(R.id.text);
        frameLayout_player2 = (LinearLayout) findViewById(R.id.frameLayout_player2);
        frameLayout_player1 = (LinearLayout) findViewById(R.id.frameLayout_player1);
        im_play_plaer2 = (ImageView) findViewById(R.id.im_play_plaer2);
        im_play_player1 = (ImageView) findViewById(R.id.im_play_player1);
        text_player2 = (TextView) findViewById(R.id.text_player2);
        text_player1 = (TextView) findViewById(R.id.text_player1);
        text_scote_player2 = (TextView) findViewById(R.id.text_scote_player2);
        text_scote_player1 = (TextView) findViewById(R.id.text_scote_player1);
        newGame = (Button) findViewById(R.id.newGame);
        back = (Button) findViewById(R.id.back);

        //добавление стиля шрифта
        Typeface tff = Typeface.createFromAsset(getAssets(),"fonts/pacifico.ttf");
        newGame.setTypeface(tff);
        back.setTypeface(tff);

        // ------- Анимацыя
        animComboClic =  AnimationUtils.loadAnimation(this, R.anim.mco);

        //-------- создание масива для адресов картинок
        addres = new int[6];
        addres[0]=R.drawable.kr300x300haki;
        addres[1]=R.drawable.kr_n1300x300;
        addres[2]=R.drawable.kr_w300x300;

        addres[3]=R.drawable.nolik300x300g;
        addres[4]=R.drawable.nolik_nichya300x300;
        addres[5]=R.drawable.nolik_w300x300;

        //------ Кто какими иконками играет -----
        im_play_plaer2.setImageResource(R.drawable.ic_and_g);
        im_play_player1.setImageResource(R.drawable.ic_you_h);

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

        //Игрок - 1 ваш ход!
        setBackGplayer1();

        //---------- Загрузка музыкальных коротких треков ----------
        // Короткая музыка при нажатии на кнопки
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener( this);
        // Присвоение треков
        soundIdShot = sp.load(this, R.raw.sou, 1);
        soundIdShotSpp = sp.load(this, R.raw.winn, 1);

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

    //------------------ Метод вызываеться перед тем как будет отображено это активити ------------- Подготовка активити перед показом
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

        // Метод запуска проигрования фоновой музыки
        playMusic();
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
    //----- Проигрование звука нажатия ---
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

   // ---------------------------------------------------------------------------------------------- 2-ва метода присвоениия цвету
   public void setBackGplayer2()
   {
       // присвоение цвета нашему фрейму
       frameLayout_player1.setBackgroundResource(0);
       frameLayout_player2.setBackgroundResource(R.drawable.frame_layout_players);

   }
   public void setBackGplayer1()
   {
       // присвоение цвета нашему фрейму

       frameLayout_player2.setBackgroundResource(0);
       frameLayout_player1.setBackgroundResource(R.drawable.frame_layout_players);
   }
   // ---------------------------------------------------------------------------------------------- Конец

    //------------------------------ Обработчик нажатия на картинки ------------------------------------
    // вызываеться при нажатии на кнопку
    @Override
    public void onClick(View v)
    {
        //Если была нажата кнопка назад Back
        if (v.getId() == R.id.back)
        {
            onBackPressed();
        }
        // ---  Условие срабатывает если была нажата кнопка НАСТРОЙКИ ---
        if (v.getId() == R.id.settings)
        {
            Intent intentSettings = new Intent(this, Settings.class);
            startActivity(intentSettings);
        }
        // --- Условие срабатывает если была нажата кнопка НОВАЯ ИГРА ---
        if (v.getId() == R.id.newGame)
        {
            //--------------- Новая игра Обновление всех переменных ---------------------------
            // Стираем красный  цвет с текста
            text_player1.setTextColor(getResources().getColor(R.color.playerNewGame));
            text_player2.setTextColor(getResources().getColor(R.color.playerNewGame));

            sp.autoPause(); // /СТОП Проигрование короткого Выйграшного трека
            playMusic();  // Метод запуска проигрования фоновой музыки

            score_newGame++;   //Каждый раз увиличение на 1 при новой игре
            text.setText("");  // Значение текстового поля
            setBackGplayer1(); //Игрок - 1 ваш ход!
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

            // --- Условие при котором игроки будут играть разными иконками -----

            if (score_newGame%2 == 0)
            {
                addres[0]=R.drawable.kr300x300haki;
                addres[1]=R.drawable.kr_n1300x300;
                addres[2]=R.drawable.kr_w300x300;
                addres[3]=R.drawable.nolik300x300g;
                addres[4]=R.drawable.nolik_nichya300x300;
                addres[5]=R.drawable.nolik_w300x300;
                //------ Кто какими иконками играет -----
                im_play_plaer2.setImageResource(R.drawable.ic_and_g);
                im_play_player1.setImageResource(R.drawable.ic_you_h);
            }
            else
            {
                addres[0]=R.drawable.nolik300x300haki;
                addres[1]=R.drawable.nolik_nichya300x300;
                addres[2]=R.drawable.nolik_w300x300;
                addres[3]=R.drawable.kr300x300g;
                addres[4]=R.drawable.kr_n1300x300;
                addres[5]=R.drawable.kr_w300x300;

                //------ Кто какими иконками играет -----
                im_play_plaer2.setImageResource(R.drawable.ic_you_g);
                im_play_player1.setImageResource(R.drawable.ic_and_h);
            }
        }

        // ----------  Цыкл который проверяет Кнопку которая была нажата -----
        for (int it=0; it<9; it++)
        {
            if (v.getId() ==  nameBotton[it])
            {
                if (stopIgra==0&&mas[it]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[it].setImageResource(addres[0]);
                        im[it].startAnimation(animComboClic);// устанавка анимаци
                        mas[it] = 1;
                        queue=1;
                        setBackGplayer2();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[it].setImageResource(addres[3]);
                        im[it].startAnimation(animComboClic);// устанавка анимаци
                        mas[it] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            }
        }

    }//------------------------- кОНЕЦ ОБРАБОТЧИКА НАЖАТИЙ НА КНОПКУ -------------------------------

//-------------------------------------------------------------------------------------------------- Метод определения выграшных комбинацый
    public void Otres()
    {
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

        // если находим выйграшную комбинацыю отрисовуем и СТОП игра
        // ----------------------------------------------------------------------------------------- Услове проверяет на виграш 1-го игрока 1-рым
        if (mas[win1]+mas[win2]+mas[win3]==3&&one_string_full==1)
        {
            im[win1].setImageResource(addres[2]);
            im[win2].setImageResource(addres[2]);
            im[win3].setImageResource(addres[2]);


            frameLayout_player1.setBackgroundResource(0);
            frameLayout_player2.setBackgroundResource(0);


            score_win_player1++;
            text_scote_player1.setText(""+ score_win_player1);
            text_player1.setTextColor(getResources().getColor(R.color.playerWin));
            text.setText("Player1 win!");
            stopIgra=1;

            stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
            playSoundWin(); //проигрование выйграшной музыки

        }

        // ----------------------------------------------------------------------------------------- Услове проверяет на виграш 2-го игрока 2-рым
        if (mas[win1]+mas[win2]+mas[win3]==-3&&one_string_full==1)
        {
            im[win1].setImageResource(addres[5]);
            im[win2].setImageResource(addres[5]);
            im[win3].setImageResource(addres[5]);


            frameLayout_player1.setBackgroundResource(0);
            frameLayout_player2.setBackgroundResource(0);

            score_win_player2++;
            text_scote_player2.setText(""+ score_win_player2);
            text_player2.setTextColor(getResources().getColor(R.color.playerWin));
            text.setText("Player2 win!");
            stopIgra=1;

            stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
            playSoundWin(); //проигрование выйграшной музыки
        }

        // ----------------------------------------------------------------------------------------- Услове проверяет на НИЧЬЮ 3-тим
        if (mas[0]!=0&&mas[1]!=0&&mas[2]!=0&&mas[3]!=0&&mas[4]!=0&&mas[5]!=0&&mas[6]!=0&&mas[7]!=0&&mas[8]!=0)
        {
            rezult=3;  //1
            nicya();   //2
            stopIgra=1;//3
        }
    }

    //---------------------------------------------------------------------------------------------- Метод который прорисовует ничью
    public  void nicya()
    {
        if (rezult==3&&stopIgra!=1)
        {
            //Подсветку очереди ходов обнуляем
            frameLayout_player1.setBackgroundResource(0);
            frameLayout_player2.setBackgroundResource(0);
            text.setText("Ничья!");

            for (int nichya = 0; nichya < mas.length; nichya++)
            {
                if (mas[nichya] == 1) {
                    im[nichya].setImageResource(addres[1]);
                    im[nichya].startAnimation(animComboClic);// устанавка анимаци
                }
                if (mas[nichya] == -1) {
                    im[nichya].setImageResource(addres[4]);
                    im[nichya].startAnimation(animComboClic);// устанавка анимаци
                }
            }
        }
    }

    //Метод который отвечает за асинхронную загрузку коротких треков для проигрования
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }
}
