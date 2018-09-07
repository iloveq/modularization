package com.credithc.hhr.module_main.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.credithc.hhr.library_common.config.Constant;
import com.credithc.hhr.module_main.R;
import com.credithc.hhr.module_main.R2;
import com.credithc.hhr.module_main.view.fragment.InitExceptionFragment;
import com.credithc.hhr.module_main.view.widget.TabView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by haoran on 2018/7/26.
 */

public class MainActivity extends AppCompatActivity {

    private Fragment[] fragments;
    private TabView[] mTabs;
    private int currentTabIndex;

    @BindView(R2.id.tvTabHome)
    TabView tabHome;
    @BindView(R2.id.tvTabProject)
    TabView tabProject;
    @BindView(R2.id.tvTabMine)
    TabView tabMine;

    Fragment homeFragment, projectFragment, mineFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_main);
        ButterKnife.bind(this);
        mTabs = new TabView[]{tabHome, tabProject, tabMine};
        tabHome.setChecked(true);

        try {
            homeFragment = (Fragment) Class.forName(Constant.fragment_home).newInstance();
        } catch (Exception e) {
            homeFragment = InitExceptionFragment.newInstance(e.getMessage());
        }
        try {
            projectFragment = (Fragment) Class.forName(Constant.fragment_project).newInstance();
        } catch (Exception e) {
            projectFragment = InitExceptionFragment.newInstance(e.getMessage());
        }
        try {
            mineFragment = (Fragment) Class.forName(Constant.fragment_mine).newInstance();
        } catch (Exception e) {
            mineFragment = InitExceptionFragment.newInstance(e.getMessage());
        }

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
            return;
        }
        if (view.getId() == R.id.tvTabProject) {
            showFragment(1);
            return;
        }
        if (view.getId() == R.id.tvTabMine) {
            showFragment(2);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < mTabs.length; i++) {
            mTabs[i] = null;
            fragments[i] = null;
        }
        homeFragment = null;
        projectFragment = null;
        mineFragment = null;
        currentTabIndex = 0;
    }
}
