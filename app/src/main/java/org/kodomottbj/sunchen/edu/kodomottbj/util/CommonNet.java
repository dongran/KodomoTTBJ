package org.kodomottbj.sunchen.edu.kodomottbj.util;

/**
 * Created by SCI01628 on 2016/08/08.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class CommonNet {
    public static final String SERVER_IP="http://kaken.sakura.ne.jp/kodomoTTBJ/";
    public static final String SERVER_ADDRESS=SERVER_IP+"index.php/Admin/Index/checkVersion";//软件更新包地址
    public static final String UPDATESOFTADDRESS=SERVER_IP+"Uploads/kodomoTTBJ.apk";//软件更新包地址

    /**
     * 向服务器发送查询请求，返回查到的StringBuilder类型数据
     *
     * @param ArrayList
     *            <NameValuePair> vps POST进来的参值对
     * @return StringBuilder builder 返回查到的结果
     * @throws Exception
     */
    public static StringBuilder post_to_server(List<NameValuePair> vps) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpResponse response = null;
            // 创建httpost.访问本地服务器网址
            HttpPost httpost = new HttpPost(SERVER_ADDRESS);
            StringBuilder builder = new StringBuilder();

            httpost.setEntity(new UrlEncodedFormEntity(vps, HTTP.UTF_8));
            response = httpclient.execute(httpost); // 执行

            if (response.getEntity() != null) {
                // 如果服务器端JSON没写对，这句是会出异常，是执行不过去的
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
                String s = reader.readLine();
                for (; s != null; s = reader.readLine()) {
                    builder.append(s);
                }
            }
            return builder;

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("msg",e.getMessage());
            return null;
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();// 关闭连接
                // 这两种释放连接的方法都可以
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("msg",e.getMessage());
            }
        }
    }

    /**
     * 获取软件版本号
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.getPackageManager().getPackageInfo(
                    "org.kodomottbj.sunchen.edu.kodomottbj", 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.e("msg",e.getMessage());
        }
        return verCode;
    }
    /**
     * 获取版本名称
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "org.kodomottbj.sunchen.edu.kodomottbj", 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e("msg",e.getMessage());
        }
        return verName;
    }
}