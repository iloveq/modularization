package com.woaiqw.sdk_share.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.share.WbShareCallback;
import com.woaiqw.sdk_share.R;
import com.woaiqw.sdk_share.ShareChannel;
import com.woaiqw.sdk_share.ShareSdkProxy;
import com.woaiqw.sdk_share.model.AppId;
import com.woaiqw.sdk_share.model.ShareBean;
import com.woaiqw.sdk_share.share.QQShare;
import com.woaiqw.sdk_share.share.SineShare;
import com.woaiqw.sdk_share.share.WXShare;
import com.woaiqw.sdk_share.ShareListener;
import com.woaiqw.sdk_share.utils.SerializeUtils;


public class ShareActivity extends Activity implements WbShareCallback, ShareListener {


    public static final int SHARE_STATUS_COMPLETE = 1;
    public static final int SHARE_STATUS_CANCEL = 2;
    public static final int SHARE_STATUS_ERROR = 3;


    private static final String SHARE_ENTRY = "ShareBean";
    private static final String SHARE_CHANNEL = "ShareChannel";


    public static final String RESULT_CHANNLE = "result_channle";
    public static final String RESULT_STATUS = "result_message";

    public static Intent getIntent(Context context, ShareBean share, int type) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(SHARE_ENTRY, share);
        intent.putExtra(SHARE_CHANNEL, type);
        return intent;
    }


    int mShareType;
    WXShare mWXShare;
    SineShare mSineShare;
    QQShare mQQShare;
    AppId appId;
    ShareBean shareBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Intent intent = getIntent();
        shareBean = intent.getParcelableExtra(SHARE_ENTRY);
        mShareType = intent.getIntExtra(SHARE_CHANNEL, -1);
        appId = (AppId) SerializeUtils.deserialization(ShareSdkProxy.getInstance().getSerializePath(this.getApplication()));
        switch (mShareType) {
            case ShareChannel.CHANNEL_WECHAT:
                weiXinFriend();
                break;
            case ShareChannel.CHANNEL_WECHAT_MOMENT:
                weiXinCircle();
                break;
            case ShareChannel.CHANNEL_QQ:
                qqShare();
                break;
            case ShareChannel.CHANNEL_QQ_ZONE:
                qZoneShare();
                break;
            case ShareChannel.CHANNEL_WEIBO:
                weiBoShare();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mSineShare != null) {
            mSineShare.doResultIntent(intent, this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mQQShare != null) {
            mQQShare.onActivityResultData(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWXShare != null) {
            mWXShare.unregisterWeixinReceiver();
        }
    }

    private void weiXinFriend() {
        mWXShare = new WXShare(this, appId.getWECHAT(), false);
        mWXShare.registerWeixinReceiver();
        mWXShare.sendWebShareMessage(shareBean, this);
    }

    private void weiXinCircle() {
        mWXShare = new WXShare(this, appId.getWECHAT(), true);
        mWXShare.registerWeixinReceiver();
        mWXShare.sendWebShareMessage(shareBean, this);
    }

    private void weiBoShare() {
        mSineShare = new SineShare(this, appId.getWEIBO());
        mSineShare.sendWebShareMessage(shareBean);
    }

    private void qqShare() {
        mQQShare = new QQShare(this, appId.getQQ(), true);
        mQQShare.sendWebShareMessage(shareBean, this);
    }

    private void qZoneShare() {
        mQQShare = new QQShare(this, appId.getQQ(), false);
        mQQShare.sendWebShareMessage(shareBean, this);
    }


    @Override
    public void onWbShareSuccess() {
        onShareSuccess();
    }

    @Override
    public void onWbShareCancel() {
        onShareCancel();
    }

    @Override
    public void onWbShareFail() {
        onShareFail("分享失败");
    }

    @Override
    public void onShareSuccess() {
        finishWithResult(SHARE_STATUS_COMPLETE);
    }


    @Override
    public void onShareCancel() {
        finishWithResult(SHARE_STATUS_CANCEL);
    }

    @Override
    public void onShareFail(String message) {
        finishWithResult(SHARE_STATUS_ERROR);
    }

    public void finishWithResult(final int status) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_CHANNLE, mShareType);
        intent.putExtra(RESULT_STATUS, status);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
