# Sdk-Share
### init

```
ShareSdkProxy.getInstance().init(this,new String[]{"100490958","wx9de3fd98743275f6","378367946"}, FileHelper.get().getCache().getPath());

```

### createDialog

```
IShareView shareDialog = ShareSdkProxy.getInstance().createShareDialog(new int[]{ShareChannel.CHANNEL_QQ, ShareChannel.CHANNEL_WEIBO,ShareChannel.CHANNEL_WECHAT_MOMENT,ShareChannel.CHANNEL_MORE});
ShareSdkProxy.getInstance().setOnShareClickListener(shareDialog);
shareDialog.show(getFragmentManager());
```
