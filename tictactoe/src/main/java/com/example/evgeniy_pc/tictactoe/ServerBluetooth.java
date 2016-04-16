package com.example.evgeniy_pc.tictactoe;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

// Клас сервер который срабатывает при нажатии кнопки New Game
public class ServerBluetooth extends Activity implements View.OnClickListener, SoundPool.OnLoadCompleteListener
{
    // Константы которые используються при вызове Диалогов
    final int DIALOG_EXIT = 1;
    final int NEW_GAME    = 2;
    final int PLAY_AGEN   = 3;
    final int CONNECTION  = 4;
    // Лог------------------------------------------------------------
    final String LOG_TAG = "myLogs";
    //Масивы ---------------------------------------------------------
    // id адреса картинок с котрыми играем
    int[] addres;                   // id адреса игровых иконок
    int[] mas        = new int [9]; // масив из 9 кнопок
    int[] arrayVrem  = new int [9]; // масив для сверки Временный масив
    int nameBotton[] = new int [9]; // Масив кнопок с ID
    //Интовые переменные-----------------------------------------------
    int sh                =0;
    int statusCompHoda    =0;
    int queue             =0; // порядок хода игроков
    // константы которые опредиляют победителя
    int win1=0,win2=0,win3=0;
    int tri=3;
    // результат и остановка игры
    int rezult=0;
    int stopIgra=0;
    int one, two, tray; // переменные для заполнения в масив данных
    //---- Переменные щета
    int score_win_you = 0, score_win_blue = 0;
    int score_newGame = 0;
    //----------------------------------------------------------------
    ServerBluetooth server;                                     // Переменная на себя
    //Картинки
    ImageView[] im = new ImageView[9];
    ImageView settings , im_play_blue, im_play_you;
    //Анимацыя
    Animation animComboClic = null;
    //Текстовые поля
    TextView text , score_you, score_blue ,yourStep, stepBlue;
    //Кнопка
    Button newGame, back;
    //Прогресс
    ProgressBar progressConnection;
    //Обект класса который занемаеться передачей данных -------------
    ClassServerConnect classServerConnect;
    //Переменная хранение настроек
    SharedPreferences musicBg;
    //------------------- МУЗЫКА В ИГРЕ ----------------------------
    //Стринговые константы
    final String STATUS_MUSIK = "status_musik";
    final String STATUS_SOUND = "status_sound";
    //Имя блютус устройства с которым играем
    String nameBluePlayer;
    //Переменная которая используеться для остановки проигрования музыки
    boolean savedStatusSound;       // переменная статуса звуков в игре
    boolean savedStatusMusic;       // переменная статуса музыки в игре
    // --- Звуки при нажатии на кнопку
    SoundPool sp;
    int soundIdShot;
    int soundIdShotSpp;
    final int MAX_STREAMS = 5;
    //Окна которые используються для подсказки очереди хода игрока
    FrameLayout yourFrame, frameBlue;
//-------------------------------------------------------------------------------------------------- Создание Активити !!!
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_blue);
        //--------------------------------- Запускаем обнаружение устройства -----------------------
        // будет показано системное окно с предложением включить видимость блютуса на 120 сек
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        // время в видимости можно изменить для етого надо передать интент с вот етими параметрами
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        // мах время 3600 сек
        // значение 0 делает блютус видимым всегда
        // запуск активити с предложением включить видимость на 300 сек
        startActivity(discoverableIntent);
        //------------------------------------------------------------------------------------------ end visible bluetooth
        //Иницыализацыя класса Который передает данные другому игроку
        classServerConnect = new ClassServerConnect(this);
        //Передачи ссылки на себя
        server = this;
        //Иницыализацыя анимацыии
        animComboClic =  AnimationUtils.loadAnimation(this ,R.anim.mco);
        //Нахождение всех ВЬЮ
        settings = (ImageView) findViewById(R.id.settings);                      //настройки
        newGame = (Button) findViewById(R.id.newGame);                           //новая игра
        back = (Button) findViewById(R.id.back);
        progressConnection = (ProgressBar) findViewById(R.id.progressConnection);//прогрес при подкл
        text = (TextView) findViewById(R.id.text);                               //текст о выграше
        im_play_blue = (ImageView) findViewById(R.id.im_play_blue);              //иконка блютус игр
        im_play_you = (ImageView) findViewById(R.id.im_play_you);                //ваша иконка
        score_you  = (TextView) findViewById(R.id.text_your_score);              //ваш счет Выйграш
        score_blue = (TextView) findViewById(R.id.text_scote_bluetooth);         //щет соперника
        // Нахождение окон и текстовых полей ОЧЕРЕДИ первого хода
        yourFrame = (FrameLayout) findViewById(R.id.yourFrame);                  //ваше поле подск х
        frameBlue = (FrameLayout) findViewById(R.id.frameBlue);                  //поле соперника хо
        yourStep = (TextView) findViewById(R.id.yourStep);                       //текст вью ваш ход
        stepBlue = (TextView) findViewById(R.id.stepBlue);                       //текст вью ход соп

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

        //добавление стиля шрифта
        Typeface tff = Typeface.createFromAsset(getAssets(),"fonts/pacifico.ttf");
        newGame.setTypeface(tff);
        back.setTypeface(tff);

        //-------- создание масива с адресами иконок для игры
        addres = new int[6];
        addres[0]=R.drawable.kr300x300haki;
        addres[1]=R.drawable.kr_n1300x300;
        addres[2]=R.drawable.kr_w300x300;

        addres[3]=R.drawable.nolik300x300g;
        addres[4]=R.drawable.nolik_nichya300x300;
        addres[5]=R.drawable.nolik_w300x300;

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

        // --- Пока идет подключение кнопку настроек и новой игры отключаем
        settings.setEnabled(false);
        newGame.setEnabled(false);
        text.setText("Connecting!");
        //Блокируем игровое поле пока не пройдет подключение
        stopIgra=1;
        //Ставим иконки кто с чем играет
        im_play_blue.setImageResource(R.drawable.ic_you_h);
        im_play_you.setImageResource(R.drawable.ic_and_g);

        //---------- Загрузка музыкальных коротких треков ----------
        // Короткая музыка при нажатии на кнопки
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener( this);
        // Присвоение треков
        soundIdShot = sp.load(this, R.raw.sou, 1);
        soundIdShotSpp = sp.load(this, R.raw.winn, 1);

        // вызываем диалог
        showDialog(CONNECTION);  // вызов диалогового окна во время подключения
        //устанавливаем подсказку очереди хода
        yourStep.setText("Your Step!");
        yourFrame.setBackgroundResource(R.drawable.frame_layout_players);
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

        playMusic();  // Метод запуска проигрования фоновой музыки
    }
    //----------------------- Метод вызываеться перед тем как будет свернуто ----------------------- Готовим активити к свертыванию
    @Override
    protected void onPause()
    {
        super.onPause();
        stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой

        sp.autoPause(); // /СТОП Проигрование короткого Выйграшного трека
    }
    //----- Проигрование музыки в фоне ---
    public void playMusic()
    {
        if (savedStatusMusic)
            startService(new Intent(this, SoundBgraund.class)); //старт сервис с музыкой фоновой
    }
    //----- Проигрование звуков при нажатии на игровое поле ---
    public void playSoundClic()
    {
        if (savedStatusSound)
            sp.play(soundIdShot, 1, 1, 0, 0, 1); // проигрование короткого трека
    }//----- Проигрование выйграшной мелодии ----
    public void playSoundWin()
    {
        if (savedStatusSound)
            stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
            sp.play(soundIdShotSpp, 1, 1, 0, 0, 1); // Проигрование короткого Выйграшного трека
    }
    ///--------------------- ВЫЗОВ ДИАЛОГОВОГО ОКНА ------------------------------------------------
    public Dialog onCreateDialog(int id) // метод создания \ компоновки диалогового окна
    {
        // диалоговое окно ВЫХОДА
        if (id == DIALOG_EXIT)
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
        // диалоговое окно создания новой игры ОТПРАВИЛ предложение
        if (id == NEW_GAME)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle("Предложыть новую игру");
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton(R.string.yes, myClickListenerNew);
            // кнопка отрицательного ответа
            adb.setNegativeButton(R.string.no, myClickListenerNew);
            // создаем диалог
            return adb.create();
        }
        // диалоговое окно создания новой игры ПОЛУЧИЛ предложение
        if (id == PLAY_AGEN)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(nameBluePlayer+" предлагает сыграть заново?");
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton(R.string.yes, myClickListenerAGEN);
            // кнопка отрицательного ответа
            adb.setNegativeButton(R.string.no, myClickListenerAGEN);
            // создаем диалог
            return adb.create();
        }
        //Диалоговое окно появляеться тогда когда проходит процесс подключения
        if (id == CONNECTION)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            //Прогресс бар
            adb.setView(R.layout.dialog_view);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }
    //----------------------- Обработчик нажатия на кнопки в диалоговом окне ВЫХОДА назад ----------
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    //Закрытие соединения
                    //classServerConnect.connectedThread.cancel();
                    finish();
                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    //--- Обработчик нажатия на кнопки в диалоговом окне которое предлагает сыграть в новою игру ---
    DialogInterface.OnClickListener myClickListenerNew = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    // Создаем новую игру передаем (true)
                    newGame(true);
                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    //--------- Обработчик нажатия на кнопки в диалоговом окне  ПРЕДЛОЖЕНИЕ СЫГРАТЬ СНОВА ----------
    DialogInterface.OnClickListener myClickListenerAGEN = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    // Просто когда мы принемаенм предложение сыграть снова МЫ обновляем переменные и играем
                    newGame(false);
                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    finish();
                    break;
            }
        }
    };
    //---------------------------------------------------------------------------------------------- НОВАЯ ИГРА
    public void newGame(boolean status)
    {
        //--------------- Новая игра Обновление всех переменных ---------------------------
        //Условие проверяет отсыльть предложение поиграть или нет
        if (status) classServerConnect.sendKr(server, "newgame");
        //Обнуление переменных
        score_newGame++;
        statusCompHoda=0;
        win1=0; win2=0; win3=0;
        rezult=0;
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
        //Очещаем текстовое вью (ЗАГЛАВОК)
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

            im_play_blue.setImageResource(R.drawable.ic_you_h);
            im_play_you.setImageResource(R.drawable.ic_and_g);

            //устанавливаем подсказку очереди хода
            yourStep.setText("Your Step!");
            yourFrame.setBackgroundResource(R.drawable.frame_layout_players);
        }
        else
        {
            addres[0]=R.drawable.nolik300x300haki;
            addres[1]=R.drawable.nolik_nichya300x300;
            addres[2]=R.drawable.nolik_w300x300;
            addres[3]=R.drawable.kr300x300g;
            addres[4]=R.drawable.kr_n1300x300;
            addres[5]=R.drawable.kr_w300x300;

            im_play_blue.setImageResource(R.drawable.ic_and_h);
            im_play_you.setImageResource(R.drawable.ic_you_g);

            //устанавливаем подсказку очереди хода
            stepBlue.setText("Step "+nameBluePlayer+"!");
            frameBlue.setBackgroundResource(R.drawable.frame_layout_players);
            //Запред хода для сервера
            stopIgra=1;

        }
    } //-------------------------------------------------------------------------------------------- КОНЕЦ новая игра
    // --- при нажатии кнопки назад предложение выйти из игры
    @Override
    public void onBackPressed()
    {
        // вызываем диалог
        showDialog(DIALOG_EXIT);  // вызов диалогового окна при нажатии на кнопку назад

    }
    //--- Метод вызываеться ИЗ ClassServerConnect
    public  void playAgen()
    {
        showDialog(PLAY_AGEN);
    }
    //--------------------------------------------------------------------------------------------- КОНЕЦ ДИАЛОГА

    //-------------- Метод в тоасте показывает что подключены устройства ---------------------------
    public  void connected()
    {
        // Открываем кнопки для пользователя
        text.setText("");
        progressConnection.setVisibility(ProgressBar.INVISIBLE);
        //Открываем игровое поле
        stopIgra=0;
        // Открываем кнопки для пользователя

        settings.setEnabled(true);
        newGame.setEnabled(true);
        removeDialog(CONNECTION);
        Toast.makeText(this, "Connected to "+nameBluePlayer+"!", Toast.LENGTH_LONG).show(); // в тосте показываем информацыю о нашем блютусе
    }
