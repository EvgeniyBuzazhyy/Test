package com.example.evgeniy_pc.tictactoe;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Evgeniy_pc on 15.12.2015.
 */
public class Cli
{
    private ServerSocket serverSocket;                               // Объект СокетСервер
    Socket socket = null;
    InternetClient client;

    public String pubstr;
    Handler updateConversationHandler;                               // позволяет нам иметь доступ к элементам UI

    Thread serverThread = null;                                      // объект потока


    public static final int SERVERPORT = 6001;                       // интовая константа
    private String SERVER_IP = "192.168.0.102";// ID сервера
    public  static  final  String TAG = "myLogs";                    // константа логов



    public Cli(InternetClient client)  // конструктор класса
    {
        this.client = client;


        updateConversationHandler = new Handler();                    // инициализация хендлера



        new Thread(new ClientThread()).start();         // новый поток (иницилизация нашего потокаClientThread . стартуем его сразу )

    }



    //----------------------------------------- Отсылаем сообщение ------------------------------------
    public void sendNol(String hots)
    {
        // с поля ввода записываем данные в строку s


        pubstr = hots;
        PrintWriter out = null;
        try
        {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println( pubstr); //    используя объект записи посылаем строку в выходящий поток
            out.flush();  //
            //    используя объект записи посылаем строку в выходящий поток

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    //---------------------- 1   Поток сервера СОЗДАЕТ СОКЕТ  -------------------------------------------------------
    class ClientThread implements Runnable                                                          // наш поток который запускает сокет сервера
    {
        public void run()                                              // главний метод запуска
        {
            try
            {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);        // создаем объект ИНТЕРНЕТ адресса через метод получить имя getByName(SERVER_IP); - передаем туда IP
                socket = new Socket(serverAddr, SERVERPORT);
            }
            catch (UnknownHostException e1)
            {
                Log.d("myLogs", "Клиент --- вышел с ош1");
                e1.printStackTrace();
            } catch (IOException e1)
            {
                e1.printStackTrace();
                Log.d("myLogs", "Клиент --- вышел с ош2");
            }
            CommunicationThread commThread = new CommunicationThread(socket);               //Наш поток :  поток связи  на выход принимает объект типа Runnable
            new Thread(commThread).start();                                                 // старт нашего потока связи
        }
    }// --------------------------------------------------------------------------- Конец внутреннего класса потока

    // -------- Создаем еще один поток
    class CommunicationThread implements Runnable                                                   // на выход принимает объект типа Runnable
    {
        // 2 переменные
        private Socket clientSocket;                       // переменная просто Сокета
        private BufferedReader input;                      // объект типа БуверныйЧитатель

        // конструктор класса
        public CommunicationThread(Socket clientSocket)    // на вход принимает (сокет - который прешел от клиента)
        {
            this.clientSocket = clientSocket;              // перегружаем его в наш объект сокета
            // в трае
            try
            {
// переменная типа буфер =  нов Буфер (нов входящие данные (с наш сокета. получить_входящие_поток))

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        // --------------------- основной метод потока ----------
        public void run()
        {


            while (!Thread.currentThread().isInterrupted()) // если поток не прерванный то           БЕСКОНЕЧНЫЙ ЦЫКЛ СЧИТЫВАНИЯ !!!!!
            {
                // в трае
                try {

                    String read = input.readLine();
                    updateConversationHandler.post(new updateUIThread(read));                       // через объект ХЕНДЛЕРА (имеем связь с UI) - во внаш внешний вызываем метод и отправляем стириг

                } catch (IOException e)
                {
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
            this.msg = str;                                      // перегрузка стринга которая пришла в нашу стринговую переменную


            Log.d("myLogs", "Клиент --- Уже читает++ "+ str);
        }

        // ----перегруженный метод исполнения --
        @Override
        public void run()
        {
            // передашь с помощью хендлера

            getStr(msg);
        }
    }
    //--------------------------------- Метод чтение строки которую получи ----- класса Cli ------------
    public void getStr(String string)
    {
        String str = string;

        if (str.startsWith("g"))
        {
            Log.d("myLogs", "Клиент --- Получил строку от Сервера 8888 --- "+str.toString());
            Log.d("myLogs", "Клиент ---Метод проверки отработал--- ");

            char x = str.charAt(1);
            char y = str.charAt(2);

            int posInMas = Character.getNumericValue(x);
            int znachInPos = Character.getNumericValue(y);
            if (znachInPos==2)
            {znachInPos=-1;}
            client.one=posInMas;
            client.two=znachInPos;

            client.tray=1;

            client.Otres();
            Log.d("myLogs", "Инт 1 =  "+posInMas);
            Log.d("myLogs", "Инт 2 =  "+znachInPos);

        }
          /* else
    {
        naChat(str);
        //StringBuffer sb = new StringBuffer(str);
        //  sb.deleteCharAt(0);
        // String s = new String(sb);
        //int who = 2;

        //chat.showMasage(who,str);
    }*/
}
   /* public void naChat(String nachat)
    {

        client.deleveryOut(nachat);
    }*/

}

