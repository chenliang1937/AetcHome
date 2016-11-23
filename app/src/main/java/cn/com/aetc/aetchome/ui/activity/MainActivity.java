package cn.com.aetc.aetchome.ui.activity;

import android.app.Notification;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.aetc.aetchome.R;
import cn.com.aetc.aetchome.common.base.BaseActivity;
import cn.com.aetc.aetchome.service.UpdateService;
import cn.com.aetc.aetchome.ui.fragment.HealthFragment;
import cn.com.aetc.aetchome.ui.fragment.MeFragment;
import cn.com.aetc.aetchome.ui.fragment.ServeFragment;
import cn.com.aetc.aetchome.ui.fragment.WorkflowFragment;
import cn.com.aetc.aetchome.widget.MainBottomTabLayout;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_bottom_tablayout)
    MainBottomTabLayout tabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager viewPager;

    private static final String URL = "";//下载地址

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.FADE;
    }

    @Override
    protected void initViews() {
        initViewPager();
    }

    private void initViewPager() {
        WorkflowFragment workflowFragment = new WorkflowFragment();
        ServeFragment serveFragment = new ServeFragment();
        HealthFragment healthFragment = new HealthFragment();
        MeFragment meFragment = new MeFragment();
        fragmentList.add(workflowFragment);
        fragmentList.add(serveFragment);
        fragmentList.add(healthFragment);
        fragmentList.add(meFragment);

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                tabLayout.setFlagVisible(1);
                tabLayout.setFlagNum(1, "3");
            }
        }, 2000);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter{

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //将super注释掉，以解决viewpager+fragment出现的重复加载数据的情况
            //super.destroyItem(container, position, object);
        }
    }

    //下载
    public void updateFlag(){
        UpdateService.Builder.create(URL)
                .setStoreDir("AetcHome/update")
                .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                .build(this);
    }
}
