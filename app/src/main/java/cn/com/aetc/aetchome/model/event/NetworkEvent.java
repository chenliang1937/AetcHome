package cn.com.aetc.aetchome.model.event;

/**
 * Created by chenliang3 on 2016/8/1.
 */
public class NetworkEvent {

    private boolean netUseful;

    public NetworkEvent(boolean netUseful){
        this.netUseful = netUseful;
    }

    public boolean isNetUseful() {
        return netUseful;
    }
}
