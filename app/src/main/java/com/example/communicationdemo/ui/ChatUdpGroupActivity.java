package com.example.communicationdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.communicationdemo.R;

/**
 * 基于Udp的组播（聊天室）实现
 */
public class ChatUdpGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_udp_group);
    }
}
