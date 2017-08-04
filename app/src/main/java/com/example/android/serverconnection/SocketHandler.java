package com.example.android.serverconnection;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Анатолий on 25.07.2017.
 */

public class SocketHandler extends Fragment {
    volatile boolean need_send = false, connected = false;
    volatile String message = "";
    Socket socket;
    Handler handler;

    public SocketHandler() {
        Thread thread = new Thread(new ClientThread());
        thread.start();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void sendMessage(String message) {
        this.message = message;
        need_send = true;
    }

    public class ClientListen implements Runnable {
        @Override
        public void run() {
            BufferedReader reader = null;
            try {

                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Log.d("ClientActivity", reader == null ? "e" : "w");
                Log.d("ClientActivity", socket.getInputStream() == null ? "e" : "w");


                int charsRead = 0;
                char[] buffer = new char[1024];
                String mid_message = "";
                while ((charsRead = reader.read(buffer)) != -1 && connected) {
                    if (charsRead > 0) {
                        mid_message += new String(buffer).substring(0, charsRead);
                        Log.d("ClientActivity11", mid_message);

                    }
                    if (mid_message.contains("\r\n")) {
                        Log.d("ClientActivity22", mid_message);
                        //handler.post(Main.);
                        };
                    }

            }
            catch (IOException e1) {
                e1.printStackTrace();
            }

            catch (Exception e) {
                Log.d("ClientActivity", "err");


            } finally {

                Log.d("ClientActivity", "FINALLY");
               /* handler.post(new Runnable() {
                    public void run() {
                        View parentLayout = findViewById(R.id.content);
                        Snackbar.make(parentLayout, "Connection lost", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });*/

                // Snackbar.make(findViewById(android.R.id.content), "Lost connection with server", Snackbar.LENGTH_LONG).show();


                connected = false;
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }

        }


    }


    public class ClientThread implements Runnable {
        public void run() {
           /* handler.post(new Runnable() {
                public void run() {
                    pb.setVisibility(View.VISIBLE);

                }
            });*/
            try {
                InetAddress serverAddr = InetAddress.getByName("192.168.1.212");
                Log.d("ClientActivity", "C: Connecting...");
                socket = new Socket(serverAddr,28563);
               // EditText editTextMessage = (EditText) findViewById(R.id.message);

                connected = true;

                Thread clientListen = new Thread(new ClientListen());

                clientListen.start();



                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                        .getOutputStream())), true);
                Log.d("ClientActivity", "C: Connected");
               /* handler.post(new Runnable() {
                    public void run() {
                        pb.setVisibility(View.GONE);
                    }
                });*/
                while (connected) {
                    try {
                        if (need_send) {
                            need_send = false;
                            if (!socket.isBound())
                                break;
                            Log.d("ClientActivity", "C: Sending command.");
                            out.println(message + "\r");

                            Log.d("ClientActivity", "C: Sent.");

                        }
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                clientListen.interrupt();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
            Thread.currentThread().interrupt();
        }
    }
}