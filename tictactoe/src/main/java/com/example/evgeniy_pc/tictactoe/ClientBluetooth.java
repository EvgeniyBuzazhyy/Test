package com.example.evgeniy_pc.tictactoe;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//Класс клиент
public class ClientBluetooth  extends Activity implements View.OnClickListener, SoundPool.OnLoadCompleteListener
{
    // Константы которые используются при вызове Диалогов
    final int DIALOG_EXIT = 1;
    static final int NEW_GAME = 2;
    final int PLAY_AGEN = 3;
    final int CONNECTION = 4;
    //Лог ------------------------------------------------------------
    final String LOG_TAG = "myLogs";
    //Масивы ---------------------------------------------------------
    int[] mas        = new int [9]; // массив из 9 кнопок
    int[] arrayVrem  = new int [9]; // массив для сверки Временный массив
    int nameBotton[] = new int [9]; // Массив кнопок с ID
    int[] addres;                   // id адреса картинок с котрыми играем
    //Объект на самого себя
    ClientBluetooth client;
    //Интовые переменнные
    int sh            =0;
    int statusCompHoda=0;
    int queue         =0; // порядок хода игроков
    //---- Переменные счета
    int score_win_you = 0, score_win_blue = 0;
    int score_newGame = 0;
    // константы которые определяют победителя
    int win1=0,win2=0,win3=0;
    int tri=3;
    // результат и остановка игры
    int rezult=0;
    int stopIgra=0;
    // переменные для заполнения в массив данных (присланых от сервера)
    int one, two, tray;
    //Прогресс который работает при процедуре подключения
    ProgressBar progressConnection;
    //---------------------------------------------------------------
    //Картинки
    ImageView[] im = new ImageView[9];
    ImageView settings, im_play_blue, im_play_you;;
    //Кнопка новой игры
    Button newGame, back;
    //Анимация
    Animation animComboClic = null;
    TextView text, score_you, score_blue ,yourStep, stepBlue;
    //Пременная в которой хранятся данные настроек
    SharedPreferences musicBg;
    // Константы статуса МУЗЫКИ
    final String STATUS_MUSIK = "status_musik";
    final String STATUS_SOUND = "status_sound";
    //Имя блютус устройства с которым играем
    String nameBluePlayer;
    //Булеан переменные которые используются для включениия \ выключения музыки и звуков
    boolean savedStatusSound;
    boolean savedStatusMusic;
    // --- Музыка при нажатии на кнопку
    SoundPool sp;
    int soundIdShot;
    int soundIdShotSpp;
    final int MAX_STREAMS = 5; // максимальное количество треков которые можно проиграть
    //--------------------------------------------------------------
    //Объект класса через который мы отсылаем и принимаем данные о сделанных ходах
    BluetoothClientConnect bluetoothClientConnect;
    //Обект БЛЮТУС девайс к которому будем подключаться
    BluetoothDevice mmDeviceee;
    //Окна в которых будет светиться подсказка о том кто ходит первым
    FrameLayout yourFrame, frameBlue;

// ---------------------------------- Метод создания Активити -------------------------------------- Создание Активити
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_blue);
        //Инициализация анимации
        animComboClic =  AnimationUtils.loadAnimation(this ,R.anim.mco);
        //Передача ссылки на самого себя
        client = this;
        //Извлечение из интента девайса который был передан с вызовом активити
        mmDeviceee = (BluetoothDevice) getIntent().getParcelableExtra("device");
        //Получение имени этого подключенного девайса
        nameBluePlayer = mmDeviceee.getName();
        //Запуск класса через который будем отсылать данные
        bluetoothClientConnect = new BluetoothClientConnect(this,mmDeviceee);
        //Нахождение всех ВЬЮ
        settings = (ImageView) findViewById(R.id.settings);                      //настройка
        newGame = (Button) findViewById(R.id.newGame);                           //кнопка новая игр
        back = (Button) findViewById(R.id.back);
        progressConnection = (ProgressBar) findViewById(R.id.progressConnection);//прогресс при подк
        text = (TextView) findViewById(R.id.text);                               //текстовое поле в
        im_play_blue = (ImageView) findViewById(R.id.im_play_blue);              //ик с чем игр сопе
        im_play_you = (ImageView) findViewById(R.id.im_play_you);                //ик с чем игр мы
        score_you  = (TextView) findViewById(R.id.text_your_score);              //наш счет
        score_blue = (TextView) findViewById(R.id.text_scote_bluetooth);         //счет соперника
        // Объявление окон и текстовых полей ОЧЕРЕДИ первого хода
        yourFrame = (FrameLayout) findViewById(R.id.yourFrame);                  //окно подс. наше
        frameBlue = (FrameLayout) findViewById(R.id.frameBlue);                  //окно подс. сопер
        yourStep = (TextView) findViewById(R.id.yourStep);                       //наш шаг
        stepBlue = (TextView) findViewById(R.id.stepBlue);                       //шаг соперника
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
        //-----------------------------------------------------
        //Отключаем кнопки пока не пройдет подключение
        settings.setEnabled(false);
        newGame.setEnabled(false);
        text.setText("Con... to "+nameBluePlayer);
        //Устанавливаем иконки КТО чем играет
        im_play_you.setImageResource(R.drawable.ic_you_h);
        im_play_blue.setImageResource(R.drawable.ic_and_g);

        //добавление стиля шрифта
        Typeface tff = Typeface.createFromAsset(getAssets(),"fonts/pacifico.ttf");
        newGame.setTypeface(tff);
        back.setTypeface(tff);

        //-------- создание массива с id игровыми иконками
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

        //---------- Загрузка музыкальных коротких треков ----------
        // Короткая музыка при нажатии на кнопки
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener( this);
        // Присвоение треков
        soundIdShot = sp.load(this, R.raw.sou, 1);
        soundIdShotSpp = sp.load(this, R.raw.winn, 1);

        // вызываем диалог который показывает что программа пытаеться подключиться
        showDialog(CONNECTION);

        //устанавливаем подсказку очереди хода
        stepBlue.setText("Step "+nameBluePlayer+"!");
        frameBlue.setBackgroundResource(R.drawable.frame_layout_players);
        stopIgra=1;
    }

    //------------------ Метод вызывается перед тем как будет отображено это активити ------------- Показываем активити перед скрытием
    @Override
    protected void onResume()
    {
        super.onResume();
        // С переменной настроек читаем информацию которая хранится в
        // STATUS_MUSIK если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusMusic = musicBg.getBoolean(STATUS_MUSIK, true);

        // С переменной настроек читаем информацию которая хранится в
        // STATUS_SOUND если true то мы будем играть ЗВУКИ
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusSound = musicBg.getBoolean(STATUS_SOUND, true);

        playMusic();  // Метод запуска проигрывания фоновой музыки
    }
    //----------------------- Метод вызываеться перед тем как будет свернуто ----------------------- Готовим активити к свертыванию
    @Override
    protected void onPause()
    {
        super.onPause();
        stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой

        //СТОП Проигрывание короткого Выиграшного трека
        sp.autoPause();
    }
    //----- Проигрование музыки в фоне ---
    public void playMusic()
    {
        if (savedStatusMusic)
            startService(new Intent(this, SoundBgraund.class)); //старт сервис с музыкой фоновой
    }
    //----- Проигрывание звука при нажатии на игровое поле ---
    public void playSoundClic()
    {
        if (savedStatusSound)
            sp.play(soundIdShot, 1, 1, 0, 0, 1); // проигрывание короткого трека
    }//----- Проигрование выиграшного звука ----
    public void playSoundWin()
    {
        if (savedStatusSound)
            stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
            sp.play(soundIdShotSpp, 1, 1, 0, 0, 1); // Проигрывание короткого Выиграшного трека
    }

    ///---------------------------------------- ВЫЗОВ ДИАЛОГОВОГО ОКНА ----------------------------- Блок Диалогов
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
        // диалоговое окно создания новой игры С ПРЕДЛОЖЕНИЕМ
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
        // диалоговое окно создания новой игры ОТ соперника
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
        //Диалоговое окно вызываеться при подключениии
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
    //----------------------- Обработчик нажатия на кнопки в диалоговом окне выхода назад ----------
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    //Закрываем потоки при выходе из игры
                    bluetoothClientConnect.dataTransfer.cancel();
                    finish();
                    break;
                // негативная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    //--- Обработчик нажатия на кнопки в диалоговом окне которое предлагает сыграть в новою игру ---
    DialogInterface.OnClickListener myClickListenerNew = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    // Предлагаем сыграть снова
                    newGame(true);
                    break;
                // негативная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    //-------------- Обработчик нажатия на кнопки в диалоговом  ПРЕДЛОЖЕНИЕ СЫГРАТЬ СНОВА ----------
    DialogInterface.OnClickListener myClickListenerAGEN = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    // Просто когда мы принимаем предложение сыграть снова МЫ обновляем переменные и играем
                    newGame(false);
                    break;
                // негативная кнопка
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
        //Условие проверяет отсылать предложение поиграть или нет
        if (status) bluetoothClientConnect.sendKr(client,"newgame");

        score_newGame++;
        statusCompHoda=0;
        win1=0; win2=0; win3=0;
        rezult=0;
        sh=0;
        stopIgra=0;
        queue = 0; // порядок хода игроков

        // ----- Очистка массива значений
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

            im_play_you.setImageResource(R.drawable.ic_you_h);
            im_play_blue.setImageResource(R.drawable.ic_and_g);

            //устанавливаем подсказку очереди хода
            stepBlue.setText("Step "+nameBluePlayer+"!");
            frameBlue.setBackgroundResource(R.drawable.frame_layout_players);
            stopIgra=1;
        }
        else
        {
            addres[0]=R.drawable.nolik300x300haki;
            addres[1]=R.drawable.nolik_nichya300x300;
            addres[2]=R.drawable.nolik_w300x300;
            addres[3]=R.drawable.kr300x300g;
            addres[4]=R.drawable.kr_n1300x300;
            addres[5]=R.drawable.kr_w300x300;

            im_play_you.setImageResource(R.drawable.ic_and_h);
            im_play_blue.setImageResource(R.drawable.ic_you_g);

            //устанавливаем подсказку очереди хода
            yourStep.setText("Your Step!");
            yourFrame.setBackgroundResource(R.drawable.frame_layout_players);

        }
    } //-------------------------------------------------------------------------------------------- КОНЕЦ новая игра
    @Override                     // при нажатии кнопки назад предложение выйти из игры
    public void onBackPressed()
    {
        // вызываем диалог
        showDialog(DIALOG_EXIT);  // вызов диалогового окна при нажатии на кнопку назад
    }
    //------------------------------------------------- Метод вызываеться ИЗ  BluetoothClientConnect
    public  void playAgen()
    {
        showDialog(PLAY_AGEN);
    }
    //--------------------------------------------------------------------------------------------- КОНЕЦ ДИАЛОГА

    //----------------------- Метод в тоасте показывает что подключены устройства ------------------
    //также открывает кнопки для игры
    public  void connected()
    {
        // Включаем кнопки для пользователя
        text.setText("");
        progressConnection.setVisibility(ProgressBar.INVISIBLE);
        settings.setEnabled(true);
        newGame.setEnabled(true);

        removeDialog(CONNECTION);
        Toast.makeText(this, "Connected to "+nameBluePlayer, Toast.LENGTH_LONG).show(); // в тосте показываем информацию о нашем блютусе
    }
