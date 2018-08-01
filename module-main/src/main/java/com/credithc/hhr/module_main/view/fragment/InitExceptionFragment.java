package com.credithc.hhr.module_main.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.credithc.hhr.module_main.R;
import com.credithc.hhr.module_main.R2;
import com.woaiqw.base.common.BaseFragment;

import butterknife.BindView;

/**
 * Created by haoran on 2018/8/1.
 */

public class InitExceptionFragment extends BaseFragment {

    @BindView(R2.id.tv_init_info)
    TextView tv_init_info;
    private static final String INFO = "init_exception_fragment_info";

    public static InitExceptionFragment newInstance(String info) {
        InitExceptionFragment fragment = new InitExceptionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INFO, info);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment_init_exception;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        String info = getArguments().getString(INFO);
        tv_init_info.setText(TextUtils.isEmpty(info) ? "unknown error" : "error:" + info);
    }
}
