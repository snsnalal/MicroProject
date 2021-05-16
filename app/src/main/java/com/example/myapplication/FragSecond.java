package com.example.myapplication;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragSecond extends Fragment{
    private View view;
    Button btn_up, btn_down;
    Switch swt;
    SendThread st;

    public static FragSecond newInstance()
    {
        FragSecond fragSecond = new FragSecond();

        return fragSecond;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_second, container, false);

        btn_up = (Button)view.findViewById(R.id.button2);
        btn_down = (Button)view.findViewById(R.id.button3);
        swt = (Switch)view.findViewById(R.id.switch1);

        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(!st.isAlive())
                    {
                        st = new SendThread(2);
                        st.start();
                    }
                }
                else
                {
                    if(!st.isAlive())
                    {
                        btn_up.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                st = new SendThread(0);
                                st.start();
                            }
                        });
                        btn_down.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                st = new SendThread(1);
                                st.start();
                            }
                        });
                    }
                }
            }
        });

        return view;
    }
    class SendThread extends Thread
    {
        int a;
        public SendThread(int a)
        {
            this.a = a;
        }

        public void run()
        {
            if(a == 0)
            {
                MainActivity.writer.println("0 0");
            }
            else if(a == 1)
            {
                MainActivity.writer.println("0 1");
            }
            else if(a == 2)
            {
                MainActivity.writer.println("0 2");
            }
        }
    }
}
