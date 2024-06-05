package com.example.skyTestLauncher.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.SkyTestLauncher.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExternalFile_Manage extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_file_manage);
//
////        listView = findViewById(R.id.listView);
////        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList);
////        listView.setAdapter(adapter);
//
//        // 检查并请求权限（对于Android 6.0以上）
//        requestPermissions();
//
//        // 注册广播接收器监听USB设备连接
//        registerReceiver(usbReceiver, new IntentFilter(Intent.ACTION_MEDIA_MOUNTED));
//        registerReceiver(usbReceiver, new IntentFilter(Intent.ACTION_MEDIA_UNMOUNTED));
//    }
//
//    private void requestPermissions() {
//        // 这里应加入运行时权限请求逻辑，具体根据Android版本和需求调整
//    }
//
//    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
//                // U盘已连接
//                String path = intent.getData().getPath();
//                Log.d("ExternalFile_Manage", "USB Mounted: " + path);
//                loadFilesFromUSB(path);
//            } else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
//                // U盘已断开
//                Log.d("ExternalFile_Manage", "USB Unmounted");
//            }
//        }
//    };
//
//    private void loadFilesFromUSB(String path) {
//        File dir = new File(path);
//        if (dir.exists() && dir.isDirectory()) {
//            fileList.clear();
//            File[] files = dir.listFiles();
//            if (files != null) {
//                for (File file : files) {
//                    fileList.add(file.getName());
//                }
//            }
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(usbReceiver);
    }
}
