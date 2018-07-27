package com.credithc.hhr.module_main.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.credithc.hhr.library_common.config.ARouterConstant;
import com.credithc.hhr.module_main.R;
import com.credithc.hhr.module_main.R2;
import com.credithc.hhr.module_main.view.widget.TabView;
import com.woaiqw.base.common.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by haoran on 2018/7/26.
 */

public class MainActivity extends BaseActivity {

    private Fragment[] fragments;
    private TabView[] mTabs;
    private int currentTabIndex;
    @BindView(R2.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R2.id.llAllTab)
    LinearLayout llAllTab;
    @BindView(R2.id.tvTabHome)
    TabView tabHome;
    @BindView(R2.id.tvTabProject)
    TabView tabProject;
    @BindView(R2.id.tvTabMine)
    TabView tabMine;


    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_main;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        mTabs = new TabView[]{tabHome, tabProject, tabMine};
        tabHome.setChecked(true);
        Fragment homeFragment = (Fragment) ARouter.getInstance().build(ARouterConstant.home_fragment_router_path).navigation();
        Fragment projectFragment = (Fragment) ARouter.getInstance().build(ARouterConstant.project_fragment_router_path).navigation();
        Fragment mineFragment = (Fragment) ARouter.getInstance().build(ARouterConstant.mine_fragment_router_path).navigation();
        fragments = new Fragment[]{homeFragment, projectFragment, mineFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragments[0]).show(fragments[0]).commitAllowingStateLoss();
        showFragment(0);
    }


    /**
     * 展示/切换Fragment
     *
     * @param index
     */
    private void showFragment(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                ft.add(R.id.fragment_container, fragments[index]);
            }
            ft.show(fragments[index]).commitAllowingStateLoss();
        }
        mTabs[currentTabIndex].setChecked(false);
        // 把当前tab设为选中状态
        mTabs[index].setChecked(true);
        currentTabIndex = index;
    }


    @OnClick({R2.id.tvTabHome, R2.id.tvTabProject, R2.id.tvTabMine})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tvTabHome) {
            showFragment(0);
        } else if (view.getId() == R.id.tvTabProject) {
            showFragment(1);
        } else if (view.getId() == R.id.tvTabMine) {
            showFragment(2);
        }

    }
}
