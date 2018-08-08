package com.woaiqw.sdk_share.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.woaiqw.sdk_share.ShareSdkProxy;

public interface IShareView {

    IShareView createShareDialog(int[] shareChannel,int spanCount);

    int show(FragmentTransaction transaction);

    void show(FragmentManager manager);

    void dismissDialog();

    void setOnShareClickListener(ShareSdkProxy.OnShareClickListener listener);
}
