package com.credithc.hhr.module_home.presenter;

import com.credithc.hhr.library_common.bean.CardListBean;
import com.credithc.hhr.module_home.contract.MainContract;
import com.credithc.hhr.module_home.model.MainModel;
import com.woaiqw.base.mvp.BasePresenter;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by haoran on 2018/6/28.
 */

public class MainPresenter extends BasePresenter<MainContract.IMainView, MainModel> implements MainContract.IMainPresenter {


    @Override
    public void getCardList() {
        checkViewAttached();
        view.showLoading();
        disposable.add(model.getCardList().subscribe(new Consumer<List<CardListBean.CardBean>>() {
            @Override
            public void accept(List<CardListBean.CardBean> cardBeanList) {
                view.hideLoading();
                if (cardBeanList == null || cardBeanList.size() == 0) {
                    view.showEmptyDataView();
                } else {
                    view.showCardList(cardBeanList);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                view.onError(throwable.getMessage());
            }
        }));
    }
}
