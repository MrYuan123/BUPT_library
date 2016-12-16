package com.example.wangyuhui.librarybookrecommend;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    TextView show;
    TextView studentNumber;
    Button search;
    Button clear;
    MyHandler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HttpUtil.serverState = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (TextView)findViewById(R.id.showMsg);
        myHandler = new MyHandler();
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true) {
                    if(HttpUtil.serverState == false) {
                        Message message = new Message();
                        message.what = 1;
                        myHandler.sendMessage(message);
                        break;
                    }
                }
            }
        });
        thread.start();
        show.setMovementMethod(ScrollingMovementMethod.getInstance());
        clear = (Button)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentNumber.setText("");
                System.out.println("点击成功");
                try {
                    show.setText(HttpUtil.postRequest("library","-----",MainActivity.this));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        search = (Button)findViewById(R.id.search);
        studentNumber = (TextView) findViewById(R.id.studentNumberLine);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(studentNumber.getText().toString().equals(""))
                        Toast.makeText(MainActivity.this,"请输入学生学号",Toast.LENGTH_LONG).show();
                    else
                    show.setText(HttpUtil.postRequest("library",studentNumber.getText().toString(),MainActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            show.setText(HttpUtil.postRequest("library","-----",this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {

            switch(msg.what)
            {
                case 1:
                    Toast.makeText(MainActivity.this,"服务器未找到,请联系作者188113736366",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}

