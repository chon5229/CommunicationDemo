package com.example.communicationdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.communicationdemo.ui.ChatTcpActivity;
import com.example.communicationdemo.ui.ChatUdpActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tcp:
                openActivity(this, ChatTcpActivity.class, null);
                break;
            case R.id.udp:
                openActivity(this, ChatUdpActivity.class, null);
                break;

        }
    }

    /**
     * 打开acticyty
     *
     * @param con
     * @param cls
     * @param bundle
     */
    public void openActivity(Context con, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(con, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        con.startActivity(intent);
    }
}
