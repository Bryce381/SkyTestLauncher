package com.example.skyTestLauncher.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.SkyTestLauncher.R;
import com.example.skyTestLauncher.SkyTestLauncherApplication;
import com.example.skyTestLauncher.logic.NetMonitorHelpers;
import com.example.skyTestLauncher.model.DataViewModel;
import com.example.skyTestLauncher.views.LineChartView;
import com.example.skyTestLauncher.views.NetUsageView;
import com.example.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class Net_Monitor extends AppCompatActivity {
    private NetUsageView netUsageView;
    private ImageButton imageButton;
    private Button button;
    private Intent intent;
    private TextView download_speed, upload_speed, ip_address;
    private LineChartView lineChartView1,lineChartView2;

    private DataViewModel dataViewModel;
    private NetMonitorHelpers netMonitorHelpers;

    private static MutableLiveData<String> liveDataUp = new MutableLiveData<>();
    private static MutableLiveData<String> liveDataDown = new MutableLiveData<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_monitor);

        List<Integer> dataList = new ArrayList<>();
        for (int i = 1; i <= 22; i++) {
            dataList.add(i);
        }

        List<String> xAxisList = new ArrayList<>();
        for (int i = 1; i <= 22; i++) {
            xAxisList.add(Integer.toString(i));
        }

        List<Integer> yAxisList = new ArrayList<>();
        for (int i = 0; i <= 40; i+=10) {
            yAxisList.add(i);
        }


        intent = new Intent(Net_Monitor.this, Wifi_Channel_manager.class);
        lineChartView1 = new LineChartView(Net_Monitor.this);
        lineChartView2 = new LineChartView(Net_Monitor.this);


        lineChartView1 = (LineChartView) findViewById(R.id.WaveView1);
        lineChartView2 = (LineChartView) findViewById(R.id.WaveView2);


        netUsageView = (NetUsageView) findViewById(R.id.netUsage);
        netUsageView.setCurrentNumAnim(500);

        netMonitorHelpers = new NetMonitorHelpers();
        ip_address = (TextView) findViewById(R.id.ip_address);
        String ip = netMonitorHelpers.getIPAddress(SkyTestLauncherApplication.getContext());
        ip_address.setText(ip);
        LogUtil.d("test1", ip);


        //监听点击imageButton事件
        imageButton = (ImageButton) findViewById(R.id.imageButton_enter);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.imageButton_enter){
                    startActivity(intent);
                }
            }
        });

        button = (Button) findViewById(R.id.Start_net_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.Start_net_test){
                    lineChartView1.updateData(dataList, xAxisList, yAxisList);
                    lineChartView2.updateData(dataList, xAxisList, yAxisList);
                }
            }
        });

        download_speed = (TextView) findViewById(R.id.downLoad_data);
        upload_speed = (TextView) findViewById(R.id.upLoad_data);

        dataViewModel.getLiveDataUp().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String now_up) {
                LogUtil.d("NetworkMonitorService", "1now_up: " + now_up);
                upload_speed.setText(now_up);
            }
        });

        dataViewModel.getLiveDataDown().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String now_down) {
                LogUtil.d("NetworkMonitorService", "1now_down: " + now_down);
                download_speed.setText(now_down);
            }
        });




    }
    @Override
    protected void onPause() {
        super.onPause();
        if (liveDataUp != null) {
            liveDataUp.removeObservers(Net_Monitor.this);
        }
        if(liveDataDown != null) {
            liveDataDown.removeObservers(Net_Monitor.this);
        }
        liveDataUp = null;
        liveDataDown = null;
    }
}