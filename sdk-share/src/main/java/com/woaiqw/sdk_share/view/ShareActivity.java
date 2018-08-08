package com.woaiqw.sdk_share.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.share.WbShareCallback;
import com.woaiqw.sdk_share.R;
import com.woaiqw.sdk_share.ShareChannel;
import com.woaiqw.sdk_share.ShareListener;
import com.woaiqw.sdk_share.ShareStatus;
import com.woaiqw.sdk_share.model.AppId;
import com.woaiqw.sdk_share.model.ShareBean;
import com.woaiqw.sdk_share.share.QQShare;
import com.woaiqw.sdk_share.share.SineShare;
import com.woaiqw.sdk_share.share.WXShare;
import com.woaiqw.sdk_share.utils.SerializeUtils;

import static com.woaiqw.sdk_share.utils.Utils.getSerializePath;


public class ShareActivity extends Activity implements WbShareCallback, ShareListener {

    private static final String SHARE_ENTRY = "ShareBean";
    private static final String SHARE_CHANNEL = "ShareChannel";


    public static final String RESULT_CHANNEL = "result_channel";
    public static final String RESULT_STATUS = "result_msg";

    public static Intent getIntent(Context context, ShareBean share, int type) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(SHARE_ENTRY, share);
        intent.putExtra(SHARE_CHANNEL, type);
        return intent;
    }


    private int mShareType;
    private WXShare mWXShare;
    private SineShare mSineShare;
    private QQShare mQQShare;
    private AppId appId;
    private ShareBean shareBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Intent intent = getIntent();
        shareBean = intent.getParcelableExtra(SHARE_ENTRY);
        mShareType = intent.getIntExtra(SHARE_CHANNEL, -1);
        appId = (AppId) SerializeUtils.deserialization(getSerializePath(this.getApplication()));
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
            mWXShare.unregisterWXReceiver();
        }
    }

    private void weiXinFriend() {
        mWXShare = new WXShare(this, appId.getWECHAT(), false);
        mWXShare.registerWXReceiver();
        mWXShare.sendWebShareMessage(shareBean, this);
    }

    private void weiXinCircle() {
        mWXShare = new WXShare(this, appId.getWECHAT(), true);
        mWXShare.registerWXReceiver();
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
        onShareFail();
    }

    @Override
    public void onShareSuccess() {
        finishWithResult(ShareStatus.SHARE_STATUS_COMPLETE);
    }


    @Override
    public void onShareCancel() {
        finishWithResult(ShareStatus.SHARE_STATUS_CANCEL);
    }

    @Override
    public void onShareFail() {
        finishWithResult(ShareStatus.SHARE_STATUS_ERROR);
    }

    public void finishWithResult(@ShareStatus final int status) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_CHANNEL, mShareType);
        intent.putExtra(RESULT_STATUS, status);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
