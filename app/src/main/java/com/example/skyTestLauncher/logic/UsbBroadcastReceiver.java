package com.example.skyTestLauncher.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.skyTestLauncher.model.ExternalFileModel;
import com.example.utils.LogUtil;

import java.util.List;

/**
 * USB广播接收器类，用于监听USB设备的连接和断开状态。
 */
public class UsbBroadcastReceiver extends BroadcastReceiver {
    /**
     * USB状态改变监听器接口，用于回调USB连接状态。
     */
    public interface UsbListener {
        /**
         * 当USB状态改变时调用的方法。
         *
         * @param connected 表示USB是否已连接。
         */
        void onUsbStateChanged(boolean connected);

        void onUsbMountStateChanged(boolean mounted);
    }

    // USB状态改变监听器实例
    private UsbListener listener;

    /**
     * 默认构造函数。
     */
    public UsbBroadcastReceiver() {}

    /**
     * 带USB监听器的构造函数。
     *
     * @param listener USB状态改变时的监听器。
     */
public UsbBroadcastReceiver(UsbListener listener) {
    if (listener == null) {
        throw new IllegalArgumentException("UsbListener cannot be null.");
    }
    this.listener = listener;
}


    /**
     * 当接收广播时调用的方法。
     *
     * @param context 上下文对象。
     * @param intent  接收到的广播Intent。
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // 获取广播Intent的动作。
        String action = intent.getAction();
        if (action != null) {
            // 根据不同的动作执行相应的处理。
            switch (action) {
//                case "android.hardware.usb.action.USB_DEVICE_ATTACHED":
//                    LogUtil.d("test1", "USB设备连接");
//                    // USB设备连接时执行的操作。
//                    if (listener != null) {
//                        listener.onUsbStateChanged(true);
//                    }
//                    break;
//                case "android.hardware.usb.action.USB_DEVICE_DETACHED":
//                    // USB设备断开时执行的操作。
//                    LogUtil.d("test1", "USB设备断开");
//                    if (listener != null) {
//                        listener.onUsbStateChanged(false);
//                    }
//                    break;
                case Intent.ACTION_MEDIA_MOUNTED:
                    LogUtil.d("test1", "U盘挂载");
                    // U盘挂载时执行的操作。
                    if (listener != null) {
                        listener.onUsbMountStateChanged(true);
                    }
                    if (intent.getData() != null) {
                        String path = intent.getData().getPath();
                        ExternalFileModel.getInstance().setExternalFilePath(path);
                        LogUtil.d("test1", "path"+path);
                    }

                    break;
                case Intent.ACTION_MEDIA_UNMOUNTED:
                case Intent.ACTION_MEDIA_REMOVED:
                    LogUtil.d("test1", "U盘卸载或移除");
                    // U盘卸载或移除时执行的操作。
                    if (listener != null) {
                        listener.onUsbMountStateChanged(false);
                    }
                    break;
            }
        }
    }
}
