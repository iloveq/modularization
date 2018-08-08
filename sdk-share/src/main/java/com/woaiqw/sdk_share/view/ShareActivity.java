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


    private int shareType;
    private WXShare wxShare;
    private SineShare sineShare;
    private QQShare qqShare;
    private AppId appId;
    private ShareBean shareBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Intent intent = getIntent();
        shareBean = intent.getParcelableExtra(SHARE_ENTRY);
        shareType = intent.getIntExtra(SHARE_CHANNEL, -1);
        appId = (AppId) SerializeUtils.deserialization(getSerializePath(this.getApplication()));
        switch (shareType) {
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
        if (sineShare != null) {
            sineShare.doResultIntent(intent, this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (qqShare != null) {
            qqShare.onActivityResultData(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wxShare != null) {
            wxShare.unregisterWXReceiver();
        }
    }

    private void weiXinFriend() {
        wxShare = new WXShare(this, appId.getWECHAT(), false);
        wxShare.registerWXReceiver();
        wxShare.sendWebShareMessage(shareBean, this);
    }

    private void weiXinCircle() {
        wxShare = new WXShare(this, appId.getWECHAT(), true);
        wxShare.registerWXReceiver();
        wxShare.sendWebShareMessage(shareBean, this);
    }

    private void weiBoShare() {
        sineShare = new SineShare(this, appId.getWEIBO());
        sineShare.sendWebShareMessage(shareBean);
    }

    private void qqShare() {
        qqShare = new QQShare(this, appId.getQQ(), true);
        qqShare.sendWebShareMessage(shareBean, this);
    }

    private void qZoneShare() {
        qqShare = new QQShare(this, appId.getQQ(), false);
        qqShare.sendWebShareMessage(shareBean, this);
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
        intent.putExtra(RESULT_CHANNEL, shareType);
        intent.putExtra(RESULT_STATUS, status);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
