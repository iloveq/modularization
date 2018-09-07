package com.credithc.hhr.module_main.actions;

import android.content.Context;
import android.content.Intent;

import com.credithc.hhr.library_common.config.ActionConstants;
import com.credithc.hhr.module_main.view.activity.MainActivity;
import com.woaiqw.scm_annotation.annotion.Action;
import com.woaiqw.scm_api.ScAction;
import com.woaiqw.scm_api.ScCallback;

/**
 * Created by haoran on 2018/9/7.
 */
@Action(name = ActionConstants.ENTRY_MAIN_PAGE)
public class EntryMainPageAction implements ScAction {
    @Override
    public void invoke(Context context, String param, ScCallback callback) {
        context.startActivity(new Intent(context, MainActivity.class));
        callback.onCallback(true,"success","EntryMainPageAction");
    }
}