//------------------------------------- Слушатель нажатия на кнопки ----------------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v)
    {
        //Переменная в которую записоваються значения которые отправляються сопернику
        String poz;
        // ---  Условие срабатывает если была нажата кнопка НАСТРОЙКИ ---
        if (v.getId() == R.id.settings)
        {
            Intent intentSettings = new Intent(this, Settings.class);
            startActivity(intentSettings);
        }
        //Кнопка НАЗАД back
        if (v.getId() == R.id.back)
        {
            onBackPressed();
        }
        // --- Условие срабатывает если была нажата кнопка НОВАЯ ИГРА ---
        if (v.getId() == R.id.newGame)
        {
            showDialog(NEW_GAME);
        }
        //Условие проверяет если игра приостановлена то не очещаем заданые поля
        if (stopIgra==0)
        {
            //Обнуляем подсказку о ходе по очереди
            yourStep.setText("");
            yourFrame.setBackgroundResource(0);
            //устанавливаем подсказку очереди хода
            stepBlue.setText("");
            frameBlue.setBackgroundResource(0);
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
                        im[it].setImageResource(addres[3]);
                        im[it].startAnimation(animComboClic);// устанавка анимаци
                        mas[it] = -1;
                        queue=1;
                        String ff = String.valueOf(it);

                        poz = "g"+ff+"2";
                        classServerConnect.sendKr(server,poz);
                        playSoundClic(); //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            }
        }
    }
