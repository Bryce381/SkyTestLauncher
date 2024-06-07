package com.example.skyTestLauncher.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.skyTestLauncher.logic.NetworkRepository;


public class NetworkchangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收到网络变化的广播后，执行检测网络状态
        NetworkRepository.getInstance().checkNetworkStatus(context);
    }
}
