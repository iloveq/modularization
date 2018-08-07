package com.woaiqw.sdk_share.presenter;

import android.app.Activity;

import com.woaiqw.sdk_share.model.ShareBean;


/**
 * Created by haoran on 2018/8/7.
 */

public interface ISharePresenter {

    void onShareWeiBo(Activity context, ShareBean entry);

    void onShareWxCircle(Activity context, ShareBean entry);

    void onShareWx(Activity context, ShareBean entry);

    void onShareQQ(Activity context, ShareBean entry);

    void onShareQZone(Activity context, ShareBean entry);

}
