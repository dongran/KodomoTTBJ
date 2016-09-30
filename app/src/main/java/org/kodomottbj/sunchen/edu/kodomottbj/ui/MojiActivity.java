package org.kodomottbj.sunchen.edu.kodomottbj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import org.kodomottbj.sunchen.edu.kodomottbj.MenuActivity;
import org.kodomottbj.sunchen.edu.kodomottbj.R;
import org.kodomottbj.sunchen.edu.kodomottbj.util.CommonSetting;

/**
 * Created by SunChen on 16/8/9.
 */
public class MojiActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.moji_level_view);

        Button moji12 = (Button) findViewById(R.id.moji12);
        moji12.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(MojiActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "moji0.xml");
                it.putExtra("domain",2);
                it.putExtra("istest", 0);
                startActivity(it);
            }

        });
        Button moji34 = (Button) findViewById(R.id.moji34);
        moji34.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(MojiActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "moji1.xml");
                it.putExtra("domain",2);
                it.putExtra("istest", 0);
                startActivity(it);
            }

        });

        Button moji56 = (Button) findViewById(R.id.moji56);
        moji56.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(MojiActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "moji2.xml");
                it.putExtra("domain",2);
                it.putExtra("istest", 0);
                startActivity(it);
            }

        });

        Button mojiback = (Button) findViewById(R.id.mojiback);
        mojiback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(MojiActivity.this, MenuActivity.class);
                startActivity(it);
            }

        });

    }
}
