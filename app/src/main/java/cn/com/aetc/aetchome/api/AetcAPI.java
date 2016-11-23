package cn.com.aetc.aetchome.api;

import cn.com.aetc.aetchome.model.News;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by chenliang3 on 2016/7/30.
 */
public interface AetcAPI {

    @GET("news/before/{date}")
    Observable<News> getBeforeNews(@Path("date") String date);

}
