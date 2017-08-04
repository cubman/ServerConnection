package com.example.android.serverconnection;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    com.example.android.serverconnection.Fragments.send_and_get saf;
    Button b_con, b_send;
    String message = "";
    Socket socket;
    TextView textView;
    ProgressBar pb;
    Handler handler;
    Thread cThread;

    volatile boolean connected = false, need_send = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView)findViewById(R.id.result);

        if (savedInstanceState != null) {
            connected = savedInstanceState.getBoolean("connected");
            need_send = savedInstanceState.getBoolean("needSend");
        }


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
        pb = (ProgressBar)findViewById(R.id.progressBar2);

      /*  b_con.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText mess = (EditText) (findViewById(R.id.addr));
                TextView res = (TextView) findViewById(R.id.result);

                if (!connected) {

                    if (cThread != null) {
                        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
                        Future longRunningTaskFuture = threadPoolExecutor.submit(cThread);
                        longRunningTaskFuture.cancel(true);
                    }
                        cThread = new Thread(new ClientThread());
                        cThread.start();
                }

                }
    });
        b_send = (Button)findViewById(R.id.send);
        b_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (connected)
                    need_send = true;
            }
        });
*/
        handler = new Handler();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public class ClientListen2 implements Runnable {
        @Override
        public void run() {
            textView.setText(message);
            message = "";
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("connected", connected);
        outState.putBoolean("needSend", need_send);


        super.onSaveInstanceState(outState);
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
