package com.example.communicationdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.communicationdemo.R;

/**
 * TCP通信(模拟聊天室)
 */
public class ChatTcpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_tcp);
    }
}
