### app

1：空壳app，编译项目

2：AndroidManifest 负责项目主进程等配置

3：application 初始化，加载配置


#### 1:build.gradle ：

```
apply plugin: 'com.android.application'


android {
    compileSdkVersion var.compileSdkVersion
    buildToolsVersion var.buildToolsVersion

    defaultConfig {
        applicationId "com.credithc.hhr"
        minSdkVersion var.minSdkVersion
        targetSdkVersion var.targetSdkVersion
        versionCode var.versionCode
        versionName var.versionName
    }


    buildTypes {
        debug {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }

        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

    resourcePrefix "app_"
}

dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'
    implementation project(':library-common')
    if (isBuildAll.toBoolean()) {
        implementation project(':module-main')
        implementation project(':module-home')
        implementation project(':module-login')
        implementation project(':module-mine')
        implementation project(':module-project')
        implementation project(':module-register')
    }

}
```


#### 2:AndroidManifest ：

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.credithc.hhr">


    <application
        android:name=".App"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
    </application>

</manifest>
```


#### 3:application ：

```
public class App extends BaseApp {

    static {

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new MaterialHeader(context).setShowBezierWave(false);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });

    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
        AFrameProxy.getInstance().initAFrame(new AFrameBinder() {
            @Override
            public Class getApiService() {
                return IApiService.class;
            }

            @Override
            public OkHttpClient getOkHttpClient() {
                return OkHttpHelper.getInstance().getClient();
            }

            @Override
            public String getServerHost() {
                return "http://118.89.233.211:3000/api/";
            }


            @Override
            public Converter.Factory getConverterFactory() {
                return GsonConverterFactory.create();
            }

            @Override
            public CallAdapter.Factory getCallAdapterFactory() {
                return RxJava2CallAdapterFactory.create();
            }
        });
    }
}

```