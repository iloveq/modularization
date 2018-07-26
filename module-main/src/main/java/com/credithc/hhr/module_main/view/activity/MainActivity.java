package com.credithc.hhr.module_main.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.credithc.hhr.library_common.config.ARouterConstant;
import com.credithc.hhr.module_main.R;
import com.credithc.hhr.module_main.R2;
import com.woaiqw.base.common.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by haoran on 2018/7/26.
 */

public class MainActivity extends BaseActivity {

    private Fragment[] fragments;
    private LinearLayout[] mTabs;
    private int currentTabIndex;
    @BindView(R2.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R2.id.llAllTab)
    LinearLayout llAllTab;
    @BindView(R2.id.llTabHome)
    LinearLayout tabHome;
    @BindView(R2.id.llTabProject)
    LinearLayout tabProject;
    @BindView(R2.id.llTabMine)
    LinearLayout tabMine;


    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_main;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        initTabButton();
        Fragment homeFragment = (Fragment) ARouter.getInstance().build(ARouterConstant.home_fragment_router_path).navigation();
        Fragment projectFragment = (Fragment) ARouter.getInstance().build(ARouterConstant.project_fragment_router_path).navigation();
        Fragment mineFragment = (Fragment) ARouter.getInstance().build(ARouterConstant.mine_fragment_router_path).navigation();
        fragments = new Fragment[]{homeFragment, projectFragment, mineFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragments[0]).show(fragments[0]).commitAllowingStateLoss();
        showFragment(0);
    }

    /**
     * 初始化Tab
     */
    private void initTabButton() {

        mTabs = new LinearLayout[3];
        mTabs[0] = tabHome;
        mTabs[1] = tabProject;
        mTabs[2] = tabMine;
        mTabs[0].setSelected(true);
    }

    /**
     * 展示/切换Fragment
     *
     * @param index
     */
    private void showFragment(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commitAllowingStateLoss();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;

        changeTabColor(currentTabIndex);
    }

    /**
     * 切换时变色
     *
     * @param index
     */
    private void changeTabColor(int index) {
        for (int i = 0; i < llAllTab.getChildCount(); i++) {

            LinearLayout currentView = (LinearLayout) llAllTab.getChildAt(i);
            ImageView currentImage = (ImageView) currentView.getChildAt(0);
            TextView current_TextView = (TextView) currentView.getChildAt(1);
            switch (i) {
                case 0:
                    if (index == i) {
                        currentImage.setImageResource(R.mipmap.home_ic_tab_home_check);
                    } else {
                        currentImage.setImageResource(R.mipmap.home_ic_tab_home_default);
                    }
                    break;

                case 1:
                    if (index == i) {
                        currentImage.setImageResource(R.mipmap.home_ic_tab_products_check);
                    } else {
                        currentImage.setImageResource(R.mipmap.home_ic_tab_products_default);
                    }
                    break;

                case 2:
                    if (index == i) {
                        currentImage.setImageResource(R.mipmap.home_ic_tab_my_check);
                    } else {
                        currentImage.setImageResource(R.mipmap.home_ic_tab_my_default);
                    }
                    break;
                default:
                    break;
            }

            if (i == index) {
                current_TextView.setTextColor(this.getResources().getColor(R.color.main_tab_text_color_select));
            } else {
                current_TextView.setTextColor(this.getResources().getColor(R.color.main_tab_text_color_unselect));
            }
        }
    }


    @OnClick({R2.id.llTabProject, R2.id.llTabMine, R2.id.llTabHome})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.llTabHome) {
            showFragment(0);
        } else if (view.getId() == R.id.llTabProject) {
            showFragment(1);
        } else if (view.getId() == R.id.llTabMine) {
            showFragment(2);
        }

    }
}
