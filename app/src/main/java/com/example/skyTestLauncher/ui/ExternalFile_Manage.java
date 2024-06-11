package com.example.skyTestLauncher.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.SkyTestLauncher.R;
import com.example.skyTestLauncher.logic.FileManageHelps;
import com.example.skyTestLauncher.logic.FileManagerAdapter;
import com.example.skyTestLauncher.model.ExternalFileModel;
import com.example.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalFile_Manage extends AppCompatActivity {

    FileManagerAdapter localAdapter;

    TextView pathTv;     // 文件目录
    ListView fileLv;     // 文件列表
    ImageView ivClean;   // 清空输入内容
    ImageView ivSearch;  // 搜索关键词
    EditText etSearch;   // 输入内容
    File currentParent;  // 当前的父目录
    File[] currentFiles; // 当前文件
    File root;           // 内部存储的根目录
    Toolbar toolbar;
    FileManageHelps fileManageHelps = new FileManageHelps();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_file_manage);

        toolbar = findViewById(R.id.external_toolbar);
        setSupportActionBar(toolbar);

        // 绑定控件
        pathTv = findViewById(R.id.tv_filepath);
        fileLv = findViewById(R.id.lv_file);
        ivClean = findViewById(R.id.iv_clean);
        ivSearch = findViewById(R.id.iv_search);
        etSearch = findViewById(R.id.et_search);

        initFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentParent.getAbsolutePath().equals(root.getAbsolutePath())) {
                    finish();
                } else {
                    currentParent = currentParent.getParentFile();
                    currentFiles = currentParent.listFiles();
                    inflatelv(currentFiles);
                }
            }
        });
    }

    //监听按键
    @Override
    public void onBackPressed() {
        if (currentParent.getAbsolutePath().equals(root.getAbsolutePath())) {
            finish();
        } else {
            currentParent = currentParent.getParentFile();
            currentFiles = currentParent.listFiles();
            inflatelv(currentFiles);
        }
    }

    private void initFile() {
        // 获取U盘的路径
        String path = ExternalFileModel.getInstance().getExternalFilePath();
        root = new File(path);
        // 当前父目录为root
        currentParent = root;
        if (currentParent.exists() && currentParent.isDirectory()) {
            // 获取当前目录的所有文件
            currentFiles = currentParent.listFiles();
            // 加载列表
            inflatelv(currentFiles);
            // 设置列表子项监听器
            setListener();
        }else{
            LogUtil.d("test1", "No files or directories found in " + currentParent);
        }
    }

    private void inflatelv(File[] currentFiles) {
        String lengthStr = null;
        String fileSize = null;
        String formattedDate = null;
        // 列表对象（元素是哈希表）
        List<Map<String, Object>> list = new ArrayList<>();
        LogUtil.d("test1", "currentFiles.length = " + currentFiles.length);
        for (int i = 0; i < currentFiles.length; i++) {
            // 哈希表对象（键是字符串，值是任意类型）
            Map<String, Object> mp = new HashMap<>();
            mp.put("filename", currentFiles[i].getName());
            // 给文件和文件夹类型赋予不同的icon
            if (currentFiles[i].isDirectory()) {
                mp.put("icon", R.drawable.ic_folder);
                //文件数量
                lengthStr = fileManageHelps.getFolderSubItems(currentFiles[i]);
                mp.put("fileCount", lengthStr);

                LogUtil.d("test1", currentFiles[i].getName() + " is a directory.");
            }else if (currentFiles[i].isFile()) {
                fileSize = fileManageHelps.getFileCount(currentFiles[i]);
                mp.put("fileCount", fileSize);
                mp.put("icon", R.drawable.ic_file);

            }

            // 文件修改时间
            formattedDate = fileManageHelps.getFileTime(currentFiles[i]);
            mp.put("fileTime",formattedDate);

            // 向列表中添加哈希表
            list.add(mp);
        }

        localAdapter = new FileManagerAdapter(this, list);
        // 列表设置适配器
        fileLv.setAdapter(localAdapter);
        // 设置当前路径文本
        pathTv.setText("当前路径：" + currentParent.getAbsolutePath());
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

    private void setListener() {
        // 列表的子项短按点击事件
        fileLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 如果当前目录是文件则返回
                if (currentFiles[i].isFile()) {
                    Toast.makeText(ExternalFile_Manage.this, "无法打开此文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                File[] temp = currentFiles[i].listFiles();
                if (temp == null || temp.length == 0) {
                    Toast.makeText(ExternalFile_Manage.this, "当前文件夹为空", Toast.LENGTH_SHORT).show();
                } else {
                    // 当前目录作为父目录
                    currentParent = currentFiles[i];
                    // 当前文件更新为父目录下的文件
                    currentFiles = temp;
                    // 数据源发生改变，重新设置适配器内容
                    inflatelv(currentFiles);
                }
            }
        });
        // 列表的子项的长按点击事件
        fileLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 对话框，确定按钮和取消按钮
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("警告")
                        .setMessage("您确定要删除该文件（夹）吗？")
                        .setIcon(R.drawable.bg_prompt)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i1) {
                                // 如果是文件夹先删除目录下所有文件
                                if (!currentFiles[i].isFile()) {
                                    File[] files = currentFiles[i].listFiles();
                                    for (int index = 0; index < files.length; index++) {
                                        deleteFile(files[index].getName());
                                    }
                                }
                                // 删除当前文件（夹）
                                if (currentFiles[i].delete()) {
                                    Toast.makeText(view.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                    currentFiles = currentParent.listFiles();
                                    // 数据源发生改变，重新设置适配器内容
                                    inflatelv(currentFiles);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i1) {

                            }
                        })
                        .show();
                return true;
            }
        });
        // 搜索按钮进行数据筛选，显示关键词数据源
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchName = etSearch.getText().toString().trim();
                List<File> tempFiles = new ArrayList<>();
                for (int i = 0; i < currentFiles.length; i++) {
                    if (currentFiles[i].getName().contains(searchName))
                        tempFiles.add(currentFiles[i]);
                }
                File[] files = new File[tempFiles.size()];
                for (int i = 0; i < tempFiles.size(); i++) {
                    files[i] = tempFiles.get(i);
                }
                inflatelv(files);
            }
        });
        // 清空搜索框内容，显示原始数据源
        ivClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
                inflatelv(currentFiles);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        //回到根目录
        if (itemId == R.id.back_root) {
            currentParent = root;
            currentFiles = currentParent.listFiles();
            inflatelv(currentFiles);
        } else if (itemId == R.id.newfile) {
            // 新建文件
            createFile("新建文件");
        } else if (itemId == R.id.newfolder) {
            // 新建文件夹
            createFile("新建文件夹");
        } else {
            // 其他情况
        }
        return true;
    }

    private void createFile(String action) {
        View myView = getLayoutInflater().inflate(R.layout.new_dialog, null, false);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(myView)
                .setCancelable(true)
                .create();
        TextView tvTitle = myView.findViewById(R.id.tv_title);
        tvTitle.setText(action);
        EditText etName = myView.findViewById(R.id.et_name);

        // 按下取消，销毁对话框
        Button tvCancel = myView.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        // 按下新建，创建新文件（夹）
        Button tvNewBuilt = myView.findViewById(R.id.tv_newbuilt);
        tvNewBuilt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = etName.getText().toString().trim();
                // 判空
                if (TextUtils.isEmpty(filename)) {
                    Toast.makeText(ExternalFile_Manage.this, "请输入名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 遍历当前父目录下所有文件，检验是否存在重名文件
                for (int i = 0; i < currentFiles.length; i++) {
                    if (currentFiles[i].getName().equals(filename)) {
                        Toast.makeText(ExternalFile_Manage.this, "存在同名文件", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 如果是新建文件
                if (action.equals("新建文件")) {
                    try {
                        // 创建个新文件对象，参数为文件路径
                        File file = new File(currentParent.getAbsolutePath() + "/" + filename);
                        // 新建文件
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    newBuildRefresh();
                    alertDialog.dismiss();
                } else {
                    // 创建个新文件对象，参数为文件路径
                    File file = new File(currentParent.getAbsolutePath() + "/" + filename);
                    // 创建新文件夹
                    file.mkdir();
                    newBuildRefresh();
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }
    // 新建后Toast提示，更新列表数据源
    private void newBuildRefresh() {
        Toast.makeText(ExternalFile_Manage.this, "新建成功！", Toast.LENGTH_SHORT).show();
        currentFiles = currentParent.listFiles();
        inflatelv(currentFiles);
    }
}
