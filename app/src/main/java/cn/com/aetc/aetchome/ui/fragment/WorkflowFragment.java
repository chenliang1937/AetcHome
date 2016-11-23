package cn.com.aetc.aetchome.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.aetc.aetchome.R;
import cn.com.aetc.aetchome.common.AppServe;
import cn.com.aetc.aetchome.common.Constants;
import cn.com.aetc.aetchome.common.base.BaseFragment;
import cn.com.aetc.aetchome.model.event.NetworkEvent;
import cn.com.aetc.aetchome.model.event.NewsEvent;
import cn.com.aetc.aetchome.ui.adapter.NewsAdapter;
import cn.com.aetc.aetchome.utils.DateUtils;
import cn.com.aetc.aetchome.utils.EventBusUtil;
import cn.com.aetc.aetchome.utils.MyUtils;
import cn.com.aetc.aetchome.utils.NetWorkUtils;

/**
 * Created by chenliang3 on 2016/7/29.
 */
public class WorkflowFragment extends BaseFragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.workflow_recyclerview)
    EasyRecyclerView recyclerView;
    @BindView(R.id.workflow_toolbar)
    Toolbar toolbar;
    @BindView(R.id.workflow_expandlayout)
    RelativeLayout expandLayout;

    private NewsAdapter adapter;
    private Handler handler = new Handler();
    private String date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workflow, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBusUtil.getInstence().register(this);
        }

        toolbar.setTitle("工单");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.divider_line),
                MyUtils.dip2px(getActivity(), 0.5f), MyUtils.dip2px(getActivity(), 0.5f), 0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);

        adapter = new NewsAdapter(getActivity());
        recyclerView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Snackbar.make(recyclerView, position + "", Snackbar.LENGTH_SHORT).show();
            }
        });
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });

        recyclerView.setRefreshListener(this);
//        onRefresh();
        date = DateUtils.getCurDate();
        String tmpDate = DateUtils.date2Str(DateUtils.getPreviousDate(DateUtils.str2Date(date, "yyyyMMdd")), "yyyyMMdd");
        AppServe.getInstance().getNewsFromDb(getActivity().getTaskId(), tmpDate);
    }

    @OnClick(R.id.workflow_expandlayout)
    public void expandClick(){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        date = DateUtils.date2Str(DateUtils.getPreviousDate(DateUtils.str2Date(date, "yyyyMMdd")), "yyyyMMdd");

        if (!NetWorkUtils.isConnectedByState(getActivity())) {
            adapter.pauseMore();
            return;
        }
        AppServe.getInstance().getNews(getActivity().getTaskId(), date, Constants.Way.LOADMORE);
    }

    @Override
    public void onRefresh() {
        date = DateUtils.getCurDate();

        if (!NetWorkUtils.isConnectedByState(getActivity())){
            adapter.pauseMore();
            recyclerView.setRefreshing(false);
            return;
        }
        AppServe.getInstance().getNews(getActivity().getTaskId(), date, Constants.Way.REFRESH);
    }

    @Subscribe
    public void onEventMainThread(NewsEvent event){
        if (event.getWay() == Constants.Way.REFRESH){
            recyclerView.setRefreshing(false);
            adapter.clear();
            if (event.getResult() == Constants.Result.SUCCESS){
                adapter.addAll(event.getNews().getStories());
            }else {
                recyclerView.showError();
            }
        }else if (event.getWay() == Constants.Way.LOADMORE){
            if (event.getResult() == Constants.Result.SUCCESS){
                adapter.addAll(event.getNews().getStories());
            }else {
                recyclerView.showError();
            }
        }else if (event.getWay() == Constants.Way.INIT){
            if (event.getResult() == Constants.Result.SUCCESS){
                if (event.getNews() != null){
                    adapter.addAll(event.getNews().getStories());
                }else {
                    onRefresh();
                }
            }else {
                onRefresh();
            }
        }
    }

    @Subscribe
    public void onEventMainThread(NetworkEvent event){
        if (event.isNetUseful()){
            expandLayout.setVisibility(View.GONE);
        }else {
            expandLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBusUtil.getInstence().unregister(this);
        }
    }

}
