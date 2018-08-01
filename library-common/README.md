# library-common 中间件

1：作为公共library ，其他 module 都会根据情况依赖

```
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])

    annotationProcessor "com.jakewharton:butterknife-compiler:${versions.butterknife}"

    api "com.android.support:appcompat-v7:${versions.support_library}"
    api "com.android.support:recyclerview-v7:${versions.support_library}"
    api "com.android.support:cardview-v7:${versions.support_library}"

    api "com.github.woaigmz:SmartDiffAdapter:${versions.diff_adapter}"

    api "com.scwang.smartrefresh:SmartRefreshLayout:${versions.refresh_layout}"
    api "com.scwang.smartrefresh:SmartRefreshHeader:${versions.refresh_header}"
    //Aframe 含rxjava retrofit okhttp3 等 https://github.com/woaigmz/AFrame
    api "com.github.woaigmz:AFrame:${versions.aframe}"

    api "com.alibaba:arouter-api:${versions.arouter_api}"

    api "com.android.support:multidex:${versions.dex}"

}
```

2：作为 common ，封装一些公共的行为和资源供 module 使用

```
bean   目录 网络请求/操作数据库的公共容器
config 目录 版本/服务器/路由 配置信息
utils  目录 常用工具类
widget 目录 自定义控件，大部分模块在使用
base   中间层
interface

   根据项目需求自行扩展 ...

```