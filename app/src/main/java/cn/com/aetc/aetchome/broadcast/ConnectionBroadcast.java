package cn.com.aetc.aetchome.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.com.aetc.aetchome.model.event.NetworkEvent;
import cn.com.aetc.aetchome.utils.EventBusUtil;

/**
 * Created by chenliang3 on 2016/5/11.
 * 网络监测
 */
public class ConnectionBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()){
            //网络不可用
            EventBusUtil.getInstence().post(new NetworkEvent(false));
        }else {
            EventBusUtil.getInstence().post(new NetworkEvent(true));
        }
    }
}
