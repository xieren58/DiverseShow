#include "include/opencv2/opencv.hpp"

#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring
Java_com_example_opencv_OpenCVLauncher_openCVTest(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}