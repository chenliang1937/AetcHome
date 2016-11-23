package cn.com.aetc.aetchome.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.aetc.aetchome.R;

/**
 * Created by chenliang3 on 2016/7/29.
 */
public class MainBottomTabLayout extends LinearLayout {

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private ArgbEvaluator mColorEvaluator;
    int mTextNormalColor, mTextSelectedColor;

    private int mLastPosition;
    private int mSelectedPosition;
    private float mSelectionOffset;

    private int mTitles[] = {R.string.bottom_tab_workflow, R.string.bottom_tab_serve,
            R.string.bottom_tab_health, R.string.bottom_tab_me};
    private int mIconRes[][] = {
        {R.mipmap.ic_workflow_normal, R.mipmap.ic_workflow_select},
        {R.mipmap.ic_serve_normal, R.mipmap.ic_serve_select},
        {R.mipmap.ic_health_normal, R.mipmap.ic_health_select},
        {R.mipmap.ic_me_normal, R.mipmap.ic_me_select}
    };

    private View[] mIconLayouts;

    public MainBottomTabLayout(Context context) {
        this(context, null);
    }

    public MainBottomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainBottomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mColorEvaluator = new ArgbEvaluator();
        mTextNormalColor = getResources().getColor(R.color.main_bottom_tab_textcolor_normal);
        mTextSelectedColor = getResources().getColor(R.color.main_bottom_tab_textcolor_selected);
    }

    public void setViewPager(ViewPager viewPager) {
        removeAllViews();
        mViewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabLayout();
        }
    }

    //只显示红点
    public void setFlagVisible(int i){
        mIconLayouts[i].findViewById(R.id.main_bottom_tab_flag).setVisibility(View.VISIBLE);
    }

    //显示红点和数字
    public void setFlagNum(int i, String num){
        mIconLayouts[i].findViewById(R.id.main_bottom_tab_num).setVisibility(View.VISIBLE);
        ((TextView)mIconLayouts[i].findViewById(R.id.main_bottom_tab_num)).setText(num);
    }

    private void populateTabLayout() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();
        mIconLayouts = new View[adapter.getCount()];

        for (int i = 0; i < adapter.getCount(); i++) {

            final View tabView = LayoutInflater.from(getContext()).inflate(R.layout.layout_mainbottom_tab, this, false);
            mIconLayouts[i] = tabView;
            TabIconView iconView = (TabIconView) tabView.findViewById(R.id.main_bottom_tab_icon);
            iconView.init(mIconRes[i][0], mIconRes[i][1]);
            TextView textView = (TextView) tabView.findViewById(R.id.main_bottom_tab_text);
            textView.setText(mTitles[i]);
            TextView flagView = (TextView) tabView.findViewById(R.id.main_bottom_tab_flag);

            if (tabView == null) {
                throw new IllegalStateException("tabView is null.");
            }

            LayoutParams lp = (LayoutParams) tabView.getLayoutParams();
            lp.width = 0;
            lp.weight = 1;

            tabView.setOnClickListener(tabClickListener);
            addView(tabView);

            if (i == mViewPager.getCurrentItem()) {
                iconView.transformPage(0);
                tabView.setSelected(true);
                textView.setTextColor(mTextSelectedColor);
            }
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;
        private boolean isFirst = true;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            onViewPagerPageChanged(position, positionOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            if (!isFirst && mIconLayouts[position].findViewById(R.id.main_bottom_tab_flag).getVisibility() == View.VISIBLE){
                mIconLayouts[position].findViewById(R.id.main_bottom_tab_flag).setVisibility(GONE);
            }
            if (!isFirst && mIconLayouts[position].findViewById(R.id.main_bottom_tab_num).getVisibility() == View.VISIBLE){
                mIconLayouts[position].findViewById(R.id.main_bottom_tab_num).setVisibility(GONE);
            }
            isFirst = false;
        }

        @Override
        public void onPageSelected(int position) {

            for (int i = 0; i < getChildCount(); i++) {
                ((TabIconView) mIconLayouts[i].findViewById(R.id.main_bottom_tab_icon))
                        .transformPage(position == i ? 0 : 1);
                ((TextView) mIconLayouts[i].findViewById(R.id.main_bottom_tab_text))
                        .setTextColor(position == i ? mTextSelectedColor : mTextNormalColor);
            }

            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                onViewPagerPageChanged(position, 0f);
            }

            for (int i = 0, size = getChildCount(); i < size; i++) {
                getChildAt(i).setSelected(position == i);
            }


            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }

    private void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        if (positionOffset == 0f && mLastPosition != mSelectedPosition) {
            mLastPosition = mSelectedPosition;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int childCount = getChildCount();
        if (childCount > 0) {
            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {

                View selectedTab = getChildAt(mSelectedPosition);
                View nextTab = getChildAt(mSelectedPosition + 1);

                View selectedIconView = ((RelativeLayout) selectedTab).getChildAt(0);
                View nextIconView = ((RelativeLayout) nextTab).getChildAt(0);

                View selectedTextView = ((RelativeLayout) selectedTab).getChildAt(1);
                View nextTextView = ((RelativeLayout) nextTab).getChildAt(1);

                //draw icon alpha
                if (selectedIconView instanceof TabIconView && nextIconView instanceof TabIconView) {
                    ((TabIconView) selectedIconView).transformPage(mSelectionOffset);
                    ((TabIconView) nextIconView).transformPage(1 - mSelectionOffset);
                }

                //draw text color
                Integer selectedColor = (Integer) mColorEvaluator.evaluate(mSelectionOffset,
                        mTextSelectedColor,
                        mTextNormalColor);
                Integer nextColor = (Integer) mColorEvaluator.evaluate(1 - mSelectionOffset,
                        mTextSelectedColor,
                        mTextNormalColor);

                if (selectedTextView instanceof TextView && nextTextView instanceof TextView) {
                    ((TextView) selectedTextView).setTextColor(selectedColor);
                    ((TextView) nextTextView).setTextColor(nextColor);
                }
            }
        }
    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < getChildCount(); i++) {
                if (v == getChildAt(i)) {
                    mViewPager.setCurrentItem(i, false);
                    return;
                }
            }
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }
}
