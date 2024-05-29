package com.example.skyTestLauncher.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.SkyTestLauncher.R;
import com.example.skyTestLauncher.views.BarChartView;

import java.util.ArrayList;
import java.util.List;

public class Wifi_Channel_manager extends AppCompatActivity {
    private BarChartView barChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_channel);


        List<Integer> dataList = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            dataList.add(i);
        }

        List<String> xAxisList = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            xAxisList.add(Integer.toString(i));
        }

        List<Integer> yAxisList = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            yAxisList.add(i*4);
        }

        barChartView = new BarChartView(Wifi_Channel_manager.this);
        barChartView = (BarChartView) findViewById(R.id.barChartView);

        barChartView.updateValueData(dataList, xAxisList, yAxisList);

    }
}