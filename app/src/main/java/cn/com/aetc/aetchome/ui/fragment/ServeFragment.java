package cn.com.aetc.aetchome.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import cn.com.aetc.aetchome.R;
import cn.com.aetc.aetchome.common.base.BaseFragment;

/**
 * Created by chenliang3 on 2016/7/29.
 */
public class ServeFragment extends BaseFragment {

    @BindView(R.id.serve_toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serve, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setTitle("服务");
    }
}
