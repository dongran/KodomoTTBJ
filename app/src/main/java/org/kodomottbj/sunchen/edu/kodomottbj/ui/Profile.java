/**
 *   Profile - Acitivity of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.kodomottbj.sunchen.edu.kodomottbj.MenuActivity;
import org.kodomottbj.sunchen.edu.kodomottbj.R;
import org.kodomottbj.sunchen.edu.kodomottbj.db.DBAccess;
import org.kodomottbj.sunchen.edu.kodomottbj.entity.ProfileEntity;
import org.kodomottbj.sunchen.edu.kodomottbj.util.CommonSetting;
import org.kodomottbj.sunchen.edu.kodomottbj.xml.ParseException;
import org.kodomottbj.sunchen.edu.kodomottbj.xml.XmlParser;

import java.io.File;

public class Profile extends Activity {
	private String fileId ;
	private String fileName ;
	private ProfileEntity profile;
	private int curDomain = -1;
	private int curnum = -1;
	private int category = 0;
	public Button goon;
	public Button back;
	public Button back2;
	/**
	 * 对错矩阵的最大行数列数
	 */
	private final int rowMax = 1;
	private final int colMax = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		fileName = getIntent().getStringExtra(CommonSetting.FileNameTag);
		curDomain = getIntent().getIntExtra("domain", -1);
		curnum = getIntent().getIntExtra("num", -1);
		category = getIntent().getIntExtra("category", 0);
		fillHeader();
		fillMatrix();
		updateScoreRand();
	}

	/**
	 * 填充考卷信息头
	 */
	private void fillHeader(){
		try {
			profile = XmlParser.getProfile(new File(CommonSetting.SDCardDiretory + fileName));
		} catch (ParseException e) {
			Toast.makeText(this,getResources().getString(R.string.parseError), Toast.LENGTH_SHORT).show();
		}
		fileId = String.valueOf(profile.getFileId());

		final TextView title = (TextView)findViewById(R.id.profileTitle);
		final TextView info = (TextView)findViewById(R.id.profileInfo);
		title.setText(String.format(getResources().getString(R.string.profileTitle),profile.getTitle()));
		//title.setText(fileId);

		final int minute = (int) DBAccess.getElapsedTime(this,fileId)/60;
		final int second = (int)DBAccess.getElapsedTime(this,fileId)%60;
		final String MIN = minute < 10 ? "0"+minute : ""+minute;
		final String SEC = second < 10 ? "0"+second : ""+second;

		info.setText(String.format(getResources().getString(R.string.profileInfo),
				profile.getTotalTime(),
				profile.getTotalScore(),
				MIN,SEC
		));

	}

	/**
	 * 更新得分勋章表
	 */
	private void updateScoreRand(){
		final TextView scroeView = (TextView)findViewById(R.id.scoreView);
		final ProgressBar sb = (ProgressBar)findViewById(R.id.scoreProgress);
		final int score = DBAccess.getScore(this,fileId);
		sb.setProgress(updateProgress(score, profile.getTotalScore()));
		scroeView.setText(""+score);
		int rCount = 0;
		int wCount = 0;
		for(boolean b : DBAccess.getRwMatrix(this,fileId)){
			if(b) rCount++;
			else wCount++;
		}
		final TextView rView = (TextView)findViewById(R.id.scoreRightCount);
		final TextView wView = (TextView)findViewById(R.id.scoreWrongCount);
		rView.setText(""+rCount);
		wView.setText(""+wCount);

		final ProgressBar prb = (ProgressBar)findViewById(R.id.profileRightProgress);
		final ProgressBar pwb = (ProgressBar)findViewById(R.id.profileWrongProgress);
		final int totalCount = DBAccess.getRwMatrix(this,fileId).length;
		prb.setProgress(updateProgress(rCount,totalCount));
		pwb.setProgress(updateProgress(wCount,totalCount));


		goon = (Button) findViewById(R.id.goon);
		back = (Button) findViewById(R.id.back);
		back2 = (Button) findViewById(R.id.back2);

		/*if(rCount/(rCount+wCount) < 0.6){
			goon.setVisibility(View.INVISIBLE);
		}*/
		//Log.d("mean","score: "+score/100);
		if (category != 1){
			//Log.d("mean","score: "+rCount/(rCount+wCount));
			/*if(curnum < 3 && curnum >= 0) {*/
				/*if ( score >= 60) {*/
					goon.setVisibility(View.VISIBLE);
					back.setVisibility(View.VISIBLE);
				/*} else {
					goon.setVisibility(View.GONE);
				}*/
//			}
		}else {
			goon.setVisibility(View.GONE);
			back.setVisibility(View.GONE);
			back2.setVisibility(View.VISIBLE);
		}
		//back.setVisibility(View.VISIBLE);
		goon.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(curDomain == 0) {
						Intent intent = new Intent(Profile.this, SpotActivity.class);
						startActivity(intent);
						/*Intent intent = new Intent(Profile.this, Process.class);
						intent.putExtra(CommonSetting.FileNameTag, "spot"+curnum+".xml");
						intent.putExtra("domain", 0);
						intent.putExtra("num", ++curnum);
						startActivity(intent);*/
				}else if(curDomain == 1) {
						Intent intent = new Intent(Profile.this, SpotMojiActivity.class);
						startActivity(intent);
						/*Intent intent = new Intent(Profile.this, Process.class);
						intent.putExtra(CommonSetting.FileNameTag, "moji"+curnum+".xml");
						intent.putExtra("domain", 1);
						intent.putExtra("num", ++curnum);
						startActivity(intent);*/

				}else {
					Intent intent = new Intent(Profile.this, MojiActivity.class);
					startActivity(intent);
					/*Intent intent = new Intent(Profile.this, Process.class);
					intent.putExtra(CommonSetting.FileNameTag, "mojispot"+curnum+".xml");
					intent.putExtra("domain", 2);
					intent.putExtra("num", ++curnum);
					startActivity(intent);*/
				}
			}
		});

		back2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Profile.this, Process.class);
				intent.putExtra(CommonSetting.FileNameTag, "spot"+curnum+".xml");
				intent.putExtra("domain", 0);
				intent.putExtra("istest", 1);
				startActivity(intent);
			}
		});

	}

	/**
	 * 更新对错进度
	 * @param cur
	 * @param max
	 * @return
	 */
	private int updateProgress(int cur,int max){
		return (Math.round((float)cur/max*100));
	}

	/**
	 * 填充对错矩阵
	 */
	private void fillMatrix(){
		final LinearLayout matrix = (LinearLayout)findViewById(R.id.scoreMatrix);
		final LinearLayout.LayoutParams lpFW = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		final LinearLayout.LayoutParams lpWW = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		for(int row = 0 ; row < rowMax ; row++){
			final LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			final boolean[] rwMatrix = DBAccess.getRwMatrix(this,fileId);
			for(int col=0;col<colMax;col++){
				final int index = row * colMax + col;
				final ImageView item = new ImageView(this);
				item.setPadding(4, 4, 4, 15);
				item.setTag(index);
				if(index < rwMatrix.length){
					item.setImageResource( rwMatrix[index] ? R.drawable.table_yes : R.drawable.table_no );
				}else{
					item.setImageResource(R.drawable.table_nop);
					item.setTag(-1);
				}
				item.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						final int position = (Integer)v.getTag();
						if( position != -1){
							final Intent intent = new Intent(Profile.this,Single.class);
							intent.putExtra(CommonSetting.FileNameTag, fileName);
							intent.putExtra(CommonSetting.SubjectLocationTag, position);
							startActivity(intent);
						}else{
							final String tip = getResources().getString(R.string.profileNotSubject);
							Toast.makeText(Profile.this, tip, Toast.LENGTH_SHORT).show();
						}
					}
				});
				ll.addView(item,lpWW);
			}
			matrix.addView(ll,lpFW);
		}
	}
}
