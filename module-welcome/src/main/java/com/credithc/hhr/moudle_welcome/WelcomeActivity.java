package com.credithc.hhr.moudle_welcome;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.credithc.hhr.library_common.config.ActionConstants;
import com.credithc.hhr.library_common.config.EventConstants;
import com.credithc.hhr.module_welcome.R;
import com.woaiqw.avatar.Avatar;
import com.woaiqw.avatar.annotation.Subscribe;
import com.woaiqw.base.common.BaseActivity;
import com.woaiqw.base.utils.PermissionListener;
import com.woaiqw.base.utils.PermissionUtils;
import com.woaiqw.base.utils.WeakHandler;
import com.woaiqw.scm_api.SCM;
import com.woaiqw.scm_api.ScCallback;

import java.util.List;

/**
 * Created by haoran on 2018/9/7.
 */
public class WelcomeActivity extends BaseActivity {

    private WeakHandler h = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    }, Looper.getMainLooper());

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            entryMain();
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.welcome_activity_welcome;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        Avatar.get().register(this);
        PermissionUtils.requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(List<String> deniedPermissions) {

            }
        });
        h.postDelayed(task, 3000);
    }

    private void entryMain() {

        try {
            SCM.get().req(this, ActionConstants.ENTRY_MAIN_PAGE, new ScCallback() {
                @Override
                public void onCallback(boolean b, String data, String tag) {
                    if (b) {
                        Toast.makeText(WelcomeActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe(tag = EventConstants.FINISH_WELCOME_PAGE)
    public void welcomeProcessGcAndReleaseSome(String data) {
        Toast.makeText(WelcomeActivity.this, data, Toast.LENGTH_SHORT).show();
        WelcomeActivity.this.finish();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
        activityManager.killBackgroundProcesses("com.credithc.hhr:welcome");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Avatar.get().unregister(this);
        h.removeCallbacksAndMessages(null);
    }


}
