#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_catherine_materialdesignapp_utils_CBridge_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jint JNICALL
Java_com_catherine_materialdesignapp_utils_CBridge_plus(
        JNIEnv *env,
        jobject /* this */,
        jint x,
        jint y) {
    return (x + y);
}