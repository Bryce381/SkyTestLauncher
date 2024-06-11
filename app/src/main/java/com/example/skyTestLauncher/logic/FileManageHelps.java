package com.example.skyTestLauncher.logic;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileManageHelps {


    public String getFolderSubItems(File currentFile) {
        String lengthStr = "0";

        lengthStr = "" + currentFile.listFiles().length + "项";

        return lengthStr;
    }

    public String getFileCount(File currentFile) {
        String fileSize = "0";

        if (currentFile.length() == 0) {
            fileSize = "" + "0 KB";
        } else if (currentFile.length() > 1024) {
            fileSize = "" + currentFile.length() / 1024 + "KB";
        } else if (currentFile.length() > 1024 * 1024) {
            fileSize = "" + currentFile.length() / 1024 / 1024 + "MB";
        } else if (currentFile.length() > 1024 * 1024 * 1024) {
            fileSize = "" + currentFile.length() / 1024 / 1024 / 1024 + "GB";
        }

        return fileSize;
    }

    public String getFileTime(File currentFile) {
        long lastModifiedTime = currentFile.lastModified();
        Date date = new Date(lastModifiedTime);
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        return formattedDate;
    }

}
