package cn.com.aetc.aetchome.common;

import android.os.Handler;
import android.os.HandlerThread;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.com.aetc.aetchome.api.AetcAPI;
import cn.com.aetc.aetchome.api.AetcFactory;
import cn.com.aetc.aetchome.rxmethod.RxNews;
import cn.com.aetc.greendao.DBHelper;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by chenliang3 on 2016/7/30.
 */
public class AppServe {

    private static final AppServe AETC_SERVE = new AppServe();
    private AetcAPI aetcAPI;
    private Map<Integer, CompositeSubscription> compositeSubByTaskId;
    private Handler handler;
    private static DBHelper dbHelper;
    private static Gson gson;

    private AppServe(){

    }

    public static AppServe getInstance(){
        return AETC_SERVE;
    }

    public void initService(){
        gson = new Gson();
        compositeSubByTaskId = new HashMap<>();
        backgroundInit();
    }

    private void backgroundInit(){
        HandlerThread ioThread = new HandlerThread("IoThread");
        ioThread.start();
        handler = new Handler(ioThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                aetcAPI = AetcFactory.getAetcAPI();
                dbHelper = DBHelper.getInstance(MyApplication.getContext());
            }
        });
    }

    public void addCompositeSub(int taskId){
        CompositeSubscription compositeSubscription;
        if (compositeSubByTaskId.get(taskId) == null){
            compositeSubscription = new CompositeSubscription();
            compositeSubByTaskId.put(taskId, compositeSubscription);
        }
    }

    public void removeCompositeSub(int taskId){
        CompositeSubscription compositeSubscription;
        if (compositeSubByTaskId != null && compositeSubByTaskId.get(taskId) != null){
            compositeSubscription = compositeSubByTaskId.get(taskId);
            compositeSubscription.unsubscribe();
            compositeSubByTaskId.remove(taskId);
        }
    }

    private CompositeSubscription getCompositeSubscription(int taskId){
        CompositeSubscription compositeSubscription;
        if (compositeSubByTaskId.get(taskId) == null){
            compositeSubscription = new CompositeSubscription();
            compositeSubByTaskId.put(taskId, compositeSubscription);
        }else {
            compositeSubscription = compositeSubByTaskId.get(taskId);
        }
        return compositeSubscription;
    }

    public static Gson getGson() {
        return gson;
    }

    public static DBHelper getDbHelper() {
        return dbHelper;
    }

    public AetcAPI getAetcAPI() {
        return aetcAPI;
    }

    public void getNews(int taskId, String date, Constants.Way way){
        getCompositeSubscription(taskId).add(RxNews.getRxNews(date, way));
    }

    public void getNewsFromDb(int taskId, String date){
        getCompositeSubscription(taskId).add(RxNews.getRxNewsFromDb(date));
    }
}
