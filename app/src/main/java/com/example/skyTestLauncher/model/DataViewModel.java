package com.example.skyTestLauncher.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {
    private static MutableLiveData<String> liveDataUp = new MutableLiveData<>();
    private static MutableLiveData<String> liveDataDown = new MutableLiveData<>();


    public static LiveData<String> getLiveDataUp() {
        return liveDataUp;
    }
    public static LiveData<String> getLiveDataDown() {
        return liveDataDown;
    }

    public static void refreshData(String up, String down) {
        String now_up = up;
        String now_down = down;

//        LogUtil.d("test1", "now_up: " + now_up + " now_down: " + now_down);

        liveDataUp.setValue(now_up);
        liveDataDown.setValue(now_down);
    }
}
