/**
 *   Category - Acitivity of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.kodomottbj.sunchen.edu.kodomottbj.R;
import org.kodomottbj.sunchen.edu.kodomottbj.db.DBAccess;
import org.kodomottbj.sunchen.edu.kodomottbj.entity.ProfileEntity;
import org.kodomottbj.sunchen.edu.kodomottbj.util.CommonSetting;
import org.kodomottbj.sunchen.edu.kodomottbj.xml.ParseException;
import org.kodomottbj.sunchen.edu.kodomottbj.xml.XmlParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 主目录界面
 * <br/>
 * @author sunchen (sunchen1221@gmail.com)
 * @data 2016-2-21
 * @version 1.1
 * <ul>
 * 		<li> 1.0 创建</li>
 * 		<li> 1.1 修复SD卡中没有数据时，报NullPointerException的问题。</li>
 * </ul>
 */
public class Category extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		File f = new File(CommonSetting.SDCardDiretory);
		if(!f.exists()) f.mkdir();
		updateCategory();
	}

	/**
	 * 当CategoryActivity被打开时，即调用些方法更新目录列表。
	 * 在更新目录时，应用首先检查SD卡目录中是否存在考卷数据。
	 * 如果存在，即把考卷全部列出。如果不存在，显示空列表，并提示用户。
	 */
	private void updateCategory(){
		final ListView categoryList = (ListView)findViewById(R.id.categoryList);
		final List<HashMap<String, Object>> dataCache = loadProfileList();
		final SimpleAdapter adapter = new SimpleAdapter(Category.this,dataCache,
				R.layout.categoryitem,
				new String[] { "icon", "title", "info","creator","fileId","fName" },
				new int[] { R.id.listIcon, R.id.listTitle, R.id.listInfo ,R.id.listAuthor,R.id.listFileId,R.id.listFileName});
		categoryList.setAdapter(adapter);
		categoryList.setOnItemClickListener(new OnItemClickListener() {  //单击事件
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				listItemClicked(view);
			}
		});
		categoryList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {//长按事件
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				getMenuInflater().inflate(R.menu.context, menu);
			}
		});
	}

	/**
	 * 从SD卡保存目录中加载Profile列表
	 * @return HashMap列表，如果没有数据，返回空列表，不是NULL。
	 * <p>
	 * 	SD卡中可能没有数据，如果没有，显示信息提示用户。
	 * </p>
	 */
	private List<HashMap<String, Object>> loadProfileList(){
		List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String,Object>>();

		List<ProfileEntity> plist = new ArrayList<ProfileEntity>();
		try {
			XmlParser.getProfiles(plist,CommonSetting.SDCardDiretory);
		} catch (ParseException e) {
			Toast.makeText(this,getResources().getString(R.string.parseSingleError), Toast.LENGTH_LONG).show();
		}
		for(ProfileEntity pe : plist){

			String fileId = String.valueOf(pe.getFileId());
			final int score = DBAccess.getScore(this,fileId);
			if (score == -1){
				continue;
			}

			HashMap<String, Object> map = new HashMap<String, Object>();
			int id = pe.getImgId();
			switch (id){
				case 0:
					map.put("icon",R.drawable.bookicon);
					break;
				case 1:
					map.put("icon",R.drawable.bookicon1);
					break;
				case 2:
					map.put("icon",R.drawable.bookicon2);
					break;
				case 3:
					map.put("icon",R.drawable.bookicon3);
					break;
				case 4:
					map.put("icon",R.drawable.bookicon4);
					break;
				case 5:
					map.put("icon",R.drawable.bookicon5);
					break;
				case 6:
					map.put("icon",R.drawable.bookicon6);
					break;
			}
			//map.put("icon",R.drawable.bookicon);
			map.put("title", pe.getTitle());
			map.put("info", pe.getDescription());
			map.put("fName", pe.getFileName());
			map.put("fileId", pe.getFileId());
			map.put("creator", String.format(getResources().getString(R.string.listAuthor),pe.getCreator()));
			dataList.add(map);
		}
		if(dataList.isEmpty()){
			Toast.makeText(this,getResources().getString(R.string.xmlEmptyError), Toast.LENGTH_LONG).show();
		}
		return dataList;
	}

	/**
	 * 简报列表被点击时，此方法被调用。
	 * <p>
	 * 列表中某一项被点击时，首先查询这一项的考卷有没有简报信息。
	 * 如果存在，则跳转到简报界面，显示简报信息。
	 * 如果不存在 ，则打开考试界面，进行考试。
	 * </p>
	 * @param view 被点击的View
	 */
	private void listItemClicked(View view){
		final String fileName = ((TextView)view.findViewById(R.id.listFileName)).getText().toString();
		final String id = ((TextView)view.findViewById(R.id.listFileId)).getText().toString();
		Intent intent = new Intent();
		intent.putExtra(CommonSetting.FileNameTag, fileName);
		if(DBAccess.profileExist(this,id)){
			intent.putExtra("category",1);
			intent.setClass(this, Profile.class);
		}else{
			intent.setClass(this, Process.class);
		}
		startActivity(intent);
	}

	/**
	 * 长按列表中的项目时，此方法被调用。
	 * <p>
	 * 根据选项执行不同的动作。
	 * </p>
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		final String fileName = ((TextView)menuinfo.targetView.findViewById(R.id.listFileName)).getText().toString();
		if(item.getOrder() == getResources().getInteger(R.integer.contextViewIndex)){
			final Intent intent = new Intent(this, Profile.class);
			intent.putExtra(CommonSetting.FileNameTag, fileName);
			startActivity(intent);
		}else if(item.getOrder() == getResources().getInteger(R.integer.contextRestartIndex)){
			final Intent intent = new Intent(this, Process.class);
			intent.putExtra(CommonSetting.FileNameTag, fileName);
			startActivity(intent);
		}else if(item.getOrder() == getResources().getInteger(R.integer.contextCleanIndex)){
			final String id = ((TextView)menuinfo.targetView.findViewById(R.id.listFileId)).getText().toString();
			DBAccess.deleteProfile(this,id);
		}else if(item.getOrder() == getResources().getInteger(R.integer.contextDeleteIndex)){
			new File(CommonSetting.SDCardDiretory + fileName).delete();
			final String id = ((TextView)menuinfo.targetView.findViewById(R.id.listFileId)).getText().toString();
			DBAccess.deleteProfile(this,id);
			updateCategory();
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 点击Menu菜单时，此方法被调用。
	 * <p>此方法显示选项菜单，菜单的操作实现参见 </p>
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.option, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Menu菜单选项被选择时，此方法被调用。
	 * <p>
	 * 根据菜单的Order，判断选择了什么选项，然后调用相关的Activity。
	 * </p>
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*final int order = item.getOrder();
		if(order == getResources().getInteger(R.integer.optionAddIndex)){
			Intent intent = new Intent(this,Download.class);
			startActivityForResult(intent, CommonSetting.SendDownload);
		}else if(order == getResources().getInteger(R.integer.optionAboutIndex)){
			Intent intent = new Intent(this,About.class);
			startActivity(intent);
		}else if(order == getResources().getInteger(R.integer.optionExitIndex)){
			finish();
		}*/
		return super.onContextItemSelected(item);
	}

	/**
	 * 下载考卷的Activity如果下载文件，则刷新目录列表
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CommonSetting.SendDownload && resultCode == CommonSetting.RecvDownload){
			if(data.getStringExtra(CommonSetting.DownloadSuccessTag).equals(CommonSetting.DownloadSuccess)){
				updateCategory();//下载成功，则更新目录列表
			}
		}
	}
}
