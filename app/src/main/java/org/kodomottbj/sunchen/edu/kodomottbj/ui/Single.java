/**
 *   Single - Acitivity of AndroidExam
 *   Copyright (C) 2016  CFuture . Sun Chen
 *
 */
package org.kodomottbj.sunchen.edu.kodomottbj.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.kodomottbj.sunchen.edu.kodomottbj.R;
import org.kodomottbj.sunchen.edu.kodomottbj.entity.AnswerEntity;
import org.kodomottbj.sunchen.edu.kodomottbj.entity.SubjectEntity;
import org.kodomottbj.sunchen.edu.kodomottbj.util.CommonSetting;
import org.kodomottbj.sunchen.edu.kodomottbj.xml.ParseException;
import org.kodomottbj.sunchen.edu.kodomottbj.xml.XmlParser;

import java.io.File;

public class Single extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single);
		String currentFileName = getIntent().getStringExtra(CommonSetting.FileNameTag);
		int subjectLocation = getIntent().getIntExtra(CommonSetting.SubjectLocationTag, 0);
		try {
			updateSingle(currentFileName,subjectLocation);
		} catch (ParseException e) {
			Toast.makeText(this,getResources().getString(R.string.parseError), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 更新
	 * @throws ParseException
	 */
	private void updateSingle(String fileName, int location) throws ParseException{
		SubjectEntity single = XmlParser.getSubject(
				new File(CommonSetting.SDCardDiretory + fileName), location);
		final TextView singleIndexView = (TextView)findViewById(R.id.singleIndex);
		final TextView singleContentView = (TextView)findViewById(R.id.singleContent);
		final LinearLayout singleAnswersLayout = (LinearLayout)findViewById(R.id.singleAnswerLayout);
		final TextView singleAnswerAnalysis = (TextView)findViewById(R.id.singleAnswerAnalysis);

		singleIndexView.setText(String.format(getResources().getString(R.string.subjectIndex), single.getIndex()+1));
		final String sContent = single.getContent();
		singleContentView.setText(sContent);
		int fontsize = sContent.length() > getResources().getInteger(R.integer.subjectContentMaxLength)
				? getResources().getInteger(R.integer.subjectContentSmallFont)
				: getResources().getInteger(R.integer.subjectContentNormalFont);
		singleContentView.setTextSize(fontsize);
		singleAnswerAnalysis.setText(single.getAnalysis());

		RadioGroup rg = new RadioGroup(this);
		for(AnswerEntity ae : single.getAnswers()){
			final RadioButton rb = new RadioButton(this);
			rb.setText(ae.getContent());
			rb.setButtonDrawable(R.drawable.true_uncheck);
			rb.setTextSize(getResources().getInteger(R.integer.answerContentSmallFont));
			rb.setClickable(false);
			if(ae.isCorrect()){
				rb.setTextColor(R.color.correctAnswerColor);
				rb.setButtonDrawable(R.drawable.true_check);
			}else{
				if(single.getType() == SubjectEntity.TRUEORFALSE){
					continue;
				}
				rb.setTextColor(R.color.processTextColor);
			}
			rg.addView(rb);
		}
		singleAnswersLayout.addView(rg);
	}
}
