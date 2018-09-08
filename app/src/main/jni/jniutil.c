//
// Created by wanggang on 2018/8/23.
//

#include "com_wanggang_www_ndksodemo_JNIUtils.h"
#include "iqiyi_child_log.h"

JNIEXPORT jstring JNICALL Java_com_wanggang_www_ndksodemo_JNIUtils_test
        (JNIEnv *env, jobject obj) {
    LOGI("jni log");
    return (*env)->NewStringUTF(env, "jni demo --- invoke success");
}