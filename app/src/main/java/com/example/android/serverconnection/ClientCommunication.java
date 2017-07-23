package com.example.android.serverconnection;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Анатолий on 21.07.2017.
 */
public class ClientCommunication extends AsyncTask<String, Void, String> {

    String dstAddress;
    Socket s;
    TextView textView;

    ClientCommunication(Socket sc) {
        s = sc;
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... arg0) {
        BufferedWriter out;
        BufferedReader in;

        if (!s.isConnected()) {
            return null;
        }
        try {
            out =
                    new BufferedWriter(new OutputStreamWriter((s.getOutputStream())));

            in =
                new BufferedReader(
                        new InputStreamReader(s.getInputStream()));

            out.write("(1,2,3)123463121");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        //textResponse.setText(response);

        Log.d("4444", "00");
        super.onPostExecute(result);
    }

}
