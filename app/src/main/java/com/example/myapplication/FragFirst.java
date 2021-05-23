package com.example.myapplication;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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
    ReceiveThread rt;
    private LineChart chart;

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

        chart = (LineChart) view.findViewById(R.id.LineChart);

        chart.setDrawGridBackground(true);
        chart.setBackgroundColor(Color.BLACK);
        chart.setGridBackgroundColor(Color.BLACK);

        chart.getDescription().setEnabled(true);
        Description des = chart.getDescription();
        des.setEnabled(true);
        des.setText("Real-Time DATA");
        des.setTextSize(15f);
        des.setTextColor(Color.WHITE);

        // touch gestures (false-비활성화)
        chart.setTouchEnabled(false);

// scaling and dragging (false-비활성화)
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

//auto scale
        chart.setAutoScaleMinMaxEnabled(true);

// if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

//X축
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawAxisLine(false);

        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setDrawGridLines(false);

//Legend
        Legend l = chart.getLegend();
        l.setEnabled(true);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setTextSize(12f);
        l.setTextColor(Color.WHITE);

//Y축
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(getResources().getColor(R.color.purple_200));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.black));

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);


// don't forget to refresh the drawing
        chart.invalidate();

        btn = (Button)view.findViewById(R.id.button);
        sensor1 = (EditText)view.findViewById(R.id.editTextTextPersonName);
        sensor2 = (EditText)view.findViewById(R.id.editTextTextPersonName2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rt = new ReceiveThread();
                rt.start();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rt.interrupt();
    }

    class ReceiveThread extends Thread
    {
        public void run()
        {
            while (true)
            {
                if (Thread.interrupted())
                {
                    break;
                }
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
                            addEntry(Double.parseDouble(data2));
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
    private void addEntry(double num) {

        LineData data = chart.getData();

        if (data == null) {
            data = new LineData();
            chart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }



        data.addEntry(new Entry((float)set.getEntryCount(), (float)num), 0);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        chart.setVisibleXRangeMaximum(150);
        // this automatically refreshes the chart (calls invalidate())
        chart.moveViewTo(data.getEntryCount(), 50f, YAxis.AxisDependency.LEFT);

    }

    private LineDataSet createSet() {



        LineDataSet set = new LineDataSet(null, "Real-time Line Data");
        set.setLineWidth(1f);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.white));
        set.setColor(getResources().getColor(R.color.white));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(Color.rgb(190, 190, 190));

        return set;
    }

}
