package com.example.evgeniy_pc.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity implements View.OnClickListener
{
    CheckBox checkBox_music, checkBox_sounds, checkBox_welcomeScreen;
    TextView set;
    RadioButton level_one, level_two, level_three;


    SharedPreferences musicBg;
    final String STATUS_MUSIK = "status_musik";
    final String STATUS_SOUND = "status_sound";
    final String STATUS_WELCOME_SCREEN = "status_welcome_screan";
    final String STATUS_LEVEL = "status_level";

    boolean savedStatusMusic;



    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //Нахождение чек боксов
        checkBox_music = (CheckBox) findViewById(R.id.checkBox_music);
        checkBox_sounds = (CheckBox) findViewById(R.id.checkBox_sounds);
        checkBox_welcomeScreen = (CheckBox) findViewById(R.id.checkBox_welcomeScreen);
        //Нахождение радиобаттонов
        level_one = (RadioButton) findViewById(R.id.radioButtonlevelOne);
        level_two = (RadioButton) findViewById(R.id.radioButtonlevelTwo);
        level_three = (RadioButton) findViewById(R.id.radioButtonlevelThree);

        set = (TextView) findViewById(R.id.settings);

        //------------------------------------------------------------------------------------------ При созданиии устанавлием Checked
        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_MUSIK если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        checkBox_music.setChecked(musicBg.getBoolean(STATUS_MUSIK, true));

        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_SOUND если true то мы будем играть ЗВУКИ
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        checkBox_sounds.setChecked(musicBg.getBoolean(STATUS_SOUND, true));

        //------------------------------------------------------------------------------------------ Считоваем значение настроек по активити Приветствия
        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_WELCOME_SCREEN если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        checkBox_welcomeScreen.setChecked(musicBg.getBoolean(STATUS_WELCOME_SCREEN, true));

        //------------------------------------------------------------------------------------------ Считоваем значение настроек уровня игры
        // С переменной настроек читаем информацыю котороя храниться в
        // STATUS_WELCOME_SCREEN если true то мы будем играть музыку фоновую
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        int st = musicBg.getInt(STATUS_LEVEL,0);

        switch (st)
        {
            case 1: level_one.setChecked(true); break;

            case 2: level_two.setChecked(true);  break;

            case 3: level_three.setChecked(true); break;
        }
    }
// ------------------------------ Обработчик нажатия на кнопки -------------------------------------
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // Кнопка выхода / возврата
            case R.id.settings_back:
                finish();
                break;
            // -- Кнопка выборка включения / выключения музыки фоновой
            case R.id.checkBox_music:
                if (checkBox_music.isChecked()) // вкл
                {

                    musicBg = getSharedPreferences("music", MODE_PRIVATE);
                    SharedPreferences.Editor ed = musicBg.edit();
                    ed.putBoolean(STATUS_MUSIK, true);
                    ed.commit();

                    //Если включаем музыку то сразу включаем
                    playMusic();

                    Toast.makeText(this, "Музыка ВКЛ", Toast.LENGTH_SHORT).show();

                }
                else                             // откл
                {
                    //Если отключаем музыку сразу вызываем метод отключения
                    stopMusic();
                    musicBg = getSharedPreferences("music", MODE_PRIVATE);
                    SharedPreferences.Editor ed = musicBg.edit();
                    ed.putBoolean(STATUS_MUSIK, false);
                    ed.commit();
                    Toast.makeText(this, "Музыка ОТКЛ", Toast.LENGTH_SHORT).show();
                }
                break;

            // -- Кнопка выборка включения / выключения звуков и выграшного трека
            case R.id.checkBox_sounds:
                if (checkBox_sounds.isChecked()) // вкл
                {
                    musicBg = getSharedPreferences("music",MODE_PRIVATE);
                    SharedPreferences.Editor ed = musicBg.edit();
                    ed.putBoolean(STATUS_SOUND, true);
                    ed.commit();

                    Toast.makeText(this, "Звуки ВКЛ", Toast.LENGTH_SHORT).show();
                }
                else                              // откл
                {
                    musicBg = getSharedPreferences("music",MODE_PRIVATE);
                    SharedPreferences.Editor ed = musicBg.edit();
                    ed.putBoolean(STATUS_SOUND, false);
                    ed.commit();

                    Toast.makeText(this, "Звуки ОТКЛ", Toast.LENGTH_SHORT).show();
                }
                break;

            //--- Флажок включения \ отключения окна приветствия ---
            case R.id.checkBox_welcomeScreen:
            if (checkBox_welcomeScreen.isChecked()) // вкл
            {
                musicBg = getSharedPreferences("music",MODE_PRIVATE);
                SharedPreferences.Editor ed = musicBg.edit();
                ed.putBoolean(STATUS_WELCOME_SCREEN, true);
                ed.commit();

                Toast.makeText(this, "Активити приведствия ВКЛ", Toast.LENGTH_SHORT).show();
            }
            else                              // вкл
            {
                musicBg = getSharedPreferences("music",MODE_PRIVATE);
                SharedPreferences.Editor ed = musicBg.edit();
                ed.putBoolean(STATUS_WELCOME_SCREEN, false);
                ed.commit();

                Toast.makeText(this, "Активити приведствия ОТКЛ", Toast.LENGTH_SHORT).show();
            }
            break;
            // --- Радио баттаны Уровней игры ------------------------------------------------------ Радио баттоны
            case R.id.radioButtonlevelOne:
                set.setText("Level 1");
                musicBg = getSharedPreferences("music", MODE_PRIVATE);
                SharedPreferences.Editor ed = musicBg.edit();
                ed.putInt(STATUS_LEVEL,1);
                ed.commit();

                Toast.makeText(this, "Уровень 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonlevelTwo:
                set.setText("Level 2");
                musicBg = getSharedPreferences("music", MODE_PRIVATE);
                SharedPreferences.Editor edi = musicBg.edit();
                edi.putInt(STATUS_LEVEL,2);
                edi.commit();

                Toast.makeText(this, "Уровень 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonlevelThree:
                set.setText("Level 3");
                musicBg = getSharedPreferences("music", MODE_PRIVATE);
                SharedPreferences.Editor edii = musicBg.edit();
                edii.putInt(STATUS_LEVEL,3);
                edii.commit();

                Toast.makeText(this, "Уровень 3", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    //------------------ Метод вызываеться перед тем как будет отображено это активити ------------- 2
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



    //----------------------- Метод вызываеться перед тем как будет свернуто ----------------------- 4
    @Override
    protected void onPause()
    {
        super.onPause();
        stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
    }

    //--- Играем фоновую музыку ---
    public void playMusic()
    {
        // Чтенеие из файла значение статуса музыки
        musicBg = getSharedPreferences("music",MODE_PRIVATE);
        savedStatusMusic = musicBg.getBoolean(STATUS_MUSIK, true);

        // если true
        if (savedStatusMusic)
            startService(new Intent(this, SoundBgraund.class)); //старт сервис с музыкой фоновой
    }
    // Метод остановки проигрования музыки в фоне
    public void stopMusic()
    {
        stopService(new Intent(this, SoundBgraund.class)); //стоп сервис с музыкой фоновой
    }


}
