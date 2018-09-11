package com.credithc.hhr.module_home.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.credithc.hhr.library_common.bean.CardListBean;
import com.credithc.hhr.module_home.R;
import com.credithc.hhr.module_home.R2;
import com.credithc.hhr.module_home.adapter.CardListAdapter;
import com.credithc.hhr.module_home.contract.MainContract;
import com.credithc.hhr.module_home.presenter.MainPresenter;
import com.credithc.hhr.module_home.view.widget.BorderDividerItemDecoration;
import com.woaiqw.base.common.BaseFragment;
import com.woaiqw.base.utils.ToastUtil;
import com.woaiqw.base.widget.NetworkStateView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by haoran on 2018/7/26.
 */

public class HomeFragment extends BaseFragment implements MainContract.IMainView, NetworkStateView.OnRetryClickListener {

    @BindView(R2.id.rv)
    RecyclerView rv;
    @BindView(R2.id.nsv)
    NetworkStateView nsv;
    MainContract.IMainPresenter presenter;
    private CardListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_home;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        nsv.setOnRetryClickListener(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.addItemDecoration(new BorderDividerItemDecoration(this.getResources().getDimensionPixelOffset(R.dimen.home_border_divider_height), this.getResources().getDimensionPixelOffset(R.dimen.home_border_padding_spans)));
        adapter = new CardListAdapter();
        presenter = new MainPresenter();
        presenter.onAttach(this);
        rv.setAdapter(adapter);
        presenter.getCardList();
    }


    @Override
    public void showLoading() {
        nsv.showLoading();
    }

    @Override
    public void hideLoading() {
        nsv.showSuccess();
    }

    @Override
    public void onError(String message) {
        ToastUtil.showShortToast(message);
        nsv.showError();
    }

    @Override
    public void showEmptyDataView() {
        nsv.showEmpty();
    }

    @Override
    public void showCardList(List<CardListBean.CardBean> cardBeanList) {
        if (adapter != null)
            adapter.replaceData(cardBeanList);
    }

    @Override
    public void onRefresh() {
        ToastUtil.showShortToast("重新请求中...");
        presenter.getCardList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


}
