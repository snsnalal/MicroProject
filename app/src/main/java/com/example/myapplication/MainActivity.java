package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    Socket s;
    TextView sensor1;
    TextView sensor2;
    Button btn;
    BufferedReader reader;
    PrintWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensor1 = (TextView)findViewById(R.id.textView);
        sensor2 = (TextView)findViewById(R.id.textView2);
        btn = (Button)findViewById(R.id.button);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                ConnectThread thread = new ConnectThread();
                thread.start();
            }
        });

    }
    class ConnectThread extends Thread
    {
        public void run()
        {
            Log.d("MyTag", "스레드 실행");
            try {
                s = new Socket("113.198.234.48", 32000);
                Log.d("MyTag", "연결");
                reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                writer = new PrintWriter(s.getOutputStream(), true);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(s.isConnected())
                        {
                            Toast.makeText(getApplicationContext(), "연결됨", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "연결안됨", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                ReceiveThread rt = new ReceiveThread();
                rt.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    class ReceiveThread extends Thread
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    String data = reader.readLine();
                    String data2 = reader.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(data.equals("0"))
                            {
                                sensor1.setText(data2);
                                Log.d("MyTag", data);
                                Log.d("MyTag", data2);

                            }
                            else
                            {
                                sensor2.setText(data2);
                                Log.d("MyTag", data);
                                Log.d("MyTag", data2);
                            }
                        }
                    });
                }
                catch (IOException e)
                {
                    Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT);
                }
            }

        }

    }
}