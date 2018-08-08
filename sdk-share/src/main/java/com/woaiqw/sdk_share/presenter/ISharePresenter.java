package com.woaiqw.sdk_share.presenter;

import com.woaiqw.sdk_share.model.ShareBean;


/**
 * Created by haoran on 2018/8/7.
 */

public interface ISharePresenter {

    void onShareWeiBo(ShareBean entry);

    void onShareWxCircle(ShareBean entry);

    void onShareWx(ShareBean entry);

    void onShareQQ(ShareBean entry);

    void onShareQZone(ShareBean entry);

}
