package com.example.evgeniy_pc.tictactoe;

/**
 * Created by Evgeniy_pc on 19.01.2016.
 */
public class test2
{
    /*package com.example.evgeniy_pc.tictactoe;

    import android.app.Activity;
    import android.bluetooth.BluetoothAdapter;
    import android.bluetooth.BluetoothDevice;
    import android.bluetooth.BluetoothServerSocket;
    import android.bluetooth.BluetoothSocket;
    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.Intent;
    import android.content.IntentFilter;
    import android.os.Bundle;
    import android.os.Handler;
    import android.os.Message;
    import android.util.Log;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.ProgressBar;
    import android.widget.Toast;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
    import java.util.ArrayList;
    import java.util.UUID;

    import static com.example.evgeniy_pc.tictactoe.ServerBluetooth.Otres;
    import static com.example.evgeniy_pc.tictactoe.ClientBluetooth.Otress;

    public class StartBluetooth extends Activity implements View.OnClickListener
    {
        private static final int REQUST_ENABLE_BT = 0;   // константа которая отправляеться на ожыдание(возможно на номер запроса)

        BluetoothAdapter bluetooth;
        ListView listWithDevicess;
        Button newGame, connectToGame;

        ArrayAdapter<String> listAdapter;
        ArrayList<BluetoothDevice> devices;
        final ArrayList<String> nameDevices = new ArrayList<String>();


        final String LOG_TAG = "myLogs";
        protected static final int SUCCESS_CONNECT = 0;
        protected static final int MESSAGE_READ = 1;
        static ConnectedThread connectedThread;
        int positionInList = 0;

        Intent intentServer;
        static ServerBluetooth server;
        static ClientBluetooth client;

        int callActiviti = 0;

        ProgressBar progress;


        //---------------------------------------------- ХЕНДЛЛЕР ПРИНЕМАЕТ ДАННЫЕ ИЗ ПОТОКОВ -----------------------------------------------------------------------------------------
        public Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {

                Log.d("myLogs", "В ХЕНДЛЕРЕ  !!!__ ");
                super.handleMessage(msg);
                switch(msg.what)
                {
                    case SUCCESS_CONNECT:                      // приполучении подключения тоесть СОКЕТА
                        connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
                        connectedThread.start();
                        String s = "successfully connected";
                        Log.d("myLogs", "В ХЕНДЛЕРЕ  ПОДКЛЮЧЕН !!!__ " );
                        connected();
                        break;
                    case MESSAGE_READ:                         // приходит любая строка
                        byte[] readBuf = (byte[])msg.obj;
                        String string = new String(readBuf);
                        Log.d("myLogs", "ПРОЧИТАНО В ХЕНДЛЕРЕ  !!!__ "+string );
                        getStr(string);                        // передаем в сроку которая её обрабатывает
                        break;
                }
            }
        };
        //------------------------------------ Метод в тоасте показывает что подключены устройства --------------------------------------------------------------------------------
        public  void connected()
        {
            Toast.makeText(this, "Подключение успешно!!!", Toast.LENGTH_LONG).show(); // в тосте показываем информацыю о нашем блютусе
        }
        //------------------------------------ Метода ОТПРАВКИ СТРОКИ ИЗ клиента / сервера  ------------
        // Отправка строки от КЛИЕНТА
        public static void sendKr(ClientBluetooth cl, String poz)
        {
            client=cl;
            String stro = poz;
            connectedThread.write(stro.getBytes());
        }
        // Отправка строки от СЕРВЕРА
        public static void sendKr(ServerBluetooth ser, String poz)
        {
            server=ser;
            String stro = poz;
            connectedThread.write(stro.getBytes());
        }
        //------------------------------------ Метод получения строки   ----------------------------------------------------------------------------------------------------------
        public void getStr(String string)
        {
            String str = string;

            if (str.startsWith("g"))
            {
                Log.d("myLogs", "Клиент --- Получил строку от Сервера 8888 --- " + str.toString());
                Log.d("myLogs", "Клиент ---Метод проверки отработал--- ");

                char x = str.charAt(1);
                char y = str.charAt(2);

                int posInMas = Character.getNumericValue(x);
                int znachInPos = Character.getNumericValue(y);
                if (znachInPos == 2) {
                    znachInPos = -1;
                }
                //если приложение играет за СЕРВЕРА то отрисовка клиента будет здесь
                if (callActiviti==1)
                {
                    server.one = posInMas;
                    server.two = znachInPos;
                    server.tray = 1;
                    Otres();
                }
                //если приложение ИГРАЕТ за КЛИЕНТА то отрисовка от сервака будет ЗДДЕСЬ тоесть 2
                if (callActiviti==2)
                {
                    client.one = posInMas;
                    client.two = znachInPos;
                    client.tray = 1;
                    Otress();
                }
                Log.d("myLogs", "Инт 1 =  " + posInMas);
                Log.d("myLogs", "Инт 2 =  " + znachInPos);
            }
            //------ ВЫЗОВА МЕТОДОВ О ПРЕДЛОЖЕНИ СЫГРАТЬ СНОВА - ОЧИСТКА !
            if (str.startsWith("n"))
            {
                Log.d("myLogs", "ЗАШЕЛ В НОВ ИГРА--- ");
                if (callActiviti==1)
                { server.playAgen();}
                if (callActiviti==2)
                {client.playAgen();}
            }
        }
        //------------------------------- Метод запуска клиенского активити -------------------------------------------------------------------------------------------------------------
        public void startCl()
        {
            intentServer = new Intent(this, ClientBluetooth.class);
            startActivity(intentServer);
        }
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

              /*      }
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
                //  блютус не поддержуеться вашым телефоном---------------------------------------------
            }
        }
        //-------------------------------------------------- ОБРАБОТЧИК НАЖАТИЙ НА КНОПКУ --------------------------------------------------------------------------------------------
        //@Override
        public void onClick(View v)*/
   /*     {
            switch (v.getId()) {
//------------------------------------ Сканирование уже спрареных блютус устройств -----------------
                case R.id.connectToGame:

                    // Метод который показывает ПРОГРЕСС БАР
                    progress.setVisibility(ProgressBar.VISIBLE);
                    newGame.setEnabled(false);
                    newGame.setBackgroundColor(R.drawable.button_pressed);
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

                                // nameDev=device;

                                nameDevices.add(positionInList, device.getName() + " - " + "\n" + device.getAddress());

                                listAdapter.notifyDataSetChanged();
                                devices.add(device);
                                positionInList++;
                            }

                            Log.d("myLogs", "Розмер масива с девайсами - " + devices.size());
                        }
                    };

                    Log.d("myLogs", "==ВСЕ ВЫШЕЛ С ПОИСКА ==" );
                    // ТОесть запускаем нашего слушателя !!!!! который описан верху НЕ ЗАБЫТЬ ЕГО ОТКЛЮЧИТЬ В УДАЛЕНИИ
                    // регистрируем BroadcastReceiver
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mReceiver, filter);

                    //bluetooth.cancelDiscovery();// метод остановки сканирования

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
                            ConnectThead conn = new ConnectThead(mmDeviceee);
                            conn.start();

                            startCl();
                        }
                    });

                    break;
//-----------------------Интент с запрорсом на переход в режым видимости на 300 секунд -------------
                case R.id.newGame:
                    callActiviti =1;
                    //--------------------------------------- Создаем сервак ---------------------------
                    Log.d("myLogs", "> 4 > ЗАШЕЛ В СТАРТ СЕРВЕР ");
                    // Запускаем наш поток сервер
                    AcceptThead acce = new AcceptThead();
                    //Старт
                    acce.start();


                    intentServer = new Intent(this, ServerBluetooth.class);
                    startActivity(intentServer);
                    break;
            }
        }

        // ---------------------------------- Наш класс поток СЕРВЕР ----------------------------------- СЕРВЕР
        ////////////////////////////////////////////////////////////////////////////////////////////////
        public class AcceptThead extends Thread                                                    // создаем свой клас ПОТОК наследник от потоока
        {
            final String LOG_TAG = "myLogs";

            String NAME = "mybluetoothserver";                                               // стринговая переменная  имени нашего приложения
            //String uniqueId = UUID.randomUUID().toString();
            String uniqueId = "186abee7-c6d8-4943-bcfd-d1241f665237";


            UUID MY_UUID = UUID.fromString(uniqueId);          // уникальный код который будет служыть неким ключем до нашего приложения

            BluetoothServerSocket mmServerSocket;                                            // переменная СЕРВЕР сокета
            //Конструктор класса
            public AcceptThead()                                                             // конструктор класса
            {
                Log.d("myLogs", "> 4 > ЗАШЕЛ  консруктор первый ГГШВ = "+uniqueId );
                // используем вспомогательную переменную
                // которую используем в дальнейшем
                // свяжем с mmServerSocket
                BluetoothServerSocket tmp = null;                                            // пустая переменная сервера сокета далее используем для создания сервера с аргументами (именем и ключем)
                try
                {
                    tmp = bluetooth.listenUsingRfcommWithServiceRecord(NAME,MY_UUID);        // создаем сам сокет
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                mmServerSocket = tmp;                                                        // передаем настроеный сокет в обект класса BluetoothServerSocket
                Log.d("myLogs", "> 4 > ЗАШЕЛ в КОНСТРУКТОР класса 1 " );
            }
            //----- перегружаный метод ПОТОКА ран срабатывает всегда в этом классе ----------
            @Override
            public  void run()
            {
                BluetoothSocket socet = null;                                                // создаем пустой сокет
                // ждем пока не произойдет ошыбка
                // или будет возвращен сокет
                while(true)                                                                  // Бесконечный цыкол который ПОСТОЯННО ожыдает сканирование
                {
                    try
                    {
                        Log.d("myLogs", "> 4 > ЗАШЕЛ В РАН ЖДЕМ ПОДКЛЮЧЕНИЯ ! 2" );
                        socet = mmServerSocket.accept();                                     // в  етот пустой сокет запишем сокет  который прийдет когда будет подключение

                        Log.d("myLogs", "> 4 > ЗАШЕЛ В РАН   +++ПОДКЛЮЧИЛИСЬ+++ ! 2" );
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                        break;                                                               // когда лимит будет исчерпан мы выйдем из цыкла
                    }
                    // если соединение было подтверждено
                    if (socet!=null)                                                         // если удаленное устройство было подключено то
                    {

                        // управляеем соединением (В ОТДЕЛЬНОМ ПОТОКЕ)
                        //ConnectedThread pr =new ConnectedThread(socet);                                       // передаем етот сокет на обработку и РОБОТУ СНИМ
                        // pr.start();
                        mHandler.obtainMessage(SUCCESS_CONNECT, socet).sendToTarget();
                        Log.d("myLogs", "> 4 > ПЕРЕДАЛ В ПОТОК ДАННЫХ   +++ПОДКЛЮЧ+++ ! 2" );
                        try
                        {
                            mmServerSocket.close();                                          //после того как мы уже взяли сокет с девайсом мы можем закрывать и очищать ресурсы основно ПОИСКОВОГО сокета
                            // закрываем для того чтоб можно было СОЗДАВАТЬ НОВОЕ ПОДКЛЮЧЕНИЕ
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////////////// Сервер КОНЕЦ

        //--------------------- НАш класс ПОТОК Клиент ------------------------------------------------- КЛИЕНТ
        private class ConnectThead extends Thread
        {
            BluetoothSocket mmSocket;
            BluetoothDevice mmDevice;
            String uniqueId = "186abee7-c6d8-4943-bcfd-d1241f665237";
            UUID MY_UUID = UUID.fromString(uniqueId);                                                   // уникальный код который будет служыть неким ключем до нашего приложения

            public ConnectThead(BluetoothDevice device)
            {
                // используем вспомагательную переменную , которую в дальнейшем
                // свяжем с mmSocket
                BluetoothSocket tmp = null;                                                             // !!! НУЖНО СТАВИТЬ null ЧТОБЫ МОЖНО БЫЛО ДЕЛАТЬ ПРИСВОЕНИЕ БЕЗ ПРОБЛЕМ
                mmDevice = device;
                // получаем BluetoothSocket чтобы соедениться с BluetoothDevice
                try
                {
                    // MY_UUID это UUID который забит в сервере
                    // Создание сокета в котором содержеться информацыя о нашем устройстве
                    tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                mmSocket = tmp;
            }

            // Основной метод выполнения в потоке
            @Override
            public void run()
            {
                Log.d("myLogs", "> 4 > ЗАШОЛ В СЛУШАТЕЛЬ НА ПОДКЛЮЧЕНИЕ 0000 "+mmDevice.getName() );
                //отменяем сканирование поскольку оно тормозит соединение
                bluetooth.cancelDiscovery();
                try
                {
                    //Соединяемся с устройством через сокет
                    // Метод блокирует выполнение программы до
                    // установки соединения или возникновения ошыбки
                    mmSocket.connect();
                    //передаем управления сокетом в (отдельный поток)


                    Log.d("myLogs", "> 4 > ЗАШЕЛ В РАН    +++КЛИЕНТА+++   +++ПОДКЛЮЧИЛИСЬ+++ ! 2" );
                } catch (IOException e)
                {
                    Log.d("myLogs", "> 4 > ПОЛУЧИЛАСЬ ОШЫБКА  +++КЛИЕНТА+++   +++ПОДКЛЮЧИЛИСЬ+++ ! 2" );

                    e.printStackTrace();
                    return;
                } // Подключенный обект сокета отправляем на внешку (Извлекаем из потока)
                // Отправляем сообщение в хендлер
                mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();

                //после того как получили сокет для економии ресурсов необходимо всегда вызывать метод close();
                // cancel();
            }
            //ОТМЕНА ОЖЫДАНИЯ СОКЕТА //
            public  void cancel()
            {
                try
                {
                    mmSocket.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }/////////////////////////////////////////////////////////////////////////////////////////////// КЛИЕНТ КОНЕЦ

        //------------------------- Наш класс Поток передачи данных ------------------------------------ ПОТОК ПЕРЕДАЧИ ДАННЫХ (Общий для сервера и клиента)
        public class ConnectedThread extends Thread
        {   // Переменная которая принемает сокет
            BluetoothSocket mmSocket;
            // Переменные входных и выходных данных
            InputStream mmInStream ;
            OutputStream mmOutStream;
            // Конструктор класса
            public ConnectedThread(final BluetoothSocket socket)
            {
                // Перезаписоваем сокет который пришол в наш
                mmSocket = socket;
                Log.d("myLogs", "> 4 > СОСТОЯНИЕ СОКЕТА -- "+mmSocket.isConnected() );
                // Входные и выходные устанавливаем как NULL
                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                try
                {
                    // Получаем входные и выходные ПОТОКИ
                    tmpIn = socket.getInputStream();
                    tmpOut = socket.getOutputStream();
                    Log.d("myLogs", "> 4 > СОСТОЯНИЕ СОКЕТА -- 22" + socket.isConnected());
                }
                catch (IOException e)
                {
                }
                // Переопределяем эти потоки в нашы переменные
                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }

            // Основной метод выполнения в потоке !!!
            @Override
            public void run()
            {
                Log.d("myLogs", "> 4 > ЗАШОЛ ДО ВАЙЛА" );
                Log.d("myLogs", "> 4 > СОСТОЯНИЕ СОКЕТА -- 2244" + mmSocket.isConnected());
                byte[] buffer;  // buffer store for the stream
                int bytes;
                // Бесконечьный цыкл считования информацыии
                while (true)
                {
                    Log.d("myLogs", "> 4 > ЗАШОЛ ДО ВАЙЛА --- 1" );
                    try {
                        // Масив с байтами (длина строки которую будем считовать)
                        buffer = new byte[1024];
                        // Записоваем данные в переменную интового типа " bytes "
                        bytes = mmInStream.read(buffer);
                        // Отправляем полученую строку в хендлер
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer) .sendToTarget();
                        Log.d("myLogs", "> 4 > ЗАШОЛ ДО ВАЙЛА --- 29999999--------====== " );
                    }
                    catch (IOException e)
                    {
                        Log.d("myLogs", "> 4 > ВЫШЕЛ ИЗ ВАЙЛА  --- 1" );
                        Log.d("myLogs", "> 4 > СОСТОЯНИЕ СОКЕТА -- 225533" + mmSocket.isConnected());

                        break;
                    }
                }
            }
            // Наш метод записи и отправки в ВЫХОДНОЙ ПОТОК (на вход принемает масив байтов)
            public void write(byte[] bytes)
            {
                try
                {
                    Log.d("myLogs", "> 4 > СОСТОЯНИЕ СОКЕТА -- 2255" + mmSocket.isConnected());
                    Log.d("myLogs", "> 4 > БУДУ ПЕРЕДАЛ СТРОКУ  --- ++ " );

                    // Отправка байтов
                    mmOutStream.write(bytes);
                    Log.d("myLogs", "> 4 > ПЕРЕДАЛ СТРОКУ  --- ++ " );
                    // Share the sent message back to the UI Activity
                } catch (IOException e)
                {
                    e.printStackTrace();
                    Log.d("myLogs", "> 4 > ВЫШЕЛ С ОШЫБКОЙ ПЕРЕДАЛ СТРОКУ  --- ++ " );
                }
            }
            // Метод закрытия сокета
            public void cancel()
            {
                try {
                    mmSocket.close();
                }
                catch (IOException e) {

                }
            }
        }/////////////////////////////////////////////////////////////////////////////////////////////// Конец класса передачи данных

    }*/


}
