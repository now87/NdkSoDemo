LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := jniutil
LOCAL_SRC_FILES := jniutil.c
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog
include $(BUILD_SHARED_LIBRARY)
