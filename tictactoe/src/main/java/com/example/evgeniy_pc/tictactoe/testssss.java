package com.example.evgeniy_pc.tictactoe;

/**
 * Created by Evgeniy_pc on 17.01.2016.
 */
public class testssss {
    /*
        switch (v.getId())
        {
            case R.id.settings:
                Intent intentSettings = new Intent(this, Settings.class);
                startActivity(intentSettings);
                break;

            case R.id.button_newGame:
                //--------------- Новая игра Обновление всех переменных ---------------------------
                sp.autoPause(); // /СТОП Проигрование короткого Выйграшного трека
                playMusic();  // Метод запуска проигрования фоновой музыки
                score_newGame++;
                statusCompHoda=0;
                win1=0; win2=0; win3=0;
                rezult=0;
                pustoyChenterOrNot = 0 ;
                sh=0;
                ren=0;
                stopIgra=0;

                // ----- Очистка масива значений
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
                    addres[0]=R.drawable.kr300x300;
                    addres[1]=R.drawable.kr_n1300x300;
                    addres[2]=R.drawable.kr_w300x300;
                    addres[3]=R.drawable.nolik300x300;
                    addres[4]=R.drawable.nolik_nichya300x300;
                    addres[5]=R.drawable.nolik_w300x300;
                    im_play_android.setImageResource(R.drawable.ic_panorama_fish_eye_black_48dp);
                    im_play_player.setImageResource(R.drawable.ic_clear_black_48dp);
                }
                else
                {
                    addres[0]=R.drawable.nolik300x300;
                    addres[1]=R.drawable.nolik_nichya300x300;
                    addres[2]=R.drawable.nolik_w300x300;
                    addres[3]=R.drawable.kr300x300;
                    addres[4]=R.drawable.kr_n1300x300;
                    addres[5]=R.drawable.kr_w300x300;
                    im_play_android.setImageResource(R.drawable.ic_clear_black_48dp);
                    im_play_player.setImageResource(R.drawable.ic_panorama_fish_eye_black_48dp);

                    // Когда очередь хода за Андроидом
                    stepComputer(true);
                    Otres(false);
                }

                break;
            // ------------------------------- Конец нашой очистки и игры снова -----------------


            case R.id.imageView1:
                // защита чтоб игра была закончина
                if (stopIgra==0&&mas[0]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша и ЯЧЕЙКА ДОЛЖНА БЫТЬ ПУСТОЙ == 0
                {
                    //int rrr = R.drawable.kr300x300;
                    im[0].setImageResource(addres[0]);
                    im[0].startAnimation(animComboClic);// устанавка анимаци
                    mas[0] = 1;
                    // вызов метода Определения победителя передаем true для того чтоб обозначить проверку от себя
                    Otres(true);
                    // очередь хода передаем false для того чтоб не сработал рендомный вариант Андроида
                    stepComputer(false);
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                }
                break;
            case R.id.imageView2:
                if (stopIgra==0&&mas[1]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    im[1].setImageResource(addres[0]);
                    im[1].startAnimation(animComboClic);// устанавка анимаци
                    mas[1] = 1;
                    Otres(true);
                    stepComputer(false);
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                }
                break;
            case R.id.imageView3:
                if (stopIgra==0&&mas[2]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    im[2].setImageResource(addres[0]);
                    im[2].startAnimation(animComboClic);// устанавка анимаци
                    mas[2] = 1;
                    Otres(true);
                    stepComputer(false);
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                }
                break;
            //----------------------------------------------

            case R.id.imageView4:
                if (stopIgra==0&&mas[3]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    im[3].setImageResource(addres[0]);
                    im[3].startAnimation(animComboClic);// устанавка анимаци
                    mas[3] = 1;
                    Otres(true);
                    stepComputer(false);
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                }
                break;
            case R.id.imageView5:
                if (stopIgra==0&&mas[4]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    im[4].setImageResource(addres[0]);
                    im[4].startAnimation(animComboClic);// устанавка анимаци
                    mas[4] = 1;
                    Otres(true);
                    stepComputer(false);;
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                }
                break;
            case R.id.imageView6:
                if (stopIgra==0&&mas[5]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    im[5].setImageResource(addres[0]);
                    im[5].startAnimation(animComboClic);// устанавка анимаци
                    mas[5] = 1;
                    Otres(true);
                    stepComputer(false);
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                }
                break;
            //----------------------------------------------

            case R.id.imageView7:
                if (stopIgra==0&&mas[6]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    im[6].setImageResource(addres[0]);
                    im[6].startAnimation(animComboClic);// устанавка анимаци
                    mas[6] = 1;
                    Otres(true);
                    stepComputer(false);;
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                }
                break;
            case R.id.imageView8:
                if (stopIgra==0&&mas[7]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    im[7].setImageResource(addres[0]);
                    im[7].startAnimation(animComboClic);// устанавка анимаци
                    mas[7] = 1;
                    Otres(true);
                    stepComputer(false);
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                }
                break;
            case R.id.imageView9:
                if (stopIgra==0&&mas[8]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    im[8].setImageResource(addres[0]);
                    im[8].startAnimation(animComboClic);// устанавка анимаци
                    mas[8] = 1;
                    Otres(true);
                    stepComputer(false);;
                    playSoundClic(); //проигрование трека при  нажатии на кнопку
                }
                break;
        }*/

