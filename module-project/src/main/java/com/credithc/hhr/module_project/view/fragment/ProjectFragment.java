package com.credithc.hhr.module_project.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.credithc.hhr.module_project.R;
import com.credithc.hhr.module_project.R2;
import com.woaiqw.base.common.BaseFragment;

import butterknife.BindView;

/**
 * Created by haoran on 2018/7/26.
 */

public class ProjectFragment extends BaseFragment {

    @BindView(R2.id.project_tv_jump_2_detail)
    TextView tv;

    @Override
    protected int getLayoutId() {
        return R.layout.project_fragment_project;
    }

    @Override
    protected void afterCreate(Bundle bundle) {


    }
}
