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

import static com.example.myapplication.MainActivity.reader;

public class FragFirst extends Fragment{
    private View view;
    TextView sensor1;
    TextView sensor2;
    Button btn;

    public static FragFirst newInstance()
    {
        FragFirst fragFirst = new FragFirst();

        return fragFirst;
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_first, container, false);

        btn = (Button)view.findViewById(R.id.button);
        sensor1 = (EditText)view.findViewById(R.id.editTextTextPersonName);
        sensor2 = (EditText)view.findViewById(R.id.editTextTextPersonName2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveThread rt = new ReceiveThread();
                rt.start();
            }
        });
        return view;
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
                            else if(data.equals("1"))
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
