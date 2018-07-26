# modularization 安卓组件化demo
可根据自己需求修改模块,感谢star issues follow ：)

### 项目简介

使用 ARouter(阿里路由)，ButterKnife(依赖注入框架)，AFrame(rxjava、okhttp3、base封装、常用util)

### 组件化控制
project/gradle.properties:

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

```
buildscript {

    ext {
    
        var = [
                compileSdkVersion: 26,
                buildToolsVersion: "26.0.2",
                ...
        ]

        versions = [
                'supportLibrary': '27.0.2',
                 ...
        ]
    }

    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.1'
        classpath 'com.jakewharton:butterknife-gradle-plugin:8.5.1'
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

app / build.gradle :

```
apply plugin: 'com.android.application'


android {
    compileSdkVersion var.compileSdkVersion
    buildToolsVersion var.buildToolsVersion

    defaultConfig {
        applicationId "com.credithc.hhr"
        minSdkVersion var.minSdkVersion
        targetSdkVersion var.targetSdkVersion
        versionCode var.versionCode
        versionName var.versionName
    }


    buildTypes {
        debug {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }

        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

    resourcePrefix "app_"
}

dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'
    implementation project(':library-common')
    if (isBuildAll.toBoolean()) {
        implementation project(':module-main')
        implementation project(':module-home')
        implementation project(':module-login')
        implementation project(':module-mine')
        implementation project(':module-project')
        implementation project(':module-register')
        ...
    }

}

```

module - * / build.gradle :

```
if(isBuildAll.toBoolean()||isLibraryLogin.toBoolean()){
    apply plugin: 'com.android.library'
}else {
    apply plugin: 'com.android.application'
}

apply plugin:'com.jakewharton.butterknife'

android {

    compileSdkVersion var.compileSdkVersion
    buildToolsVersion var.buildToolsVersion

    defaultConfig {
        minSdkVersion var.minSdkVersion
        targetSdkVersion var.targetSdkVersion
        versionCode var.versionCode
        versionName var.versionName
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [ moduleName : project.getName() ]
            }
        }
    }


    sourceSets {
        main {
            if (isBuildAll.toBoolean() || isLibraryLogin.toBoolean()) {
                manifest.srcFile 'src/main/AndroidManifest.xml'

                //We'll not compile the debug folder when building all.
                java {
                    exclude 'module/**'
                }
            } else {
                manifest.srcFile 'src/main/java/module/AndroidManifest.xml'
            }
        }
    }
    //根据 module - * 修改 "*_"
    resourcePrefix "login_"
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'
    annotationProcessor "com.alibaba:arouter-compiler:1.1.4"
    implementation project(':library-common')
}
```


### 模块介绍

##### 1：app

##### 2：library-common

##### 3：module-main

##### 4：module-home

##### 5：module-project

##### 6：module-mine

##### 7：module-login

##### 8：module-register

##### 9：module-web

##### 10：module-share
