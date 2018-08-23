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
