package com.example.skyTestLauncher.logic;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import com.example.utils.LogUtil;


public class NetworkRepository {
    private static final String TAG = "DEBUG";

    private static NetworkRepository instance = new NetworkRepository();

    private NetworkRepository() {
    }
    public static NetworkRepository getInstance() {
        if (instance == null) {
            instance = new NetworkRepository();
        }
        return instance;
    }

    public void checkNetworkStatus(Context context) {
        LogUtil.d(TAG, "NetworkStatusModel");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork != null) {
                LogUtil.d(TAG, "NetworkStatusModel111");
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                // 检查网络是否可用
                boolean isConnected = networkCapabilities != null
                        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                if (isConnected) {
                    // 网络连接可用，可以进行网络操作
                    Toast.makeText(context, "Network available,"+getNetworkType(context), Toast.LENGTH_SHORT).show();
                } else {
                    // 网络连接不可用或无网络
                    Toast.makeText(context, "Network unavailable", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 无网络连接
            }
        } else {
            // 对于Android API 28及以下版本，使用旧的方法
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // 网络连接可用
            } else {
                // 网络连接不可用或无网络
            }
        }
    }

    private String getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return "Mobile data";
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return "WiFi";
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                        return "BLUETOOTH";
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return "ETHERNET";
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        return "VPN";
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                        return "WIFI_AWARE";
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                        return "LowPAN";
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_USB)) {
                        return "USB";
                    }
                }
            }
        }
        return "Unknown";
    }


}
