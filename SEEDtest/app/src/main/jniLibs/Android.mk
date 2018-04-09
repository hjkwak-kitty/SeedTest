LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := KiSACrypto
LOCAL_SRC_FILES := armeabi/libKiSACrypto.so
LOCAL_LDLIBS := -llog
LOCAL_CFLAGS := -fpic
include $(PREBUILT_SHARED_LIBRARY)