//-------------------------------------------------- Метод ОТРИСОВКИ ------------------------------------------------------------------------------------------------------
    public void Otres()
    {
        //  ОТРИСОВКА НЕПОСРЕДСТВЕНО ПО ЯЧЕЙКИ КОТОРУЮ ВЫБРАЛ КОМП
        //------------------------------------------------- ВЫЗОВ ИЗ МАИН АКТИВИТИ -----------------
        if (tray==1)
        {
            mas[one] = two;
            //Обнуляем подсказку о ходе по очереди
            yourStep.setText("");
            yourFrame.setBackgroundResource(0);
            stepBlue.setText("");
            frameBlue.setBackgroundResource(0);
            //Запред хода для сервера
            stopIgra=0;
        }
        //------------------------------------------------------------------------------------------
        for (int pr=0; pr<mas.length; pr++)
        {
            if (arrayVrem[pr]!= mas[pr]&&mas[pr] == 1)
            {
                im[pr].setImageResource(addres[0]);  // НОЛИКИ
                im[pr].startAnimation(animComboClic);  // устанавка анимаци
                arrayVrem[pr]=mas[pr];                           // запись елимента которого небыло (сделано для того чтоб не перерисовувать все елименты)
                queue = 0; // чтоб не можно было зделать выбор в поле пока этого не зделает 2 игрок
                tray=0;
                playSoundClic(); //проигрование трека при  нажатии на кнопку
            }
        }
//------------------------------------------------- ПРОВЕРКА НА ВЫИГРАШ ------------------------------------------------------------------------------------------------------
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
//------------------------------------- ПРОВЕРКА НА НИЧЬЮ ------------------------------------------
        if (mas[0]!=0&&mas[1]!=0&&mas[2]!=0&&mas[3]!=0&&mas[4]!=0&&mas[5]!=0&&mas[6]!=0&&mas[7]!=0&&mas[8]!=0)
        {
            rezult=3;
            text.setText("Ничья!");
            if (rezult==3&&stopIgra!=1)
            {
                for (int nichya= 0; nichya<mas.length; nichya++)
                {
                    if(mas[nichya]==1)
                    {
                        im[nichya].setImageResource(addres[1]);
                    }
                    if(mas[nichya]==-1)
                    {
                        im[nichya].setImageResource(addres[4]);
                    }
                }
            }// конец ничьии
            stopIgra=1;
        }
//------------------------------ ВЫИГРАШ -----------------------------------------------------------
        // если находим выйграшную комбинацыю отрисовуем и СТОП игра
        if (mas[win1]+mas[win2]+mas[win3]==3&&one_string_full==1)
        {
            im[win1].setImageResource(addres[2]);
            im[win2].setImageResource(addres[2]);
            im[win3].setImageResource(addres[2]);

            score_win_blue++;
            score_blue.setText("" + score_win_blue);
            text.setText(nameBluePlayer+" is Win!");
            stopIgra=1;
            playSoundWin();
        }
        if (mas[win1]+mas[win2]+mas[win3]==-3&&one_string_full==1)
        {
            im[win1].setImageResource(addres[5]);
            im[win2].setImageResource(addres[5]);
            im[win3].setImageResource(addres[5]);

            score_win_you++;
            score_you.setText("" + score_win_you);
            text.setText("You Win!");
            stopIgra=1;
            playSoundWin();
        }
        // если у нас ничья то зарисовуем все поля
    }
    // --- Метод синхронной подгрузки треков ---
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }
}
