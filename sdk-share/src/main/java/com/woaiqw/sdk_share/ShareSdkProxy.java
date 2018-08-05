package com.woaiqw.sdk_share;

import android.app.Application;

import com.woaiqw.sdk_share.view.IShareView;

/**
 * Created by haoran on 2018/8/3.
 */

public class ShareSdkProxy {

    private ShareSdkProxy() {
    }

    private static class Holder {
        private static final ShareSdkProxy IN = new ShareSdkProxy();
    }


    public static ShareSdkProxy getInstance() {
        return Holder.IN;
    }

    public static void init(Application app, String[] appIds) {

        if (app != null && appIds != null && appIds.length > 0) {

        }

    }

    public static IShareView createShareDialog(int[] shareChannel) {
        return null;
    }


}
