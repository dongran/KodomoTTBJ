/**
 *   AnswerEntity - Entity of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.entity;

/**
 * 答案实体
 * @author sunchen (sunchen1221@gmail.com)
 * @data 2016-2-21
 * @version 1.1
 * <ul>
 * 		<li>1.0 创建</li>
 * 		<li>1.1 改setter方法，使之可以成为链式POJO</li>
 * </ul>
 */
public class AnswerEntity {

	/**
	 * 是否为正确选项
	 */
	private boolean correct = false;

	/**
	 * 是否被选择
	 * 这一项数据不保存到XML数据文件中
	 */
	private boolean selected = false;

	/**
	 * 答案内容
	 */
	private String content;

	public boolean isCorrect() {
		return correct;
	}

	public AnswerEntity setCorrect(boolean correct) {
		this.correct = correct;
		return this;
	}

	public boolean isSelected() {
		return selected;
	}

	public AnswerEntity setSelected(boolean selected) {
		this.selected = selected;
		return this;
	}

	public String getContent() {
		return content;
	}

	public AnswerEntity setContent(String content) {
		this.content = content;
		return this;
	}
}
