/**
 *   XmlParse - Util of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.xml;

import android.util.Log;
import android.util.Xml;

import org.kodomottbj.sunchen.edu.kodomottbj.entity.AnswerEntity;
import org.kodomottbj.sunchen.edu.kodomottbj.entity.ProfileEntity;
import org.kodomottbj.sunchen.edu.kodomottbj.entity.SubjectEntity;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 利用SAX引擎处理XML文件解析类，用于转换XML文件到Object对象。
 * <br/>
 * 其核心是继承DefaultHandler类，通过SAX引擎逐个标签解析时回调endElement()方法，
 * 在遇到指定标签时创建Object的值。
 *
 * @author sunchen (sunchen1221@gmail.com)
 * @data 2016-2-21
 * @version 1.6
 * 		<li> 1.0 创建</li>
 * 		<li> 1.1 见{@link ProfileParser}修改部分</li>
 * 		<li> 1.2 为每个考卷记录文件名</li>
 * 		<li> 1.3 将全部静态方法修改为非静态方法</li>
 * 		<li> 1.4 添加文件后缀验证</li>
 * 		<li> 1.5 
 * 					<br/>1、把全部类都设为Final。
 * 					<br/>2、把异常全部压制在类内，如果解析XML文件出现异常，返回null。
 * 		</li>
 * 		<li> 1.6 把方法改回静态方法。经过测试，静态方法与非静态方法内存占用相关甚小。
 * 						此类为工具类，方法之间独立，为方便使用，把方法都改回静态方法。</li>
 * 		<li> 1.7 今天跟朋友讨论了一下，我这样把异常压制住是不合理的。出现异常，类内能处理则处理，
 * 				不能处理就得往上层抛出，由上层处理。
 * 		</li>
 */
final public class XmlParser {

	private static final String LOG_TAG = "AndroidExam";
	/**
	 * 读取指定文件的Profile数据
	 * @param pf
	 * 			需要读取Profile的文件对象，假定这个文件对象是一个XML格式的文件。
	 * @return
	 * 			读取并解析文档成功时返回ProfileEntity对象。
	 * @throws ParseException
	 * 			解析XML文档出现错误时，抛出异常。
	 */
	public static ProfileEntity getProfile(File pf) throws ParseException{
		final ProfileParser profileParser = new ProfileParser();
		try {
			parseXml(pf,profileParser);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error of Parser : "+e.getMessage());
			throw new ParseException(e.getMessage());
		}
		return profileParser.getProfile();
	}

	/**
	 * 加载目录下的考卷信息，以ProfileEntity对象列表方式返回。
	 * @param dir
	 * 			目录
	 * @return
	 * 			如果目录下没有考卷文件，返回NULL。
	 * @throws ParseException
	 * 			解析XML文档出现错误时，抛出异常。
	 *
	 * @version 1.2
	 * 	<ul>
	 * 		<li>1.0 创建</li>
	 * 		<li>1.1 判断传入参数不是目录的判断</li>
	 * 		<li>1.2 根据ITEye网友@zhao_chong的建议，把结果List用引用传入，出现异常时，在方法最后返回。</li>
	 *  </ul> 
	 */
	public static void getProfiles(List<ProfileEntity> plist , String dir) throws ParseException{
		File dirFile  = new File(dir);
		if(dirFile.isFile()) dirFile = dirFile.getParentFile();
		final File[] fList = dirFile.listFiles();
		boolean errorFlag = false;
		StringBuffer errorMesg = new StringBuffer();
		if(fList != null){
			for(File f : fList){
				if(!f.getName().endsWith(".xml")) continue;
				try {
					ProfileEntity pe = getProfile(f);
					pe.setFileName(f.getName());
					plist.add(pe);
				} catch (ParseException e) {
					String mesg = "Error of Parser in : "+f.getName()+" ! Caused by "+e.getMessage();
					Log.e(LOG_TAG, mesg);
					errorMesg.append(mesg+"[,]");
					errorFlag = true;
					continue;
				}
			}
		}
		if(errorFlag) throw new ParseException(errorMesg.toString());
	}

	/**
	 * 加载指定文件的题目列表，以SubjectEntity对象列表返回。
	 * @param   file
	 * 			需要读取题目的文件
	 * @return
	 * 			如果解析出错或者没有题目，则返回Null。
	 * @throws ParseException
	 * 			解析XML文档出现错误时，抛出异常。
	 */
	public static List<SubjectEntity> getSubjects(File sf)throws ParseException{
		final SubjectParser subjectParser = new SubjectParser();
		try{
			parseXml(sf,subjectParser);
		}catch(Exception e){
			Log.e(LOG_TAG, "Error of Parser : "+e.getMessage());
			throw new ParseException(e.getMessage());
		}
		return subjectParser.getSubjects();
	}

	/**
	 * 取得指定文件中location位置的题目
	 * @param file
	 * 		需要读取题目的文件
	 * @param location
	 * 		题目位置，下标从0开始。
	 * @return 下标被自动限制在0到列表最大数量之间。如果列表内没有数据，返回null。
	 * @throws ParseException
	 * 		解析XML文档出现错误时，抛出异常。
	 */
	public static SubjectEntity getSubject(File file, int location) throws ParseException{
		final List<SubjectEntity> list = getSubjects(file);
		location = Math.min(list.size()-1, Math.max(0, location));
		return list == null ? null : list.get(location);
	}

