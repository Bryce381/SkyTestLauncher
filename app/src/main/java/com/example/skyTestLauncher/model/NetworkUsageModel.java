package com.example.skyTestLauncher.model;

// 网络使用情况统计模型
public class NetworkUsageModel {
    private static String appName;
    private static String uploadData;
    private static String downloadData;

    //私有的静态对象
    private static final NetworkUsageModel networking = new NetworkUsageModel();
    //私有的无参构造函数
    private NetworkUsageModel() {
    }
    public NetworkUsageModel(String uploadData, String downloadData) {
        this.uploadData = uploadData;
        this.downloadData = downloadData;
    }
    //公有的无参静态方法
    public static NetworkUsageModel getInstance() {
        return networking;
    }

    // getters and setters
    public static String getUploadData() {
        return uploadData;
    }
    public void setUploadData(String uploadData) {
        this.uploadData = uploadData;
    }
    public static String getDownloadData() {
        return downloadData;
    }
    public void setDownloadData(String downloadData) {
        this.downloadData = downloadData;
    }

    public String getTotalData() {
        return uploadData + downloadData;
    }
    public void setTotalData(String uploadData,String downloadData) {
        this.uploadData = uploadData;
        this.downloadData = downloadData;
    }
}
