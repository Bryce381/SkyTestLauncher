package com.example.skyTestLauncher.ui;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.SkyTestLauncher.R;
import com.example.skyTestLauncher.model.WifiScanResultsModel;
import com.example.skyTestLauncher.views.BarChartView;
import com.example.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wifi_Channel_manager extends AppCompatActivity {
    private BarChartView barChartView;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private WifiManager wifiManager;
    private IntentFilter scanResultFilter;
    WifiScanResultsModel wifiScanResultsModel;
    List<ScanResult> results = new ArrayList<>();
    List<Integer> testList = new ArrayList<>();


    private Handler scanHandler = new Handler(Looper.getMainLooper());
    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            // 重新安排10秒后的下一次扫描
            scanHandler.postDelayed(this, 5000); // 10000毫秒即10秒
            scanForWifiNetworks();
            //测试控件效果
            testDataShow(testList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_channel);

        wifiScanResultsModel = new WifiScanResultsModel();

        barChartView = new BarChartView(Wifi_Channel_manager.this);
        barChartView = (BarChartView) findViewById(R.id.barChartView);


        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        // Android 12 及以上版本需要在运行时请求ACCESS_FINE_LOCATION权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestFineLocationPermission();
        } else {
            checkAndRequestPermissions();
        }
        //测试控件效果
        testDataShow(testList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 移除可能存在的旧任务，防止重复调度
        scanHandler.removeCallbacksAndMessages(null);
        // 没10s进行一次扫描
        scanHandler.post(scanRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 服务销毁时，取消所有待处理的任务，避免内存泄漏或不必要的操作
        scanHandler.removeCallbacksAndMessages(null);
    }

    private void requestFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            checkAndRequestPermissions();
        }
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            // 在服务创建时立即开始第一次扫描，之后由Runnable自动安排后续扫描
            scanForWifiNetworks();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.CHANGE_WIFI_STATE},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    private void scanForWifiNetworks() {
        List<Integer> channelList = new ArrayList<>();
        if (wifiManager != null) {
            //startScan没有明确的返回值
            wifiManager.startScan();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            results = wifiManager.getScanResults();
            channelList.clear();
            for (ScanResult result : results) {
                // 检查2.4GHz的Wi-Fi
                if (result.frequency >= 2400 && result.frequency <= 2500  ) {
                    Integer channel = calculateWifiChannel(result.frequency);
//                    LogUtil.d("test1","other Connected to: " + result.SSID + ", Channel: " + channel);
                    channelList.add(channel);
                }
            }
            // 统计每个数字出现的次数
            Map<Integer, Integer> counts = countOccurrences(channelList);
            testList = new ArrayList<>(Collections.nCopies(13, 0));
            // 打印每个数字及其出现的次数
            for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
                int temp = entry.getKey() - 1;
                if (temp >= 0 && temp < testList.size()) {
                    testList.set(temp, entry.getValue());
                } else {
                    // 处理边界情况，比如记录错误日志或采取其他补救措施
                    LogUtil.e("scanForWifiNetworks", "Attempted to access out of bounds index: " + temp);
                }
            }
            //遍历testList
            LogUtil.d("test1","testList: " + testList);
        }
    }
    private int calculateWifiChannel(int frequency) {
        // 2.4GHz Wi-Fi信道计算
        return (frequency - 2407) / 5 + 1;
    }

    private static Map<Integer, Integer> countOccurrences(List<Integer> numbers) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Integer number : numbers) {
            counts.put(number, counts.getOrDefault(number, 0) + 1);
        }
        return counts;
    }

    private void testDataShow(List<Integer> testList){
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

        barChartView.updateValueData(testList, xAxisList, yAxisList);
    }
}