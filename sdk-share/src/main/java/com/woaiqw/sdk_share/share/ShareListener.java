package com.woaiqw.sdk_share.share;

/**
 * Created by haoran on 2018/8/3.
 */

public interface ShareListener {

    void onShareSuccess();

    void onShareCancel();

    void onShareFail(String msg);

}
