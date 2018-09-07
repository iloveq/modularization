package com.credithc.hhr.module_project.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.credithc.hhr.library_common.config.EventConstants;
import com.credithc.hhr.module_project.R;
import com.credithc.hhr.module_project.R2;
import com.woaiqw.avatar.Avatar;
import com.woaiqw.base.common.BaseActivity;

import butterknife.BindView;

/**
 * Created by haoran on 2018/9/7.
 */
public class ProjectDetailActivity extends BaseActivity {

    @BindView(R2.id.project_tv_close_main)
    TextView tv;

    @Override
    protected int getLayoutId() {
        return R.layout.project_activity_project_detail;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Avatar.get().post(EventConstants.CLOSE_MAIN_PAGE,"close-main");
            }
        });
    }
}
