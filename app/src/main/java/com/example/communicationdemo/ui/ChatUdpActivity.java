package com.example.communicationdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.communicationdemo.R;
import com.example.communicationdemo.utils.UdpHelper;

import java.net.InetSocketAddress;

public class ChatUdpActivity extends AppCompatActivity {

    private EditText edt;
    private UdpHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_udp);
        initView();
        helper = new UdpHelper();
        new Thread(helper).start();
    }

    private void initView() {
        edt = (EditText) findViewById(R.id.edt);

    }

    public void send(View view) {
        final String trim = edt.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                helper.send(trim);
            }
        }).start();

    }
}
