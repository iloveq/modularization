package com.credithc.hhr.module_home.model;

import com.credithc.hhr.library_common.IApiService;
import com.credithc.hhr.library_common.bean.CardListBean;
import com.credithc.hhr.library_common.utils.RxUtils;
import com.credithc.hhr.module_home.contract.MainContract;
import com.woaiqw.base.AFrameProxy;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by haoran on 2018/6/28.
 */

public class MainModel implements MainContract.IMainModel {


    @Override
    public Observable<List<CardListBean.CardBean>> getCardList() {
        return AFrameProxy.getInstance().<IApiService>createService().getCardList("111", "0", "0").compose(RxUtils.<CardListBean>transform()).map(new Function<CardListBean, List<CardListBean.CardBean>>() {
            @Override
            public List<CardListBean.CardBean> apply(CardListBean cardListBean) {
                return cardListBean.getCardList();
            }
        });
    }
}
