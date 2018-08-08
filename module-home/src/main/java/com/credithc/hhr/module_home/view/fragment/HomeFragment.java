package com.credithc.hhr.module_home.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.credithc.hhr.library_common.config.ARouterConstant;
import com.credithc.hhr.module_home.R;
import com.credithc.hhr.module_home.R2;
import com.woaiqw.base.common.BaseFragment;
import com.woaiqw.base.utils.ToastUtil;
import com.woaiqw.sdk_share.ShareChannel;
import com.woaiqw.sdk_share.ShareSdkProxy;
import com.woaiqw.sdk_share.ShareStatus;
import com.woaiqw.sdk_share.model.ShareBean;
import com.woaiqw.sdk_share.view.IShareView;
import com.woaiqw.sdk_share.view.ShareActivity;

import butterknife.BindView;

/**
 * Created by haoran on 2018/7/26.
 */
@Route(path = ARouterConstant.fragment_home_router_path)
public class HomeFragment extends BaseFragment {

    @BindView(R2.id.tv_home)
    TextView tv_home;

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_home;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IShareView shareDialog = ShareSdkProxy.getInstance().createShareDialog(new int[]{ShareChannel.CHANNEL_QQ, ShareChannel.CHANNEL_WEIBO, ShareChannel.CHANNEL_WECHAT_MOMENT, ShareChannel.CHANNEL_QQ_ZONE}, 4);
                ShareSdkProxy.getInstance().setOnShareClickListener(shareDialog, getActivity(), new ShareBean("分享了", "今天天气不错", "http://118.89.233.211:3000/images/1530106897838_.jpg", R.drawable.ic_launcher, "http://www.baidu.com"));
                shareDialog.show(getFragmentManager());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            int status = data.getIntExtra(ShareActivity.RESULT_STATUS, -1);
            switch (status) {
                case ShareStatus.SHARE_STATUS_COMPLETE:
                    ToastUtil.showShortToast("分享成功");
                    break;
                case ShareStatus.SHARE_STATUS_ERROR:
                    ToastUtil.showShortToast("分享失败");
                    break;
                case ShareStatus.SHARE_STATUS_CANCEL:
                    ToastUtil.showShortToast("取消分享");
                    break;
            }
        }
    }
}