    //777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777

    /*
        switch (v.getId())
        {
            // --- Иконка вызова настроек ---
            case R.id.settings:
                Intent intentSettings = new Intent(this, Settings.class);
                startActivity(intentSettings);
                break;

            case R.id.button_newGame:
                //--------------- Новая игра Обновление всех переменных ---------------------------
                // Стираем красный  цвет с текста
                text_player1.setTextColor(getResources().getColor(R.color.playerNewGame));
                text_player2.setTextColor(getResources().getColor(R.color.playerNewGame));

                sp.autoPause(); // /СТОП Проигрование короткого Выйграшного трека
                playMusic();  // Метод запуска проигрования фоновой музыки

                score_newGame++;   //Каждый раз увиличение на 1 при новой игре
                text.setText("");  // Значение текстового поля
                setBackGplayer1(); //Игрок - 1 ваш ход!
                statusCompHoda=0;
                win1=0; win2=0; win3=0;
                rezult=0;
                pustoyChenterOrNot = 0 ;
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

                // --- Условие при котором игроки будут играть разными иконками -----

                if (score_newGame%2 == 0)
                {
                    addres[0]=R.drawable.kr300x300;
                    addres[1]=R.drawable.kr_n1300x300;
                    addres[2]=R.drawable.kr_w300x300;
                    addres[3]=R.drawable.nolik300x300;
                    addres[4]=R.drawable.nolik_nichya300x300;
                    addres[5]=R.drawable.nolik_w300x300;
                    //------ Кто какими иконками играет -----
                    im_play_plaer2.setImageResource(R.drawable.ic_panorama_fish_eye_black_48dp);
                    im_play_player1.setImageResource(R.drawable.ic_clear_black_48dp);
                }
                else
                {
                    addres[0]=R.drawable.nolik300x300;
                    addres[1]=R.drawable.nolik_nichya300x300;
                    addres[2]=R.drawable.nolik_w300x300;
                    addres[3]=R.drawable.kr300x300;
                    addres[4]=R.drawable.kr_n1300x300;
                    addres[5]=R.drawable.kr_w300x300;
                    //------ Кто какими иконками играет -----
                    im_play_plaer2.setImageResource(R.drawable.ic_clear_black_48dp);
                    im_play_player1.setImageResource(R.drawable.ic_panorama_fish_eye_black_48dp);
                }
                break;
            // ------------------------------- Конец нашой очистки и игры снова -----------------

//-------------------------------------------------------------------------------------------------- Обработчик нажатий на поля ИГРЫ
            case R.id.imageView1:
                // защита чтоб игра была закончина
                if (stopIgra==0&&mas[0]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша и ЯЧЕЙКА ДОЛЖНА БЫТЬ ПУСТОЙ == 0
                {
                    if (queue==0)
                    {
                        im[0].setImageResource(addres[0]);
                        im[0].startAnimation(animComboClic);// устанавка анимаци
                        mas[0] = 1;
                        queue=1;
                        setBackGplayer2();                   //Подсведка очереди ходов
                        playSoundClic();                     //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[0].setImageResource(addres[3]);
                        im[0].startAnimation(animComboClic);// устанавка анимаци
                        mas[0] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            //----------------------------- 2
            case R.id.imageView2:
                if (stopIgra==0&&mas[1]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[1].setImageResource(addres[0]);
                        im[1].startAnimation(animComboClic);// устанавка анимаци
                        mas[1] = 1;
                        queue=1;
                        setBackGplayer2();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[1].setImageResource(addres[3]);
                        im[1].startAnimation(animComboClic);// устанавка анимаци
                        mas[1] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            //----------------------------- 3
            case R.id.imageView3:
                if (stopIgra==0&&mas[2]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[2].setImageResource(addres[0]);
                        im[2].startAnimation(animComboClic);// устанавка анимаци
                        mas[2] = 1;
                        queue=1;
                        setBackGplayer2();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[2].setImageResource(addres[3]);
                        im[2].startAnimation(animComboClic);// устанавка анимаци
                        mas[2] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            //----------------------------- 4
            case R.id.imageView4:
                if (stopIgra==0&&mas[3]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[3].setImageResource(addres[0]);
                        im[3].startAnimation(animComboClic);// устанавка анимаци
                        mas[3] = 1;
                        queue=1;
                        setBackGplayer2();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[3].setImageResource(addres[3]);
                        im[3].startAnimation(animComboClic);// устанавка анимаци
                        mas[3] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            //----------------------------- 5
            case R.id.imageView5:
                if (stopIgra==0&&mas[4]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[4].setImageResource(addres[0]);
                        im[4].startAnimation(animComboClic);// устанавка анимаци
                        mas[4] = 1;
                        queue=1;
                        setBackGplayer2();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[4].setImageResource(addres[3]);
                        im[4].startAnimation(animComboClic);// устанавка анимаци
                        mas[4] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            //----------------------------- 6
            case R.id.imageView6:
                if (stopIgra==0&&mas[5]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[5].setImageResource(addres[0]);
                        im[5].startAnimation(animComboClic);// устанавка анимаци
                        mas[5] = 1;
                        queue=1;
                        setBackGplayer2();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[5].setImageResource(addres[3]);
                        im[5].startAnimation(animComboClic);// устанавка анимаци
                        mas[5] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            //----------------------------- 7
            case R.id.imageView7:
                if (stopIgra==0&&mas[6]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[6].setImageResource(addres[0]);
                        im[6].startAnimation(animComboClic);// устанавка анимаци
                        mas[6] = 1;
                        queue=1;
                        setBackGplayer2();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[6].setImageResource(addres[3]);
                        im[6].startAnimation(animComboClic);// устанавка анимаци
                        mas[6] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            case R.id.imageView8:
                //------------------------- 8
                if (stopIgra==0&&mas[7]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[7].setImageResource(addres[0]);
                        im[7].startAnimation(animComboClic);// устанавка анимаци
                        mas[7] = 1;
                        queue=1;
                        setBackGplayer2();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[7].setImageResource(addres[3]);
                        im[7].startAnimation(animComboClic);// устанавка анимаци
                        mas[7] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
            //---------------------------- 9
            case R.id.imageView9:
                if (stopIgra==0&&mas[8]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[8].setImageResource(addres[0]);
                        im[8].startAnimation(animComboClic);// устанавка анимаци
                        mas[8] = 1;
                        queue=1;
                        setBackGplayer2();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    else
                    {
                        im[8].setImageResource(addres[3]);
                        im[8].startAnimation(animComboClic);// устанавка анимаци
                        mas[8] = -1;
                        queue=0;
                        setBackGplayer1();                  //Подсведка очереди ходов
                        playSoundClic();                    //проигрование трека при  нажатии на кнопку
                    }
                    Otres();
                }
                break;
        }*/

