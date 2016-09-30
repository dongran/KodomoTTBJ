/**
 * 	 Splash - Splash  Activity of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 */
package org.kodomottbj.sunchen.edu.kodomottbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import org.kodomottbj.sunchen.edu.kodomottbj.ui.LoginActivity;


/**
 * 启动界面
 * <br/>
 * 显示版本、作者、Logo。
 * @author SC (sunchen1221@gmail.com)
 * @data 2016-1-23
 */

public class Splash extends Activity {

	private ImageView splashImgView = null;
	private int onShowTime = 2000;
	private Class<?> nextUI = LoginActivity.class; //不加<?>会有警告

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		splashImgView =(ImageView)findViewById(R.id.splashImageView);
	}
	@Override
	protected void onStart() {
		super.onStart();
		int sIndex = 0;
		showImg(0.2f,R.drawable.cfuture_logo_320dpi,300);
		showImg(0.2f,R.drawable.splash,onShowTime * ++sIndex);
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				startActivity(new Intent(Splash.this,nextUI));
				Splash.this.finish();
			}
		}, onShowTime * ++sIndex);
	}

	private void showImg(final float startAlpha,final int drawableId,int delay){
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				splashImgView.setImageResource(drawableId);
				AlphaAnimation animation = new AlphaAnimation(startAlpha, 1.0f);
				animation.setDuration(4000);
				splashImgView.startAnimation(animation);
			}
		},delay);
	}
}