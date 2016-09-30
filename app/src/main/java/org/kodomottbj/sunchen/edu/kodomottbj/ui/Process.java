/**
 * Process - Acitivity of AndroidExam
 * Copyright (C) 2016  CFuture . Sun Chen
 */
package org.kodomottbj.sunchen.edu.kodomottbj.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.test.suitebuilder.TestMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.kodomottbj.sunchen.edu.kodomottbj.R;
import org.kodomottbj.sunchen.edu.kodomottbj.db.DBAccess;
import org.kodomottbj.sunchen.edu.kodomottbj.entity.AnswerEntity;
import org.kodomottbj.sunchen.edu.kodomottbj.entity.ProfileEntity;
import org.kodomottbj.sunchen.edu.kodomottbj.entity.SubjectEntity;
import org.kodomottbj.sunchen.edu.kodomottbj.util.AudioService;
import org.kodomottbj.sunchen.edu.kodomottbj.util.CommonSetting;
import org.kodomottbj.sunchen.edu.kodomottbj.util.FilpperCallback;
import org.kodomottbj.sunchen.edu.kodomottbj.util.MyViewFilpper;

import org.kodomottbj.sunchen.edu.kodomottbj.xml.ParseException;
import org.kodomottbj.sunchen.edu.kodomottbj.xml.XmlParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Process extends Activity implements FilpperCallback, OnClickListener {
    /**
     * 统计用到的成员变量
     */
    private long baseLine = 0L;
    private long elapsedTime = 0L;
    private int score = 0;
    private String fileId;
    private int totalSubjectCount = 0;
    private String currentFileName;
    private int hadAnsweredCount = 0;
    private int currentSubjectLocation = 0;
    private int currentDomain = 0;
    private int isTest = 0;
    private int curnum = 0;
    /**
     * 因跨成员方法使用数据而设置的组件引用
     */
    static private boolean isTimeout = false;
    static private int progressValue = 1;
    static private ProgressBar progressBar = null;
    private Thread myProgressThread = null;
    private Chronometer chronometer = null;
    private MyViewFilpper subjectFilpper = null;
    private List<SubjectEntity> subjects = null;
    private List<AnswerEntity> currentAnswerGroup = null;


    //	private MediaPlayer mediaPlayer = null;
    //audios 相关
    private String nullMp3Url = CommonSetting.SDCardDiretory + "audiosnull.mp3";
    private AudioService audioService;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            audioService = ((AudioService.LocalBinder) iBinder).getService();

            String mp3url = CommonSetting.SDCardDiretory + "audios" + subjects.get(currentSubjectLocation).getAudio() + ".mp3";
            if (audioService != null) {
                if (!mp3url.equals(nullMp3Url)) {
                    if (mp3url != null) {
                        audioService.PlayMp3(mp3url);
                    }
                }
            } else {
                Log.d("msg", "audioService is null1");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private Button skipButton = null;
    private Button answerButton = null;


    /**
     * 题目状态标识
     */
    private boolean isNewSubject = false;
    private boolean hadChosenAnswer = false;

    /**
     * 震动效果
     */
    private Vibrator vibrator;
    private long[] pattern = {10, 50};

    private int iconOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process);

        currentFileName = getIntent().getStringExtra(CommonSetting.FileNameTag);
        currentDomain = getIntent().getIntExtra("domain", -1);
        isTest = getIntent().getIntExtra("istest", 0);
        curnum = getIntent().getIntExtra("num", -1);
        vibrator = (Vibrator) getSystemService(ContextWrapper.VIBRATOR_SERVICE);
        fillHeader();
        fillSubjects();


        Intent bindIntent = new Intent(this, AudioService.class);
        //bindIntent.putExtra("audio0", subjects.get(0).getAudio());
        //startService(bindIntent);
        bindService(bindIntent, conn, BIND_AUTO_CREATE);


        //进度条
        progressBar.setMax(15);


    }

    /**
     * 填充考_卷信息头
     *
     * @param
     */
    private void fillHeader() {
        final TextView titleTextView = (TextView) findViewById(R.id.textpaperTitle);
        final TextView totalTimeTextView = (TextView) findViewById(R.id.testpaperInfoTotalTime);
        chronometer = (Chronometer) findViewById(R.id.testpaperInfoElapsedTime);
        final TextView totalScoreTextView = (TextView) findViewById(R.id.testpaperInfoTotalScore);
        progressBar = (ProgressBar) findViewById(R.id.testProgressBar);

        answerButton = (Button) findViewById(R.id.submitButton);
        skipButton = (Button) findViewById(R.id.skipButton);

        try {
            ProfileEntity pe = XmlParser.getProfile(new File(CommonSetting.SDCardDiretory + currentFileName));
            titleTextView.setText(String.format(getResources().getString(R.string.titleTag), pe.getTitle()));
            totalTimeTextView.setText(getResources().getString(R.string.infoTotalTimeTag) + pe.getTotalTime());
            totalScoreTextView.setText(getResources().getString(R.string.infoTotalScoreTag) + pe.getTotalScore());


            //可设置倒计时功能
            chronometer.setFormat(getResources().getString(R.string.infoElapsedTimeTag) + "%s");
            baseLine = SystemClock.elapsedRealtime();
            chronometer.setBase(baseLine);
            chronometer.start();


            fileId = String.valueOf(pe.getFileId());

        } catch (ParseException e) {
            Toast.makeText(this, getResources().getString(R.string.parseError), Toast.LENGTH_SHORT).show();
        }

        answerButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);


    }

    /**
     * 填充题目
     * <p>
     * 目前实现的是，把题目全部从XML文件中读出，保存到subjects引用中。
     * 题目动态生成，显示样式在R.layout.processitem中设置，由MyViewFilpper显示。
     * </p>
     */
    private void fillSubjects() {
        subjectFilpper = (MyViewFilpper) findViewById(R.id.subjectViewFilpper);
        try {
            subjects = XmlParser.getSubjects(new File(CommonSetting.SDCardDiretory + currentFileName));

        } catch (ParseException e) {
            Toast.makeText(this, getResources().getString(R.string.parseError), Toast.LENGTH_SHORT).show();
        }
        for (SubjectEntity se : subjects) {
            ++totalSubjectCount;
            LinearLayout item = new LinearLayout(Process.this);
            item.addView(View.inflate(Process.this, R.layout.processitem, null));
            final TextView sContentView = (TextView) item.findViewById(R.id.subjectContent);
            final TextView sIndexView = (TextView) item.findViewById(R.id.subjectIndex);
            //final TextView mp3UrlView_temp = (TextView) item.findViewById(R.id.mp3Url);

            final ImageView sImageView = (ImageView) item.findViewById(R.id.subjectImage);
            String myPngPath = CommonSetting.SDCardDiretory + "images" + se.getImage() + ".png";
            //String myPngPath = "/sdcard/androidexam/imagesA11p.png";

            String tempImgurl = CommonSetting.SDCardDiretory + "imagesnull.png";
            if (!myPngPath.equals(tempImgurl)) {
                //Log.d("mean", "zoule");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(myPngPath, options);
                sImageView.setImageBitmap(bm);
                sImageView.setVisibility(View.VISIBLE);
            }


            String okaudio = "audios" + se.getAudio();
            //Log.d("msg", se.getAudio());
            if (!okaudio.equals("audiosnull")) {
                ImageView view = (ImageView) findViewById(R.id.audiotip);
                view.setVisibility(View.VISIBLE);
            }else {
                ImageView view = (ImageView) findViewById(R.id.audiotip);
                view.setVisibility(View.GONE);
            }

            final String sContent = se.getContent();

            int fontsize = sContent.length() > getResources().getInteger(R.integer.subjectContentMaxLength)
                    ? getResources().getInteger(R.integer.subjectContentSmallFont)
                    : getResources().getInteger(R.integer.subjectContentNormalFont);
            sContentView.setTextSize(fontsize);

            sContentView.setText(sContent);
            //sContentView.setWidth(1200);
            sIndexView.setText(String.format(getResources().getString(R.string.subjectIndex), se.getIndex() + 1));


            LinearLayout answerLayout = (LinearLayout) item.findViewById(R.id.answerLayout);
            createAnswerView(answerLayout, se.getType(), se.getAnswers());
            //GridView answerLayout = (GridView)item.findViewById(R.id.answerLayout);
            //createAnswerView(answerLayout, se.getType(), se.getAnswers());

            subjectFilpper.addView(item);
        }
        //updateProgress(1);

        subjectFilpper.setTotalItemCounts(totalSubjectCount);//初始化滑动页面
        subjectFilpper.setCallback(this);
        DBAccess.createProfile(this, fileId, totalSubjectCount); //初始化一个试题数据

        //将Filpper功能加到左右按键上 真正的考试不需要
        /*ImageButton leftButton = (ImageButton)findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subjectFilpper.showPrevious();
			}
		});

		ImageButton rightButton = (ImageButton)findViewById(R.id.rightButton);
		rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subjectFilpper.showNext();
			}
		});*/
    }


    /**
     * 根据题目的类型，创建不同的答案。
     *
     * @param ll   答案区域的LinearLayout
     * @param type 答案类型
     */
    private void createAnswerView(LinearLayout ll, int type, List<AnswerEntity> answers) {
        switch (type) {
            case SubjectEntity.SINGLE:
                createRadioGroup(ll, answers);
                break;
            case SubjectEntity.MULTLCHECK:
                createMutilChecked(ll, answers);
                break;
            case SubjectEntity.TRUEORFALSE:
                createTrueOrFalse(ll, answers);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 当答题选项被选择时，此方法被调用
     * <p>
     * 在此方法中，修改当前题目的答案选择情况。
     * 标识是否为还没回答的题目。
     * </p>
     */
    @Override
    public void subjectChange(int location) {
        currentSubjectLocation = location;
        hadChosenAnswer = false;
        isNewSubject = true;
        currentAnswerGroup = subjects.get(currentSubjectLocation).getAnswers();
        // Log.d("msg", "getAudios: " + subjects.get(currentSubjectLocation).getAudio());
        // Log.d("msg","subjectchange");
        for (AnswerEntity ao : currentAnswerGroup) {
            if (ao.isSelected()) {
                //Log.d("debug", "我被选择了 ");
                hadChosenAnswer = true;
                isNewSubject = false;
            }
        }

        String mp3url = CommonSetting.SDCardDiretory + "audios" + subjects.get(currentSubjectLocation).getAudio() + ".mp3";
        if (audioService != null) {
            if (!mp3url.equals(nullMp3Url)) {
                if (mp3url != null) {
                    audioService.PlayMp3(mp3url);
                }
            }
        }

        progressBar.setProgress(0);
        progressValue = 1;
        if (myProgressThread == null) {
            myProgressThread = new Thread() {
                @Override
                public void run() {
                    try {
                        isTimeout = false;
                        while (progressValue <= 15) {
                            // 由线程来控制进度
                            //xh_pDialog.setProgress(xh_count++);
                            //Log.d("msg", progressValue + "");
                            Thread.sleep(1000);
                            //updateProgress(progressValue++);
                            progressBar.setProgress(progressValue++);
                        }
                        isTimeout = true;
                    } catch (Exception e) {
                        return;
                    }
                }
            };
            myProgressThread.start();
        }else {
            myProgressThread.interrupt();
            myProgressThread = null;
            myProgressThread = new Thread() {
                @Override
                public void run() {
                    try {
                        isTimeout = false;
                        while (progressValue <= 15) {
                            // 由线程来控制进度
                            //xh_pDialog.setProgress(xh_count++);
                           // Log.d("msg", progressValue + "");
                            Thread.sleep(1000);
                            //updateProgress(progressValue++);
                            progressBar.setProgress(progressValue++);
                        }
                        isTimeout = true;
                    } catch (Exception e) {
                        return;
                    }
                }
            };
            myProgressThread.start();
        }

    }


    /**
     * 创建单选答案组
     *
     * @param ll
     */
    private void createRadioGroup(LinearLayout ll, List<AnswerEntity> answers) {

        //View currentView = (View)Inflater.inflate(getResources().getLayout(R.layout.processitem));

        final RadioButton aItem0 = (RadioButton) ll.findViewById(R.id.radio0);
        final RadioButton aItem1 = (RadioButton) ll.findViewById(R.id.radio1);
        final RadioButton aItem2 = (RadioButton) ll.findViewById(R.id.radio2);
        final RadioButton aItem3 = (RadioButton) ll.findViewById(R.id.radio3);

        final String answerContent0 = answers.get(0).getContent();
        //RadioButton aItem0 = new RadioButton(Process.this);

        //Log.d("size",answerContent0);

        aItem0.setText(answerContent0);
        aItem0.setTag(0);
        aItem0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimeout) {
                    aItem0.setChecked(true);
                    aItem1.setChecked(false);
                    aItem2.setChecked(false);
                    aItem3.setChecked(false);
                    hadChosenAnswer = true;
                    for (AnswerEntity ae : currentAnswerGroup) {
                        ae.setSelected(false);
                    }
                    currentAnswerGroup.get((Integer) aItem0.getTag()).setSelected(true);
                } else {
                    hadChosenAnswer = true;
                    aItem0.setEnabled(false);
                    aItem1.setEnabled(false);
                    aItem2.setEnabled(false);
                    aItem3.setEnabled(false);
                    aItem0.setChecked(false);
                    aItem1.setChecked(false);
                    aItem2.setChecked(false);
                    aItem3.setChecked(false);

                }

            }
        });


        final String answerContent1 = answers.get(1).getContent();
        aItem1.setText(answerContent1);
        aItem1.setTag(1);
        aItem1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimeout) {
                    aItem1.setChecked(true);
                    aItem2.setChecked(false);
                    aItem3.setChecked(false);
                    aItem0.setChecked(false);
                    hadChosenAnswer = true;
                    for (AnswerEntity ae : currentAnswerGroup) {
                        ae.setSelected(false);
                    }
                    currentAnswerGroup.get((Integer) aItem1.getTag()).setSelected(true);
                } else {
                    hadChosenAnswer = true;
                    aItem0.setEnabled(false);
                    aItem1.setEnabled(false);
                    aItem2.setEnabled(false);
                    aItem3.setEnabled(false);
                    aItem0.setChecked(false);
                    aItem1.setChecked(false);
                    aItem2.setChecked(false);
                    aItem3.setChecked(false);

                }


            }
        });

        final String answerContent2 = answers.get(2).getContent();

        aItem2.setText(answerContent2);
        aItem2.setTag(2);
        aItem2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimeout) {
                    aItem2.setChecked(true);
                    aItem3.setChecked(false);
                    aItem0.setChecked(false);
                    aItem1.setChecked(false);
                    hadChosenAnswer = true;
                    for (AnswerEntity ae : currentAnswerGroup) {
                        ae.setSelected(false);
                    }
                    currentAnswerGroup.get((Integer) aItem2.getTag()).setSelected(true);
                } else {
                    hadChosenAnswer = true;
                    aItem0.setEnabled(false);
                    aItem1.setEnabled(false);
                    aItem2.setEnabled(false);
                    aItem3.setEnabled(false);
                    aItem0.setChecked(false);
                    aItem1.setChecked(false);
                    aItem2.setChecked(false);
                    aItem3.setChecked(false);

                }
            }
        });

        final String answerContent3 = answers.get(3).getContent();

        aItem3.setText(answerContent3);
        aItem3.setTag(3);
        aItem3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimeout) {
                    aItem3.setChecked(true);
                    aItem0.setChecked(false);
                    aItem1.setChecked(false);
                    aItem2.setChecked(false);
                    hadChosenAnswer = true;
                    for (AnswerEntity ae : currentAnswerGroup) {
                        ae.setSelected(false);
                    }
                    currentAnswerGroup.get((Integer) aItem3.getTag()).setSelected(true);
                } else {
                    hadChosenAnswer = true;
                    aItem0.setEnabled(false);
                    aItem1.setEnabled(false);
                    aItem2.setEnabled(false);
                    aItem3.setEnabled(false);
                    aItem0.setChecked(false);
                    aItem1.setChecked(false);
                    aItem2.setChecked(false);
                    aItem3.setChecked(false);

                }
            }
        });
    }

    /**
     * 创建多项选择答案组
     *
     * @param ll
     */
    private void createMutilChecked(LinearLayout ll, List<AnswerEntity> answers) {
        ll.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < answers.size(); i++) {
            final CheckBox item = new CheckBox(this);
            item.setText(answers.get(i).getContent());
            item.setTag(i);
            item.setButtonDrawable(R.drawable.mycheck);
            item.setPadding(iconOffset, 0, 0, 0);
            int fontSize = answers.get(i).getContent().length() > getResources().getInteger(R.integer.answerContentMaxLength)
                    ? getResources().getInteger(R.integer.answerContentNormalFont)
                    : getResources().getInteger(R.integer.answerContentSmallFont);
            item.setTextSize(fontSize);
            item.setTextColor(getResources().getColor(R.color.processTextColor));
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setChecked(item.isChecked());
                    hadChosenAnswer = item.isChecked();
                    currentAnswerGroup.get((Integer) item.getTag()).setSelected(true);
                }
            });
            ll.addView(item);
        }
    }

    /**
     * 创建判断题答案组
     *
     * @param ll
     */
    private void createTrueOrFalse(LinearLayout ll, List<AnswerEntity> answers) {
        RadioGroup rg = new RadioGroup(Process.this);
        RadioButton rBtn = null;
        RadioButton wBtn = null;

        for (int i = 0; i < answers.size(); i++) {
            final RadioButton rb = new RadioButton(this);
            rb.setTag(i);
            rb.setPadding(55, 15, 0, 20);
            rb.setText(answers.get(i).getContent());
            rb.setTextColor(getResources().getColor(R.color.processTextColor));
            int fontSize = answers.get(i).getContent().length() > getResources().getInteger(R.integer.answerContentMaxLength)
                    ? getResources().getInteger(R.integer.answerContentNormalFont)
                    : getResources().getInteger(R.integer.answerContentSmallFont);
            rb.setTextSize(fontSize);
            if (answers.get(i).isCorrect()) {
                rBtn = rb;
                rb.setButtonDrawable(R.drawable.radiobutton_yes);
            } else {
                wBtn = rb;
                rb.setButtonDrawable(R.drawable.radiobutton_no);
            }
            rb.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    rb.setChecked(true);
                    hadChosenAnswer = true;
                    for (AnswerEntity ae : currentAnswerGroup) {
                        ae.setSelected(false);
                    }
                    currentAnswerGroup.get((Integer) rb.getTag()).setSelected(true);
                }
            });
        }
        rg.addView(rBtn);
        rg.addView(wBtn);
        ll.addView(rg);
    }

    /**
     * 答题按钮
     * 点击答题时，此方法被调用。每回答一题，答题进度条就增加。
     * 当答完全部题目时，会自动跳转到ProfileActivity中，显示
     * 考卷的简报信息。
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitButton:
                if (hadChosenAnswer) {

                    hadAnsweredCount++;

                    //if (!isNewSubject) hadAnsweredCount++;
                    vibrator.vibrate(pattern, -1);


                    boolean answerFlag = true;
                    for (AnswerEntity ao : currentAnswerGroup) {
                        answerFlag &= ao.isCorrect() == ao.isSelected();
                    }
                    if (answerFlag) {
                        score += subjects.get(currentSubjectLocation).getValue();
                    }
                    long usedTime = (elapsedTime += SystemClock.elapsedRealtime() - baseLine) / 1000;
                    DBAccess.updateProfile(this, fileId, score, usedTime, currentSubjectLocation, answerFlag);

                    if (hadAnsweredCount < totalSubjectCount) {
                        subjectFilpper.showNext();
                        //answerButton.setEnabled(true);
                        //进度条重新初始化

                    } else {
                        if (isTest == 1) {
                            Intent intent = new Intent(this, Profile.class);
                            intent.putExtra("domain", currentDomain);
                            intent.putExtra(CommonSetting.FileNameTag, currentFileName);
                            intent.putExtra("num", curnum);
                            startActivity(intent);
                            finish();
                        } else {
                            if (currentDomain == 0) {
                                Intent intent = new Intent(this, SpotActivity.class);
                                startActivity(intent);
                            } else if (currentDomain == 1) {
                                Intent intent = new Intent(this, SpotMojiActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(this, MojiActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
                break;
            case R.id.skipButton:
                break;
        }


    }

    /**
     * 检测按键
     * <br/>
     * 当前界面是考试状态，如果退出的话，需要保存考试状态。
     * 检测返回键是否被按下，如果按下，弹出退出提示框，让用户确认是否退出。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showBackTip();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示退出提示框
     */
    private void showBackTip() {
        final AlertDialog alertDialog = new AlertDialog.Builder(Process.this)
                .setTitle(R.string.exitTitle).setMessage(R.string.exitTip)
                .setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Process.this.finish();
                    }
                }).setNegativeButton(R.string.buttonCancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create();
        alertDialog.show();
    }


    /**
     * 设置margin方法
     */
    private static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * 更新进度条
     *
     * @param progress
     */
    private void updateProgress(int progress) {
        //progressBar.setProgress(Math.round((float)progress/totalSubjectCount*100));
        progressBar.setProgress(progress);
    }

    /**
     * 当Activity失去焦点时，此方法被调用。
     * 在此方法中，把计时暂停。
     */
    @Override
    protected void onPause() {
        super.onPause();
        elapsedTime += SystemClock.elapsedRealtime() - baseLine;
        baseLine = SystemClock.elapsedRealtime();
        chronometer.stop();
    }

    /**
     * 当Activity重新取得焦点时，此方法被调用。
     * 在此方法中，计时继续。
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        chronometer.setBase(baseLine);
        chronometer.start();
    }

}
