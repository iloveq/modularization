package com.woaiqw.sdk_share;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.woaiqw.sdk_share.model.AppId;
import com.woaiqw.sdk_share.model.ShareBean;
import com.woaiqw.sdk_share.presenter.SharePresenter;
import com.woaiqw.sdk_share.utils.SerializeUtils;
import com.woaiqw.sdk_share.view.IShareView;
import com.woaiqw.sdk_share.view.OnShareClickListener;
import com.woaiqw.sdk_share.view.ShareDialog;

import java.io.File;

/**
 * Created by haoran on 2018/8/3.
 */

public class ShareSdkProxy {

    private ShareSdkProxy() {
    }

    private static class Holder {
        private static ShareSdkProxy IN = new ShareSdkProxy();
    }


    public static ShareSdkProxy getInstance() {
        return Holder.IN;
    }

    public void init(Application app, String[] appIds) {

        if (app != null && appIds != null && appIds.length == 3) {
            AppId appId = new AppId(appIds[0], appIds[1], appIds[2]);
            String serializePath = getSerializePath(app);
            SerializeUtils.serialization(serializePath, appId);
        } else {
            throw new RuntimeException(" ShareSdk 初始化失败 ");
        }

    }

    public String getSerializePath(Application app) {
        File dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = app.getExternalFilesDir("share-sdk");
        } else {
            dir = app.getCacheDir();
        }
        assert dir != null;
        File serializeFile = new File(dir, "sdk.txt");
        return serializeFile.getAbsolutePath();
    }

    /**
     * the channel turn can inflect the turn of IShareView
     *
     * @param shareChannel
     * @return
     * @link ShareChannel.java
     */
    public IShareView createShareDialog(int[] shareChannel, int spanCount) {
        return new ShareDialog().createShareDialog(shareChannel, spanCount);
    }

    public void setOnShareClickListener(IShareView iShareView, final Activity activity, final ShareBean shareBean) {

        iShareView.setOnShareClickListener(new OnShareClickListener() {
            @Override
            public void onShareClick(int channel) {
                switch (channel) {
                    case ShareChannel.CHANNEL_WECHAT:
                        SharePresenter.start().onShareWx(activity, shareBean);
                        break;
                    case ShareChannel.CHANNEL_WECHAT_MOMENT:
                        SharePresenter.start().onShareWxCircle(activity, shareBean);
                        break;
                    case ShareChannel.CHANNEL_QQ:
                        SharePresenter.start().onShareQQ(activity, shareBean);
                        break;
                    case ShareChannel.CHANNEL_QQ_ZONE:
                        SharePresenter.start().onShareQZone(activity, shareBean);
                        break;
                    case ShareChannel.CHANNEL_WEIBO:
                        SharePresenter.start().onShareWeiBo(activity, shareBean);
                        break;
                    case ShareChannel.CHANNEL_MORE:
                        break;
                    case ShareChannel.CHANNEL_CANNEL:
                        break;

                }
            }
        });

    }


}
