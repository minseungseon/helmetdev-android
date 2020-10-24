package com.minseungseon.helmetrev;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LineChart mChart;
    private int mFillColor = Color.argb(150,51,181,229);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = (LineChart) findViewById(R.id.mchart);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setGridBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(true);

        mChart.setDragEnabled(true);
        mChart.setDrawGridBackground(true);
        mChart.setScaleEnabled(false);
        mChart.setDrawBorders(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);

        Legend l = mChart.getLegend();
        l.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(300);
        leftAxis.setAxisMinimum(50);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawGridLines(false);
        setData(100,50);


    }

    private void setData(int count, float range) {
        ArrayList<Entry> yVals1 = new ArrayList<>();

        for(int i=0; i<count; i++){
            float val = (float) (Math.random()*range) +100;
            yVals1.add(new Entry(i, val));
        }

        ArrayList<Entry> yVals2 = new ArrayList<>();

        for(int i=0; i<count; i++){
            float val = (float) (Math.random()*range) +150;
            yVals2.add(new Entry(i, val));
        }

        LineDataSet set1, set2;

        set1 = new LineDataSet((yVals1), "데이터 세트 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.BLUE);
        set1.setDrawCircles(false);
        set1.setLineWidth(3f);
        set1.setFillAlpha(255);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.WHITE);

        set2 = new LineDataSet((yVals2), "데이터 세트 2");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(Color.RED);
        set2.setDrawCircles(false);
        set2.setLineWidth(3f);
        set2.setFillAlpha(255);
        set2.setDrawFilled(true);
        set2.setFillColor(Color.WHITE);

        LineData data = new LineData(set2, set1);
        data.setDrawValues(false);

        mChart.setData(data);

    }
}
