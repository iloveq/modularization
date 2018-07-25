package com.credithc.hhr.module_home.view.activity;


import android.Manifest;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;


import com.credithc.hhr.module_home.R;
import com.credithc.hhr.module_home.R2;
import com.credithc.hhr.module_home.adapter.CardListAdapter;
import com.credithc.hhr.library_common.bean.CardListBean;
import com.credithc.hhr.module_home.contract.MainContract;
import com.credithc.hhr.module_home.presenter.MainPresenter;
import com.credithc.hhr.module_home.view.widget.BorderDividerItemDecoration;
import com.woaiqw.base.common.BaseActivity;
import com.woaiqw.base.utils.PermissionListener;
import com.woaiqw.base.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.IMainView {

    @BindView(R2.id.rv)
    RecyclerView rv;
    MainContract.IMainPresenter presenter;
    private CardListAdapter adapter;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected int getLayoutId() {
        return R.layout.home_activity_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.addItemDecoration(new BorderDividerItemDecoration(this.getResources().getDimensionPixelOffset(R.dimen.home_border_divider_height), this.getResources().getDimensionPixelOffset(R.dimen.home_border_padding_spans)));
        adapter = new CardListAdapter();
        rv.setAdapter(adapter);
        presenter = new MainPresenter();
        presenter.onAttach(this);
        requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                presenter.getCardList();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                if (deniedPermissions.contains(permissions[0]) || deniedPermissions.contains(permissions[1]))
                    ToastUtil.showLongToast("3G下浏览图片会造成浪费流量资源");
                presenter.getCardList();
            }
        });

    }

    @Override
    protected void onResume() {
        final long time = SystemClock.uptimeMillis();
        super.onResume();
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                // on Measure() -> onDraw() 耗时
                Log.i(MainActivity.this.getClass().getSimpleName(), "onCreate -> idle : " + (SystemClock.uptimeMillis() - time));
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public void onNetworkViewRefresh() {
        super.onNetworkViewRefresh();
        ToastUtil.showShortToast("重新请求中...");
        presenter.getCardList();
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void hideLoading() {
        showContentView();
    }

    @Override
    public void onError(String message) {
        ToastUtil.showShortToast(message);
        showErrorView();
    }

    @Override
    public void showEmptyDataView() {
        showEmptyView();
    }

    @Override
    public void showCardList(List<CardListBean.CardBean> cardBeanList) {
        if (adapter != null)
            adapter.replaceData(cardBeanList);
    }

}
