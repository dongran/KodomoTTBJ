package org.kodomottbj.sunchen.edu.kodomottbj.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
public class SpotActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.spot_level_view);


       /* Button testSpot = (Button) findViewById(R.id.testSpot);
        testSpot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotActivity.this, SpotExmActivity.class);
                startActivity(it);
            }

            });*/
        Button YA = (Button) findViewById(R.id.YA);
        YA.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "spot0.xml");
                it.putExtra("domain",0);
                it.putExtra("istest", 1);
                it.putExtra("num", 0);
                startActivity(it);
            }

        });
        Button YB = (Button) findViewById(R.id.YB);
        YB.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "spotYB1.xml");
                it.putExtra("domain",0);
                it.putExtra("istest", 0);
                startActivity(it);
            }

        });
        Button YC = (Button) findViewById(R.id.YC);
        YC.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotActivity.this, Process.class);
                it.putExtra(CommonSetting.FileNameTag, "spotYC1.xml");
                it.putExtra("domain",0);
                it.putExtra("istest", 0);
                startActivity(it);
            }

        });
        Button back = (Button) findViewById(R.id.YCback);
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent();
                it.setClass(SpotActivity.this, MenuActivity.class);
                startActivity(it);
            }

        });

    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
