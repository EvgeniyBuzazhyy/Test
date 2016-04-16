package com.example.evgeniy_pc.tictactoe;

        import android.app.Activity;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.SharedPreferences;
        import android.content.res.Resources;
        import android.graphics.Typeface;
        import android.media.SoundPool;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.Toast;
        import java.util.ArrayList;


public class StartBluetooth extends Activity implements View.OnClickListener
{
    private static final int REQUST_ENABLE_BT = 0;   // константа которая отправляеться на ожыдание(возможно на номер запроса)

    BluetoothAdapter bluetooth;
    ListView listWithDevicess;
    Button newGame, connectToGame;

    ArrayAdapter<String> listAdapter;
    ArrayList<BluetoothDevice> devices;
    ArrayList<String> nameDevices = new ArrayList<String>();

    final String LOG_TAG = "myLogs";

    int positionInList = 0;

    Intent intentServer;

    int callActiviti = 0;

    ProgressBar progress;

    //---------------------
    SharedPreferences musicBg;
    final String STATUS_MUSIK = "status_musik";


    // static boolean savedStatusSound;
    boolean savedStatusMusic;
    ///-------------------------



    //--------------------------------------------- ОСновной метод создания Активити -----------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_blue);
        // находим список

        newGame = (Button) findViewById(R.id.newGame);
        connectToGame = (Button) findViewById(R.id.connectToGame);
        devices = new ArrayList<BluetoothDevice>();
        listWithDevicess = (ListView) findViewById(R.id.listWithDevicess);
        listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nameDevices);
        listWithDevicess.setAdapter(listAdapter);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        //добавление стиля шрифта
        Typeface tff = Typeface.createFromAsset(getAssets(),"fonts/pacifico.ttf");
        newGame.setTypeface(tff);
        connectToGame.setTypeface(tff);


