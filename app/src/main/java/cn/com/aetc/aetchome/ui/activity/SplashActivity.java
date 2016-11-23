package cn.com.aetc.aetchome.ui.activity;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import butterknife.BindView;
import cn.com.aetc.aetchome.R;
import cn.com.aetc.aetchome.common.base.BaseActivity;
import cn.com.aetc.aetchome.utils.MyUtils;

/**
 * Created by chenliang3 on 2016/7/30.
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_image)
    ImageView imageView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.FADE;
    }

    @Override
    protected void initViews() {
        MyUtils.setStatusBarTranslucent(this);
        //解决按home键回到桌面后再次进入重启的问题
        if (!this.isTaskRoot()){
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)){
                finish();
                return;
            }
        }

        startAnimation();
    }

    private void startAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                MyUtils.startActivity(SplashActivity.this, MainActivity.class);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.setAnimation(scaleAnimation);
    }
}
