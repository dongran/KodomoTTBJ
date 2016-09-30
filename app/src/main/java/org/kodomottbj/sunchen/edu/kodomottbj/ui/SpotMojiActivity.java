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
public class SpotMojiActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mojispot_level_view);


        Button mojispor1 = (Button) findViewById(R.id.mojispor1);
        mojispor1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotMojiActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "KSPOT1.xml");
                it.putExtra("domain",1);
                it.putExtra("istest", 0);
                startActivity(it);
            }

        });
        Button mojispor2 = (Button) findViewById(R.id.mojispor2);
        mojispor2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotMojiActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "KSPOT2.xml");
                it.putExtra("domain",1);
                it.putExtra("istest", 0);
                startActivity(it);
            }

        });

        Button mojisporback = (Button) findViewById(R.id.mojisporback);
        mojisporback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotMojiActivity.this, MenuActivity.class);
                startActivity(it);
            }

        });
    }
}
