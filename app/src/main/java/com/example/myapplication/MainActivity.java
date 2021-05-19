package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    Socket s;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter adapter;
    static BufferedReader reader;
    static PrintWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager2);
        ConnectThread ct = new ConnectThread();
        ct.start();
        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("측정값"));
        tabLayout.addTab(tabLayout.newTab().setText("창문"));
        tabLayout.addTab(tabLayout.newTab().setText("블라인드"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("MyTag", Integer.toString(tab.getPosition()));

                viewPager2.setCurrentItem((tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }
    class ConnectThread extends Thread
    {
        public void run()
        {
            Log.d("MyTag", "스레드 실행");
            while(true)
            {
                try {
                    s = new Socket("113.198.234.48", 32000);
                    Log.d("MyTag", "연결");
                    reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    writer = new PrintWriter(s.getOutputStream(), true);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (s.isConnected()) {
                                Toast.makeText(getApplicationContext(),"연결됨", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(),"연결안됨" ,Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    if(s.isConnected())
                    {
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}