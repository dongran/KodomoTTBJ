/**
 *   ProfileEntity - Entity of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.entity;

/**
 * 考卷简报
 * <br/>
 * 描述考卷的信息
 * @author sunchen (sunchen1221@gmail.com)
 * @data 2016-2-21
 * @version 1.3
 * 	<ul>
 * 		<li> 1.1 : 
 * 			标识考卷得分，考卷用时，考卷对错矩阵为删除状态。
 * 			修改String fName为long fileId。文件的唯一标识。
 * 		</li>
 * 		<li>
 * 			1.2:
 * 			添加文件名，以方便查看文件
 * 		</li>
 * 		<li>
 * 			1.3:
 * 			读了一篇博文，博主推崇JQuery的链式POJO，我深以为然。于是，直接上代码。
 * 		</li>
 *  </ul>
 */
public class ProfileEntity {
	/**
	 * 考卷标题
	 */
	private String title = null;

	/**
	 * 考卷时间
	 */
	private int totalTime = 90;

	/**
	 * 考卷总分
	 */
	private int totalScore = 100;

	/**
	 * 考卷题目数
	 */
	private int subjectCount = 75;

	/**
	 * 得分
	 * 此数据不属于Profile内容，建议保存到数据库中。
	 */
	@Deprecated
	private int score = 0;

	/**
	 * 用时
	 * 此数据不属于Profile内容，建议保存到数据库中。
	 */
	@Deprecated
	private long elapsedTime = 0;

	/**
	 * 合格分数
	 */
	private int passingScore = 0;

	/**
	 * 考卷描述
	 */
	private String description  = null;

	/**
	 * 考卷创建者
	 */
	private String creator = null;

	/**
	 * 对错矩阵
	 * <i>
	 * 	此矩阵用于记录考卷的考试状态，但是每次更新此矩阵都需要整个考卷删除再生成，过于浪费资源。
	 *  所以标识删除，以待后面版本中修改。建议使用SharePerformance或者数据库进行保存。字段fileId是
	 *  特地为此而创建。
	 * </i>
	 */
	@Deprecated
	private String rwMatrix = null;

	/**
	 * 文件标识ID
	 * <br/>
	 * 标识此文件，在创建考卷时生成。
	 * Modify by chenyoca at 2011-10-21 23:43
	 */
	private long fileId = Math.round(1084L);

	/**
	 * 文件名
	 */
	private String fileName = null;

	/**
	 *
	 * 小图标标识
     */
    private int imgId = 0;

	public int getImgId(){
		return imgId;
	}

	public ProfileEntity setImgId(int imgId){
		this.imgId = imgId;
		return this;
	}


	public String getFileName() {
		return fileName;
	}

	public ProfileEntity setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public boolean finish(){
		return !rwMatrix.contains("N");
	}

	@Deprecated
	public void updateRWMatrix(int index,boolean wORr){
		final char[] array = rwMatrix.toCharArray();
		array[index] = (wORr ? 'R' : 'W');
		rwMatrix = new String(array);
	}

	public String getTitle() {
		return title;
	}

	public ProfileEntity setTitle(String title) {
		this.title = title;
		return this;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public ProfileEntity setTotalTime(int totalTime) {
		this.totalTime = totalTime;
		return this;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public ProfileEntity setTotalScore(int totalScore) {
		this.totalScore = totalScore;
		return this;
	}

	public int getSubjectCount() {
		return subjectCount;
	}

	public ProfileEntity setSubjectCount(int subjectCount) {
		this.subjectCount = subjectCount;
		return this;
	}

	@Deprecated
	public int getScore() {
		return score;
	}

	@Deprecated
	public void setScore(int score) {
		this.score = score;
	}

	@Deprecated
	public long getElapsedTime() {
		return elapsedTime;
	}

	@Deprecated
	public void setElapsedTime(long elapseTime) {
		this.elapsedTime = elapseTime;
	}

	public int getPassingScore() {
		return passingScore;
	}

	public ProfileEntity setPassingScore(int passingScore) {
		this.passingScore = passingScore;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public ProfileEntity setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getCreator() {
		return creator;
	}

	public ProfileEntity setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	@Deprecated
	public String getRwMatrix() {
		return rwMatrix;
	}

	@Deprecated
	public void setRwMatrix(String rwMatrix) {
		this.rwMatrix = rwMatrix;
	}

	public long getFileId() {
		return fileId;
	}

	public ProfileEntity setFileId(long fileId) {
		this.fileId = fileId;
		return this;
	}
}
