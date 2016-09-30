/**
 *   PaperEntity - Entity of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.entity;

import java.util.List;

/**
 * 考卷实体类
 * <br/>
 * 考卷包含两部分数据：考卷简报，题目列表
 * @author sunchen (sunchen1221@gmail.com)
 * @data 2016-2-21
 * @version 1.0
 */
public class PaperEntity {
	//简报
	public ProfileEntity profile;
	//题目列表
	public List<SubjectEntity> subjects;
}
