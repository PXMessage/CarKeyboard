package com.example.carkeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CustomCarNumberKeyboard.InputListener {
    CustomCarNumberKeyboard activity_keyboard;
    RelativeLayout rl_new_power;
    RelativeLayout main_rl_container;
    String defaultCarnum = "冀F00000";
    TextView tv_set_num;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_new_power = findViewById(R.id.rl_new_power);
        activity_keyboard = findViewById(R.id.activity_keyboard);
        tv_set_num = findViewById(R.id.tv_set_num);
        main_rl_container = findViewById(R.id.main_rl_container);
        activity_keyboard.setInputListener(this);

        activity_keyboard.setKeyboardContainerLayout(main_rl_container);
        activity_keyboard.setViewCount(7);
        activity_keyboard.onSetTextColor(R.color.colorAccent);
        tv_set_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_keyboard.setViewContent(defaultCarnum);
            }
        });
        rl_new_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_keyboard.setViewCount(8);
            }
        });
    }

    @Override
    public void inputComplete(String content) {
        Log.e("输入的车牌号是：", content);
    }
}