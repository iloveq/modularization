# module-home 
## 1：工具使用
ImageLoader 使用方法：
基于Glide4.0 单独封装的 GlidleModule
```
    ImageLoader.loadImage(myViewHolder.getImage(), cardBean.getImgurl());

```

AFrame 使用方法 ：

##### 创建IApiService (因为AFrame用到retrofit，网络权限可以不添加AFrame的manifest里已经声明过了)
```
public interface IApiService {

    //欢迎页获取全局配置信息
    @POST("getCardList")
    @FormUrlEncoded
    Observable<BaseResult<CardListBean>> getCardList(@Field("name") String name, @Field("page") String page, @Field("max") String max);

}

```
##### 创建App 继承BaseApp (注意：主项目的manifest的application里android:name=".App")
```
public class App extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}

```
##### 初始化代理(在你的App里)
```
public class App extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
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
##### 网络请求部分(使用rxjava)
注: 创建apiservice是网络请求的关键，可在mvp的model层里网络请求 ：）
```
AFrameProxy.getInstance().<IApiService>createService() 

```

## 2：MVP 使用方法:
module：
```
public class MainModel implements MainContract.IMainModel {

    public static IBaseModel newInstance() {
        return new MainModel();
    }

    @Override
    public Observable<List<CardListBean.CardBean>> getCardList() {
        return AFrameProxy.getInstance().<IApiService>createService().getCardList("111", "0","0").compose(RxUtils.<CardListBean>transform()).map(new Function<CardListBean, List<CardListBean.CardBean>>() {
            @Override
            public List<CardListBean.CardBean> apply(CardListBean cardListBean) throws Exception {
                return cardListBean.getCardList();
            }
        });
    }
}

```
presenter：
```
public class MainPresenter extends BasePresenter<MainContract.IMainView, MainContract.IMainModel> implements MainContract.IMainPresenter {

    @Override
    public MainContract.IMainModel bindModel() {
        return (MainContract.IMainModel) MainModel.newInstance();
    }

    @Override
    public void getCardList() {
        checkViewAttached();
        mView.showLoading();
        mDisposable.add(mModel.getCardList().subscribe(new Consumer<List<CardListBean.CardBean>>() {
            @Override
            public void accept(List<CardListBean.CardBean> cardBeanList) throws Exception {
                mView.hideLoading();
                if (cardBeanList == null || cardBeanList.size() == 0) {
                    mView.showEmptyDataView();
                } else {
                    mView.showCardList(cardBeanList);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.onError(throwable.getMessage());
            }
        }));
    }
}
```
view:
```
public class HomeActivity extends BaseActivity implements MainContract.IMainView {

    @BindView(R2.id.rv)
    RecyclerView rv;
    MainContract.IMainPresenter presenter;
    private CardListAdapter adapter;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected int getLayoutId() {
        return R.layout.home_activity_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.addItemDecoration(new BorderDividerItemDecoration(this.getResources().getDimensionPixelOffset(R.dimen.home_border_divider_height), this.getResources().getDimensionPixelOffset(R.dimen.home_border_padding_spans)));
        adapter = new CardListAdapter();
        rv.setAdapter(adapter);
        presenter = new MainPresenter();
        presenter.onAttach(this);
        requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                presenter.getCardList();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                if (deniedPermissions.contains(permissions[0]) || deniedPermissions.contains(permissions[1]))
                    ToastUtil.showLongToast("3G下浏览图片会造成浪费流量资源");
                presenter.getCardList();
            }
        });

    }

    @Override
    protected void onResume() {
        final long time = SystemClock.uptimeMillis();
        super.onResume();
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                // on Measure() -> onDraw() 耗时
                Log.i(HomeActivity.this.getClass().getSimpleName(), "onCreate -> idle : " + (SystemClock.uptimeMillis() - time));
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public void onNetworkViewRefresh() {
        super.onNetworkViewRefresh();
        ToastUtil.showShortToast("重新请求中...");
        presenter.getCardList();
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void hideLoading() {
        showContentView();
    }

    @Override
    public void onError(String message) {
        ToastUtil.showShortToast(message);
        showErrorView();
    }

    @Override
    public void showEmptyDataView() {
        showEmptyView();
    }

    @Override
    public void showCardList(List<CardListBean.CardBean> cardBeanList) {
        if (adapter != null)
            adapter.replaceData(cardBeanList);
    }

}

```
### 3：HomeFragment
```
@Route(path = ARouterConstant.fragment_home_router_path)
public class HomeFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_home;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }
}
```
