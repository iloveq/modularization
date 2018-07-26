package com.credithc.hhr.module_home.view.fragment;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.credithc.hhr.library_common.config.ARouterConstant;
import com.credithc.hhr.module_home.R;
import com.woaiqw.base.common.BaseFragment;

/**
 * Created by haoran on 2018/7/26.
 */
@Route(path = ARouterConstant.home_fragment_router_path)
public class HomeFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_home;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }
}
