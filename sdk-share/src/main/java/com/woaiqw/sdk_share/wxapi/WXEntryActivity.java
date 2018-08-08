package com.woaiqw.sdk_share.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.woaiqw.sdk_share.model.AppId;
import com.woaiqw.sdk_share.share.WXShare;
import com.woaiqw.sdk_share.utils.SerializeUtils;

import static com.woaiqw.sdk_share.utils.Utils.getSerializePath;


/**
 * 微信客户端回调activity示例
 *
 * @zshh 修改微信登陆过程中获取code, 之后发送消息給, LoginActivity, Activity直接进行页面跳转．＠91行
 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI mWxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppId appId = (AppId) SerializeUtils.deserialization(getSerializePath(this.getApplication()));
        mWxapi = WXAPIFactory.createWXAPI(this, appId.getWECHAT());
        mWxapi.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        WXShare.sendBroadcast(this, resp.errCode);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxapi.handleIntent(intent, this);
        finish();
    }

}
