package com.example.skyTestLauncher.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.SkyTestLauncher.R;
import com.example.skyTestLauncher.views.BarChartView;
import com.example.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class Wifi_Channel_manager extends AppCompatActivity {
    private BarChartView barChartView;
    private static final int PERMISSIONS_REQUEST_CODE = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_channel);
        //测试控件效果
        testDataShow();

        //检查并请求wifi权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            LogUtil.d("test1", "0000000");
            if (ActivityCompat.checkSelfPermission(Wifi_Channel_manager.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
                LogUtil.d("test1", "1111111");
            } else {
                //以获取权限，获取wifi网络列表
                getWifiNetworks();
                LogUtil.d("test1", "2222222");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSIONS_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //以获取权限，获取wifi网络列表
                getWifiNetworks();
            }
        }

    }

    private void getWifiNetworks() {
        //获取wifi网络列表
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            LogUtil.d("test1", "没有获取到权限");
            return;
        }
        List<ScanResult> scanResults = wifiManager.getScanResults();

        for (ScanResult scanResult : scanResults){
            String ssid = scanResult.SSID;
            LogUtil.d("test1", "3333333 ssid = " + ssid);
        }
    }

    private void testDataShow(){
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