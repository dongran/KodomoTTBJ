package org.kodomottbj.sunchen.edu.kodomottbj;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kodomottbj.sunchen.edu.kodomottbj.ui.Category;
import org.kodomottbj.sunchen.edu.kodomottbj.ui.LoginActivity;
import org.kodomottbj.sunchen.edu.kodomottbj.ui.MojiActivity;
import org.kodomottbj.sunchen.edu.kodomottbj.ui.Process;
import org.kodomottbj.sunchen.edu.kodomottbj.ui.SpotActivity;
import org.kodomottbj.sunchen.edu.kodomottbj.ui.SpotMojiActivity;
import org.kodomottbj.sunchen.edu.kodomottbj.util.CommonNet;
import org.kodomottbj.sunchen.edu.kodomottbj.util.CommonSetting;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    long m_newVerCode; //最新版的版本号
    String m_newVerName; //最新版的版本名
    String m_appNameStr; //下载到本地要给这个APP命的名字

    Handler m_mainHandler;
    ProgressDialog m_progressDlg;

    ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* 九宫格 start*/
        GridView gridview = (GridView) findViewById(R.id.gridView1);
        initVariable(); //初始化相关变量
        SimpleAdapter saImageItems = new SimpleAdapter(this,
                lstImageItem,
                R.layout.grid_item,
                new String[] {"ItemImage","ItemText"},
                new int[] {R.id.imageView1,R.id.msg}
        );

        gridview.setAdapter(saImageItems);
        gridview.setOnItemClickListener(new ItemClickListener());


        /*  九宫格 end */


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 检查软件更新
                new checkNewestVersionAsyncTask().execute();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

   /*  九宫格 start */
    class  ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it  = new Intent();
            switch (position){
                case 0:
                    it.setClass(MenuActivity.this, SpotActivity.class);
                    /*it.putExtra(CommonSetting.FileNameTag, "test.xml");
                    it.putExtra("domain",0);
                    it.putExtra("num", 0);*/
                    startActivity(it);
                    break;
                case 1:
                    it.setClass(MenuActivity.this, SpotMojiActivity.class);
                     /* it.putExtra(CommonSetting.FileNameTag, "test.xml");
                    it.putExtra("domain",1);
                    it.putExtra("num", 0);*/
                    startActivity(it);
                    break;
                case 2:
                    it.setClass(MenuActivity.this, MojiActivity.class);
                    /*it.putExtra(CommonSetting.FileNameTag, "test.xml");
                    it.putExtra("domain",1);
                    it.putExtra("num", 0);*/
                    startActivity(it);
                    break;
                case 3:
                    it.setClass(MenuActivity.this, Category.class);
                    startActivity(it);
                    break;
            }

        }
    }
    /*  九宫格 end */

    private void initVariable()
    {
        m_mainHandler = new Handler();
        m_progressDlg =  new ProgressDialog(this);
        m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        m_progressDlg.setIndeterminate(false);
        m_appNameStr = "kodomoTTBJ.apk";

        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("ItemImage", R.drawable.list_pg);//添加图像资源的ID
        map1.put("ItemText", "SPOT");//按序号做ItemText
        lstImageItem.add(map1);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("ItemImage", R.drawable.list_pg);//添加图像资源的ID
        map2.put("ItemText", "かんじSPOT");//按序号做ItemText
        lstImageItem.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("ItemImage",R.drawable.list_pg);//添加图像资源的ID
        map3.put("ItemText", "かんじテスト");//按序号做ItemText
        lstImageItem.add(map3);


        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("ItemImage",R.drawable.tool_box_baohe);//添加图像资源的ID
        map4.put("ItemText", "成績");//按序号做ItemText
        lstImageItem.add(map4);

//        HashMap<String, Object> map5 = new HashMap<String, Object>();
//        map5.put("ItemImage", R.drawable.tool_box_network);//添加图像资源的ID
//        map5.put("ItemText", "ダンロード");//按序号做ItemText
//        lstImageItem.add(map5);
    }

    /* CheckVersions start */
    public  class checkNewestVersionAsyncTask extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            if(postCheckNewestVersionCommand2Server())
            {
                int vercode = CommonNet.getVerCode(getApplicationContext()); // 引用CommonNet包
                if (m_newVerCode > vercode) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            if (result) {//如果有最新版本
                doNewVersionUpdate(); // 更新新版本
            }else {
                notNewVersionDlgShow(); // 提示当前为最新版本
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
    }

    /**
     * 从服务器获取当前最新版本号，如果成功返回TURE，如果失败，返回FALSE
     * @return
     */
    private Boolean postCheckNewestVersionCommand2Server()
    {
        StringBuilder builder = new StringBuilder();
        JSONObject jsonArray = null;
        try {
            // 构造POST方法的{name:value} 参数对
            List<NameValuePair> vps = new ArrayList<NameValuePair>();
            // 将参数传入post方法中
            vps.add(new BasicNameValuePair("action", "checkNewestVersion"));
            builder = CommonNet.post_to_server(vps);
            Log.e("msg", builder.toString());
            jsonArray = new JSONObject(builder.toString());
            if (jsonArray.length()>0) {
                if (jsonArray.getInt("id") == 1) {
                    m_newVerName = jsonArray.getString("verName");
                    m_newVerCode = jsonArray.getLong("verCode");

                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            Log.e("msg",e.getMessage());
            m_newVerName="";
            m_newVerCode=-1;
            return false;
        }
    }

    /**
     * 提示更新新版本
     */
    private void doNewVersionUpdate() {
        int verCode = CommonNet.getVerCode(getApplicationContext());
        String verName = CommonNet.getVerName(getApplicationContext());

        String str= "Now Version："+verName+" Code: "+verCode+" , Find new version: "+m_newVerName+
                " Code: "+m_newVerCode+" , Whether to download the new Version？";
        Dialog dialog = new AlertDialog.Builder(this).setTitle("Update New Version").setMessage(str)
                // 设置内容
                .setPositiveButton("Update",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                m_progressDlg.setTitle("Updating");
                                m_progressDlg.setMessage("waiting please...");
                                downFile(CommonNet.UPDATESOFTADDRESS);  //开始下载
                            }
                        })
                .setNegativeButton("Not Now",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 点击"取消"按钮之后退出程序
                                dialog.cancel();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }
    /**
     *  提示当前为最新版本
     */
    private void notNewVersionDlgShow()
    {
        int verCode = CommonNet.getVerCode(this);
        String verName = CommonNet.getVerName(this);
        String str="Now Version:"+verName+" Code:"+verCode+",/nAlready the lastest!";
        Dialog dialog = new AlertDialog.Builder(this).setTitle("Update New Version")
                .setMessage(str)// 设置内容
                .setPositiveButton("Sure",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }


    private void downFile(final String url)
    {
        m_progressDlg.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();

                    m_progressDlg.setMax((int)length);//设置进度条的最大值

                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                m_appNameStr);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                                m_progressDlg.setProgress(count);
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();  //告诉HANDER已经下载完成了，可以安装了
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 告诉HANDER已经下载完成了，可以安装了
     */
    private void down() {
        m_mainHandler.post(new Runnable() {
            public void run() {
                m_progressDlg.cancel();
                update();
            }
        });
    }
    /**
     * 安装程序
     */
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // 关键，，我不加这一句还是会安装到一般闪退，以下加上这句的说明
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), m_appNameStr)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /* CheckVersions end */



    /*  下载题目 start*/
    private void downExam(final String url)
    {
        m_progressDlg.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();

                    m_progressDlg.setMax((int)length);//设置进度条的最大值

                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                m_appNameStr);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                                m_progressDlg.setProgress(count);
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();  //告诉HANDER已经下载完成了，可以安装了
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /* 下载题目 end*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    /**
     *
     * 正在更新试卷
     */
    private Dialog updateExamProcess()
    {
        String str="Now Downloading, please wait a minute!";
        Dialog dialog = new AlertDialog.Builder(this).setTitle("Update New Exams")
                .setMessage(str)// 设置内容
                .setPositiveButton("Sure",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
        return dialog;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exam) {
            // Handle the camera action
            //String str="Now Downloading, please wait a minute!";
            Toast.makeText(this, "Downloading, Please Wait minute!", Toast.LENGTH_LONG).show();
            copyFilesFassets(MenuActivity.this, "data", CommonSetting.SDCardDiretory);

            String str="Already Updated questions";
            Dialog dialog = new AlertDialog.Builder(this).setTitle("Update questions")
                    .setMessage(str)// 设置内容
                    .setPositiveButton("Sure",// 设置确定按钮
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            }).create();// 创建
            dialog.show();



        } else if (id == R.id.nav_logout) {
            Intent it= new Intent();
            it.setClass(MenuActivity.this, LoginActivity.class);
            startActivity(it);

        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "Manage My exams!", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Sharing with US !", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "Send Scores!", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
