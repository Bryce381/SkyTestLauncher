package com.example.skyTestLauncher;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.SkyTestLauncher.R;
import com.example.skyTestLauncher.ui.Net_Monitor;
import com.example.skyTestLauncher.ui.File_Manager;
import com.example.skyTestLauncher.ui.NetworkMonitorService;
import com.example.skyTestLauncher.ui.NetworkchangeReceiver;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;//滑动菜单
    private Toolbar toolbar;
    private NavigationView navView;//导航视图
    private IntentFilter intentFilter;
    private NetworkchangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);


        Intent net_intent = new Intent(this, Net_Monitor.class);
        Intent file_intent = new Intent(this, File_Manager.class);


        /*开辟一个工作线程用于启动网络监控service*/
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), NetworkMonitorService.class);
                startService(intent);
            }
        });

        //注册网络状态监听广播
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkchangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

        // 工具栏按钮点击
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        //导航菜单点击
        navView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.net_monitor) {
                //跳转至网络检测页面
                startActivity(net_intent);
            } else if (itemId == R.id.file_manager) {
                //跳转至文件管理页面
                startActivity(file_intent);
            }
            //关闭滑动菜单
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }
}