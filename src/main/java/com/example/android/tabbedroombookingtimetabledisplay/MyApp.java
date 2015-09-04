package com.example.android.tabbedroombookingtimetabledisplay;

import android.app.Application;
import android.content.Context;


//class to get application context.
public class MyApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
//returning application context
    public static Context getContext() {
        return mContext;
    }
}
