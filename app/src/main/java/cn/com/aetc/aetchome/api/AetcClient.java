package cn.com.aetc.aetchome.api;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by chenliang3 on 2016/7/30.
 */
public class AetcClient {

    private AetcAPI aetcAPI;

    public AetcClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com/api/4/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        aetcAPI = retrofit.create(AetcAPI.class);
    }

    public AetcAPI getAetcAPI() {
        return aetcAPI;
    }
}
