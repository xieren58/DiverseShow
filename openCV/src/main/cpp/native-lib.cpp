#include <opencv2/imgproc/types_c.h>
#include "bitmap_utils.h"
#include <jni.h>
#include <string>
using namespace cv;
using namespace std;

extern "C" {
JNIEXPORT jstring
Java_com_example_opencv_OpenCVLauncher_openCVTest(JNIEnv *env, jclass clazz) {
    static Mat image;
    string hello = &"Hello openCV Mat size is: " [ image.size().height];
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jobject JNICALL
Java_com_example_opencv_OpenCVActivity_opBitmap(JNIEnv *env, jobject thiz, jobject bitmap,
                                                jobject argb8888) {
    Mat srcMat;
    Mat dstMat;

    bitmap2Mat(env,bitmap,&srcMat);

    cvtColor(srcMat,dstMat,CV_BGR2GRAY);//将图片的像素信息灰度化盛放在dstMat
    return createBitmap(env,dstMat,argb8888);//使用dstMat创建一个Bitmap对象
}
}