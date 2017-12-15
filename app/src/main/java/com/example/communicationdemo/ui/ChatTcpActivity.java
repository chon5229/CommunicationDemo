package com.example.communicationdemo.ui;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.communicationdemo.R;
import com.example.communicationdemo.adapter.ChatAdapter;
import com.example.communicationdemo.entity.ChatEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * TCP通信(模拟聊天室)
 */
public class ChatTcpActivity extends AppCompatActivity {

    private EditText edt;
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private EditText ip_server;
    private ListView lv;
    private EditText creat_user;
    private String my_user;
    private List<ChatEntity> chatList = null;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_tcp);
        initView();
        initAdapter();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ChatEntity chatEntity = (ChatEntity) msg.obj;
                    chatList.add(chatEntity);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initAdapter() {
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(this, chatList);
        lv.setAdapter(adapter);
    }

    /**
     * 连接服务器
     */
    public void connect(View view) {
        if (TextUtils.isEmpty(ip_server.getText().toString())) {
            my_user = creat_user.getText().toString().trim();
            Toast.makeText(ChatTcpActivity.this, "请输入服务器IP", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(my_user)) {
            Toast.makeText(this, "给自己创建一个名字吧", Toast.LENGTH_SHORT).show();
            return;
        }
        //开启线程
        new MyAsyncTask().execute(ip_server.getText().toString());
    }

    /**
     * 一个线程，维护连接（毕竟是网路请求，子线程是必要的）
     */
    private class MyAsyncTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                //建立连接 ，端口号需要一致，前一个是ip地址
                socket = new Socket(params[0], 12345);
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                publishProgress("@success");
                String line;
                while (true) {
                    //睡一秒
                    Log.e("======", "执行了");
                    Thread.sleep(1000);
                    while ((line = reader.readLine()) != null) {
                        publishProgress(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final String... values) {
            if (values[0].equals("@success")) {
                Toast.makeText(ChatTcpActivity.this, "服务器已经连接上了", Toast.LENGTH_SHORT).show();
            } else {
                //解析是一个耗时操作，放到线程里面去
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ChatEntity parseJson = parseJson(values[0]);
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = parseJson;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            super.onProgressUpdate(values);
        }
    }

    private void initView() {
        edt = (EditText) findViewById(R.id.edt);
        ip_server = (EditText) findViewById(R.id.ip_server);
        lv = (ListView) findViewById(R.id.lv);
        //自动底部
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        creat_user = (EditText) findViewById(R.id.creat_user);
    }

    /**
     * 给自己创建个名字，毕竟没有登陆相关的操作（想实现自己可以做）
     *
     * @param view
     */
    public void creat(View view) {
        if (!TextUtils.isEmpty(creat_user.getText().toString())) {
            my_user = creat_user.getText().toString().trim();
        } else {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 发送按钮
     *
     * @param view
     */
    public void send(View view) {

        if (TextUtils.isEmpty(my_user)) {
            Toast.makeText(this, "先给自己创建一个账号吧", Toast.LENGTH_SHORT).show();
            return;
        }
        if (socket == null || reader == null || writer == null) {
            Toast.makeText(this, "请先链接服务器", Toast.LENGTH_SHORT).show();
            return;
        }
        String toString = edt.getText().toString().trim();
        if (TextUtils.isEmpty(toString)) {
            Toast.makeText(this, "不能发送空消息", Toast.LENGTH_SHORT).show();
            return;
        }
        //包装成一个json数据
        String toJson = "{\"name\":\"" + my_user + "\",\"count\":\"" + toString + "\",\"other\":\"1\"}";
        try {
            writer.write(toJson + "\n");
            writer.flush();
            //   tv.append("我说：" + toString + "\n");
            edt.setText("");
            ChatEntity chat = new ChatEntity();
            chat.setName(my_user);
            chat.setCount(toString);
            chat.setOther(0);
            //添加刷新
            chatList.add(chat);
            adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //解析服务器传递过来的数据
    private ChatEntity parseJson(String json) throws JSONException {
        //"{\"name\":\"" + my_user + "\",\"count\":\"" + toString + "\",\"other\":\"1\"}";
        JSONObject object = new JSONObject(json);
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setName(object.getString("name"));
        chatEntity.setCount(object.getString("count"));
        chatEntity.setOther(object.getInt("other"));
        return chatEntity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
