package com.credithc.hhr.moudle_welcome;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.credithc.hhr.library_common.config.ActionConstants;
import com.credithc.hhr.library_common.config.EventConstants;
import com.credithc.hhr.module_welcome.R;
import com.woaiqw.avatar.Avatar;
import com.woaiqw.avatar.annotation.Subscribe;
import com.woaiqw.base.utils.WeakHandler;
import com.woaiqw.scm_api.SCM;
import com.woaiqw.scm_api.ScCallback;

/**
 * Created by haoran on 2018/9/7.
 */
public class WelcomeActivity extends AppCompatActivity {

    private WeakHandler h = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    }, Looper.getMainLooper());

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            entryMain();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_activity_welcome);
        Avatar.get().register(this);
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
