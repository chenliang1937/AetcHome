package cn.com.aetc.aetchome.rxmethod;

import java.util.List;

import cn.com.aetc.aetchome.common.AppServe;
import cn.com.aetc.aetchome.common.Constants;
import cn.com.aetc.aetchome.model.News;
import cn.com.aetc.aetchome.model.event.NewsEvent;
import cn.com.aetc.aetchome.utils.EventBusUtil;
import cn.com.aetc.greendao.GreenNews;
import cn.com.aetc.greendao.GreenNewsDao;
import de.greenrobot.dao.query.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/7/30.
 */
public class RxNews {

    public static Subscription getRxNewsFromDb(final String date){
        Subscription subscription = Observable.create(new Observable.OnSubscribe<News>() {
            @Override
            public void call(Subscriber<? super News> subscriber) {
                News news = getCacheNews(date);
                subscriber.onNext(news);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        EventBusUtil.getInstence().post(new NewsEvent(news, Constants.Way.INIT, Constants.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBusUtil.getInstence().post(new NewsEvent(null, Constants.Way.INIT, Constants.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription getRxNews(String date, final Constants.Way way){
        Subscription subscription = AppServe.getInstance().getAetcAPI().getBeforeNews(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        cacheNews(news);
                    }
                })
                .subscribe(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        EventBusUtil.getInstence().post(new NewsEvent(news, way, Constants.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBusUtil.getInstence().post(new NewsEvent(null, way, Constants.Result.FAIL));
                    }
                });
        return subscription;
    }

    private static void cacheNews(News news){
        GreenNewsDao greenNewsDao = AppServe.getDbHelper().getDaoSession().getGreenNewsDao();
        String newsList = AppServe.getGson().toJson(news);
        GreenNews greenNews = new GreenNews(null, news.getDate(), newsList);
        greenNewsDao.insertOrReplace(greenNews);
    }

    private static News getCacheNews(String date){
        News news = null;
        GreenNewsDao greenNewsDao = AppServe.getDbHelper().getDaoSession().getGreenNewsDao();
        Query query = greenNewsDao.queryBuilder()
                .where(GreenNewsDao.Properties.Date.eq(date))
                .limit(1)
                .orderDesc(GreenNewsDao.Properties.Id)
                .build();
        List<GreenNews> greenNews = query.list();
        if (greenNews != null && greenNews.size() > 0){
            news = AppServe.getGson().fromJson(greenNews.get(0).getNewslist(), News.class);
        }
        return news;
    }

}
