package com.credithc.hhr.module_mine.view.fragment;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.credithc.hhr.library_common.config.ARouterConstant;

import com.credithc.hhr.module_mine.R;
import com.woaiqw.base.common.BaseFragment;

/**
 * Created by haoran on 2018/7/26.
 */
@Route(path = ARouterConstant.mine_fragment_router_path)
public class MineFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment_mine;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }
}
