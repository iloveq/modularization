package com.woaiqw.sdk_share;

import android.app.Application;

import com.woaiqw.sdk_share.utils.SerializeUtils;
import com.woaiqw.sdk_share.view.IShareView;
import com.woaiqw.sdk_share.view.OnShareClickListener;
import com.woaiqw.sdk_share.view.ShareDialog;

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

    public void init(Application app, String[] appIds, String filePath) {

        if (app != null && appIds != null && appIds.length == 3) {
            //SerializeUtils.serialization(filePath, appIds);
        } else {
            throw new RuntimeException(" ShareSdk 初始化失败 ");
        }

    }

    /**
     * the channel turn can inflect the turn of IShareView
     *
     * @param shareChannel
     * @return
     * @link ShareChannel.java
     */
    public IShareView createShareDialog(int[] shareChannel,int spanCount) {
        return new ShareDialog().createShareDialog(shareChannel,spanCount);
    }

    public void setOnShareClickListener(IShareView iShareView) {

        iShareView.setOnShareClickListener(new OnShareClickListener() {
            @Override
            public void onShareClick(int channel) {
                switch (channel) {
                    case ShareChannel.CHANNEL_WECHAT:
                        break;
                    case ShareChannel.CHANNEL_WECHAT_MOMENT:
                        break;
                    case ShareChannel.CHANNEL_QQ:
                        break;
                    case ShareChannel.CHANNEL_QQ_ZONE:
                        break;
                    case ShareChannel.CHANNEL_WEIBO:
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
