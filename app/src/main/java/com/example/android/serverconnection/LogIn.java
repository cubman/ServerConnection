package com.example.android.serverconnection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogIn extends AppCompatActivity {
    Intent intent;
    SocketHandler socketHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        socketHandler = new SocketHandler();
        intent = new Intent(this, Main.class);
       //getIntent().putExtra("socket", )
       // startActivity(inent);
        Button button = (Button)findViewById(R.id.SignInButtom);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socketHandler.connected)
                    socketHandler.sendMessage("connect\nadmin\n1234\r\n");
                else
                    socketHandler = new SocketHandler();
            }
        });
    }
}
