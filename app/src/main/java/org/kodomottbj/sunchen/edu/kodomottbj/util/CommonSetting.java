/**
 *   SETTING - Setting of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.util;

public interface CommonSetting {
	String SDCardDiretory = "/sdcard/kodomoTTBJ/";
	String XMLPostfix = ".xml";
	int SendDownload = 1083;
	int RecvDownload = 1084;
	String DownloadSuccess = "SUCCESS";
	String DownloadFailed = "FAILED";
	String DownloadSuccessTag = "RS";
	String FileNameTag = "fileNameTag";
	String SubjectLocationTag = "location";
	String QueryURL = "http://server.chenyoca.com/androidexam/downloadlist.html";
	String CheckVersionURL = "http://kaken.sakura.ne.jp/kodomoTTBJ/index.php/Admin/Index/checkVersion";
	String UserAgent = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";
}
