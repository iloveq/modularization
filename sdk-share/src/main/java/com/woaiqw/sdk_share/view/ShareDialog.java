package com.woaiqw.sdk_share.view;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.woaiqw.sdk_share.R;
import com.woaiqw.sdk_share.ShareChannel;
import com.woaiqw.sdk_share.ShareSdkProxy;

/**
 * Created by haoran on 2018/8/6.
 */

public class ShareDialog extends DialogFragment implements IShareView, View.OnClickListener {

    private TextView tvShareCancel;
    private RecyclerView mRecyclerView;

    private static final String KEY = "share_dialog_channel";
    private static final String CHANNEL = "channel";
    private static final String COUNT = "span_count";
    private ShareSdkProxy.OnShareClickListener listener;

    @Override
    public void setOnShareClickListener(ShareSdkProxy.OnShareClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_bottom_share);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        mRecyclerView = dialog.findViewById(R.id.recycler_view_share);
        tvShareCancel = dialog.findViewById(R.id.tv_share_cancel);
        int[] arr = getArguments().getIntArray(CHANNEL);
        int count = getArguments().getInt(COUNT);
        assert arr != null;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), count == 0 ? 4 : count));
        mRecyclerView.setAdapter(new ShareAdapter(arr));
        tvShareCancel.setOnClickListener(this);
        return dialog;
    }


    @Override
    public IShareView createShareDialog(int[] shareChannel, int spanCount) {
        Bundle bundle = new Bundle();
        bundle.putIntArray(CHANNEL, shareChannel);
        bundle.putInt(COUNT, spanCount);
        ShareDialog dialog = new ShareDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int show(FragmentTransaction transaction) {
        return super.show(transaction, KEY);
    }

    @Override
    public void show(FragmentManager manager) {
        super.show(manager, KEY);
    }

    @Override
    public void onClick(View v) {
        dismissDialog();
    }

    @Override
    public void dismissDialog() {
        listener.onShareClick(ShareChannel.CHANNEL_CANNEL);
        dismissAllowingStateLoss();
    }

    public static ShareDialog get() {
        return new ShareDialog();
    }


    private class ShareAdapter extends RecyclerView.Adapter<MyShareHolder> {

        private int[] resId;
        private String[] name;

        ShareAdapter(int[] arr) {
            resId = new int[arr.length];
            name = new String[arr.length];
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == ShareChannel.CHANNEL_WECHAT) {
                    resId[i] = R.drawable.share_wx;
                    name[i] = "微信";
                } else if (arr[i] == ShareChannel.CHANNEL_WECHAT_MOMENT) {
                    resId[i] = R.drawable.share_wx_moment;
                    name[i] = "朋友圈";
                } else if (arr[i] == ShareChannel.CHANNEL_QQ) {
                    resId[i] = R.drawable.share_qq;
                    name[i] = "QQ";
                } else if (arr[i] == ShareChannel.CHANNEL_QQ_ZONE) {
                    resId[i] = R.drawable.share_qq_zone;
                    name[i] = "QQ空间";
                } else if (arr[i] == ShareChannel.CHANNEL_WEIBO) {
                    resId[i] = R.drawable.share_sine;
                    name[i] = "微博";
                } else if (arr[i] == ShareChannel.CHANNEL_MORE) {
                    resId[i] = R.drawable.share_more;
                    name[i] = "更多";
                } else {
                    throw new RuntimeException(" ShareChannel 数组初始化错误 ");
                }
            }
        }

        @Override
        public MyShareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyShareHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false));
        }

        @Override
        public void onBindViewHolder(MyShareHolder holder, int position) {
            final int pos = holder.getAdapterPosition();
            holder.tvName.setText(name[pos]);
            Drawable drawable = getContext().getResources().getDrawable(resId[pos]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tvName.setCompoundDrawables(null, drawable, null, null);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (name[pos]) {
                        case "微信":
                            listener.onShareClick(ShareChannel.CHANNEL_WECHAT);
                            break;
                        case "朋友圈":
                            listener.onShareClick(ShareChannel.CHANNEL_WECHAT_MOMENT);
                            break;
                        case "QQ":
                            listener.onShareClick(ShareChannel.CHANNEL_QQ);
                            break;
                        case "QQ空间":
                            listener.onShareClick(ShareChannel.CHANNEL_QQ_ZONE);
                            break;
                        case "微博":
                            listener.onShareClick(ShareChannel.CHANNEL_WEIBO);
                            break;
                        case "更多":
                            listener.onShareClick(ShareChannel.CHANNEL_MORE);
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return resId.length;
        }

    }

    private static class MyShareHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        MyShareHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_share);
        }
    }
}
