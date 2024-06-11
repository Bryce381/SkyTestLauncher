package com.example.skyTestLauncher.logic;

import com.example.utils.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

    private String getFileType(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        if (lastIndex != -1 && lastIndex < name.length() - 1) {
            String extension = name.substring(lastIndex + 1).toLowerCase();
            switch (extension) {
                case "ts":
                    return "ts file";
                default:
                    return "Unknown File Type";
            }
        }
        return "Unknown File Type";
    }


    public static File[] orderByName(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        List fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
        // 将排序后的List转换回File数组
        files = (File[]) fileList.toArray(new File[fileList.size()]);
        return files;
    }

    public static File[] orderByDate(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                return diff > 0 ? 1 : (diff == 0 ? 0 : -1);
            }
        });
        return files;
    }

    public static File[] orderByLength(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.length() - f2.length();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
            }

            public boolean equals(Object obj) {
                return true;
            }
        });

        files = (File[]) fileList.toArray(new File[fileList.size()]);
        return files;
    }

}
