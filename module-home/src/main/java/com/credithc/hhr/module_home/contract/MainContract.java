package com.credithc.hhr.module_home.contract;

import com.credithc.hhr.library_common.bean.CardListBean;
import com.woaiqw.base.mvp.IBaseModel;
import com.woaiqw.base.mvp.IBaseView;
import com.woaiqw.base.mvp.IPresenter;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by haoran on 2018/6/28.
 */

public interface MainContract {

    interface IMainView extends IBaseView {

        void showCardList(List<CardListBean.CardBean> cardBeanList);

    }

    interface IMainModel extends IBaseModel {

        Observable<List<CardListBean.CardBean>> getCardList();

    }

    interface IMainPresenter extends IPresenter<IMainView> {

        void getCardList();

    }
}
