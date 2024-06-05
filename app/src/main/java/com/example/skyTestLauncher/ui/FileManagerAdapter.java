package com.example.skyTestLauncher.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.SkyTestLauncher.R;

import java.util.List;
import java.util.Map;

public class FileManagerAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> dataList;

    public FileManagerAdapter(Context context, List<Map<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
            viewHolder.textView = convertView.findViewById(R.id.item_tv);
            viewHolder.imageView = convertView.findViewById(R.id.item_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Map<String, Object> data = dataList.get(position);
        viewHolder.textView.setText((String) data.get("filename"));
        viewHolder.imageView.setImageResource((Integer) data.get("icon"));

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}