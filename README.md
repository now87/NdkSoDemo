# NdkSoDemo
写一个简单的JNI程序

一、写在前面的话

1、AndroidStudio环境：
```Java
Android Studio 3.1.4
Build #AI-173.4907809, built on July 24, 2018
JRE: 1.8.0_152-release-1024-b01 x86_64
JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
Mac OS X 10.13.6
```

2、下载并配置好NDK
<br>（1）下载NDK：
<br>进入AndroidStudio的Preference的Android SDK的SDK tools，
<br>勾选并下载CMake、LLDB、NDK。
<br>（2）在local.properties文件配置：
<br>ndk.dir=/Users/wanggang/Library/Android/sdk/ndk-bundle
<br>sdk.dir=/Users/wanggang/Library/Android/sdk

二、开始写一个简单的JNI程序

1、新建一个类JNIUtils
```Java
public class JNIUtils {
    static {
        //括号的参数可以任意修改
        System.loadLibrary("jniutil");
    }

    //java调C/C++中的方法都需要用native声明且方法名必须和C/C++的方法名一样
    public native String test();
}
```

2、在主module的build.gradle文件里defaultConfig中配置ndk。
```Java
    defaultConfig {
        //...

        ndk {
            moduleName "jniutil"//编译出so的名字
            abiFilters 'x86', 'armeabi-v7a', 'x86_64' //输出指定abi体系结构下的so库。
        }
    }
```

3、针对步骤1中的类JNIUtils，我们再执行Build-Make Project命令，完成后便生成字节码文件JNIUtils.class。
<br>在NdkSoDemo/app/build/intermediates/classes/debug/com/wanggang/www/ndksodemo/JNIUtils.class中。

4、根据JNIUtils.class生成.h文件
<br>（1）方法一
<br>在AndroidStudio的terminal中执行：cd app/src/main/，
<br>然后执行javah -d jni -classpath 编译后的class文件的绝对路径：
<br>javah -d jni -classpath /Users/wanggang/now/code/eclipse-workspace/NdkSoDemo/app/build/intermediates/classes/debug com.wanggang.www.ndksodemo.JNIUtils
<br>结果在app/src/main/jni/目录下生成.h文件：
<br>com_wanggang_www_ndksodemo_JNIUtils.h
<br>（2）方法二
<br>在AndroidStudio的terminal中执行：cd app/build/intermediates/classes/debug/，
<br>然后执行javah com.wanggang.www.ndksodemo.JNIUtils，
<br>结果在app/build/intermediates/classes/debug/目录下生成.h文件：
<br>com_wanggang_www_ndksodemo_JNIUtils.h
<br>最后将该.h文件copy到app/src/main/jni/目录下。

5、编写.c文件（jniutil.c）
<br>在app/src/main/jni/目录下创建jniutil.c文件，并实现其方法：
```Java
#include "com_wanggang_www_ndksodemo_JNIUtils.h"

JNIEXPORT jstring JNICALL Java_com_wanggang_www_ndksodemo_JNIUtils_test
        (JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "jni调用成功");
}
```

6、在MainActivity中调用
```Java
public class MainActivity extends AppCompatActivity {

    private TextView ndk_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ndk_txt = findViewById(R.id.ndk_txt);
        ndk_txt.setText(new JNIUtils().test());
    }
}
```

7、创建编译文件Android.mk
```Java
在app/src/main/jni/目录下创建Android.mk文件：
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := jniutil
LOCAL_SRC_FILES := jniutil.c
include $(BUILD_SHARED_LIBRARY)
```

8、在主module的build.gradle文件里android中配置ndkBuild路径：
```Java
    externalNativeBuild{
        ndkBuild{
            path file("src/main/jni/Android.mk")
        }
    }
```

三、如何在AndroidStudio中输出JNI log？

1、在app/build.gradle中添加ldLibs "log"
```Java
android {
    defaultConfig {
        ndk {
            ldLibs "log"
            moduleName "jniutil"//编译出so的名字
            abiFilters 'x86', 'armeabi-v7a', 'x86_64' //输出指定abi体系结构下的so库。
        }
    }
}
```
2、在Android.mk中添加：
```Java
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog
```
3、添加头文件iqiyi_child_log.h
```Java
//
// Created by wanggang on 2018/9/8.
//

#ifndef NDKSODEMO_IQIYI_CHILD_LOG_H
#define NDKSODEMO_IQIYI_CHILD_LOG_H
#ifdef __cplusplus
extern "C" {
#endif
#include <android/log.h>
// 宏定义类似java 层的定义,不同级别的Log LOGI, LOGD, LOGW, LOGE, LOGF。 对就Java中的 Log.i log.d
#define LOG_TAG    "JNILOG_WANGGANG" // 这个是自定义的LOG的标识
//#undef LOG // 取消默认的LOG
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG, __VA_ARGS__)
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG, __VA_ARGS__)
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG_TAG, __VA_ARGS__)

#ifdef __cplusplus
}
#endif
#endif //NDKSODEMO_IQIYI_CHILD_LOG_H
```
4、使用JNI LOG
```Java
#include "com_wanggang_www_ndksodemo_JNIUtils.h"
#include "iqiyi_child_log.h"

JNIEXPORT jstring JNICALL Java_com_wanggang_www_ndksodemo_JNIUtils_test
        (JNIEnv *env, jobject obj) {
    LOGI("jni log");
    return (*env)->NewStringUTF(env, "jni demo --- invoke success");
}
```
![image](https://github.com/ican87/NdkSoDemo/blob/master/1.jpg)

