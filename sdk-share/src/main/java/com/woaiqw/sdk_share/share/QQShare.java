package com.woaiqw.sdk_share.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.woaiqw.sdk_share.model.IShareModel;
import com.woaiqw.sdk_share.ShareListener;


/**
 * Created by haoran on 2018/8/7.
 */

public class QQShare {


    private Activity mActivity;
    private final Tencent mTencent;
    private boolean isShareToQQ;
    private ShareListener mShareListener;


    private IUiListener mIUiListener = new IUiListener() {

        @Override
        public void onComplete(Object o) {
            if (mShareListener != null) {
                mShareListener.onShareSuccess();
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (mShareListener != null) {
                mShareListener.onShareFail("分享失败");
            }
        }

        @Override
        public void onCancel() {
            if (mShareListener != null) {
                mShareListener.onShareCancel();
            }
        }
    };


    public QQShare(Activity activity, String appId, boolean shareToQQ) {
        this.isShareToQQ = shareToQQ;
        this.mActivity = activity;
        mTencent = Tencent.createInstance(appId, activity);
    }

    public void onActivityResultData(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
    }

    public void sendWebShareMessage(IShareModel share, ShareListener listener) {

        mShareListener = listener;

        final String title = share.getTitle();
        final String content = share.getContent();
        final String actionUrl = share.getActionUrl();
        final String imgUrl = share.getImgUrl();

        Bundle params = new Bundle();
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, content);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, actionUrl);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
        if (isShareToQQ) {
            params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_EXT_INT, com.tencent.connect.share.QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        } else {
            params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_EXT_INT, com.tencent.connect.share.QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }

        mTencent.shareToQQ(mActivity, params, mIUiListener);

    }

    private void sendTextMessage(IShareModel share, ShareListener listener) {
        this.mShareListener = listener;
        final String title = share.getTitle();
        final String content = share.getContent();
        final String actionUrl = share.getActionUrl();

        Bundle params = new Bundle();

        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, content);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, actionUrl);

        if (isShareToQQ) {
            params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_EXT_INT, com.tencent.connect.share.QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        } else {
            params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_EXT_INT, com.tencent.connect.share.QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        mTencent.shareToQQ(mActivity, params, mIUiListener);
    }


    public void sendImageMessage(IShareModel share, ShareListener listener) {
        this.mShareListener = listener;
        final String title = share.getTitle();
        final String imgUrl = share.getImgUrl();

        Bundle params = new Bundle();
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imgUrl);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_APP_NAME, title);
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_EXT_INT, com.tencent.connect.share.QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        if (isShareToQQ) {
            params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_EXT_INT, com.tencent.connect.share.QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        } else {
            params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_EXT_INT, com.tencent.connect.share.QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        mTencent.shareToQQ(mActivity, params, mIUiListener);
    }

}
