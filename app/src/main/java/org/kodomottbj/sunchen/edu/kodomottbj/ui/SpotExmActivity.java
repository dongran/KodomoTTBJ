package org.kodomottbj.sunchen.edu.kodomottbj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import org.kodomottbj.sunchen.edu.kodomottbj.R;
import org.kodomottbj.sunchen.edu.kodomottbj.util.CommonSetting;

/**
 * Created by SCI01628 on 2016/08/09.
 */

public class SpotExmActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.testexam_view);

        Button testYAB = (Button) findViewById(R.id.testYAB);
        testYAB.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotExmActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "spot0.xml");
                it.putExtra("domain",0);
                it.putExtra("istest", 1);
                it.putExtra("num", 0);
                startActivity(it);
            }

        });
        Button testYC = (Button) findViewById(R.id.testYC);
        testYC.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotExmActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "spot1.xml");
                it.putExtra("domain",0);
                it.putExtra("istest", 1);
                it.putExtra("num", 1);
                startActivity(it);
            }

        });
        Button Yback = (Button) findViewById(R.id.Yback);
        Yback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotExmActivity.this, SpotActivity.class);
                startActivity(it);
            }

        });
    }
}
