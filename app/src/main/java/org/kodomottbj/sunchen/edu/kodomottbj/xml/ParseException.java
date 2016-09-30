/**
 *   ParseExcption - Exception of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.xml;

/**
 * 解析文档异常类
 * @author sunchen (sunchen1221@gmail.com)
 * @data 2016-2-21
 */
public class ParseException extends Exception {
	private static final long serialVersionUID = -3824561947913147233L;
	private String mesg;
	private static final String ERR_TIP = "Parseing XML doc failure ! Caused by ";

	public ParseException(){}

	public ParseException(String mesg){
		this.mesg = mesg;
	}

	public ParseException(Exception e){
		this.mesg = e.getMessage();
	}

	@Override
	public String getMessage() {
		return ERR_TIP + mesg;
	}
	@Override
	public String toString(){
		return getMessage();
	}
}
