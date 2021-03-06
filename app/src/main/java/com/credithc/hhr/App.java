package com.credithc.hhr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.credithc.hhr.library_common.IApiService;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.woaiqw.avatar.Avatar;
import com.woaiqw.avatar.utils.ProcessUtil;
import com.woaiqw.base.AFrameBinder;
import com.woaiqw.base.AFrameProxy;
import com.woaiqw.base.common.BaseApp;
import com.woaiqw.base.network.OkHttpHelper;
import com.woaiqw.scm_api.SCM;
import com.woaiqw.sdk_share.ShareSdkProxy;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haoran on 2018/5/13.
 */

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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initMultiProcessCoreLib();

        //非主进程，至此返回
        if (!ProcessUtil.isMainProcess(this)) return;

        //init主进程库
        initMainProcessCoreLib();


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Avatar.recycleSource();
    }

    private void initMultiProcessCoreLib() {

        Avatar.init(this);

        SCM.get().scanningSCMTable(new String[]{"Main", "Home", "Login", "Mine", "Register", "Project", "Web"});

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
        ShareSdkProxy.getInstance().init(this, new String[]{"214506", "wxa552e31d6839de85", "1550938859"});
    }

    private void initMainProcessCoreLib() {

    }
}
