/**
 *   DatabaseAccess - Database util of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 */
package org.kodomottbj.sunchen.edu.kodomottbj.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用于保存考卷简报的类
 * <p>
 * 	主要利用SharedPreferences进行数据保存。SharedPreferences是以Key-Value（键值对）的方式
 * 	把数据保存到/data/data/本应用的包名/shared_prefs里。本应用所保存的数据量很小，所以不
 * 	需要用到数据库进行保存。
 * </p>
 * @author sunchen ( sunchen1221@gmail.com )
 * @data 2016-2-22
 * @version 1.1
 * 		<ul>
 * 			<li>1.0 创建</li>
 * 			<li>1.1 把全部方法修改为静态方法</li>
 *      </ul>
 */
public class DBAccess {

	private final static String profileDatabase = "androidexam_profile_database";
	/**
	 * 如果这些顺序被修改，请修改{@link updateProfile}内的顺序。
	 */
	private final static int scoreIndex  = 0;
	private final static int timeIndex  = 1;
	private final static int matrixIndex  = 2;

	/**
	 * 查询ID对应的简报信息是否存在
	 * @param id
	 * 		Long型数据，记录在考卷中，唯一标识考卷的数值串
	 * @return
	 * 		如果考卷简报信息存在，则返回true，否则返回false。
	 */
	public static boolean profileExist(Context context, String id){
		SharedPreferences sp = context.getSharedPreferences(profileDatabase, Activity.MODE_PRIVATE);
		return sp.getString(id, null) == null ? false : true;
	}

	/**
	 * 根据指定的考卷ID，读取考卷的成绩
	 * @param id 考卷ID
	 * @return 考卷成绩
	 */
	public static int getScore(Context context, String id){
		int score = -1;
		SharedPreferences sp = context.getSharedPreferences(profileDatabase, Activity.MODE_PRIVATE);
		String data = sp.getString(id, null);
		if(data != null){
			score = Integer.valueOf(data.split(",")[scoreIndex]);
		}
		return score;
	}

	/**
	 * 读取考卷用时
	 * @param id
	 * @return
	 */
	public static long getElapsedTime(Context context, String id){
		long time = 0L;
		SharedPreferences sp = context.getSharedPreferences(profileDatabase, Activity.MODE_PRIVATE);
		String data = sp.getString(id, null);
		if(data != null){
			time = Long.valueOf(data.split(",")[timeIndex]);
		}
		return time;
	}

	/**
	 * 取得考卷对错矩阵
	 * @param id
	 * @return
	 */
	public static boolean[] getRwMatrix(Context context, String id){
		SharedPreferences sp = context.getSharedPreferences(profileDatabase, Activity.MODE_PRIVATE);
		String data = sp.getString(id, null);
		if(data != null){
			char[] cMatrix = data.split(",")[matrixIndex].toCharArray();
			boolean[] rwMatrix = new boolean[cMatrix.length];
			for(int i=0;i<cMatrix.length;i++){
				rwMatrix[i] = cMatrix[i] == 'R' ? true : false;
			}
			return rwMatrix;
		}
		return null;
	}

	/**
	 * 创建空的Profile
	 * @param context
	 * @param id
	 * @param subjectCount
	 */
	public static void createProfile(Context context, String id, int subjectCount){
		final boolean[] matrix = new boolean[subjectCount];
		for(int i=0;i<matrix.length;i++) matrix[i] = false;
		updateProfile(context,id,0,0L,matrix);
	}

	/**
	 * 更新Profile
	 * @param id
	 * @param score
	 * @param time
	 * @param matrix
	 */
	private static void updateProfile(Context context, String id, int score, long time, boolean[] matrix){
		StringBuffer buffer = new StringBuffer("");
		buffer.append(String.valueOf(score));
		buffer.append(",");
		buffer.append(String.valueOf(time));
		buffer.append(",");
		for(boolean item : matrix){
			buffer.append(item ? 'R' : 'W');
		}
		SharedPreferences sp = context.getSharedPreferences(profileDatabase, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(id, buffer.toString());
		editor.commit();
	}

	/**
	 * 更新指定位置的对错矩阵
	 * @param id
	 * @param score
	 * @param time
	 * @param location
	 * @param rOrw
	 */
	public static void updateProfile(Context context, String id, int score, long time, int location, boolean rOrw){
		final boolean[] matrix = getRwMatrix(context,id);
		matrix[location] = rOrw;
		updateProfile(context,id,score,time,matrix);
	}

	/**
	 * 删除ID所在记录
	 * @param id
	 */
	public static void deleteProfile(Context context, String id){
		SharedPreferences sp = context.getSharedPreferences(profileDatabase, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(id);
		editor.commit();
	}
}
