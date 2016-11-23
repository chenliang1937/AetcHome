package cn.com.aetc.aetchome.common;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.com.aetc.aetchome.BuildConfig;
import cn.com.aetc.aetchome.ui.activity.MainActivity;

/**
 * Created by chenliang3 on 2016/7/29.
 */
public class MyApplication extends Application {

    private static String TAG = "AetcHome";
    private RefWatcher refWatcher;
    private static Context context;

    public static RefWatcher getRefWatcher(Context context){
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        AppServe.getInstance().initService();

        if (BuildConfig.LOG_DEBUG){//debug
            Logger.init(TAG).logLevel(LogLevel.FULL);//打印log
            refWatcher = LeakCanary.install(this);//内存泄露监测
            CustomActivityOnCrash.setShowErrorDetails(true);//崩溃页面是否显示崩溃详细信息
        }else {//release
            Logger.init(TAG).logLevel(LogLevel.NONE);//关闭log
            refWatcher = RefWatcher.DISABLED;//关闭内存泄露监测
            CustomActivityOnCrash.setShowErrorDetails(false);
        }

        //launching a custom activity when the app crashes
        CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(true);
        CustomActivityOnCrash.setEnableAppRestart(true);
        CustomActivityOnCrash.setRestartActivityClass(MainActivity.class);
        CustomActivityOnCrash.install(this);
    }

    public static Context getContext() {
        return context;
    }

}
