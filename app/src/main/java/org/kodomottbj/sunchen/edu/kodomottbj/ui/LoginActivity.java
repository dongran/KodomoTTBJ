package org.kodomottbj.sunchen.edu.kodomottbj.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


import org.kodomottbj.sunchen.edu.kodomottbj.MenuActivity;
import org.kodomottbj.sunchen.edu.kodomottbj.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by SunChen on 16/03/09.
 */
public class LoginActivity extends Activity {
    private Button login;
    private EditText username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //没标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        findViewId();
        listener();

        //copyFilesFassets(LoginActivity.this, "data", CommonSetting.SDCardDiretory);

    }

    private void findViewId() {
        login = (Button) findViewById(R.id.login_activity_login);
        username = (EditText) findViewById(R.id.username);
    }
    private void listener() {
        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();*/

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
                /*if(!user.trim().equals("")&&!pass.trim().equals("")) {

                    if("admin".equals(user)&&"123456".equals(pass)) {
                        Intent intent = new Intent(LoginActivity.this, Menu.class);
                        startActivity(intent);
                        finish();
                    } else if ("admin".equals(user)&&!"123456".equals(pass)) {
                        Toast.makeText(getApplicationContext(),R.string.pass_miss, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),R.string.login_lose, Toast.LENGTH_SHORT).show();
                    }
                } else if (user.equals("")&&!pass.equals("")) {
                    Toast.makeText(getApplicationContext(),R.string.user_isnotnull, Toast.LENGTH_SHORT).show();

                } else if (!user.equals("")&&pass.equals("")){
                    Toast.makeText(getApplicationContext(),R.string.pass_isnotnull, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),R.string.user_pass_isnotnull, Toast.LENGTH_SHORT).show();
                }*/

            }
        });
    }

    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    public void copyFilesFassets(Context context, String oldPath, String newPath) {

        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名

            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                //blockSize = getFileSizes(file);

                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    //Log.d("test","from"+oldPath + "/"+ fileName + " to"+newPath+fileName);
                    copyFilesFassets(context,oldPath + "/" + fileName,newPath+fileName);
                }
            } else {//如果是文件

                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount=0;

                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
            //CopyAssetToSD.handler.sendEmptyMessage(COPY_FALSE);
        }
    }

    /**
     * 获取指定文件大小
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception
    {
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        else{
            file.createNewFile();
            //Log.e("获取文件大小","文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++){
            if (flist[i].isDirectory()){
                size = size + getFileSizes(flist[i]);
            }
            else{
                size =size + getFileSize(flist[i]);
            }
        }
        return size;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

