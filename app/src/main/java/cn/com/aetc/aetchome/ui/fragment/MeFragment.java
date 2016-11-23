package cn.com.aetc.aetchome.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.aetc.aetchome.R;
import cn.com.aetc.aetchome.common.Constants;
import cn.com.aetc.aetchome.common.base.BaseFragment;
import cn.com.aetc.aetchome.ui.activity.UserInfoActivity;
import cn.com.aetc.aetchome.utils.DataCleanManager;
import cn.com.aetc.aetchome.utils.MyUtils;
import cn.com.aetc.aetchome.utils.PrefserUtil;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by chenliang3 on 2016/7/29.
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.me_toolbar)
    Toolbar toolbar;
    @BindView(R.id.me_scrollview)
    ScrollView scrollView;
    @BindView(R.id.me_photo)
    ImageView photoImage;
    @BindView(R.id.me_cache_size)
    TextView cacheSize;
    @BindView(R.id.me_clear_cache)
    WaveLoadingView waveLoadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("我");
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // FIXME: 暂时解决4.0显示bug
            waveLoadingView.setAmplitudeRatio(100);
        }
        try {
            String tmpCache = DataCleanManager.getTotalCacheSize(getActivity());
            cacheSize.setText(tmpCache);
            setClearCacheWave(tmpCache);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadPhoto();
    }

    private void loadPhoto(){
        String tmpPath = PrefserUtil.getInstance(getActivity()).get(Constants.USER_HEAD_PATH, String.class, "");
        if (!TextUtils.isEmpty(tmpPath)){
            File tmpFile = new File(tmpPath);
            if (tmpFile.exists()){
                Glide.with(this)
                        .load(tmpFile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .bitmapTransform(new RoundedCornersTransformation(getActivity(), 10, 0, RoundedCornersTransformation.CornerType.ALL))
                        .error(R.mipmap.ic_default)
                        .into(photoImage);
            }else {
                // TODO: 2016/8/4 从服务器下载
            }
        }
    }

    private void setClearCacheWave(String cache){
        int progress;
        if (cache.contains("KB")) {
            if (cache.equals("0KB")) {
                progress = 0;
            } else {
                progress = 5;
            }
        } else if (cache.contains("MB")) {
            float mem = Integer.parseInt(cache.split("M")[0]);
            if (mem < 2) {
                progress = 10;
            } else if (mem < 3) {
                progress = 15;
            } else if (mem < 4) {
                progress = 20;
            } else if (mem < 6) {
                progress = 25;
            } else if (mem < 8) {
                progress = 30;
            } else if (mem < 10) {
                progress = 35;
            } else if (mem < 15) {
                progress = 40;
            } else if (mem < 20) {
                progress = 45;
            } else if (mem < 25) {
                progress = 50;
            } else if (mem < 30) {
                progress = 60;
            } else if (mem < 40) {
                progress = 70;
            } else if (mem < 50) {
                progress = 80;
            } else if (mem < 60) {
                progress = 90;
            } else {
                progress = 95;
            }
        } else {
            progress = 100;
        }
        waveLoadingView.setProgressValue(progress);
    }

    @OnClick(R.id.me_userinfo)
    public void userinfoClick(){
        MyUtils.startActivity(getActivity(), UserInfoActivity.class);
    }

    //清除缓存
    @OnClick(R.id.me_layout4)
    public void clearCacheClick(){
        try {
            DataCleanManager.clearAllCache(getActivity());
            String tmpCache = DataCleanManager.getTotalCacheSize(getActivity());
            cacheSize.setText(tmpCache);
            setClearCacheWave(tmpCache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
