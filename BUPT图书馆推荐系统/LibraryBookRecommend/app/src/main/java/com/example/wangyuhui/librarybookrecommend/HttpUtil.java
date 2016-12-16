package com.example.wangyuhui.librarybookrecommend;


import android.content.Context;
import android.content.Intent;

import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by wangyuhui on 16/12/14.
 */
public class HttpUtil {
    public static HttpURLConnection conn;
    public static final String BASE_URL = "http://192.168.199.243:8080/webDemo/qt/";
    public static boolean serverState = true;
    public static String postRequest(final String m_url, final String requestString, final Context context)
            throws Exception {
        URL url = new URL(BASE_URL + m_url);
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(3000 /* milliseconds */);
        conn.setConnectTimeout(3000 /* milliseconds */);
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
        conn.setRequestProperty("Charset", "UTF-8");
        boolean flag = false;
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    OutputStream outputStream = conn.getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject("|"+requestString);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    int response = conn.getResponseCode();
                    System.out.println("response"+response);
                    if (response == 200) {
                        InputStream inputStream = conn.getInputStream();
                        return convertStreamToString(inputStream);
                    }
                    return  "something error";
                }
                catch (Exception e1)
                {
                    System.out.println("*****************链接超时*******************");
                    serverState = false;
                }
                return "";
            }
        });
        new Thread(task).start();
        return  task.get();
    }

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();

        String line = null;

        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line + "/n");

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                is.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        return sb.toString().replaceAll("/n","\n");
    }


}
