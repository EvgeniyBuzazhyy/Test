package com.example.evgeniy_pc.tictactoe;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Evgeniy_pc on 19.01.2016.
 */
public class BluetoothClientConnect
{
    //Константы используются для передачи в ХЕНДЛЕР
    protected static final int SUCCESS_CONNECT = 0;
    protected static final int MESSAGE_READ    = 1;
    protected static final int SOCCET_CLOSE    = 2;
    //Класс передачи данных
    DataTransfer dataTransfer;
    //Объект самого себя
    ClientBluetooth client;
    //Блютус девайс к которому будем подключаться
    BluetoothDevice mmDeviceee;

    // --- Конструктор класса ---
    public BluetoothClientConnect(ClientBluetooth clientBluetooth, BluetoothDevice mmDeviceee)
    {
        this.client = clientBluetooth;  //Ссылка на внешний основной класс игры
        this.mmDeviceee = mmDeviceee;   //Девайс который приходит
        //Запускаем класс который организовует подключение к серверу
        ClientConnect conn = new ClientConnect(mmDeviceee);
        conn.start();
    }
    //------------------------- ХЕНДЛЕР ПРИНИМАЕТ ДАННЫЕ ИЗ ПОТОКОВ -------------------------------
    //Принимает сообщения из других потоков
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            Log.d("myLogs", "В ХЕНДЛЕРЕ  !!!__ ");
            super.handleMessage(msg);
            switch(msg.what)
            {
                // при получении подключения то есть СОКЕТА
                case SUCCESS_CONNECT:
                    dataTransfer = new DataTransfer((BluetoothSocket)msg.obj);
                    dataTransfer.start();
                    String s = "successfully connected";
                    Log.d("myLogs", "В ХЕНДЛЕРЕ  ПОДКЛЮЧЕН !!!__ " );
                    client.connected();
                    break;
                // приходит любая строка
                case MESSAGE_READ:
                    byte[] readBuf = (byte[])msg.obj;
                    String string = new String(readBuf);
                    Log.d("myLogs", "ПРОЧИТАНО В ХЕНДЛЕРЕ  !!!__ "+string );
                    getStr(string);                        // передаем в сроку которая её обрабатывает
                    break;
                //Хендлер получит уведомление что сокет не подключен
                case SOCCET_CLOSE:
                    client.timeConnectingOver();
                    break;
            }
        }
    };

    // --- Отправка строки от КЛИЕНТА ---
    // --- Метод ОТПРАВЛЯЕТ ДАННЫЕ ---
    public  void sendKr(ClientBluetooth cl, String poz)
    {
        client=cl;
        String str = poz;
        dataTransfer.write(str.getBytes());
    }
    // --- Чтение строки ---
    //Метод на вход принимает строку
    public void getStr(String string)
    {
        String str = string;

        //Условие если строка начинается на букву 'g'
        //То это ХОД в игре
        if (str.startsWith("g"))
        {
            Log.d("myLogs", "Клиент --- Получил строку от Сервера 8888 --- " + str.toString());
            Log.d("myLogs", "Клиент ---Метод проверки отработал--- ");

            char x = str.charAt(1);
            char y = str.charAt(2);

            int posInMas = Character.getNumericValue(x);
            int znachInPos = Character.getNumericValue(y);
            if (znachInPos == 2)
            {
                znachInPos = -1;
            }

                client.one = posInMas;
                client.two = znachInPos;
                client.tray = 1;
                client.Otress();

            Log.d("myLogs", "Инт 1 =  " + posInMas);
            Log.d("myLogs", "Инт 2 =  " + znachInPos);
        }
        //------ ВЫЗОВА МЕТОДОВ О ПРЕДЛОЖЕНИ СЫГРАТЬ СНОВА - ОЧИСТКА !
        if (str.startsWith("n"))
        {
            Log.d("myLogs", "ЗАШЕЛ В НОВ ИГРА--- ");

            client.playAgen();
        }
    }

    //--------------------- НАш класс ПОТОК Клиент ------------------------------------------------- КЛИЕНТ
    private class ClientConnect extends Thread
    {
        BluetoothSocket mmSocket;
        BluetoothDevice mmDevice;
        // уникальный код который будет служить неким ключем до нашего приложения
        String uniqueId = "186abee7-c6d8-4943-bcfd-d1241f665237";
        UUID MY_UUID = UUID.fromString(uniqueId);
        //Конструктор класса
        public ClientConnect (BluetoothDevice device)
        {
            // используем вспомагательную переменную , которую в дальнейшем
            // свяжем с mmSocket
            // !!! НУЖНО СТАВИТЬ null ЧТОБЫ МОЖНО БЫЛО ДЕЛАТЬ ПРИСВОЕНИЕ БЕЗ ПРОБЛЕМ
            BluetoothSocket tmp = null;
            mmDevice = device;
            // получаем BluetoothSocket чтобы соедениться с BluetoothDevice
            try
            {
                // MY_UUID это UUID который забит в сервере
                // Создание сокета в котором содержится информация о нашем устройстве
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            }
            catch (IOException e)
            {
                // -------------------------- ОБРАБОТЧИК ОШИБОК 1111
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        // --- Основной метод выполнения в потоке ---
        @Override
        public void run()
        {
            Log.d("myLogs", "> 4 > ЗАШЁЛ В СЛУШАТЕЛЬ НА ПОДКЛЮЧЕНИЕ 0000 " + mmDevice.getName());
            //отменяем сканирование поскольку оно тормозит соединение

            try
            {
                //Соединяемся с устройством через сокет
                // Метод блокирует выполнение программы до
                // установки соединения или возникновения ошибки
                mmSocket.connect();
                //передаем управления сокетом в (отдельный поток)

                Log.d("myLogs", "> 4 > ЗАШЕЛ В РАН    +++КЛИЕНТА+++   +++ПОДКЛЮЧИЛИСЬ+++ ! 2" );
            }
            catch (IOException e)
            {
                Log.d("myLogs", "> 4 > ПОЛУЧИЛАСЬ ОШИБКА  +++КЛИЕНТА+++   +++ПОДКЛЮЧИЛИСЬ+++ ! 2" );
                //Метод основного  класса который сообщает что врямя подключения вышло и выходит с основного класса
                mHandler.obtainMessage(SOCCET_CLOSE).sendToTarget();
                //И закрываем сокет
                //cancel();

                //Печать в лог
                e.printStackTrace();
                return;
            }
            // Подключенный обект сокета отправляем на внешку (Извлекаем из потока)
            // Отправляем сообщение в хендлер
            mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();

            //после того как получили сокет для економии ресурсов необходимо всегда вызывать метод close();
            ///cancel();
        }
        //ОТМЕНА ОЖИДАНИЯ СОКЕТА //
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
    public class DataTransfer extends Thread
    {   // Переменная которая принимает сокет
        BluetoothSocket mmSocket;
        // Переменные входных и выходных данных
        InputStream mmInStream ;
        OutputStream mmOutStream;
        // Конструктор класса
        public DataTransfer (final BluetoothSocket socket)
        {
            // Перезаписываем сокет который пришёл в наш
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

        // --- Основной метод выполнения в потоке !!! ---
        @Override
        public void run()
        {
            Log.d("myLogs", "> 4 > ЗАШЁЛ ДО ВАЙЛА" );
            Log.d("myLogs", "> 4 > СОСТОЯНИЕ СОКЕТА -- 2244" + mmSocket.isConnected());
            byte[] buffer;  // buffer store for the stream
            int bytes;
            // Бесконечьный цыкл считывания информации
            while (true)
            {
                Log.d("myLogs", "> 4 > ЗАШЁЛ ДО ВАЙЛА --- 1" );
                try
                {
                    // Масив с байтами (длина строки которую будем считывать)
                    buffer = new byte[1024];
                    // Записываем данные в переменную интового типа " bytes "
                    bytes = mmInStream.read(buffer);
                    // Отправляем полученую строку в хендлер
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer) .sendToTarget();
                    Log.d("myLogs", "> 4 > ЗАШЁЛ ДО ВАЙЛА --- 29999999--------====== " );
                }
                catch (IOException e)
                {
                    Log.d("myLogs", "> 4 > ВЫШЕЛ ИЗ ВАЙЛА  --- 1" );
                    Log.d("myLogs", "> 4 > СОСТОЯНИЕ СОКЕТА -- 225533" + mmSocket.isConnected());

                    break;
                }
            }
        }
        // ---- Наш метод записи и отправки в ВЫХОДНОЙ ПОТОК (на вход принимает массив байтов) ------
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
