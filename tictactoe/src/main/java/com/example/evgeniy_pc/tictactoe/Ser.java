package com.example.evgeniy_pc.tictactoe;


import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Ser
{
    private ServerSocket serverSocket;                               // Обект СокетСервер
    Socket socket = null;
    InternetServer server;



    Handler updateConversationHandler;                               // позволяет нам иметь доступ к елиментам UI
    String strMes;

    Thread serverThread = null;                                      // обект потока

    public static final int SERVERPORT = 6001;                       // интовая константа
    public static final String TAG = "myLogs";                    // константа логов

    public String pubstr;



    String fff;



    public Ser(InternetServer server)  // коннструктор класса
    {
        this.server = server;
       // chat = new Chat();


        updateConversationHandler = new Handler();                    // иницыализацыя хендлера


        this.serverThread = new Thread(new ServerThread());           // присвоение нашему обекту и создание нового потока потока - ServerThread() - наш поток который описан ниже
        this.serverThread.start();                                    // именно старт потока
    }

    //----------------------------------------- Отсилаем сообщение ------------------------------------

    public void sendKr(String poz)
    {
        // с поля ввода записоваем данные в строку s

     pubstr = poz;

        PrintWriter out = null;
        try {
            //   out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(pubstr); //    используя обект записи посилаем строку в выходящий поток                                    //    используя обект записи посилаем строку в выходящий поток

            out.flush();       // чистим поток
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //---------------------- 1   Поток сервера СОЗДАЕТ СОКЕТ  -------------------------------------------------------
    class ServerThread implements Runnable                                                          // наш поток который запускает сокет сервера
    {
        public void run()                                              // главний метод запуска
        {
            try {
                serverSocket = new ServerSocket(SERVERPORT);                                        // Создание сокета сервера с портом - 6000
                Log.d("myLogs", "Сервер --- Создание СОКЕТА !!!");

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Log.d("myLogs", "Сервер --- ОЖЫДАНИЕ ПОДКЛЮЧЕНИЯ !!!");
                socket = serverSocket.accept();                                                 // ожыдане подключения к нашему сокет серверу ставим метод - .accept() - который ожидает подключение

                CommunicationThread commThread = new CommunicationThread(socket);          //Наш поток :  поток связи  на выход принемает обект типа Runnable
                new Thread(commThread).start();                                            // старт нашего потока связи
                Log.d("myLogs", "Сервер --- ПОДКЛЮЧИЛСЯ ++++++++ !!!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }// --------------------------------------------------------------------------- Конец внутренего класса потока

    // -------- Создаем еще один поток
    class CommunicationThread implements Runnable                                                   // на выход принемает обект типа Runnable
    {
        // 2 переменные
        private Socket clientSocket;                       // переменная просто Сокета
        private BufferedReader input;                      // обект типа БуверныйЧитатель

        // конструктор класса
        public CommunicationThread(Socket clientSocket)    // на вход принемает (сокет - который прешол от слиента)
        {
            this.clientSocket = clientSocket;              // перегружаем его в наш обект сокета
            // в трае
            try {
// переменная типа буфер =  нов Буфер (нов входящие данные (с наш сокета. получыить_входящие_поток))

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // --------------------- основной метод потока ----------
        public void run() {


            while (!Thread.currentThread().isInterrupted()) // если поток не прерванный то           БЕСКОНЕЧНЫЙ ЦЫКЛ СЧИТОВАНИЯ !!!!!
            {
                // в трае
                try {

                    String read = input.readLine();                                                 // чтение строки которая пришла - в стринговую переменную записывае прочитать СТРОКИ

                    updateConversationHandler.post(new updateUIThread(read));                       // через обект ХЕНДЛЕРА (имеем связь с UI) - во внаш внешний вызываем метод и отправляем стириг

                    // чтение строки которая пришла - в стринговую переменную записывае прочитать СТРОКИ

// хендлер через метод ПОЧТА на внешний клас отправляем (иницыализацыя нашего класса updateUIThread (и предаем строку которою прочитали))
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
        public updateUIThread(String str)                        // конструктор на вход принемает стринг
        {
            this.msg = str;                                      // перегруска стринга которая пришла в нашу стринговую переменную
            Log.d("myLogs", "Сервер --- Получил строку от клиента 77777--- " + msg.toString());

            Log.d("myLogs", "Сервер --- Уже читает++ " + str);
        }

        // ----перегружаный метод исполнения --
        @Override
        public void run()
        {
            getStr(msg);
        }
    }

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
            if (znachInPos == 2)
            {
                znachInPos = -1;
            }
            server.one = posInMas;
            server.two = znachInPos;

            server.tray = 1;

            server.Otres();
            Log.d("myLogs", "Инт 1 =  " + posInMas);
            Log.d("myLogs", "Инт 2 =  " + znachInPos);
        }
       /* else
        {
            naChat(str);
           // StringBuffer sb = new StringBuffer(str);
            //sb.deleteCharAt(0);
            //String s = new String(sb);
            //int who = 1;

            //chat.showMasage(who, s);
        }*/
    }

    /*public void naChat(String nachat)
    {

       server.deleveryOut(nachat);
    }*/
}