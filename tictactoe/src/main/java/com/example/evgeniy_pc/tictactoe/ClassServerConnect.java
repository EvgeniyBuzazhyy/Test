package com.example.evgeniy_pc.tictactoe;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class ClassServerConnect
{
    final String LOG_TAG = "myLogs";
    protected static final int SUCCESS_CONNECT = 0;
    protected static final int MESSAGE_READ = 1;
    ConnectedThread connectedThread;
    BluetoothAdapter bluetooth;
    ServerBluetooth server;


    // Конструктр класса
    public ClassServerConnect(ServerBluetooth server)
    {
        this.server = server;
        bluetooth = BluetoothAdapter.getDefaultAdapter();    // убеждаемся что телефон имеет адаптер и вызываем его
        // Запускаем наш поток сервер
        AcceptThead acce = new AcceptThead();
        //Старт
        acce.start();

    }
    //---------------------------------------------- ХЕНДЛЛЕР ПРИНИМАЕТ ДАННЫЕ ИЗ ПОТОКОВ -----------------------------------------------------------------------------------------
    public Handler mHandlerServer = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {

            Log.d("myLogs", "В ХЕНДЛЕРЕ  !!!__ ");
            super.handleMessage(msg);
            switch(msg.what)
            {
                case SUCCESS_CONNECT:                      // при получении подключения то есть СОКЕТА
                    connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
                    connectedThread.start();
                    //передаем имя подключенного девайса в основное активити игры
                    server.nameBluePlayer = ((BluetoothSocket) msg.obj).getRemoteDevice().getName();
                    String s = "successfully connected";
                    Log.d("myLogs", "В ХЕНДЛЕРЕ  ПОДКЛЮЧЕН !!!__ " );
                    server.connected();
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

    // Отправка строки от СЕРВЕРА
    public void sendKr(ServerBluetooth ser, String poz)
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

                server.one = posInMas;
                server.two = znachInPos;
                server.tray = 1;
                server.Otres();
        }

        //------ ВЫЗОВА МЕТОДОВ О ПРЕДЛОЖЕНИ СЫГРАТЬ СНОВА - ОЧИСТКА !
        if (str.startsWith("n"))
        {
            Log.d("myLogs", "ЗАШЕЛ В НОВ ИГРА--- ");
             server.playAgen();
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


        UUID MY_UUID = UUID.fromString(uniqueId);          // уникальный код который будет служить неким ключем до нашего приложения

        BluetoothServerSocket mmServerSocket;                                            // переменная СЕРВЕР сокета
        //Конструктор класса
        public AcceptThead()                                                             // конструктор класса
        {
            Log.d("myLogs", "> 4 > ЗАШЕЛ  консруктор первый ГГШВ = " + uniqueId);
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
            mmServerSocket = tmp;                                                        // передаем настроенный сокет в объект класса BluetoothServerSocket
            Log.d("myLogs", "> 4 > ЗАШЕЛ в КОНСТРУКТОР класса 1 " );
        }
        //----- перегруженный метод ПОТОКА ран срабатывает всегда в этом классе ----------
        @Override
        public  void run()
        {
            BluetoothSocket socet = null;                                                // создаем пустой сокет
            // ждем пока не произойдет ошибка
            // или будет возвращен сокет
            while(true)                                                                  // Бесконечный цыкл который ПОСТОЯННО ожидает сканирование
            {
                try
                {
                    Log.d("myLogs", "> 4 > ЗАШЕЛ В РАН ЖДЕМ ПОДКЛЮЧЕНИЯ ! 2" );
                    socet = mmServerSocket.accept();                                     // в  этот пустой сокет запишем сокет  который придет когда будет подключение

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
                    //ConnectedThread pr =new ConnectedThread(socet);                                       // передаем этот сокет на обработку и РАБОТУ С НИМ
                    // pr.start();
                    mHandlerServer.obtainMessage(SUCCESS_CONNECT, socet).sendToTarget();
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

    //------------------------- Наш класс Поток передачи данных ------------------------------------ ПОТОК ПЕРЕДАЧИ ДАННЫХ (Общий для сервера и клиента)
    public class ConnectedThread extends Thread
    {   // Переменная которая принимает сокет
        BluetoothSocket mmSocket;
        // Переменные входных и выходных данных
        InputStream mmInStream ;
        OutputStream mmOutStream;
        // Конструктор класса
        public ConnectedThread(final BluetoothSocket socket)
        {
            // Перезаписываем сокет который пришел в наш
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
            // Переопределяем эти потоки в наши переменные
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        // Основной метод выполнения в потоке !!!
        @Override
        public void run()
        {
            Log.d("myLogs", "> 4 > ЗАШЁЛ ДО ВАЙЛА" );
            Log.d("myLogs", "> 4 > СОСТОЯНИЕ СОКЕТА -- 2244" + mmSocket.isConnected());
            byte[] buffer;  // buffer store for the stream
            int bytes;
            // Бесконечный цыкл считывания информации
            while (true)
            {
                Log.d("myLogs", "> 4 > ЗАШЁЛ ДО ВАЙЛА --- 1" );
                try {
                    // Массив с байтами (длина строки которую будем считывать)
                    buffer = new byte[1024];
                    // Записываем данные в переменную интового типа " bytes "
                    bytes = mmInStream.read(buffer);
                    // Отправляем полученую строку в хендлер
                    mHandlerServer.obtainMessage(MESSAGE_READ, bytes, -1, buffer) .sendToTarget();
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
        // Наш метод записи и отправки в ВЫХОДНОЙ ПОТОК (на вход принимает массив байтов)
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
                Log.d("myLogs", "> 4 > ВЫШЕЛ С ОШИБКОЙ ПЕРЕДАЛ СТРОКУ  --- ++ " );
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

}