//--------------------------------------- ПРОВЕРЯЕМ НАЛИЧИЕ БЛЮТУС УСТРОЙСТВА ---------------------------------------------------------------
        bluetooth = BluetoothAdapter.getDefaultAdapter();    // убеждаемся что телефон имеет адаптер и вызываем его
        if (bluetooth != null)   //если телефон не поддержывает блютус то он вернет Null------------------------------------------------------
        {
            // с блютусом все хорошо
            if (bluetooth.isEnabled())      // проверяем то включен ли блютус вообще если нет то предлагаем включить его
            {
                // если блютус включен то работаем
                // Отабразим имя и адрес нашего адаптера
                String status;
                if (bluetooth.isEnabled()) // если блютус работает
                {
                    String mydeviceaddress = bluetooth.getAddress();  // получение адреса нашего блютуса
                    String mydevicename = bluetooth.getName();        // получение имени нашего блютуса
                    status = mydevicename+" "+mydeviceaddress;        // в стринговую переменную записоваем имя и адресс нашего блютуса
                    // если приложение имеет разрешение BLUETOOTH_ADMIN в манифесте то вы можете изменять имя блютус устройства
                    //bluetooth.setName("My Bluetooth");
                    // состояние адаптера  В ИНТОВОМ ТИПЕ
                    int state = bluetooth.getState();
                    status = mydevicename+" "+mydeviceaddress+" >>> "+state;        // в стринговую переменную записоваем имя и адресс и состояние нашего блютуса
                    /*Приходят ответы виде констант
                    STATE_TURNING_ON    // в процессе включения
                    STATE_ON            // включен                     - 12 in int
                    STATE_TURNING_OFF   // в процесе выключения
                    STATE_OFF           // выключен
                    * */
                }
                else // если наш блютус не роботает то
                {
                    status = "Блютус Выключен";
                }
                Toast.makeText(this, status, Toast.LENGTH_LONG).show(); // в тосте показываем информацыю о нашем блютусе
            }
            else
            {
                // если блютус не роботает то предлагаем пользователю его включить
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); // в блютус адаптере просим включить (через слушатель) его
                startActivityForResult(enableBtIntent,REQUST_ENABLE_BT);                    // новое активити на запросс включения блютус
            }
        }
        else
        {
            Toast.makeText(this, "Блютус не поддержуеться вашым телефоном !!!", Toast.LENGTH_LONG).show(); // в тосте показываем информацыю о нашем блютусе
            //  блютус не поддержуеться вашым телефоном---------------------------------------------
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();

        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_MUSIK если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusMusic = musicBg.getBoolean(STATUS_MUSIK, true);

        playMusic();  // Метод запуска проигрования фоновой музыки
    }
    //----------------------- Метод вызываеться перед тем как будет свернуто ----------------------- Готовим активити к свертыванию
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("myLogs", "Клиент --- Сработал перед тем свернуто--- ");
        stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
    }
    //----- Проигрование музыки в фоне ---
    public void playMusic()
    {
        if (savedStatusMusic)
            startService(new Intent(this, SoundBgraund.class)); //старт сервис с музыкой фоновой

    }
    //-------------------------------------------------- ОБРАБОТЧИК НАЖАТИЙ НА КНОПКУ --------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v)
    {

        switch (v.getId()) {
//------------------------------------ Сканирование уже спрареных блютус устройств -----------------
            case R.id.settings:
                Intent intentSettings = new Intent(this, Settings.class);
                startActivity(intentSettings);
                break;
            case R.id.connectToGame:
                // Удаление всех устройств которые нашли
                bluetooth.cancelDiscovery();// метод остановки сканирования
                nameDevices.clear();
                devices.clear();

                // Обнуляем позицыю елиментов в листе
                positionInList= 0;
                Log.d("myLogs", "Розмер масива с девайсами  111 clear  - " + nameDevices.size());

                // Метод который показывает ПРОГРЕСС БАР
                progress.setVisibility(ProgressBar.VISIBLE);
                newGame.setBackgroundColor(0);

                // Просто на кнопку устанавливаем нитральный цвет
                newGame.setBackgroundColor(getResources().getColor(R.color.colorNewGame));
                newGame.setEnabled(false);
                //newGame.setBackgroundColor(R.drawable.button_pressed);
                callActiviti =2;
                //Создае BroadcastReceiver для ACTION_FOUND
                Log.d("myLogs", "Заш в СКАН!!!__ ");
                // получить спареные блютус устройства
                final BroadcastReceiver mReceiver = new BroadcastReceiver()
                {
                    @Override
                    public void onReceive(Context context, Intent intent)
                    {
                        Log.d("myLogs", "Зашел в онРесиве !!!__ ");

                        String action = intent.getAction();
                        // Когда найдено новое устройство
                        if (BluetoothDevice.ACTION_FOUND.equals(action)) // тоесть когда ресивер обнаружыл сообщение котороре пришло в интенте и передано стрингу action то мы сравниваем его  с флагом ACTION_FOUND
                        // если интент верен то :
                        {
                            Log.d("myLogs", "Зашел в сканирование !!!__ ");
                            // получаем обект BluetoothDevice из интента тоесть само устройство которое нашли
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            // добавим в масив чтоб отобразить в масиве
                            String str = device.getName() + " " + device.getAddress();
                            Log.d("myLogs", "Нашел!!__ " + str);
                            //nameDev = device;


                                boolean result = true;
                                Log.d("myLogs", "Проверяем  - " + device.getName() + " - " + "\n" + device.getAddress());
                                //Log.d("myLogs", "Проверяем 2 - " + nameDevices.get(i).toString());

                                for (int i = 0; i < nameDevices.size(); i++)
                                {
                                    if (nameDevices.get(i).toString().equals(device.getName() + " - " + "\n" + device.getAddress()))
                                    {
                                        result = false;
                                    }

                                }
                            if (result)
                            {
                                nameDevices.add(positionInList, device.getName() + " - " + "\n" + device.getAddress());

                                listAdapter.notifyDataSetChanged();
                                devices.add(device);
                                positionInList++;
                                Log.d("myLogs", "Розмер масива с девайсами  111 СОЗДАН  - " + nameDevices.size());

                            }

                        }

                        Log.d("myLogs", "Розмер масива с девайсами - " + devices.size());
                    }

                };

                Log.d("myLogs", "==ВСЕ ВЫШЕЛ С ПОИСКА ==" );
                // ТОесть запускаем нашего слушателя !!!!! который описан верху НЕ ЗАБЫТЬ ЕГО ОТКЛЮЧИТЬ В УДАЛЕНИИ
                // регистрируем BroadcastReceiver
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);

                //

                if (!bluetooth.isDiscovering()) // ели сканирование не активно
                    bluetooth.startDiscovery(); // то мы его запускаем

//---------------------------------- СЛУШАТЕЛЬ ВЫБОРКИ ИЗ ЛИСТА СКАНИРОВАНИЯ -----------------------------------------------
                listWithDevicess.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {

                        // Метод который показывает СКРЫВАЕТ прогресс БАР
                        progress.setVisibility(ProgressBar.INVISIBLE);
                        bluetooth.cancelDiscovery();// метод остановки сканирования

                        Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "+ id);
                        Log.d(LOG_TAG, "itemClick: position = " + devices.get(position).getName() + ", id = "+ id);


                        BluetoothDevice mmDeviceee = devices.get(position);
                        Log.d(LOG_TAG, "itemClick: position = " + mmDeviceee.getName() + ", id = "+ mmDeviceee.getAddress());

                        // ЗАПУСКАЕМ КЛИЕНТА

                        startCl(mmDeviceee);
                    }
                });

                break;
//-----------------------Интент с запрорсом на переход в режым видимости на 300 секунд -------------
            case R.id.newGame:
                callActiviti = 1;
                //--------------------------------------- Создаем сервак ---------------------------
                Log.d("myLogs", "> 4 > ЗАШЕЛ В СТАРТ СЕРВЕР ");

                intentServer = new Intent(this, ServerBluetooth.class);
                // Вередаем обект обекта менеджера блютус

                startActivity(intentServer);
                break;
        }
    }

    //------------------------------- Метод запуска клиенского активити -------------------------------------------------------------------------------------------------------------
    public void startCl(BluetoothDevice mmDeviceee)
    {
        intentServer = new Intent(this, ClientBluetooth.class);

        intentServer.putExtra("device",mmDeviceee);

        startActivity(intentServer);
    }
}

