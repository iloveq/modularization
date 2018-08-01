# modularization 安卓组件化demo
可根据自己需求修改模块,感谢star issues follow ：)

### 项目简介

使用 ARouter(阿里路由)，ButterKnife(依赖注入框架)，AFrame(rxjava、okhttp3、base封装、常用util)

### 组件化控制

##### project/gradle.properties:

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

project / build.gradle :
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

##### 2：library-common

##### 3：[module-main](https://github.com/woaigmz/modularization/blob/master/module-main/README.md)

##### 4：module-home

##### 5：module-project

##### 6：module-mine

##### 7：module-login

##### 8：module-register

##### 9：module-web

##### 10：module-share
