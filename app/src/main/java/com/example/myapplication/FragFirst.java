package com.example.myapplication;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FragFirst extends Fragment{
    private View view;
    Socket s;
    TextView sensor1;
    TextView sensor2;
    Button btn;
    BufferedReader reader;
    PrintWriter writer;

    public static FragFirst newInstance()
    {
        FragFirst fragFirst = new FragFirst();

        return fragFirst;
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.frag_first, container, false);
        btn = (Button)myView.findViewById(R.id.button);
        sensor1 = (EditText)myView.findViewById(R.id.editTextTextPersonName);
        sensor2 = (EditText)myView.findViewById(R.id.editTextTextPersonName2);

        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                ConnectThread thread = new ConnectThread();
                thread.start();
            }
        });

        return view;
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (s.isConnected()) {
                            Toast.makeText(getActivity(), "연결됨", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "연결안됨", Toast.LENGTH_LONG).show();
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

                    getActivity().runOnUiThread(new Runnable() {
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
                    Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

}
