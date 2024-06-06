package com.example.skyTestLauncher.model;

public class ExternalFileModel {

    private static final ExternalFileModel instance = new ExternalFileModel() ;

    private  String externalFilePath;

    private ExternalFileModel(){}

    public static ExternalFileModel getInstance()
    {
        return instance;
    }

    public String getExternalFilePath() {
        return externalFilePath;
    }

    public void setExternalFilePath(String externalFilePath) {
        this.externalFilePath = externalFilePath;
    }
}
