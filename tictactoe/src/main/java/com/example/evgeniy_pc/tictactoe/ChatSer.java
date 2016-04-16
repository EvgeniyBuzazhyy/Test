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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Evgeniy_pc on 16.12.2015.
 */
public class ChatSer extends Activity
{
    private ServerSocket serverSocket;                               // Объект СокетСервер
    Socket socket = null;

    Handler updateConversationHandler;                               // позволяет нам иметь доступ к элементам UI

    Thread serverThread = null;                                      // объект потока

    private TextView text;                                           // текстовое поле
    EditText ed;
    Button bt;

    public static final int SERVERPORT = 6002;                       // интовая константа
    public static final String TAG = "myLogs";                    // константа логов

    //------------------------------ Метод создания Активити -------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        ////////////////////////////////////////////////////////////////////////////////////////////////////


        bt = (Button) findViewById(R.id.send_m);
        ed = (EditText) findViewById(R.id.editText);

        updateConversationHandler = new Handler();                    // инициализация хендлера

        Log.d("myLogs", "Сервер --- криеит прошел");
        this.serverThread = new Thread(new ServerThread());           // присвоение нашему объекту и создание нового потока потока - ServerThread() - наш поток который описан ниже
        this.serverThread.start();                                    // именно старт потока

//----------------------------------------- Отсылаем сообщение ------------------------------------
        bt.setOnClickListener(new View.OnClickListener() {
            // перегружаний метод ОН клик
            @Override
            public void onClick(View v) {
                try {
                    String str = ed.getText().toString();              // с поля ввода записываем данные в строку str
                    ed.setText("");
                    //----------------------------------------------------------------------------------------------
                    LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout_chat);
                    LayoutInflater ltInflater = getLayoutInflater();

                    View server = ltInflater.inflate(R.layout.server, linLayout, false);
                    TextView tvName_server = (TextView) server.findViewById(R.id.tvText);
                    tvName_server.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    tvName_server.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                    tvName_server.setText(str);
                    linLayout.addView(server);
                    //-----------------------------------------------------------------------------------------------

                    Log.d("myLogs", "Клиент  --- нажал кнопку отправки ");

// СОЗДАЕМ ОБЪЕКТ ДЛЯ ЗАПИСИ В СОКЕТ
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(str); //    используя обект записи посилаем строку в выходящий поток


                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    } //-------------------------------------------------------------------------- \\\\ Конец метода СОздать

    //----------------------------------- Перегруженный метод Стоп для безопасной остановки потока
    @Override
    protected void onStop() {
        super.onStop();
        try {
            serverSocket.close();                                      // остановка потока serverSocket
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //*********************************************************************************************************************************************************

    //---------------------- 1   Поток сервера СОЗДАЕТ СОКЕТ  -------------------------------------------------------
    class ServerThread implements Runnable                                                          // наш поток который запускает сокет сервера
    {
        public void run()                                              // главний метод запуска
        {
            try {
                serverSocket = new ServerSocket(SERVERPORT);                                        // Создание сокета сервера с портом - 6000
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket = serverSocket.accept();                                                 // ожидане подключения к нашему сокет серверу ставим метод - .accept() - который ожидает подключение
                CommunicationThread commThread = new CommunicationThread(socket);          //Наш поток :  поток связи  на выход принимает объект типа Runnable
                new Thread(commThread).start();                                            // старт нашего потока связи
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }// --------------------------------------------------------------------------- Конец внутреннего класса потока

    //*********************************************************************************************************************************************************

    // ---------------------- 2   Наш класс поток Связи ------------------------------------------------------
    // -------- Создаем еще один поток
    class CommunicationThread implements Runnable                                                   // на выход принимает объект типа Runnable
    {
        // 2 переменные
        private Socket clientSocket;                       // переменная просто Сокета
        private BufferedReader input;                      // обект типа БуверныйЧитатель

        // конструктор класса
        public CommunicationThread(Socket clientSocket)    // на вход принимает (сокет - который прешел от клиента)
        {
            this.clientSocket = clientSocket;              // перегружаем его в наш объект сокета
            // в трае
            try {
// переменная типа буфер =  нов Буфер (нов входящие данные (с наш сокета. получить_входящие_поток))

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // --------------------- основной метод потока ----------
        public void run() {
            while (!Thread.currentThread().isInterrupted()) // если поток не прерванный то           БЕСКОНЕЧНЫЙ ЦЫКЛ СЧИТЫВАНИЯ !!!!!
            {
                // в трае
                try {
                    String read = input.readLine();                                                 // чтение строки которая пришла - в стринговую переменную записывае прочитать СТРОКИ
// хендлер через метод ПОЧТА на внешний класс отправляем (инициализация нашего класса updateUIThread (и предаем строку которою прочитали))
                    updateConversationHandler.post(new updateUIThread(read));                       // через обект ХЕНДЛЕРА (имеем связь с UI) - во внаш внешний вызываем метод и отправляем стириг
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            this.msg = str;                                      // перегруска стринга которая пришла в нашу стринговую переменную
            Log.d("myLogs", "Сервер --- Уже читает++ " + str);
        }

        // ----перегруженный метод исполнения --
        @Override
        public void run() {
            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout_chat);
            LayoutInflater ltInflater = getLayoutInflater();

            View client = ltInflater.inflate(R.layout.clien, linLayout, false);
            TextView tvName_client = (TextView) client.findViewById(R.id.tvText);
            tvName_client.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvName_client.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

            tvName_client.setText(msg);
            linLayout.addView(client);
        }
    }
}

