package cn.com.aetc.aetchome.api;

/**
 * Created by chenliang3 on 2016/7/30.
 */
public class AetcFactory {

    private static AetcAPI aetcAPI = null;
    private static final Object WATCH_DOG = new Object();

    private AetcFactory(){

    }

    public static AetcAPI getAetcAPI(){
        synchronized (WATCH_DOG){
            if (aetcAPI == null){
                AetcClient aetcClient = new AetcClient();
                aetcAPI = aetcClient.getAetcAPI();
            }
            return aetcAPI;
        }
    }

}
