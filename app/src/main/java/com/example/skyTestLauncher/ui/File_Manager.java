package com.example.skyTestLauncher.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.SkyTestLauncher.R;
import com.example.skyTestLauncher.SkyTestLauncherApplication;
import com.example.skyTestLauncher.logic.FileManagerAdapter;
import com.example.skyTestLauncher.logic.UsbBroadcastReceiver;
import com.example.skyTestLauncher.model.ExternalFileModel;
import com.example.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class File_Manager extends AppCompatActivity {

    ListView fileLv;     // 文件列表
    private UsbBroadcastReceiver usbReceiver;
    IntentFilter filter;

    List<Map<String, Object>> list;
    HashMap<String, UsbDevice> deviceList;
    FileManagerAdapter adapter;
    Toolbar toolbar;
    MenuItem sortMenuItem;
    MenuItem backMenuItem;
    MenuItem newBuildMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 获取UsbManager服务
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        // 获取所有已连接的USB设备列表
        deviceList = usbManager.getDeviceList();
        //绑定广播接收器
        filter = new IntentFilter();
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addDataScheme("file");

        // 绑定控件
        fileLv = findViewById(R.id.lv_file_manager);
        initFile();
        LogUtil.d("test1", "list11" + list.size());

        if("mounted" == ExternalFileModel.getInstance().getUsbMountStatus()) {
            // 处理U盘挂载的逻辑
            Map<String, Object> mp1 = new HashMap<>();
            mp1.put("filename", "外部存储空间");
            mp1.put("icon", R.drawable.ic_folder);
            list.add(mp1);
            LogUtil.d("test1", "list11" + list.size());
        }else if("unmounted" == ExternalFileModel.getInstance().getUsbMountStatus()){
            // 处理U盘卸载的逻辑
            if(2 == list.size()) {
                list.remove(list.size()-1);
            }
            LogUtil.d("test1", "list22"+list.size());
        }

    }

    private void initFile() {
        inflatelv();
        // 设置列表子项监听器
        setListener();
    }

    private void inflatelv() {
        // 列表对象（元素是哈希表）
        list = new ArrayList<>();
        Map<String, Object> mp = new HashMap<>();
        mp.put("filename", "内部存储空间");
        mp.put("icon", R.drawable.ic_folder);
        list.add(mp);

        // 创建适配器对象
        adapter = new FileManagerAdapter(this, list);
        // 列表设置适配器
        fileLv.setAdapter(adapter);
    }

    private void setListener() {
        // 列表的子项短按点击事件
        // 为fileLv设置点击事件监听器，当列表项被点击时执行相应的操作
        fileLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * AdapterView.OnItemClickListener接口的onItemClick方法，当AdapterView的某一项被点击时调用。
             * 参数说明：
             * - parent: AdapterView<?> - 触发事件的AdapterView对象，例如ListView或GridView。
             * - itemView: View - 用户点击的具体列表项视图对象。
             * - position: int - 被点击的列表项在Adapter中的位置索引，从0开始。
             * - itemId: long - 被点击的列表项的唯一标识符，具体含义取决于Adapter的实现。
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View itemView, int position, long itemId) {
                // 检查是否点击了列表的第一个子项（索引从0开始）
                if (position == 0) {
                    // 创建一个Intent，用于从当前Activity跳转到LocalFile_Manage类的Activity
                    Intent intent = new Intent(SkyTestLauncherApplication.getContext(), LocalFile_Manage.class);
                    // 启动新的Activity
                    startActivity(intent);
                }
                if (position == 1){
                    // 创建一个Intent，用于从当前Activity跳转到LocalFile_Manage类的Activity
                    Intent intent = new Intent(SkyTestLauncherApplication.getContext(), ExternalFile_Manage.class);
                    // 启动新的Activity
                    startActivity(intent);
                }
            }
        });

        // 列表的子项的长按点击事件
        fileLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        sortMenuItem = menu.findItem(R.id.sort);
        backMenuItem = menu.findItem(R.id.back_root);
        newBuildMenuItem = menu.findItem(R.id.newbuilt);

        //在文件管理主页面设置toolbar中item隐藏
        sortMenuItem.setVisible(false);
        backMenuItem.setVisible(false);
        newBuildMenuItem.setVisible(false);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        usbReceiver = new UsbBroadcastReceiver(new UsbBroadcastReceiver.UsbListener() {
            @Override
            public void onUsbStateChanged(boolean connected) {
                if (connected){

                }else{

                }
            }

            @Override
            public void onUsbMountStateChanged(boolean mounted) {
                if (mounted) {
                    // 处理U盘挂载的逻辑
                    Map<String, Object> mp1 = new HashMap<>();
                    mp1.put("filename", "外部存储空间");
                    mp1.put("icon", R.drawable.ic_folder);
                    list.add(mp1);
                    LogUtil.d("test1", "list1"+list.size());
                } else {
                    // 处理U盘卸载的逻辑
                    if(2 == list.size()) {
                        list.remove(list.size()-1);
                    }
                    LogUtil.d("test1", "list2"+list.size());
                }
                adapter.notifyDataSetChanged();
            }

        });

        registerReceiver(usbReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbReceiver);
    }
}
