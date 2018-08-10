##### 代码设计：
```
1：单一职责原则   (Single Responsibility Principle, SRP)   一个类只负责一个功能领域中的相应职责
2：开闭原则       (Open-Closed Principle, OCP)             软件实体应对扩展开放，而对修改关闭
3：里氏代换原则   (Liskov Substitution Principle, LSP)     所有引用基类对象的地方能够透明地使用其子类的对象
4：依赖倒转原则   (Dependence  Inversion Principle, DIP)   抽象不应该依赖于细节，细节应该依赖于抽象
5：接口隔离原则   (Interface Segregation Principle, ISP)   使用多个专门的接口，而不使用单一的总接口
6：合成复用原则   (Composite Reuse Principle, CRP)         尽量使用对象组合，而不是继承来达到复用的目的
7：迪米特法则     (Law of Demeter, LoD)                    一个软件实体应当尽可能少地与其他实体发生相互作用
```
##### 我的 share-sdk 层次：
![share-sdk](https://upload-images.jianshu.io/upload_images/8886407-7ff420ae27cf213b.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 分析：
##### view层：
dialog(一般情况下的分享弹窗)，
ShareActivity(用一个透明的view来隔离三总分享回调)
##### model层：
AppId(三个平台注册的appid)
ShareBean(存放分享内容)
##### presenter层：
通过调起 ShareActivity 进而吊起 SDK (QQShare/WXShare/SineShare)
##### proxy:
提供sdk入口：初始化appid，创建分享dialog，设置点击监听 ，其他功能可根据情况扩展
##### 其他:
util：缩略图异步处理并dislrucache，序列化id等工具
@IntDef注解：将分享渠道和状态从代码里分离，解耦
share：将分享功能sdk处理放在这里
## 使用：
微信分享要调通必须要release包签名，包名，签名要和微信公众平台上的一致
##### 1: Application 里
（这些id先用美团的吧，注意替换自己的）
```
ShareSdkProxy.getInstance().init(this,new String[]{"214506","wxa552e31d6839de85","1550938859"});
```
##### 2: 自己的 Fragment 或 Acitvity 里
创建 分享视图
```
//创建 dialog
IShareView shareDialog = ShareSdkProxy.getInstance().createShareDialog(new int[]{ShareChannel.CHANNEL_QQ, ShareChannel.CHANNEL_WEIBO, ShareChannel.CHANNEL_WECHAT_MOMENT, ShareChannel.CHANNEL_QQ_ZONE}, 4);
// 设置点击回调和数据
ShareSdkProxy.getInstance().setOnShareClickListener(shareDialog, getActivity(), new ShareBean("分享了", "今天天气不错", "http://118.89.233.211:3000/images/1530106897838_.jpg", R.drawable.ic_launcher, "http://www.baidu.com"));
//展示 dialog
shareDialog.show(getFragmentManager());
```
##### 3: 自己的 Fragment 或 Acitvity 里
接收回调结果
```
@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            int status = data.getIntExtra(ShareActivity.RESULT_STATUS, -1);
            switch (status) {
                case ShareStatus.SHARE_STATUS_COMPLETE:
                    ToastUtil.showShortToast("分享成功");
                    break;
                case ShareStatus.SHARE_STATUS_ERROR:
                    ToastUtil.showShortToast("分享失败");
                    break;
                case ShareStatus.SHARE_STATUS_CANCEL:
                    ToastUtil.showShortToast("取消分享");
                    break;
            }
        }
    }
```
## 详情：
##### 1：入口ShareSdkProxy
```
public class ShareSdkProxy implements IShareSdkProxy {

    private ShareSdkProxy() {
    }

    public interface OnShareClickListener {
        void onShareClick(@ShareChannel int channel);
    }

    private static class Holder {
        private static final ShareSdkProxy IN = new ShareSdkProxy();
    }


    public static ShareSdkProxy getInstance() {
        return Holder.IN;
    }

    @Override
    public void init(Application app, String[] appIds) {

        if (app != null && appIds != null && appIds.length == 3) {
            try {
                SerializeUtils.serialization(getSerializePath(app), new AppId(appIds[0], appIds[1], appIds[2]));
            } catch (Exception e) {
                throw new RuntimeException(" ShareSdk serialized error： " + e);
            }
        } else {
            throw new RuntimeException(" ShareSdk init error ");
        }

    }


    /**
     * the channel turn can inflect the turn of IShareView( in this is a dialog )
     *
     * @param shareChannel the type of share function
     * @param column the column of dialog
     * @return the instance of dialog
     * @link ShareChannel.java
     */
    @Override
    public IShareView createShareDialog(int[] shareChannel, int column) {
        return ShareDialog.get().createShareDialog(shareChannel, column);
    }

    /**
     * @param dialog    dialog
     * @param activity  the current activity to accept the result of the share sdk
     * @param shareBean the model of share function
     */
    @Override
    public void setOnShareClickListener(IShareView dialog, final Activity activity, final ShareBean shareBean) {
        dialog.setOnShareClickListener(new OnShareClickListener() {
            @Override
            public void onShareClick(int channel) {
                switch (channel) {
                    case ShareChannel.CHANNEL_WECHAT:
                        SharePresenter.start(activity).onShareWx(shareBean);
                        break;
                    case ShareChannel.CHANNEL_WECHAT_MOMENT:
                        SharePresenter.start(activity).onShareWxCircle(shareBean);
                        break;
                    case ShareChannel.CHANNEL_QQ:
                        SharePresenter.start(activity).onShareQQ(shareBean);
                        break;
                    case ShareChannel.CHANNEL_QQ_ZONE:
                        SharePresenter.start(activity).onShareQZone(shareBean);
                        break;
                    case ShareChannel.CHANNEL_WEIBO:
                        SharePresenter.start(activity).onShareWeiBo(shareBean);
                        break;
                    case ShareChannel.CHANNEL_MORE:
                        break;
                    case ShareChannel.CHANNEL_CANNEL:
                        break;

                }
            }
        });
    }
}
```
##### 2：控制器：SharePresenter
控制model调起view（ShareActivity）
```
public class SharePresenter implements ISharePresenter {


    private static volatile SharePresenter presenter = null;

    public static ISharePresenter start(Activity activity) {
        if (presenter == null) {
            synchronized (SharePresenter.class) {
                if (presenter == null)
                    presenter = new SharePresenter(activity);
            }
        }
        return presenter;
    }

    private SharePresenter(Activity activity) {
        this.context = new WeakReference<>(activity);
    }

    private void startActivityForResult(int type, ShareBean entry) {
        Activity activity = context.get();
        assert activity != null : " activity weak reference maybe a null object ";
        activity.startActivityForResult(ShareActivity.getIntent(activity, entry, type), type);
    }

    private WeakReference<Activity> context;

    @Override
    public void onShareWeiBo(ShareBean entry) {
        startActivityForResult(ShareChannel.CHANNEL_WEIBO, entry);
    }

    @Override
    public void onShareWxCircle(ShareBean entry) {
        startActivityForResult(ShareChannel.CHANNEL_WECHAT_MOMENT, entry);
    }

    @Override
    public void onShareWx(ShareBean entry) {
        startActivityForResult(ShareChannel.CHANNEL_WECHAT, entry);
    }

    @Override
    public void onShareQQ(ShareBean entry) {
        startActivityForResult(ShareChannel.CHANNEL_QQ, entry);
    }

    @Override
    public void onShareQZone(ShareBean entry) {
        startActivityForResult(ShareChannel.CHANNEL_QQ_ZONE, entry);
    }

}
```
##### 3：隔离者，中间件：
ShareActivity（背景透明），处理分享逻辑
1:初始化appid和分享type以及share的数据
2:通过type 初始化 sdk
3:处理sdk回调，成功、失败、取消，因微信分享没有提供listener，所以在wxapi下发送广播send回调response，ShareActivity注册广播接收者，接收回调信息
4:回调完成，不管什么结构都要finish这个中间件，见finishWithResult方法
```
public class ShareActivity extends Activity implements WbShareCallback, ShareListener {

    private static final String SHARE_ENTRY = "ShareBean";
    private static final String SHARE_CHANNEL = "ShareChannel";


    public static final String RESULT_CHANNEL = "result_channel";
    public static final String RESULT_STATUS = "result_msg";

    public static Intent getIntent(Context context, ShareBean share, int type) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(SHARE_ENTRY, share);
        intent.putExtra(SHARE_CHANNEL, type);
        return intent;
    }


    private int shareType;
    private WXShare wxShare;
    private SineShare sineShare;
    private QQShare qqShare;
    private AppId appId;
    private ShareBean shareBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Intent intent = getIntent();
        shareBean = intent.getParcelableExtra(SHARE_ENTRY);
        shareType = intent.getIntExtra(SHARE_CHANNEL, -1);
        appId = (AppId) SerializeUtils.deserialization(getSerializePath(this.getApplication()));
        switch (shareType) {
            case ShareChannel.CHANNEL_WECHAT:
                weiXinFriend();
                break;
            case ShareChannel.CHANNEL_WECHAT_MOMENT:
                weiXinCircle();
                break;
            case ShareChannel.CHANNEL_QQ:
                qqShare();
                break;
            case ShareChannel.CHANNEL_QQ_ZONE:
                qZoneShare();
                break;
            case ShareChannel.CHANNEL_WEIBO:
                weiBoShare();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (sineShare != null) {
            sineShare.doResultIntent(intent, this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (qqShare != null) {
            qqShare.onActivityResultData(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wxShare != null) {
            wxShare.unregisterWXReceiver();
        }
    }

    private void weiXinFriend() {
        wxShare = new WXShare(this, appId.getWECHAT(), false);
        wxShare.registerWXReceiver();
        wxShare.sendWebShareMessage(shareBean, this);
    }

    private void weiXinCircle() {
        wxShare = new WXShare(this, appId.getWECHAT(), true);
        wxShare.registerWXReceiver();
        wxShare.sendWebShareMessage(shareBean, this);
    }

    private void weiBoShare() {
        sineShare = new SineShare(this, appId.getWEIBO());
        sineShare.sendWebShareMessage(shareBean);
    }

    private void qqShare() {
        qqShare = new QQShare(this, appId.getQQ(), true);
        qqShare.sendWebShareMessage(shareBean, this);
    }

    private void qZoneShare() {
        qqShare = new QQShare(this, appId.getQQ(), false);
        qqShare.sendWebShareMessage(shareBean, this);
    }


    @Override
    public void onWbShareSuccess() {
        onShareSuccess();
    }

    @Override
    public void onWbShareCancel() {
        onShareCancel();
    }

    @Override
    public void onWbShareFail() {
        onShareFail();
    }

    @Override
    public void onShareSuccess() {
        finishWithResult(ShareStatus.SHARE_STATUS_COMPLETE);
    }


    @Override
    public void onShareCancel() {
        finishWithResult(ShareStatus.SHARE_STATUS_CANCEL);
    }

    @Override
    public void onShareFail() {
        finishWithResult(ShareStatus.SHARE_STATUS_ERROR);
    }

    public void finishWithResult(@ShareStatus final int status) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_CHANNEL, shareType);
        intent.putExtra(RESULT_STATUS, status);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
```
##### 4：share目录下分享sdk调用
，QQShare、WXShare、SineShare，只举一个吧
WXShare：（通过flag判断微信好友还是朋友圈）
1：将 ShareBean 的数据添加到 SDK 的调用
2：处理图片
3：因没有 回调的listener，提供广播
```
public class WXShare {

    private static final String ACTION_WX_CALLBACK = "com.credithc.hhr.action.WX_CALLBACK";
    private static final String EXTRA_WX_RESULT = "wx_result";

    private final IWXAPI iwxapi;
    private WXShareReceiver receiver;
    private Context context;
    private ShareListener shareListener;
    private boolean isTimeLine;

    /**
     * constructor for WXShare
     *
     * @param context context
     * @param appId   微信 id
     * @param flag    true：好友 false：朋友圈
     */
    public WXShare(Context context, String appId, boolean flag) {
        this.context = context;
        iwxapi = WXAPIFactory.createWXAPI(context, appId);
        this.isTimeLine = flag;
    }

    /**
     * 注册微信回调广播
     */
    public void registerWXReceiver() {
        receiver = new WXShareReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_WX_CALLBACK);
        context.registerReceiver(receiver, intentFilter);
    }

    /**
     * unregister
     */
    public void unregisterWXReceiver() {
        if (null != context && null != receiver) {
            context.unregisterReceiver(receiver);
        }
    }


    public void sendTextMessage(IShareModel shareEntry, ShareListener listener) {
        this.shareListener = listener;
        String title = shareEntry.getTitle();
        String content = shareEntry.getContent();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXTextObject textObj = new WXTextObject();
        textObj.text = content;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.title = title;
        msg.description = content;

        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        iwxapi.sendReq(req);
    }

    public void sendImageMessage(IShareModel shareEntry, ShareListener listener) {
        this.shareListener = listener;
        String imgUrl = shareEntry.getImgUrl();
        final int drawableId = shareEntry.getDrawableId();
        final WXMediaMessage msg = new WXMediaMessage();
        new BitmapAsyncTask(context, imgUrl, new BitmapAsyncTask.OnBitmapListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                WXImageObject imgObj = new WXImageObject(bitmap);
                msg.mediaObject = imgObj;
                msg.setThumbImage(getWxShareBitmap(bitmap));
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("img");
                req.message = msg;
                req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                iwxapi.sendReq(req);
            }

            @Override
            public void onException(Exception exception) {

                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), drawableId);
                WXImageObject imgObj = new WXImageObject(bmp);
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                bmp.recycle();
                msg.thumbData = bmpToByteArray(thumbBmp, true);
                msg.mediaObject = imgObj;
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("img");
                req.message = msg;
                req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                iwxapi.sendReq(req);
            }
        }).execute();
    }


    public void sendWebShareMessage(IShareModel share, ShareListener listener) {
        this.shareListener = listener;
        final String title = share.getTitle();
        final String content = share.getContent();
        final String imgUrl = share.getImgUrl();
        final String actionUrl = share.getActionUrl();
        final int drawableId = share.getDrawableId();
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        if (TextUtils.isEmpty(imgUrl)) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), drawableId);
            WXMediaMessage wxMediaMessage = new WXMediaMessage();
            wxMediaMessage.title = title;
            wxMediaMessage.description = content;
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            bmp.recycle();
            wxMediaMessage.thumbData = bmpToByteArray(thumbBmp, true);
            wxMediaMessage.mediaObject = new WXWebpageObject(actionUrl);
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = wxMediaMessage;
            req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            iwxapi.sendReq(req);
        } else {
            new BitmapAsyncTask(context, imgUrl, new BitmapAsyncTask.OnBitmapListener() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    WXMediaMessage wxMediaMessage = new WXMediaMessage();
                    wxMediaMessage.title = title;
                    wxMediaMessage.description = content;
                    wxMediaMessage.setThumbImage(getWxShareBitmap(bitmap));
                    wxMediaMessage.mediaObject = new WXWebpageObject(actionUrl);
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = wxMediaMessage;
                    req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                    iwxapi.sendReq(req);
                }

                @Override
                public void onException(Exception exception) {
                    WXMediaMessage wxMediaMessage = new WXMediaMessage();
                    wxMediaMessage.title = title;
                    wxMediaMessage.description = content;
                    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), drawableId);
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                    bmp.recycle();
                    wxMediaMessage.thumbData = bmpToByteArray(thumbBmp, true);
                    wxMediaMessage.mediaObject = new WXWebpageObject(actionUrl);
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = wxMediaMessage;
                    req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                    iwxapi.sendReq(req);
                }
            }).execute();
        }
    }


    public static void sendBroadcast(Context context, int errCode) {
        Intent intent = new Intent();
        intent.setAction(ACTION_WX_CALLBACK);
        intent.putExtra(EXTRA_WX_RESULT, errCode);
        context.sendBroadcast(intent);
    }

    /**
     * 微信分享回调广播
     */
    private class WXShareReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(EXTRA_WX_RESULT)) {
                int errCode = intent.getIntExtra(EXTRA_WX_RESULT, BaseResp.ErrCode.ERR_USER_CANCEL);
                Log.w("WXShareReceiver", "errCode:" + errCode);
                switch (errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        if (shareListener != null)
                            shareListener.onShareSuccess();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        if (shareListener != null)
                            shareListener.onShareCancel();
                        break;
                    default:
                        if (shareListener != null)
                            shareListener.onShareFail();
                        break;
                }
            }
        }

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
```
##### 5：Dialog
1:DialogFragment
2:setArguments传递参数
3:recyclerview 实现 ui 显示
```
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

    public static IShareView get() {
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
```

使用可以直接下载下library
api project(':sdk-share')
可自己根据实际项目ui、逻辑调整

感谢：）
