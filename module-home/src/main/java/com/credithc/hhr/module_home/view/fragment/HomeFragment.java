package com.credithc.hhr.module_home.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.credithc.hhr.library_common.config.ARouterConstant;
import com.credithc.hhr.module_home.R;
import com.credithc.hhr.module_home.R2;
import com.woaiqw.base.common.BaseFragment;
import com.woaiqw.sdk_share.ShareChannel;
import com.woaiqw.sdk_share.ShareSdkProxy;
import com.woaiqw.sdk_share.model.ShareBean;
import com.woaiqw.sdk_share.view.IShareView;

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
                IShareView shareDialog = ShareSdkProxy.getInstance().createShareDialog(new int[]{ShareChannel.CHANNEL_QQ, ShareChannel.CHANNEL_WEIBO, ShareChannel.CHANNEL_WECHAT_MOMENT, ShareChannel.CHANNEL_MORE}, 3);
                ShareSdkProxy.getInstance().setOnShareClickListener(shareDialog, getActivity(), new ShareBean("分享了", "今天天气不错", "", R.drawable.ic_launcher, "http://www.baidu.com"));
                shareDialog.show(getFragmentManager());
            }
        });
    }
}
