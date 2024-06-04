package com.example.skyTestLauncher.ui;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;
import java.io.File;
import com.example.SkyTestLauncher.R;
import com.example.utils.LogUtil;

/**
 * 文件预览器类，继承自Presenter，用于在Leanback环境下展示文件列表项。
 */
public class FilePresenter extends Presenter {

    /**
     * 创建一个新的ViewHolder实例，用于持有文件项的视图。
     *
     * @param parent 文件项视图的父 ViewGroup。
     * @return 新创建的ViewHolder实例。
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        // 使用LayoutInflater从xml布局文件中创建文件项的视图
        LogUtil.d("test1","aaaaa0");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 将数据绑定到已创建的ViewHolder的视图上。
     *
     * @param viewHolder 文件项的ViewHolder。
     * @param item       待展示的文件对象。
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        File file = (File) item;
        View view = viewHolder.view;
        ImageView fileIcon = view.findViewById(R.id.file_icon);
        TextView fileName = view.findViewById(R.id.file_name);


        fileName.setText(file.getName());
        if (file.isDirectory()) {
            fileIcon.setImageResource(R.drawable.ic_folder);
        } else {
            fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

    /**
     * 解绑ViewHolder上的数据。在此方法中可以进行资源的回收和清理。
     *
     * @param viewHolder 被解绑的ViewHolder。
     */
    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        // Clean up any references to the view to avoid memory leaks
        ImageView fileIcon = viewHolder.view.findViewById(R.id.file_icon);
        fileIcon.setImageDrawable(null);

        TextView fileName = viewHolder.view.findViewById(R.id.file_name);
        fileName.setText(null);
    }
}
