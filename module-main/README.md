### main

1：组件入口  SplashActivity  MainActivity

```
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
            homeFragment = (Fragment) ARouter.getInstance().build(ARouterConstant.fragment_home_router_path).navigation();
        } catch (InitException e) {
            homeFragment = InitExceptionFragment.newInstance(e.getMessage());
        }
        try {
            projectFragment = (Fragment) ARouter.getInstance().build(ARouterConstant.fragment_project_router_path).navigation();
        } catch (InitException e) {
            projectFragment = InitExceptionFragment.newInstance(e.getMessage());
        }
        try {
            mineFragment = (Fragment) ARouter.getInstance().build(ARouterConstant.fragment_mine_router_path).navigation();
        } catch (InitException e) {
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
```

2：通过 ARouter 初始化(反射 newInstance) MainActivity 里的 3 个 模块的 Fragment (职责分离)

```
case FRAGMENT:
                Class fragmentMeta = postcard.getDestination();
                try {
                    Object instance = fragmentMeta.getConstructor().newInstance();
                    if (instance instanceof Fragment) {
                        ((Fragment) instance).setArguments(postcard.getExtras());
                    } else if (instance instanceof android.support.v4.app.Fragment) {
                        ((android.support.v4.app.Fragment) instance).setArguments(postcard.getExtras());
                    }

                    return instance;
                } catch (Exception ex) {
                    logger.error(Consts.TAG, "Fetch fragment instance error, " + TextUtils.formatStackTrace(ex.getStackTrace()));
                }
```

3：UI 上 使用 TabView ( 通过扩展 TextView ) 将 NavigationBottomView 的 布局层次降低，减少 GPU 过度绘制

```
 <LinearLayout
        android:id="@+id/llAllTab"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:background="@null"
        android:orientation="horizontal">

        <com.credithc.hhr.module_main.view.widget.TabView
            android:id="@+id/tvTabHome"
            style="@style/main_tab_view"
            android:drawableTop="@drawable/main_tab1_ico_selector"
            android:text="首页" />

        <com.credithc.hhr.module_main.view.widget.TabView
            android:id="@+id/tvTabProject"
            style="@style/main_tab_view"
            android:drawableTop="@drawable/main_tab2_ico_selector"
            android:text="项目" />

        <com.credithc.hhr.module_main.view.widget.TabView
            android:id="@+id/tvTabMine"
            style="@style/main_tab_view"
            android:drawableTop="@drawable/main_tab3_ico_selector"
            android:text="我的" />
    </LinearLayout>
```

4：gradle 和 MainApp 配置 使模块可单独运行

```
if(isBuildAll.toBoolean()||isLibraryMain.toBoolean()){
    apply plugin: 'com.android.library'
}else {
    apply plugin: 'com.android.application'
}

apply plugin:'com.jakewharton.butterknife'

android {

    compileSdkVersion var.compileSdkVersion
    buildToolsVersion var.buildToolsVersion

    defaultConfig {
        minSdkVersion var.minSdkVersion
        targetSdkVersion var.targetSdkVersion
        versionCode var.versionCode
        versionName var.versionName
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [ moduleName : project.getName() ]
            }
        }
    }

    sourceSets {
        main {
            if (isBuildAll.toBoolean() || isLibraryMain.toBoolean()) {
                manifest.srcFile 'src/main/AndroidManifest.xml'

                //We'll not compile the debug folder when building all.
                java {
                    exclude 'module/**'
                }
            } else {
                manifest.srcFile 'src/main/java/module/AndroidManifest.xml'
            }
        }
    }

    resourcePrefix "main_"

}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    annotationProcessor "com.jakewharton:butterknife-compiler:${versions.butterknife}"
    annotationProcessor "com.alibaba:arouter-compiler:${versions.arouter_compiler}"
    implementation project(':library-common')

    if(!isLibraryMain.toBoolean()){
        implementation project(':module-home')
        implementation project(':module-project')
        implementation project(':module-mine')
    }
}

```