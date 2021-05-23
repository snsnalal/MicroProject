package com.example.myapplication;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.myapplication.MainActivity.writer;

public class FragThird extends Fragment{
    private View view;
    Button btn_up, btn_down;
    Switch swt;
    SendThread st;

    public static FragThird newInstance()
    {
        FragThird fragThird = new FragThird();

        return fragThird;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_third, container, false);

        btn_up = (Button)view.findViewById(R.id.button2);
        btn_down = (Button)view.findViewById(R.id.button3);
        swt = (Switch)view.findViewById(R.id.switch1);
        swt.setChecked(false);

        btn_up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                st = new SendThread(0);
                st.start();
            }
        });

        btn_up.setOnTouchListener(new RepeatListener(1000, 1000, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st = new SendThread(0);
                st.start();
            }
        }));

        btn_down.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                st = new SendThread(1);
                st.start();
            }
        });

        btn_down.setOnTouchListener(new RepeatListener(1000, 1000, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st = new SendThread(1);
                st.start();
            }
        }));

        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(swt.isChecked())
                {
                    st = new SendThread(2);
                    st.start();
                    btn_up.setEnabled(!isChecked);
                    btn_down.setEnabled(!isChecked);

                }else
                {
                    btn_up.setEnabled(!isChecked);
                    btn_down.setEnabled(!isChecked);
                }
            }

        });

        return view;
    }
    public void onDestroy() {
        super.onDestroy();
        st.interrupt();
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
                writer.println("1 0");
            }
            else if(a == 1)
            {
                writer.println("1 1");
            }
            else if(a == 2)
            {
                writer.println("1 2");
            }
            Log.d("MyTag", String.valueOf(a));
        }
    }
    public class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler();

        private int initialInterval;
        private final int normalInterval;
        private final View.OnClickListener clickListener;
        private View touchedView;

        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (touchedView.isEnabled()) {
                    handler.postDelayed(this, normalInterval);
                    clickListener.onClick(touchedView);
                } else {
                    // if the view was disabled by the clickListener, remove the callback
                    handler.removeCallbacks(handlerRunnable);
                    touchedView.setPressed(false);
                    touchedView = null;
                }
            }
        };

        /**
         * @param initialInterval The interval after first click event
         * @param normalInterval  The interval after second and subsequent click
         *                        events
         * @param clickListener   The OnClickListener, that will be called
         *                        periodically
         */
        public RepeatListener(int initialInterval, int normalInterval,
                              View.OnClickListener clickListener) {
            if (clickListener == null)
                throw new IllegalArgumentException("null runnable");
            if (initialInterval < 0 || normalInterval < 0)
                throw new IllegalArgumentException("negative interval");

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacks(handlerRunnable);
                    handler.postDelayed(handlerRunnable, initialInterval);
                    touchedView = view;
                    touchedView.setPressed(true);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacks(handlerRunnable);
                    touchedView.setPressed(false);
                    touchedView = null;
                    return true;
            }

            return false;
        }
    }
}
