package com.wanggang.www.ndksodemo;

import android.util.Log;

public class JNIUtils {
    static {
        //括号的参数可以任意修改
        Log.i("wanggang", "load jni");
        System.loadLibrary("jniutil");
    }

    //java调C/C++中的方法都需要用native声明且方法名必须和C/C++的方法名一样
    public native String test();
}
