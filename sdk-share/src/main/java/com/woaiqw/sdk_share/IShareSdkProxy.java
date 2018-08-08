package com.woaiqw.sdk_share;

import android.app.Activity;
import android.app.Application;

import com.woaiqw.sdk_share.model.ShareBean;
import com.woaiqw.sdk_share.view.IShareView;

/**
 * Created by haoran on 2018/8/8.
 */

public interface IShareSdkProxy {

    void init(Application app, String[] appIds);
    IShareView createShareDialog(int[] shareChannel, int column);
    void setOnShareClickListener(IShareView dialog, final Activity activity, final ShareBean shareBean);

}
