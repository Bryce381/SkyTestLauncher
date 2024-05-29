package com.example.skyTestLauncher.model;

public class NetworkStatusModel {
    private static boolean isConnected;
    private static String mConnectionType;


    public NetworkStatusModel(boolean isConnected, String connectionType) {
        this.isConnected = isConnected;
        this.mConnectionType = connectionType;
    }

    // getters and setters
    public static boolean isConnected() {
        return isConnected;
    }
    public static void setConnected(boolean connected) {
        isConnected = connected;
    }
    public static String getConnectionType() {
        return mConnectionType;
    }
    public static void setConnectionType(String connectionType) {
        mConnectionType = connectionType;
    }
}

