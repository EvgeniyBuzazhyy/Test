package com.example.evgeniy_pc.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class ChatCli extends Activity {
    private Socket socket = null;                                  // объект типа СОкет
    public static final String TAG = "myLogs";
    Handler updateConversationHandler;                               // позволяет нам иметь доступ к элементам UI

    private static final int SERVERPORT = 6002;             // порт
    private String SERVER_IP = "192.168.0.102";// ID сервера

    // текстовое поле и поле ввода
    EditText massage, setIp;
    Button send, ip;

    //----------------------------- Метод создания активити --------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        updateConversationHandler = new Handler();                    // инициализация хендлера

        // находим кнопки текст ввода
        massage = (EditText) findViewById(R.id.editText);
        setIp = (EditText) findViewById(R.id.setIp);
        send = (Button) findViewById(R.id.send_m);
        ip = (Button) findViewById(R.id.ip);
        new Thread(new ClientThread()).start();         // новый поток (иницилизация нашего потокаClientThread . стартуем его сразу )

        //setIp.setText("192.168.0.102");
//---------------------------- Нажимаем кнопку ОТОСЛАТЬ IP------------------------------------------
       /* ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SERVER_IP = setIp.getText().toString();
                Log.d("myLogs", "ип ---  " + SERVER_IP);


                setIp.setText("");

            }
        });*/
//--------------------------- Нажимаем кнопку ОТОСЛАТЬ сообщение -----------------------------------
        // на кнопку вешаем обработчик нажатия на кнопку
       send.setOnClickListener(new View.OnClickListener() {
            // перегружаний метод ОН клик
            @Override
            public void onClick(View v) {
                try {
                    Log.d("myLogs", "Клиент  --- нажал кнопку отправки ");

                    String str = massage.getText().toString();              // с поля ввода записываем данные в строку str
                    massage.setText("");


                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout_chat);
                    LayoutInflater ltInflater = getLayoutInflater();

                    View client = ltInflater.inflate(R.layout.clien, linLayout, false);
                    TextView tvName_client = (TextView) client.findViewById(R.id.tvText);
                    tvName_client.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    tvName_client.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                    tvName_client.setText(str);
                    linLayout.addView(client);
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    // СОЗДАЕМ ОБЪЕКТ ДЛЯ ЗАПИСИ В СОКЕТ
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    out.println(str); //    используя объект записи посылаем строку в выходящий поток
                    out.flush();  // очищаем поток

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }// ---------------------------- конец он криейте ----------------------------

    //------------------------------ Наш класс поток -------------------------------------------------  СОЗДАНИЕ СОКЕТА виполняется первым
    class ClientThread implements Runnable                    // наследуется от рунейбле значит будет поток
    {
        // основной поток выполнения
        @Override
        public void run() {
            Log.d("myLogs", "Клиент --- поток клиента создался");
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);        // создаем объект ИНТЕРНЕТ адресса через метод получить имя getByName(SERVER_IP); - передаем туда IP
                socket = new Socket(serverAddr, SERVERPORT);                      // создание непосредственно сокет новый сокет (на вход IP , и порт) Все сокет СОЗДАН
                Log.d("myLogs", "Клиент --- сокет созд");

            } catch (UnknownHostException e1) {
                Log.d("myLogs", "Клиент --- вышел с ош1");
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.d("myLogs", "Клиент --- вышел с ош2");
            }
            CommunicationThread commThread = new CommunicationThread(socket);               //Наш поток :  поток связи  на выход принимает объект типа Runnable
            new Thread(commThread).start();                                                 // старт нашего потока связи
        }
    }

    // ---------------------- 2   Наш класс поток Связи ------------------------------------------------------
    // -------- Создаем еще один поток
    class CommunicationThread implements Runnable                                                   // на выход принимает объект типа Runnable
    {
        // 2 переменные
        private Socket socket;                            // переменная просто Сокета
        private BufferedReader input;                      // обект типа БуверныйЧитатель

        // конструктор класса
        public CommunicationThread(Socket clientSocket)    // на вход принимает (сокет - который прешел от клиента)
        {
            this.socket = clientSocket;              // перегружаем его в наш объект сокета
            // в трае
            try {
// переменная типа буфер =  нов Буфер (нов входящие данные (с наш сокета. получить_входящие_поток))
                this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();    // здесь можем организовать переподключение
            }
        }

        // --------------------- основной метод потока ----------
        String read = "a";

        public void run() {
            while (!Thread.currentThread().isInterrupted()) // если поток не прерванный то           БЕСКОНЕЧНЫЙ ЦЫКЛ СЧИТЫВАНИЯ !!!!!
            {
                // в трае
                if (read != null) {
                    try {
                        Log.d("myLogs", "Сервер --- Уже читает ");
                        read = input.readLine();                                                 // чтение строки которая пришла - в стринговую переменную записывае прочитать СТРОКИ
// хендлер через метод ПОЧТА на внешний класс отправляем (инициализация нашего класса updateUIThread (и предаем строку которою прочитали))
                        updateConversationHandler.post(new updateUIThread(read));                       // через объект ХЕНДЛЕРА (имеем связь с UI) - во внаш внешний вызываем метод и отправляем стириг
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else return;
            }
        }

    }//--------- конец нашего потока потока --------------------------------------------------------

    //*********************************************************************************************************************************************************
//---------------------------3  Наш метод МАИН - Активити -------------------------------------------
    class updateUIThread implements Runnable                     // наш класс наследник потока
    {
        private String msg;                                      // просто стринговоя переменная

        // конструктор класса
        public updateUIThread(String str)                        // конструктор на вход принимает стринг
        {
            this.msg = str;                                      // перегрузка стринга которая пришла в нашу стринговую переменную
            Log.d("myLogs", "Сервер --- Уже читает++ " + str);
        }

        // ----перегруженный метод исполнения --
        @Override
        public void run() {
            //----------------------------------------------------------------------------------------------
            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout_chat);
            LayoutInflater ltInflater = getLayoutInflater();
            View server = ltInflater.inflate(R.layout.server, linLayout, false);
            TextView tvName_server = (TextView) server.findViewById(R.id.tvText);
            tvName_server.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvName_server.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

            tvName_server.setText(msg);
            linLayout.addView(server);
            //-----------------------------------------------------------------------------------------------
        }
    }
}
