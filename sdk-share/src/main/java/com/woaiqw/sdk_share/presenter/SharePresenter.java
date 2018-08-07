package com.woaiqw.sdk_share.presenter;

import android.app.Activity;

import com.woaiqw.sdk_share.ShareChannel;
import com.woaiqw.sdk_share.model.ShareBean;
import com.woaiqw.sdk_share.view.ShareActivity;

/**
 * Created by haoran on 2018/8/7.
 */

public class SharePresenter implements ISharePresenter {

    private static final class Holder {
        private static SharePresenter IN = new SharePresenter();
    }


    private void startActivityForResult(Activity activity, int type, ShareBean entry) {
        activity.startActivityForResult(ShareActivity.getIntent(activity, entry, type), type);
    }


    public static ISharePresenter start() {
        return Holder.IN;
    }

    @Override
    public void onShareWeiBo(Activity context, ShareBean entry) {
        startActivityForResult(context, ShareChannel.CHANNEL_WEIBO, entry);
    }

    @Override
    public void onShareWxCircle(Activity context, ShareBean entry) {
        startActivityForResult(context, ShareChannel.CHANNEL_WECHAT_MOMENT, entry);
    }

    @Override
    public void onShareWx(Activity context, ShareBean entry) {
        startActivityForResult(context, ShareChannel.CHANNEL_WECHAT, entry);
    }

    @Override
    public void onShareQQ(Activity context, ShareBean entry) {
        startActivityForResult(context, ShareChannel.CHANNEL_QQ, entry);
    }

    @Override
    public void onShareQZone(Activity context, ShareBean entry) {
        startActivityForResult(context, ShareChannel.CHANNEL_QQ_ZONE, entry);
    }

}