//------------------------------------- Слушатель нажатия на кнопки --------------------------------
    @Override
    public void onClick(View v)
    {
        String poz;
        // ---  Условие срабатывает если была нажата кнопка НАСТРОЙКИ ---
        if (v.getId() == R.id.settings)
        {
            Intent intentSettings = new Intent(this, Settings.class);
            startActivity(intentSettings);
        }
        //Возврат НАЗАД back
        if (v.getId() == R.id.back)
        {
            onBackPressed();
        }
        // --- Условие срабатывает если была нажата кнопка НОВАЯ ИГРА ---
        if (v.getId() == R.id.newGame)
        {
            //--------------- Новая игра Обновление всех переменных ---------------------------
            sp.autoPause(); // /СТОП Проигрывание короткого Выиграшного трека
            playMusic();  // Метод запуска проигрывания фоновой музыки
            showDialog(NEW_GAME);
        }

        // Обнуляем подсказку подсказку очереди хода
        if (stopIgra==0)
        {
            stepBlue.setText("");
            frameBlue.setBackgroundResource(0);
            yourStep.setText("");
            yourFrame.setBackgroundResource(0);
        }
        // ----------  Цыкл который проверяет Кнопку которая была нажата -----
        for (int it=0; it<9; it++)
        {
            if (v.getId() ==  nameBotton[it])
            {
                if (stopIgra==0&&mas[it]==0)         // код ниже будет работать только тогда когда не было КОМАНДЫ СТОП при формировании Выиграша
                {
                    if (queue==0)
                    {
                        im[it].setImageResource(addres[0]);
                        im[it].startAnimation(animComboClic);// устанавка анимации
                        mas[it] = 1;
                        queue=1;
                        String ff = String.valueOf(it);

                        poz = "g"+ff+"1";
                        bluetoothClientConnect.sendKr(client, poz);
                        playSoundClic(); //проигрывание трека при  нажатии на кнопку
                    }
                    Otress();
                }
                break;
            }
        }
    }
    // -------- Метод отрисовует иконку которую выбрал соперник и определяет Победителя ------------
    public void Otress()
    {
        //Записываем в массив те изменения которые прислал соперник
        if (tray==1)
        {
            mas[one] = two;
            // Обнуляем подсказку подсказку очереди хода
            stepBlue.setText("");
            frameBlue.setBackgroundResource(0);
            // Возможность поставить свой ход после того как придет ответ
            stopIgra=0;
        }
        //Отрисовка новых заполненных ячеек
        for (int pr=0; pr<mas.length; pr++)
        {
            if (arrayVrem[pr]!= mas[pr]&&mas[pr] == -1)
            {
                im[pr].setImageResource(addres[3]);  // НОЛИКИ
                im[pr].startAnimation(animComboClic);  // устанавка анимации
                arrayVrem[pr]=mas[pr];                           // запись элемента которого небыло (сделано для того чтоб не перерисовывать все элементы)
                queue = 0; // чтоб не можно было сделать выбор в поле пока этого не сделает 2 игрок
                tray=0;
                playSoundClic(); //проигрывание трека при  нажатии на кнопку
            }
        }

        //---------------------------------- Поиск ПОБЕДИТЕЛЯ --------------------------------------
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

        //------------------------- Условие проверяет на наличие НИЧЬИ ----------------------------
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

            }// конец ничьи
            stopIgra=1;
        }

