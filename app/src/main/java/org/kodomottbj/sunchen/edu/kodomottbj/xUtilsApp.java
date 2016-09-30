package org.kodomottbj.sunchen.edu.kodomottbj;

import android.app.Application;

import org.xutils.x;

/**
 * Created by SunChen on 16/8/7.
 */
public class xUtilsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
