package com.woaiqw.sdk_share;

import android.app.Activity;
import android.app.Application;

import com.woaiqw.sdk_share.model.AppId;
import com.woaiqw.sdk_share.model.ShareBean;
import com.woaiqw.sdk_share.presenter.SharePresenter;
import com.woaiqw.sdk_share.utils.SerializeUtils;
import com.woaiqw.sdk_share.view.IShareView;
import com.woaiqw.sdk_share.view.ShareDialog;

import static com.woaiqw.sdk_share.utils.Utils.getSerializePath;

/**
 * Created by haoran on 2018/8/3.
 */

public class ShareSdkProxy {

    private ShareSdkProxy() {
    }

    public interface OnShareClickListener {
        void onShareClick(@ShareChannel int channel);
    }

    private static class Holder {
        private static final ShareSdkProxy IN = new ShareSdkProxy();
    }


    public static ShareSdkProxy getInstance() {
        return Holder.IN;
    }

    public void init(Application app, String[] appIds) {

        if (app != null && appIds != null && appIds.length == 3) {
            try {
                SerializeUtils.serialization(getSerializePath(app), new AppId(appIds[0], appIds[1], appIds[2]));
            } catch (Exception e) {
                throw new RuntimeException(" ShareSdk serialized error： " + e);
            }
        } else {
            throw new RuntimeException(" ShareSdk init error ");
        }

    }


    /**
     * the channel turn can inflect the turn of IShareView( in this is a dialog )
     *
     * @param shareChannel the type of share function
     * @param column the column of dialog
     * @return the instance of dialog
     * @link ShareChannel.java
     */
    public IShareView createShareDialog(int[] shareChannel, int column) {
        return ShareDialog.get().createShareDialog(shareChannel, column);
    }

    /**
     * @param dialog    dialog
     * @param activity  the current activity to accept the result of the share sdk
     * @param shareBean the model of share function
     */
    public void setOnShareClickListener(IShareView dialog, final Activity activity, final ShareBean shareBean) {
        dialog.setOnShareClickListener(new OnShareClickListener() {
            @Override
            public void onShareClick(int channel) {
                switch (channel) {
                    case ShareChannel.CHANNEL_WECHAT:
                        SharePresenter.start(activity).onShareWx(shareBean);
                        break;
                    case ShareChannel.CHANNEL_WECHAT_MOMENT:
                        SharePresenter.start(activity).onShareWxCircle(shareBean);
                        break;
                    case ShareChannel.CHANNEL_QQ:
                        SharePresenter.start(activity).onShareQQ(shareBean);
                        break;
                    case ShareChannel.CHANNEL_QQ_ZONE:
                        SharePresenter.start(activity).onShareQZone(shareBean);
                        break;
                    case ShareChannel.CHANNEL_WEIBO:
                        SharePresenter.start(activity).onShareWeiBo(shareBean);
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
