package cn.com.aetc.aetchome.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by chenliang3 on 2016/7/30.
 * EventBus 单例
 */
public class EventBusUtil {

    private static EventBus singleton;

    public static EventBus getInstence(){
        if (singleton == null){
            synchronized (EventBusUtil.class){
                if (singleton == null){
                    singleton = EventBus.getDefault();
                }
            }
        }
        return singleton;
    }

}
