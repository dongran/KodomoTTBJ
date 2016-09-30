/**
 *   SubjectEntity - Entity of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 题目实体
 * <br/>
 * @author sunchen (sunchen1221@gmail.com)
 * @data 2016-2-21
 * @version 1.1
 * <ul>
 * 		<li>1.0 创建</li>
 * 		<li>1.1 改setter方法，使之可以成为链式POJO</li>
 * </ul>
 */
public class SubjectEntity {
	/**
	 * 单选题
	 */
	public static final int SINGLE = 0;

	/**
	 * 多选题
	 */
	public static final int MULTLCHECK = 1;

	/**
	 * 判断题目
	 */
	public static final int TRUEORFALSE = 2;


	/**
	 * 题目序号
	 */
	private int index;

	/**
	 * 题目类型
	 */
	private int type;

	/**
	 * 分值
	 */
	private int value;

	/**
	 * 图片数据
	 */
	private String image;

	/**
	 * 音频数据
	 */
	private String audio;


	/**
	 * 题目内容
	 */
	private String content;

	/**
	 * 答案解释
	 */
	private String analysis;

	/**
	 * 答案列表
	 */
	private List<AnswerEntity> answers = new ArrayList<AnswerEntity>();

	public int getIndex() {
		return index;
	}

	public SubjectEntity setIndex(int index) {
		this.index = index;
		return this;
	}

	public int getType() {
		return type;
	}

	public SubjectEntity setType(int type) {
		this.type = type;
		return this;
	}

	public String getImage() {
		return image;
	}

	public SubjectEntity setImage(String image) {
		this.image = image;
		return this;
	}

	public String getAudio() {
		return audio;
	}

	public SubjectEntity setAudio(String audio) {
		this.audio = audio;
		return this;
	}

	public String getContent() {
		return content;
	}

	public SubjectEntity setContent(String content) {
		this.content = content;
		return this;
	}

	public String getAnalysis() {
		return analysis;
	}

	public SubjectEntity setAnalysis(String analysis) {
		this.analysis = analysis;
		return this;
	}

	public List<AnswerEntity> getAnswers() {
		return answers;
	}

	public SubjectEntity setAnswers(List<AnswerEntity> answers) {
		this.answers = answers;
		return this;
	}

	public int getValue() {
		return value;
	}

	public SubjectEntity setValue(int value) {
		this.value = value;
		return this;
	}
}
