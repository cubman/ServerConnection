package com.example.android.serverconnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Анатолий on 21.07.2017.
 */
public class ClientConnection extends AsyncTask<Void, Void, Socket> {

    String dstAddress;
    int dstPort;
    String response = "";
    ProgressDialog progressDialog;
    Context mainContext;

    ClientConnection(String addr, int port, Context c) {
        dstAddress = addr;
        dstPort = port;
        mainContext = c;
    }

    @Override
    protected void onPreExecute() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mainContext);
            progressDialog.setMessage("Connecting");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
    }

    @Override
    protected Socket doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);

            socket.connect(new InetSocketAddress(dstAddress, dstPort), 1000);
            socket.setKeepAlive(true);
            Log.d("4444", socket.isConnected() ? "yes" : "no");
        } catch (IOException e) {
            if (socket != null) {

                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();

            }

        }
        Log.d("4444", socket.isConnected() ? "yes" : "no");
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        return  socket;
    }



    @Override
    protected void onPostExecute(Socket result) {
        //textResponse.setText(response);

        Log.d("4444", result.isConnected() ? "yes" : "no");
        super.onPostExecute(result);
    }

}