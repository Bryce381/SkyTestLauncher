package com.example.skyTestLauncher.ui;

import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.app.VerticalGridSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.VerticalGridPresenter;

import com.example.SkyTestLauncher.R;

import java.io.File;

public class File_Manager extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FileManagerFragment())
                    .commit();
        }
    }

    public static class FileManagerFragment extends VerticalGridSupportFragment {
        private static final int NUM_COLUMNS = 1;
        private ArrayObjectAdapter mAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setTitle("File Manager");

            VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
            gridPresenter.setNumberOfColumns(NUM_COLUMNS);
            setGridPresenter(gridPresenter);

            mAdapter = new ArrayObjectAdapter(new FilePresenter());
            setAdapter(mAdapter);

            loadFiles();
        }

        private void loadFiles() {
            // 获取根目录
            File rootDir = Environment.getExternalStorageDirectory();
            File[] files = rootDir.listFiles();

            if (files != null) {
                for (File file : files) {
                    mAdapter.add(file);
                }
            }
        }
    }
}