    //================================================================================================
    //------------------------------------------------------------------------------------ Код с Клиент Блютус
        /*
        switch (v.getId())
        {
            case R.id.newGame:           // при нажатии кнопки новая игра
                showDialog(NEW_GAME);

                break;
            // ------------------------------- Конец нашой очистки и игры снова -----------------

            case R.id.imageView1:
                // защита чтоб игра была закончина

                if (stopIgra==0&&mas[0]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша и ЯЧЕЙКА ДОЛЖНА БЫТЬ ПУСТОЙ == 0
                {
                    if (queue==0)
                    {
                        im[0].setImageResource(addres[0]);
                        im[0].startAnimation(animComboClic);// устанавка анимаци
                        mas[0] = 1;
                        queue=1;

                        poz = "g01";
                        sendKr(client,poz);
                    }
                    Otress();
                }
                break;
            case R.id.imageView2:
                if (stopIgra==0&&mas[1]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[1].setImageResource(addres[0]);
                        im[1].startAnimation(animComboClic);// устанавка анимаци
                        mas[1] = 1;
                        queue=1;

                        poz = "g11";
                        sendKr(client,poz);
                    }
                    Otress();
                }
                break;
            case R.id.imageView3:
                if (stopIgra==0&&mas[2]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[2].setImageResource(addres[0]);
                        im[2].startAnimation(animComboClic);// устанавка анимаци
                        mas[2] = 1;
                        queue=1;

                        poz = "g21";
                        sendKr(client,poz);
                    }
                    Otress();
                }
                break;
            //----------------------------------------------

            case R.id.imageView4:
                if (stopIgra==0&&mas[3]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[3].setImageResource(addres[0]);
                        im[3].startAnimation(animComboClic);// устанавка анимаци
                        mas[3] = 1;
                        queue=1;

                        poz = "g31";
                        sendKr(client,poz);
                    }
                    Otress();
                }
                break;
            case R.id.imageView5:
                if (stopIgra==0&&mas[4]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[4].setImageResource(addres[0]);
                        im[4].startAnimation(animComboClic);// устанавка анимаци
                        mas[4] = 1;
                        queue=1;

                        poz = "g41";
                        sendKr(client,poz);
                    }
                    Otress();
                }
                break;
            case R.id.imageView6:
                if (stopIgra==0&&mas[5]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[5].setImageResource(addres[0]);
                        im[5].startAnimation(animComboClic);// устанавка анимаци
                        mas[5] = 1;
                        queue=1;

                        poz = "g51";
                        sendKr(client,poz);
                    }
                    Otress();
                }
                break;
            //----------------------------------------------

            case R.id.imageView7:
                if (stopIgra==0&&mas[6]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[6].setImageResource(addres[0]);
                        im[6].startAnimation(animComboClic);// устанавка анимаци
                        mas[6] = 1;
                        queue=1;

                        poz = "g61";
                        sendKr(client,poz);
                    }
                    Otress();
                }
                break;
            case R.id.imageView8:
                if (stopIgra==0&&mas[7]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[7].setImageResource(addres[0]);
                        im[7].startAnimation(animComboClic);// устанавка анимаци
                        mas[7] = 1;
                        queue=1;

                        poz = "g71";
                        sendKr(client,poz);
                    }
                    Otress();
                }
                break;
            case R.id.imageView9:
                if (stopIgra==0&&mas[8]==0)         // код ниже будет роботать только тогда когда небыло КОМАНДЫ СТОП при формировании Выграша
                {
                    if (queue==0)
                    {
                        im[8].setImageResource(addres[0]);
                        im[8].startAnimation(animComboClic);// устанавка анимаци
                        mas[8] = 1;
                        queue=1;

                        poz = "g81";
                        sendKr(client,poz);
                    }
                    Otress();
                }
                break;
        }*/
}
