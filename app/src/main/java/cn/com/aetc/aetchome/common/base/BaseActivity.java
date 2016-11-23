package cn.com.aetc.aetchome.common.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import cn.com.aetc.aetchome.R;
import cn.com.aetc.aetchome.broadcast.ConnectionBroadcast;
import cn.com.aetc.aetchome.common.AppManager;
import cn.com.aetc.aetchome.common.AppServe;
import cn.com.aetc.aetchome.common.MyApplication;
import icepick.Icepick;

/**
 * Created by chenliang on 16/7/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public enum TransitionMode{
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE, ZOOM
    }

    private ConnectionBroadcast receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        switch (getOverridePendingTransitionMode()){
            case LEFT:
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case RIGHT:
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case TOP:
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
            case BOTTOM:
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                break;
            case SCALE:
                overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                break;
            case FADE:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case ZOOM:
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            default:
                break;
        }
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        AppServe.getInstance().addCompositeSub(getTaskId());
        AppManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        registerReceiver();
        initViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Icepick.saveInstanceState(this, outState);
    }

    private void registerReceiver(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new ConnectionBroadcast();
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppServe.getInstance().removeCompositeSub(getTaskId());
        this.unregisterReceiver(receiver);
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    @Override
    public void finish() {
        super.finish();
        AppManager.getInstance().removeActivity(this);
        switch (getOverridePendingTransitionMode()){
            case LEFT:
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case RIGHT:
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case TOP:
                overridePendingTransition(R.anim.top_in,R.anim.top_out);
                break;
            case BOTTOM:
                overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                break;
            case SCALE:
                overridePendingTransition(R.anim.scale_in,R.anim.scale_out);
                break;
            case FADE:
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case ZOOM:
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
            default:
                break;
        }
    }

    protected abstract int getContentViewId();
    protected abstract TransitionMode getOverridePendingTransitionMode();
    protected abstract void initViews();
}
