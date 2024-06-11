package com.example.skyTestLauncher.logic;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.skyTestLauncher.model.ExternalFileModel;

public class UsbMountMonitorService extends Service {

    private UsbBroadcastReceiver usbMountReceiver;
    IntentFilter usbMountFilter;

    /**
     * UsbMountMonitorService的构造函数。
     */
    public UsbMountMonitorService() {
        // 在这里进行构造函数的实现，可能会包括一些初始化逻辑
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //绑定广播接收器
        usbMountFilter = new IntentFilter();
        usbMountFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        usbMountFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        usbMountFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        usbMountFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        usbMountFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        usbMountFilter.addDataScheme("file");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        usbMountReceiver = new UsbBroadcastReceiver(new UsbBroadcastReceiver.UsbListener() {
            @Override
            public void onUsbStateChanged(boolean mounted) {

            }

            @Override
            public void onUsbMountStateChanged(boolean mounted) {
                if (mounted) {
                    ExternalFileModel.getInstance().setUsbMountStatus("mounted");
                }else{
                    ExternalFileModel.getInstance().setUsbMountStatus("unmounted");
                }
            }
        });
        registerReceiver(usbMountReceiver, usbMountFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbMountReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}