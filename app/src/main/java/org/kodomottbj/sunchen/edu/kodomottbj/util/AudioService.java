package org.kodomottbj.sunchen.edu.kodomottbj.util;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by SunChen on 16/8/21.
 */
public class AudioService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private MediaPlayer mediaPlayer = null;

    //final  String nullUrl = CommonSetting.SDCardDiretory+"audiosnull.mp3";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {

        public AudioService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return AudioService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }


    /**
     * 播放音频
     */

    public void PlayMp3(String url) {

        if (mediaPlayer == null) {
            //Log.d("mean", "建Mediaplay");
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer = createLocalMp3(url);
        //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
        //以便其他应用程序可以使用该资源:
        /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp) {
				if(mp != null) {
					mp.reset();//释放音频资源
					Log.d("mean", "reseted");
				}
			}
		});*/
        try {
            //在播放音频资源之前，必须调用Prepare方法完成些准备工作
            //Log.d("mean", "播放播放播放");
            mediaPlayer.prepare();
            //开始播放音频
            //Log.d("mean", "播放播放播放2");
            mediaPlayer.start();
            //Log.d("mean", "播放播放播放3");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StopMp3() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

    }

    /**
     * 创建本地MP3音频文件
     */
    private MediaPlayer createLocalMp3(String url) {
        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalStateException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        //mediaPlayer.stop();
        return mediaPlayer;
    }

    /**
     * 创建网络mp3
     *
     * @return
     */
    private MediaPlayer createNetMp3(String url) {
        //String url="http://192.168.1.100:8080/media/beatit.mp3";
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(url);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalStateException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return mp;
    }

}
