# modularization 安卓组件化demo
可根据自己需求修改模块,感谢star issues follow ：)

### 项目简介

使用 ARouter(阿里路由)，ButterKnife(依赖注入框架)，AFrame(rxjava、okhttp3、base封装、常用util)

### 组件化控制

##### project/gradle.properties:
组件化开关 toggle

```
isBuildAll=true

isLibraryMain=true

isLibraryHome=true
isLibraryLogin=true
isLibraryMine=true
isLibraryProject=true
isLibraryRegister=true
isLibraryWeb=true
```

##### project / build.gradle :
版本,签名,依赖
```
buildscript {

    ext {

        signigConfig = [
                storePassword: 'xxx',
                keyAlias     : 'xxx',
                keyPassword  : 'xxx'
        ]

        var = [
                gradle              : "3.0.1",
                compileSdkVersion   : 26,
                buildToolsVersion   : "26.0.2",
                minSdkVersion       : 15,
                targetSdkVersion    : 26,
                versionCode         : 1,
                versionName         : "1.0"

        ]

        versions = [

                'support_library'   : '26.1.0',
                'diff_adapter'      : '0.2.1',
                'refresh_layout'    : '1.0.5.1',
                'refresh_header'    : '1.0.3',
                'aframe'            : '0.0.8',
                'leakcanary'        : '1.5.4',
                'butterknife'       : '8.5.1',
                'dex'               : '1.0.3',
                'arouter_api'       : '1.3.1',
                'arouter_compiler'  : '1.1.4'
        ]
    }

    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath "com.android.tools.build:gradle:${var.gradle}"
        classpath "com.jakewharton:butterknife-gradle-plugin:${versions.butterknife}"
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```



### 模块介绍

##### 1：[app](https://github.com/woaigmz/modularization/blob/master/app/README.md)

```
1：空壳app，编译项目

2：AndroidManifest 负责项目主进程等配置

3：application 初始化，加载配置
```

##### 2：[library-common](https://github.com/woaigmz/modularization/blob/master/library-common/README.md)

```
中间件

1：作为公共library ，其他 module 根据情况依赖
2：作为 common ，封装一些公共的行为和资源供 module 使用:
        bean   目录 网络请求/操作数据库的公共容器
        config 目录 版本/服务器/路由 配置信息
        utils  目录 常用工具类
        widget 目录 自定义控件，大部分模块在使用
        base   中间层
        interface
           根据项目需求自行扩展 ...

```

##### 3：[module-main](https://github.com/woaigmz/modularization/blob/master/module-main/README.md)

```
1：组件入口  SplashActivity  MainActivity 
2：通过 ARouter 初始化 MainActivity 里的 3 个 模块的 Fragment (职责分离)(反射 newInstance)
3：UI 上 使用 TabView ( 通过扩展 TextView 替代 TextView+ImageView) 将 NavigationBottomView 的 布局层次降低，减少 GPU 过度绘制
4：配置 gradle 和 MainApp 配置 使模块可单独运行(单独运行时通过依赖 module-home/module-project/module-mine 和 ARouter)
```

##### 4：[module-home]()

```
1：通过初始化 AFrame 使用  Retrofit OKhttp3 Glide 等框架 和 MVP/base/utils/network-state-view/premission 等工具
```

##### 5：module-project

##### 6：module-mine

##### 7：module-login

##### 8：module-register

##### 9：module-web

##### 10：module-share
