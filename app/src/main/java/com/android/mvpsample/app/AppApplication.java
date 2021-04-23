package com.android.mvpsample.app;


import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;

import baseapp.BaseApplication;

public class AppApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
    }

    private void initLog() {
        LogUtils.getConfig().setLogSwitch(AppUtils.isAppDebug());
    }

}
