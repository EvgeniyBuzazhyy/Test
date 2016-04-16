package com.example.evgeniy_pc.tictactoe;


import android.util.Log;

public class Engine {

    final String LOG_TAG = "myLogs";
    int statusCompHoda;
    int int_status_level = 0;
    int mas[] = new int[9];             // масив из 9 кнопок
    int setElemens[][] = new int[4][];
    boolean rand;
    int reandomWinPlayer;


    public int[][] startEngine(int getmas[][]) {

        this.setElemens = getmas;
        this.statusCompHoda = getmas[1][0];
        this.int_status_level = getmas[2][0];
        this.reandomWinPlayer = getmas[3][0];
        Log.d("myLogs", "---   ДВИГЛО пришло reandomWinPlayer  --- "+reandomWinPlayer);

        for (int i = 0; i < 9; i++) {
            mas[i] = setElemens[0][i];
        }


        //Если это 3 тий уровень то рендом определить давать выйграть игроку или нет
     /*   if (int_status_level == 3)
        {
            int proc = 50;
            int i = (int) (Math.random() * 100);
            if (i <= proc) {
                rand = true;
            } else {
                rand = false;
            }
        }*/


        // КОМПЬЮТЕР ПЫТАЕТЬСЯ ВЫИГРАТЬ
        ///////////////////////////////////
        // строка 012
        if (mas[0] == -1 & mas[1] == -1 & mas[2] == 0 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[2] = -1;
        }
        if (mas[0] == -1 & mas[1] == 0 & mas[2] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[1] = -1;
        }
        if (mas[0] == 0 & mas[1] == -1 & mas[2] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[0] = -1;
        }

        //строка 345
        if (mas[3] == -1 & mas[4] == -1 & mas[5] == 0 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[5] = -1;
        }
        if (mas[3] == -1 & mas[4] == 0 & mas[5] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[4] = -1;
        }
        if (mas[3] == 0 & mas[4] == -1 & mas[5] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[3] = -1;
        }
        //строка 678
        if (mas[6] == -1 & mas[7] == -1 & mas[8] == 0 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[8] = -1;
        }
        if (mas[6] == -1 & mas[7] == 0 & mas[8] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[7] = -1;
        }
        if (mas[6] == 0 & mas[7] == -1 & mas[8] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[6] = -1;
        }
        //строка 036
        if (mas[0] == -1 & mas[3] == -1 & mas[6] == 0 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[6] = -1;
        }
        if (mas[0] == -1 & mas[3] == 0 & mas[6] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[3] = -1;
        }
        if (mas[0] == 0 & mas[3] == -1 & mas[6] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[0] = -1;
        }
        //строка 147
        if (mas[1] == -1 & mas[4] == -1 & mas[7] == 0 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[7] = -1;
        }
        if (mas[1] == -1 & mas[4] == 0 & mas[7] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[4] = -1;
        }
        if (mas[1] == 0 & mas[4] == -1 & mas[7] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[1] = -1;
        }
        //строка 258
        if (mas[2] == -1 & mas[5] == -1 & mas[8] == 0 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[8] = -1;
        }
        if (mas[2] == -1 & mas[5] == 0 & mas[8] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[5] = -1;
        }
        if (mas[2] == 0 & mas[5] == -1 & mas[8] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[2] = -1;
        }
        //строка 048
        if (mas[0] == -1 & mas[4] == -1 & mas[8] == 0 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[8] = -1;
        }
        if (mas[0] == -1 & mas[4] == 0 & mas[8] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[4] = -1;
        }
        if (mas[0] == 0 & mas[4] == -1 & mas[8] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[0] = -1;
        }
        //строка 246
        if (mas[2] == -1 & mas[4] == -1 & mas[6] == 0 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[6] = -1;
        }
        if (mas[2] == -1 & mas[4] == 0 & mas[6] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[4] = -1;
        }
        if (mas[2] == 0 & mas[4] == -1 & mas[6] == -1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[2] = -1;
        }

        Log.d("myLogs", "--- ДВИГЛО 1 ВЫШЕЛ --- ");


        //Начало 2-го уровня
        if (int_status_level >= 2) {
            // КОМПЬЮТЕР ПЫТАЕТЬСЯ ПОМЕШАТЬ
            /////////////////////////////////////////////
            // строка 012

            if (mas[0] == 1 & mas[1] == 1 & mas[2] == 0 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[2] = -1;
            }
            if (mas[0] == 1 & mas[1] == 0 & mas[2] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[1] = -1;
            }
            if (mas[0] == 0 & mas[1] == 1 & mas[2] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[0] = -1;
            }
            // строка 345
            if (mas[3] == 1 & mas[4] == 1 & mas[5] == 0 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[5] = -1;
            }
            if (mas[3] == 1 & mas[4] == 0 & mas[5] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[4] = -1;
            }
            if (mas[3] == 0 & mas[4] == 1 & mas[5] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[3] = -1;
            }
            //строка 678
            if (mas[6] == 1 & mas[7] == 1 & mas[8] == 0 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[8] = -1;
            }
            if (mas[6] == 1 & mas[7] == 0 & mas[8] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[7] = -1;
            }
            if (mas[6] == 0 & mas[7] == 1 & mas[8] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[6] = -1;
            }
            //строка 036
            if (mas[0] == 1 & mas[3] == 1 & mas[6] == 0 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[6] = -1;
            }
            if (mas[0] == 1 & mas[3] == 0 & mas[6] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[3] = -1;
            }
            if (mas[0] == 0 & mas[3] == 1 & mas[6] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[0] = -1;
            }
            //строка 147
            if (mas[1] == 1 & mas[4] == 1 & mas[7] == 0 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[7] = -1;
            }
            if (mas[1] == 1 & mas[4] == 0 & mas[7] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[4] = -1;
            }
            if (mas[1] == 0 & mas[4] == 1 & mas[7] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[1] = -1;
            }
            //строка 258
            if (mas[2] == 1 & mas[5] == 1 & mas[8] == 0 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[8] = -1;
            }
            if (mas[2] == 1 & mas[5] == 0 & mas[8] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[5] = -1;
            }
            if (mas[2] == 0 & mas[5] == 1 & mas[8] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[2] = -1;
            }

            if (int_status_level<3)
            {
                levelThree();
                Log.d("myLogs", "---  ВВВ ДВИГЛО 2 ВЫШЕЛ --- ");
            }
            if (int_status_level==3 & reandomWinPlayer==1 )
            {

                    levelThree();
                    Log.d("myLogs", "---  ПОЗВОЛИЛ ВЫЙГРАТЬ ВВВ ДВИГЛО 3 ВЫШЕЛ --- ");

            }

            //строка 048
            if (mas[0] == 1 & mas[4] == 1 & mas[8] == 0 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[8] = -1;
            }
            if (mas[0] == 1 & mas[4] == 0 & mas[8] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[4] = -1;
            }
            if (mas[0] == 0 & mas[4] == 1 & mas[8] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[0] = -1;
            }
            //строка 246
            if (mas[2] == 1 & mas[4] == 1 & mas[6] == 0 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[6] = -1;
            }
            if (mas[2] == 1 & mas[4] == 0 & mas[6] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[4] = -1;
            }
            if (mas[2] == 0 & mas[4] == 1 & mas[6] == 1 & statusCompHoda == 0) {
                statusCompHoda += 1;
                mas[2] = -1;
            }
            Log.d("myLogs", "--- ДВИГЛО 2 ВЫШЕЛ --- ");
        }//Конец 2-го уровня


        //Если у нас 3 - тий левел
        if (int_status_level == 3) {
            if (mas[0] == 1 & mas[8] == 1) {
                if (mas[3] == 0 & statusCompHoda == 0) {
                    mas[3] = -1;
                    statusCompHoda += 1;
                }
                if (mas[5] == 0 & statusCompHoda == 0) {
                    mas[5] = -1;
                    statusCompHoda += 1;
                }
            }

            if (mas[2] == 1 & mas[6] == 1) {
                if (mas[1] == 0 & statusCompHoda == 0) {
                    mas[1] = -1;
                    statusCompHoda += 1;
                }
                if (mas[7] == 0 & statusCompHoda == 0) {
                    mas[7] = -1;
                    statusCompHoda += 1;
                }
            }
        }

        //Начало 3-го уровня
        if (int_status_level == 3) {
            if (mas[4] == 0 & statusCompHoda == 0)                     // если Центр пустой
            {
                mas[4] = -1;                     // то ставим в центр
                statusCompHoda += 1;
            }// если занято то:
            if (mas[0] == 0 & statusCompHoda == 0)                     // если Центр пустой
            {
                mas[0] = -1;                     // то ставим в центр
                statusCompHoda += 1;
            }// если занято то:
            if (mas[2] == 0 & statusCompHoda == 0)                     // если Центр пустой
            {
                mas[2] = -1;                     // то ставим в центр
                statusCompHoda += 1;
            }// если занято то:
            if (mas[6] == 0 & statusCompHoda == 0)                     // если Центр пустой
            {
                mas[6] = -1;                     // то ставим в центр
                statusCompHoda += 1;
            }// если занято то:
            if (mas[8] == 0 & statusCompHoda == 0)                     // если Центр пустой
            {
                mas[8] = -1;                     // то ставим в центр
                statusCompHoda += 1;
            }// если занято то:
            Log.d("myLogs", "--- ДВИГЛО 3 ВЫШЕЛ --- ");
        }//Конец 3-го уровня


        // --- Запаковка измененных значений ---
        for (int i = 0; i < 9; i++) {
            setElemens[0][i] = mas[i];
        }

        setElemens[1][0] = statusCompHoda;

        // Отправка результата.
        return setElemens;
    }

    public void levelThree() {
        if (mas[0] == 1 & mas[4] == 1 & mas[8] == 0 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[8] = -1;
        }
        if (mas[0] == 1 & mas[4] == 0 & mas[8] == 1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[4] = -1;
        }
        if (mas[0] == 0 & mas[4] == 1 & mas[8] == 1 & statusCompHoda == 0) {
            statusCompHoda += 1;
            mas[0] = -1;

        }
    }
}