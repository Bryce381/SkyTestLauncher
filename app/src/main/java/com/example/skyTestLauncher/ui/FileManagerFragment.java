package com.example.skyTestLauncher.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;

import com.example.skyTestLauncher.ui.FilePresenter;
import com.example.utils.LogUtil;

import java.io.File;

/**
 * FileManagerFragment 是一个基于 BrowseSupportFragment 的组件，用于展示文件管理器界面。
 * 它使用 Leanback 框架显示指定目录中的文件和文件夹列表。
 */
public class FileManagerFragment extends BrowseSupportFragment {

    /**
     * 创建并初始化 Fragment 的视图。
     *
     * @param inflater   用于创建视图的 LayoutInflater 对象。
     * @param container  可能为 null 的 ViewGroup，Fragment 的 UI 将被添加到其中。
     * @param savedInstanceState 若 Fragment 正在从先前保存的状态恢复，这是该状态。
     * @return 返回 Fragment 的视图，或 null。
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 初始化 Fragment 的视图组件。
     *
     * @param view       在 onCreateView 中创建的视图。
     * @param savedInstanceState 若 Fragment 正在从先前保存的状态恢复，这是该状态。
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置标题为 "File Manager"
        setTitle("File Manager");
        // 禁用头部区域
        setHeadersState(HEADERS_DISABLED);

        // 创建一个 ListRowPresenter 类型的 ArrayObjectAdapter 用于存储数据
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        // 加载文件和文件夹列表
        File rootDir = new File("/storage/emulated/0/"); // 设置根目录为外部存储的默认路径
        File[] files = rootDir.listFiles(); // 获取根目录下的文件和目录
        if (!rootDir.exists() || !rootDir.canRead()) {
            LogUtil.e("test1", "Root directory does not exist or cannot be read.");
            return;
        }

        // 如果根目录存在文件或目录
        if (files != null) {
            LogUtil.d("test1","11111");
            // 创建一个 FilePresenter 类型的 ArrayObjectAdapter 用于显示文件列表
            ArrayObjectAdapter fileAdapter = new ArrayObjectAdapter(new FilePresenter());
            // 将根目录下的文件和目录添加到文件列表适配器中
            for (File file : files) {
                fileAdapter.add(file);
                LogUtil.d("test1","22222"+ "Added file: " + file.getName());
            }
            LogUtil.d("test1","33333");
            // 创建 HeaderItem 作为文件列表的标题
            HeaderItem header = new HeaderItem(0, "Files and Folders");
            // 创建一个 ListRow 并将其添加到行适配器中
            rowsAdapter.add(new ListRow(header, fileAdapter));
        }
        LogUtil.d("test1","44444");

        // 设置数据适配器
        setAdapter(rowsAdapter);
    }
}
