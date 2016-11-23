package cn.com.aetc.aetchome.utils;

import android.content.Context;

import com.github.pwittchen.prefser.library.Prefser;

/**
 * Created by chenliang3 on 2016/7/30.
 * prefser 单例
 */
public class PrefserUtil {

    private static Prefser singleton;

    public static Prefser getInstance(Context context){
        if (singleton == null){
            synchronized (PrefserUtil.class){
                if (singleton == null){
                    singleton = new Prefser(context);
                }
            }
        }
        return singleton;
    }

}
