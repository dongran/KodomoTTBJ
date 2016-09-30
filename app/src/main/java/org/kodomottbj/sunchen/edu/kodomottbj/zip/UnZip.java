/**
 *   UnZip - UnZip Util of AndroidExam
 *   Copyright (C) 2011  CFuture . Chen Yong Jia
 *
 * 	 This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.

 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.zip;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip {
	private static final int bufferSize = 5210;

	/**
	 * 解压缩zip文件到指定目录
	 * @param zipFile 压缩zip文件
	 * @param targetDir 目标文件夹
	 * @throws ZipException 解压缩出错则抛出异常
	 */
	public static void unzip(String zipFile, String targetDir) throws ZipException{
		try{
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zipEntris = new ZipInputStream(new BufferedInputStream(fis));
			while(true){
				ZipEntry entry = zipEntris.getNextEntry();
				if(entry == null) break;
				byte cache[] = new byte[bufferSize];
				String strEntry = entry.getName();
				File fileInZip = new File(targetDir + strEntry);
				FileOutputStream fos = new FileOutputStream(fileInZip);
				BufferedOutputStream targetCache = new BufferedOutputStream(fos, bufferSize);
				int length = 0;
				while ((length = zipEntris.read(cache, 0, bufferSize)) != -1) {
					targetCache.write(cache, 0, length);
				}
				targetCache.flush();
				targetCache.close();
			}
			zipEntris.close();
		}catch (Exception ex){
			Log.e("AndroidExam",ex.getMessage());
			throw new ZipException(ex);
		}
	}

	/**
	 * 解压缩到当前目录
	 * @param filepath 压缩zip文件
	 * @throws ZipException 解压缩出错则抛出异常
	 */
	public static void unzip(String filepath) throws ZipException{
		unzip(filepath,filepath.substring(0,filepath.lastIndexOf("/")+1));
	}
}
