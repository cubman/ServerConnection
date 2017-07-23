package com.example.android.serverconnection;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.serverconnection.Fragments.send_and_get;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    com.example.android.serverconnection.Fragments.send_and_get saf;
    Button b_con, b_send;

    Socket socket;
    Runnable listen = new ClientListen();
     TextView textView;

    volatile boolean connected = false, need_send = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView)findViewById(R.id.result);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connected = false;
                Snackbar.make(view, "Connect was breaked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        saf = new send_and_get();
        b_con = (Button)findViewById(R.id.connect);
        b_con.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText mess = (EditText) (findViewById(R.id.addr));
                TextView res = (TextView) findViewById(R.id.result);


               /* ClientConnection myClient = new ClientConnection("77.66.231.96", 28562, Main.this);
                //ClientConnection myClient = new ClientConnection("192.168.1.236", 28563, socket);
                Log.d("1", "4");
                myClient.execute();


                try {
                    socket = myClient.get();
                    Log.d("2222", myClient.get().isConnected() ? "yes" : "no");
                    Log.d("4444", socket.isConnected() ? "yes" : "no");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (socket != null)
                        was_connected(true);
                    else
                        was_connected(false);*/

                if (!connected) {
                   // serverIpAddress = serverIp.getText().toString();
                   // if (!serverIpAddress.equals("")) {
                        Thread cThread = new Thread(new ClientThread());
                        textView.post(listen);
                        cThread.start();

                }

                }
    });
        b_send = (Button)findViewById(R.id.send);
        b_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
              /*  EditText mess = (EditText) (findViewById(R.id.addr));
                TextView res = (TextView) findViewById(R.id.result);


                ClientCommunication ClienMes = new ClientCommunication(socket);
                //ClientConnection myClient = new ClientConnection("192.168.1.236", 28563, socket);
                ClienMes.execute();
                Log.d("1", "4");

                try {
                    String sf = ClienMes.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (socket != null)
                    was_connected(true);
                else
                    was_connected(false);*/
              need_send = true;
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public class ClientListen implements Runnable {

        @Override
        public void run() {
            //TextView textView = (TextView)getWindow().getDecorView().getRootView().findViewById(R.id.result);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d("ClientActivity", reader == null ? "e" : "w");
                Log.d("ClientActivity", socket.getInputStream() == null ? "e" : "w");
                String res = "";

                String message = "";
                int charsRead = 0;
                char[] buffer = new char[1024];

                while ((charsRead = reader.read(buffer)) != -1 && connected) {
                    if (charsRead > 0)
                        message += new String(buffer).substring(0, charsRead);
                    if (message.contains("\r\n"))
                    {
                        textView.setText(res);
                        res = "";
                    }
                }


                connected = false;
                reader.close();



            }
            catch (Exception e) {
                Log.d("ClientActivity", "Get exception");
            }
        }


    }


    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(((EditText) findViewById(R.id.addr)).getText().toString());
                Log.d("ClientActivity", "C: Connecting...");
                socket = new Socket(serverAddr, Integer.parseInt(((EditText) findViewById(R.id.port)).getText().toString()));
                EditText editTextMessage = (EditText) findViewById(R.id.message);

                connected = true;
                Thread clientListen = new Thread(listen);
                clientListen.start();
                //Snackbar.make( button, "Replace with your own action", Snackbar.LENGTH_LONG)
                //               .setAction("Action", null).show();


                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                        .getOutputStream())), true);
                Log.d("ClientActivity", "C: Connected");
                while (connected) {
                    try {
                        if (need_send) {
                            need_send = false;
                            if (!socket.isBound())
                                break;
                            Log.d("ClientActivity", "C: Sending command.");
                            out.println(editTextMessage.getText().toString() + "\r");

                            Log.d("ClientActivity", "C: Sent.");

                        }
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }

    private void was_connected(boolean b) {
        if (b) {
            findViewById(R.id.connectionLayer).setVisibility(View.GONE);
            findViewById(R.id.connect).setVisibility(View.GONE);
        } else {
            findViewById(R.id.connectionLayer).setVisibility(View.VISIBLE);
            findViewById(R.id.connect).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (id == R.id.nav_camera) {
           // ft.replace(R.id.content, saf);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void send(View view) {

    }
}
