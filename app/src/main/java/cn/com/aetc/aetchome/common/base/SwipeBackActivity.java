package cn.com.aetc.aetchome.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jude.swipbackhelper.SwipeBackHelper;

/**
 * Created by chenliang on 16/7/25.
 */

public abstract class SwipeBackActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