	/**
	 * 执行XML解析的类
	 * <br/>
	 *
	 * @param file
	 * 			需要解析的文件
	 * @param xml
	 * 			回调进行处理的解析类
	 * @throws IOException
	 * 			文件打开失败，或者文件不存在时，此异常被抛出。
	 * @throws SAXException
	 * 			SAX引擎解析出错时，此异常被抛出
	 */
	private static void parseXml(File file, DefaultHandler xml) throws SAXException,IOException {
		android.util.Xml.parse(new FileInputStream(file), Xml.Encoding.UTF_8,xml);
	}

	/**
	 * 执行题目解析的类
	 * SAX引擎对XML文件解析时，采用这种方式进行解析：遇到标签时就回调startElement()方法;
	 * 当遇到标签结束时，就回调endElement()方法。
	 *
	 * @author sunchen (sunchen1221@gmail.com)
	 * @data 2016-2-21
	 * @version 1.0
	 */
	final static private class SubjectParser extends DefaultHandler {
		private List<SubjectEntity> slist = null;
		private StringBuffer buffer = new StringBuffer();
		private AnswerEntity tempAnswerRel = null;
		private List<AnswerEntity> tempAnswerListRel = null;
		private SubjectEntity tempSubjectRel = null;
		@Override
		public void startElement(String uri, String localName, String qName,
								 Attributes attributes) throws SAXException {
			if(localName.equals("subjects")){
				slist = new ArrayList<SubjectEntity>();
			}if(localName.equals("subject")){
				tempSubjectRel = new SubjectEntity();
			}else if(localName.equals("answers")){
				tempAnswerListRel = new ArrayList<AnswerEntity>();
			}else if(localName.equals("answer")){
				tempAnswerRel = new AnswerEntity();
				tempAnswerRel.setSelected(false);
			}
			super.startElement(uri, localName, qName, attributes);
		}
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			buffer.append(ch,start,length);
			super.characters(ch, start, length);
		}
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			boolean cleanBuffer = true;
			if(localName.equals("subject")){
				slist.add(tempSubjectRel);
				cleanBuffer = false;
			}else if(localName.equals("index")){
				tempSubjectRel.setIndex(Integer.parseInt(buffer.toString().trim()));
			}else if(localName.equals("type")){
				tempSubjectRel.setType(Integer.parseInt(buffer.toString().trim()));
			}else if(localName.equals("value")){
				tempSubjectRel.setValue(Integer.parseInt(buffer.toString().trim()));
			}else if(localName.equals("image")) {
				tempSubjectRel.setImage(buffer.toString().trim());
			}else if (localName.equals("audioid")){
				tempSubjectRel.setAudio(buffer.toString().trim());
			}else if(localName.equals("subjectContent")){
				tempSubjectRel.setContent(buffer.toString().trim());
			}else if(localName.equals("analysis")){
				tempSubjectRel.setAnalysis(buffer.toString().trim());
			}else if(localName.equals("answers")){
				tempSubjectRel.setAnswers(tempAnswerListRel);
			}else if(localName.equals("answer")){
				tempAnswerListRel.add(tempAnswerRel);
			}else if(localName.equals("correct")){
				tempAnswerRel.setCorrect(Boolean.parseBoolean(buffer.toString().trim()));
			}else if(localName.equals("content")){
				tempAnswerRel.setContent(buffer.toString().trim());
			}
			if(cleanBuffer) buffer.setLength(0);
			super.endElement(uri, localName, qName);
		}

		public List<SubjectEntity> getSubjects(){
			return slist;
		}
	}

	/**
	 * 执行Profile解析的类
	 * SAX引擎对XML文件解析时，采用这种方式进行解析：遇到标签时就回调startElement()方法;
	 * 当遇到标签结束时，就回调endElement()方法。
	 *
	 * @author chenyoca (chenyoca@163.com)
	 * @data 2011-10-21
	 * @version 1.1
	 * <br/>
	 * 		<li>version 1.0 创建</li>
	 * 		<li>version 1.1 删除score 、elapsedTime、rwMatrix等方法。添加fileId方法</li>
	 *
	 */
	final static private class ProfileParser extends DefaultHandler {
		private ProfileEntity profile = null;
		private StringBuffer buffer = new StringBuffer();

		@Override
		public void startElement(String uri, String localName, String qName,
								 Attributes attributes) throws SAXException {
			if(localName.equals("profile")){
				profile = new ProfileEntity();
			}
			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			buffer.append(ch,start,length);
			super.characters(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			boolean cleanBuffer = true;
			if(localName.equals("profile")){
				cleanBuffer = false;
			}else if(localName.equals("title")) {
				profile.setTitle(buffer.toString().trim());
			}else if(localName.equals("imgId")){
				profile.setImgId(Integer.parseInt(buffer.toString().trim()));
			}else if(localName.equals("totalTime")){
				profile.setTotalTime(Integer.parseInt(buffer.toString().trim()));
			}else if(localName.equals("totalScore")){
				profile.setTotalScore(Integer.parseInt(buffer.toString().trim()));
			}else if(localName.equals("subjectCount")){
				profile.setSubjectCount(Integer.parseInt(buffer.toString().trim()));
			}else if(localName.equals("passingScore")){
				profile.setPassingScore(Integer.parseInt(buffer.toString().trim()));
			}else if(localName.equals("descriptions")){
				profile.setDescription(buffer.toString().trim());
			}else if(localName.equals("creator")){
				profile.setCreator(buffer.toString().trim());
			}else if(localName.equals("fileId")){
				profile.setFileId(Long.parseLong(buffer.toString().trim()));

			}
			if(cleanBuffer) buffer.setLength(0);
			super.endElement(uri, localName, qName);
		}

		public ProfileEntity getProfile(){
			return profile;
		}
	}
}
