package com.wan.qd.kysdkdemo;

import android.app.Application;

import com.wan.qc.sdk.open.QCSDKManager;

/**
 * Create by xukai03
 * Date:2020/6/12
 * Description:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QCSDKManager.getInstance().initApplication(this);
    }
}