///////////////////////////////////////////////////////////////////////////////
        // если находим выйграшную комбинацию отрисовуем и СТОП игра
        if (mas[win1]+mas[win2]+mas[win3]==3&&one_string_full==1)
        {
            im[win1].setImageResource(addres[2]);
            im[win2].setImageResource(addres[2]);
            im[win3].setImageResource(addres[2]);

            score_win_you++;
            score_you.setText("" + score_win_you);
            text.setText("You Win!");
            stopIgra=1;
            // Метод проигрывания выиграшного трека
            playSoundWin();
        }
        if (mas[win1]+mas[win2]+mas[win3]==-3&&one_string_full==1)
        {
            im[win1].setImageResource(addres[5]);
            im[win2].setImageResource(addres[5]);
            im[win3].setImageResource(addres[5]);

            score_win_blue++;
            score_blue.setText("" + score_win_blue);
            text.setText(nameBluePlayer+" is Win!");
            stopIgra=1;

            playSoundWin();
        }
        // если у нас ничья то зарисовываем все поля
    }
    public void timeConnectingOver()
    {
        Toast.makeText(this, "Connection Close !", Toast.LENGTH_LONG).show();
        finish();
    }

    // ---------------- Метод отвечает за синхронную подгрузку треков ------------------------------
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }
}
