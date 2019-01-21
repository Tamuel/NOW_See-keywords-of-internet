package com.softart.shift.now;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

/**
 * Created by DongKyu on 2016-06-02.
 */
public class ServerConnector {
    public static ServerConnector instance = null;
    private String dataFromServer;

    private ServerConnector() {

    }

    public static ServerConnector getInstance() {
        if(instance == null)
            instance = new ServerConnector();
        return instance;
    }

    public String getWordDataFromServer(String url) {
        final String u = url;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuffer sb = new StringBuffer();

                try {
                    URL url = new URL(u);
                    HttpURLConnection conn =
                            (HttpURLConnection)url.openConnection();
                    if (conn != null) {
                        conn.setConnectTimeout(2000);
                        conn.setUseCaches(false);
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                            BufferedReader br
                                    = new BufferedReader(new InputStreamReader
                                    (conn.getInputStream(),"utf-8"));
                            while(true) {
                                String line = br.readLine();
                                if (line == null) break;
                                sb.append(line+"\n");
                            }
                            br.close();
                        }
                        conn.disconnect();
                    }

                    dataFromServer = sb.toString();
                    System.out.println(dataFromServer);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작

        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataFromServer;
    }
}
